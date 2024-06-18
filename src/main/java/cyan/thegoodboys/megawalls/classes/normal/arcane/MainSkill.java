/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.classes.normal.arcane;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.classes.normal.puppet.Puppet;
import cyan.thegoodboys.megawalls.classes.novice.skeleton.Skeleton;
import cyan.thegoodboys.megawalls.game.ChestManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.listener.BlockListener;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MainSkill extends Skill {
    private static final Set<Material> set = new HashSet<Material>();

    static {
        set.addAll(Arrays.asList(Material.values()));
    }

    public MainSkill(Classes classes) {
        super("奥数激光", classes);
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
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u4e00\u675f\u6fc0\u5149,\u9020\u6210\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
            lore.add("   \u00a77\u4e5f\u53ef\u4ee5\u4f7f\u7528\u6b64\u6280\u80fd\u8fdb\u884c\u6316\u6398\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a1\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u4e00\u675f\u6fc0\u5149,\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
        lore.add("   \u00a77\u4e5f\u53ef\u4ee5\u4f7f\u7528\u6b64\u6280\u80fd\u8fdb\u884c\u6316\u6398\u3002");
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
        Player player = gamePlayer.getPlayer();
        ArrayList<Player> damaged = new ArrayList<>();
        int blockCount = 0; // 添加一个计数器
        for (Block block : player.getLineOfSight(set, 34)) {
            ParticleEffect.REDSTONE.display(0.0f, 0.0f, 0.0f, 0.0f, 3, block.getLocation(), 10.0);
            block.getWorld().playSound(block.getLocation(), Sound.FIREWORK_LAUNCH, 1.0f, 1.0f);
            for (Block block1 : LocationUtils.getCube(block.getLocation(), 2)) { // 修改为3x3方块
                if (blockCount >= 200) break; // 如果已经破坏了10个方块，就跳出循环
                if (MegaWalls.getInstance().getGame().isProtected(block1.getLocation()) || MegaWalls.getInstance().getGame().isUnbreakable(block1.getLocation()) || block1.getType() == Material.BEDROCK || block1.getType() == Material.TRAPPED_CHEST || block1.getType() == Material.FURNACE || block1.getType() == Material.BARRIER || block1.getType() == Material.DIAMOND_ORE || block1.getType() == Material.BURNING_FURNACE || block1.getType() == Material.CHEST && !gamePlayer.isProtectedBlock(block1))
                    continue;
                // 获取掉落物
                if (!MegaWalls.getInstance().getGame().isWallsFall()) {
                    Collection<ItemStack> drops;
                    if (block1.getType() == Material.IRON_ORE) {
                        drops = Collections.singletonList(new ItemStack(Material.IRON_INGOT));
                    } else {
                        drops = block1.getDrops();
                    }
                    // 判定玩家背包不是-1
                    if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                        gamePlayer.getPlayer().getInventory().addItem(drops.toArray(new ItemStack[0]));
                    } else if (gamePlayer.getEnderChest().firstEmpty() != -1) { // 判定玩家末影箱不是-1
                        gamePlayer.getEnderChest().addItem(drops.toArray(new ItemStack[0]));
                    } else {
                        // 否则直接掉落
                        for (ItemStack item : drops) {
                            gamePlayer.getPlayer().getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }

                    block1.setType(Material.AIR);
                    blockCount++; // 破坏一个方块，计数器加1

                    if (block1.getType() == Material.TRAPPED_CHEST) {
                        Chest chest = (Chest) block1.getState();
                        Classes classes = ClassesManager.getSelected(gamePlayer);
                        ChestManager.fillInventory(chest.getBlockInventory());
                        if (!BlockListener.firstinv.getOrDefault(gamePlayer, false)) {
                            chest.getBlockInventory().addItem(new ItemStack(Material.getMaterial(58)), new ItemStack(Material.FURNACE, 4));
                            if (!(classes instanceof Skeleton || classes instanceof Puppet)) {
                                chest.getBlockInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
                                BlockListener.firstinv.put(gamePlayer, true);
                            }
                        }
                    }
                }
            }
            for (Player player1 : getNearbyPlayers(block.getLocation(), player, 2)) {
                if (damaged.contains(player1)) continue;
                PlayerUtils.realDamage(player1, player, 2); // 造成2点伤害
                ParticleEffect.FIREWORKS_SPARK.display(0.0f, 0.0f, 0.0f, 0.0f, 10, player1.getLocation(), 10.0);
                damaged.add(player1);
            }
        }
        new BukkitRunnable() {
            public void run() {
                Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).addEnergy(36, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                cancel();
            }
        }.runTaskTimer(MegaWalls.getInstance(), 5L, 0);
        return true;
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName() + " " + (gamePlayer.getEnergy() == 100 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l\u2715");
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
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
}

