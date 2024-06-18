/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.arcane;

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

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("奥数暴风", classes);
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

    public int getSpeed(int level) {
        switch (level) {
            case 1:
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
        }
        return 2;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7击杀玩家时,会获得§a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590dII");
            lore.add("   \u00a77\u548c6\u79d2\u7684\u901f\u5ea6\u00a7a" + StringUtils.level(this.getSpeed(level)) + "\u00a77\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u73a9\u5bb6\u65f6,\u4f1a\u83b7\u5f97\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590dII");
        lore.add("   \u00a77\u548c6\u79d2\u7684\u901f\u5ea6\u00a78" + (this.getSpeed(level - 1) == this.getSpeed(level) ? StringUtils.level(this.getSpeed(level)) : StringUtils.level(this.getSpeed(level - 1)) + " \u279c \u00a7a" + StringUtils.level(this.getSpeed(level))) + "\u00a77\u6548\u679c\u3002");
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
        Player player = gamePlayer.getPlayer();
        int level = kitStats.getSkill2Level();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(level) * 20.0), 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, this.getSpeed(level) - 1));
        return true;
    }
}

