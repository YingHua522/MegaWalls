/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class ClassesBan {
    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("Classes Selector");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(final Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 1;
                int row2 = 4;
                int column2 = 1;
                List<Classes> classesList = ClassesManager.sort(ClassesManager.getNormalClasses());
                List<Classes> classesList2 = ClassesManager.sort(ClassesManager.getMythicClasses());
                for (final Classes classes : classesList) {
                    if (ClassesManager.isBanned(classes)) continue;
                    KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                    ItemBuilder itemBuilder = new ItemBuilder(classes.getIconType(), 1, classes.getIconData()).setDisplayName("§a" + classes.getDisplayName() + ClassesBan.getMasterStar(kitStats.getLevel()));
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add("§8" + classes.getClassesType().getName() + " Class");
                    lore.add("§7play Style:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                    lore.add("§7Difficulty:" + classes.getDifficulty().getText());
                    lore.add(" ");
                    lore.add("§eSkill §8- §7" + classes.getMainSkill().getName());
                    lore.add(" ");
                    lore.add("§7Upgrades progress:" + kitStats.upgradePercent());
                    lore.add("§7Ender Chest:§a" + kitStats.getEnderChest() + " rows");
                    lore.add(" ");
                    lore.add("§eClick to ban!");
                    itemBuilder.setLore(lore);
                    contents.set(row, column, ClickableItem.of(itemBuilder.build(), e -> {
                        e.setCancelled(true);
                        GamePlayer.getOnlinePlayers().forEach(viewer -> viewer.sendMessage(gamePlayer.getDisplayName((GamePlayer) viewer) + " §c禁用了 §e" + classes.getDisplayName()));
                        gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                        ClassesManager.removeClasses(classes);
                        contents.inventory().close(player);
                        player.getInventory().remove(Material.GOLD_SWORD);
                        GamePlayer.getOnlinePlayers().forEach(viewer -> contents.inventory().close(viewer.getPlayer()));

                    }));
                    if (column + 1 > 7) {
                        ++row;
                        column = 1;
                        continue;
                    }
                    ++column;
                }
                for (final Classes classes : classesList2) {
                    if (ClassesManager.isBanned(classes)) continue;
                    KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                    ItemBuilder itemBuilder = new ItemBuilder(classes.getIconType(), 1, classes.getIconData()).setDisplayName("§a" + classes.getDisplayName() + ClassesBan.getMasterStar(kitStats.getLevel()));
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add("§8" + classes.getClassesType().getName() + " Class");
                    lore.add("§7play Style:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                    lore.add("§7Difficulty:" + classes.getDifficulty().getText());
                    lore.add(" ");
                    lore.add("§eSkill §8- §7" + classes.getMainSkill().getName());
                    lore.add(" ");
                    lore.add("§7Upgrades progress:" + kitStats.upgradePercent());
                    lore.add("§7Ender Chest:§a" + kitStats.getEnderChest() + " rows");
                    lore.add(" ");
                    lore.add("§eClick to ban!");
                    itemBuilder.setLore(lore);
                    contents.set(row2, column2, ClickableItem.of(itemBuilder.build(), e -> {
                        e.setCancelled(true);
                        GamePlayer.getOnlinePlayers().forEach(viewer -> viewer.sendMessage(gamePlayer.getDisplayName((GamePlayer) viewer) + " §c禁用了 §e" + classes.getDisplayName()));
                        gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                        ClassesManager.removeClasses(classes);
                        contents.inventory().close(player);
                        player.getInventory().remove(Material.GOLD_SWORD);
                        GamePlayer.getOnlinePlayers().forEach(viewer -> contents.inventory().close(viewer.getPlayer()));

                    }));
                    if (column2 + 1 > 7) {
                        ++row2;
                        column2 = 1;
                        continue;
                    }
                    ++column2;
                }
                contents.set(1, 0, ClickableItem.of(new ItemBuilder(Material.PAPER, 1, (byte) 0).setDisplayName("§aNormal:").build(), e -> {
                }));
                contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.PAPER, 1, (byte) 0).setDisplayName("§aMythic:").build(), e -> {
                }));
                ArrayList<String> lore = new ArrayList<String>();
                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
                ItemBuilder itemBuilder = new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§ePrestige §8- §7" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                lore.clear();
                lore.add("§7Prestige Upgrades provide further challenges and");
                lore.add("§7rewards once class is maxed out");
                lore.add(" ");
                lore.add("§eClick to view upgrades!");
                itemBuilder.setLore(lore);
                contents.set(5, 8, ClickableItem.of(itemBuilder.build(), e -> {
                    MasterMenu.master(gamePlayer.getPlayerStats().getSelected(), contents.inventory()).open(player);
                }));
                itemBuilder.setLore(lore);
                ItemBuilder itemBuilder1 = new ItemBuilder(Material.GLASS).setDisplayName("§eColor Blind Menu");
                lore.clear();
                lore.add("§7The Color Blind Menu provides a variety of options");
                lore.add("§7for changing how teams appear in game to assist");
                lore.add("§7with visibility");
                lore.add(" ");
                lore.add("§eClick to browse!");
                itemBuilder1.setLore(lore);
                contents.set(4, 8, ClickableItem.of(itemBuilder1.build(), e -> DisplaySettingsMenu.displaysettings(contents.inventory()).open(player)));
                itemBuilder1.setLore(lore);
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static String getMasterStar(int level) {
        if (level >= 2) {
            StringBuffer sb = new StringBuffer(" §6");
            for (int i = 2; i <= level; ++i) {
                sb.append("✫");
            }
            return sb.toString();
        }
        return "";
    }
}

