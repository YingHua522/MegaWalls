/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.reward;

import com.google.gson.JsonObject;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.TaskStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class RewardManager {
    public static final JsonObject defaultChallenge = new JsonObject();
    private static final Map<Integer, Task> taskMap = new HashMap<Integer, Task>();
    private static final Map<Integer, Challenge> challengeMap = new HashMap<Integer, Challenge>();
    private static boolean registered = false;

    public static Task getTask(int id) {
        return taskMap.getOrDefault(id, null);
    }

    public static void registerTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public static List<Task> getTasks() {
        return new ArrayList<Task>(taskMap.values());
    }

    public static Challenge getChallenge(int id) {
        return challengeMap.getOrDefault(id, null);
    }

    public static void registerChallenge(Challenge challenge) {
        challengeMap.put(challenge.getId(), challenge);
    }

    public static List<Challenge> getChallenges() {
        return new ArrayList<Challenge>(challengeMap.values());
    }

    public static boolean registerAll() {
        if (registered) {
            return false;
        }
        RewardManager.registerTask(new Task(1, Task.Type.DAILY, "Game of the Day") {

            @Override
            public List<String> getInfo(TaskStatsContainer container) {
                int now = container.getJsonObject().get("games").getAsInt();
                if (now > 1) {
                    now = 1;
                }
                return Arrays.asList("\u00a77Play a Megawalss Game \u00a7b(\u00a76" + now + "\u00a7b/\u00a761\u00a7b)");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(1500) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(1500);
            }

            @Override
            public boolean isFinish(TaskStatsContainer container) {
                int now = container.getJsonObject().get("games").getAsInt();
                return now >= 1;
            }

            @Override
            public JsonObject defaultData() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("games", (Number) 0);
                return jsonObject;
            }
        });
        RewardManager.registerTask(new Task(2, Task.Type.DAILY, "Win quest") {

            @Override
            public List<String> getInfo(TaskStatsContainer container) {
                int now = container.getJsonObject().get("games").getAsInt();
                if (now > 1) {
                    now = 1;
                }
                return Arrays.asList("\u00a77\u5728\u8d85\u7ea7\u6218\u5899\u4e2d,\u8d62\u4e00\u573a\u6bd4\u8d5b", "\u00a77\u5e76\u4e14\u81f3\u5c11\u83b7\u5f971\u51fb\u6740 \u00a7b(\u00a76" + now + "\u00a7b/\u00a761\u00a7b)");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(1500) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(1500);
            }

            @Override
            public boolean isFinish(TaskStatsContainer container) {
                int now = container.getJsonObject().get("games").getAsInt();
                return now >= 1;
            }

            @Override
            public JsonObject defaultData() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("games", (Number) 0);
                return jsonObject;
            }
        });
        RewardManager.registerTask(new Task(3, Task.Type.MYTHIC, "Mythic quest") {

            @Override
            public List<String> getInfo(TaskStatsContainer container) {
                int wins;
                int games = container.getJsonObject().get("games").getAsInt();
                if (games > 3) {
                    games = 3;
                }
                if ((wins = container.getJsonObject().get("wins").getAsInt()) > 1) {
                    wins = 1;
                }
                return Arrays.asList("\u00a77\u53c2\u4e0e3\u6b21\u8d85\u7ea7\u6218\u5899\u6807\u51c6\u6a21\u5f0f \u00a7b(\u00a76" + games + "\u00a7b/\u00a763\u00a7b)", "\u00a77\u83b7\u5f971\u573a\u8d85\u7ea7\u6218\u5899\u6807\u51c6\u6a21\u5f0f\u80dc\u5229 \u00a7b(\u00a76" + wins + "\u00a7b/\u00a761\u00a7b)");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+\u00a7e1\u8d85\u7ea7\u6218\u5899\u795e\u8bdd\u4e4b\u5c18");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveMythicDust(1);
            }

            @Override
            public boolean isFinish(TaskStatsContainer container) {
                int games = container.getJsonObject().get("games").getAsInt();
                int wins = container.getJsonObject().get("wins").getAsInt();
                return games >= 3 && wins >= 1;
            }

            @Override
            public JsonObject defaultData() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("games", (Number) 0);
                jsonObject.addProperty("wins", (Number) 0);
                return jsonObject;
            }
        });
        RewardManager.registerTask(new Task(4, Task.Type.DAILY, "Kills quest") {

            @Override
            public List<String> getInfo(TaskStatsContainer container) {
                int kills = container.getJsonObject().get("kills").getAsInt();
                if (kills > 15) {
                    kills = 15;
                }
                return Arrays.asList("\u00a77\u5728\u8d85\u7ea7\u6218\u5899\u4e2d\u6740\u6b7b15\u540d\u73a9\u5bb6 \u00a7b(\u00a76" + kills + "\u00a7b/\u00a7615\u00a7b)");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(1500) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(1500);
            }

            @Override
            public boolean isFinish(TaskStatsContainer container) {
                int kills = container.getJsonObject().get("kills").getAsInt();
                return kills >= 15;
            }

            @Override
            public JsonObject defaultData() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("kills", (Number) 0);
                return jsonObject;
            }
        });
        RewardManager.registerTask(new Task(5, Task.Type.WEEKLY, "Mega Waller quest") {

            @Override
            public List<String> getInfo(TaskStatsContainer container) {
                int kills;
                int games = container.getJsonObject().get("games").getAsInt();
                if (games > 15) {
                    games = 15;
                }
                if ((kills = container.getJsonObject().get("kills").getAsInt()) > 15) {
                    kills = 25;
                }
                return Arrays.asList("\u00a77\u73a915\u573a\u8d85\u7ea7\u6218\u5899 \u00a7b(\u00a76" + games + "\u00a7b/\u00a7615\u00a7b)", "\u00a77\u5728\u8d85\u7ea7\u6218\u5899\u4e2d\u6740\u6b7b25\u540d\u73a9\u5bb6 \u00a7b(\u00a76" + kills + "\u00a7b/\u00a7625\u00a7b)");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(3500) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(3500);
            }

            @Override
            public boolean isFinish(TaskStatsContainer container) {
                int games = container.getJsonObject().get("games").getAsInt();
                int kills = container.getJsonObject().get("kills").getAsInt();
                return games >= 15 && kills >= 25;
            }

            @Override
            public JsonObject defaultData() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("games", (Number) 0);
                jsonObject.addProperty("kills", (Number) 0);
                return jsonObject;
            }
        });
        RewardManager.registerChallenge(new Challenge(1, "Wither Damage") {

            @Override
            public List<String> getInfo() {
                return Arrays.asList("\u00a77\u5728\u4e00\u5c40\u6e38\u620f\u4e2d\u5bf9\u51cb\u96f6\u9020\u6210", "\u00a77\u81f3\u5c11200\u70b9\u4f24\u5bb3\u3002");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(100) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(100);

            }
        });
        RewardManager.registerChallenge(new Challenge(2, "\u4fdd\u536b\u8005") {

            @Override
            public List<String> getInfo() {
                return Arrays.asList("\u00a77\u5728\u5b88\u536b\u4f60\u7684\u51cb\u96f6\u65f6,", "\u00a77\u53d6\u5f97\u4e86\u81f3\u5c1110\u6b21\u51fb\u6740/\u52a9\u653b\u3002");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(100) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(100);
            }
        });
        RewardManager.registerChallenge(new Challenge(3, "Rage") {

            @Override
            public List<String> getInfo() {
                return Arrays.asList("\u00a77\u5728\u4e00\u5c40\u6e38\u620f\u4e2d\u83b7\u5f97\u6700\u7ec8\u51fb\u6740\u524d\u4e09\u540d\u3002");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(100) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(100);
            }
        });
        RewardManager.registerChallenge(new Challenge(4, "Win") {

            @Override
            public List<String> getInfo() {
                return Arrays.asList("\u00a77\u5728\u5df1\u65b9\u51cb\u96f6\u5df2\u7ecf\u6b7b\u4ea1\u7684\u60c5\u51b5\u4e0b\u8d62\u5f97\u4e00\u5c40\u6e38\u620f\u3002");
            }

            @Override
            public List<String> getRewardInfo() {
                return Arrays.asList("\u00a78+ \u00a76" + StringUtils.formattedCoins(100) + " \u00a77MegaWalls Coin");
            }

            @Override
            public void giveRewarad(GamePlayer gamePlayer) {
                gamePlayer.getPlayerStats().giveCoins(100);
            }
        });
        for (Challenge challenge : RewardManager.getChallenges()) {
            defaultChallenge.addProperty(String.valueOf(challenge.getId()), (Number) 0);
            defaultChallenge.addProperty(String.valueOf(challenge.getId()) + "_lastFinished", "2019-01-01");
        }
        registered = true;
        return true;
    }

    public static void addProgress(GamePlayer gamePlayer, int taskId, String name, int amount) {
        RewardManager.addProgress(gamePlayer, taskId, name, amount, true);
    }

    public static void addProgress(final GamePlayer gamePlayer, int taskId, String name, int amount, boolean message) {
        final Task task = RewardManager.getTask(taskId);
        if (task == null) {
            return;
        }
        final TaskStatsContainer container = gamePlayer.getPlayerStats().getTaskStats(task);
        if (container.isAccept() && !task.isFinish(container) && Task.isLater(Task.current(), container.getLastFinished(), task.getType())) {
            container.addProgress(name, amount);
        }
        if (message) {
            Bukkit.getScheduler().runTaskLater((Plugin) MegaWalls.getInstance(), new Runnable() {

                @Override
                public void run() {
                    if (container.isAccept() && task.isFinish(container) && Task.isLater(Task.current(), container.getLastFinished(), task.getType())) {
                        container.setAccept(false);
                        container.setJsonObject(task.defaultData());
                        container.setFinished();
                        task.giveRewarad(gamePlayer);
                        gamePlayer.sendMessage(" ");
                        gamePlayer.sendMessage("\u00a7a" + task.getType().getName() + "Daily Quest: " + task.getName() + "Completed!");
                        for (String line : task.getRewardInfo()) {
                            gamePlayer.sendMessage(" " + line);
                        }
                        gamePlayer.sendMessage(" ");
                    }
                }
            }, 40L);
        }
    }

    public static void addChallenge(final GamePlayer gamePlayer, final int challengeId, int amount) {
        final Challenge challenge = RewardManager.getChallenge(challengeId);
        if (challenge == null) {
            return;
        }
        JsonObject jsonObject = gamePlayer.getPlayerStats().getChallenges();
        if (!challenge.isFinish(jsonObject)) {
            gamePlayer.getPlayerStats().addChallenge(challengeId, amount);
            Bukkit.getScheduler().runTaskLater((Plugin) MegaWalls.getInstance(), new Runnable() {

                @Override
                public void run() {
                    gamePlayer.getPlayerStats().setChallengeFinished(challengeId);
                    challenge.giveRewarad(gamePlayer);
                    gamePlayer.sendMessage(" ");
                    gamePlayer.sendMessage("\u00a7a" + challenge.getName() + " completed!");
                    for (String line : challenge.getRewardInfo()) {
                        gamePlayer.sendMessage(" " + line);
                    }
                    gamePlayer.sendMessage(" ");
                }
            }, 40L);
        }
    }
}

