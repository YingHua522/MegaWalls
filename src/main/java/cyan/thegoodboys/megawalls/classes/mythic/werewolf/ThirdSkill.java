/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.werewolf;

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

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("吞噬", classes);
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
            lore.add(" §8▪ §7食用Steak时,有§a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u83b7\u5f97");
            lore.add("   \u00a775\u79d2\u7684\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u98df\u7528Steak\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("   \u00a7a" + StringUtils.percent(this.getAttribute(level - 1)) + "\u00a77\u51e0\u7387\u83b7\u5f97");
        lore.add("   \u00a775\u79d2\u7684\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
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
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 1));
        return true;
    }
}

