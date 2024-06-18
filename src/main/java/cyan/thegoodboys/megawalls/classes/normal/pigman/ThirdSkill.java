/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.pigman;

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
        super("守护", classes);
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
                return 6.0;
            }
            case 3: {
                return 7.0;
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
        if (Pigman.skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Player player = gamePlayer.getPlayer();
        int level = kitStats.getSkill3Level();
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) this.getAttribute(level) * 20, 1));
        Pigman.skill3Cooldown.put(gamePlayer, 30);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Pigman.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Pigman.skill3Cooldown.get(gamePlayer) + "s");
    }

}

