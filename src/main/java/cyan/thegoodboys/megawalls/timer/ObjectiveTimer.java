/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.classes.mythic.assassin.Assassin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.spectator.SpectatorSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class ObjectiveTimer implements Runnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = GamePlayer.get(p.getUniqueId());
            int i = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                final double hp = (player).getHealth();
                final double ahp = ((CraftPlayer) player).getHandle().getAbsorptionHearts();
                Objective obj = p.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
                if (obj == null) {
                    obj = p.getScoreboard().registerNewObjective(String.valueOf(System.currentTimeMillis() + (long) i), "health");
                    obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                    obj.setDisplayName(ChatColor.RED + "❤");
                }
                obj.setDisplayName(ChatColor.RED + "❤");
                obj.getScore(player).setScore((int) (hp + ahp));
                ++i;
                obj = p.getScoreboard().getObjective(DisplaySlot.PLAYER_LIST);
                if (obj == null) {
                    obj = p.getScoreboard().registerNewObjective(String.valueOf(System.currentTimeMillis() + (long) i), "dummy");
                    obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
                }
                obj.getScore(player).setScore((int) (hp + ahp));
                ++i;
                GamePlayer otherPlayer = GamePlayer.get(player.getUniqueId());
                assert gamePlayer != null;
                assert otherPlayer != null;
                if (gamePlayer.isSpectator() && otherPlayer.isSpectator()) {
                    if (SpectatorSettings.get(gamePlayer).getOption(SpectatorSettings.Option.HIDEOTHER)) {
                        p.hidePlayer(player);
                        continue;
                    }
                    p.showPlayer(player);
                    continue;
                }
                if (!gamePlayer.isSpectator() && !otherPlayer.isSpectator()) {
                    p.showPlayer(player);
                    player.showPlayer(p);
                    continue;
                }
                if (!gamePlayer.isSpectator() && otherPlayer.isSpectator()) {
                    p.hidePlayer(player);
                    continue;
                }
                if (Assassin.skill.contains(gamePlayer) && Assassin.AssassinNameTag.containsKey(gamePlayer)) {
                    player.hidePlayer(p);
                }
                if (gamePlayer.isSpectator() && !otherPlayer.isSpectator()) {
                    player.hidePlayer(p);
                    continue;
                }
                p.showPlayer(player);
                player.showPlayer(p);
            }
        }
    }

}

