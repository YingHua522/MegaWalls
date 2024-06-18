/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 */
package cyan.thegoodboys.megawalls.inv.opener;

import com.google.common.collect.ImmutableList;
import cyan.thegoodboys.megawalls.inv.InventoryManager;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class SpecialInventoryOpener
        implements InventoryOpener {
    private static final List<InventoryType> SUPPORTED = ImmutableList.of(InventoryType.FURNACE, InventoryType.WORKBENCH, InventoryType.DISPENSER, InventoryType.DROPPER, InventoryType.ENCHANTING, InventoryType.BREWING, InventoryType.ANVIL, InventoryType.BEACON, InventoryType.HOPPER);

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        InventoryManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(player, inv.getType(), inv.getTitle());
        this.fill(handle, manager.getContents(player).get());
        player.closeInventory();
        player.openInventory(handle);

        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return SUPPORTED.contains(type);
    }
}

