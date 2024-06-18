/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.shark;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill
        extends Skill {
    public SecondSkill(Classes classes) {
        super("嗜血", classes);
    }

    public static float getDistance(final Location lc1, final Location lc2) {
        return (float) Math.sqrt(Math.pow(lc1.getX() - lc2.getX(), 2.0) + Math.pow(lc1.getY() - lc2.getY(), 2.0) + Math.pow(lc1.getZ() - lc2.getZ(), 2.0));
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 2.0;
            }
            case 2: {
                return 3.0;
            }
            case 3: {
                return 4.0;
            }
        }
        return 2.0;
    }

    public double getSeconds(int level) {
        switch (level) {
            case 1: {
                return 0.8;
            }
            case 2: {
                return 1.6;
            }
            case 3: {
                return 2.4;
            }
        }
        return 0.8;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();

        lore.add(" \u00a78\u25aa \u00a77略");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        double boost = PlayerUtils.getNearbyPlayersH(gamePlayer.getPlayer().getLocation(), 8, 15).size();
        if (boost > 5) {
            boost = 5;
        }
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " \u00a7c" + boost * 22 + "%";
    }
}

