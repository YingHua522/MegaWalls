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

import cyan.thegoodboys.megawalls.game.GamePlayer;
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

public class MythicDustShop {
    public static SmartInventory build() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("神话之尘转换");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.SHOPMENU);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                contents.set(0, 2, ClickableItem.of((new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)).setDisplayName("§e购买神话之尘").build(), (e) -> {
                }));
                contents.set(0, 6, ClickableItem.of((new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)).setDisplayName("§e出售神话之尘").build(), (e) -> {
                }));
                int[] amounts = new int[]{1, 3, 5, 10};

                int i;
                int amount;
                int reward;
                boolean enough;
                ItemBuilder button;
                ArrayList lore;
                for (i = 0; i < 4; ++i) {
                    amount = amounts[i];
                    reward = amount * 32000;
                    enough = gamePlayer.getPlayerStats().getCoins() >= reward;
                    button = (new ItemBuilder(Material.STAINED_GLASS_PANE, amount, (byte) 4)).setDisplayName("§c购买");
                    lore = new ArrayList();
                    lore.add("§7购买§e" + amount + "个神话之尘");
                    lore.add("§7使用§6" + StringUtils.formattedCoins(reward) + "硬币");
                    lore.add(" ");
                    if (enough) {
                        lore.add("§e点击购买！");
                    } else {
                        lore.add("§c硬币不足！");
                    }

                    button.setLore(lore);
                    boolean finalEnough = enough;
                    int finalReward = reward;
                    int finalAmount = amount;
                    contents.set(i + 1, 2, ClickableItem.of(button.build(), (e) -> {
                        if (finalEnough) {
                            gamePlayer.getPlayerStats().takeCoins(finalReward);
                            gamePlayer.getPlayerStats().giveMythicDust(finalAmount);
                            gamePlayer.sendMessage("§a购买成功！");
                            contents.inventory().open(player);
                        } else {
                            gamePlayer.sendMessage("§c硬币不足！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        }

                    }));
                }

                for (i = 0; i < 4; ++i) {
                    amount = amounts[i];
                    reward = amount * 3000;
                    enough = gamePlayer.getPlayerStats().getMythicDust() >= amount;
                    button = (new ItemBuilder(Material.STAINED_GLASS_PANE, amount, (byte) 1)).setDisplayName("§c出售");
                    lore = new ArrayList();
                    lore.add("§7出售§e" + amount + "个神话之尘");
                    lore.add("§7来获取§6" + StringUtils.formattedCoins(reward) + "硬币");
                    lore.add(" ");
                    if (enough) {
                        lore.add("§e点击出售！");
                    } else {
                        lore.add("§c神话之尘不足！");
                    }

                    button.setLore(lore);
                    boolean finalEnough1 = enough;
                    int finalAmount1 = amount;
                    int finalReward1 = reward;
                    contents.set(i + 1, 6, ClickableItem.of(button.build(), (e) -> {
                        if (finalEnough1) {
                            gamePlayer.getPlayerStats().takeMythicDust(finalAmount1);
                            gamePlayer.getPlayerStats().giveCoins(finalReward1);
                            gamePlayer.sendMessage("§a出售成功！");
                            contents.inventory().open(player);
                        } else {
                            gamePlayer.sendMessage("§c神话之尘不足！");
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        }

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