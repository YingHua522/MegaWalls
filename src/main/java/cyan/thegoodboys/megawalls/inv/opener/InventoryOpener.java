/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 */
package cyan.thegoodboys.megawalls.inv.opener;

import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InventoryOpener {
    Inventory open(SmartInventory var1, Player var2);

    boolean supports(InventoryType var1);

    default void fill(Inventory handle, InventoryContents contents) {
        ClickableItem[][] items = contents.all();
        for (int row = 0; row < items.length; ++row) {
            for (int column = 0; column < items[row].length; ++column) {
                if (items[row][column] == null) continue;
                handle.setItem(9 * row + column, items[row][column].getItem());
            }
        }
    }
}

