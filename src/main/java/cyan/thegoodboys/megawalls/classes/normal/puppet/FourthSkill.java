/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.puppet;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthSkill extends CollectSkill {
    public static final Map<GamePlayer, Integer> logCount = new HashMap<>();

    public FourthSkill(Classes classes) {
        super("化木为铁", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.12;
            }
            case 2: {
                return 0.16;
            }
            case 3: {
                return 0.2;
            }
        }
        return 0.12;
    }

    public int getAmount(int level) {
        switch (level) {
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 4;
        }
        return 6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u4f10\u6728\u7684\u65f6\u5019\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
            lore.add("    \u00a77\u83b7\u5f97\u4e00\u4e2a\u94c1\u5757");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u4f10\u6728\u7684\u65f6\u5019\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
        lore.add("    \u00a77\u83b7\u5f97\u4e00\u4e2a\u94c1\u5757");
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
        if ((e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)) {
            GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
            logCount.put(gamePlayer, logCount.getOrDefault(gamePlayer, 0) + 1);
            if (logCount.get(gamePlayer) >= getAmount(kitStats.getSkill4Level())) {
                if (gamePlayer != null) {
                    gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_BLOCK));
                    gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ANVIL_LAND, 1.0f, 1.0f);
                    Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                        gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ANVIL_LAND, 1.0f, 1.2f);
                    }, 11L);
                    Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                        gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ANVIL_LAND, 1.0f, 1.4f);
                    }, 22L);
                    logCount.put(gamePlayer, 0); // 重置计数器
                }
            }
        }
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + "§7" + logCount.getOrDefault(gamePlayer, 0) + "/" + getAmount(gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level());
    }
}

