/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.creeper;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("引爆", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 6.0;
            }
            case 2: {
                return 7.0;
            }
            case 3: {
                return 8.0;
            }
            case 4: {
                return 9.0;
            }
            case 5: {
                return 10.0;
            }
        }
        return 6.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f15\u53d1\u4e00\u573a\u7206\u70b8,");
            lore.add("   \u00a77\u5bf9\u5468\u56f4\u654c\u4eba\u9020\u6210\u81f3\u591a\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
            lore.add("   \u00a77\u4e0d\u8fc7,\u9700\u89813\u79d2\u624d\u80fd\u5f15\u7206\u3002");
            return lore;
        }
        lore.add("   \u00a77\u5bf9\u5468\u56f4\u654c\u4eba\u9020\u6210\u81f3\u591a\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
        lore.add("   \u00a77\u4e0d\u8fc7,\u9700\u89813\u79d2\u624d\u80fd\u5f15\u7206\u3002");
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
    public boolean use(GamePlayer gamePlayer, final KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0f, 0.0f);
        new BukkitRunnable() {
            final List<Player> damaged1 = new ArrayList<>();
            int step = 19;
            int countdown = 3;

            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                ParticleEffect.SMOKE_NORMAL.display(0.1f, 0.5f, 0.1f, 0.75f, 2, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
                if (MegaWalls.getRandom().nextInt(100) <= 20) {
                    ParticleEffect.VILLAGER_ANGRY.display(0.1f, 0.4f, 0.1f, 1.0f, 1, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
                }
                ++this.step;
                if (this.step == 20 && this.countdown > 0) {
                    this.step = 0;
                    --this.countdown;
                    ParticleEffect.EXPLOSION_LARGE.display(0.0f, 0.0f, 0.0f, 0.3f, 3, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
                    ParticleEffect.EXPLOSION_NORMAL.display(0.0f, 0.5f, 0.0f, 1.0f, 20, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
                    player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, 1.0f, 2.0f);
                } else if (this.countdown == 0) {
                    ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
                    player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 0.5f);
                    player.sendMessage("§a§lBoom！");
                    for (Player player1 : MainSkill.this.getNearbyPlayers(player.getLocation(), player, 6)) {
                        int dis = (int) player1.getLocation().distance(player.getLocation());
                        if (damaged1.contains(player1)) continue;
                        int damage = Math.max(1, 10 - dis);
                        PlayerUtils.realDamage(player1, player, damage);
                        damaged1.add(player1);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        return true;
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius)
                continue;
            players.add(other);
        }
        return players;
    }
}

