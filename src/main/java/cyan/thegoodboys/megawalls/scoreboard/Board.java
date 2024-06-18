/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Scoreboard
 */
package cyan.thegoodboys.megawalls.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class Board {
    protected final Scoreboard scoreboard;

    protected Board(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public static boolean nil(Object i) {
        return i == null;
    }

    public static Scoreboard getScoreboardOf(Player p) {
        Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        p.setScoreboard(scoreboard);
        return scoreboard;
    }

    public Objective getObjectiveOf(DisplaySlot slot) {
        Objective objective = this.scoreboard.registerNewObjective("board", "dummy");
        objective.setDisplaySlot(slot);
        return objective;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public abstract void update();
}

