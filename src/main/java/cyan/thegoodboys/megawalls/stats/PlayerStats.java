/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package cyan.thegoodboys.megawalls.stats;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.classes.ClassesType;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.reward.Challenge;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.reward.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStats {
    private static final Map<GamePlayer, PlayerStats> statsMap = new HashMap<GamePlayer, PlayerStats>();
    private static final DataBase database = MegaWalls.getInstance().getDataBase();
    private static final Game game = MegaWalls.getInstance().getGame();
    private static final Gson gson = new Gson();
    private final GamePlayer gamePlayer;
    private int kills;
    private int finalKills;
    private int wins;
    private int games;
    private int mvp;
    private int coins;
    private int getedcoins;
    private ClassesSkin selectedSkin;
    private String selectedSkinName;
    private int mythicDust;
    private String selected;
    private EffectStatsContainer effectStats;
    private JsonObject challenges;
    private Map<Task, TaskStatsContainer> taskMap = new HashMap<Task, TaskStatsContainer>();
    private Map<Classes, KitStatsContainer> kitStatsMap = new HashMap<Classes, KitStatsContainer>();
    private Classes cacheSelected;

    public PlayerStats(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (!database.isValueExists("megawalls_stats", SQLSettings.KV_STATS, new KeyValue("uuid", gamePlayer.getUuid().toString()))) {
            database.dbInsert("megawalls_stats", new KeyValue("uuid", gamePlayer.getUuid().toString()).add("realname", gamePlayer.getName()).add("kills", 0).add("finalKills", 0).add("wins", 0).add("mvps", 0).add("games", 0).add("coins", 5000).add("mythicDust", 0).add("selected", "HIM").add("Skin", "Steve"));
            database.dbInsert("megawalls_effects", new KeyValue("uuid", gamePlayer.getUuid().toString()).add("realname", gamePlayer.getName()).add("hologramEffect", "Default").add("killMessage", "Default").add("redColor", "RED").add("greenColor", "GREEN").add("blueColor", "BLUE").add("yellowColor", "YELLOW").add("enablePrefix", "true").add("enableItalic", "false").add("enableBold", "false").add("surface", "false"));
            database.dbInsert("megawalls_challenges", new KeyValue("uuid", gamePlayer.getUuid().toString()).add("realname", gamePlayer.getName()).add("challenges", RewardManager.defaultChallenge.toString()));
            for (Task task : RewardManager.getTasks()) {
                database.dbInsert("megawalls_reward_" + task.getId(), new KeyValue("uuid", gamePlayer.getUuid().toString()).add("realname", gamePlayer.getName()).add("progress", task.defaultData().toString()).add("lastFinished", "2019-01-01"));
            }
            for (Classes classes : ClassesManager.getClasses()) {
                database.dbInsert("classes_" + classes.getName(), (new KeyValue("uuid", gamePlayer.getUuid().toString())).add("realname", gamePlayer.getName()).add("level", classes.getClassesType() == ClassesType.NOVICE ? 1 : 0).add("equipLevel", classes.getClassesType() == ClassesType.NOVICE ? 5 : 1).add("skillLevel", classes.getClassesType() == ClassesType.NOVICE ? 5 : 1).add("skill2Level", 1).add("skill3Level", 1).add("skill4Level", 1).add("wins", 0).add("mvps", 0).add("games", 0).add("finalKills", 0).add("finalAssists", 0).add("enderChest", 3).add("masterPoints", 0).add("enableGoldTag", "false").add("playTime", 0).add("inventory", "null"));
            }
        }
    }

    public static PlayerStats get(GamePlayer gamePlayer) {
        if (statsMap.containsKey(gamePlayer)) {
            return statsMap.get(gamePlayer);
        }
        PlayerStats ps = new PlayerStats(gamePlayer);
        statsMap.put(gamePlayer, ps);
        return statsMap.get(gamePlayer);
    }

    public static void remove(GamePlayer gamePlayer) {
        if (!statsMap.containsKey(gamePlayer)) {
            return;
        }
        statsMap.remove(gamePlayer);
    }

    public void update() {
        String inventory;
        for (KeyValue kv : database.dbSelect("megawalls_stats", SQLSettings.KV_STATS, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
            this.kills = Integer.parseInt(kv.getString("kills"));
            this.finalKills = Integer.parseInt(kv.getString("finalKills"));
            this.wins = Integer.parseInt(kv.getString("wins"));
            this.mvp = Integer.parseInt(kv.getString("mvps"));
            this.games = Integer.parseInt(kv.getString("games"));
            this.coins = Integer.parseInt(kv.getString("coins"));
            this.mythicDust = Integer.parseInt(kv.getString("mythicDust"));
            this.selected = kv.getString("selected");
            this.selectedSkinName = kv.getString("Skin");
        }
        for (KeyValue kv : database.dbSelect("megawalls_effects", SQLSettings.KV_EFFECTS, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
            this.effectStats = new EffectStatsContainer(this.gamePlayer);
            this.effectStats.setHologramEffect(kv.getString("hologramEffect"));
            this.effectStats.setKillMessage(kv.getString("killMessage"));
            this.effectStats.setRedColor(kv.getString("redColor"));
            this.effectStats.setGreenColor(kv.getString("greenColor"));
            this.effectStats.setBlueColor(kv.getString("blueColor"));
            this.effectStats.setYellowColor(kv.getString("yellowColor"));
            this.effectStats.setEnablePrefix(Boolean.parseBoolean(kv.getString("enablePrefix")));
            this.effectStats.setEnableItalic(Boolean.parseBoolean(kv.getString("enableItalic")));
            this.effectStats.setEnableBold(Boolean.parseBoolean(kv.getString("enableBold")));
            this.effectStats.setSurface(Boolean.parseBoolean(kv.getString("surface")));
        }
        for (KeyValue kv : database.dbSelect("megawalls_challenges", SQLSettings.KV_CHALLENGES, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
            this.challenges = gson.fromJson(kv.getString("challenges"), JsonObject.class);
        }
        for (Task task : RewardManager.getTasks()) {
            for (KeyValue kv : database.dbSelect("megawalls_reward_" + task.getId(), SQLSettings.KV_REWARD, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
                boolean accept = Boolean.valueOf(kv.getString("accept"));
                JsonObject jsonObject = (JsonObject) gson.fromJson(kv.getString("progress"), JsonObject.class);
                String lastFinished = kv.getString("lastFinished");
                this.taskMap.put(task, new TaskStatsContainer(this.gamePlayer, task, accept, jsonObject, lastFinished));
            }
        }
        for (Classes classes : ClassesManager.getClasses()) {
            for (KeyValue kv : database.dbSelect("classes_" + classes.getName(), SQLSettings.KV_CLASSES, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
                KitStatsContainer kitStats = new KitStatsContainer(this.gamePlayer, classes);
                kitStats.setLevel(Integer.parseInt(kv.getString("level")));
                kitStats.setEquipLevel(Integer.parseInt(kv.getString("equipLevel")));
                kitStats.setSkillLevel(Integer.parseInt(kv.getString("skillLevel")));
                kitStats.setSkill2Level(Integer.parseInt(kv.getString("skill2Level")));
                kitStats.setSkill3Level(Integer.parseInt(kv.getString("skill3Level")));
                kitStats.setSkill4Level(Integer.parseInt(kv.getString("skill4Level")));
                kitStats.setWins(Integer.parseInt(kv.getString("wins")));
                kitStats.setMvps(Integer.parseInt(kv.getString("mvps")));
                kitStats.setGames(Integer.parseInt(kv.getString("games")));
                kitStats.setFinalKills(Integer.parseInt(kv.getString("finalKills")));
                kitStats.setFinalAssists(Integer.parseInt(kv.getString("finalAssists")));
                kitStats.setEnderChest(Integer.parseInt(kv.getString("enderChest")));
                kitStats.setMasterPoints(Integer.parseInt(kv.getString("masterPoints")));
                kitStats.setEnableGoldTag(Boolean.parseBoolean(kv.getString("enableGoldTag")));
                kitStats.setPlayTime(Long.parseLong(kv.getString("playTime")));
                inventory = kv.getString("inventory");
                if (!inventory.equalsIgnoreCase("null") && !inventory.equalsIgnoreCase("")) {
                    kitStats.setInventory(gson.fromJson(inventory, JsonObject.class));
                }
                this.kitStatsMap.put(classes, kitStats);
            }
        }
        for (Classes classes : ClassesManager.getClasses()) {
            if (!this.kitStatsMap.containsKey(classes)) {
                database.dbInsert("classes_" + classes.getName(), (new KeyValue("uuid", this.gamePlayer.getUuid().toString())).add("realname", this.gamePlayer.getName()).add("level", classes.getClassesType() == ClassesType.NOVICE ? 1 : 0).add("equipLevel", classes.getClassesType() == ClassesType.NOVICE ? 5 : 1).add("skillLevel", classes.getClassesType() == ClassesType.NOVICE ? 5 : 1).add("skill2Level", 1).add("skill3Level", 1).add("skill4Level", 1).add("wins", 0).add("mvps", 0).add("games", 0).add("finalKills", 0).add("finalAssists", 0).add("enderChest", 3).add("masterPoints", 0).add("enableGoldTag", "false").add("playTime", 0).add("inventory", "null"));
            }
            for (KeyValue kv : database.dbSelect("classes_" + classes.getName(), SQLSettings.KV_CLASSES, new KeyValue("uuid", this.gamePlayer.getUuid().toString()))) {
                KitStatsContainer kitStats = new KitStatsContainer(this.gamePlayer, classes);
                kitStats.setLevel(Integer.parseInt(kv.getString("level")));
                kitStats.setEquipLevel(Integer.parseInt(kv.getString("equipLevel")));
                kitStats.setSkillLevel(Integer.parseInt(kv.getString("skillLevel")));
                kitStats.setSkill2Level(Integer.parseInt(kv.getString("skill2Level")));
                kitStats.setSkill3Level(Integer.parseInt(kv.getString("skill3Level")));
                kitStats.setSkill4Level(Integer.parseInt(kv.getString("skill4Level")));
                kitStats.setWins(Integer.parseInt(kv.getString("wins")));
                kitStats.setMvps(Integer.parseInt(kv.getString("mvps")));
                kitStats.setGames(Integer.parseInt(kv.getString("games")));
                kitStats.setFinalKills(Integer.parseInt(kv.getString("finalKills")));
                kitStats.setFinalAssists(Integer.parseInt(kv.getString("finalAssists")));
                kitStats.setEnderChest(Integer.parseInt(kv.getString("enderChest")));
                kitStats.setMasterPoints(Integer.parseInt(kv.getString("masterPoints")));
                kitStats.setEnableGoldTag(Boolean.parseBoolean(kv.getString("enableGoldTag")));
                kitStats.setPlayTime(Long.parseLong(kv.getString("playTime")));
                inventory = kv.getString("inventory");
                if (!inventory.equalsIgnoreCase("null") && !inventory.equalsIgnoreCase("")) {
                    kitStats.setInventory((JsonObject) gson.fromJson(inventory, JsonObject.class));
                }
                this.kitStatsMap.put(classes, kitStats);
            }
        }
    }

    public Classes getSelected() {
        return ClassesManager.getClassesByName(this.selected);
    }

    public void setSelected(String classesName) {
        if (ClassesManager.getClassesByName(classesName) == null) {
            return;
        }
        database.dbUpdate("megawalls_stats", new KeyValue("Selected", classesName), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
        this.selected = classesName;
    }

    public ClassesSkin getSelectedSkin() {
        return this.selectedSkin;
    }

    public void setSelectedSkin(ClassesSkin selectedSkin) {
        database.dbUpdate("megawalls_stats", new KeyValue("Skin", selectedSkinName), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
        this.selectedSkin = selectedSkin;
        this.selectedSkinName = selectedSkin.getName();
    }

    public void addKills(int amount) {
        this.kills += amount;
        RewardManager.addProgress(this.gamePlayer, 4, "kills", amount);
        RewardManager.addProgress(this.gamePlayer, 5, "kills", amount);
        database.dbUpdate("megawalls_stats", new KeyValue("kills", this.kills), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addFinalKills(int amount) {
        this.finalKills += amount;
        RewardManager.addProgress(this.gamePlayer, 4, "kills", amount);
        RewardManager.addProgress(this.gamePlayer, 5, "kills", amount);
        database.dbUpdate("megawalls_stats", new KeyValue("finalKills", this.finalKills), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void addWins() {
        ++this.wins;
        database.dbUpdate("megawalls_stats", new KeyValue("wins", this.wins), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }


    public void addMvp() {
        ++this.mvp;
        database.dbUpdate("megawalls_stats", new KeyValue("mvps", this.mvp), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }


    public void addGames() {
        ++this.games;
        RewardManager.addProgress(this.gamePlayer, 1, "games", 1);
        RewardManager.addProgress(this.gamePlayer, 3, "games", 1);
        if (game.isOver() && game.getWinner() != null && game.getWinner().isInTeam(this.gamePlayer)) {
            if (this.gamePlayer.getKills() >= 1 || this.gamePlayer.getFinalKills() >= 1) {
                RewardManager.addProgress(this.gamePlayer, 2, "games", 1);
            }
            RewardManager.addProgress(this.gamePlayer, 3, "wins", 1, false);
        }
        RewardManager.addProgress(this.gamePlayer, 5, "games", 1);
        database.dbUpdate("megawalls_stats", new KeyValue("games", this.games), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void giveCoins(CurrencyPackage currencyPackage) {
        if (MegaWalls.isActiveMode()) {
            currencyPackage.booster(1.0, "§6Happy Hour!");
        }
        int amount = currencyPackage.getCurrency();
        this.gamePlayer.sendMessage("§6+" + amount + " coins! " + currencyPackage.getTip());
        this.getedcoins += amount;
        this.coins += amount;
        database.dbUpdate("megawalls_stats", new KeyValue("coins", this.coins), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void giveCoins(int amount) {
        this.getedcoins += amount;
        this.coins += amount;
        database.dbUpdate("megawalls_stats", new KeyValue("coins", this.coins), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void takeCoins(int amount) {
        if (this.coins - amount < 0) {
            return;
        }
        this.coins -= amount;
        database.dbUpdate("megawalls_stats", new KeyValue("coins", this.coins), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void giveMythicDust(int amount) {
        this.mythicDust += amount;
        database.dbUpdate("megawalls_stats", new KeyValue("mythicDust", this.mythicDust), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void takeMythicDust(int amount) {
        if (this.mythicDust - amount < 0) {
            return;
        }
        this.mythicDust -= amount;
        database.dbUpdate("megawalls_stats", new KeyValue("mythicDust", this.mythicDust), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public TaskStatsContainer getTaskStats(Task task) {
        return this.taskMap.getOrDefault(task, null);
    }

    public KitStatsContainer getKitStats(Classes classes) {
        return this.kitStatsMap.getOrDefault(classes, null);
    }

    public List<Classes> unlockedClasses() {
        ArrayList<Classes> list = new ArrayList<Classes>();
        for (Map.Entry<Classes, KitStatsContainer> entry : this.kitStatsMap.entrySet()) {
            if (!this.isUnlocked(entry.getKey())) continue;
            list.add(entry.getKey());
        }
        return list;
    }

    public List<Classes> unlockedNormalClasses() {
        ArrayList<Classes> list = new ArrayList<Classes>();
        for (Classes classes : ClassesManager.getNormalClasses()) {
            if (!this.isUnlocked(classes)) continue;
            list.add(classes);
        }
        return list;
    }

    public List<Classes> unlockedMythicClasses() {
        ArrayList<Classes> list = new ArrayList<Classes>();
        for (Classes classes : ClassesManager.getMythicClasses()) {
            if (!this.isUnlocked(classes)) continue;
            list.add(classes);
        }
        return list;
    }

    public boolean isUnlocked(Classes classes) {
        return this.kitStatsMap.get(classes).getLevel() >= 1;
    }

    public KitStatsContainer usuallyUsedClasses() {
        KitStatsContainer kitStats = null;
        for (Map.Entry<Classes, KitStatsContainer> entry : this.kitStatsMap.entrySet()) {
            if (kitStats == null) {
                kitStats = entry.getValue();
                continue;
            }
            if (entry.getValue().getPlayTime() <= kitStats.getPlayTime()) continue;
            kitStats = entry.getValue();
        }
        return kitStats;
    }

    public List<Classes> maxedClasses() {
        ArrayList<Classes> list = new ArrayList<Classes>();
        for (Classes classes : this.unlockedClasses()) {
            if (!this.kitStatsMap.get(classes).isMaxed()) continue;
            list.add(classes);
        }
        return list;
    }

    public int masterClasses() {
        int i = 0;
        List<Classes> unlocked = this.unlockedClasses();
        for (Classes classes : unlocked) {
            i += this.kitStatsMap.get(classes).getLevel();
        }
        return i - unlocked.size();
    }

    public int getChallenge(int challengeId) {
        return this.challenges.get(String.valueOf(challengeId)).getAsInt();
    }

    public String getChallengeLastFinished(int challengeId) {
        return this.challenges.get(String.valueOf(challengeId) + "_lastFinished").getAsString();
    }

    public void addChallenge(int challengeId, int amount) {
        JsonObject newJson = new JsonObject();
        for (Map.Entry entry : this.challenges.entrySet()) {
            if (((String) entry.getKey()).equals(String.valueOf(challengeId))) {
                if (Challenge.isLater(Challenge.current(), this.challenges.get(String.valueOf(challengeId) + "_lastFinished").getAsString())) {
                    newJson.addProperty(String.valueOf(challengeId), (Number) amount);
                    continue;
                }
                newJson.addProperty(String.valueOf(challengeId), (Number) (this.challenges.get(String.valueOf(challengeId)).getAsInt() + amount));
                continue;
            }
            newJson.add((String) entry.getKey(), (JsonElement) entry.getValue());
        }
        this.challenges = newJson;
        database.dbUpdate("megawalls_challenges", new KeyValue("challenges", this.challenges.toString()), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void setChallengeFinished(int challengeId) {
        String current = Task.current();
        JsonObject newJson = new JsonObject();
        for (Map.Entry entry : this.challenges.entrySet()) {
            if (((String) entry.getKey()).equals(String.valueOf(challengeId) + "_lastFinished")) {
                newJson.addProperty(String.valueOf(challengeId) + "_lastFinished", current);
                continue;
            }
            newJson.add((String) entry.getKey(), (JsonElement) entry.getValue());
        }
        this.challenges = newJson;
        database.dbUpdate("megawalls_challenges", new KeyValue("challenges", this.challenges.toString()), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public int getKills() {
        return this.kills;
    }

    public int getFinalKills() {
        return this.finalKills;
    }

    public int getWins() {
        return this.wins;
    }

    public int getMvp() {
        return mvp;
    }

    public int getGames() {
        return this.games;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getMythicDust() {
        return this.mythicDust;
    }

    public EffectStatsContainer getEffectStats() {
        return this.effectStats;
    }

    public JsonObject getChallenges() {
        return this.challenges;
    }

    public Map<Task, TaskStatsContainer> getTaskMap() {
        return this.taskMap;
    }

    public Map<Classes, KitStatsContainer> getKitStatsMap() {
        return this.kitStatsMap;
    }

    public Classes getCacheSelected() {
        return this.cacheSelected;
    }

    public int getGetedcoins() {
        return getedcoins;
    }

    public void setCacheSelected(Classes cacheSelected) {
        this.cacheSelected = cacheSelected;
    }
}

