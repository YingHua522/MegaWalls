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
 */
package cyan.thegoodboys.megawalls.classes.normal.hunter;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("金苹果", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.25;
            }
            case 2: {
                return 0.5;
            }
            case 3: {
                return 0.75;
            }
        }
        return 0.25;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f97\u6c38\u4e45\u591c\u89c6\u6548\u679c,\u5e76\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684");
            lore.add("    \u00a77\u51e0\u7387\u5728\u7bb1\u5b50\u4e2d\u83b7\u5f97\u4e00\u4e2aGolden Apple\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f97\u6c38\u4e45\u591c\u89c6\u6548\u679c,\u5e76\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684");
        lore.add("    \u00a77\u51e0\u7387\u5728\u7bb1\u5b50\u4e2d\u83b7\u5f97\u4e00\u4e2aGolden Apple\u3002");
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
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            if (e.getBlock().getType() == Material.TRAPPED_CHEST && (double) MegaWalls.getRandom().nextInt(300) <= (FourthSkill.this.getAttribute(kitStats.getSkill4Level()) + 0.5) * 100.0) {
                Chest chest = (Chest) e.getBlock().getState();
                chest.getBlockInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }
        }, 3L);
    }
}

