/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.novice.skeleton;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("爆裂箭矢", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 2;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 5;
            }
            case 5: {
                return 6;
            }
        }
        return 0.1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u4e00\u652f\u7206\u70b8Arrow,");
            lore.add("   \u00a77\u9020\u6210\u654c\u4eba\u5269\u4f59\u8840\u91cf\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3,");
            lore.add("   \u00a77\u5e76\u51fb\u9000\u5468\u56f4\u654c\u4eba\u3002\u6700\u5c11\u9020\u62102\u70b9\u4f24\u5bb3\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u4e00\u652f\u7206\u70b8Arrow,");
        lore.add("   \u00a77\u9020\u6210\u654c\u4eba\u5269\u4f59\u8840\u91cf\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("   \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3,");
        lore.add("   \u00a77\u5e76\u51fb\u9000\u5468\u56f4\u654c\u4eba\u3002\u6700\u5c11\u9020\u62102\u70b9\u4f24\u5bb3\u3002");
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
    public boolean use(final GamePlayer gamePlayer, final KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        final Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setMetadata(MegaWalls.getMetadataValue(), MegaWalls.getFixedMetadataValue());
        arrow.setVelocity(player.getLocation().getDirection().multiply(2.35));
        new BukkitRunnable() {
            public void run() {
                if (arrow.isDead() || arrow.isOnGround()) {
                    Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> {
                        ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, arrow.getLocation(), 10.0);
                        arrow.getWorld().playSound(arrow.getLocation(), Sound.EXPLODE, 1.0f, 0.0f);
                    });
                    TNTPrimed tntPrimed = player.getWorld().spawn(arrow.getLocation().add(0.0D, 1.0D, 0.0D), TNTPrimed.class);
                    tntPrimed.setYield(1f);
                    tntPrimed.setMetadata("SKELETON", new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
                    tntPrimed.setFuseTicks(1);
                    for (Player player1 : PlayerUtils.getNearbyPlayers(arrow.getLocation(), 6.0)) {
                        GamePlayer gamePlayer1 = GamePlayer.get(player1.getUniqueId());
                        if (gamePlayer1 != null && (gamePlayer1.isSpectator() || gamePlayer.getGameTeam().isInTeam(gamePlayer1)))
                            continue;
                        PlayerUtils.realDamage(player1, player, 4);
                    }
                    if (!arrow.isDead()) {
                        arrow.remove();
                    }

                    Block center = arrow.getLocation().getBlock();

                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                Block block = center.getRelative(x, y, z);
                                if (MegaWalls.getInstance().getGame().isProtected(block.getLocation()) || MegaWalls.getInstance().getGame().isUnbreakable(block.getLocation()) || (block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.BARRIER) || block.getType() == Material.BEDROCK) {
                                    continue;
                                }
                                block.setType(Material.AIR);
                            }
                        }
                    }
                    this.cancel();
                    return;
                }
                Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> ParticleEffect.CRIT_MAGIC.display(0.0f, 0.0f, 0.0f, 1.0f, 5, arrow.getLocation(), 10.0));
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        return true;
    }

    private List<Player> getTeammates(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || !Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }
}

