/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.him;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.WorldBoards;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("力量", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 5.0;
            }
            case 3: {
                return 6.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u4e00\u540d\u654c\u4eba\u540e,\u83b7\u5f9785%\u7684\u8fd1\u6218");
            lore.add("   \u00a77\u4f24\u5bb3\u52a0\u6210,\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u4e00\u540d\u654c\u4eba\u540e,\u83b7\u5f9785%\u7684\u8fd1\u6218");
        lore.add("   \u00a77\u4f24\u5bb3\u52a0\u6210,\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u3002");
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
        HIM.skill2.put(gamePlayer, (int) this.getAttribute(kitStats.getSkill2Level()));
        WorldBoards.giveRedScreenEffect(gamePlayer.getPlayer());
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(),()-> WorldBoards.clearRedScreenEffect(gamePlayer.getPlayer()),100L);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        int seconds = HIM.skill2.getOrDefault(gamePlayer, 0);
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " §c§l85%" + seconds + "秒";
    }
}

