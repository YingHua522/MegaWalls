/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.tellraw;

public class JsonBuilder {
    public static String[] REPLACEMENT_CHARS = new String[128];

    static {
        for (int i = 0; i <= 31; ++i) {
            JsonBuilder.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        JsonBuilder.REPLACEMENT_CHARS[34] = "\\\"";
        JsonBuilder.REPLACEMENT_CHARS[92] = "\\\\";
        JsonBuilder.REPLACEMENT_CHARS[9] = "\\t";
        JsonBuilder.REPLACEMENT_CHARS[8] = "\\b";
        JsonBuilder.REPLACEMENT_CHARS[10] = "\\n";
        JsonBuilder.REPLACEMENT_CHARS[13] = "\\r";
        JsonBuilder.REPLACEMENT_CHARS[12] = "\\f";
    }

    StringBuilder json = new StringBuilder();

    public JsonBuilder() {
    }

    public JsonBuilder(String string) {
        this();
        this.append(string);
    }

    public void append(String value) {
        int last = 0;
        int length = value.length();
        for (int i = 0; i < length; ++i) {
            String replacement;
            char c = value.charAt(i);
            if (c < '\u0080') {
                replacement = REPLACEMENT_CHARS[c];
                if (replacement == null) {
                    continue;
                }
            } else if (c == '\u2028') {
                replacement = "\\u2028";
            } else {
                if (c != '\u2029') continue;
                replacement = "\\u2029";
            }
            if (last < i) {
                this.json.append(value, last, i);
            }
            this.json.append(replacement);
            last = i + 1;
        }
        if (last < length) {
            this.json.append(value, last, length);
        }
    }

    public void deleteLastChar() {
        this.json.deleteCharAt(this.json.length() - 1);
    }

    public boolean isEmpty() {
        return this.json.length() == 0;
    }

    public int length() {
        return this.json.length();
    }

    public String toString() {
        return this.json.toString();
    }
}

