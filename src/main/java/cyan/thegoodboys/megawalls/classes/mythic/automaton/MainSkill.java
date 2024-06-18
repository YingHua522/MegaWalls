/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.mythic.automaton;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainSkill extends Skill {
    public static final Random random = new Random(System.nanoTime());

    public MainSkill(Classes classes) {
        super("EMP", classes);
    }

    public static void createEMP(Player player) {
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        new BukkitRunnable() {
            int seconds = 0;
            public void run() {
                if (gamePlayer != null && (this.seconds == 6 || !gamePlayer.isOnline())) {
                    this.cancel();
                    return;
                }
                for (Player other : getNearbyPlayers(player.getLocation(), player, 6)) {
                    other.playSound(player.getLocation(), Sound.IRONGOLEM_THROW, 1, 1);
                    GamePlayer otherGamePlayer = GamePlayer.get(other.getUniqueId());
                    if (gamePlayer != null && otherGamePlayer != null && otherGamePlayer.getGameTeam().isInTeam(gamePlayer)) {
                        Classes classes = ClassesManager.getSelected(gamePlayer);
                        if (!(classes instanceof Automaton)) {
                            if (seconds % 2 == 0) {
                                otherGamePlayer.addEnergy(6, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                            }
                        }
                    } else {
                        if (seconds % 2 == 1) {
                            other.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 2));
                            PlayerUtils.realDamage(other, player, 1);
                            if (otherGamePlayer != null) {
                                otherGamePlayer.setEnergy(otherGamePlayer.getEnergy() - 8);
                            }
                        }
                    }
                }
                ++this.seconds;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 40L);
    }

    private static List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || other.getLocation().distance(location) > (double) radius) continue;
            players.add(other);
        }
        return players;
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 5.0;
            }
            case 3: {
                return 6.0;
            }
            case 4: {
                return 7.0;
            }
            case 5: {
                return 8.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f975\u79d2\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
            lore.add("   \u00a77\u5468\u56f4\u7684\u73a9\u5bb6\u5c06\u53d7\u5230");
            lore.add("   \u00a77\u706b\u7130\u65cb\u6da1\u7684\u5f71\u54cd,\u6301\u7eed5\u79d2");
            lore.add("   \u00a77\u9020\u6210\u6700\u591a\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f975\u79d2\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
        lore.add("   \u00a77\u5468\u56f4\u7684\u73a9\u5bb6\u5c06\u53d7\u5230");
        lore.add("   \u00a77\u706b\u7130\u65cb\u6da1\u7684\u5f71\u54cd,\u6301\u7eed5\u79d2");
        lore.add("   \u00a77\u9020\u6210\u6700\u591a\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
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
        createEMP(player);
        return true;
    }
}

