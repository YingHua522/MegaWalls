package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Skin {

    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("皮肤选择器");
        builder.size(6, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                if (gamePlayer == null) return;
                KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
                ClassesSkin defaultSkin = gamePlayer.getPlayerStats().getSelected().getDefaultSkin();
                ItemBuilder itemBuilder = (new ItemBuilder(Material.SKULL_ITEM, 1, (byte)3)).setDisplayName("§e皮肤 §8- §7" + gamePlayer.getPlayerStats().getSelected().getDisplayName()).setSkullSkin(defaultSkin.getValue());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add("§7" + gamePlayer.getPlayerStats().getSelected().getDisplayName() + "§7的默认皮肤，");
                lore.add("§7该皮肤默认拥有。");
                itemBuilder.setLore(lore);
                contents.set(1, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                }));
                if (!kitStats.isPrestigeSkin()) {
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte)10)).setDisplayName("§e皮肤 §8- §7" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.clear();
                    lore.add("§a已选择！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                        player.sendMessage("§a你已经选择该皮肤了！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }));
                } else {
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte)8)).setDisplayName("§e皮肤 §8- §7" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.clear();
                    lore.add("§e点击选择！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 1, ClickableItem.of(itemBuilder.build(), (e) -> {
                        kitStats.setPrestigeSkin(false);
                        player.getInventory().setItem(1, (new ItemBuilder(Material.SKULL_ITEM, 1, (byte)3)).setDisplayName("§a皮肤选择器§7(右键点击)").setSkullSkin(defaultSkin.getValue()).build());
                        PlayerUtils.skinChange((CraftPlayer)player, defaultSkin.getValue(), defaultSkin.getSignature());
                        player.sendMessage("§a你选择了§e" + gamePlayer.getPlayerStats().getSelected().getDisplayName() + "§a皮肤！");
                        gamePlayer.playSound(Sound.CLICK, 1.0F, 1.0F);
                    }));
                }

                ClassesSkin prestigeskin = gamePlayer.getPlayerStats().getSelected().getPrestigeSkin();
                itemBuilder = (new ItemBuilder(Material.SKULL_ITEM, 1, (byte)3)).setDisplayName("§e皮肤 §8- §7精通" + gamePlayer.getPlayerStats().getSelected().getDisplayName()).setSkullSkin(prestigeskin.getValue());
                lore.clear();
                lore.add(" ");
                lore.add("§7" + gamePlayer.getPlayerStats().getSelected().getDisplayName() + "§7的精通皮肤，");
                lore.add("§7在该职业达到精通III后解锁。");
                itemBuilder.setLore(lore);
                contents.set(1, 2, ClickableItem.of(itemBuilder.build(), (e) -> {
                }));
                if (kitStats.isPrestigeSkin()) {
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte)10)).setDisplayName("§e皮肤 §8- §7精通" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.clear();
                    lore.add("§a已选择！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 2, ClickableItem.of(itemBuilder.build(), (e) -> {
                        player.sendMessage("§a你已经选择该皮肤了！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }));
                } else if (kitStats.getLevel() >= 4) {
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte)8)).setDisplayName("§e皮肤 §8- §7精通" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.clear();
                    lore.add("§e点击选择！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 2, ClickableItem.of(itemBuilder.build(), (e) -> {
                        kitStats.setPrestigeSkin(true);
                        player.getInventory().setItem(1, (new ItemBuilder(Material.SKULL_ITEM, 1, (byte)3)).setDisplayName("§a皮肤选择器§7(右键点击)").setSkullSkin(prestigeskin.getValue()).build());
                        PlayerUtils.skinChange((CraftPlayer)player, prestigeskin.getValue(), prestigeskin.getSignature());
                        player.sendMessage("§a你选择了§e精通" + gamePlayer.getPlayerStats().getSelected().getDisplayName() + "§a皮肤！");
                        gamePlayer.playSound(Sound.CLICK, 1.0F, 1.0F);
                    }));
                } else {
                    itemBuilder = (new ItemBuilder(Material.INK_SACK, 1, (byte)5)).setDisplayName("§e皮肤 §8- §7精通" + gamePlayer.getPlayerStats().getSelected().getDisplayName());
                    lore.clear();
                    lore.add("§c尚未解锁！");
                    itemBuilder.setLore(lore);
                    contents.set(2, 2, ClickableItem.of(itemBuilder.build(), (e) -> {
                        player.sendMessage("§c你尚未解锁此皮肤！");
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }));
                }

                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.BARRIER)).setDisplayName("§c关闭").build(), (e) -> player.closeInventory()));
            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }
}
