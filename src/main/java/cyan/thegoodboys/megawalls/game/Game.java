/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scoreboard.Team
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.GameStartEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.config.FileConfig;
import cyan.thegoodboys.megawalls.game.stage.StageManager;
import cyan.thegoodboys.megawalls.game.team.TeamColor;
import cyan.thegoodboys.megawalls.inv.opener.SQL;
import cyan.thegoodboys.megawalls.inventory.SpectatorInventory;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.timer.*;
import cyan.thegoodboys.megawalls.util.LuckPremsUtils;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Game {
    @Getter
    private final List<GameTeam> teams = new ArrayList<>();
    @Getter
    private final List<GameParty> parties = new ArrayList<GameParty>();
    @Getter
    private final List<GameWall> walls = new ArrayList<>();
    public int witherAlive = 4;
    @Getter
    private String mapName = null;
    @Getter
    private GameType gameType = GameType.NORMAL;
    @Getter
    private GameState state = GameState.STOP;
    @Getter
    private GameTimer gameTimer = null;
    @Getter
    private StageManager stageManager = null;
    @Getter
    private Location lobbyLocation = null;
    @Getter
    private GameRegion centerArea = null;
    @Getter
    private long startTime = 0L;
    @Getter
    private boolean wallsFall = false;
    @Getter
    private boolean witherAngry = false;
    @Getter
    private boolean deathMatch = false;
    @Getter
    private GameOverTimer gameOverTimer = null;
    @Getter
    private int minPlayers;
    @Getter
    private int maxPlayers;
    @Getter
    private int teamSize;

    public static boolean build(FileConfig config, CommandSender sender) {
        try {
            for (NPCRegistry npcRegistry : CitizensAPI.getNPCRegistries()) {
                npcRegistry.deregisterAll();
            }
            CitizensAPI.getDataFolder().delete();
            Game game = new Game();
            game.teamSize = MegaWalls.getInstance().getConfig().getInt("team-size", 20);
            game.mapName = config.getString("mapname", "默认");
            game.gameType = GameType.valueOf(config.getString("type", "NORMAL"));
            game.maxPlayers = game.gameType == GameType.DUEL ? game.teamSize * 2 : game.teamSize * 4;
            //game.minPlayers = game.maxPlayers / 4;
            game.minPlayers = MegaWalls.getInstance().getConfig().getInt("min-Players");
            game.lobbyLocation = config.getLocation("lobby");
            game.centerArea = new GameRegion(config.getBlockLocation("center-area.loc1"), config.getBlockLocation("center-area.loc2"));
            for (TeamColor teamColor : TeamColor.values()) {
                if (game.gameType == GameType.DUEL && (teamColor == TeamColor.YELLOW || teamColor == TeamColor.GREEN))
                    continue;
                game.teams.add(GameTeam.build(config, teamColor, sender));
            }
            ConfigurationSection section = config.getConfigurationSection("walls");
            for (String key : section.getKeys(false)) {
                GameWall gameWall = new GameWall(config.getBlockLocation("walls." + key + ".loc1"), config.getBlockLocation("walls." + key + ".loc2"));
                game.walls.add(gameWall);
            }
            MegaWalls.getInstance().setGame(game);
            game.init();
            return true;
        } catch (Exception e) {
            sender.sendMessage("§c加载游戏时出错: §b" + e.getMessage());
            return false;
        }
    }

    public boolean isWitherAlive() {
        return witherAlive == 1;
    }

    public void init() {
        this.setState(GameState.LOBBY);
        this.gameTimer = new GameTimer();
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), this.gameTimer, 0L, 20L);
        this.stageManager = new StageManager(this);
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new ScoreBoardTimer(this), 0L, 20L);
    }

    public void scheduleTaskTimer(long delay, long period, Runnable task) {
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), task, delay, period);
    }

    public void onStart() {
        if (!this.isWaiting()) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(this));
        this.setState(GameState.INGAME);
        this.moveFreePlayersToTeam();
        Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> {
            for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
                GameTeam gameTeam = gamePlayer.getGameTeam();
                EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
                gamePlayer.sendTitle(effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + "你在" + gameTeam.getTeamColor().getText() + "队", "§e§l巨墙将在§b§l" + (this.gameType == GameType.NORMAL ? 6 : 10) + "分钟§e§l后倒塌。", 10, 40, 10);
            }
        });
        this.startTime = System.currentTimeMillis();
        this.stageManager.start();
        ClassesManager.start();
        for (Entity entity : Bukkit.getWorld("world").getLivingEntities()) {
            if (entity instanceof Player) continue;
            entity.remove();
        }
            for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
                gamePlayer.createEnderChest();
                gamePlayer.getPlayerStats().setCacheSelected(ClassesManager.getSelected(gamePlayer));
                GameTeam gameTeam = gamePlayer.getGameTeam();
                EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
                gamePlayer.sendMessage(effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + "你被分配到" + gameTeam.getTeamColor().getText() + "队");
                gamePlayer.sendMessage("§e§l巨墙在§b§l" + (this.gameType == GameType.NORMAL ? 6 : 10) + "分钟§e§l后倒塌。");
                Player player = gamePlayer.getPlayer();
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setLevel(0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 7500, 1));
                player.setExp(0.0F);
                player.setGameMode(GameMode.SURVIVAL);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 4));
                player.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                player.sendMessage("§f                                       §l超级战墙");
                player.sendMessage(" ");
                player.sendMessage("                                §e§l巨墙还有" + (this.gameType == GameType.NORMAL ? 6 : 10) + "分钟倒塌！");
                player.sendMessage("                      §e§l采集资源,并装备好自己去摧毁敌人");
                player.sendMessage("                            §e§l及敌方凋灵,击败其他队伍");
                player.sendMessage(" ");
                player.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                    Classes classes = gamePlayer.getPlayerStats().getSelected();
                    KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
                    player.setMaxHealth(kitStats.getLevel() >= 2 ? 44.0D : 40.0D);
                    gamePlayer.sendTitle("§e§l超级战墙", effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + gameTeam.getTeamColor().getText() + "队", 10, 40, 10);
                    gameTeam.getClearLobby().falldown();
                }, 60L);
            }
        for (GameTeam gameTeam : this.teams) {
            gameTeam.spawnWither();
        }

        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            Game.this.registerScoreboardTeams();
            for (GameTeam gameTeam : Game.this.teams) {
                gameTeam.teleportToSpawn();
            }
        }, 20L);
        this.gameTimer.setTime(3925);

        scheduleTaskTimer(0L, 5L, new CompassTimer(MegaWalls.getInstance()));
        scheduleTaskTimer(0L, 20L, new ObjectiveTimer());
        scheduleTaskTimer(0L, 5L, new SkillTimer());
        scheduleTaskTimer(0L, 1L, new PlayerVisibleTimer(this));
        scheduleTaskTimer(0L, 0L, new AssassinTimer());
        scheduleTaskTimer(0L, 1L, new OneTickTimer());
    }


    public void onStop() {
        for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
            Player player = gamePlayer.getPlayer();
            player.teleport(this.lobbyLocation);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            MegaWalls.getInstance().tpToLobby(player);
        }
        this.stageManager.stop();
        this.setState(GameState.STOP);
        for (GamePlayer gamePlayer : FakePlayer.getList()) {
            FakePlayer.getList().remove(gamePlayer);
            MegaWalls.updateRejoin(gamePlayer, "none", System.currentTimeMillis());
        }
    }

    public void forceStart() {
        this.onStart();
    }

    public boolean isOver() {
        int alives = 0;
        for (GameTeam gameTeam : this.teams) {
            if (gameTeam.isDead()) continue;
            ++alives;
        }
        return alives <= 1;
    }

    public void registerScoreboardTeams() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.registerScoreboardTeams(player);
        }
    }

    public void registerScoreboardTeams(Player player) {
        GamePlayer gamePlayer1 = GamePlayer.get(player.getUniqueId());
        EffectStatsContainer effectStats = null;
        if (gamePlayer1 != null) {
            effectStats = gamePlayer1.getPlayerStats().getEffectStats();
        }
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = GamePlayer.get(player1.getUniqueId());
            GameTeam gameTeam = null;
            if (gamePlayer != null) {
                gameTeam = gamePlayer.getGameTeam();
            }
            String name = (gameTeam != null ? gameTeam.getTeamColor().getText() : "S") + player1.getName();
            if (player.getScoreboard().getTeam(name) != null) continue;
            Team team = player.getScoreboard().registerNewTeam(name);
            Classes classes = null;
            if (gamePlayer != null) {
                classes = ClassesManager.getSelected(gamePlayer);
            }
            KitStatsContainer kitStats = null;
            if (gamePlayer != null) {
                kitStats = gamePlayer.getPlayerStats().getKitStats(classes);
            }
            if (isWaiting()) {
                if (gamePlayer != null) {
                    team.setPrefix(LuckPremsUtils.getPrefix(gamePlayer.getPlayer()));
                    team.addEntry(LuckPremsUtils.getPrefix(gamePlayer.getPlayer()) + gamePlayer.getName());
                    continue;
                }
            }

            if (gamePlayer != null && gamePlayer.isSpectator()) {
                if (effectStats != null) {
                    team.setPrefix("§7✖ " + (gameTeam != null ? effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + " " : "§9"));
                }
                team.addEntry(gamePlayer.getName());
                continue;
            }
            String suffix = null;
            if (kitStats != null) {
                suffix = (kitStats.isEnableGoldTag() ? "§6" : "§7") + "[" + classes.getING() + "]";
            }
            if (!this.isWallsFall()) {
                String prefix;
                if (gamePlayer != null) {
                    if (gamePlayer1 != null && effectStats != null) {
                        if (gamePlayer1.getGameTeam().equals(gamePlayer.getGameTeam())) {
                            // 如果玩家在同一队伍，设置正常的前缀
                            prefix = effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + (effectStats.isEnablePrefix() ? gamePlayer.getGameTeam().getTeamColor().getText() : "") + " " + (effectStats.isEnableItalic() ? effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + "" + ChatColor.ITALIC : "");
                        } else {
                            // 如果玩家不在同一队伍，设置乱码的前缀
                            prefix = effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + (effectStats.isEnablePrefix() ? gamePlayer.getGameTeam().getTeamColor().getText() : "") + " " + (effectStats.isEnableItalic() ? effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + "" + ChatColor.ITALIC : "§k++");
                        }
                        String suffix1 = " " + suffix;
                        team.setPrefix(prefix);
                        team.setSuffix(suffix1);
                        team.addEntry(gamePlayer.getName());
                        continue;
                    }
                }
            }
            if (gamePlayer != null && gamePlayer.getGameTeam() == null) continue;
            if (gamePlayer != null && gamePlayer.getGameTeam() != null && effectStats != null) {
                team.setPrefix(effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + (effectStats.isEnablePrefix() ? gamePlayer.getGameTeam().getTeamColor().getText() : "") + " " + (effectStats.isEnableBold() ? effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + "" + ChatColor.BOLD : ""));
                team.setSuffix(" " + suffix);
                team.addEntry(gamePlayer.getName());
                team.setNameTagVisibility(NameTagVisibility.ALWAYS);
            }
        }
    }


    public String[] getScoreboardTeamLines(GamePlayer gamePlayer) {
        EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
        String[] lines = new String[this.gameType == GameType.DUEL ? 2 : 4];
        block0:
        for (GameTeam gameTeam : this.teams) {
            int i;
            boolean isInTeam = gameTeam.isInTeam(gamePlayer);
            StringBuilder sb = new StringBuilder();
            if (!gameTeam.isDead()) {
                sb.append(effectStats.getColor(gameTeam.getTeamColor()).getChatColor()).append(gameTeam.getTeamColor().getTag()).append(" ");
                sb.append(isInTeam ? "" : "§f");
                sb.append(gameTeam.isWitherDead() ? "玩家 " : "凋灵 " + effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + "❤");
                sb.append("§7: ").append(gameTeam.isWitherDead() ? gameTeam.getAlivePlayers().size() : "" + effectStats.getColor(gameTeam.getTeamColor()).getChatColor() + (int) gameTeam.getTeamWither().getHealth());
            } else {
                sb.append(ChatColor.GRAY).append(gameTeam.getTeamColor().getText()).append("已淘汰！");
            }
            if (gamePlayer.isSpectator()) {
                for (i = 0; i < lines.length; ++i) {
                    if (lines[i] != null) continue;
                    lines[i] = sb.toString();
                    continue block0;
                }
                continue;
            }
            if (isInTeam) {
                lines[0] = sb.toString();
                continue;
            }
            for (i = 1; i < lines.length; ++i) {
                if (lines[i] != null) continue;
                lines[i] = sb.toString();
                continue block0;
            }
        }
        return lines;
    }

    public void openSpectatorInventory(GamePlayer player) {
        if (!player.isSpectator()) {
            return;
        }
        SpectatorInventory.alivePlayers(MegaWalls.getIngame()).open(player.getPlayer());
    }

    public GameTeam getWinner() {
        for (GameTeam team : this.teams) {
            if (team.isDead()) continue;
            return team;
        }
        return null;
    }

    public boolean isTeamRegion(Location location) {
        for (GameTeam gameTeam : this.teams) {
            if (!gameTeam.getRegion().isInRegion(location)) continue;
            return true;
        }
        return false;
    }

    public boolean isProtected(Location location) {
        for (GameTeam gameTeam : this.teams) {
            if (gameTeam.getCastleRegion().isInRegion(location) && !gameTeam.isWitherDead()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWall(Location location) {
        for (GameWall gameWall : this.walls) {
            if (!gameWall.isInWall(location)) continue;
            return true;
        }
        return false;
    }

    public boolean isUnbreakable(Location location) {
        if (!this.getCenterArea().isInRegion(location) && !this.isTeamRegion(location)) {
            return true;
        }
        return this.isWall(location) && !this.isWallsFall();
    }

    public boolean isWaiting() {
        return this.state == GameState.LOBBY;
    }

    public boolean isStarted() {
        return this.state == GameState.INGAME;
    }

    public String getFormattedTime(int time) {
        int min = (int) Math.floor((double) time / 60);
        int sec = time % 60;
        String minStr = min < 10 ? "0" + min : String.valueOf(min);
        String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
        return minStr + ":" + secStr;
    }

    public GameTeam getTeamByName(String name) {
        for (GameTeam gameTeam : this.teams) {
            if (!gameTeam.getTeamColor().getText().equals(name)) continue;
            return gameTeam;
        }
        return null;
    }

    private void moveFreePlayersToTeam() {
        GameTeam lowest;
        for (GameParty gameParty : this.parties) {
            lowest = this.getLowestTeam();
            for (GamePlayer gamePlayer : gameParty.getPlayers()) {
                if (gamePlayer.getGameTeam() != null || lowest.addPlayer(gamePlayer)) continue;
                lowest = this.getLowestTeam();
            }
        }
        for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
            if (gamePlayer.getGameTeam() != null) continue;
            lowest = this.getLowestTeam();
            lowest.addPlayer(gamePlayer);
        }
    }

    private GameTeam getLowestTeam() {
        GameTeam lowest = null;
        for (GameTeam team : this.teams) {
            if (lowest == null) {
                lowest = team;
                continue;
            }
            if (team.getGamePlayers().size() >= lowest.getGamePlayers().size()) continue;
            lowest = team;
        }
        return lowest;
    }

    public GameParty getPlayerParty(GamePlayer gamePlayer) {
        for (GameParty gameParty : this.parties) {
            if (!gameParty.isInTeam(gamePlayer)) continue;
            return gameParty;
        }
        return null;
    }

    public void addParty(GameParty gameParty) {
        this.parties.add(gameParty);
    }

    public void removeParty(GameParty gameParty) {
        this.parties.remove(gameParty);
    }

    public void broadcastMessage(String message) {
        this.broadcastMessage(message, GamePlayer.getOnlinePlayers().toArray(new GamePlayer[0]));
    }

    public void broadcastMessage(String message, GamePlayer... gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.sendMessage(message);
        }
    }

    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.broadcastTitle(title, subtitle, fadeIn, stay, fadeOut, GamePlayer.getOnlinePlayers().toArray(new GamePlayer[0]));
    }


    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, GamePlayer... gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public void broadcastAction(String message) {
        this.broadcastAction(message, GamePlayer.getOnlinePlayers().toArray(new GamePlayer[0]));
    }

    public void broadcastAction(String message, GamePlayer... gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.sendActionBar(message);
        }
    }

    public void broadcastSound(Sound sound, float volume, float pitch) {
        this.broadcastSound(sound, volume, pitch, GamePlayer.getOnlinePlayers().toArray(new GamePlayer[0]));
    }

    public void broadcastSound(Sound sound, float volume, float pitch, GamePlayer... gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.playSound(sound, volume, pitch);
        }
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setWallsFall(boolean wallsFall) {
        this.wallsFall = wallsFall;
    }

    public void setWitherAngry(boolean witherAngry) {
        this.witherAngry = witherAngry;
    }

    public void setDeathMatch(boolean deathMatch) {
        this.deathMatch = deathMatch;
    }

    public void setGameOverTimer(GameOverTimer gameOverTimer) {
        this.gameOverTimer = gameOverTimer;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
}

