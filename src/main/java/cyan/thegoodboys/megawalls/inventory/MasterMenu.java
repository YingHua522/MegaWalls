/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MasterMenu {
    public static SmartInventory master(final Classes classes, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(classes.getDisplayName() + "精通");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                boolean enoughCoins = gamePlayer.getPlayerStats().getCoins() >= 250000;
                ItemBuilder itemBuilder = (new ItemBuilder(Material.GOLDEN_APPLE)).setDisplayName("§e精通I-§7" + classes.getDisplayName());
                List<String> lore = new ArrayList();
                lore.add("§8能力升级");
                lore.add(" §a+ §7每条生命获得§a+2❤");
                lore.add(" ");
                lore.add("§7花费:§6" + StringUtils.formattedCoins(250000) + "硬币");
                lore.add(" ");
                if (kitStats.getLevel() >= 2) {
                    lore.add("§a已解锁");
                } else if (!enoughCoins) {
                    lore.add("§c你没有足够的硬币购买此物品！");
                } else if (enoughCoins) {
                    lore.add("§e点击解锁！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(1, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (kitStats.getLevel() >= 2) {
                        gamePlayer.sendMessage("§c你已经解锁这一等级！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughCoins) {
                        gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else {
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build("精通升级", e.getCurrentItem(), inventory, (event) -> {
                            gamePlayer.sendMessage("§a升级成功！");

                            for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
                                online.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                                online.sendMessage("§e恭喜§7" + gamePlayer.getPlayer().getDisplayName() + "§e解锁了§c" + classes.getDisplayName() + "§e精通I！");
                                online.sendTitle("§7" + gamePlayer.getPlayer().getDisplayName(), "§e解锁了§c" + classes.getDisplayName() + "§e精通I！", 10, 200, 10);
                            }

                            gamePlayer.getPlayerStats().takeCoins(250000);
                            kitStats.addLevel();
                            inventory.open(player);
                        }).open(player);
                    }

                }));
                boolean enoughCoins2 = gamePlayer.getPlayerStats().getCoins() >= 500000;
                boolean enoughMasterPoints2 = kitStats.getMasterPoints() >= 500;
                itemBuilder = (new ItemBuilder(Material.GOLD_INGOT)).setDisplayName("§e精通II-§7" + classes.getDisplayName());
                lore.clear();
                lore.add("§8特效升级");
                lore.add(" §a+ §7技能光环");
                lore.add(" ");
                lore.add("§7需要:§e" + StringUtils.formattedCoins(500) + "积分");
                lore.add("§7花费:§6" + StringUtils.formattedCoins(500000) + "硬币");
                lore.add(" ");
                if (kitStats.getLevel() >= 3) {
                    lore.add("§a已解锁");
                } else if (!enoughCoins2) {
                    lore.add("§c你没有足够的硬币购买此物品！");
                } else if (!enoughMasterPoints2) {
                    lore.add("§c还需要" + (500 - kitStats.getMasterPoints()) + "精通积分！");
                } else if (enoughCoins2 && enoughMasterPoints2) {
                    lore.add("§e点击解锁！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(1, 3, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (kitStats.getLevel() >= 3) {
                        gamePlayer.sendMessage("§c你已经解锁这一等级！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughCoins2) {
                        gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughMasterPoints2) {
                        gamePlayer.sendMessage("§c还需要" + (500 - kitStats.getMasterPoints()) + "精通积分！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (enoughCoins2 && enoughMasterPoints2) {
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build("精通升级", e.getCurrentItem(), inventory, (event) -> {
                            gamePlayer.sendMessage("§a升级成功！");
                            Iterator var6 = GamePlayer.getOnlinePlayers().iterator();

                            while (var6.hasNext()) {
                                GamePlayer online = (GamePlayer) var6.next();
                                online.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                                online.sendMessage("§e恭喜§7" + gamePlayer.getPlayer().getDisplayName() + "§e解锁了§c" + classes.getDisplayName() + "§e精通II！");
                                online.sendTitle("§7" + gamePlayer.getPlayer().getDisplayName(), "§e解锁了§c" + classes.getDisplayName() + "§e精通II！", 10, 200, 10);
                            }

                            gamePlayer.getPlayerStats().takeCoins(500000);
                            kitStats.takeMasterPoints(500);
                            kitStats.addLevel();
                            inventory.open(player);
                        }).open(player);
                    }

                }));
                boolean enoughCoins3 = gamePlayer.getPlayerStats().getCoins() >= 500000;
                boolean enoughMasterPoints3 = kitStats.getMasterPoints() >= 1000;
                itemBuilder = (new ItemBuilder(Material.GOLD_INGOT)).setDisplayName("§e精通III-§7" + classes.getDisplayName());
                lore.clear();
                lore.add("§8特效升级");
                lore.add(" §a+ §7皮肤:§a精通" + classes.getDisplayName());
                lore.add(" ");
                lore.add("§7需要:§e" + StringUtils.formattedCoins(1000) + "积分");
                lore.add("§7花费:§6" + StringUtils.formattedCoins(500000) + "硬币");
                lore.add(" ");
                if (kitStats.getLevel() >= 4) {
                    lore.add("§a已解锁");
                } else if (!enoughCoins3) {
                    lore.add("§c你没有足够的硬币购买此物品！");
                } else if (!enoughMasterPoints3) {
                    lore.add("§c还需要" + (1000 - kitStats.getMasterPoints()) + "精通积分！");
                } else if (enoughCoins3 && enoughMasterPoints3) {
                    lore.add("§e点击解锁！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(1, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (kitStats.getLevel() >= 4) {
                        gamePlayer.sendMessage("§c你已经解锁这一等级！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughCoins3) {
                        gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughMasterPoints3) {
                        gamePlayer.sendMessage("§c还需要" + (1000 - kitStats.getMasterPoints()) + "精通积分！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else {
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build("精通升级", e.getCurrentItem(), inventory, (event) -> {
                            gamePlayer.sendMessage("§a升级成功！");

                            for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
                                online.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                                online.sendMessage("§e恭喜§7" + gamePlayer.getPlayer().getDisplayName() + "§e解锁了§c" + classes.getDisplayName() + "§e精通III！");
                                online.sendTitle("§7" + gamePlayer.getPlayer().getDisplayName(), "§e解锁了§c" + classes.getDisplayName() + "§e精通III！", 10, 200, 10);
                            }

                            gamePlayer.getPlayerStats().takeCoins(500000);
                            kitStats.takeMasterPoints(1000);
                            kitStats.addLevel();
                            inventory.open(player);
                        }).open(player);
                    }

                }));
                boolean enoughCoins4 = gamePlayer.getPlayerStats().getCoins() >= 750000;
                boolean enoughMasterPoints4 = kitStats.getMasterPoints() >= 2000;
                itemBuilder = (new ItemBuilder(Material.GOLD_BLOCK)).setDisplayName("§e精通IV-§7" + classes.getDisplayName());
                lore.clear();
                lore.add("§8最终升级");
                lore.add(" §a+ §7金色§6[" + classes.getDisplayName() + "]§7职业名牌");
                lore.add(" §a+ §6+1硬币§7/击杀");
                lore.add(" ");
                lore.add("§8硬币加成对所有职业有效,");
                lore.add("§8可叠加且只在标准模式下生效。");
                lore.add(" ");
                lore.add("§7需要:§e" + StringUtils.formattedCoins(2000) + "积分");
                lore.add("§7花费:§6" + StringUtils.formattedCoins(750000) + "硬币");
                lore.add(" ");
                if (kitStats.getLevel() >= 5) {
                    lore.add("§a已解锁");
                } else if (!enoughCoins4) {
                    lore.add("§c你没有足够的硬币购买此物品！");
                } else if (!enoughMasterPoints4) {
                    lore.add("§c还需要" + (2000 - kitStats.getMasterPoints()) + "精通积分！");
                } else if (enoughCoins4 && enoughMasterPoints4) {
                    lore.add("§e点击解锁！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(1, 7, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (kitStats.getLevel() >= 5) {
                        gamePlayer.sendMessage("§c你已经解锁这一等级！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughCoins4) {
                        gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (!enoughMasterPoints4) {
                        gamePlayer.sendMessage("§c还需要" + (2000 - kitStats.getMasterPoints()) + "精通积分！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else if (enoughCoins4 && enoughMasterPoints4) {
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build("精通升级", e.getCurrentItem(), inventory, (event) -> {
                            gamePlayer.sendMessage("§a升级成功！");

                            for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
                                online.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                                online.sendMessage("§e恭喜§7" + gamePlayer.getPlayer().getDisplayName() + "§e解锁了§c" + classes.getDisplayName() + "§e精通IV！");
                                online.sendTitle("§7" + gamePlayer.getPlayer().getDisplayName(), "§e解锁了§c" + classes.getDisplayName() + "§e精通IV！", 10, 200, 10);
                            }

                            gamePlayer.getPlayerStats().takeCoins(750000);
                            kitStats.takeMasterPoints(2000);
                            kitStats.addLevel();
                            inventory.open(player);
                        }).open(player);
                    }

                }));
                itemBuilder = (new ItemBuilder(Material.EMPTY_MAP)).setDisplayName("§e精通积分");
                lore.clear();
                lore.add("§7在标准模式使用" + classes.getDisplayName() + "获得");
                lore.add("§7最终击杀/助攻和胜场来获得分数。");
                lore.add(" ");
                lore.add("§7胜利=§e10分");
                lore.add("§7最终击杀=§e1分");
                lore.add(" ");
                lore.add("§7你拥有:§e" + kitStats.getMasterPoints() + "积分");
                itemBuilder.setLore((List) lore);
                contents.set(3, 3, ClickableItem.of(itemBuilder.build(), (e) -> {
                }));
                if (kitStats.getLevel() >= 5) {
                    itemBuilder = (new ItemBuilder(Material.GOLD_NUGGET)).setDisplayName("§e开启或关闭金色职业名牌");
                    lore.clear();
                    lore.add("§7当前状态:" + (kitStats.isEnableGoldTag() ? "§a开启" : "§c关闭"));
                    lore.add(" ");
                    lore.add("§e点击" + (kitStats.isEnableGoldTag() ? "关闭" : "开启") + "！");
                    itemBuilder.setLore((List) lore);
                    contents.set(3, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                        boolean change = !kitStats.isEnableGoldTag();
                        gamePlayer.sendMessage("§a你" + (kitStats.isEnableGoldTag() ? "关闭" : "开启") + "了金色职业名牌！");
                        kitStats.setEnableGoldTag(change);
                        contents.inventory().open(player);
                    }));
                } else {
                    itemBuilder = (new ItemBuilder(Material.GOLD_NUGGET)).setDisplayName("§e开启或关闭金色职业名牌");
                    lore.clear();
                    lore.add("§7开启或关闭你的§6[" + classes.getDisplayName() + "]§7职业名牌");
                    lore.add(" ");
                    lore.add("§c需要精通IV！");
                    itemBuilder.setLore((List) lore);
                    contents.set(3, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                        e.getWhoClicked().sendMessage("§c你需要精通IV来使用它！");
                    }));
                }

                contents.set(5, 3, ClickableItem.of((new ItemBuilder(Material.ARROW)).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), (e) -> {
                    builder.getParent().open(player);
                }));
                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.EMERALD)).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), (e) -> {
                }));
            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }
}

