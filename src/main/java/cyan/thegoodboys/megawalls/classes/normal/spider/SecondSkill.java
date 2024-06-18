/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.normal.spider;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondSkill extends Skill {
    public static Map<GamePlayer, Integer> meleeCounter = new HashMap<>();

    public SecondSkill(Classes classes) {
        super("毒液之击", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.1;
            }
            case 2: {
                return 0.15;
            }
            case 3: {
                return 0.2;
            }
        }
        return 0.1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u53d7\u5230\u6454\u843d\u4f24\u5bb3,\u6bcf\u635f\u5931\u4e00\u9897\u5fc3\u7684\u751f\u547d\u503c,");
            lore.add("   \u00a77\u83b7\u5f97\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3\u52a0\u6210(\u6700\u591a4\u6b21)");
            return lore;
        }
        lore.add(" §8▪ §7受到摔落伤害,每损失一颗心的生命值,");
        lore.add("   §7获得§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ §a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u4f24\u5bb3\u52a0\u6210(\u6700\u591a4\u6b21)");
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
        Player player = gamePlayer.getPlayer();
        meleeCounter.put(gamePlayer, meleeCounter.getOrDefault(gamePlayer, 0) + 1);
        if (meleeCounter.get(gamePlayer) >= 4) {
            //添加中毒效果
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0)); // 10 Seconds
            new BukkitRunnable() {
                int counter = 0;

                public void run() {
                    if (counter < 5) {
                        if (player.getHealth() - 0.6 > 0) { // 如果玩家的生命值减去0.6大于0
                            player.setHealth(player.getHealth() - 0.6);
                        } else {
                            player.setHealth(0);
                        }
                        counter++;
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(MegaWalls.getInstance(), 0L, 20L); // 20 ticks = 1 second

            // 重置计数器
            meleeCounter.put(gamePlayer, 0);
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        if (Spider.skill2.getOrDefault(gamePlayer, 0) == 0) {
            return null;
        }
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " §c§l" + StringUtils.percent(this.getAttribute(this.getPlayerLevel(gamePlayer))) + " " + Spider.skill2.get(gamePlayer) + " CHANCE";
    }
}

