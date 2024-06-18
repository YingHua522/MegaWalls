/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.him;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
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
        super("飓风", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 5.0;
            }
            case 2: {
                return 4.0;
            }
            case 3: {
                return 3.0;
            }
        }
        return 5.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6bcf\u00a7a" + this.getAttribute(level) + "\u00a77\u6b21\u653b\u51fb,\u83b7\u5f97");
            lore.add("   \u00a77\u901f\u5ea6 II \u548c\u751f\u547d\u6062\u590d II \u6301\u7eed3\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6bcf\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u6b21\u653b\u51fb,\u83b7\u5f97");
        lore.add("   \u00a77\u901f\u5ea6 II \u548c\u751f\u547d\u6062\u590d II \u6301\u7eed3\u79d2");
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
        int maxAttack = (int) this.getAttribute(gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)).getSkill3Level());
        int attack = HIM.skill3.getOrDefault(gamePlayer, maxAttack);
        if (attack == maxAttack) {
            HIM.skill3.put(gamePlayer, 1);
            return true;
        }
        int i = attack + 1;
        HIM.skill3.put(gamePlayer, i);
        if (i == maxAttack) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 0));
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        int skillLevel = gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)).getSkill3Level();
        int attack = HIM.skill3.getOrDefault(gamePlayer, (int) this.getAttribute(skillLevel));
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (attack == (int) this.getAttribute(skillLevel) ? "\u00a7a\u00a7l\u2713" : "\u00a7a\u00a7l" + attack);
    }
}

