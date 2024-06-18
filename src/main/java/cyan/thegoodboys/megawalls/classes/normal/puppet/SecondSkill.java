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

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("铜皮铁骨", classes);
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
                return 4;
            }
            case 2: {
                return 7;
            }
            case 3: {
                return 10;
            }
        }
        return 4;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u73a9\u5bb6\u540e,\u83b7\u5f97\u6301\u7eed\u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u7684");
            lore.add("   \u00a77\u5438\u6536II\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u73a9\u5bb6\u540e,\u83b7\u5f97\u6301\u7eed\u00a78" + this.getSeconds(level - 1) + " \u279c \u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u7684");
        lore.add("   \u00a77\u5438\u6536II\u6548\u679c\u3002");
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
        if (Puppet.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, this.getSeconds(kitStats.getSkill2Level()) * 20, 1));
        Puppet.skill2Cooldown.put(gamePlayer, 10);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName() + " " + (Puppet.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l\u2715") : "\u00a7c\u00a7l" + Puppet.skill2Cooldown.get(gamePlayer) + "\u79d2");
    }
}

