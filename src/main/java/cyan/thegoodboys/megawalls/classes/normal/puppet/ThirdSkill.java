/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.puppet;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("钢铁之心", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        return 0.0;
    }

    public int getSeconds(int level) {
        switch (level) {
            case 1: {
                return 3;
            }
            case 2: {
                return 6;
            }
            case 3: {
                return 9;
            }
        }
        return 3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u88abArrow\u77e2\u51fb\u4e2d\u65f6,\u4f1a\u83b7\u5f97\u6301\u7eed\u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u7684");
            lore.add("   \u00a77\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u88abArrow\u77e2\u51fb\u4e2d\u65f6,\u4f1a\u83b7\u5f97\u6301\u7eed\u00a78" + this.getSeconds(level - 1) + " \u279c \u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u7684");
        lore.add("   \u00a77\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
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
        if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, this.getSeconds(kitStats.getSkill3Level()) * 20, 0));
        return true;
    }
}

