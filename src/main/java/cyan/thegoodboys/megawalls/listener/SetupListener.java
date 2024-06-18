/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

public class SetupListener extends BaseListener {
    public static Location left;
    public static Location right;
    private ItemStack axe = new ItemBuilder(Material.DIAMOND_AXE).setDisplayName("§a§l选择工具").setUnbreakable(true).build();

    public SetupListener(MegaWalls plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (!e.getPlayer().isOp()) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§c未发现游戏实例,不能进入该服务器！");
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setItem(0, this.axe);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand() == null) {
            return;
        }
        if (e.getPlayer().getItemInHand().equals(this.axe) && (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                left = e.getClickedBlock().getLocation();
                e.getPlayer().sendMessage("§e选择第一个点:" + left.getBlockX() + " " + left.getBlockY() + " " + left.getBlockZ());
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                right = e.getClickedBlock().getLocation();
                e.getPlayer().sendMessage("§e选择第二个点:" + right.getBlockX() + " " + right.getBlockY() + " " + right.getBlockZ());
            }
            e.setCancelled(true);
        }
    }
}

