/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.novice.enderman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class MainSkill extends Skill {
    private static final Set<Material> set = new HashSet<>();

    static {
        set.addAll(Arrays.asList(Material.values()));
    }

    public MainSkill(Classes classes) {
        super("瞬移", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 13.0;
            }
            case 2: {
                return 16.0;
            }
            case 3: {
                return 19.0;
            }
            case 4: {
                return 22.0;
            }
            case 5: {
                return 25.0;
            }
        }
        return 13.0;
    }

    public int getSpeed(int level) {
        switch (level) {
            case 1:
            case 2: {
                return 0;
            }
            case 3:
            case 4: {
                return 1;
            }
            case 5: {
                return 2;
            }
        }
        return 0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<>();
        if (level == 1) {
            lore.add(" §8▪ §7瞬移到在§a" + this.getAttribute(level) + "§7格以内的指定玩家,");
            lore.add("   §7并获得5秒的速度§a" + StringUtils.level(this.getSpeed(level)) + "§7效果。");
            lore.add("   §7在激活速度" + StringUtils.level(this.getSpeed(level)) + "效果时,每次攻击");
            lore.add("   §7获得的能量减半");
            lore.add(" ");
            lore.add("§7冷却时间:§a5秒");
            return lore;
        }
        lore.add(" §8▪ §7瞬移到在§8" + this.getAttribute(level - 1) + " ➜");
        lore.add("   §a" + this.getAttribute(level) + "§7格以内的指定玩家,");
        if (level % 2 == 0) {
            lore.add("   §7并获得5秒的速度§8" + StringUtils.level(this.getSpeed(level)) + "§7效果。");
        } else {
            lore.add("   §7并获得5秒的速度§8" + StringUtils.level(this.getSpeed(level - 1)) + " ➜");
            lore.add("   §a" + StringUtils.level(this.getSpeed(level)) + "效果。");
        }
        lore.add("   §7在激活速度" + StringUtils.level(this.getSpeed(level)) + "效果时,每次攻击");
        lore.add("   §7获得的能量减半");
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
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        if (isSkillInCooldown(gamePlayer)) {
            player.sendMessage("§c你的技能冷却还有 §e" + Enderman.skillCooldown.getOrDefault(gamePlayer, 0) + " 秒§!");
            return false;
        }
        Player target = getTargetPlayer(player, (int) this.getAttribute(kitStats.getSkillLevel()));
        if (target == null) {
            player.sendMessage("§c附近没有任何玩家!");
            return false;
        }

        Location targetLocation = calculateTargetLocation(target);
        boolean isSolidBlocksTraversed = isSolidBlocksTraversed(player.getLocation(), target.getLocation());
        if (isLocationSafe(targetLocation)) {
            if (isSolidBlocksTraversed) {
                applyWeaknessEffect(player);
            }
            player.teleport(target);
        } else {
            if (isSolidBlocksTraversed) {
                applyWeaknessEffect(player);
            }
            player.teleport(targetLocation);
        }
        Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> applyEffects(player, kitStats));
        updateCooldowns(gamePlayer);
        return true;
    }

    //检查技能是否在冷却中
    private boolean isSkillInCooldown(GamePlayer gamePlayer) {
        return Enderman.skillCooldown.getOrDefault(gamePlayer, 0) > 0;
    }

    //获取目标玩家
    private Player getTargetPlayer(Player player, int range) {
        Player target = null;
        for (Block block : player.getLineOfSight(set, range)) {
            for (Player player2 : this.getNearbyPlayers(block.getLocation(), player, 2)) {
                if (target != null && !(player2.getLocation().distance(player.getLocation()) < target.getLocation().distance(player.getLocation())))
                    continue;
                target = player2;
            }
        }
        return target;
    }

    //计算目标位置
    private Location calculateTargetLocation(Player target) {
        Location loc = target.getLocation();
        Vector vec = loc.getDirection();
        vec.multiply(-0.7);
        loc.add(vec.getX(), -1, vec.getZ());
        return loc.add(0, 1, 0);
    }

    //检查是否安全
    private boolean isLocationSafe(Location location) {
        Location loc2 = location.clone().add(0, 1, 0);
        return !location.getBlock().isEmpty() && !loc2.getBlock().isEmpty() && !location.getBlock().isLiquid() && !loc2.getBlock().isLiquid();
    }

    //检查是否通过实心方块传送
    private boolean isSolidBlocksTraversed(Location locA, Location locB) {
        return countSolidBlocksTraversed(locA, locB) > 6;
    }

    //添加减弱效果
    private void applyWeaknessEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
        player.sendMessage("§c你因通过实心方块传送而减弱!");
    }

    //添加药水效果及特效
    private void applyEffects(Player player, KitStatsContainer kitStats) {
        new BukkitRunnable() {
            public void run() {
                for (Location location : LocationUtils.getCircle(player.getLocation(), 0.5, 20)) {
                    ParticleEffect.SPELL_WITCH.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, 30.0);
                }
            }
        }.runTaskLater(MegaWalls.getInstance(), 5L);
        player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, getSpeed(kitStats.getSkillLevel())));
    }


    //更新计时器
    private void updateCooldowns(GamePlayer gamePlayer) {
        Enderman.skillCooldown.put(gamePlayer, 8);
        Enderman.skill3.put(gamePlayer, Enderman.skill3.get(gamePlayer) - 1);
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Enderman.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + Enderman.skillCooldown.get(gamePlayer) + "s");
    }

    private HashSet<Player> getNearbyPlayers(Location location, Player player, int radius) {
        HashSet<Player> players = new HashSet<>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }

    public int countSolidBlocksTraversed(Location locA, Location locB) {
        // 获取玩家位置
        // 计算直线路径上的所有方块坐标
        double dx = Math.abs(locB.getX() - locA.getX());
        double dy = Math.abs(locB.getY() - locA.getY());
        double dz = Math.abs(locB.getZ() - locA.getZ());

        int xStep = locA.getX() < locB.getX() ? 1 : -1;
        int yStep = locA.getY() < locB.getY() ? 1 : -1;
        int zStep = locA.getZ() < locB.getZ() ? 1 : -1;

        int solidBlocksCount = 0;
        for (int x = locA.getBlockX(); x != locB.getBlockX() + xStep; x += xStep) {
            for (int y = locA.getBlockY(); y != locB.getBlockY() + yStep; y += yStep) {
                for (int z = locA.getBlockZ(); z != locB.getBlockZ() + zStep; z += zStep) {
                    // 获取当前坐标的方块
                    World world = locA.getWorld();
                    Material blockType = world.getBlockAt(x, y, z).getType();

                    // 检查是否为固体方块
                    if (!blockType.isTransparent()) {
                        solidBlocksCount++;
                    }
                }
            }
        }

        return solidBlocksCount;
    }
}

