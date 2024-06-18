/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.novice.skeleton;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("效率", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1.0;
            }
            case 2: {
                return 2.0;
            }
            case 3: {
                return 3.0;
            }
        }
        return 0.3333;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5728\u6316\u77ff\u6216\u4f10\u6728\u65f6,");
            lore.add("    \u00a77\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u83b7\u5f97\u4e09\u500d\u6389\u843d\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5728\u6316\u77ff\u6216\u4f10\u6728\u65f6,");
        lore.add("    \u00a77\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("    \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u83b7\u5f97\u4e09\u500d\u6389\u843d\u3002");
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
        if ((e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.DIAMOND_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) && (double) MegaWalls.getRandom().nextInt(100) <= this.getAttribute(kitStats.getSkill4Level()) * 100.0) {
            Player player = e.getPlayer();
            for (ItemStack itemStack : e.getBlock().getDrops()) {
                double size = getAttribute(kitStats.getSkill4Level());
                itemStack.setAmount((int) (itemStack.getAmount() * size));
                if (e.getBlock().getType() == Material.DIAMOND_ORE) {
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND, (int) (itemStack.getAmount() * size)));
                }
                player.getInventory().addItem(itemStack);
            }
        }
    }
}

