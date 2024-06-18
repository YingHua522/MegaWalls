/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.effect.TeamDisplayColor;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.team.TeamColor;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class DisplaySettingsMenu {
    public static SmartInventory displaysettings(SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("色盲");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
                ItemBuilder itemBuilder = (new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 14)).setDisplayName("§a更换红队");
                List<String> lore = new ArrayList();
                lore.add("§7使用此物品更换游戏中红队的颜色。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(1, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                    DisplaySettingsMenu.teamColor(TeamColor.RED, contents.inventory()).open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 5)).setDisplayName("§a更换绿队");
                lore.clear();
                lore.add("§7使用此物品更换游戏中绿队的颜色。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(1, 3, ClickableItem.of(itemBuilder.build(), (e) -> {
                    DisplaySettingsMenu.teamColor(TeamColor.GREEN, contents.inventory()).open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 3)).setDisplayName("§a更换蓝队");
                lore.clear();
                lore.add("§7使用此物品更换游戏中蓝队的颜色。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(1, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                    DisplaySettingsMenu.teamColor(TeamColor.BLUE, contents.inventory()).open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 4)).setDisplayName("§a更换黄队");
                lore.clear();
                lore.add("§7使用此物品更换游戏中黄队的颜色。");
                lore.add(" ");
                lore.add("§e点击浏览！");
                itemBuilder.setLore((List) lore);
                contents.set(1, 7, ClickableItem.of(itemBuilder.build(), (e) -> {
                    DisplaySettingsMenu.teamColor(TeamColor.YELLOW, contents.inventory()).open(player);
                }));
                boolean enablePrefix = effectStats.isEnablePrefix();
                boolean enableItalic = effectStats.isEnableItalic();
                boolean enableBold = effectStats.isEnableBold();
                itemBuilder = (new ItemBuilder(Material.BOOK)).setDisplayName("§a团队预览");
                lore.clear();
                lore.add(effectStats.getRedColor().getChatColor() + (enablePrefix ? "[R] " : "") + effectStats.getRedColor().getChatColor() + (enableItalic ? ChatColor.ITALIC : "") + gamePlayer.getDisplayName((GamePlayer) null));
                lore.add(effectStats.getGreenColor().getChatColor() + (enablePrefix ? "[G] " : "") + effectStats.getGreenColor().getChatColor() + (enableBold ? ChatColor.BOLD : "") + gamePlayer.getDisplayName((GamePlayer) null));
                lore.add(effectStats.getBlueColor().getChatColor() + (enablePrefix ? "[B] " : "") + effectStats.getBlueColor().getChatColor() + (enableBold ? ChatColor.BOLD : "") + gamePlayer.getDisplayName((GamePlayer) null));
                lore.add(effectStats.getYellowColor().getChatColor() + (enablePrefix ? "[Y] " : "") + effectStats.getYellowColor().getChatColor() + (enableBold ? ChatColor.BOLD : "") + gamePlayer.getDisplayName((GamePlayer) null));
                itemBuilder.setLore((List) lore);
                contents.set(2, 4, ClickableItem.empty(itemBuilder.build()));
                itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte) (enablePrefix ? 10 : 8))).setDisplayName("§a称号模式");
                lore.clear();
                lore.add("§7该选项会在玩家名字前面");
                lore.add("§7加上队伍的名称,以帮助");
                lore.add("§7玩家相互区分。");
                lore.add(" ");
                if (!enablePrefix) {
                    lore.add("§e点击启用！");
                } else {
                    lore.add("§e点击禁用！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(3, 3, ClickableItem.of(itemBuilder.build(), (e) -> {
                    boolean flag = !enablePrefix;
                    gamePlayer.sendMessage((flag ? ChatColor.GREEN : ChatColor.RED) + "称号模式 " + (flag ? "启用" : "禁用"));
                    gamePlayer.playSound(Sound.NOTE_PLING, 1.0F, 3.0F);
                    effectStats.updateEnablePrefix(flag);
                    contents.inventory().open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte) (enableItalic ? 10 : 8))).setDisplayName("§a团队斜体模式");
                lore.clear();
                lore.add("§7该选项能让你队名变成斜体,");
                lore.add("§7使其容易辨识。");
                lore.add(" ");
                if (!enableItalic) {
                    lore.add("§e点击启用！");
                } else {
                    lore.add("§e点击禁用！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(3, 4, ClickableItem.of(itemBuilder.build(), (e) -> {
                    boolean flag = !enableItalic;
                    gamePlayer.sendMessage((flag ? ChatColor.GREEN : ChatColor.RED) + "团队斜体模式 " + (flag ? "启用" : "禁用"));
                    gamePlayer.playSound(Sound.NOTE_PLING, 1.0F, 3.0F);
                    effectStats.updateEnableItalic(flag);
                    contents.inventory().open(player);
                }));
                itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte) (enableBold ? 10 : 8))).setDisplayName("§a敌队加粗模式");
                lore.clear();
                lore.add("§7该选项将使敌方队伍名称");
                lore.add("§7加粗,便于区分。");
                lore.add(" ");
                if (!enableBold) {
                    lore.add("§e点击启用！");
                } else {
                    lore.add("§e点击禁用！");
                }

                itemBuilder.setLore((List) lore);
                contents.set(3, 5, ClickableItem.of(itemBuilder.build(), (e) -> {
                    boolean flag = !enableBold;
                    gamePlayer.sendMessage((flag ? ChatColor.GREEN : ChatColor.RED) + "敌队加粗模式 " + (flag ? "启用" : "禁用"));
                    gamePlayer.playSound(Sound.NOTE_PLING, 1.0F, 3.0F);
                    effectStats.updateEnableBold(flag);
                    contents.inventory().open(player);
                }));
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

    public static SmartInventory teamColor(final TeamColor teamColor, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(teamColor.getText() + "队");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
                TeamDisplayColor[] var5 = TeamDisplayColor.values();
                int var6 = var5.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    TeamDisplayColor color = var5[var7];
                    int row = color.getSlot() / 9;
                    int column = color.getSlot() % 9;
                    boolean selected = effectStats.getColor(teamColor) == color;
                    ItemBuilder itemBuilder = (new ItemBuilder(Material.STAINED_GLASS_PANE, 1, color.getData())).setDisplayName("§a更换成" + color.getDisplayname() + "色");
                    List<String> lore = new ArrayList();
                    lore.add("§7将§8" + teamColor.getText() + "队§7在游戏中的颜色变为§8" + color.getDisplayname() + "§7色。");
                    lore.add(" ");
                    lore.add(color.getChatColor() + "§l超级战墙");
                    lore.add(color.getChatColor() + teamColor.getText());
                    lore.add(color.getChatColor() + teamColor.getTag() + " " + gamePlayer.getDisplayName((GamePlayer) null));
                    lore.add(" ");
                    if (!selected) {
                        lore.add("§e点击启用！");
                    } else {
                        lore.add("§e点击禁用！");
                    }

                    itemBuilder.setLore((List) lore);
                    contents.set(row, column, ClickableItem.empty(itemBuilder.build()));
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte) (selected ? 10 : 8))).setDisplayName(selected ? "§a启用" : "§c禁用");
                    lore.clear();
                    if (!selected) {
                        lore.add("§e点击启用！");
                    } else {
                        lore.add("§e点击禁用！");
                    }

                    itemBuilder.setLore((List) lore);
                    contents.set(row + 1, column, ClickableItem.of(itemBuilder.build(), (e) -> {
                        if (!selected) {
                            gamePlayer.sendMessage("§a已启用！");
                            effectStats.setColor(teamColor, color);
                            contents.inventory().open(player);
                        } else {
                            gamePlayer.sendMessage("§7已选择！");
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

