/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.classes.mythic.assassin.Assassin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;

public class AssassinTimer implements Runnable {
    @Override
    public void run() {
        for (GamePlayer online : GamePlayer.getOnlinePlayers()) {
            for (GamePlayer rec : GamePlayer.getOnlinePlayers()) {
                if (Assassin.skill.contains(online) && Assassin.AssassinNameTag.containsKey(online)) {
                    PlayerUtils.hideArmor(online.getPlayer(),rec.getPlayer());
                }
            }
        }
    }
}