/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.game;

public enum GameType {
    DUEL("\u5bf9\u51b3"),
    NORMAL("\u6807\u51c6");

    private String name;

    private GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

