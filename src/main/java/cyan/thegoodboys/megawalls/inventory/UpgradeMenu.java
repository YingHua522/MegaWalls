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

import cyan.thegoodboys.megawalls.classes.*;
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
import java.util.List;

public class UpgradeMenu {
    public static SmartInventory menu(final Classes classes, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(classes.getDisplayName() + "职业");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                if (gamePlayer == null) {
                    return;
                }
                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                if (gamePlayer.getPlayerStats().isUnlocked(classes)) {
                    ItemBuilder itemBuilder = new ItemBuilder(classes.getIconType(), 1, classes.getIconData()).setDisplayName("§a" + classes.getDisplayName());
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("§8" + classes.getClassesType().getName() + "职业");
                    lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                    lore.add("§7难度:" + classes.getDifficulty().getText());
                    itemBuilder.setLore(lore);
                    contents.set(1, 1, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, kitStats.getEquipLevel() == 5 ? (byte) 5 : 4).setDisplayName("§e职业套装 §8- §7" + classes.getDisplayName());
                    lore.clear();
                    lore.addAll(classes.getEquipmentPackage().getInfo(kitStats.getEquipLevel()));
                    lore.add(" ");
                    lore.add("§7等级:§e" + StringUtils.level(kitStats.getEquipLevel()));
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 2, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgrade(classes.getEquipmentPackage(), contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, kitStats.getSkillLevel() == 5 ? (byte) 5 : 4).setDisplayName("§e技能 §8- §7" + classes.getMainSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getMainSkill().getInfo(kitStats.getSkillLevel()));
                    lore.add(" ");
                    lore.add("§7等级:§e" + StringUtils.level(kitStats.getSkillLevel()));
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 3, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgrade(classes.getMainSkill(), contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, kitStats.getSkill2Level() == 3 ? (byte) 5 : 4).setDisplayName("§e被动技能 §8- §7" + classes.getSecondSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getSecondSkill().getInfo(kitStats.getSkill2Level()));
                    lore.add(" ");
                    lore.add("§7等级:§e" + StringUtils.level(kitStats.getSkill2Level()));
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 4, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgrade(classes.getSecondSkill(), contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, kitStats.getSkill3Level() == 3 ? (byte) 5 : 4).setDisplayName("§e被动技能 §8- §7" + classes.getThirdSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getThirdSkill().getInfo(kitStats.getSkill3Level()));
                    lore.add(" ");
                    lore.add("§7等级:§e" + StringUtils.level(kitStats.getSkill3Level()));
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 5, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgrade(classes.getThirdSkill(), contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, kitStats.getSkill4Level() == 3 ? (byte) 5 : 4).setDisplayName("§e采集 §8- §7" + classes.getCollectSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getCollectSkill().getInfo(kitStats.getSkill4Level()));
                    lore.add(" ");
                    lore.add("§7等级:§e" + StringUtils.level(kitStats.getSkill4Level()));
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 6, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgrade(classes.getCollectSkill(), contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§e精通 §8- §7" + classes.getDisplayName());
                    lore.clear();
                    lore.add("§7当职业等级提升到最高,");
                    lore.add("§7精通升级能有更多的挑战和奖励");
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(1, 7, ClickableItem.of(itemBuilder.build(), e -> MasterMenu.master(classes, contents.inventory()).open(player)));
                    itemBuilder = new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§a" + classes.getDisplayName() + "Ender Chest");
                    lore.clear();
                    lore.add("§7游戏中,Ender Chest可以帮你");
                    lore.add("§7储存物品。");
                    lore.add(" ");
                    lore.add("§7行数:§a" + kitStats.getEnderChest() + "行");
                    lore.add(" ");
                    lore.add("§e点击查看升级！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 3, ClickableItem.of(itemBuilder.build(), e -> UpgradeMenu.upgradeEnderChest(classes, contents.inventory()).open(player)));

                    itemBuilder = (new ItemBuilder(Material.ANVIL)).setDisplayName("§a编辑" + classes.getDisplayName() + "的物品位置");
                    lore.clear();
                    lore.add("§7单击自定义你的" + classes.getDisplayName());
                    lore.add("§7职业物品位置");
                    lore.add(" ");
                    lore.add("§e点击打开！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 4, ClickableItem.of(itemBuilder.build(), (e) -> {
                        EditInventoryMenu.edit(classes, contents.inventory()).open(player);
                    }));

                    itemBuilder = new ItemBuilder(Material.PAPER).setDisplayName("§a" + classes.getDisplayName() + "统计数据");
                    lore.clear();
                    lore.add("§7胜场数: §a" + gamePlayer.getPlayerStats().getKitStats(classes).getWins());
                    lore.add("§7MVP: §a" + gamePlayer.getPlayerStats().getKitStats(classes).getMvps());
                    lore.add("§7最终击杀: §a" + gamePlayer.getPlayerStats().getKitStats(classes).getFinalKills());
                    lore.add("§7最终助攻: §a" + gamePlayer.getPlayerStats().getKitStats(classes).getFinalAssists());
                    lore.add("§7游戏时间: §a" + StringUtils.formatLongTime(gamePlayer.getPlayerStats().getKitStats(classes).getPlayTime()));
                    itemBuilder.setLore(lore);
                    contents.set(2, 5, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    boolean selected = gamePlayer.getPlayerStats().getSelected().equals(classes);
                    itemBuilder = new ItemBuilder(Material.INK_SACK, 1, selected ? (byte) 10 : 8).setDisplayName("§a" + (selected ? "已选择该职业！" : "选择该职业"));
                    lore.clear();
                    if (!selected) {
                        lore.add("§7已选择:§a" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                        lore.add(" ");
                        lore.add("§e点击选择！");
                    }
                    itemBuilder.setLore(lore);
                    contents.set(3, 4, ClickableItem.of(itemBuilder.build(), e -> {
                        if (selected) {
                            gamePlayer.sendMessage("§c你已经选择了§e" + classes.getDisplayName() + "§c职业！");
                            return;
                        }
                        gamePlayer.sendMessage("§a你选择了§e" + classes.getDisplayName() + "§a职业！");
                        gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                        gamePlayer.getPlayerStats().setSelected(classes.getName());
                        ItemBuilder buttonBuilder = new ItemBuilder(Material.INK_SACK, 1, (byte) 10).setDisplayName("§a已选择该职业！");
                        e.setCurrentItem(buttonBuilder.build());
                        contents.inventory().open(player);
                    }));
                } else {
                    ItemBuilder itemBuilder = new ItemBuilder(classes.getIconType(), 1, classes.getIconData()).setDisplayName("§a" + classes.getDisplayName());
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add("§8" + classes.getClassesType().getName() + "职业");
                    lore.add("§7职业定位:" + classes.getOrientations()[0].getText() + " " + classes.getOrientations()[1].getText());
                    lore.add("§7难度:" + classes.getDifficulty().getText());
                    itemBuilder.setLore(lore);
                    contents.set(1, 1, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4).setDisplayName("§e职业套装预览 §8- §7" + classes.getDisplayName());
                    lore.clear();
                    lore.add(" ");
                    lore.add("§7§o预览展示的是完全");
                    lore.add("§7§o升级的职业套装。");
                    itemBuilder.setLore(lore);
                    contents.set(1, 2, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4).setDisplayName("§e技能预览 §8- §7" + classes.getMainSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getMainSkill().getInfo(5));
                    lore.add(" ");
                    lore.add("§7§o预览展示的是");
                    lore.add("§7§o完全升级的技能。");
                    itemBuilder.setLore(lore);
                    contents.set(1, 3, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4).setDisplayName("§e被动技能预览 §8- §7" + classes.getSecondSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getSecondSkill().getInfo(3));
                    lore.add(" ");
                    lore.add("§7§o预览展示的是");
                    lore.add("§7§o完全升级的技能。");
                    itemBuilder.setLore(lore);
                    contents.set(1, 4, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4).setDisplayName("§e被动技能预览 §8- §7" + classes.getThirdSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getThirdSkill().getInfo(3));
                    lore.add(" ");
                    lore.add("§7§o预览展示的是");
                    lore.add("§7§o完全升级的技能。");
                    itemBuilder.setLore(lore);
                    contents.set(1, 5, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4).setDisplayName("§e采集预览 §8- §7" + classes.getCollectSkill().getName());
                    lore.clear();
                    lore.addAll(classes.getCollectSkill().getInfo(3));
                    lore.add(" ");
                    lore.add("§7§o预览展示的是");
                    lore.add("§7§o完全升级的技能。");
                    itemBuilder.setLore(lore);
                    contents.set(1, 6, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    itemBuilder = new ItemBuilder(Material.EMPTY_MAP).setDisplayName("§a这个职业适合谁？");
                    lore.clear();
                    lore.add("§e符合下列情况就解锁吧");
                    lore.add("§8+");
                    lore.addAll(classes.getInfo());
                    itemBuilder.setLore(lore);
                    contents.set(1, 7, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                    if (classes.getClassesType() == ClassesType.NORMAL) {
                        boolean enough = gamePlayer.getPlayerStats().getCoins() >= classes.unlockCost();
                        itemBuilder = new ItemBuilder(Material.STAINED_CLAY, 1, enough ? (byte) 13 : 14).setDisplayName((enough ? "§e" : "§c") + "解锁" + classes.getDisplayName());
                        lore.clear();
                        lore.add("§7将此职业加入你的职业收集,");
                        lore.add("§7以在游戏内使用。");
                        lore.add(" ");
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(classes.unlockCost()));
                        if (enough) {
                            lore.add("§e点击解锁！");
                        } else {
                            lore.add("§c硬币不足！");
                        }
                        itemBuilder.setLore(lore);
                        contents.set(3, 3, ClickableItem.of(itemBuilder.build(), e -> {
                            if (!enough) {
                                gamePlayer.sendMessage("§c硬币不足！");
                                gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                return;
                            }
                            ConfirmMenu.build("解锁" + classes.getDisplayName(), e.getCurrentItem(), contents.inventory(), event -> {
                                gamePlayer.sendMessage("§a你解锁了§e" + classes.getDisplayName() + "§a职业！");
                                gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
                                gamePlayer.getPlayerStats().takeCoins(classes.unlockCost());
                                kitStats.addLevel();
                                builder.getParent().open(player);
                            }).open(player);
                        }));
                    } else if (classes.getClassesType() == ClassesType.MYTHIC) {
                        boolean enough = gamePlayer.getPlayerStats().getMythicDust() >= classes.unlockCost();
                        itemBuilder = new ItemBuilder(Material.STAINED_CLAY, 1, enough ? (byte) 13 : 14).setDisplayName((enough ? "§e" : "§c") + "解锁" + classes.getDisplayName());
                        lore.clear();
                        lore.add("§7将此职业加入你的职业收集,");
                        lore.add("§7以在游戏内使用。");
                        lore.add(" ");
                        lore.add("§7花费:§e" + classes.unlockCost() + "个神话之尘");
                        if (enough) {
                            lore.add("§e点击解锁！");
                        } else {
                            lore.add("§c神话之尘不足！");
                        }
                        itemBuilder.setLore(lore);
                        contents.set(3, 3, ClickableItem.of(itemBuilder.build(), e -> {
                            if (!enough) {
                                gamePlayer.sendMessage("§c神话之尘不足！");
                                gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                return;
                            }
                            ConfirmMenu.build("解锁" + classes.getDisplayName(), e.getCurrentItem(), contents.inventory(), event -> {
                                gamePlayer.sendMessage("§a你解锁了§e" + classes.getDisplayName() + "§a职业！");
                                gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
                                gamePlayer.getPlayerStats().takeMythicDust(classes.unlockCost());
                                kitStats.addLevel();
                                builder.getParent().open(player);
                            }).open(player);
                        }));
                    }

                    itemBuilder = new ItemBuilder(Material.PAPER).setDisplayName("§a升级信息");
                    lore.clear();
                    lore.add("§7当你解锁此职业之后,");
                    lore.add("§7别忘了升级它哦！");
                    lore.add(" ");
                    if (classes.getClassesType() == ClassesType.NORMAL) {
                        lore.add("§7你应该先升级职业套装,");
                        lore.add("§7接着升级主技能。");
                    } else if (classes.getClassesType() == ClassesType.MYTHIC) {
                        lore.add("§7" + classes.getDisplayName() + "是神话职业,因此你需要使用");
                        lore.add("§7该职业获得最终击杀和胜场来升级它。");
                    }
                    lore.add(" ");
                    lore.add("§e满级价格");
                    lore.add("§7" + classes.getDisplayName() + "职业套装:§6" + StringUtils.formattedCoins(7800));
                    lore.add("§7" + classes.getMainSkill().getName() + ":§6" + StringUtils.formattedCoins(6500));
                    lore.add("§7" + classes.getSecondSkill().getName() + ":§6" + StringUtils.formattedCoins(4500));
                    lore.add("§7" + classes.getThirdSkill().getName() + ":§6" + StringUtils.formattedCoins(4500));
                    lore.add("§7" + classes.getCollectSkill().getName() + ":§6" + StringUtils.formattedCoins(4500));
                    itemBuilder.setLore(lore);
                    contents.set(3, 5, ClickableItem.of(itemBuilder.build(), e -> {
                    }));
                }
                contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), e -> builder.getParent().open(player)));
                contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), e -> {
                }));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static int getMythic(int level, Upgradeable upgradeable) {
        if (upgradeable.maxedLevel() == 5) {
            return 5;
        }else {
            return 2;
        }
    }


    /*
    public static void maxOutUpgradeable(Upgradeable upgradeable, GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
        int cost = getMythic(kitStats.getSkillLevel(),upgradeable);
        Classes classes = ClassesManager.getSelected(gamePlayer);

        // 检查玩家是否已经满级
        if (upgradeable.getPlayerLevel(gamePlayer) >= upgradeable.maxedLevel()) {
            gamePlayer.sendMessage("§c你的 " + upgradeable.getName() + " 已经满级，无法再进行升级！");
            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }

        // 获取玩家神话之尘是否足够该神话倾注物品
        if (gamePlayer.getPlayerStats().getMythicDust() < cost) {
            gamePlayer.sendMessage("§c神话之尘不足！需要" + cost + "个神话之尘！");
            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }

        if (classes.getClassesType() != ClassesType.MYTHIC) {
            gamePlayer.sendMessage("§c你的职业不是神话职业，无法进行升级！");
            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }
        // 减去玩家的神话之尘数量
        gamePlayer.getPlayerStats().takeMythicDust(cost);

        if (upgradeable instanceof EquipmentPackage) {
            kitStats.setEquipLevel(upgradeable.maxedLevel());
            kitStats.addEquipLevel();
        } else if (upgradeable instanceof Skill) {
            Skill skill = (Skill) upgradeable;
            if (skill.equals(classes.getMainSkill())) {
                kitStats.setSkillLevel(upgradeable.maxedLevel());
                kitStats.addSkillLevel();
            } else if (skill.equals(classes.getSecondSkill())) {
                kitStats.setSkill2Level(upgradeable.maxedLevel());
                kitStats.addSkill2Level();
            } else if (skill.equals(classes.getThirdSkill())) {
                kitStats.setSkill3Level(upgradeable.maxedLevel());
                kitStats.addSkill3Level();
            } else if (skill.equals(classes.getCollectSkill())) {
                kitStats.setSkill4Level(upgradeable.maxedLevel());
                kitStats.addSkill4Level();
            }
        }
        gamePlayer.sendMessage("§a你的 " + upgradeable.getName() + " 已满级！");
        gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
    }

     */


    public static SmartInventory upgrade(final Upgradeable upgradeable, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(upgradeable.getName() + "升级");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                if (gamePlayer == null) return;
                ItemBuilder itemBuilder = new ItemBuilder(upgradeable.getIconType(), 1, upgradeable.getIconData()).setDisplayName("§a" + upgradeable.getName() + "升级I");
                ArrayList<String> lore = new ArrayList<>(upgradeable.getInfo(1));
                lore.add(" ");
                lore.add("§a已解锁");
                itemBuilder.setLore(lore);
                contents.set(1, upgradeable.maxedLevel() == 5 ? 2 : 3, ClickableItem.of(itemBuilder.build(), e -> {
                }));
                int i = 2;
                while (i <= 5 && i <= upgradeable.maxedLevel()) {
                    int slot;
                    boolean upgraded = upgradeable.getPlayerLevel(gamePlayer) >= i;
                    boolean lastUpgraded = upgradeable.getPlayerLevel(gamePlayer) >= i - 1;
                    int coins = upgradeable.getCost(i);
                    boolean enoughCoins = gamePlayer.getPlayerStats().getCoins() >= coins;
                    slot = upgradeable.maxedLevel() == 5 ? i + 1 : i + 2;
                    if (i == upgradeable.maxedLevel()) {
                        itemBuilder = new ItemBuilder(Material.STAINED_GLASS, 1, upgraded ? (byte) 5 : 14);
                        if (upgraded) {
                            itemBuilder.setDisplayName("§a最高" + upgradeable.getName() + "升级");
                            itemBuilder.setDurability((short) 5);
                            itemBuilder.addGlow();
                        } else if (!lastUpgraded || !enoughCoins) {
                            itemBuilder.setDisplayName("§c最高" + upgradeable.getName() + "升级");
                            itemBuilder.setDurability((short) 14);
                        } else {
                            itemBuilder.setDisplayName("§a最高" + upgradeable.getName() + "升级");
                            itemBuilder.setDurability((short) 4);
                        }
                    } else {
                        itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE);
                        if (upgraded) {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 5);
                        } else if (!lastUpgraded || !enoughCoins) {
                            itemBuilder.setDisplayName("§c升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 14);
                        } else {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 4);
                        }
                    }
                    itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                    lore.clear();
                    lore.addAll(upgradeable.getInfo(i));
                    lore.add(" ");
                    if (upgraded) {
                        lore.add("§a已解锁");
                    } else if (!lastUpgraded) {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§c你需要先升级" + StringUtils.level(i - 1) + "！");
                    } else if (!enoughCoins) {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§c你没有足够的硬币购买此物品！");
                    } else {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§e点击升级！");
                    }
                    itemBuilder.setLore(lore);
                    int num = i++;
                    contents.set(1, slot, ClickableItem.of(itemBuilder.build(), e -> {
                        if (upgraded) {
                            gamePlayer.sendMessage("§c你已经解锁这一等级！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        if (!lastUpgraded) {
                            gamePlayer.sendMessage("§c你需要先升级" + StringUtils.level(num - 1) + "！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        if (!enoughCoins) {
                            gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build(upgradeable.getName() + "升级", e.getCurrentItem(), inventory, event -> {
                            gamePlayer.sendMessage("§a升级成功！");
                            gamePlayer.playSound(Sound.NOTE_PLING, 1.0f, 3.0f);
                            gamePlayer.getPlayerStats().takeCoins(coins);
                            upgradeable.upgrade(gamePlayer);
                            inventory.open(player);
                        }).open(player);
                    }));
                }
                /*
                Classes classes = ClassesManager.getSelected(gamePlayer);
                if (classes.getClassesType() == ClassesType.MYTHIC) {
                    itemBuilder = new ItemBuilder(Material.DIAMOND).setDisplayName("§a神话倾注");
                    lore.clear();
                    lore.add("§7将你的职业升级到最高等级。");
                    lore.add(" ");
                    lore.add("§e点击升级！");
                    itemBuilder.setLore(lore);
                    contents.set(5,5, ClickableItem.of(itemBuilder.build(), e -> {
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build(upgradeable.getName() + "神话倾注", e.getCurrentItem(), inventory, event -> {
                            UpgradeMenu.maxOutUpgradeable(upgradeable, gamePlayer);
                            inventory.open(player);
                        }).open(player);
                    }));
                }

                 */
                contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), e -> builder.getParent().open(player)));
                contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), e -> {
                }));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static SmartInventory upgradeEnderChest(final Classes classes, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("末影箱升级");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                KitStatsContainer kitStats;
                if (gamePlayer != null) {
                    kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                } else {
                    kitStats = null;
                }
                ItemBuilder itemBuilder = (new ItemBuilder(Material.ENDER_CHEST)).setDisplayName("§a" + classes.getDisplayName() + "末影箱");
                List<String> lore = new ArrayList<>();
                lore.add("§7游戏中,末影箱可以帮你");
                lore.add("§7储存物品。");
                lore.add(" ");
                lore.add("§7行数:§a3行");
                itemBuilder.setLore(lore);
                contents.set(1, 3, ClickableItem.of(itemBuilder.build(), (e) -> {
                }));

                for (int i = 4; i <= 5; ++i) {
                    boolean upgraded;
                    if (kitStats != null) {
                        upgraded = kitStats.getEnderChest() >= i;
                    } else {
                        upgraded = false;
                    }
                    boolean lastUpgraded;
                    if (kitStats != null) {
                        lastUpgraded = kitStats.getEnderChest() >= i - 1;
                    } else {
                        lastUpgraded = false;
                    }
                    int coins = i == 4 ? 10000 : 25000;
                    boolean enoughCoins;
                    if (gamePlayer != null) {
                        enoughCoins = gamePlayer.getPlayerStats().getCoins() >= coins;
                    } else {
                        enoughCoins = false;
                    }
                    if (i == 4) {
                        itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE);
                        if (upgraded) {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 5);
                        } else if (lastUpgraded && enoughCoins) {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 4);
                        } else {
                            itemBuilder.setDisplayName("§c升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 14);
                        }
                    } else {
                        itemBuilder = new ItemBuilder(Material.STAINED_GLASS, 1, (byte) (upgraded ? 5 : 14));
                        if (upgraded) {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 5);
                            itemBuilder.addGlow();
                        } else if (lastUpgraded && enoughCoins) {
                            itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 4);
                        } else {
                            itemBuilder.setDisplayName("§c升级" + StringUtils.level(i));
                            itemBuilder.setDurability((short) 14);
                        }
                    }

                    itemBuilder.setDisplayName("§a升级" + StringUtils.level(i));
                    lore.clear();
                    lore.add("§7行数: §8" + (i - 1) + " ➜ §a" + i);
                    lore.add(" ");
                    if (upgraded) {
                        lore.add("§a已解锁");
                    } else if (!lastUpgraded) {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§c请先把行数升级到" + (i - 1) + "行！");
                    } else if (!enoughCoins) {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§c你没有足够的硬币购买此物品！");
                    } else {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(coins));
                        lore.add(" ");
                        lore.add("§e点击解锁！");
                    }
                    itemBuilder.setLore(lore);
                    int num = i;
                    contents.set(1, i, ClickableItem.of(itemBuilder.build(), e -> {
                        if (upgraded) {
                            gamePlayer.sendMessage("§c你已经解锁这一等级！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        if (!lastUpgraded) {
                            if (gamePlayer != null) {
                                gamePlayer.sendMessage("§c请先把行数升级到" + (num - 1) + "行！");
                            }
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        if (!enoughCoins) {
                            gamePlayer.sendMessage("§c你没有足够的硬币购买此物品！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return;
                        }
                        SmartInventory inventory = contents.inventory();
                        ConfirmMenu.build("Ender Chest升级", e.getCurrentItem(), inventory, event -> {
                            gamePlayer.sendMessage("§a升级成功！");
                            gamePlayer.playSound(Sound.NOTE_PLING, 1.0f, 3.0f);
                            gamePlayer.getPlayerStats().takeCoins(coins);
                            kitStats.addEnderChest();
                            inventory.open(player);
                        }).open(player);
                    }));
                }
                contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), e -> builder.getParent().open(player)));
                contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), e -> {
                }));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }
}

