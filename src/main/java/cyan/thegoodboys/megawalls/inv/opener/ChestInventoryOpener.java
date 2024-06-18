/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package cyan.thegoodboys.megawalls.inv.opener;


import com.google.common.base.Preconditions;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestInventoryOpener
        implements InventoryOpener {
    @Override
    public Inventory open(SmartInventory inv, Player player) {
        Preconditions.checkArgument((inv.getColumns() == 9 ? 1 : 0) != 0, "The column count for the chest inventory must be 9, found: %s.", inv.getColumns());
        Preconditions.checkArgument((inv.getRows() >= 1 && inv.getRows() <= 6 ? 1 : 0) != 0, "The row count for the chest inventory must be between 1 and 6, found: %s", inv.getRows());
        InventoryManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getRows() * inv.getColumns(), inv.getTitle());
        this.fill(handle, manager.getContents(player).get());
        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }
}

