/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.config.FileConfig;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GameParty;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.scoreboard.FixedBody;
import cyan.thegoodboys.megawalls.scoreboard.Line;
import cyan.thegoodboys.megawalls.scoreboard.SidebarBoard;
import cyan.thegoodboys.megawalls.scoreboard.TextLine;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoardTimer implements Runnable {
    public static final Map<GamePlayer, SidebarBoard> scoreboards = new HashMap<>();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
    public static final FileConfig congig = MegaWalls.getInstance().getConfig();
    private static final String WAIT_TITLE = "§e§l超级战墙";
    private final Game game;
    public GamePlayer gp;

    public ScoreBoardTimer(Game game) {
        this.game = game;
    }

    public void run() {
        if (this.game != null) {
            for (GameTeam gameTeam : game.getTeams()) {
                if (!gameTeam.getWitherLocation().getChunk().isLoaded()) {
                    gameTeam.getWitherLocation().getChunk().load();
                }
            }

            for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
                // 创建一个新的异步任务来更新计分板
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // 在主线程中创建计分板
                        Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> updateScoreboard(gamePlayer));
                    }
                }.runTaskAsynchronously(MegaWalls.getInstance());
            }
        }
        if (gp != null) {
            // 创建一个新的异步任务来更新计分板
            new BukkitRunnable() {
                @Override
                public void run() {
                    // 在主线程中创建计分板
                    Bukkit.getScheduler().runTask(MegaWalls.getInstance(), () -> updateScoreboard(gp));
                }
            }.runTaskAsynchronously(MegaWalls.getInstance());
        }
    }

    private void updateScoreboard(GamePlayer gamePlayer) {
        SidebarBoard b;
        if (!scoreboards.containsKey(gamePlayer)) {
            b = SidebarBoard.of(MegaWalls.getInstance(), gamePlayer.getPlayer());
            gamePlayer.getPlayer().setScoreboard(b.getScoreboard());
            b.setHead(TextLine.of(WAIT_TITLE));
            b.setBody(this.getBody(gamePlayer));
            scoreboards.put(gamePlayer, b);
        } else {
            b = scoreboards.get(gamePlayer);
            gamePlayer.getPlayer().setScoreboard(b.getScoreboard());
            EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
            b.setHead(TextLine.of(gamePlayer.getGameTeam() == null ? WAIT_TITLE : effectStats.getColor(gamePlayer.getGameTeam().getTeamColor()).getChatColor() + "§l超级战墙"));
            b.setBody(this.getBody(gamePlayer));
        }
        b.update();
    }

    private FixedBody getBody(GamePlayer gamePlayer) {
        ArrayList<Line> lines = new ArrayList<>();
        if (this.game.isWaiting()) {
            addLobbyScoreBoard(gamePlayer, lines);
        } else if (this.game.isStarted()) {
            addGameStartSoreBoard(gamePlayer, lines);
        }

        return FixedBody.of(lines);
    }

    private void addLobbyScoreBoard(GamePlayer gamePlayer, ArrayList<Line> lines) {
        lines.add(TextLine.of("§7" + dateFormat.format(new Date())));
        lines.add(TextLine.of(" "));
        lines.add(TextLine.of("§f地图: §a" + this.game.getMapName()));
        lines.add(TextLine.of("§f玩家: §a" + MegaWalls.getIngame().size() + "/" + this.game.getMaxPlayers()));
        lines.add(TextLine.of("  "));
        lines.add(TextLine.of("§f倒计时: §a" + this.game.getFormattedTime(this.game.getGameTimer().getTime())));
        if (this.game.getMinPlayers() >= MegaWalls.getIngame().size()) {
            lines.add(TextLine.of("§f需要玩家: §a" + (this.game.getMinPlayers() - MegaWalls.getIngame().size())));
        } else {
            lines.add(TextLine.of("   "));
            lines.add(TextLine.of("§f等待玩家加入..."));
            lines.add(TextLine.of("§f已允许更多玩家加入。"));
        }
        lines.add(TextLine.of("   "));
        lines.add(TextLine.of("§f已选择职业:"));
        lines.add(TextLine.of("§a" + gamePlayer.getPlayerStats().getSelected().getDisplayName()));
        GameParty party = this.game.getPlayerParty(gamePlayer);
        if (party != null && !party.getMates(gamePlayer).isEmpty()) {
            lines.add(TextLine.of("   "));
            lines.add(TextLine.of("§f队友:"));
            for (GamePlayer mate : party.getMates(gamePlayer)) {
                lines.add(TextLine.of("§d" + mate.getName()));
            }
        }
        if (MegaWalls.isMythicMode()) {
            lines.add(TextLine.of("    "));
            lines.add(TextLine.of("§6神话游戏"));
            lines.add(TextLine.of("§e职业满级，双倍硬币。" + "持续§a§l" + this.getFormattedTime(MegaWalls.getMythicMode() - System.currentTimeMillis())));
        }
        lines.add(TextLine.of("     "));
        lines.add(TextLine.of(congig.getString("server-name").replace("&", "§")));
    }


    private String getFormattedTime(long l) {
        int time = (int) l / 1000;
        int min = (int) Math.floor((double) time / 60);
        int sec = time % 60;
        String minStr = min < 10 ? "0" + min : String.valueOf(min);
        String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
        return minStr + ":" + secStr;
    }

    private void addGameStartSoreBoard(GamePlayer gamePlayer, ArrayList<Line> lines) {
        lines.add(TextLine.of("§720" + dateFormat.format(new Date())));
        lines.add(TextLine.of(this.game.getStageManager().formattedNextStage()));
        lines.add(TextLine.of("  "));

        String[] teamLines = this.game.getScoreboardTeamLines(gamePlayer);
        for (String teamLine : teamLines) {
            lines.add(TextLine.of(teamLine));
        }
        lines.add(TextLine.of("   "));
        lines.add(TextLine.of("§a" + gamePlayer.getKills() + " §f击杀数 §a" + gamePlayer.getaKills() + " §f助攻数"));
        lines.add(TextLine.of("§a" + gamePlayer.getFinalKills() + " §f最终击杀 §a" + gamePlayer.getFinalaKills() + " §f最终助攻"));
        lines.add(TextLine.of("§6" + gamePlayer.getPlayerStats().getGetedcoins() + " §f硬币"));
        lines.add(TextLine.of("    "));
        lines.add(TextLine.of(congig.getString("server-name").replace("&", "§")));
    }
}




