/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.enderman;

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
        super("灵魂爆发", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.33;
            }
            case 2: {
                return 6.66;
            }
            case 3: {
                return 8.23;
            }
        }
        return 4.33;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u80fd\u91cf\u8fbe\u5230100%\u65f6,");
            lore.add("   \u00a77\u83b7\u5f97\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u80fd\u91cf\u8fbe\u5230100%\u65f6,");
        lore.add("   \u00a77\u83b7\u5f97\u00a78" + this.getAttribute(level - 1) + " \u279c");
        lore.add("   \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
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
        if (gamePlayer.getEnergy() > 80 && Enderman.skill3.getOrDefault(gamePlayer, 0) < 1 && Enderman.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
            Enderman.skill3.put(gamePlayer, 1);
            Enderman.skill3Cooldown.put(gamePlayer, 15);
        }
        return true;
    }

    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Enderman.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Enderman.skill3Cooldown.get(gamePlayer) + "s");
    }
}

