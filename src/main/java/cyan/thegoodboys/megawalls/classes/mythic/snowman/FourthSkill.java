/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.snowman;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("冬日宝藏", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.8;
            }
            case 2: {
                return 0.9;
            }
            case 3: {
                return 1.0;
            }
        }
        return 0.8;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u51e0\u7387\u83b7\u5f97");
            lore.add("    \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed10\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u51e0\u7387\u83b7\u5f97");
        lore.add("    \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed10\u79d2\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill4Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
    }

    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        this.getClasses().getSecondSkill().use(gamePlayer, kitStats);
        SecondSkill.snow1.put(gamePlayer, SecondSkill.snow1.getOrDefault(gamePlayer, 0) + 1);
        SecondSkill.snow2.put(gamePlayer, SecondSkill.snow2.getOrDefault(gamePlayer, 0) + 1);

    }

    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " \u00a77Snow " + SecondSkill.snow1.getOrDefault(gamePlayer, 0) + "\u00a77/8 " + " \u00a77Pumpkin " + SecondSkill.snow2.getOrDefault(gamePlayer, 0) + "\u00a77/18 ";

    }
}

