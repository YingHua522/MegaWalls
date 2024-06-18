/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.pirate;

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
        super("轻功水上漂", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 16.0;
            }
            case 2: {
                return 20.0;
            }
            case 3: {
                return 24.0;
            }
        }
        return 16.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u751f\u547d\u964d\u5230\u00a7a" + this.getAttribute(level) + "\u00a77\u65f6,\u83b7\u5f97\u6301\u7eed");
            lore.add("   \u00a7715\u79d2\u7684\u901f\u5ea6II\u6548\u679c\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4\uff1a\u00a7a30\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u751f\u547d\u964d\u5230\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u65f6,\u83b7\u5f97\u6301\u7eed");
        lore.add("   \u00a7715\u79d2\u7684\u901f\u5ea6II\u6548\u679c\u3002");
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
        if (Pirate.skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
        if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
        Pirate.skill3Cooldown.put(gamePlayer, 30);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Pirate.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Pirate.skill3Cooldown.get(gamePlayer) + "s");
    }
}

