/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.server.ServerListPingEvent
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener
        extends BaseListener {
    private final Game game;

    public ServerListener(MegaWalls plugin) {
        super(plugin);
        this.game = plugin.getGame();
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        if (this.game != null) {
            GameState state = this.game.getState();
            if (state == GameState.LOBBY) {
                e.setMotd("§a等待中");
            } else if (state == GameState.INGAME) {
                e.setMotd("§e游戏中");
            } else if (state == GameState.STOP) {
                e.setMotd("§c已结束");
            }
        }
    }
}

