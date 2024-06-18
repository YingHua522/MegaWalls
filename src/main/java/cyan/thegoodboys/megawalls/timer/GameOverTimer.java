/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameState;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.stats.CurrencyPackage;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.stats.PlayerStats;
import cyan.thegoodboys.megawalls.util.FireWorkUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameOverTimer implements Runnable {
    private final Game game;
    private final GameTeam winner;
    private int counter = 25;
    private GamePlayer mvp = null;
    private final long endTime;
    private boolean isDraw;

    public GameOverTimer(Game game) {
        this.game = game;
        game.setState(GameState.STOP);
        game.setGameOverTimer(this);
        this.endTime = System.currentTimeMillis();
        this.winner = determineWinningTeam(game.getTeams());
        if (this.isDraw) {
            for (GamePlayer gamePlayer : GamePlayer.getGamePlayers()) {
                PlayerStats stats = gamePlayer.getPlayerStats();
                stats.addGames();
                stats.giveCoins(new CurrencyPackage(100, "(Participation Bonus)"));
                game.broadcastTitle("§6平局！", "§f游戏没有获胜玩家", 0, 200, 0);
                KitStatsContainer kitStats = stats.getKitStats(stats.getSelected());
                kitStats.addGames();
                kitStats.addPlayTime(this.endTime - this.game.getStartTime());
            }
            ArrayList<String> messages = new ArrayList<>();
            messages.add("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            messages.add("§f                                       §l超级战墙");
            messages.add(" ");
            messages.add("                                   §c§l没有胜利队伍！");
            messages.add("                               §7最终击杀玩家");
            messages.add(" ");
            Iterator<GamePlayer> it = GamePlayer.sortFinalKills().iterator();
            for (int i = 0; it.hasNext() && i < 3; ++i) {
                GamePlayer gamePlayer = it.next();
                messages.add("                               §7" + gamePlayer.getName() + " - §e" + gamePlayer.getFinalKills() + " 最终击杀");
                RewardManager.addChallenge(gamePlayer, 3, 1);
            }
            messages.add(" ");
            messages.add("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            for (String line : messages) {
                Bukkit.broadcastMessage(line);
            }
        }
        if (this.winner != null) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            messages.add("§f                                       §l超级战墙");
            messages.add(" ");
            messages.add("                                   §e§l胜利队伍 §7- " + this.winner.getTeamColor().getChatColor() + this.winner.getTeamColor().getText() + "Team");
            messages.add("                               §7最终击杀玩家");
            messages.add(" ");
            Iterator<GamePlayer> it = GamePlayer.sortFinalKills().iterator();
            for (int i = 0; it.hasNext() && i < 3; ++i) {
                GamePlayer gamePlayer = it.next();
                messages.add("                               §7" + gamePlayer.getName() + " - §e" + gamePlayer.getFinalKills() + " 最终击杀");
                RewardManager.addChallenge(gamePlayer, 3, 1);
            }
            messages.add(" ");
            messages.add("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

            for (String line : messages) {
                Bukkit.broadcastMessage(line);
            }
        }
    }

    private GameTeam determineWinningTeam(List<GameTeam> teams) {
        GameTeam winningTeam = null;
        int maxFinals = 0;
        int maxFinalsTeams = 0;
        int aliveTeams = 0;

        for (GameTeam team : teams) {
            int teamFinals = 0;
            boolean teamHasAlivePlayer = false;
            for (GamePlayer player : team.getGamePlayers()) {
                if (!player.isSpectator()) {
                    teamFinals += player.getFinalKills();
                    teamHasAlivePlayer = true;
                }
            }

            if (teamHasAlivePlayer) {
                aliveTeams++;
                if (teamFinals > maxFinals) {
                    winningTeam = team;
                    maxFinals = teamFinals;
                    maxFinalsTeams = 1;
                } else if (teamFinals == maxFinals && team != winningTeam) {
                    maxFinalsTeams++;
                }
            }
        }

        if (aliveTeams == 1) {
            return winningTeam;
        } else if (maxFinalsTeams > 1) {
            isDraw = true;
            return null;
        }

        return winningTeam;
    }

    public void displayMVPTitle() {
        int maxFinals = 0;
        for (GameTeam team : game.getTeams()) {
            for (GamePlayer player : team.getGamePlayers()) {
                if (!player.isSpectator() && player.getFinalKills() > maxFinals) {
                    mvp = player;
                    maxFinals = player.getFinalKills();
                }
            }
        }
        if (mvp != null) {
            TextComponent tp;
            PlayerStats stats = mvp.getPlayerStats();
            KitStatsContainer kitStats = stats.getKitStats(stats.getSelected());
            stats.addMvp();
            kitStats.addMvp();
            game.broadcastTitle("§b本局MVP", "§a" + mvp.getName() + " " + maxFinals + "最终击杀", 10, 20, 10);
            mvp.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            mvp.sendMessage("§e§l在本局里你是MVP玩家，特地为你增加了mvp的数据及职业数据！愿你今后继续加油！");
            tp = new TextComponent("§a点击领取奖励");
            tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "coins give " + mvp.getName() + " 200"));
            tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§b点击领取奖励！")).create()));
            mvp.getPlayer().spigot().sendMessage(new TextComponent("      "), tp, new TextComponent("       "));
            mvp.sendMessage("");
            mvp.sendMessage("§a你领取了MVP专用的奖励！");
            mvp.sendMessage("");
            mvp.sendMessage("§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        }else {
            game.broadcastTitle("§7无MVP玩家！", "", 10, 20, 10);
        }
    }

    @Override
    public void run() {
        if (counter == 15) {
            this.displayMVPTitle();
        }
        if (mvp != null) {
            FireWorkUtil.spawnFireWork(mvp.getPlayer().getLocation().add(0.0D, 2.0D, 0.0D), mvp.getPlayer().getLocation().getWorld());
        }
        if (this.counter == 25 && this.winner != null) {
            for (GamePlayer gamePlayer : this.winner.getGamePlayers()) {
                PlayerStats stats = gamePlayer.getPlayerStats();
                stats.addWins();
                stats.addGames();
                gamePlayer.sendTitle("§6胜利！", "", 0, 200, 0);
                gamePlayer.sendMessage("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                gamePlayer.sendMessage("");
                gamePlayer.sendMessage("§fTotal Damage: §b" + gamePlayer.getDamage() + " §c❤");
                gamePlayer.sendMessage("§fTaken Damage: §b" + gamePlayer.getDef() + " §c❤");
                gamePlayer.sendMessage("§fFinal Kill: §b" + gamePlayer.getFinalKills());
                gamePlayer.sendMessage("§fFinal Assists: §b" + gamePlayer.getFinalaKills());
                gamePlayer.sendMessage("");
                gamePlayer.sendMessage("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                stats.giveCoins(new CurrencyPackage(100, "(Participation Bonus)"));
                stats.giveCoins(new CurrencyPackage(180, "(Win)"));
                KitStatsContainer kitStats = stats.getKitStats(stats.getSelected());
                kitStats.addWins();
                kitStats.addGames();
                kitStats.addPlayTime(this.endTime - this.game.getStartTime());
                if (!this.winner.isWitherDead()) continue;
                RewardManager.addChallenge(gamePlayer, 4, 1);
            }
            List<GamePlayer> otherList = GamePlayer.getGamePlayers();
            otherList.removeAll(this.winner.getGamePlayers());
            for (GamePlayer gamePlayer : otherList) {
                EffectStatsContainer effectStats = gamePlayer.getPlayerStats().getEffectStats();
                PlayerStats stats = gamePlayer.getPlayerStats();
                gamePlayer.sendTitle("§7失败", "§f胜利队伍§7- " + effectStats.getColor(this.winner.getTeamColor()).getChatColor() + this.winner.getTeamColor().getText() + "Team", 0, 60, 0);
                gamePlayer.sendMessage("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                gamePlayer.sendMessage("");
                gamePlayer.sendMessage("§fTotal Damage: §b" + gamePlayer.getDamage() + " §c❤");
                gamePlayer.sendMessage("§fTaken Damage: §b" + gamePlayer.getDef() + " §c❤");
                gamePlayer.sendMessage("§fFinal Kill: §b" + gamePlayer.getFinalKills());
                gamePlayer.sendMessage("§fFinal Assists: §b" + gamePlayer.getFinalaKills());
                gamePlayer.sendMessage("");
                gamePlayer.sendMessage("§b▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
            }

        } else if (this.counter == 5) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                MegaWalls.getInstance().tpToLobby(player);
            }
        } else if (this.counter == 0) {
            Bukkit.shutdown();
        }
        --this.counter;
    }
}

