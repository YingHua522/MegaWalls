/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.cow;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("抚慰之哞", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1.7;
            }
            case 2: {
                return 1.9;
            }
            case 3: {
                return 2.1;
            }
            case 4: {
                return 2.3;
            }
            case 5: {
                return 2.5;
            }
        }
        return 1.7;
    }

    public int getRegenLevel(int level) {
        switch (level) {
            case 1:
            case 2: {
                return 1;
            }
            case 3:
            case 4:
            case 5: {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u54de,");
            lore.add("   \u00a77\u81ea\u8eab\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u548c\u751f\u547d");
            lore.add("   \u00a77\u6062\u590dI\u6548\u679c,\u5468\u56f4\u7684\u961f\u53cb\u83b7\u5f97");
            lore.add("   \u00a77\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590d\u00a7a" + StringUtils.level(this.getRegenLevel(level)) + "\u00a77\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u54de,");
        lore.add("   \u00a77\u81ea\u8eab\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u548c\u751f\u547d");
        lore.add("   \u00a77\u6062\u590dI\u6548\u679c,\u5468\u56f4\u7684\u961f\u53cb\u83b7\u5f97");
        lore.add("   \u00a77\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590d\u00a78" + StringUtils.level(this.getRegenLevel(level - 1)) + " \u279c \u00a7a" + StringUtils.level(this.getRegenLevel(level)) + "\u00a77\u6548\u679c\u3002");
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
        int level = kitStats.getSkillLevel();
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (this.getAttribute(level) * 20.0), 0));
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(level) * 20.0), 0));
        for (Player teammate : this.getTeammates(player, 5)) {
            if (teammate.hasPotionEffect(PotionEffectType.REGENERATION)) {
                teammate.removePotionEffect(PotionEffectType.REGENERATION);
            }
            teammate.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(level) * 20.0), this.getRegenLevel(level)));
        }
        player.playSound(player.getLocation(), Sound.COW_IDLE, 2f, 1f);
        new BukkitRunnable() {
            int step = 0;

            public void run() {
                ++this.step;
                if (this.step == 40 || !gamePlayer.isOnline()) {
                    this.cancel();
                    return;
                }
                ParticleEffect.NOTE.display(0.0f, 0.0f, 0.0f, 0.0f, 1, player.getLocation().add(0.0, 3.0, 0.0), 20.0);
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        return true;
    }

    private List<Player> getTeammates(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || !GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther))
                continue;
            players.add(other);
        }
        return players;
    }
}

