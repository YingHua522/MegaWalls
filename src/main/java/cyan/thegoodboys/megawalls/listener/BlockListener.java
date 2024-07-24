package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.mythic.snowman.Snowman;
import cyan.thegoodboys.megawalls.classes.normal.dreadlord.Dreadlord;
import cyan.thegoodboys.megawalls.classes.normal.puppet.Puppet;
import cyan.thegoodboys.megawalls.classes.novice.skeleton.Skeleton;
import cyan.thegoodboys.megawalls.game.ChestManager;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameState;
import cyan.thegoodboys.megawalls.game.stage.ProtectStage;
import cyan.thegoodboys.megawalls.nms.SnowmanFriend;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class BlockListener extends BaseListener {
    public static HashMap<GamePlayer, Boolean> firstinv = new HashMap<>();
    private final Game game;

    public BlockListener(MegaWalls plugin) {
        super(plugin);
        this.game = plugin.getGame();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (!this.game.isStarted() || MegaWalls.getInstance().getGame().getState() == GameState.LOBBY) {
            e.setCancelled(true);
            return;
        }
        if (this.game.isStarted()  && !gamePlayer.isSpectator() && this.game.getStageManager().currentStage() instanceof ProtectStage) {
            e.setCancelled(true);
            gamePlayer.sendMessage("§c现在不能破坏方块！");
            return;
        }
        if ( !gamePlayer.isSpectator() && this.game.isStarted() && !(this.game.getStageManager().currentStage() instanceof ProtectStage)) {
            if (this.game.isProtected(e.getBlock().getLocation())) {
                e.setCancelled(true);
                gamePlayer.sendMessage("§c你不可以修改你的城堡！");
            } else if (this.game.isUnbreakable(e.getBlock().getLocation())) {
                e.setCancelled(true);
            } else if (gamePlayer.getGameTeam().getTeamRegion().isInRegion(e.getBlock().getLocation()) && !gamePlayer.getGameTeam().getTeamWither().getBukkitEntity().isDead()) {
                e.setCancelled(true);
                gamePlayer.sendMessage("§c你不可以修改你的出生点！");
            } else {
                Block block = e.getBlock();
                if ((block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE || block.getType() == Material.TRAPPED_CHEST) && !this.game.isWallsFall()) {
                    if (!gamePlayer.isProtectedBlock(block)) {
                        e.setCancelled(true);
                        gamePlayer.sendMessage("§c你不能破坏受保护的方块！");
                    } else {
                        if (block.getType() == Material.FURNACE) {
                            gamePlayer.sendMessage("§a你破坏了你保护的§e熔炉");
                        } else if (block.getType() == Material.TRAPPED_CHEST) {
                            Chest chest = (Chest) block.getState();
                            for (ItemStack item : chest.getBlockInventory().getContents()) {
                                if (item != null) {
                                    if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                                        gamePlayer.getPlayer().getInventory().addItem(item);
                                    } else if (gamePlayer.getEnderChest().firstEmpty() != -1) {
                                        gamePlayer.getEnderChest().addItem(item);
                                    } else {
                                        gamePlayer.getPlayer().getWorld().dropItem(block.getLocation(), item);
                                    }
                                }
                            }
                            chest.getBlockInventory().clear();
                            gamePlayer.sendMessage("§a你破坏了你保护的§e陷阱箱");
                        }
                        gamePlayer.removeProtectedBlock(e.getBlock());
                    }
                } else {
                    // 获取方块的掉落物
                    Collection<ItemStack> drops = block.getDrops();
                    for (ItemStack drop : drops) {
                        if (gamePlayer.getPlayer().getInventory().firstEmpty() == -1) {
                            boolean itemAdded = false;
                            for (ItemStack item : gamePlayer.getEnderChest().getContents()) {
                                // 检查末影箱中是否存在相同的物品，并且该物品的数量没有达到最大堆叠数量
                                if (item != null && item.isSimilar(drop) && item.getAmount() < item.getMaxStackSize()) {
                                    item.setAmount(item.getAmount() + drop.getAmount());
                                    itemAdded = true;
                                    break;
                                }
                            }
                            if (!itemAdded && gamePlayer.getEnderChest().firstEmpty() != -1) {
                                gamePlayer.getEnderChest().setItem(gamePlayer.getEnderChest().firstEmpty(), drop);
                            } else if (!itemAdded) {
                                gamePlayer.getPlayer().getWorld().dropItem(gamePlayer.getPlayer().getLocation(), drop);
                            }
                        }
                        // 获取被破坏的方块的掉落物
                        Classes classes = ClassesManager.getSelected(gamePlayer);
                        if (e.getBlock().getType() == Material.IRON_ORE && classes instanceof Dreadlord) {
                            continue;
                        }
                        if (e.getBlock().getType() == Material.SNOW_BLOCK && classes instanceof Snowman) {
                            continue;
                        }
                        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
                            return;
                        }
                        gamePlayer.getPlayer().getInventory().addItem(drop);
                    }

                    if (block.getType() == Material.LOG && !this.game.isWallsFall()) {
                        byte data = block.getData();
                        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                            if (block.getLocation().getBlock().getType() == Material.AIR) {
                                block.setType(Material.LOG);
                                block.setData(data);
                                block.getWorld().playSound(block.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                            }

                        }, 200L);
                    }
                }
                Classes classes = ClassesManager.getSelected(gamePlayer);
                if (classes instanceof Snowman && block.getType() == Material.SNOW_BLOCK) {
                    gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 1));
                    block.setType(Material.AIR);
                }
                if ((e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.DIRT || e.getBlock().getType() == Material.GRASS || e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)  && !MegaWalls.getInstance().getGame().isDeathMatch()) {
                    if (MegaWalls.getRandom().nextInt(this.game.isWallsFall() ? 500 : 100) <= 6) {
                        e.setCancelled(true);
                        block.setType(Material.TRAPPED_CHEST);
                        gamePlayer.addProtectedBlock(block);
                        Chest chest = (Chest) block.getState();
                        gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1, 1);
                        ChestManager.fillInventory(chest.getBlockInventory());
                        if (!firstinv.getOrDefault(gamePlayer, false)) {
                            chest.getBlockInventory().addItem(new ItemStack(Material.getMaterial(58)), new ItemStack(Material.FURNACE, 6));
                            if (!(classes instanceof Skeleton || classes instanceof Puppet)) {
                                chest.getBlockInventory().addItem(new ItemStack(Material.IRON_AXE, 1));
                                firstinv.put(gamePlayer, true);
                            }
                        }
                    }
                }
                classes.getCollectSkill().onBlockBreak(gamePlayer.getPlayerStats().getKitStats(classes), e);

                e.setExpToDrop(0);
                if (e.getBlock().getType() == Material.TRAPPED_CHEST) {
                    return;
                }
                e.getBlock().setType(Material.AIR);
            }
        }
    }


    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block b = event.getBlock();
        if (b.hasMetadata("SharkWater")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void EntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if ( (gamePlayer.isSpectator() || !this.game.isStarted())) {
            e.setCancelled(true);
            return;
        }
        if ( gamePlayer.getGameTeam().getTeamRegion().isInRegion(e.getBlock().getLocation()) && !gamePlayer.getGameTeam().getTeamWither().getBukkitEntity().isDead()) {
            e.setCancelled(true);
            e.setBuild(false);
            gamePlayer.sendMessage("§c你不可以修改你的出生点！");
        }
        if ( this.game.isProtected(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.setBuild(false);
            gamePlayer.sendMessage("§c你不可以修改你的城堡！");
            return;
        } else if (this.game.isUnbreakable(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.setBuild(false);
            return;
        }
        if (!game.isWallsFall()) {
            if (e.getBlock().getType() == Material.TNT) {
                e.setCancelled(true);
                e.setBuild(false);
                    gamePlayer.sendMessage("§c你不能在巨墙倒塌前放置TNT！");
                    return;

            }
            if (e.getBlock().getType() == Material.IRON_BLOCK) {
                e.setCancelled(true);
                e.setBuild(false);
                    gamePlayer.sendMessage("§c你不能在巨墙倒塌放置此物品！");
                    return;
            }
        }
        if (e.getBlock().getType()== Material.ANVIL || e.getBlock().getType() == Material.ENCHANTMENT_TABLE) {
            e.setCancelled(true);
            e.setBuild(false);
                gamePlayer.sendMessage("§c你不能放置此物品！");
            return;
        }
        Block block = e.getBlock();
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (block.getType() == Material.PUMPKIN && block.getLocation().add(0, -1, 0).getBlock().getType() == Material.SNOW_BLOCK && block.getLocation().add(0, -2, 0).getBlock().getType() == Material.SNOW_BLOCK && Snowman.snowman.getOrDefault(gamePlayer, 0) < 4) {
                if (classes instanceof Snowman) {
                    if (Snowman.snowman.getOrDefault(gamePlayer, 0) >= 4) {
                        e.setCancelled(true);
                        e.setBuild(false);
                        gamePlayer.sendMessage("§c你不能放置更多的雪人！");
                        return;
                    }
                    block.setType(Material.AIR);
                    block.getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);
                    block.getLocation().add(0, -2, 0).getBlock().setType(Material.AIR);
                    final SnowmanFriend sf = new SnowmanFriend(((CraftWorld) Bukkit.getWorld("world")).getHandle());
                    sf.setGamePlayer(gamePlayer);
                    sf.getBukkitEntity().setMetadata(MegaWalls.getMetadataValue(), new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
                    EntityTypes.spawnEntity(sf, block.getLocation().add(0, -2, 0));
                    Snowman.snowman.put(gamePlayer, Snowman.snowman.getOrDefault(gamePlayer, 0) + 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sf.die();
                            Snowman.snowman.put(gamePlayer, Snowman.snowman.getOrDefault(gamePlayer, 0) - 1);
                            this.cancel();
                        }
                    }.runTaskLater(MegaWalls.getInstance(), 300L);
                }
            }

        if (!this.game.isWallsFall()) {
            for (BlockFace face : BlockFace.values()) {
                Block relativeBlock = block.getRelative(face);
                if (relativeBlock == null || relativeBlock.equals(block) || relativeBlock.getType() != Material.TRAPPED_CHEST && relativeBlock.getType() != Material.FURNACE)
                    continue;
                e.setCancelled(true);
                e.setBuild(false);
                    gamePlayer.sendMessage("§c你不能在受保护的方块附近一格内摆放其他方块！");

                return;
            }
            if (block.getType() == Material.FURNACE || block.getType() == Material.TRAPPED_CHEST) {

                    gamePlayer.addProtectedBlock(block);

                if (block.getType() == Material.TRAPPED_CHEST) {
                        gamePlayer.sendMessage("§a你的§e陷阱箱§a已被保护！");
                }
                if (block.getType() == Material.FURNACE) {
                        gamePlayer.sendMessage("§a你的§e熔炉§a已被保护！");

                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent e) {
        if (!(e.getRemover() instanceof Player)) {
            return;
        }
        if (Objects.requireNonNull(GamePlayer.get(e.getRemover().getUniqueId())).isSpectator() || !this.game.isStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace(HangingPlaceEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).isSpectator() || !this.game.isStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent e) {
        e.setCancelled(true);
    }
}









/*
                    if (!this.game.isWallsFall() && (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GRASS || e.getBlock().getType() == Material.DIRT || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE)) {
                        // 获取方块的掉落物
                        Collection<ItemStack> drops = block.getDrops();
                        for (ItemStack drop : drops) {
                            if (gamePlayer.getPlayer().getInventory().firstEmpty() == -1) {
                                boolean itemAdded = false;
                                for (ItemStack item : gamePlayer.getEnderChest().getContents()) {
                                    // 检查末影箱中是否存在相同的物品，并且该物品的数量没有达到最大堆叠数量
                                    if (item != null && item.isSimilar(drop) && item.getAmount() < item.getMaxStackSize()) {
                                        item.setAmount(item.getAmount() + drop.getAmount());
                                        itemAdded = true;
                                        break;
                                    }
                                }
                                if (!itemAdded && gamePlayer.getEnderChest().firstEmpty() != -1) {
                                    gamePlayer.getEnderChest().setItem(gamePlayer.getEnderChest().firstEmpty(), drop);
                                } else if (!itemAdded) {
                                    gamePlayer.getPlayer().getWorld().dropItem(gamePlayer.getPlayer().getLocation(), drop);
                                }
                            }  // 将掉落物添加到玩家的背包
                            //gamePlayer.getPlayer().getInventory().addItem(drop);
                            //block.setType(Material.AIR);
                        }
                    }

                     */


