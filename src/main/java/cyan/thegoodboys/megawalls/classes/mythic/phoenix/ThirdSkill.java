/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.mythic.phoenix;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public static List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();

    public ThirdSkill(Classes classes) {
        super("凤凰涅槃", classes);
    }

    public static void use(final GamePlayer gamePlayer) {
        if (!gamePlayers.contains(gamePlayer)) {
            Phoenix.skill3.add(gamePlayer);
            final Player player = gamePlayer.getPlayer();
            PlayerUtils.heal(player, player.getMaxHealth());
            gamePlayers.add(gamePlayer);
            final ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setGravity(false);
            as.setPassenger(player);
            ParticleEffect.FLAME.display(2.0f, 2.0f, 2.0f, 0.0f, 5, player.getLocation().clone().add(0.0, 1.0, 0.0), 10.0);
            player.getWorld().createExplosion(player.getLocation().add(0, 1, 0), 4);
            as.setVelocity(new Vector(0.0, 0.15, 0.0)); // Adjusted velocity for 3 blocks upward movement
            gamePlayer.sendTitle("§c凤凰涅槃", "§c", 0, 200, 0);
            new BukkitRunnable() {
                int ticks = 0;

                public void run() {
                    if (this.ticks >= 60) {
                        as.setPassenger(null);
                        as.remove();
                        Phoenix.skill3.remove(gamePlayer);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
                        this.cancel();
                        return;
                    }
                    player.setNoDamageTicks(120); // Refresh no damage ticks every tick
                    as.teleport(as.getLocation().add(0.0, 0.05, 0.0)); // Adjusted teleportation for 3 blocks upward movement in 60 ticks
                    ++this.ticks;
                }
            }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        }
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.6;
            }
            case 2: {
                return 0.8;
            }
            case 3: {
                return 1.0;
            }
        }
        return 0.6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7受到致命伤害时,将会暂时石化并同时恢复§a" + StringUtils.percent(this.getAttribute(level)) + "§7的生命值");
            lore.add(" §8▪ §7在涅槃结束时你获得虚弱IV、速度II和抗性提升II,持续10秒");
            lore.add(" §8▪ §7每局游戏你只能涅槃一次");
            lore.add(" §8▪ §7该效果在凋零死亡后生效");
            lore.add("   §7凤凰涅槃对你的契约队友也生效一次,恢复其§a" + StringUtils.percent(this.getAttribute(level) / 2.0) + "§7的生命值");
            return lore;
        }
        lore.add(" §8▪ §7受到致命伤害时,将会暂时石化并同时恢复§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ §a" + StringUtils.percent(this.getAttribute(level)) + "§7的生命值");
        lore.add("   §7凤凰涅槃对你的契约队友也生效一次,恢复其§8" + StringUtils.percent(this.getAttribute(level - 1) / 2.0) + " ➜ §a" + StringUtils.percent(this.getAttribute(level) / 2.0) + "§7的生命值");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill3Level();
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (!gamePlayers.contains(gamePlayer) ? "§a§l✓" : "§c§l✕");
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill3Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        ThirdSkill.use(gamePlayer);
        return true;
    }
}

