/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.normal.hunter;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.*;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import cyan.thegoodboys.megawalls.util.StringUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SecondSkill extends Skill {
    private final Map<String, Class<? extends CustomEntity>> classMap = new HashMap<String, Class<? extends CustomEntity>>();

    public SecondSkill(Classes classes) {
        super("动物伙伴", classes);
        this.classMap.put("Spider", CustomSpider.class);
        this.classMap.put("Boom Sheep", BoomSheep.class);
        this.classMap.put("Wolf", ShamanWolf.class);
        this.classMap.put("Skeleton", CusomSke.class);
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
            lore.add(" §8▪ \u00a77\u5f53\u4f60\u88ab\u653b\u51fb,\u4f60\u5c06\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684");
            lore.add("   §7几率召\u5524\u4e00\u53ea\u968f\u673a\u5ba0\u7269\u3002");
            lore.add("   §7它\u53ef\u80fd\u662f:\u76ae\u76ae\u732a\u3001\u8718\u86db");
            lore.add("   \u00a77\u7206\u70b8\u7f8a\u6216\u72fc\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a4\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u88ab\u653b\u51fb,\u4f60\u5c06\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684");
        lore.add("   \u00a77\u51e0\u7387\u53ec\u5524\u4e00\u53ea\u968f\u673a\u5ba0\u7269\u3002");
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
        if (Hunter.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        // 创建一个随机数生成器
        Random random = new Random();

        // 将映射转换为列表
        List<Map.Entry<String, Class<? extends CustomEntity>>> entries = new ArrayList<>(classMap.entrySet());

        // 随机选择一个条目
        Map.Entry<String, Class<? extends CustomEntity>> randomEntry = entries.get(random.nextInt(entries.size()));

        // 获取选中条目的值（实体类）
        Class<? extends CustomEntity> entityClass = randomEntry.getValue();

        // 创建实体类的实例
        CustomEntity customEntity;
        try {
            customEntity = entityClass.getConstructor(World.class).newInstance(((CraftWorld) Bukkit.getWorld("world")).getHandle());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }
        // 设置游戏玩家
        customEntity.setGamePlayer(gamePlayer);
        // 设置元数据
        customEntity.getBukkitEntity().setMetadata(MegaWalls.getMetadataValue(), new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
        // 在玩家的位置生成实体
        EntityTypes.spawnEntity((Entity) customEntity, gamePlayer.getPlayer().getLocation());
        // 向玩家发送消息
        gamePlayer.sendMessage("§a你的动物伙伴技能召唤了一只" + randomEntry.getKey() + "！");
        Hunter.skill2Cooldown.put(gamePlayer, 4);
        return true;
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Hunter.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Hunter.skill2Cooldown.get(gamePlayer) + "s");
    }
}

