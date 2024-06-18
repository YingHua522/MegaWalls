/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.assassin;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("暗影披风", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 7.2;
            }
            case 2: {
                return 8.4;
            }
            case 3: {
                return 9.6;
            }
            case 4: {
                return 10.8;
            }
            case 5: {
                return 13.0;
            }
        }
        return 3.2;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f97\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6 II \u6548\u679c\u3002");
            lore.add("   \u00a77\u5728\u8fd9\u671f\u95f4,\u4f60\u4f1a\u6062\u590d\u4f60\u9020\u6210\u4f24\u5bb3\u768430%");
            lore.add("   \u00a77\u5e76\u4e14\u653b\u51fb\u654c\u4eba\u65f6\u9020\u62102\u70b9\u771f\u5b9e\u4f24\u5bb3");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f97\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6 II \u6548\u679c\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkillLevel();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkillLevel();
    }

    @Override
    public boolean use(final GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        Game game = MegaWalls.getInstance().getGame();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10 * 20, 0));
        player.getWorld().playSound(player.getLocation(), Sound.WITHER_IDLE, 1.0f, 3f);
        Assassin.skill.add(gamePlayer);
        Assassin.skillTimer.put(gamePlayer, 10);
        Assassin.AssassinNameTag.put(gamePlayer, true);
        Assassin.hit.clear();
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), game::registerScoreboardTeams, 30L);

        new BukkitRunnable() {
            public void run() {
                if (Assassin.skillTimer.getOrDefault(gamePlayer, 0) == 0) {
                    Assassin.skill.remove(gamePlayer);
                    Assassin.AssassinNameTag.remove(gamePlayer);
                    Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), game::registerScoreboardTeams, 30L);
                    player.getWorld().playSound(player.getLocation(), Sound.BURP, 0.2f, 1.0f);
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 20L);
        return true;
    }
}

