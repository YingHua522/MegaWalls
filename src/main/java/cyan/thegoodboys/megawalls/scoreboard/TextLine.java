/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.scoreboard;

public class TextLine
        implements Line {
    private final String text;

    private TextLine(String text) {
        this.text = text;
    }

    public static Line of(String text) {
        return new TextLine(text);
    }

    @Override
    public String getText() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }
}

