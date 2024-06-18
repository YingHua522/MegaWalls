/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.util;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class NameTagUtils {
    public static final Field TEAM_NAME = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "a");
    public static final Field DISPLAY_NAME = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "b");
    public static final Field PREFIX = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "c");
    public static final Field SUFFIX = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "d");
    public static final Field PARAM_INT = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "h");
    public static final Field PACK_OPTION = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "i");
    public static final Field MEMBERS = NameTagUtils.getAccessibleField(PacketPlayOutScoreboardTeam.class, "g");

    public static PacketPlayOutScoreboardTeam createPacket(String name, String prefix, String suffix) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        NameTagUtils.setName(packet, name);
        NameTagUtils.setDisplayName(packet, name);
        NameTagUtils.setPrefix(packet, prefix);
        NameTagUtils.setSuffix(packet, suffix);
        NameTagUtils.setPackOption(packet);
        return packet;
    }

    public static void sendPacket(Player reciver, PacketPlayOutScoreboardTeam packet) {
        CraftPlayer c = (CraftPlayer) reciver;
        c.getHandle().playerConnection.sendPacket((Packet) packet);
    }

    public static void setName(PacketPlayOutScoreboardTeam packet, String teamName) {
        NameTagUtils.set(TEAM_NAME, packet, teamName);
    }

    public static void setDisplayName(PacketPlayOutScoreboardTeam packet, String teamDisplayName) {
        NameTagUtils.set(DISPLAY_NAME, packet, teamDisplayName);
    }

    public static void setPrefix(PacketPlayOutScoreboardTeam packet, String teamPrefix) {
        NameTagUtils.set(PREFIX, packet, teamPrefix);
    }

    public static void setSuffix(PacketPlayOutScoreboardTeam packet, String teamSuffix) {
        NameTagUtils.set(SUFFIX, packet, teamSuffix);
    }

    public static void setPararmInt(PacketPlayOutScoreboardTeam packet, int paramInt) {
        NameTagUtils.set(PARAM_INT, packet, paramInt);
    }

    public static void setPackOption(PacketPlayOutScoreboardTeam packet) {
        NameTagUtils.set(PACK_OPTION, packet, 1);
    }

    public static void addMember(PacketPlayOutScoreboardTeam packet, String player) {
        try {
            ((List) MEMBERS.get(packet)).add(player);
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            // empty catch block
        }
    }

    public static void set(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getAccessibleField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            NameTagUtils.makeAccessible(field);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void makeAccessible(Field field) {
        if (!(Modifier.isPublic(field.getModifiers()) && Modifier.isPublic(field.getDeclaringClass().getModifiers()) && !Modifier.isFinal(field.getModifiers()) || field.isAccessible())) {
            field.setAccessible(true);
        }
    }
}

