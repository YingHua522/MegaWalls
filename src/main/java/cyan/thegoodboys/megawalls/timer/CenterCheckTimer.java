/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CenterCheckTimer implements Runnable {
    private final Game game;

    public CenterCheckTimer(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
            if (gamePlayer.isSpectator() || this.game.getCenterArea().isInRegion(gamePlayer.getPlayer().getLocation())) {
                continue;
            }
            gamePlayer.sendTitle("", "§c设法到达中间阻止饥饿!", 0, 20, 0);
            gamePlayer.sendMessage("§c设法到达中间阻止饥饿!");

            gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, HungerIncreaseTimer.hungerLevel));
        }
    }
}

