/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.shaman;

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
        super("灵力开采", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.2;
            }
            case 2: {
                return 0.4;
            }
            case 3: {
                return 0.6;
            }
        }
        return 0.2;
    }

    public int getAmount(int level) {
        switch (level) {
            case 1: {
                return 4;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 2;
            }
        }
        return 4;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" \u00a78\u25aa \u00a77\u6316\u94c1\u77ff\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u3002");
        lore.add("   \u00a77\u6389\u843d\u4e00\u6839\u539f\u6728\u548c\u4e00\u5757\u7164\u70ad\u3002");
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
        Player player = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer != null) {
            if (e.getBlock().getType() == Material.IRON_ORE) {
                int count = Shaman.ironOreCount.getOrDefault(gamePlayer, 0) + 1;
                if (count < getAmount(kitStats.getSkill4Level())) {
                    Shaman.ironOreCount.put(gamePlayer, count);
                    return;
                }
                if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                    gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.LOG));
                    gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.COAL));
                }else if (gamePlayer.getPlayer().getEnderChest().firstEmpty() != -1) {
                    gamePlayer.getEnderChest().addItem(new ItemStack(Material.LOG));
                    gamePlayer.getEnderChest().addItem(new ItemStack(Material.COAL));
                }else {
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.LOG));
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.COAL));
                }
                Shaman.ironOreCount.put(gamePlayer, 0);
            }
        }
    }
}

