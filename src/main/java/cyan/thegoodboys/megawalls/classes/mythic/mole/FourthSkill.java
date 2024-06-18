/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.entity.Firework
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.mythic.mole;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.ChestManager;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("囤铁", classes);
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
            lore.add(" \u00a78\u25aa \u00a77\u627e\u5230\u5b9d\u7bb1\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
            lore.add("   \u00a77\u989d\u5916\u83b7\u5f97\u94c1\u952d\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u627e\u5230\u5b9d\u7bb1\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
        lore.add("   \u00a77\u989d\u5916\u83b7\u5f97\u94c1\u952d\u3002");
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
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        assert gamePlayer != null;
        if ((e.getBlock().getType() == Material.GRASS || e.getBlock().getType() == Material.DIRT || e.getBlock().getType() == Material.SAND || e.getBlock().getType() == Material.SNOW_BLOCK)) {
            this.getClasses().getSecondSkill().use(gamePlayer, kitStats);
            if (MegaWalls.getRandom().nextInt(MegaWalls.getInstance().getGame().isWallsFall() ? 1000 : 150) <= 6) {
                e.setCancelled(true);
                Block block = e.getBlock();
                block.setType(Material.TRAPPED_CHEST);
                gamePlayer.addProtectedBlock(block);
                Chest chest = (Chest) block.getState();
                gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1, 1);
                ChestManager.fillInventory(chest.getBlockInventory());
            }
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                SecondSkill.exp1.put(gamePlayer, SecondSkill.exp1.getOrDefault(gamePlayer, 0) + 1);
                SecondSkill.exp2.put(gamePlayer, SecondSkill.exp2.getOrDefault(gamePlayer, 0) + 1);
                SecondSkill.exp3.put(gamePlayer, SecondSkill.exp3.getOrDefault(gamePlayer, 0) + 1);
                if (e.getBlock().getType() == Material.TRAPPED_CHEST && MegaWalls.getRandom().nextInt(100) <= FourthSkill.this.getAttribute(kitStats.getSkill4Level()) * 100.0d) {
                    Chest chest2 = (Chest) e.getBlock().getState();
                    chest2.getBlockInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
                }
            }, 1L);
        }
    }
}

