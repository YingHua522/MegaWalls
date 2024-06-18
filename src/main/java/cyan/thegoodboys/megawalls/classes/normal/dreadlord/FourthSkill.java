/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.dreadlord;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FourthSkill extends CollectSkill {
    private static final Map<UUID, Integer> ironOreCounter = new HashMap<>();
    private static final Map<UUID, Integer> itemIndexCounter = new HashMap<>();
    private static final Map<UUID, Boolean> isUsingSkill = new HashMap<>();
    private static final Material[] items = new Material[]{Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS};

    public FourthSkill(Classes classes) {
        super("暗物质", classes);
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

    public int getChestplate(int level) {
        switch (level) {
            case 1: {
                return 65;
            }
            case 2: {
                return 35;
            }
            case 3: {
                return 20;
            }
        }
        return 0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u51e0\u7387\u6389\u843d\u94c1\u952d,");
            lore.add("    \u00a77\u6316\u4efb\u4f55\u77ff\u7269\u90fd\u6709\u00a7a" + StringUtils.percent(this.getChestplate(level)) + "\u00a77\u51e0\u7387");
            lore.add("    \u00a77\u6389\u843d\u4e00\u4ef6\u94c1\u62a4\u7532\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("    \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u6389\u843d\u94c1\u952d,");
        lore.add("    \u00a77\u6316\u4efb\u4f55\u77ff\u7269\u90fd\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("    \u00a7a" + StringUtils.percent(this.getChestplate(level)) + "\u00a77\u51e0\u7387");
        lore.add("    \u00a77\u6389\u843d\u4e00\u4ef6\u94c1\u62a4\u7532\u3002");
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
        if (e.getBlock().getType() == Material.IRON_ORE) {
            e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
            e.getBlock().setType(Material.AIR);
            UUID playerId = e.getPlayer().getUniqueId();
            ironOreCounter.put(playerId, ironOreCounter.getOrDefault(playerId, 0) + 1);
            if (!isUsingSkill.getOrDefault(playerId, false)) {
                ironOreCounter.put(playerId, this.getChestplate(kitStats.getSkill4Level()));
                isUsingSkill.put(playerId, true);
            }
            if (ironOreCounter.get(playerId) >= this.getChestplate(kitStats.getSkill4Level())) {
                int itemIndex = itemIndexCounter.getOrDefault(playerId, 0);
                e.getPlayer().getInventory().addItem(new ItemStack(items[itemIndex]));
                ironOreCounter.put(playerId, 0);
                itemIndexCounter.put(playerId, (itemIndex + 1) % items.length);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.WITHER_HURT, 1.0F, 0.8F);
            }
        }
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        int ironOreCount = ironOreCounter.getOrDefault(gamePlayer.getPlayer().getUniqueId(), 0);
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + "§7§l" + ironOreCount + "/" + this.getChestplate(this.getPlayerLevel(gamePlayer));
    }
}

