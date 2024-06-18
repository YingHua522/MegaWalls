/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.pigman;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("勇气", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.08;
            }
            case 2: {
                return 0.09;
            }
            case 3: {
                return 0.1;
            }
        }
        return 0.08;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u654c\u4eba\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u7ed9\u4e88\u5468\u56f4");
            lore.add("   \u00a77\u8840\u91cf\u5c11\u4e8e20\u7684\u73a9\u5bb6\u6297\u6027\u63d0\u5347I\u6548\u679c");
            lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed8\u79d2\u3002\u65e0\u8bba\u4f60\u7684");
            lore.add("   \u00a77\u8840\u91cf\u9ad8\u4f4e,\u540c\u65f6\u7ed9\u4e88\u4f605\u79d2\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u654c\u4eba\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u7ed9\u4e88\u5468\u56f4");
        lore.add("   \u00a77\u8840\u91cf\u5c11\u4e8e20\u7684\u73a9\u5bb6\u6297\u6027\u63d0\u5347I\u6548\u679c");
        lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed8\u79d2\u3002\u65e0\u8bba\u4f60\u7684");
        lore.add("   \u00a77\u8840\u91cf\u9ad8\u4f4e,\u540c\u65f6\u7ed9\u4e88\u4f605\u79d2\u751f\u547d\u6062\u590dI\u6548\u679c\u3002");
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
    public String getSkillTip(GamePlayer gamePlayer) {
        if (Pigman.skill.getOrDefault(gamePlayer, 0) == 0) {
            return null;
        }
        int seconds = Pigman.skill.getOrDefault(gamePlayer, 0);
        return " §c§l+" + "§c§l" + "20% " + seconds + "s";
    }


    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        int maxAttack = 6;
        int attack = Pigman.skill2.getOrDefault(gamePlayer, maxAttack);
        if (attack == maxAttack) {
            Pigman.skill2.put(gamePlayer, 1);
            return true;
        }
        int i = attack + 1;
        Pigman.skill2.put(gamePlayer, i);
        if (i == maxAttack) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
            for (Player other : PlayerUtils.getNearbyPlayers(player, 8.0)) {
                GamePlayer gameOther;
                if (CitizensAPI.getNPCRegistry().isNPC(other) || (Objects.requireNonNull(gameOther = GamePlayer.get(other.getUniqueId()))).isSpectator() || !GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther) || !(other.getHealth() <= 20.0))
                    continue;
                if (other.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    other.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                if (other.hasPotionEffect(PotionEffectType.REGENERATION)) {
                    other.removePotionEffect(PotionEffectType.REGENERATION);
                }
                other.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0));
                other.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
            }
        }
        return true;
    }
}

