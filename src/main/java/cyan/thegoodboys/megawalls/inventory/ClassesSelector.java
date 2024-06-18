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

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.classes.novice.random.Random;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTimer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class ClassesSelector {
    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("职业选择器");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 1;
                List<Classes> classesList = null;
                if (gamePlayer != null) {
                    if (MegaWalls.isMythicMode()) {
                        classesList = ClassesManager.getClasses();
                    }else {
                        classesList = ClassesManager.sort(gamePlayer.getPlayerStats().unlockedClasses());
                    }
                }

                if (classesList != null) {
                    for (Classes classes : classesList) {
                        if (gamePlayer.getPlayerStats().isUnlocked(classes)) {
                            if (classes instanceof Random) {
                                ItemBuilder itemBuilderx = (new ItemBuilder(classes.getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName() + ClassesSelector.getMasterStar(0));
                                List<String> lorex = new ArrayList<>();
                                lorex.add("§8" + classes.getClassesType().getName() + "职业");
                                lorex.add("§7职业定位:随机");
                                lorex.add("§7难度:随机");
                                lorex.add(" ");
                                lorex.add("§7末影箱:§a§k9§r§a行");
                                lorex.add(" ");
                                if (gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                                    lorex.add("§a已选择！");
                                } else {
                                    lorex.add("§e点击选择！");
                                }

                                itemBuilderx.setLore(lorex);
                                contents.set(5, 5, ClickableItem.of(itemBuilderx.build(), (e) -> {
                                    e.setCancelled(true);
                                    if (gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                                        gamePlayer.sendMessage("§a你已经选择了该职业！");
                                    } else {
                                        gamePlayer.sendMessage("§a你选择了§e" + classes.getDisplayName() + "§a职业！");
                                        gamePlayer.playSound(Sound.CLICK, 1.0F, 1.0F);
                                        gamePlayer.getPlayerStats().setSelected(classes.getName());
                                        gamePlayer.getPlayerStats().setSelectedSkin(classes.getDefaultSkin());
                                        Bukkit.getScheduler().runTaskLaterAsynchronously(MegaWalls.getInstance(), () -> {
                                            ClassesSkin defaultSkin = gamePlayer.getPlayerStats().getSelectedSkin();
                                            PlayerUtils.skinChange((CraftPlayer) player, defaultSkin.getValue(), defaultSkin.getSignature());
                                        }, 1L);
                                    }
                                }));
                            } else {
                                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                                ItemBuilder itemBuilder = (new ItemBuilder(classes.getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName() + ClassesSelector.getMasterStar(kitStats.getLevel()));
                                List<String> lore = new ArrayList<>();
                                lore.add("§8" + classes.getClassesType().getName() + "职业");
                                lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                                lore.add("§7难度:" + classes.getDifficulty().getText());
                                lore.add(" ");
                                lore.add("§e技能 §8- §7" + classes.getMainSkill().getName());
                                lore.addAll(classes.getMainSkill().getInfo(kitStats.getSkillLevel()));
                                lore.add(" ");
                                lore.add("§7升级:" + kitStats.upgradePercent());
                                lore.add("§7末影箱:§a" + kitStats.getEnderChest() + "行");
                                lore.add(" ");
                                if (gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                                    lore.add("§a已选择！");
                                } else {
                                    lore.add("§e点击选择！");
                                }

                                itemBuilder.setLore(lore);
                                contents.set(row, column, ClickableItem.of(itemBuilder.build(), (e) -> {
                                    e.setCancelled(true);
                                    if (MegaWalls.getInstance().getGame().getGameTimer().getTime() <= 5 || MegaWalls.getInstance().getGame().isStarted()) {
                                        gamePlayer.sendMessage("§c不再可用！");
                                        gamePlayer.getPlayer().closeInventory();
                                        return;
                                    }
                                    if (gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                                        gamePlayer.sendMessage("§a你已经选择了该职业！");
                                    } else {
                                        gamePlayer.sendMessage("§a你选择了§e" + classes.getDisplayName() + "§a职业！");
                                        gamePlayer.playSound(Sound.CLICK, 1.0F, 1.0F);
                                        gamePlayer.getPlayerStats().setSelected(classes.getName());
                                        gamePlayer.getPlayerStats().setSelectedSkin(classes.getDefaultSkin());
                                        Bukkit.getScheduler().runTaskLaterAsynchronously(MegaWalls.getInstance(), () -> {
                                            ClassesSkin defaultSkin = gamePlayer.getPlayerStats().getSelectedSkin();
                                            PlayerUtils.skinChange((CraftPlayer) player, defaultSkin.getValue(), defaultSkin.getSignature());
                                        }, 1L);
                                    }

                                }));
                                if (column + 1 > 7) {
                                    ++row;
                                    column = 1;
                                } else {
                                    ++column;
                                }
                            }
                        }
                    }
                }

                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.BARRIER)).setDisplayName("§c关闭").build(), (e) -> {
                    player.closeInventory();
                }));
            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static String getMasterStar(int level) {
        if (level < 2) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer(" §6");

            for (int i = 2; i <= level; ++i) {
                sb.append("✫");
            }

            return sb.toString();
        }
    }
}

