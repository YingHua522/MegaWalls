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

public class PlayerEnergyChangeEvent
        extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Game game;
    private GamePlayer player;
    private ChangeReason changeReason;
    private int amount;

    public PlayerEnergyChangeEvent(Game game, GamePlayer player, ChangeReason changeReason, int amount) {
        this.game = game;
        this.player = player;
        this.changeReason = changeReason;
        this.amount = amount;
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

    public GamePlayer getPlayer() {
        return this.player;
    }

    public ChangeReason getChangeReason() {
        return this.changeReason;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static enum ChangeReason {
        MELLEE,
        BOW,
        MAGIC;

    }
}

