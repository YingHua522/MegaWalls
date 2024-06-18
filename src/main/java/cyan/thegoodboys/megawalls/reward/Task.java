/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package cyan.thegoodboys.megawalls.reward;

import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.TaskStatsContainer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class Task {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private int id;
    private Type type;
    private String name;

    public Task(int id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public static boolean isLater(String date1, String date2, Type type) {
        try {
            long time1 = format.parse(date1).getTime();
            long time2 = format.parse(date2).getTime();
            long l = type == Type.WEEKLY ? 604800000L : 86400000L;
            return time1 - time2 >= l;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String current() {
        return format.format(new Date());
    }

    public abstract List<String> getInfo(TaskStatsContainer var1);

    public abstract List<String> getRewardInfo();

    public abstract void giveRewarad(GamePlayer var1);

    public abstract boolean isFinish(TaskStatsContainer var1);

    public abstract JsonObject defaultData();

    public int getId() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public static enum Type {
        DAILY("Daily "),
        MYTHIC("Mythic "),
        WEEKLY("Weekly ");

        private String name;

        private Type(String name) {
            this.name = name;
        }

        public String getInfo() {
            return this == WEEKLY ? "\u00a78\u00a7o\u6bcf\u5468\u4efb\u52a1\u53ef\u4ee5\u6bcf\u5468\u5b8c\u6210\u4e00\u6b21\u3002" : "\u00a78\u00a7o\u6bcf\u5929\u53ea\u80fd\u5b8c\u6210\u4e00\u6b21\u6bcf\u65e5\u4efb\u52a1\u3002";
        }

        public String getName() {
            return this.name;
        }
    }
}

