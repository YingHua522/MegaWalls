/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.block.BlockBreakEvent
 */
package cyan.thegoodboys.megawalls.classes;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.event.block.BlockBreakEvent;

public abstract class CollectSkill extends Skill {
    public CollectSkill(String name, Classes classes) {
        super(name, classes);
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        return true;
    }

    public abstract void onBlockBreak(KitStatsContainer var1, BlockBreakEvent var2);
}

