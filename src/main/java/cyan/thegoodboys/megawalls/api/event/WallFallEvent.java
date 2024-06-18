/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package cyan.thegoodboys.megawalls.api.event;

import cyan.thegoodboys.megawalls.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WallFallEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Game game;

    public WallFallEvent(Game game) {
        this.game = game;
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
}

