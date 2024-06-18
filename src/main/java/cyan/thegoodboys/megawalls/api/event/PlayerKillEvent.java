/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package cyan.thegoodboys.megawalls.api.event;

import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Game game = null;
    private GamePlayer killer = null;
    private GamePlayer player = null;
    private boolean finalKill = false;

    public PlayerKillEvent(Game game, GamePlayer killer, GamePlayer player) {
        this(game, killer, player, false);
    }

    public PlayerKillEvent(Game game, GamePlayer killer, GamePlayer player, boolean finalKill) {
        this.game = game;
        this.killer = killer;
        this.player = player;
        this.finalKill = finalKill;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Game getGame() {
        return this.game;
    }

    public GamePlayer getKiller() {
        return this.killer;
    }

    public GamePlayer getPlayer() {
        return this.player;
    }

    public boolean isFinalKill() {
        return this.finalKill;
    }
}

