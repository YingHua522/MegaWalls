/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityBlaze
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.Item
 *  net.minecraft.server.v1_8_R3.PathEntity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.normal.blaze.SecondSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CustomBlaze extends EntityBlaze implements CustomEntity, Runnable {
    private GamePlayer gamePlayer;
    private int taskId = Bukkit.getScheduler().runTaskTimer((Plugin) MegaWalls.getInstance(), (Runnable) this, 0L, 5L).getTaskId();

    public CustomBlaze(World world) {
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
        this.setCustomNameVisible(true);
    }

    @Override
    protected void dropDeathLoot(boolean b, int i) {
        //
    }

    public void setGoalTarget(EntityLiving entityliving) {
        this.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, false);
    }

    public void setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fire) {
        if (!(entityliving instanceof EntityPlayer)) {
            return;
        }
        GamePlayer gamePlayer = this.getGamePlayer();
        this.follow(gamePlayer.getPlayer());
        if (gamePlayer.getGameTeam() != null && gamePlayer.getGameTeam().isInTeam(GamePlayer.get(((EntityPlayer) entityliving).getBukkitEntity().getUniqueId()))) {
            return;
        }
        super.setGoalTarget(entityliving, reason, fire);
    }

    public void die() {
        List list = SecondSkill.blazes.getOrDefault(this.gamePlayer, new ArrayList());
        list.remove(this);
        SecondSkill.blazes.put(this.gamePlayer, list);
        Bukkit.getScheduler().cancelTask(this.taskId);
        super.die();
    }

    public void follow(Player player) {
        if (player == null) {
            return;
        }
        this.getNavigation().a(2.0f);
        Location targetLocation = player.getLocation();
        PathEntity path = this.getNavigation().a(targetLocation.getX() + 1.0, targetLocation.getY(), targetLocation.getZ() + 1.0);
        try {
            int distance = (int) Bukkit.getPlayer((String) player.getName()).getLocation().distance(this.getBukkitEntity().getLocation());
            if (distance > 10 && this.valid && player.isOnGround()) {
                this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0f, 0.0f);
            }
            if (path != null && (double) distance > 3.3) {
                double speed = 1.05;
                this.getNavigation().a(path, speed);
                this.getNavigation().a(speed);
            }
        } catch (IllegalArgumentException e) {
            this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0f, 0.0f);
        }
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

    protected Item getLoot() {
        return null;
    }
}

