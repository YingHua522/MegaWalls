/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.novice.him;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.ChestManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("宝藏猎人", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1.4;
            }
            case 2: {
                return 1.7;
            }
            case 3: {
                return 2.0;
            }
        }
        return 1.4;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u627e\u5230\u5b9d\u7bb1\u7684\u51e0\u7387\u63d0\u5347\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u627e\u5230\u5b9d\u7bb1\u7684\u51e0\u7387\u63d0\u5347\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u3002");
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
        Bukkit.getScheduler().runTaskLater((Plugin) MegaWalls.getInstance(), new Runnable() {

            @Override
            public void run() {
                if ((e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) && e.getBlock().getType() != Material.TRAPPED_CHEST && (double) MegaWalls.getRandom().nextInt(200) <= FourthSkill.this.getAttribute(kitStats.getSkill4Level()) * 10.0) {
                    Block block = e.getBlock();
                    block.setType(Material.TRAPPED_CHEST);
                    Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).addProtectedBlock(block);
                    Chest chest = (Chest) block.getState();
                    ChestManager.fillInventory(chest.getBlockInventory());
                }
            }
        }, 5L);
    }
}

