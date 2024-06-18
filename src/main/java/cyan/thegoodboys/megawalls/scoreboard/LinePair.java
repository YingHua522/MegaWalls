/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.scoreboard;

public class LinePair {
    private final Line line;
    private final int score;

    private LinePair(Line line, int score) {
        this.line = line;
        this.score = score;
    }

    public static LinePair of(Line line, int i) {
        return new LinePair(line, i);
    }

    public Line getLine() {
        return this.line;
    }

    public int getScore() {
        return this.score;
    }

    public String getText() {
        return this.line.getText();
    }
}

