/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.spider;

import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill
        extends Skill {
    public ThirdSkill(Classes classes) {
        super("飞掠", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.0;
            }
            case 2: {
                return 4.0;
            }
            case 3: {
                return 5.0;
            }
        }
        return 3.0;
    }

    public int getEnergy(int level) {
        switch (level) {
            case 1: {
                return 10;
            }
            case 2: {
                return 15;
            }
            case 3: {
                return 20;
            }
        }
        return 10;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u57283\u79d2\u5185\u6d88\u80174\u6b21\u6bd2\u6db2\u4e4b\u51fb\u65f6,");
            lore.add("   \u00a77\u4f60\u5c06\u83b7\u5f97\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6I\u6548\u679c");
            lore.add("   \u00a77\u548c\u00a7a" + this.getEnergy(level) + "\u00a77\u80fd\u91cf\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u57283\u79d2\u5185\u6d88\u80174\u6b21\u6bd2\u6db2\u4e4b\u51fb\u65f6,");
        lore.add("   \u00a77\u4f60\u5c06\u83b7\u5f97\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6I\u6548\u679c");
        lore.add("   \u00a77\u548c\u00a78" + this.getEnergy(level - 1) + " \u279c \u00a7a" + this.getEnergy(level) + "\u00a77\u80fd\u91cf\u3002");
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
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (this.getAttribute(kitStats.getSkill3Level()) * 20.0), 0));
        gamePlayer.addEnergy(this.getEnergy(kitStats.getSkill3Level()), PlayerEnergyChangeEvent.ChangeReason.MELLEE);
        return true;
    }
}

