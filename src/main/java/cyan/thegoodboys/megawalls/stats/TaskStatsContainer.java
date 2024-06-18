/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package cyan.thegoodboys.megawalls.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.reward.Task;

import java.util.Map;

public class TaskStatsContainer {
    private static DataBase database = MegaWalls.getInstance().getDataBase();
    private GamePlayer gamePlayer;
    private Task task;
    private boolean accept;
    private JsonObject jsonObject;
    private String lastFinished;

    public TaskStatsContainer(GamePlayer gamePlayer, Task task, boolean accept, JsonObject jsonObject, String lastFinished) {
        this.gamePlayer = gamePlayer;
        this.task = task;
        this.accept = accept;
        this.jsonObject = jsonObject;
        this.lastFinished = lastFinished;
    }

    public void accept() {
        JsonObject defaultData;
        this.accept = true;
        this.jsonObject = defaultData = this.task.defaultData();
        database.dbUpdate("megawalls_reward_" + this.task.getId(), new KeyValue("accept", "true").add("progress", defaultData.toString()), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateAccept(boolean accept) {
        this.accept = accept;
        database.dbUpdate("megawalls_reward_" + this.task.getId(), new KeyValue("accept", String.valueOf(accept)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addProgress(String name, int amount) {
        JsonObject newJson = new JsonObject();
        for (Map.Entry entry : this.jsonObject.entrySet()) {
            if (((String) entry.getKey()).equals(name)) {
                newJson.addProperty(name, (Number) (this.jsonObject.get(name).getAsInt() + amount));
                continue;
            }
            newJson.add((String) entry.getKey(), (JsonElement) entry.getValue());
        }
        this.jsonObject = newJson;
        database.dbUpdate("megawalls_reward_" + this.task.getId(), new KeyValue("progress", this.jsonObject.toString()), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void setFinished() {
        String current;
        this.lastFinished = current = Task.current();
        database.dbUpdate("megawalls_reward_" + this.task.getId(), new KeyValue("lastFinished", this.lastFinished), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public Task getTask() {
        return this.task;
    }

    public boolean isAccept() {
        return this.accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getLastFinished() {
        return this.lastFinished;
    }

    public void setLastFinished(String lastFinished) {
        this.lastFinished = lastFinished;
    }
}

