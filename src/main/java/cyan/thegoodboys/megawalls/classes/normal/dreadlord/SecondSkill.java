/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.classes.normal.dreadlord;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("灵魂吸噬", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.14;
            }
            case 2: {
                return 0.18;
            }
            case 3: {
                return 0.2;
            }
        }
        return 0.14;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u6062\u590d3\u70b9\u9965\u997f\u503c");
            lore.add("   \u00a77\u4ee5\u53ca4\u70b9\u751f\u547d\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a3\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u65f6\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u6062\u590d3\u70b9\u9965\u997f\u503c");
        lore.add("   \u00a77\u4ee5\u53ca2\u70b9\u751f\u547d\u3002");
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
        int attack = Dreadlord.skill2.getOrDefault(gamePlayer, 0);
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (attack == 5 ? "§a§l✓" : "§7§l" + attack + "/5");
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        int maxAttack = 5;
        int attack = Dreadlord.skill2.getOrDefault(gamePlayer, 0);
        Player player = gamePlayer.getPlayer();
        attack++;
        if (player.getFallDistance() > 0.0F && !player.isOnGround()) {
            attack++;
        }
        if (attack >= maxAttack) {
            PlayerUtils.food(player, 3);
            PlayerUtils.heal(player, 2);
            player.sendMessage("§e你的噬魂者技能给你恢复了3点饱和，2点生命值!");
            player.playSound(player.getLocation(), Sound.WITHER_IDLE, 1F, 0.3F);
            attack = 0;
        }
        Dreadlord.skill2.put(gamePlayer, attack);
        return true;
    }
}

