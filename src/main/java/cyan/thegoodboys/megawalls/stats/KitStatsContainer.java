/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 */
package cyan.thegoodboys.megawalls.stats;

import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import lombok.Getter;

public class KitStatsContainer {
    private static final DataBase database = MegaWalls.getInstance().getDataBase();
    @Getter
    private final GamePlayer gamePlayer;
    @Getter
    private final Classes classes;
    @Getter
    private int chestCount = 0;
    @Getter
    private int phxChest = 0;
    private int level;
    private int equipLevel;
    private int skillLevel;
    private int skill2Level;
    private int skill3Level;
    private int skill4Level;
    @Getter
    private int wins;
    @Getter
    private int mvps;
    @Getter
    private int games;
    @Getter
    private int finalKills;
    @Getter
    private int finalAssists;
    private int enderChest;
    @Getter
    private int masterPoints;
    @Getter
    private boolean enableGoldTag;
    @Getter
    private long playTime;
    @Getter
    private JsonObject inventory;

    public KitStatsContainer(GamePlayer gamePlayer, Classes classes) {
        this.gamePlayer = gamePlayer;
        this.classes = classes;
    }

    public void addLevel() {
        ++this.level;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("level", this.level), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addEquipLevel() {
        ++this.equipLevel;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("equipLevel", this.equipLevel), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addSkillLevel() {
        ++this.skillLevel;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("skillLevel", this.skillLevel), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addSkill2Level() {
        ++this.skill2Level;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("skill2Level", this.skill2Level), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addSkill3Level() {
        ++this.skill3Level;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("skill3Level", this.skill3Level), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void incrementChestCount() {
        this.chestCount++;
    }

    public void incrementPhxChest() {
        this.phxChest++;
    }

    public void resetPhxChest() {
        this.phxChest = 0;
    }

    public void resetChestCount() {
        this.chestCount = 0;
    }

    public void addSkill4Level() {
        ++this.skill4Level;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("skill4Level", this.skill4Level), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addWins() {
        ++this.wins;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("wins", this.wins), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addMvp() {
        ++this.mvps;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("mvps", this.mvps), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addGames() {
        ++this.games;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("games", this.games), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addFinalKills(int amount) {
        this.finalKills += amount;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("finalKills", this.finalKills), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addFinalAssists(int amount) {
        this.finalAssists += amount;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("finalAssists", this.finalAssists), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addEnderChest() {
        ++this.enderChest;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("enderChest", this.enderChest), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void giveMasterPoints(int amount) {
        this.masterPoints += amount;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("masterPoints", this.masterPoints), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void takeMasterPoints(int amount) {
        if (this.masterPoints - amount < 0) {
            return;
        }
        this.masterPoints -= amount;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("masterPoints", this.masterPoints), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addPlayTime(long time) {
        this.playTime += time;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("playTime", this.playTime), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateInventory(JsonObject inventory) {
        this.inventory = inventory;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("inventory", inventory == null ? "null" : inventory.toString()), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public String upgradePercent() {
        double percent = (double) (this.equipLevel + this.skillLevel + this.skill2Level + this.skill3Level + this.skill4Level - 8) / 16.0;
        return (percent >= 1.0 ? "§a§l" : "§e") + StringUtils.percent(percent);
    }

    public boolean isMaxed() {
        return this.equipLevel + this.skillLevel + this.skill2Level + this.skill3Level + this.skill4Level + this.enderChest - 8 == 16;
    }

    public int getLevel() {
        return MegaWalls.isMythicMode() ? 2 : this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEquipLevel() {
        return MegaWalls.isMythicMode() ? 5 : this.equipLevel;
    }

    public void setEquipLevel(int equipLevel) {
        this.equipLevel = equipLevel;
    }

    public int getSkillLevel() {
        return MegaWalls.isMythicMode() ? 5 : this.skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getSkill2Level() {
        return MegaWalls.isMythicMode() ? 3 : this.skill2Level;
    }

    public void setSkill2Level(int skill2Level) {
        this.skill2Level = skill2Level;
    }

    public int getSkill3Level() {
        return MegaWalls.isMythicMode() ? 3 : this.skill3Level;
    }

    public void setSkill3Level(int skill3Level) {
        this.skill3Level = skill3Level;
    }

    public int getSkill4Level() {
        return MegaWalls.isMythicMode() ? 3 : this.skill4Level;
    }

    public void setSkill4Level(int skill4Level) {
        this.skill4Level = skill4Level;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setMvps(int mvps) {
        this.mvps = mvps;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public void setFinalAssists(int finalAssists) {
        this.finalAssists = finalAssists;
    }

    public int getEnderChest() {
        return MegaWalls.isMythicMode() ? 5 : this.enderChest;
    }

    public void setEnderChest(int enderChest) {
        this.enderChest = enderChest;
    }

    public void setMasterPoints(int masterPoints) {
        this.masterPoints = masterPoints;
    }

    public void setEnableGoldTag(boolean enableGoldTag) {
        this.enableGoldTag = enableGoldTag;
        database.dbUpdate("classes_" + this.classes.getName(), new KeyValue("enableGoldTag", String.valueOf(enableGoldTag)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public void setInventory(JsonObject inventory) {
        this.inventory = inventory;
    }
}

