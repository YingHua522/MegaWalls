/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.normal.blaze;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdSkill extends Skill {
    public static Map<GamePlayer, Integer> meleeAttackCount = new HashMap<>();
    public static Map<GamePlayer, Long> lastAttackTime = new HashMap<>();

    public ThirdSkill(Classes classes) {
        super("熔融之心", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.05;
            }
            case 2: {
                return 0.075;
            }
            case 3: {
                return 0.1;
            }
        }
        return 0.05;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u654c\u4eba\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u5c06\u5176\u70b9\u71c33\u79d2\u3002");
            lore.add("   \u00a77BowArrow\u547d\u4e2d\u65f6\u51e0\u7387\u52a0\u500d\u3002");
            lore.add("   \u00a77\u89e6\u53d1\u65f6\u83b7\u5f97\u751f\u547d\u6062\u590d\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a1\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u654c\u4eba\u65f6\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u5c06\u5176\u70b9\u71c33\u79d2\u3002");
        lore.add("   \u00a77BowArrow\u547d\u4e2d\u65f6\u51e0\u7387\u52a0\u500d\u3002");
        lore.add("   \u00a77\u89e6\u53d1\u65f6\u83b7\u5f97\u751f\u547d\u6062\u590d\u3002");
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
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Blaze.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§c§l" + Blaze.skill3Cooldown.get(gamePlayer) + "秒");
    }
}

