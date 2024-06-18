package cyan.thegoodboys.megawalls.api.event;

import cyan.thegoodboys.megawalls.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGameJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Game game;

    public PlayerGameJoinEvent(Game game) {
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
