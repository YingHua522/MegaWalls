/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.zombie;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill
        extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("Well Trained", classes);
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
            lore.add(" \u00a78\u25aa \u00a7a80%\u00a77\u51e0\u7387\u5728\u6316\u77ff\u6216\u4f10\u6728\u65f6\u83b7\u5f97");
            lore.add("   \u00a77\u6025\u8feb\u6548\u679c\u3002\u50f5\u5c38\u5728\u5899\u5012\u584c\u4e4b\u524d");
            lore.add("   \u00a77\u80fd\u4e00\u76f4\u83b7\u5f97\u6025\u8febIII\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u5728\u6316\u77ff\u6216\u4f10\u6728\u65f6\u83b7\u5f97");
        lore.add("   \u00a77\u6025\u8feb\u6548\u679c\u3002\u50f5\u5c38\u5728\u5899\u5012\u584c\u4e4b\u524d");
        lore.add("   \u00a77\u80fd\u4e00\u76f4\u83b7\u5f97\u6025\u8febIII\u6548\u679c\u3002");
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
        if ((e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)) {
            if (e.getPlayer().hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                e.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            if (!MegaWalls.getInstance().getGame().isWallsFall()) {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 2));
            } else {
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
            }
        }
    }
}

