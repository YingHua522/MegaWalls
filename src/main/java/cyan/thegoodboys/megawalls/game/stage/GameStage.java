/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.game.Game;

public abstract class GameStage {
    private String name;
    private int excuteSeconds;
    private int excuteLeftSeconds;
    private int priority;

    public GameStage(String name, int excuteSeconds, int excuteLeftSeconds, int priority) {
        this.name = name;
        this.excuteSeconds = excuteSeconds;
        this.excuteLeftSeconds = excuteLeftSeconds;
        this.priority = priority;
    }

    public abstract void excute(Game var1);

    public abstract void excuteLeftSeconds(Game var1, int var2);

    public String getName() {
        return this.name;
    }

    public int getExcuteSeconds() {
        return this.excuteSeconds;
    }

    public int getExcuteLeftSeconds() {
        return this.excuteLeftSeconds;
    }

    public int getPriority() {
        return this.priority;
    }
}

