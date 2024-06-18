/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.game.GameType;
import cyan.thegoodboys.megawalls.spectator.SpectatorSettings;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import cyan.thegoodboys.megawalls.inv.content.SlotIterator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpectatorInventory {
    public static SmartInventory buildSettings() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("旁观者设置");
        builder.type(InventoryType.CHEST);
        builder.size(4, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                SpectatorSettings settings = SpectatorSettings.get(gamePlayer);
                contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName("§a没有速度效果").build(), e -> {
                    if (settings.getSpeed() == 0) {
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.sendMessage("§c你不再有任何速度效果！");
                    settings.setSpeed(0);
                    contents.inventory().open(player);
                }));
                contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.CHAINMAIL_BOOTS).setDisplayName("§a速度 I").build(), e -> {
                    if (settings.getSpeed() == 1) {
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                    player.sendMessage("§a你获得了 速度 I 效果！");
                    settings.setSpeed(1);
                    contents.inventory().open(player);
                }));
                contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.IRON_BOOTS).setDisplayName("§a速度 II").build(), e -> {
                    if (settings.getSpeed() == 2) {
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                    player.sendMessage("§a你获得了 速度 II 效果！");
                    settings.setSpeed(2);
                    contents.inventory().open(player);
                }));
                contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName("§a速度 III").build(), e -> {
                    if (settings.getSpeed() == 3) {
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                    player.sendMessage("§a你获得了 速度 III 效果！");
                    settings.setSpeed(3);
                    contents.inventory().open(player);
                }));
                contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName("§a速度 IV").build(), e -> {
                    if (settings.getSpeed() == 4) {
                        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
                    player.sendMessage("§a你获得了 速度 IV 效果！");
                    settings.setSpeed(4);
                    contents.inventory().open(player);
                }));
                if (settings.getOption(SpectatorSettings.Option.AUTOTP)) {
                    contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.COMPASS).setDisplayName("§c停用自动传送").setLore("§7点击停用自动传送").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.AUTOTP, false);
                        player.sendMessage("§c你不再被自动传送到目标位置！");
                        contents.inventory().open(player);
                    }));
                } else {
                    contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.COMPASS).setDisplayName("§a启动自动传送").setLore("§7点击启用自动传送").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.AUTOTP, true);
                        player.sendMessage("§a你开启了自动传送功能！");
                        contents.inventory().open(player);
                    }));
                }
                if (settings.getOption(SpectatorSettings.Option.NIGHTVISION)) {
                    contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§c停用夜视").setLore("§7点击停用夜视").build(), e -> {
                        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        }
                        settings.setOption(SpectatorSettings.Option.NIGHTVISION, false);
                        player.sendMessage("§c你不再有夜视效果了！");
                        contents.inventory().open(player);
                    }));
                } else {
                    contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.EYE_OF_ENDER).setDisplayName("§a启动夜视").setLore("§7点击启用夜视").build(), e -> {
                        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                        settings.setOption(SpectatorSettings.Option.NIGHTVISION, true);
                        player.sendMessage("§a你现在拥有了夜视！");
                        contents.inventory().open(player);
                    }));
                }
                if (settings.getOption(SpectatorSettings.Option.FIRSTPERSON)) {
                    contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.WATCH).setDisplayName("§c停用第一人称旁观").setLore("§7点击停用第一人称旁观").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.FIRSTPERSON, false);
                        player.sendMessage("§c你将默认使用第三人称模式！");
                        if (gamePlayer.isSpectator() && player.getGameMode() == GameMode.SPECTATOR) {
                            gamePlayer.sendTitle("§e退出旁观模式", "", 0, 20, 0);
                            player.setGameMode(GameMode.ADVENTURE);
                            player.setAllowFlight(true);
                            player.setFlying(true);
                        }
                        contents.inventory().open(player);
                    }));
                } else {
                    contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.WATCH).setDisplayName("§a启动第一人称旁观").setLore("§7点击确认使用指南针时", "§7自动沿用第一人称旁观！", "§7你也可以右键点击一位玩家", "§7来启用第一人称旁观").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.FIRSTPERSON, true);
                        player.sendMessage("§a当你用你的指南针现在一个玩家后，你会被自动传送到他那里！");
                        contents.inventory().open(player);
                    }));
                }
                if (settings.getOption(SpectatorSettings.Option.HIDEOTHER)) {
                    contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayName("§a查看旁观者").setLore("§7点击以显示其他旁观者").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.HIDEOTHER, false);
                        player.sendMessage("§a你现在可以看见其他旁观者了！");
                        contents.inventory().open(player);
                    }));
                } else {
                    contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.REDSTONE).setDisplayName("§c隐藏旁观者").setLore("§7点击来隐藏其他旁观者").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.HIDEOTHER, true);
                        player.sendMessage("§c你不会再看到其他的旁观者！");
                        contents.inventory().open(player);
                    }));
                }
                if (settings.getOption(SpectatorSettings.Option.FLY)) {
                    contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.FEATHER).setDisplayName("§c停用持续飞行").setLore("§7点击停用飞行").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.FLY, false);
                        player.sendMessage("§a你现在能停止飞行！");
                        contents.inventory().open(player);
                    }));
                } else {
                    contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.FEATHER).setDisplayName("§a启动持续飞行").setLore("§7点击启用飞行").build(), e -> {
                        settings.setOption(SpectatorSettings.Option.FLY, true);
                        player.sendMessage("§a你现在不能停止飞行！");
                        if (player.isOnGround()) {
                            player.getLocation().setY(player.getLocation().getY() + 0.1);
                        }
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        contents.inventory().open(player);
                    }));
                }
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static SmartInventory alivePlayers(final List<GamePlayer> players) {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("传送器");
        builder.type(InventoryType.CHEST);
        builder.size(5, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            SlotIterator.Impl slotIterator = null;

            @Override
            public void init(Player player, InventoryContents contents) {
                int i;
                Game game = MegaWalls.getInstance().getGame();
                EffectStatsContainer effectStats = GamePlayer.get(player.getUniqueId()).getPlayerStats().getEffectStats();
                contents.pagination().setItemsPerPage(28);
                ArrayList<ClickableItem> items = new ArrayList<ClickableItem>();
                for (GamePlayer gamePlayer : players) {
                    ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setDisplayName("§7" + gamePlayer.getPlayer().getDisplayName()).setSkullOwner(gamePlayer.getName());
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + gamePlayer.getGameTeam().getTeamColor().getText() + "队");
                    lore.add(" ");
                    lore.add("§7血量: §f" + (int) gamePlayer.getPlayer().getHealth() + " §c❤");
                    lore.add("§7职业: §f" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.add(" ");
                    lore.add("§7点击旁观！");
                    itemBuilder.setLore(lore);
                    items.add(ClickableItem.of(itemBuilder.build(), e -> {
                        GamePlayer.get(player.getUniqueId()).getSpectatorTarget().setTarget(gamePlayer);
                        GamePlayer.get(player.getUniqueId()).getSpectatorTarget().tp();
                        player.closeInventory();
                    }));
                }
                contents.pagination().setItems(items.toArray(new ClickableItem[0]));
                this.slotIterator = new SlotIterator.Impl(contents, contents.inventory(), SlotIterator.Type.HORIZONTAL, 0, 1);
                this.slotIterator.allowOverride(true);
                for (i = 0; i <= 3; ++i) {
                    this.slotIterator.blacklist(i, 0);
                    this.slotIterator.blacklist(i, 8);
                }
                for (i = 0; i <= 8; ++i) {
                    this.slotIterator.blacklist(4, i);
                }
                for (ClickableItem item : contents.pagination().getPageItems()) {
                    this.slotIterator.next().set(item);
                    if (this.slotIterator.ended()) break;
                }
                this.update(player, contents);
                int slot = game.getGameType() == GameType.NORMAL ? 2 : 3;
                for (GameTeam gameTeam : game.getTeams()) {
                    String name = effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + gameTeam.getTeamColor().getText() + "队";
                    contents.set(4, slot, ClickableItem.of(new ItemBuilder(Material.WOOL).setDisplayName(name).setLore("§7点此以仅显示" + name + "§7！").setDyeColor(gameTeam.getTeamColor().getDyeColor()).build(), e -> SpectatorInventory.alivePlayers(gameTeam.getAlivePlayers()).open(player)));
                    ++slot;
                }
                contents.set(4, slot, ClickableItem.of(new ItemBuilder(Material.GLASS).setDisplayName("§f所有玩家").setLore("§7点此以仅显示所有玩家！").build(), e -> SpectatorInventory.alivePlayers(MegaWalls.getIngame()).open(player)));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
                boolean onlyOnePage;
                boolean bl = onlyOnePage = contents.pagination().isFirst() && contents.pagination().isLast();
                if (!contents.pagination().isFirst() && !onlyOnePage) {
                    contents.set(2, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a上一页").build(), e -> {
                        contents.pagination().previous();
                        this.slotIterator.row(0).column(1);
                        for (ClickableItem item : contents.pagination().getPageItems()) {
                            this.slotIterator.next().set(item);
                            if (this.slotIterator.ended()) break;
                        }
                        this.update(player, contents);
                    }));
                } else {
                    contents.set(2, 0, null);
                }
                if (!contents.pagination().isLast() && !onlyOnePage) {
                    contents.set(2, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("§a下一页").build(), e -> {
                        contents.pagination().next();
                        this.slotIterator.row(0).column(1);
                        for (ClickableItem item : contents.pagination().getPageItems()) {
                            this.slotIterator.next().set(item);
                            if (this.slotIterator.ended()) break;
                        }
                        this.update(player, contents);
                    }));
                } else {
                    contents.set(2, 8, null);
                }
            }
        });
        return builder.build();
    }
}

