/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.classes.normal.hunter;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("鹰眼", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 7.6;
            }
            case 2: {
                return 9.2;
            }
            case 3: {
                return 10.8;
            }
            case 4: {
                return 12.4;
            }
            case 5: {
                return 14.0;
            }
        }
        return 7.6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u4e3a\u62c9\u6ee1\u7684BowArrow\u9644\u52a0\u8ffd\u8e2a\u6548\u679c,\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u3002");
            lore.add("   \u00a77\u6bcf\u6b21\u547d\u4e2d\u6062\u590d0.5\u8840\u91cf,\u4f46\u4e0d\u4f1a\u589e\u52a0\u80fd\u91cf\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u4e3a\u62c9\u6ee1\u7684BowArrow\u9644\u52a0\u8ffd\u8e2a\u6548\u679c,\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u3002");
        lore.add("   \u00a77\u6bcf\u6b21\u547d\u4e2d\u6062\u590d0.5\u8840\u91cf,\u4f46\u4e0d\u4f1a\u589e\u52a0\u80fd\u91cf\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkillLevel();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkillLevel();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        if (Hunter.skill.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("\u00a7cYour ability is still cooldown for §e" + Hunter.skill.getOrDefault(gamePlayer, 0) + " seconds§!");
            return false;
        }
        gamePlayer.playSound(Sound.ORB_PICKUP, 1.0f, 1.0f);
        Hunter.skill.put(gamePlayer, (int) this.getAttribute(this.getPlayerLevel(gamePlayer)));
        Hunter.hit.clear();
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Hunter.skill.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l\u2715") : "\u00a7e\u00a7l" + Hunter.skill.get(gamePlayer));
    }
}

