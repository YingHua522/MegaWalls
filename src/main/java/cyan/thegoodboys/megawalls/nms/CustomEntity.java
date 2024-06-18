/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 */
package cyan.thegoodboys.megawalls.nms;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

public interface CustomEntity {
    GamePlayer getGamePlayer();

    void setGamePlayer(GamePlayer var1);

    CraftEntity getBukkitEntity();
}

