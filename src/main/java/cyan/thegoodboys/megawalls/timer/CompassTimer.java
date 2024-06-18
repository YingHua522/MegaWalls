/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.spectator.SpectatorTarget;
import cyan.thegoodboys.megawalls.util.BossBar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;

public class CompassTimer implements Runnable {

    //private final HashMap<Player, BossBar> bossBars = new HashMap<>();
    private final MegaWalls plugin;

    public CompassTimer(MegaWalls plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
            if (online.isSpectator()) {
                SpectatorTarget target = online.getSpectatorTarget();
                target.sendTip();
                target.autoTp();
                continue;
            }
            Player player = online.getPlayer();
            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null || itemStack.getType() != Material.COMPASS) continue;
            online.getPlayerCompass().sendClosestPlayer();
        }
    }
}

