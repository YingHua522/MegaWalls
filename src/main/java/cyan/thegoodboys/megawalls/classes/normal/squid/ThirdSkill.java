/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.squid;

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
        super("重生", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 2;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 4;
            }
        }
        return 1.3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u751f\u547d\u964d\u523021\u4ee5\u4e0b\u65f6,\u83b7\u5f97\u6301\u7eed");
            lore.add("   \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590dIV\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u751f\u547d\u964d\u523021\u4ee5\u4e0b\u65f6,\u83b7\u5f97\u6301\u7eed");
        lore.add("   \u00a78" + this.getAttribute(level - 1) + " \u279c");
        lore.add("   \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u751f\u547d\u6062\u590dIV\u6548\u679c\u3002");
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
        if (Squid.skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Player player = gamePlayer.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 4));
        Squid.skill3Cooldown.put(gamePlayer, 30);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Squid.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§c§l" + Squid.skill3Cooldown.get(gamePlayer) + "s");

    }
}

