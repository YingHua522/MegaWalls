/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.timer.CenterCheckTimer;
import cyan.thegoodboys.megawalls.timer.HungerIncreaseTimer;
import org.bukkit.Bukkit;

public class DeathMatchStage extends GameStage {
    public DeathMatchStage() {
        super("死亡竞赛", 10, 10, 99);
    }

    @Override
    public void excute(Game game) {
        game.setDeathMatch(true);
        game.getStageManager().setCurrentStage(3);
        game.getStageManager().setSeconds(((BattleStage) game.getStageManager().getStage(3)).getCacheLeft() + 10);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new HungerIncreaseTimer()::getHungerLevel, 0L, 3600L), 3600L);
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new CenterCheckTimer(game), 0L, 100L);
    }

    @Override
    public void excuteLeftSeconds(Game game, int left) {
        if (left <= 5) {
            game.broadcastMessage("§c§l所有的凋灵都死了！还有§b§l" + left + "§c§l秒开启死亡竞赛！");
        }
    }
}

