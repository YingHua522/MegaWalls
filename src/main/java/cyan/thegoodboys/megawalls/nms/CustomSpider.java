/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.EntitySpider
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Spider
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

public class CustomSpider extends EntitySpider implements CustomEntity, Runnable {
    private GamePlayer gamePlayer;
    private int taskId = Bukkit.getScheduler().runTaskTimer((Plugin) MegaWalls.getInstance(), (Runnable) this, 0L, 5L).getTaskId();

    public CustomSpider(World world) {
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
        ((Spider) this.getBukkitEntity()).setCanPickupItems(false);
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
    }

    protected void dropDeathLoot(boolean flag, int i) {
    }

    @Override
    public void run() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0)) {
            GamePlayer gamePlayer;
            if (player == null || (gamePlayer = GamePlayer.get(player.getUniqueId())) == null || gamePlayer.isSpectator() || this.getGamePlayer().getGameTeam().isInTeam(gamePlayer))
                continue;
            this.setGoalTarget((EntityLiving) ((CraftPlayer) player).getHandle());
        }
    }

    public void die() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        super.die();
    }
}

