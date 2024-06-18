/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.EntitySheep
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Sheep
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityTargetEvent;

public class BoomSheep extends EntitySheep implements CustomEntity, Runnable {
    private GamePlayer gamePlayer;
    private DyeColor currentColor = DyeColor.YELLOW; // 初始化颜色为黄色
    private boolean initialColorSet = false;

    public BoomSheep(World world) {
        super(world);
        ((Sheep) this.getBukkitEntity()).setColor(DyeColor.YELLOW);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), this, 60L);
    }

    @Override
    public GamePlayer getGamePlayer() {
        if (this.gamePlayer != null) {
            return this.gamePlayer;
        }
        String name = ChatColor.stripColor(this.getCustomName());
        Player player = Bukkit.getPlayer(name);
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
        ((Sheep) this.getBukkitEntity()).setCanPickupItems(false);
    }

    public void changeColor() {
        Sheep sheep = (Sheep) this.getBukkitEntity();
        switch (currentColor) {
            case YELLOW:
                currentColor = DyeColor.ORANGE;
                break;
            case ORANGE:
                currentColor = DyeColor.RED;
                break;
            case RED:
                currentColor = DyeColor.YELLOW;
                break;
        }
        sheep.setColor(currentColor);
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
        if (!this.getGamePlayer().isOnline() || !this.isAlive()) {
            return;
        }
        if (!initialColorSet) {
            ((Sheep) this.getBukkitEntity()).setColor(currentColor);
            initialColorSet = true;
        }
        // 在爆炸之前改变羊的颜色
        this.changeColor();
        ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, this.getBukkitEntity().getLocation(), 30.0);
        this.getBukkitEntity().getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.EXPLODE, 1.0f, 0.0f);
        for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 5.0)) {
            GamePlayer gamePlayer;
            if (player == null || (gamePlayer = GamePlayer.get(player.getUniqueId())) == null || gamePlayer.isSpectator() || this.getGamePlayer().getGameTeam().isInTeam(gamePlayer))
                continue;
            player.damage(4.0, this.getGamePlayer().getPlayer());
        }
        this.die();
    }
}

