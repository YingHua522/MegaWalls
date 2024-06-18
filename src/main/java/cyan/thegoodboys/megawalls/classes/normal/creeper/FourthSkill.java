/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.creeper;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthSkill extends CollectSkill {

    public static Map<GamePlayer, Integer> tnt = new HashMap<>();

    public FourthSkill(Classes classes) {
        super("TNT收集", classes);
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

    public int getAmount(int level) {
        switch (level) {
            case 1: {
                return 3;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 1;
            }
        }
        return 3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u7164\u77ff\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u83b7\u5f97TNT");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u7164\u77ff\u65f6\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u83b7\u5f97TNT");
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
        Material blockType = e.getBlock().getType();
        Player player = e.getPlayer();
        GamePlayer gp = GamePlayer.get(player.getUniqueId());
        if (gp != null) {
            int level = tnt.getOrDefault(gp, 0);
            if (blockType == Material.COAL_ORE) {
                level++;
                if (level >= getAmount(kitStats.getSkill4Level())) {
                    player.getInventory().addItem(new ItemStack(Material.TNT, 1));
                    tnt.put(gp, 0);
                }
            } else if (blockType == Material.COAL_BLOCK) {
                level++;
                if (level >= getAmount(kitStats.getSkill4Level())) {
                    player.getInventory().addItem(new ItemStack(Material.TNT, 9));
                    tnt.put(gp, 0);
                }
            }
        }
    }
}

