/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.game.Game;

public class PlayerVisibleTimer implements Runnable {
    private Game game;

    public PlayerVisibleTimer(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
    }
}

