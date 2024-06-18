/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Furnace
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.game.GameType;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class ProtectStage extends GameStage {
    public ProtectStage() {
        super("距离开门还有", 15, 10, 0);
    }

    public void excute(final Game game) {
        Iterator var2 = game.getTeams().iterator();

        while (var2.hasNext()) {
            GameTeam gameTeam = (GameTeam) var2.next();
            gameTeam.getReserveWall().falldown();
        }

        game.broadcastMessage("§c§l做好防御准备！");
        var2 = MegaWalls.getIngame().iterator();

        while (var2.hasNext()) {
            GamePlayer gamePlayer = (GamePlayer) var2.next();
            ClassesManager.giveItems(gamePlayer);
        }

        if (game.getGameType() == GameType.NORMAL) {
            (new BukkitRunnable() {
                public void run() {
                    if (game.isWallsFall()) {
                        this.cancel();
                    } else {

                        for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
                            gamePlayer.getProtectedBlock().stream().filter((block) -> block.getType() == Material.BURNING_FURNACE).forEach((block) -> {
                                Furnace furnace = (Furnace) block.getState();
                                furnace.setCookTime((short) (furnace.getCookTime() + 60));
                            });
                        }
                    }

                }
            }).runTaskTimer(MegaWalls.getInstance(), 0L, 20L);
        }

    }

    public void excuteLeftSeconds(Game game, int left) {
        if (left == 10 || left <= 5) {
            game.broadcastMessage("§e门将在§c" + left + "§e秒后开启！");
        }
    }
}

