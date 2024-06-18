/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLightningStrike;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WitherSpecialAttackTimer extends BukkitRunnable {
    private final Game game;
    private int state = 0;
    private int counter = 0;

    public WitherSpecialAttackTimer(Game game) {
        this.game = game;
    }

    public void run() {
        if (!this.game.isWitherAngry()) {
            return;
        }
        counter++;
        if (counter >= 6) {
            counter = 0;
            state = (state + 1) % 5;
        }
        for (GameTeam gameTeam : this.game.getTeams()) {
            if (gameTeam.isWitherDead() || !this.game.isWitherAngry()) continue;
            for (Player other : PlayerUtils.getNearbyPlayers(gameTeam.getWitherLocation(), 25)) {
                GamePlayer gamePlayer = GamePlayer.get(other.getUniqueId());
                if (gamePlayer != null && (gamePlayer.isSpectator() || gameTeam.isInTeam(gamePlayer))) continue;
                if (counter == 3) {
                    if (gamePlayer != null) {
                        switch (state) {
                            case 0:
                                gamePlayer.sendMessage("§c" + gameTeam.getTeamColor().getText() + "队凋灵正在蓄力发动冲击波！");
                                break;
                            case 1:
                                gamePlayer.sendMessage("§c" + gameTeam.getTeamColor().getText() + "队凋灵正在蓄力击退！");
                                break;
                            case 2:
                                gamePlayer.sendMessage("§c" + gameTeam.getTeamColor().getText() + "队凋灵正在准备它的炼狱技能！");
                                break;
                            case 3:
                                gamePlayer.sendMessage("§c" + gameTeam.getTeamColor().getText() + "队凋灵正在准备它的闪电之击！");
                                break;
                            case 4:
                                gamePlayer.sendMessage("§c" + gameTeam.getTeamColor().getText() + "队凋灵正在准备它的地震技能！");
                                break;
                        }
                    }
                } else if (counter == 5){
                    switch (state) {
                        case 0:
                            other.setVelocity(new Vector(0.0, 1.0, 0.0));
                            break;
                        case 1:
                            Vector vector = other.getLocation().getDirection();
                            other.setVelocity(new Vector(-0.5 * vector.getX(), 0.0, -0.5 * vector.getZ()));
                            break;
                        case 2:
                            World world = gameTeam.getTeamWither().getWorld().getWorld();
                            LightningStrike ls = null;
                            if (gamePlayer != null) {
                                ls = world.strikeLightning(gamePlayer.getPlayer().getLocation());
                            }
                            CraftLightningStrike cls = (CraftLightningStrike) ls;
                            EntityLightning entityLightning = cls.getHandle();
                            entityLightning.isSilent = false;
                            entityLightning.fireTicks = 10;
                            gamePlayer.getPlayer().setFireTicks(10 * 20);
                            break;
                        case 3:
                            World world2 = gameTeam.getTeamWither().getWorld().getWorld();
                            LightningStrike ls2 = null;
                            if (gamePlayer != null) {
                                ls2 = world2.strikeLightning(gamePlayer.getPlayer().getLocation());
                            }
                            CraftLightningStrike cls2 = (CraftLightningStrike) ls2;
                            EntityLightning entityLightning2 = cls2.getHandle();
                            entityLightning2.isSilent = false;
                            gamePlayer.getPlayer().damage(20.0);
                            break;
                        case 4:
                            if (gamePlayer != null) {
                                gamePlayer.getPlayer().damage(3.0);
                                gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                            }
                            ParticleUtils.play(EnumParticle.EXPLOSION_LARGE, gameTeam.getTeamWither().getBukkitEntity().getLocation(), 0.1, 0.1, 0.1, 0, 4);
                            break;
                    }
                }
            }
        }
    }
}

