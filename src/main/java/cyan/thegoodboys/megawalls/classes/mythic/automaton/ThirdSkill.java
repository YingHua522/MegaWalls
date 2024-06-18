/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.automaton;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill
        extends Skill {
    public ThirdSkill(Classes classes) {
        super("微积分学", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 5.0;
            }
            case 3: {
                return 6.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u751f\u547d\u503c\u4f4e\u4e8e7\u5fc3\u65f6,\u4f1a\u83b7\u5f97");
            lore.add("   \u00a7a" + (int) this.getAttribute(level) + "\u00a77\u79d2\u7684\u6297\u6027\u63d0\u5347II\u6548\u679c");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4\uff1a\u00a7a30\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u751f\u547d\u503c\u4f4e\u4e8e7\u5fc3\u65f6,\u4f1a\u83b7\u5f97");
        lore.add("   \u00a78" + (int) this.getAttribute(level - 1) + " \u279c \u00a7a" + (int) this.getAttribute(level) + "\u00a77\u79d2\u7684\u6297\u6027\u63d0\u5347II\u6548\u679c");
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
        double damageMultiplier = 1.0;
        double baseDamage = 10.0; // 定义 baseDamage

        if (player.getHealth() > 20) {
            damageMultiplier += ((double) gamePlayer.getEnergy() / 20) * 0.02;
        } else {
            damageMultiplier -= ((double) gamePlayer.getEnergy() / 20) * 0.05;
        }
        for (Player enemy : PlayerUtils.getNearbyPlayers(player.getLocation(),  6)) {
            double damage = baseDamage * damageMultiplier; // baseDamage is the original damage value
            PlayerUtils.realDamage(enemy, player, damage);
        }
        return true;
    }
}

