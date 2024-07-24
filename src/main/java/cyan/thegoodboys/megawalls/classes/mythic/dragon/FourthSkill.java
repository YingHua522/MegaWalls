/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.mythic.dragon;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill
extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("矮人黄金", classes);
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

    public double getChestplate(int level) {
        switch (level) {
            case 1: {
                return 0.4;
            }
            case 2: {
                return 0.5;
            }
            case 3: {
                return 0.6;
            }
        }
        return 0.4;
    }
    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + Dragon.gold.getOrDefault(gamePlayer,5);
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

    }
}

