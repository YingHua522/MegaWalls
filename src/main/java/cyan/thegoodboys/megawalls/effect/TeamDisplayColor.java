/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package cyan.thegoodboys.megawalls.effect;

import org.bukkit.ChatColor;

public enum TeamDisplayColor {
    RED("红", ChatColor.RED, (byte) 14, 1),
    LIGHT_PURPLE("淡紫", ChatColor.LIGHT_PURPLE, (byte) 2, 2),
    DARK_PURPLE("深紫", ChatColor.DARK_PURPLE, (byte) 10, 3),
    GREEN("绿", ChatColor.GREEN, (byte) 5, 5),
    DARK_GREEN("深绿", ChatColor.DARK_GREEN, (byte) 13, 6),
    DARK_GRAY("深灰", ChatColor.DARK_GRAY, (byte) 7, 7),
    BLUE("蓝", ChatColor.BLUE, (byte) 3, 28),
    DARK_BLUE("深蓝", ChatColor.DARK_BLUE, (byte) 11, 29),
    DARK_AQUA("深水绿", ChatColor.DARK_AQUA, (byte) 9, 30),
    YELLOW("黄", ChatColor.YELLOW, (byte) 4, 32),
    GOLD("金", ChatColor.GOLD, (byte) 1, 33),
    WHITE("白", ChatColor.WHITE, (byte) 0, 34);

    private final String displayname;
    private final ChatColor chatColor;
    private final byte data;
    private final int slot;

    private TeamDisplayColor(String displayname, ChatColor chatColor, byte data, int slot) {
        this.displayname = displayname;
        this.chatColor = chatColor;
        this.data = data;
        this.slot = slot;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public byte getData() {
        return this.data;
    }

    public int getSlot() {
        return this.slot;
    }
}