/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.util;

import java.text.DecimalFormat;

public class StringUtils {
    private static final DecimalFormat coinsFormat = new DecimalFormat("#,###");

    public static String formattedCoins(int coins) {
        return coinsFormat.format(coins);
    }

    public static String upgradeBar(int level, int max) {
        StringBuffer sb = new StringBuffer("\u00a78");
        for (int i = 0; i < max; ++i) {
            if (i >= level) {
                sb.append("\u2592");
                continue;
            }
            sb.append("\u2588");
        }
        return sb.toString();
    }

    public static String percent(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0%");
        return decimalFormat.format(value);
    }

    public static String level(int level) {
        switch (level) {
            case 1: {
                return "I";
            }
            case 2: {
                return "II";
            }
            case 3: {
                return "III";
            }
            case 4: {
                return "IV";
            }
            case 5: {
                return "V";
            }
        }
        return "I";
    }

    public static String formatLongTime(long time) {
        int hour = (int) Math.floor(time / 3600000L);
        int min = (int) Math.floor(time / 60000L);
        if (hour > 0) {
            return hour + "\u5c0f\u65f6";
        }
        return min + "\u5206\u949f";
    }
}

