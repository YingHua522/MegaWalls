/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class BaseListener implements Listener {
    private final MegaWalls plugin;

    public BaseListener(MegaWalls plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public MegaWalls getPlugin() {
        return this.plugin;
    }
}

