/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.classes.novice.random.Random;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
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

public class ClassesContainer {
    static boolean lt = false;

    public static SmartInventory build() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("职业收集");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.SHOPMENU);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                int row = 1;
                int column = 1;
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                List<Classes> classesList = ClassesManager.sort(ClassesManager.getClasses());
                for (Classes classes : classesList) {
                    if (classes instanceof Random) {

                        ItemBuilder itemBuilder = (new ItemBuilder(classes.getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName() + getMasterStar(0));
                        List<String> lore = new ArrayList<>();
                        lore.add("§8" + classes.getClassesType().getName() + "职业");
                        lore.add("§7职业定位:" + "随机");
                        lore.add("§7难度:" + "随机");
                        lore.add(" ");
                        lore.add("§7末影箱:§a" + "§k9§r§a" + "行");
                        lore.add(" ");
                        if (gamePlayer != null && gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                            lore.add("§a已选择！");
                        }
                        itemBuilder.setLore(lore);
                        contents.set(4, 4, ClickableItem.of(itemBuilder.build(), (e) -> {
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
//                            if (column + 1 > 7) {
//                                ++row;
//                                column = 1;
//                            } else {
//                                ++column;
//                            }
                        continue;
                    }
//                    if (gamePlayer.getPlayerStats().isUnlocked(classes)) {
                    KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                    ItemBuilder itemBuilder = (new ItemBuilder(gamePlayer.getPlayerStats().isUnlocked(classes) ? classes.getIconType() : Material.STAINED_GLASS_PANE, 1, gamePlayer.getPlayerStats().isUnlocked(classes) ? classes.getIconData() : 1)).setDisplayName("§a" + classes.getDisplayName() + ClassesContainer.getMasterStar(0));
                    List<String> lore = new ArrayList();
                    lore.add("§8" + classes.getClassesType().getName() + "职业");
                    lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                    lore.add("§7难度:" + classes.getDifficulty().getText());
                    lore.add(" ");
                    lore.add("§7升级进度:" + kitStats.upgradePercent());
                    lore.add(ClassesContainer.getUpgradeBar((classes.getDisplayName() + "职业套装"), kitStats, kitStats.getEquipLevel(), 5));
                    lore.add(getUpgradeBar(classes.getMainSkill(), kitStats, kitStats.getSkillLevel(), 5));
                    lore.add(getUpgradeBar(classes.getSecondSkill(), kitStats, kitStats.getSkill2Level(), 3));
                    lore.add(getUpgradeBar(classes.getThirdSkill(), kitStats, kitStats.getSkill3Level(), 3));
                    lore.add(getUpgradeBar(classes.getCollectSkill(), kitStats, kitStats.getSkill4Level(), 3));
                    lore.add(" ");
                    lore.add("§7末影箱:§a" + kitStats.getEnderChest() + "行");
                    lore.add(" ");
                    lore.add(gamePlayer.getPlayerStats().isUnlocked(classes) ? "§e点击查看！" : "§a点击解锁");
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
//                    }
                }

                contents.set(5, 3, ClickableItem.of((new ItemBuilder(Material.ARROW)).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), (e) -> {
                    builder.getParent().open(player);
                }));
                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.EMERALD)).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), (e) -> {
                }));
            }

            public void update(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                List<Classes> classesList = ClassesManager.sort(ClassesManager.getClasses());
                for (Classes classes : classesList) {
                    if (classes instanceof Random && !lt) {
                        ItemBuilder itemBuilder = (new ItemBuilder(classesList.get(new java.util.Random().nextInt(classesList.size())).getIconType(), 1, classes.getIconData())).setDisplayName("§a" + classes.getDisplayName() + getMasterStar(0));
                        List<String> lore = new ArrayList<>();
                        lore.add("§8" + classes.getClassesType().getName() + "职业");
                        lore.add("§7职业定位:" + "随机");
                        lore.add("§7难度:" + "随机");
                        lore.add(" ");
                        lore.add("§7末影箱:§a" + "§k9§r§a" + "行");
                        lore.add(" ");
                        if (gamePlayer != null && gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                            lore.add("§a已选择！");
                        }
                        itemBuilder.setLore(lore);
                        contents.set(5, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                            e.setCancelled(true);
                            if (gamePlayer != null && gamePlayer.getPlayerStats().getSelected().equals(classes)) {
                                gamePlayer.sendMessage("§a你已经选择了该职业！");
                            }
                        }));
                        lt = true;
                        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> lt = false, 35);
                    }
                }
            }
        });
        return builder.build();
    }

    public static String getUpgradeBar(Skill skill, KitStatsContainer kitStats, int level, int max) {
        return StringUtils.upgradeBar(level, max) + " §7" + skill.getName();
    }

    public static String getUpgradeBar(String name, KitStatsContainer kitStats, int level, int max) {
        return StringUtils.upgradeBar(level, max) + " §7" + name;
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

