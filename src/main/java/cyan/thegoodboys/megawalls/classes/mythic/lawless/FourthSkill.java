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
package cyan.thegoodboys.megawalls.classes.mythic.lawless;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill
        extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("子弹上膛", classes);
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
        return 1.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7找到宝箱时,有§a" + StringUtils.percent(this.getAttribute(level)) + "§7的几率");
            lore.add("   §7获得§e30§7支箭。");
            return lore;
        }
        lore.add(" §8▪ §7找到宝箱时,有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "§7的几率");
        lore.add("   §7获得§e30§7支箭。");
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
                if (e.getBlock().getType() == Material.TRAPPED_CHEST && (double) MegaWalls.getRandom().nextInt(250) <= FourthSkill.this.getAttribute(kitStats.getSkill4Level()) * 100.0) {
                    Chest chest = (Chest) e.getBlock().getState();
                    ItemBuilder itemBuilder = new ItemBuilder(Material.ARROW, 30);
                    chest.getBlockInventory().addItem(itemBuilder.build());
                }
            }
        }, 3L);
    }
}

