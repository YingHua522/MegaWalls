/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ConfirmMenu {
    public static SmartInventory build(String title, final ItemStack itemStack, SmartInventory parentInventory, final Consumer<InventoryClickEvent> consumer) {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title(title);
        builder.type(InventoryType.CHEST);
        builder.size(3, 9);
        builder.closeable(true);
        builder.parent(parentInventory);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer.get(player.getUniqueId());
                itemStack.setType(Material.STAINED_CLAY);
                itemStack.setDurability((short) 13);
                contents.set(1, 2, ClickableItem.of(itemStack, consumer));
                contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 14).setDisplayName("§c拒绝").setLore("§7返回到上一级菜单。").build(), e -> builder.getParent().open(player)));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }
}

