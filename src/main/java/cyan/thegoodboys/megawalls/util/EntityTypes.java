/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityTypes
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package cyan.thegoodboys.megawalls.util;

import cyan.thegoodboys.megawalls.game.team.TeamWither;
import cyan.thegoodboys.megawalls.nms.*;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.util.Map;

public enum EntityTypes {
    Wither("Wither", 64, TeamWither.class),
    Wolf("Wolf", 95, ShamanWolf.class),
    Blaze("Blaze", 61, CustomBlaze.class),
    Creeper("Creeper", 50, CustomCreeper.class),
    Bat("Bat", 65, CustomBat.class),
    Pig("Pig", 90, CustomPig.class),
    BoomSheep("BoomSheep", 91, BoomSheep.class),
    Snowman("Snowman", 97, SnowmanFriend.class),
    Spider("Spider", 52, CustomSpider.class),
    Skeleton("Skeleton", 51, CusomSke.class),
    Zombie("Zombie", 54, CustomZombie.class);
    //WitherSkull("WitherSkull",19,CustomWitherSkull.class);

    private EntityTypes(String name, int id, Class<? extends Entity> custom) {
        EntityTypes.addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    private static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Object o = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    private static void addToMaps(Class clazz, String name, int id) {
        ((Map) EntityTypes.getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map) EntityTypes.getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map) EntityTypes.getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, id);
    }
}

