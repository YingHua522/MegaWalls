/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UnlockClassesMenu {
    public static SmartInventory buildNormal() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("解锁普通职业");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.SHOPMENU);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 1;
                List<Classes> classesList = ClassesManager.sort(ClassesManager.getNormalClasses());

                for (Classes classes : classesList) {
                    if (gamePlayer != null && !gamePlayer.getPlayerStats().isUnlocked(classes)) {
                        ItemBuilder itemBuilder = (new ItemBuilder(classes.getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName());
                        List<String> lore = new ArrayList<>();
                        lore.add("§8" + classes.getClassesType().getName() + "职业");
                        lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                        lore.add("§7难度:" + classes.getDifficulty().getText());
                        lore.add(" ");
                        lore.add("§e技能 §8- §7" + classes.getMainSkill().getName());
                        lore.addAll(classes.getMainSkill().getInfo(1));
                        lore.add(" ");
                        lore.add("§7解锁花费:§6" + classes.unlockCost());
                        lore.add(" ");
                        lore.add("§e点击查看更多！");
                        itemBuilder.setLore(lore);
                        contents.set(row, column, ClickableItem.of(itemBuilder.build(), (e) -> {
                            UpgradeMenu.menu(classes, contents.inventory()).open(player);
                        }));
                        if (column + 1 > 7) {
                            ++row;
                            column = 1;
                        } else {
                            ++column;
                        }
                    }
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

    public static SmartInventory buildMythic() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("解锁神话职业");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.SHOPMENU);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 1;
                List<Classes> classesList = ClassesManager.sort(ClassesManager.getMythicClasses());

                for (Classes classes : classesList) {
                    if (!gamePlayer.getPlayerStats().isUnlocked(classes)) {
                        ItemBuilder itemBuilder = (new ItemBuilder(classes.getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName());
                        List<String> lore = new ArrayList<>();
                        lore.add("§8" + classes.getClassesType().getName() + "职业");
                        lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                        lore.add("§7难度:" + classes.getDifficulty().getText());
                        lore.add(" ");
                        lore.add("§e技能 §8- §7" + classes.getMainSkill().getName());
                        lore.addAll(classes.getMainSkill().getInfo(1));
                        lore.add(" ");
                        lore.add("§7解锁花费:§e" + classes.unlockCost() + "个神话之尘");
                        lore.add(" ");
                        lore.add("§e点击查看更多！");
                        itemBuilder.setLore(lore);
                        contents.set(row, column, ClickableItem.of(itemBuilder.build(), (e) -> {
                            UpgradeMenu.menu(classes, contents.inventory()).open(player);
                        }));
                        if (column + 1 > 7) {
                            ++row;
                            column = 1;
                        } else {
                            ++column;
                        }
                    }
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

