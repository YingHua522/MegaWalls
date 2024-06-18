/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.mythic.snowman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.SnowmanFriend;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("雪人伙伴", classes);
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
        // 每20秒检查一次我方凋灵是否死亡，如果没有死亡，就生成一个新的雪傀儡
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), () -> {
            if (!gamePlayer.getGameTeam().isWitherDead() && Snowman.snowman.getOrDefault(gamePlayer, 0) <= 4) {
                // 创建新的雪傀儡并设置其属性
                final SnowmanFriend sf = new SnowmanFriend(((CraftWorld) Bukkit.getWorld("world")).getHandle());
                sf.setGamePlayer(gamePlayer);
                sf.getBukkitEntity().setMetadata(MegaWalls.getMetadataValue(), new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
                EntityTypes.spawnEntity(sf, gamePlayer.getPlayer().getLocation());
                Snowman.snowman.put(gamePlayer, Snowman.snowman.getOrDefault(gamePlayer, 0) + 1);
                // 设置30秒后雪傀儡消失
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                    sf.die();
                    Snowman.snowman.put(gamePlayer, Snowman.snowman.getOrDefault(gamePlayer, 0) - 1);
                }, 30 * 20);
            }
        }, 20 * 20, 20 * 20);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Snowman.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Snowman.skill3Cooldown.get(gamePlayer) + "\u79d2");
    }
}

