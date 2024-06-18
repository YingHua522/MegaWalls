/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.effect.EffectManager;
import cyan.thegoodboys.megawalls.effect.HologramEffect;
import cyan.thegoodboys.megawalls.effect.KillMessage;
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

import java.util.ArrayList;

public class EffectMenu {
    public static SmartInventory build() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("杂项升级");
        builder.size(4, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.SHOPMENU);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                boolean have = gamePlayer.getPlayerStats().getEffectStats().isSurface();
                boolean enough = gamePlayer.getPlayerStats().getCoins() >= 25000;
                ItemBuilder itemBuilder = new ItemBuilder(Material.GRASS).setDisplayName("§c地面");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("§7高墙倒塌前,输入§f/surface§7指令。");
                lore.add("§7该指令能把你传送回地面。");
                lore.add(" ");
                lore.add("§7花费:§6" + StringUtils.formattedCoins(25000));
                lore.add(" ");
                if (have) {
                    lore.add("§a已解锁");
                } else if (enough) {
                    lore.add("§e点击购买！");
                } else {
                    lore.add("§c硬币不足！");
                }
                itemBuilder.setLore(lore);
                contents.set(1, 2, ClickableItem.of(itemBuilder.build(), e -> {
                    e.setCancelled(true);
                    if (have) {
                        return;
                    }
                    if (enough) {
                        gamePlayer.getPlayerStats().takeCoins(25000);
                        gamePlayer.getPlayerStats().getEffectStats().updateSurface(true);
                        gamePlayer.sendMessage("§a购买成功！");
                        contents.inventory().open(player);
                        return;
                    }
                    gamePlayer.sendMessage("§c硬币不足！");
                    gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }));
                itemBuilder = new ItemBuilder(Material.SIGN).setDisplayName("§a全息图");
                lore.clear();
                lore.add("§7所有的凋零死亡后,每击");
                lore.add("§7杀一个敌人都会在其坟墓");
                lore.add("§7上放置一张全息图。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore(lore);
                contents.set(1, 4, ClickableItem.of(itemBuilder.build(), e -> InventoryManager.EFFECTMENU_HOLOGRAMEFFECT.open(player)));
                itemBuilder = new ItemBuilder(Material.PAPER).setDisplayName("§a击杀信息");
                lore.clear();
                lore.add("§7个性化设置聊天窗口中你");
                lore.add("§7的击杀数显示。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore(lore);
                contents.set(1, 6, ClickableItem.of(itemBuilder.build(), e -> InventoryManager.EFFECTMENU_KILLMESSAGE.open(player)));
                contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a返回").setLore("§7至" + builder.getParent().getTitle()).build(), e -> builder.getParent().open(player)));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static SmartInventory buildHologramEffect() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("全息图");
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.EFFECTMENU);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 2;
                for (HologramEffect hologramEffect : EffectManager.getHologramEffects()) {
                    boolean have = player.hasPermission("MegaWalls.hologrameffect." + hologramEffect.getName());
                    boolean selected = gamePlayer.getPlayerStats().getEffectStats().getHologramEffect().equals(hologramEffect);
                    boolean enough = gamePlayer.getPlayerStats().getMythicDust() >= hologramEffect.getPrice();
                    ItemBuilder itemBuilder = new ItemBuilder(hologramEffect.getIconType(), 1, hologramEffect.getIconData()).setDisplayName("§a" + hologramEffect.getLine());
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.addAll(hologramEffect.getInfo(0));
                    lore.add(" ");
                    if (selected) {
                        lore.add("§a激活");
                    } else if (have) {
                        lore.add("§a已解锁");
                    } else if (enough) {
                        lore.add("§7花费:§e" + hologramEffect.getPrice() + "神话之尘");
                        lore.add("§e点击购买！");
                    } else {
                        lore.add("§7花费:§e" + hologramEffect.getPrice() + "神话之尘");
                        lore.add("§c神话之尘不足！");
                    }
                    itemBuilder.setLore(lore);
                    contents.set(row, column, ClickableItem.of(itemBuilder.build(), e -> {
                        e.setCancelled(true);
                        if (selected) {
                            return;
                        }
                        if (have) {
                            gamePlayer.getPlayerStats().getEffectStats().updateHologramEffect(hologramEffect.getName());
                            gamePlayer.sendMessage("§a选择成功！");
                            contents.inventory().open(player);
                            return;
                        }
                        if (enough) {
                            gamePlayer.getPlayerStats().takeMythicDust(hologramEffect.getPrice());
                            hologramEffect.upgrade(gamePlayer);
                            gamePlayer.sendMessage("§a购买成功！");
                            contents.inventory().open(player);
                            return;
                        }
                        gamePlayer.sendMessage("§c神话之尘不足！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    }));
                    if (column + 1 > 6) {
                        ++row;
                        column = 2;
                        continue;
                    }
                    ++column;
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

    public static SmartInventory buildKillMessage() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("击杀信息");
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(InventoryManager.EFFECTMENU);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                int row = 1;
                int column = 2;
                for (KillMessage killMessage : EffectManager.getKillMessages()) {
                    boolean have = player.hasPermission("MegaWalls.killmessage." + killMessage.getName());
                    boolean selected = gamePlayer.getPlayerStats().getEffectStats().getKillMessage().equals(killMessage);
                    boolean enough = gamePlayer.getPlayerStats().getMythicDust() >= killMessage.getPrice();
                    ItemBuilder itemBuilder = new ItemBuilder(killMessage.getIconType(), 1, killMessage.getIconData()).setDisplayName("§a" + killMessage.getDisplayName());
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.addAll(killMessage.getInfo(0));
                    lore.add(" ");
                    if (selected) {
                        lore.add("§a激活");
                    } else if (have) {
                        lore.add("§a已解锁");
                    } else if (enough) {
                        lore.add("§7花费:§e" + killMessage.getPrice() + "神话之尘");
                        lore.add("§e点击购买！");
                    } else {
                        lore.add("§7花费:§6" + StringUtils.formattedCoins(killMessage.getPrice()));
                        lore.add("§c神话之尘不足！");
                    }
                    itemBuilder.setLore(lore);
                    contents.set(row, column, ClickableItem.of(itemBuilder.build(), e -> {
                        e.setCancelled(true);
                        if (selected) {
                            return;
                        }
                        if (have) {
                            gamePlayer.getPlayerStats().getEffectStats().updateKillMessage(killMessage.getName());
                            gamePlayer.sendMessage("§a选择成功！");
                            contents.inventory().open(player);
                            return;
                        }
                        if (enough) {
                            gamePlayer.getPlayerStats().takeMythicDust(killMessage.getPrice());
                            killMessage.upgrade(gamePlayer);
                            gamePlayer.sendMessage("§a购买成功！");
                            contents.inventory().open(player);
                            return;
                        }
                        gamePlayer.sendMessage("§c神话之尘不足！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    }));
                    if (column + 1 > 6) {
                        ++row;
                        column = 2;
                        continue;
                    }
                    ++column;
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

