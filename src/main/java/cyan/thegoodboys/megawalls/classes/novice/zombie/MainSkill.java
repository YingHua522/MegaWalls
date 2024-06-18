/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.novice.zombie;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("治愈之环", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.2;
            }
            case 2: {
                return 4.8;
            }
            case 3: {
                return 5.4;
            }
            case 4: {
                return 6.0;
            }
            case 5: {
                return 6.6;
            }
        }
        return 4.2;
    }

    public double getMateAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.5;
            }
            case 2: {
                return 3.9;
            }
            case 3: {
                return 4.3;
            }
            case 4: {
                return 4.7;
            }
            case 5: {
                return 5.1;
            }
        }
        return 3.55;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7恢复你自己 §a" + this.getAttribute(level) + "血量");
            lore.add("   §7和你附近的队友 §a" + this.getMateAttribute(level) + "血量");
            return lore;
        }
        lore.add(" §8▪ §7恢复你自己 §8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "血量");
        lore.add("   §7和你附近的队友 §8" + this.getMateAttribute(level - 1) + " ➜ §a" + this.getMateAttribute(level) + "血量");
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
        if (!gamePlayer.isOnline()) {
            return false;
        }
        int level = kitStats.getSkillLevel();
        Player player = gamePlayer.getPlayer();
        PlayerUtils.heal(player, this.getMateAttribute(level));
        for (Player teammate : this.getTeammates(player, 5)) {
            PlayerUtils.heal(teammate, this.getMateAttribute(level));
            teammate.sendMessage("§a§l你的治愈光环治疗了 " + player.getDisplayName());
        }
        new BukkitRunnable() {
            int step = 0;

            public void run() {
                ++this.step;
                if (this.step == 40) {
                    this.cancel();
                    return;
                }
                Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> ParticleEffect.HEART.display(0.2f, 0.5f, 0.2f, 1.0f, 1, gamePlayer.getPlayer().getLocation().add(0.0, 1.0, 0.0), 10.0));
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        return true;
    }

    private List<Player> getTeammates(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || !Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

