/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.game;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChestManager {
    private static final List<ChestItem> loot = new ArrayList<>();
    private static final List<Integer> slots = new ArrayList<>();

    static {
        for (int i = 0; i < 27; ++i) {
            slots.add(i);
        }
        ChestManager.addItem(45, Material.COOKED_FISH, 0, new Random().nextInt(3) + 1);
        ChestManager.addItem(45, Material.APPLE, 0, new Random().nextInt(5) + 1);
        ChestManager.addItem(50, Material.COAL, 0, 3);
        ChestManager.addItem(45, Material.BREAD, 0, 2);
        ChestManager.addItem(45, Material.COOKED_BEEF, 0, new Random().nextInt(6) + 3);
        ChestManager.addItem(50, Material.LOG, 0, 1);
        ChestManager.addItem(50, Material.COBBLESTONE, 0, 5);
        ChestManager.addItem(35, Material.ARROW, 0, new Random().nextInt(33) + 32);
        ChestManager.addItem(45, Material.IRON_INGOT, 0,  new Random().nextInt(6) + 3);
        ChestManager.addItem(25, Material.TNT, 0, new Random().nextInt(6) + 4);
        ChestManager.addItem(50, Material.REDSTONE, 0, 5);
        ChestManager.addItem(15, Material.ARROW, 0, 6);
    }

    public static void addItem(int chance, Material material, int durability, int amount) {
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        if (durability != 0) {
            item.setDurability((short) durability);
        }
        loot.add(new ChestItem(chance, item));
    }

    public static void fillInventory(Inventory inventory) {
        inventory.clear();
        int added = 0;
        Collections.shuffle(slots);
        Random random = new Random();
        int i = 0;
        for (ChestItem chestItem : loot) {
            if (i >= 10) break;
            if (random.nextInt(100) > chestItem.getChance()) continue;
            inventory.setItem(slots.get(added), chestItem.getItemStack());
            ++i;
            if (added++ < 27) continue;
            break;
        }
    }

    public static class ChestItem {
        private final int chance;
        private final ItemStack itemStack;

        public ChestItem(int chance, ItemStack itemStack) {
            this.chance = chance;
            this.itemStack = itemStack;
        }

        public int getChance() {
            return this.chance;
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }
    }
}

