/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.stats;

import cyan.thegoodboys.megawalls.database.KeyValue;

public class SQLSettings {
    public static final String TABLE_STATS = "megawalls_stats";
    public static final String TABLE_EFFECTS = "megawalls_effects";
    public static final String TABLE_CHALLENGES = "megawalls_challenges";
    public static final String TABLE_REJOIN = "megawalls_rejoin";
    public static final String TABLE_REWARD_PREFIX = "megawalls_reward_";
    public static final String url = "https://gitee.com/h1a/mega-walls-license/raw/master/mw";
    public static final String TABLE_SPECTATOR_SETTINGS = "spectator_settings";
    public static final String TABLE_CLASSES_PREFIX = "classes_";
    public static final KeyValue KV_STATS = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("kills", "INTEGER").add("finalKills", "INTEGER").add("wins", "INTEGER").add("mvps", "INTEGER").add("games", "INTEGER").add("coins", "INTEGER").add("mythicDust", "INTEGER").add("selected", "VARCHAR(40)").add("Skin", "VARCHAR(40)");
    public static final KeyValue KV_EFFECTS = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("hologramEffect", "VARCHAR(20)").add("killMessage", "VARCHAR(20)").add("redColor", "VARCHAR(20)").add("greenColor", "VARCHAR(20)").add("blueColor", "VARCHAR(20)").add("yellowColor", "VARCHAR(20)").add("enablePrefix", "VARCHAR(5)").add("enableItalic", "VARCHAR(5)").add("enableBold", "VARCHAR(5)").add("surface", "VARCHAR(5)");
    public static final KeyValue KV_CHALLENGES = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("challenges", "LONGTEXT");
    public static final KeyValue KV_REJOIN = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("server", "VARCHAR(30)").add("time", "BIGINT");
    public static final KeyValue KV_REWARD = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("accept", "VARCHAR(5)").add("progress", "TEXT").add("lastFinished", "VARCHAR(10)");
    public static final KeyValue KV_SPECTATOR_SETTINGS = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("speed", "INTEGER").add("speed", "INTEGER").add("autoTp", "INTEGER").add("nightVision", "INTEGER").add("firstPerson", "INTEGER").add("hideOther", "INTEGER").add("fly", "INTEGER");
    public static final KeyValue KV_CLASSES = new KeyValue("uuid", "VARCHAR(36) PRIMARY KEY").add("realname", "VARCHAR(20)").add("level", "INTEGER").add("equipLevel", "INTEGER").add("skillLevel", "INTEGER").add("skill2Level", "INTEGER").add("skill3Level", "INTEGER").add("skill4Level", "INTEGER").add("collectLevel", "INTEGER").add("wins", "INTEGER").add("mvps", "INTEGER").add("games", "INTEGER").add("finalKills", "INTEGER").add("finalAssists", "INTEGER").add("enderChest", "INTEGER").add("masterPoints", "INTEGER").add("enableGoldTag", "VARCHAR(5)").add("playTime", "BIGINT").add("inventory", "LONGTEXT");
}

