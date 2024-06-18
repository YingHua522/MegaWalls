/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryType
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
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;

public class GameMenu {
    public static SmartInventory build() {
        final SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("游戏设置");
        builder.type(InventoryType.CHEST);
        builder.size(4, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND).setDisplayName("\u00a7e钻石模式");
                ArrayList<String> lore = new ArrayList<String>();
                lore.clear();
                lore.add("\u00a77启用全钻套模式");
                itemBuilder.setLore(lore);
                contents.set(1, 1, ClickableItem.of(itemBuilder.build(), e -> {
                }));
            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });

        return builder.build();
    }

}

