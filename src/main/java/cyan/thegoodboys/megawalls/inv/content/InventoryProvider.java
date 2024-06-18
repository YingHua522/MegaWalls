/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.inv.content;

import org.bukkit.entity.Player;

public interface InventoryProvider {
    void init(Player var1, InventoryContents var2);

    void update(Player var1, InventoryContents var2);
}

