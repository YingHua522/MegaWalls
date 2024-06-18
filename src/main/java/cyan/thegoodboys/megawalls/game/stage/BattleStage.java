/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.GameOverEvent;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.timer.GameOverTimer;
import org.bukkit.Bukkit;

public class BattleStage extends GameStage {
    private int cacheLeft;

    public BattleStage() {
        super("游戏结束", 1740, 60, 3);
    }

    @Override
    public void excute(Game game) {
        Bukkit.getPluginManager().callEvent(new GameOverEvent(game));
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new GameOverTimer(game), 0L, 20L);
    }

    @Override
    public void excuteLeftSeconds(Game game, int left) {
        if (left == 60 || left == 30 || left <= 5) {
            game.broadcastMessage("§e游戏将在§c" + left + "§e秒后结束！");
        }
    }

    public int getCacheLeft() {
        return this.cacheLeft;
    }

    public void setCacheLeft(int cacheLeft) {
        this.cacheLeft = cacheLeft;
    }
}

