/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.dragon;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class

ThirdSkill
extends Skill {
    public ThirdSkill(Classes classes) {
        super("狂暴火焰", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.55;
            }
            case 2: {
                return 0.65;
            }
            case 3: {
                return 0.75;
            }
        }
        return 0.55;
    }

    public double getSeconds(int level) {
        switch (level) {
            case 1: {
                return 1.0;
            }
            case 2: {
                return 2.5;
            }
            case 3: {
                return 5.0;
            }
        }
        return 1.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u654c\u4eba\u65f6,\u83b7\u5f97\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u4f24\u5bb3\u52a0\u6210");
            lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb杀\u654c\u4eba\u65f6,\u83b7\u5f97\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u4f24\u5bb3\u52a0\u6210");
        lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a78" + this.getSeconds(level - 1) + " \u279c \u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill3Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill3Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        return true;
    }
}

