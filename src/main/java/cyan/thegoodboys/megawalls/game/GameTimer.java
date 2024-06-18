/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.GameOverEvent;
import cyan.thegoodboys.megawalls.api.event.GameStartEvent;
import cyan.thegoodboys.megawalls.timer.GameOverTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.UUID;

public class GameTimer implements Runnable {

    private final FileConfiguration config = MegaWalls.getInstance().getConfig();
    //设置游戏的倒计时
    private int time = config.getInt("countdown.lobby");
    private int waitSecond = 0;
    private boolean flag = false;

    @Override
    public void run() {
        if (this.time == -1) {
            return;
        }
        --this.time;
        Game game = MegaWalls.getInstance().getGame();
        if (game.isWaiting()) {
            if (Bukkit.getOnlinePlayers().size() < game.getMinPlayers()) {
                this.flag = false;
                this.time = 360;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setExp(0.0f);
                    p.setLevel(0);
                }
                this.waitSecond = this.waitSecond >= 360 ? 0 : ++this.waitSecond;
            } else {
                if (!this.flag) {
                    this.time = config.getInt("countdown.lobby");
                    this.flag = true;
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setExp(0.0f);
                    p.setLevel(this.time > 120 ? 0 : this.time);
                }
            }
            for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
                gamePlayer.sendActionBar("§c请输入 §b/team invite name §c进行组队");
            }
        } else if (game.isStarted()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    final double hp = (player).getHealth();
                    final double ahp = ((CraftPlayer) player).getHandle().getAbsorptionHearts();
                    Objective obj = p.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
                    if (obj == null) {
                        obj = p.getScoreboard().registerNewObjective(UUID.randomUUID().toString().substring(0, 6), "health");
                        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                    }
                    obj.setDisplayName(ChatColor.RED + "❤");
                    obj.getScore(player).setScore((int) (hp + ahp));
                    obj = p.getScoreboard().getObjective(DisplaySlot.PLAYER_LIST);
                    if (obj == null) {
                        obj = p.getScoreboard().registerNewObjective(UUID.randomUUID().toString().substring(0, 6), "tabhealth");
                        obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
                    }
                    obj.getScore(player).setScore((int) (hp + ahp));
                }
            }
            if (game.isOver() && game.getGameOverTimer() == null) {
                Bukkit.getPluginManager().callEvent(new GameOverEvent(game));
                Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new GameOverTimer(game), 0L, 20L);
            }
        }
        if (this.time == 0) {
            this.time = -1;
            this.finishCountdown();
        } else {
            this.broadcastState(this.time);
        }
    }

    public void broadcastState(int time) {
        Game game = MegaWalls.getInstance().getGame();
        switch (game.getState()) {
            case LOBBY: {
                if (time == 60 || time == 30 || time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    Bukkit.broadcastMessage("§e游戏将在§c" + time + "§e秒后开始！");
                    game.broadcastTitle("§c§l" + time, "§e准备战斗吧！", 0, 20, 0);
                    this.playSound(Sound.CLICK);
                    break;
                }
            }
            case INGAME:
                if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    Bukkit.broadcastMessage("§e游戏将在§c" + time + "§e秒后结束！");
                    game.broadcastTitle("§c§l" + time, "§e准备结束！", 0, 20, 0);
                    this.playSound(Sound.CLICK);
                    break;
                }
        }
    }

    public void finishCountdown() {
        switch (MegaWalls.getInstance().getGame().getState()) {
            case INGAME: {
                Bukkit.getPluginManager().callEvent(new GameOverEvent(MegaWalls.getInstance().getGame()));
                Game game = MegaWalls.getInstance().getGame();
                if (!game.isOver() || game.getGameOverTimer() != null) break;
                Bukkit.getPluginManager().callEvent(new GameOverEvent(game));
                Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new GameOverTimer(game), 0L, 20L);
                break;
            }
            case LOBBY: {
                if (MegaWalls.getIngame().size() >= MegaWalls.getInstance().getGame().getMinPlayers()) {
                    Bukkit.getPluginManager().callEvent(new GameStartEvent(MegaWalls.getInstance().getGame()));
                    MegaWalls.getInstance().getGame().onStart();
                    this.setTime(3925);
                    break;
                }
                this.setTime(60);
                break;
            }
        }
    }

    private void playSound(Sound sound) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

