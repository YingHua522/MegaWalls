package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.skin.SkinProvider;
import cyan.thegoodboys.megawalls.skin.SkinManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SkinMenu {
    public static SmartInventory createSkinSelectionMenu(final Classes classes, SmartInventory parentInventory, GamePlayer gamePlayer) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(classes.getNameColor() + "选择" + classes.getDisplayName() + "的皮肤");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {

            public void init(Player player, InventoryContents contents) {

                SkinManager registry = new SkinManager();
                SkinProvider skinProvider = registry.getProvider(classes.getName());

                // 获取这个职业的所有可用皮肤
                if (skinProvider == null) {
                    return;
                }

                List<ClassesSkin> skins = skinProvider.getSkinsForClass(classes, gamePlayer);

                // 将每个皮肤添加到GUI中
                if (skins == null) {
                    return;
                }

                for (int i = 0; i < skins.size(); i++) {
                    ClassesSkin skin = skins.get(i);
                    ItemStack skinItem = createSkinItem(skin); // 创建代表皮肤的物品
                    contents.set(i / 9, i % 9, ClickableItem.of(skinItem, e -> {
                        // 当玩家点击皮肤时，设置玩家的皮肤
                        gamePlayer.getPlayerStats().setSelectedSkin(skin);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(MegaWalls.getInstance(), () -> {
                            ClassesSkin defaultSkin = gamePlayer.getPlayerStats().getSelectedSkin();
                            PlayerUtils.skinChange((CraftPlayer) player, defaultSkin.getValue(), defaultSkin.getSignature());
                            gamePlayer.sendMessage("§a你选择了§e" + defaultSkin.getDisplayName() + "§a皮肤！");
                        }, 1L);
                        gamePlayer.playSound(Sound.CLICK, 1.0F, 1.0F);
                    }));
                }

                // 添加返回按钮
                contents.set(5, 3, ClickableItem.of(new ItemStack(Material.ARROW), e -> {
                    if (e.getCursor().getType() == Material.AIR) {
                        builder.getParent().open(player);
                    }
                }));
                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.EMERALD)).setDisplayName("§7总硬币:§6" + StringUtils.formattedCoins(gamePlayer.getPlayerStats().getCoins())).build(), (e) -> {
                }));
            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }


    private static ItemStack createSkinItem(ClassesSkin skin) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); // 创建一个新的头颅物品
        SkullMeta meta = (SkullMeta) item.getItemMeta(); // 获取物品的元数据

        meta.setDisplayName(skin.getDisplayName()); // 设置物品的显示名称为皮肤的显示名称
        meta.setOwner(skin.getOwner()); // 设置头颅的皮肤

        List<String> lore = new ArrayList<>(skin.getInfo()); // 将皮肤的信息添加到物品的描述中
        meta.setLore(lore);

        item.setItemMeta(meta); // 将修改后的元数据应用到物品上

        return item;
    }
}
