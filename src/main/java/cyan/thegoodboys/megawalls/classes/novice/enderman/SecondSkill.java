/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.novice.enderman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.stage.BattleStage;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("末影之心", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.13;
            }
            case 2: {
                return 0.26;
            }
            case 3: {
                return 0.39;
            }
        }
        return 0.13;
    }

    public double getHeal(int level) {
        switch (level) {
            case 1: {
                return 1.0;
            }
            case 2: {
                return 2.0;
            }
            case 3: {
                return 3.0;
            }
        }
        return 1.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7有§a" + StringUtils.percent(this.getAttribute(level)) + "§7几率在死后不会掉落物品。");
            lore.add("   §7当你的凋零死后,每次击杀获得§a" + this.getHeal(level) + "§7点生命。");
            return lore;
        }
        lore.add(" §8▪ §7有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜");
        lore.add("   §a" + StringUtils.percent(this.getAttribute(level)) + "§7几率在死后不会掉落物品。");
        lore.add("   §7当你的凋零死后,每次击杀获得§8" + this.getHeal(level - 1) + " ➜");
        lore.add("   §a" + this.getHeal(level) + "§7点生命。");
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
        if (MegaWalls.getInstance().getGame().getStageManager().currentStage() instanceof BattleStage) {
            return true;
        }
        PlayerUtils.heal(gamePlayer.getPlayer(), this.getHeal(kitStats.getSkill2Level()));
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return null;
    }
}

