/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Chest
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.shark;

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
        super("深海宝藏", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.5;
            }
            case 2: {
                return 0.6;
            }
            case 3: {
                return 0.7;
            }
        }
        return 0.15;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u5728\u7bb1\u5b50\u4e2d\u627e\u5230");
            lore.add("    \u00a771:00\u4f24\u5bb3\u5438\u6536II\u836f\u6c34\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("    \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u5728\u7bb1\u5b50\u4e2d\u627e\u5230");
        lore.add("    \u00a771:00\u4f24\u5bb3\u5438\u6536II\u836f\u6c34\u3002");
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
    public void onBlockBreak(final KitStatsContainer kitStats, final BlockBreakEvent e) {
    }
}

