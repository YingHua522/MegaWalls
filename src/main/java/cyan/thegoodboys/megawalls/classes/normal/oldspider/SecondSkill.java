/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.normal.oldspider;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill
        extends Skill {
    public SecondSkill(Classes classes) {
        super("落地踩", classes);
    }

    public static List<Block> getSphere(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;
        for (int x = X - radius; x <= X + radius; ++x) {
            for (int z = Z - radius; z <= Z + radius; ++z) {
                if ((X - x) * (X - x) + (Z - z) * (Z - z) > radiusSquared) continue;
                blocks.add(location.getWorld().getBlockAt(x, Y, z));
            }
        }
        return blocks;
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.1;
            }
            case 2: {
                return 0.15;
            }
            case 3: {
                return 0.2;
            }
        }
        return 0.1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u53d7\u5230\u6454\u843d\u4f24\u5bb3,\u6bcf\u635f\u5931\u4e00\u9897\u5fc3\u7684\u751f\u547d\u503c,");
            lore.add("   \u00a77\u83b7\u5f97\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3\u52a0\u6210(\u6700\u591a4\u6b21)");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u53d7\u5230\u6454\u843d\u4f24\u5bb3,\u6bcf\u635f\u5931\u4e00\u9897\u5fc3\u7684\u751f\u547d\u503c,");
        lore.add("   \u00a77\u83b7\u5f97\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3\u52a0\u6210(\u6700\u591a4\u6b21)");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        int blocks = (int) player.getFallDistance();
        int damage;
        if (blocks >= 5) {
            damage = (int) (blocks * 1.5);
            for (Player player1 : getNearbyPlayers(player, 5)) {
                if (gamePlayer.getGameTeam().isInTeam(gamePlayer)) continue;
                if (player1.equals(player)) continue;
                if (damage < 14) {
                    PlayerUtils.realDamage(player1, player, damage);
                } else if (damage > 14) {
                    PlayerUtils.realDamage(player1, player, 14);
                }
                player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
            }
            player.getWorld().playSound(player.getLocation(), Sound.SPIDER_DEATH, 1.0f, 0.0f);
            player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, (float) 0.7, (float) 0.5);
            ParticleUtils.play(EnumParticle.EXPLOSION_LARGE, player.getLocation(), 0.1, 0.1, 0.1, 0, 3);
            ParticleUtils.play(EnumParticle.LAVA, player.getLocation(), 0.3, 0.3, 0.3, 0, 10);
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return null;
    }

    private List<Player> getNearbyPlayers(Player player, double radius) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther))
                continue;
            players.add(other);
        }
        return players;
    }
}

