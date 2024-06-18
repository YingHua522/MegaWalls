package cyan.thegoodboys.megawalls.inventory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.ItemUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EditInventoryMenu {

    public static SmartInventory edit(final Classes classes, SmartInventory parentInventory) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("编辑" + classes.getDisplayName() + "的物品位置");
        builder.type(InventoryType.CHEST);
        builder.size(6, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {

            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                if (gamePlayer != null) {
                    KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                    JsonObject inventoryData = kitStats.getInventory();
                    if (inventoryData != null) {
                        for (Map.Entry<String, JsonElement> entry : inventoryData.entrySet()) {
                            int slot = Integer.parseInt(entry.getKey());
                            ItemStack item = ItemUtils.read(java.util.Base64.getDecoder().decode(entry.getValue().getAsString()));
                            contents.set(slot / 9, slot % 9, ClickableItem.of(item, e -> {
                                e.setCurrentItem(e.getCurrentItem());
                            }));
                        }
                    } else {
                        List<ItemStack> items = classes.getEquipmentPackage().getEquipments(gamePlayer.getPlayerStats().getKitStats(classes).getEquipLevel());
                        int[] rows = new int[]{4, 2, 1, 0};
                        int columnx = 0;
                        int slot = 0;

                        for (ItemStack item : items) {
                            if (!ItemUtils.isHelmet(item) && !ItemUtils.isChestplate(item) && !ItemUtils.isLeggings(item) && !ItemUtils.isBoots(item)) {
                                contents.set(rows[slot], columnx, ClickableItem.of(item, (e) -> {
                                    e.setCurrentItem(e.getCurrentItem());
                                }));
                                ++columnx;
                                if (columnx > 8) {
                                    ++slot;
                                    columnx = 0;
                                }
                            }
                        }
                    }
                }

                contents.fillRow(3, ClickableItem.empty((new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 15)).setDisplayName("§7以下物品都在快捷栏里。").build()));
                contents.set(5, 3, ClickableItem.of((new ItemBuilder(Material.ARROW)).setDisplayName("§a返回").setLore(new String[]{"§7至" + builder.getParent().getTitle()}).build(), (e) -> {
                    if (e.getCursor().getType() == Material.AIR) {
                        builder.getParent().open(player);
                    }
                }));
                contents.set(5, 4, ClickableItem.of((new ItemBuilder(Material.CHEST)).setDisplayName("§a保存").build(), (e) -> {
                    if (e.getCursor().getType() == Material.AIR) {
                        EditInventoryMenu.saveInventory(player, classes);
                    }
                }));
                contents.set(5, 5, ClickableItem.of((new ItemBuilder(Material.TNT)).setDisplayName("§c重置物品位置").setLore(new String[]{"§7注意！这将重置物品位置,且无法恢复。"}).build(), (e) -> {
                    if (e.getCursor().getType() == Material.AIR) {
                        if (gamePlayer != null) {
                            gamePlayer.getPlayerStats().getKitStats(classes).updateInventory(null);
                        }
                        player.closeInventory();
                        player.sendMessage("§a重置成功！");
                    }
                }));
            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    private static void saveInventory(Player player, Classes classes) {
        KitStatsContainer kitStats = Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getPlayerStats().getKitStats(classes);
        Map<Integer, ItemStack> map = new HashMap<>();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            map.put(i, itemStack);
        }

        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<Integer, ItemStack> integerItemStackEntry : map.entrySet()) {
            if (integerItemStackEntry.getValue() != null && integerItemStackEntry.getValue().getType() != Material.AIR) {
                jsonObject.addProperty(String.valueOf(integerItemStackEntry.getKey()), Base64.getEncoder().encodeToString(ItemUtils.write(integerItemStackEntry.getValue())));
            }
        }

        kitStats.updateInventory(jsonObject);
        player.sendMessage("§a保存成功！");
        player.closeInventory();
    }

    private static boolean contains(ItemStack itemStack, List<ItemStack> items) {
        int same = 0;

        for (ItemStack item : items) {
            if (item.equals(itemStack)) {
                ++same;
            }
        }

        return same == 1;
    }
}
