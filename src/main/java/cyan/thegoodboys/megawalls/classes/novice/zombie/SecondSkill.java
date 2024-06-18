/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.zombie;

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
        super("坚韧", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.125;
            }
            case 2: {
                return 0.25;
            }
            case 3: {
                return 0.375;
            }
        }
        return 0.125;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u88ab\u8fd1\u6218\u547d\u4e2d\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
            lore.add("   \u00a77\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78▪ \u00a77\u88ab\u8fd1战命中时,有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("   \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
        lore.add("   \u00a77\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
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
        int maxAttack = 3;
        int attack = Zombie.skill2.getOrDefault(gamePlayer, maxAttack);
        if (attack == maxAttack) {
            Zombie.skill2.put(gamePlayer, 1);
            return true;
        }
        int i = attack + 1;
        Zombie.skill2.put(gamePlayer, i);
        if (i == maxAttack) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        int attack = Zombie.skill2.getOrDefault(gamePlayer, 0);
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (attack >= 3 ? "§a§l✓" : "§a§l" + attack);
    }
}

