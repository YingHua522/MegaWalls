/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
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
import java.util.List;

public class ShopMenu {
    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("超级战墙商店");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                ItemBuilder itemBuilder = (new ItemBuilder(Material.CHEST)).setDisplayName("§a职业收集");
                List<String> lore = new ArrayList();
                lore.add("§7查看并升级你已经解锁的职业。");
                lore.add(" ");
                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().usuallyUsedClasses().getClasses());
                lore.add("§7最常使用:§a" + gamePlayer.getPlayerStats().usuallyUsedClasses().getClasses().getDisplayName() + "§8(" + StringUtils.formatLongTime(kitStats.getPlayTime()) + ")");
                lore.add("§7已选择:§a" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                lore.add(" ");
                lore.add("§7已解锁:§a" + gamePlayer.getPlayerStats().unlockedClasses().size() + "§7/§a25 §8(" + StringUtils.percent((double) gamePlayer.getPlayerStats().unlockedClasses().size() / 29.0D) + ")");
                lore.add("§7已满级:§6" + gamePlayer.getPlayerStats().maxedClasses().size() + "§7/§625 §8(" + StringUtils.percent((double) gamePlayer.getPlayerStats().maxedClasses().size() / 19.0D) + ")");
                lore.add("§7已精通:§e" + gamePlayer.getPlayerStats().masterClasses() + "§7/§e48 §8(" + StringUtils.percent((double) gamePlayer.getPlayerStats().masterClasses() / 48.0D) + ")");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore(lore);
                contents.set(1, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                    e.setCancelled(true);
                    InventoryManager.CLASSESCONTAINER.open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.IRON_SWORD)).setDisplayName("§a购买普通职业");
                lore.clear();
                lore.add("§7在职业收集预览和解锁新职业。");
                lore.add(" ");
                lore.add("§7这些职业需要§6硬币");
                lore.add("§7来购买和升级。");
                lore.add(" ");
                lore.add("§7可用:§a" + (13 - gamePlayer.getPlayerStats().unlockedNormalClasses().size()) + "个职业");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore(lore);
                contents.set(1, 4, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (gamePlayer.getPlayerStats().unlockedNormalClasses().size() == 13) {
                        player.sendMessage("§a你自己购买了所有该类职业！恭喜！");
                        player.sendMessage("§a在职业收集里查看你的职业。");
                    } else {
                        InventoryManager.UNLOCKCLASSES_NORMAL.open(player);
                    }

                }));
                itemBuilder = (new ItemBuilder(Material.GOLD_SWORD)).setDisplayName("§a购买神话职业");
                lore.clear();
                lore.add("§7使用§e神话之尘§7来解锁这些职业。");
                lore.add(" ");
                lore.add("§7神话职业升级也拥有");
                lore.add("§7最终淘汰和胜利要求。");
                lore.add(" ");
                lore.add("§7可用:§a" + (7 - gamePlayer.getPlayerStats().unlockedMythicClasses().size()) + "个职业");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore(lore);
                contents.set(1, 7, ClickableItem.of(itemBuilder.build(), (e) -> {
                    if (gamePlayer.getPlayerStats().unlockedMythicClasses().size() == 13) {
                        player.sendMessage("§a你自己购买了所有该类职业！恭喜！");
                        player.sendMessage("§a在职业收集里查看你的职业。");
                    } else {
                        InventoryManager.UNLOCKCLASSES_MYTHIC.open(player);
                    }

                }));
                itemBuilder = (new ItemBuilder(Material.STAINED_GLASS, 1, (byte) 5)).setDisplayName("§a色盲菜单");
                lore.clear();
                lore.add("§7色盲菜单提供各式各样的选项,");
                lore.add("§7这些选项可以改变队伍在游戏");
                lore.add("§7里出现的方式,以帮助玩家更");
                lore.add("§7好地体验游戏。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(3, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                    DisplaySettingsMenu.displaysettings(contents.inventory()).open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.ANVIL)).setDisplayName("§a杂项升级");
                lore.clear();
                lore.add("§7可以解锁或者升级的加成和特性。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(3, 4, ClickableItem.of(itemBuilder.build(), (e) -> {
                    InventoryManager.EFFECTMENU.open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.GLOWSTONE_DUST)).setDisplayName("§a神话之尘转换");
                lore.clear();
                lore.add("§7转换§6硬币§7为§e神话之尘§7,反之亦然。");
                lore.add(" ");
                lore.add("§7你还可以在任务达人领取");
                lore.add("§7信仰任务来获得神话之尘。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(3, 7, ClickableItem.of(itemBuilder.build(), (e) -> {
                    InventoryManager.MYTHICDUSTSHOP.open(player);
                }));
                contents.set(5, 3, ClickableItem.of((new ItemBuilder(Material.BARRIER)).setDisplayName("§c关闭").build(), (e) -> {
                    player.closeInventory();
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

