/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.entity.Firework
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.oldspider;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.ChestManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Firework;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill
        extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("寻铁者", classes);
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
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u91c7\u96c6\u6ce5\u571f\u3001\u8349\u3001\u6c99\u5b50\u3001");
            lore.add("    \u00a77\u7802\u783e\u6216\u83cc\u4e1d\u65f6,");
            lore.add("    \u00a77\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u83b7\u5f97\u94c1\u952d\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u91c7\u96c6\u6ce5\u571f\u3001\u8349\u3001\u6c99\u5b50\u3001");
        lore.add("    \u00a77\u7802\u783e\u6216\u83cc\u4e1d\u65f6,");
        lore.add("    \u00a77\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u83b7\u5f97\u94c1\u952d\u3002");
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
        if ((e.getBlock().getType() == Material.DIRT || e.getBlock().getType() == Material.GRASS || e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.GRAVEL || e.getBlock().getType() == Material.MYCEL) && (double) MegaWalls.getRandom().nextInt(100) <= this.getAttribute(kitStats.getSkill4Level()) * 100.0) {
            e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
            if (MegaWalls.getRandom().nextInt(MegaWalls.getInstance().getGame().isWallsFall() ? 1000 : 150) <= 6) {
                e.setCancelled(true);
                Block block = e.getBlock();
                block.setType(Material.TRAPPED_CHEST);
                GamePlayer.get(e.getPlayer().getUniqueId()).addProtectedBlock(block);
                Chest chest = (Chest) block.getState();
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1, 1);
                ChestManager.fillInventory(chest.getBlockInventory());
            }
        }
    }
}

