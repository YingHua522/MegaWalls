/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.tellraw;

public class MessagePart {
    private static String TEXT_FORMAT = "\"text\":\"%s\"";
    private static String CLICK_FORMAT = "\"clickEvent\":{\"action\":\"%s\",\"value\":\"%s\"}";
    private static String HOVER_FORMAT = "\"hoverEvent\":{\"action\":\"%s\",\"value\":\"%s\"}";
    private static String INSERT_FORMAT = " \"insertion\":\"%s\"";
    public String text;
    public String clickActionName;
    public String clickActionData;
    public String hoverActionName;
    public String hoverActionData;
    public String insertionData;

    public MessagePart() {
        this("");
    }

    public MessagePart(String text) {
        this.text = text;
    }

    public boolean hasText() {
        return this.text != null && !this.text.isEmpty();
    }

    public void writeJson(StringBuilder str) {
        str.append("{");
        str.append(String.format(TEXT_FORMAT, new JsonBuilder(this.text)));
        if (this.clickActionName != null) {
            str.append(",");
            str.append(String.format(CLICK_FORMAT, this.clickActionName, new JsonBuilder(this.clickActionData)));
        }
        if (this.hoverActionName != null) {
            str.append(",");
            str.append(String.format(HOVER_FORMAT, this.hoverActionName, new JsonBuilder(this.hoverActionData)));
        }
        if (this.insertionData != null) {
            str.append(",");
            str.append(String.format(INSERT_FORMAT, new JsonBuilder(this.insertionData)));
        }
        str.append("}");
    }
}

