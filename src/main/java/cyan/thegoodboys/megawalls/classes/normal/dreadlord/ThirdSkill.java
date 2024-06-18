/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.dreadlord;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.WorldBoards;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("噬魂者", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.55;
            }
            case 2: {
                return 0.65;
            }
            case 3: {
                return 0.95;
            }
        }
        return 0.55;
    }

    public double getSeconds(int level) {
        switch (level) {
            case 1: {
                return 1.0;
            }
            case 2: {
                return 2.5;
            }
            case 3: {
                return 6.0;
            }
        }
        return 1.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78▪ \u00a77\u51fb\u6740\u654c\u4eba\u65f6,\u83b7\u5f97\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u4f24\u5bb3\u52a0\u6210");
            lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u654c\u4eba\u65f6,\u83b7\u5f97\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u4f24\u5bb3\u52a0\u6210");
        lore.add("   \u00a77\u548c\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a78" + this.getSeconds(level - 1) + " \u279c \u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u3002");
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
        int level = kitStats.getSkill3Level();
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getSeconds(level) * 20.0), 0));
        player.getWorld().playSound(player.getLocation(), Sound.WITHER_HURT,1,0.3f);
        WorldBoards.giveRedScreenEffect(gamePlayer.getPlayer());
        Dreadlord.skill3.put(gamePlayer, (int) this.getSeconds(level));
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        if (Dreadlord.skill3.getOrDefault(gamePlayer, 0) == 0) {
            WorldBoards.clearRedScreenEffect(gamePlayer.getPlayer());
            return null;
        }
        int level = this.getPlayerLevel(gamePlayer);
        String percent = StringUtils.percent(this.getAttribute(level));
        int seconds = (int) this.getSeconds(level);
        seconds--;
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " §c§l" + percent + " " + seconds + "s";
    }
}

