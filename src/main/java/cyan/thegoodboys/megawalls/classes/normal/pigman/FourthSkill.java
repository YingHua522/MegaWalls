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
package cyan.thegoodboys.megawalls.classes.normal.pigman;

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
        super("足智多谋", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.8;
            }
            case 2: {
                return 0.9;
            }
            case 3: {
                return 1.0;
            }
        }
        return 0.8;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a7a80%\u00a77\u51e0\u7387\u5728\u7bb1\u5b50\u91cc");
            lore.add("   \u00a77\u627e\u5230\u4e00\u6574\u5957\u94c1\u62a4\u7532\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u5728\u7bb1\u5b50\u91cc");
        lore.add("   \u00a77\u627e\u5230\u4e00\u6574\u5957\u94c1\u62a4\u7532\u3002");
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
            if (e.getBlock().getType() == Material.TRAPPED_CHEST) {
                Chest chest = (Chest) e.getBlock().getState();
                chest.getBlockInventory().addItem(new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS));
            }
        }, 3L);
    }
}

