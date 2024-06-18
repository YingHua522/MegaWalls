/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.mythic.phoenix.Phoenix;
import cyan.thegoodboys.megawalls.game.GamePlayer;

import java.util.HashMap;
import java.util.Map;


public class OneTickTimer implements Runnable {
    public static final Map<GamePlayer, Integer> noVelcityCheck = new HashMap<GamePlayer, Integer>();

    @Override
    public void run() {
        for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
            if (Phoenix.attackTick.getOrDefault(online, 0) > 0) {
                Phoenix.attackTick.put(online, Phoenix.attackTick.getOrDefault(online, 0) - 1);
            }
            if (noVelcityCheck.getOrDefault(online, 0) > 0) {
                noVelcityCheck.put(online, noVelcityCheck.getOrDefault(online, 0) - 1);
            }
            MegaWalls.getInstance().getGame().registerScoreboardTeams();
        }
    }
}


