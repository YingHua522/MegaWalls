/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Scoreboard
 */
package cyan.thegoodboys.megawalls.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SidebarBoard extends Board {
    private final Plugin plugin;
    private final Objective objective;
    private int taskId;
    private Body body;
    private Line head;
    private List<String> bodyText;

    public SidebarBoard(Plugin plugin, Scoreboard scoreboard) {
        super(scoreboard);
        this.plugin = plugin;
        this.objective = this.getObjectiveOf(DisplaySlot.SIDEBAR);
    }

    public static SidebarBoard of(Plugin plugin) {
        return new SidebarBoard(plugin, plugin.getServer().getScoreboardManager().getNewScoreboard());
    }

    public static SidebarBoard of(Plugin plugin, Player p) {
        return SidebarBoard.of(plugin, Board.getScoreboardOf(p));
    }

    public static SidebarBoard of(Plugin plugin, Scoreboard scoreboard) {
        return new SidebarBoard(plugin, scoreboard);
    }

    @Override
    public void update() {
        String headText;
        String string = headText = SidebarBoard.nil(this.head) ? null : this.head.getText();
        if (!this.objective.getDisplayName().equals(headText)) {
            this.objective.setDisplayName(headText);
        }
        List<String> lastBody = this.bodyText;
        this.bodyText = new ArrayList<String>();
        if (SidebarBoard.nil(lastBody)) {
            if (!SidebarBoard.nil(this.body)) {
                this.body.getList().forEach(pair -> {
                    String line = pair.getText();
                    this.bodyText.add(line);
                    this.objective.getScore(line).setScore(pair.getScore());
                });
            }
        } else {
            if (!SidebarBoard.nil(this.body)) {
                this.body.getList().forEach(pair -> {
                    String line = pair.getText();
                    lastBody.remove(line);
                    this.bodyText.add(line);
                    this.objective.getScore(line).setScore(pair.getScore());
                });
            }
            lastBody.forEach(this.scoreboard::resetScores);
        }
    }

    public void update(Supplier<Boolean> condition, int interval) {
        if (this.taskId == 0) {
            this.taskId = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
                if (condition.get()) {
                    this.update();
                } else {
                    this.cancel();
                }
            }, 0L, interval).getTaskId();
        }
    }

    public void setHead(Line head) {
        this.head = head;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void cancel() {
        if (this.taskId != 0) {
            this.plugin.getServer().getScheduler().cancelTask(this.taskId);
        }
    }
}

