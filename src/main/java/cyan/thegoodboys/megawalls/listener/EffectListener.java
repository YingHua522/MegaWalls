/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.ItemMergeEvent
 *  org.bukkit.event.player.PlayerArmorStandManipulateEvent
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class EffectListener
        extends BaseListener {
    public EffectListener(MegaWalls plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e) {
        if (e.getRightClicked().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
        for (ItemStack itemStack : e.getDrops()) {
            if (ClassesManager.isClassesItem(itemStack)) {
                e.getDrops().remove(itemStack);//掉落物清除
                return;
            }
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent e) {
        if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
            e.setCancelled(true);
        }
    }
}

