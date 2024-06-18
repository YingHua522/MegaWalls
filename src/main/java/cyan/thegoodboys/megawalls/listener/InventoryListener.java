/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.SkullMeta
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryListener
        extends BaseListener {
    private static Game game = MegaWalls.getInstance().getGame();

    public InventoryListener(MegaWalls plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (item == null) {
            return;
        }
        if (item.getType() == Material.AIR) {
            return;
        }
        if (gamePlayer.isSpectator()) {
            e.setCancelled(true);
            switch (item.getType()) {
                case COMPASS: {
                    if (!item.hasItemMeta() || !item.getItemMeta().getDisplayName().contains("Teleporter")) break;
                    game.openSpectatorInventory(gamePlayer);
                    break;
                }
                case DIODE: {
                    InventoryManager.SPECTATORSETTINGS.open(player);
                    break;
                }
                case BED: {
                    MegaWalls.getInstance().tpToLobby(player);
                    break;
                }
            }
            if (e.getInventory().getName().equals("\u00a7a\u00a7lTeleporter")) {
                e.setCancelled(true);
                ItemStack eitem = e.getCurrentItem();
                if (eitem == null || !eitem.getType().equals((Object) Material.SKULL_ITEM)) {
                    return;
                }
                SkullMeta meta = (SkullMeta) eitem.getItemMeta();
                Player p = Bukkit.getServer().getPlayer(meta.getOwner());
                if (p == null) {
                    return;
                }
                player.closeInventory();
                player.teleport((Entity) p);
                return;
            }
        }

        if (game.isWaiting()) {
            e.setCancelled(true);
            if (item.getType() == Material.BED) {
                MegaWalls.getInstance().tpToLobby(player);
                return;
            }
        } else if (game.isStarted() && !gamePlayer.isSpectator() && ClassesManager.isClassesItem(item)) {
            if (player.getOpenInventory().getType() != InventoryType.CRAFTING && e.getClickedInventory().getType() == InventoryType.PLAYER) {
                e.setCancelled(true);
                return;
            }
            if (player.getOpenInventory().getType() != InventoryType.CRAFTING && e.getClickedInventory().getType() != InventoryType.PLAYER) {
                e.setCancelled(true);
                return;
            }
        }
    }
}

