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
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class StageManager implements Runnable {
    private Game game;
    private OptionalInt taskId = OptionalInt.empty();
    private int currentStage = 0;
    private int seconds = 0;
    private Map<Integer, GameStage> stages = new LinkedHashMap<>();

    public StageManager(Game game) {
        this.game = game;
        this.registerStage(new ProtectStage());
        this.registerStage(new WallFallStage());
        this.registerStage(new WitherAngryStage());
        this.registerStage(new DeathMatchStage());
        this.registerStage(new BattleStage());
        this.registerStage(new OverStage());
    }

    @Override
    public void run() {
        Optional<GameStage> stageOpt = Optional.ofNullable(this.currentStage());
        stageOpt.ifPresent(stage -> {
            if (this.seconds >= stage.getExcuteSeconds()) {
                this.setCurrentStage(stage.getPriority() + 1);
                stage.excute(this.game);
            } else if (stage.getExcuteLeftSeconds() >= stage.getExcuteSeconds() - this.seconds) {
                stage.excuteLeftSeconds(this.game, stage.getExcuteSeconds() - this.seconds);
            }
            ++this.seconds;
        });
    }

    public GameStage currentStage() {
        return this.stages.getOrDefault(this.currentStage, null);
    }

    public void setCurrentStage(int priority) {
        this.seconds = 0;
        this.currentStage = priority;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getLeftTime() {
        return this.currentStage().getExcuteSeconds() - this.seconds;
    }

    public String formattedNextStage() {
        GameStage currentStage = this.currentStage();
        String color = this.getLeftTime() <= 30 ? (this.getLeftTime() % 2 == 0 ? "§a" : "§c") : "§a";
        return "§f" + currentStage.getName() + ": " + color + this.game.getFormattedTime(this.getLeftTime());
    }

    private void registerStage(GameStage event) {
        this.stages.put(event.getPriority(), event);
    }

    public GameStage getStage(int priority) {
        return this.stages.getOrDefault(priority, null);
    }

    public void start() {
        if (taskId.isPresent()) {
            return;
        }
        taskId = OptionalInt.of(Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), this, 0L, 20L).getTaskId());
    }

    public void stop() {
        if (!taskId.isPresent()) {
            return;
        }
        Bukkit.getScheduler().cancelTask(taskId.getAsInt());
        taskId = OptionalInt.empty();
        this.currentStage = 0;
        this.seconds = 0;
    }
}

