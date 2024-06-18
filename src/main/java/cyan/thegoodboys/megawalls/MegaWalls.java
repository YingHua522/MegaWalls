/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataOutput
 *  com.google.common.io.ByteStreams
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package cyan.thegoodboys.megawalls;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.command.CommandHandler;
import cyan.thegoodboys.megawalls.config.FileConfig;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.FakePlayer;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.inventory.InventoryManager;
import cyan.thegoodboys.megawalls.listener.*;
import cyan.thegoodboys.megawalls.nms.CustomBrid;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.reward.Task;
import cyan.thegoodboys.megawalls.stats.SQLSettings;
import cyan.thegoodboys.megawalls.inv.ClassesMessage;
import cyan.thegoodboys.megawalls.inv.opener.SQL;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MegaWalls extends JavaPlugin {
    private static final Random random = new Random();
    private static long activeMode = System.currentTimeMillis();
    @Getter
    private static long mythicMode = System.currentTimeMillis();
    private static MegaWalls instance;
    private FileConfig config;
    private CommandHandler commandHandler;
    private cyan.thegoodboys.megawalls.inv.InventoryManager inventoryManager;
    private DataBase database;
    private Game game = null;

    public static List<GamePlayer> getIngame() {
        ArrayList<GamePlayer> gamePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : GamePlayer.getGamePlayers()) {
            if (!gamePlayer.isOnline() || gamePlayer.isSpectator()) continue;
            gamePlayers.add(gamePlayer);
        }
        return gamePlayers;
    }

    public static MegaWalls getInstance() {
        return instance;
    }

    public static boolean isActiveMode() {
        return System.currentTimeMillis() < activeMode;
    }

    public static void updateRejoin(GamePlayer gamePlayer, String server, long time) {
        DataBase database = MegaWalls.getInstance().getDataBase();
        if (!database.isValueExists("megawalls_rejoin", SQLSettings.KV_REJOIN, new KeyValue("uuid", gamePlayer.getUuid().toString()))) {
            database.dbInsert("megawalls_rejoin", new KeyValue("uuid", gamePlayer.getUuid().toString()).add("realname", gamePlayer.getName()).add("server", server).add("time", time));
            return;
        }
        database.dbUpdate("megawalls_rejoin", new KeyValue("server", server).add("time", time), new KeyValue("uuid", gamePlayer.getUuid().toString()));
    }

    public static String getMetadataValue() {
        return "MegaWalls";
    }

    public static FixedMetadataValue getFixedMetadataValue() {
        return new FixedMetadataValue(instance, true);
    }

    public static boolean isMythicMode() {
        return System.currentTimeMillis() < mythicMode;
    }

    public static void setMythicMode(long mythicMode) {
        MegaWalls.getInstance().getDataBase().dbDelete("megawalls_mythicmode", new KeyValue("enable", "true"));
        MegaWalls.getInstance().getDataBase().dbDelete("megawalls_mythicmode", new KeyValue("enable", "false"));
        MegaWalls.getInstance().getDataBase().dbInsert("megawalls_mythicmode", new KeyValue("time", mythicMode).add("enable", "true"));
        MegaWalls.mythicMode = mythicMode;
    }


    public static KeyValue getRejoin(GamePlayer gamePlayer) {
        DataBase database = MegaWalls.getInstance().getDataBase();
        if (database.isValueExists("megawalls_rejoin", SQLSettings.KV_REJOIN, new KeyValue("uuid", gamePlayer.getUuid().toString()))) {
            String server = "";
            String time = "0";
            for (KeyValue kv : database.dbSelect("megawalls_rejoin", SQLSettings.KV_REJOIN, new KeyValue("uuid", gamePlayer.getUuid().toString()))) {
                server = kv.getString("server");
                time = kv.getString("time");
            }
            return new KeyValue("server", server).add("time", time);
        }
        return null;
    }

    public static Random getRandom() {
        return random;
    }

    public static long getActiveMode() {
        return activeMode;
    }

    public static void setActiveMode(long activeMode) {
        MegaWalls.activeMode = activeMode;
    }

    public void onLoad() {
    }

    public void onDisable() {
        try {
            if (this.game != null && this.game.isStarted()) {
                this.game.onStop();
            }
            if (this.database != null) {
                this.database.close();
            }
            for (FakePlayer fakePlayer : FakePlayer.getFakePlayerMap().values()) {
                fakePlayer.delete();
            }
        } catch (Exception ignored) {
        }
    }

    public FileConfig getConfig() {
        return this.config;
    }

    public DataBase getDataBase() {
        return this.database;
    }

    public void tpToLobby(Player p) {
        try {
            ByteArrayDataOutput buf = ByteStreams.newDataOutput();
            buf.writeUTF("Connect");
            buf.writeUTF(this.getConfig().getString("lobby"));
            p.sendPluginMessage(this, "BungeeCord", buf.toByteArray());
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public void onEnable() {
        instance = this;
        this.config = new FileConfig(this);
        SQL.Register();
        ClassesMessage classesMessage = new ClassesMessage();
        classesMessage.onStart();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MegaWalls 启动成功！作者: TheGoodBoys 二次开发！");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "接插件定制QQ1512592535");
        CustomBrid customBird1 = new CustomBrid();
        customBird1.onEnable();
        this.commandHandler = new CommandHandler();
        this.inventoryManager = new cyan.thegoodboys.megawalls.inv.InventoryManager(this);
        this.inventoryManager.init();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        ClassesManager.registerAll();
        RewardManager.registerAll();
        InventoryManager.registerAll();
        if (!this.config.getBoolean("setup", false)) {
            new SetupListener(this);
            return;
        }
        this.database = DataBase.create(this.getConfig().getConfigurationSection("database"));
        this.database.createTables("megawalls_stats", SQLSettings.KV_STATS, null);
        this.database.createTables("megawalls_effects", SQLSettings.KV_EFFECTS, null);
        this.database.createTables("spectator_settings", SQLSettings.KV_SPECTATOR_SETTINGS, null);
        this.database.createTables("megawalls_challenges", SQLSettings.KV_CHALLENGES, null);
        this.database.createTables("megawalls_rejoin", SQLSettings.KV_REJOIN, null);
        for (Task task : RewardManager.getTasks()) {
            this.database.createTables("megawalls_reward_" + task.getId(), SQLSettings.KV_REWARD, null);
        }
        for (Classes classes : ClassesManager.getClasses()) {
            this.database.createTables("classes_" + classes.getName(), SQLSettings.KV_CLASSES, null);
        }
        Game.build(new FileConfig(this, "game.yml"), Bukkit.getConsoleSender());

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!isMythicMode()) {
                String s = getDataBase().dbSelectFirst("megawalls_mythicmode", "time", new KeyValue("enable", "true"));
                if (s == null) return;
                mythicMode = Long.parseLong(s);
            }
        }, 20 * 10, 20 * 20);
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public cyan.thegoodboys.megawalls.inv.InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        if (game == null) {
            return;
        }
        this.game = game;
        new BlockListener(this);
        new EntityListener(this);
        new PlayerListener(this);
        new ServerListener(this);
        new InventoryListener(this);
        new EffectListener(this);
    }
}

