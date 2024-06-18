/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityBat
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.Item
 *  net.minecraft.server.v1_8_R3.PathEntity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

public class CustomBat extends EntityBat implements CustomEntity, Runnable {
    private GamePlayer gamePlayer;
    private int taskId = Bukkit.getScheduler().runTaskTimer((Plugin) MegaWalls.getInstance(), (Runnable) this, 0L, 5L).getTaskId();

    public CustomBat(World world) {
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

    public void die() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        super.die();
    }

    @Override
    public void run() {
        if (this.getBukkitEntity().getPassenger() != null) {
            Player player = (Player) this.getBukkitEntity().getPassenger();
            if (player.getInventory().getItemInHand().getType() == Material.BOW || player.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD || player.getInventory().getItemInHand().getType() == Material.IRON_SWORD) {
                this.getBukkitEntity().setVelocity(player.getLocation().getDirection().multiply(0.6));
            }
        } else {
            this.die();
        }
    }


    protected Item getLoot() {
        return null;
    }
}

