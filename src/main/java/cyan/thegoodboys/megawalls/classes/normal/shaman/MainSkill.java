/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.shaman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    int number = 0;

    public MainSkill(Classes classes) {
        super("龙卷风", classes);
    }

    public static void createTornadodmg(Player player) {
        new BukkitRunnable() {
            final Location loc = player.getLocation();
            final GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            final Vector direction = player.getEyeLocation().getDirection();
            final Location location = loc.clone();
            int seconds = 0;

            public void run() {
                if (seconds >= 5) {
                    this.cancel();
                    Shaman.wind.put(gamePlayer, Shaman.wind.getOrDefault(gamePlayer, 0) - 1);
                    return;
                }
                location.add(direction.getX(), 0, direction.getZ());
                location.setY(loc.getY());
                for (int i = 0; i <= 8; i++) {
                    Block block = location.clone().add(0, i, 0).getBlock();
                    if (MegaWalls.getInstance().getGame().isUnbreakable(block.getLocation()) || (block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE || block.getType() == Material.TRAPPED_CHEST) && !gamePlayer.isProtectedBlock(block) || block.getType() == Material.BEDROCK)
                        continue;
                    if (!block.isEmpty()) {
                        block.setType(Material.AIR);
                    }
                }
                for (Player other : getNearbyPlayers(loc, player, 5)) {
                    PlayerUtils.realDamage(other, player, 2);
                    Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).addEnergy(10, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                }
                spawnTornadoParticles(player);
                player.getWorld().playSound(location, Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
                seconds++;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 20L);
    }

    private static List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }

    private static List<Block> getBlock(Location location, int high) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int i = 0; i <= high; ++i) {
            Location loc = location.add(0, i, 0);
            Block block1 = loc.getBlock();
            blocks.add(block1);
        }
        return blocks;
    }

    public static void spawnTornadoParticles(Player player) {
        Location loc = player.getLocation();
        for (double y = 0; y <= 2; y += 0.05) {
            for (double t = 0; t <= Math.PI * 2; t += Math.PI / 16) {
                double x = 0.5 * t * Math.cos(t);
                double z = 0.5 * t * Math.sin(t);
                loc.add(x, y, z);
                ParticleEffect.CLOUD.display(0.0f, 0.0f, 0.0f, 0.0f, 1, loc, 100.0);
                loc.subtract(x, y, z);
            }
        }
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1.0;
            }
            case 2: {
                return 1.0;
            }
            case 3: {
                return 1.0;
            }
            case 4: {
                return 1.0;
            }
            case 5: {
                return 1.0;
            }
        }
        return 1.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" §8▪ §7生成一个毁灭性的龙卷风,");
        lore.add("   §7每秒对沿途玩家造成§a" + this.getAttribute(level) + "§7点伤害。");
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
        if (Shaman.wind.getOrDefault(gamePlayer, 0) < 3) {
            final Player player = gamePlayer.getPlayer();
            createTornadodmg(player);
            Shaman.wind.put(gamePlayer, Shaman.wind.getOrDefault(gamePlayer, 0) + 1);
        } else {
            gamePlayer.sendMessage("§c你不能释放更多的龙卷风");
            return false;
        }
        return true;
    }
}

