/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.spectator;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.SQLSettings;

import java.util.HashMap;
import java.util.Map;

public class SpectatorSettings {
    private static final Map<GamePlayer, SpectatorSettings> CACHES = new HashMap<GamePlayer, SpectatorSettings>();
    private static DataBase database = MegaWalls.getInstance().getDataBase();
    private final GamePlayer player;
    private int speed;
    private boolean autoTp;
    private boolean nightVision;
    private boolean firstPerson;
    private boolean hideOther;
    private boolean fly;

    public SpectatorSettings(GamePlayer player) {
        this.player = player;
        if (!database.isValueExists("spectator_settings", SQLSettings.KV_SPECTATOR_SETTINGS, new KeyValue("uuid", player.getUuid().toString()))) {
            database.dbInsert("spectator_settings", new KeyValue("uuid", player.getUuid().toString()).add("speed", 0).add("autoTp", 0).add("nightVision", 0).add("firstPerson", 1).add("hideOther", 0).add("fly", 0));
        }
        this.load();
    }

    public static SpectatorSettings get(GamePlayer player) {
        if (CACHES.containsKey(player)) {
            return CACHES.get(player);
        }
        SpectatorSettings s = new SpectatorSettings(player);
        CACHES.put(player, s);
        return s;
    }

    public void load() {
        for (KeyValue kv : database.dbSelect("spectator_settings", SQLSettings.KV_SPECTATOR_SETTINGS, new KeyValue("uuid", this.player.getUuid().toString()))) {
            this.speed = Integer.parseInt(kv.getString("speed"));
            this.autoTp = Integer.parseInt(kv.getString("autoTp")) == 1;
            this.nightVision = Integer.parseInt(kv.getString("nightVision")) == 1;
            this.firstPerson = Integer.parseInt(kv.getString("firstPerson")) == 1;
            this.hideOther = Integer.parseInt(kv.getString("hideOther")) == 1;
            this.fly = Integer.parseInt(kv.getString("fly")) == 1;
        }
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int key) {
        if (key < 0 || key > 4) {
            return;
        }
        this.speed = key;
        database.dbUpdate("spectator_settings", new KeyValue("speed", key), new KeyValue("uuid", this.player.getUuid().toString()));
    }

    public boolean getOption(Option o) {
        switch (o) {
            case AUTOTP: {
                return this.autoTp;
            }
            case NIGHTVISION: {
                return this.nightVision;
            }
            case FIRSTPERSON: {
                return this.firstPerson;
            }
            case HIDEOTHER: {
                return this.hideOther;
            }
            case FLY: {
                return this.fly;
            }
        }
        return false;
    }

    public void setOption(Option o, boolean key) {
        if (this.getOption(o) && key) {
            return;
        }
        switch (o) {
            case AUTOTP: {
                this.autoTp = key;
                database.dbUpdate("spectator_settings", new KeyValue("autoTp", key ? 1 : 0), new KeyValue("uuid", this.player.getUuid().toString()));
                break;
            }
            case NIGHTVISION: {
                this.nightVision = key;
                database.dbUpdate("spectator_settings", new KeyValue("nightVision", key ? 1 : 0), new KeyValue("uuid", this.player.getUuid().toString()));
                break;
            }
            case FIRSTPERSON: {
                this.firstPerson = key;
                database.dbUpdate("spectator_settings", new KeyValue("firstPerson", key ? 1 : 0), new KeyValue("uuid", this.player.getUuid().toString()));
                break;
            }
            case HIDEOTHER: {
                this.hideOther = key;
                database.dbUpdate("spectator_settings", new KeyValue("hideOther", key ? 1 : 0), new KeyValue("uuid", this.player.getUuid().toString()));
                break;
            }
            case FLY: {
                this.fly = key;
                database.dbUpdate("spectator_settings", new KeyValue("fly", key ? 1 : 0), new KeyValue("uuid", this.player.getUuid().toString()));
            }
        }
    }

    public GamePlayer getPlayer() {
        return this.player;
    }

    public static enum Option {
        AUTOTP,
        NIGHTVISION,
        FIRSTPERSON,
        HIDEOTHER,
        FLY;

    }
}

