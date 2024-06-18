/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.block.BlockBreakEvent
 */
package cyan.thegoodboys.megawalls.classes.mythic.werewolf;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("食肉", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.6;
            }
            case 2: {
                return 0.8;
            }
            case 3: {
                return 1.0;
            }
        }
        return 0.6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7击杀敌人有§a" + StringUtils.percent(this.getAttribute(level)) + "§7几率掉落1个Steak,");
            lore.add("    §7最终击杀可获得4个Steak。");
            return lore;
        }
        lore.add(" §8▪ §7击杀敌人有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ §a" + StringUtils.percent(this.getAttribute(level)) + "§7几率掉落1个Steak,");
        lore.add("    §7最终击杀可获得4个Steak。");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill4Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
    }

    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
    }
}

