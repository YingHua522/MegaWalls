/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.EntityWolf
 *  net.minecraft.server.v1_8_R3.PathEntity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Wolf
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.EntityTargetEvent;

public class SnowmanFriend extends EntitySnowman implements CustomEntity, Runnable {
    private final int taskId = Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), this, 0L, 5L).getTaskId();
    private GamePlayer gamePlayer;

    public SnowmanFriend(World world) {
        super(world);
    }

    @Override
    public GamePlayer getGamePlayer() {
        if (this.gamePlayer != null) {
            return this.gamePlayer;
        }
        String name = ChatColor.stripColor((String) this.getCustomName());
        Player player = Bukkit.getPlayer((String) name);
        if (player == null) {
            return null;
        }
        this.gamePlayer = GamePlayer.get(player.getUniqueId());
        return this.gamePlayer;
    }

    @Override
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.setCustomName(gamePlayer.getDisplayName(null));
        ((Snowman) this.getBukkitEntity()).setCanPickupItems(false);
        ((Snowman) this.getBukkitEntity()).setMaxHealth(14.0);
        ((Snowman) this.getBukkitEntity()).setHealth(14.0);
    }

    public void setGoalTarget(EntityLiving entityliving) {
        this.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, false);
    }

    public void setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fire) {
        if (!(entityliving instanceof EntityPlayer)) {
            return;
        }
        GamePlayer gamePlayer = this.getGamePlayer();
        if (gamePlayer.getGameTeam() != null && gamePlayer.getGameTeam().isInTeam(GamePlayer.get(((EntityPlayer) entityliving).getBukkitEntity().getUniqueId()))) {
            return;
        }
        super.setGoalTarget(entityliving, reason, fire);
        this.follow(gamePlayer.getPlayer());
    }

    @Override
    public void move(double d0, double d1, double d2) {
        this.follow(gamePlayer.getPlayer());
        super.move(d0, d1, d2);
    }

    public void follow(Player player) {
        if (player == null) {
            return;
        }
        this.getNavigation().a(2.0f);
        Location targetLocation = player.getLocation();
        try {
            int distance = (int) Bukkit.getPlayer(player.getName()).getLocation().distance(this.getBukkitEntity().getLocation());
            if (distance > 10 && this.valid && player.isOnGround()) {
                this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0f, 0.0f);
            }

        } catch (IllegalArgumentException e) {
            this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0f, 0.0f);
        }
    }

    public void die() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        super.die();
    }

    @Override
    public void run() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0)) {
            GamePlayer gamePlayer;
            if (player == null || (gamePlayer = GamePlayer.get(player.getUniqueId())) == null || gamePlayer.isSpectator() || this.getGamePlayer().getGameTeam().isInTeam(gamePlayer))
                continue;
            this.setGoalTarget(((CraftPlayer) player).getHandle());
        }
    }
}

