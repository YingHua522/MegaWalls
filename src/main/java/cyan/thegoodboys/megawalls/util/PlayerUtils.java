/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.citizensnpcs.api.CitizensAPI
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayInClientCommand
 *  net.minecraft.server.v1_8_R3.PacketPlayInClientCommand$EnumClientCommand
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo$EnumPlayerInfoAction
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  skinsrestorer.bukkit.SkinsRestorer
 */
package cyan.thegoodboys.megawalls.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import skinsrestorer.bukkit.SkinsRestorer;
import skinsrestorer.shared.exception.SkinRequestException;
import skinsrestorer.shared.interfaces.ISkinsRestorerAPI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerUtils {

    public static void respawn(final Player player) {
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), new Runnable() {

            @Override
            public void run() {
                PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                ((CraftPlayer) player).getHandle().playerConnection.a(packet);
            }
        }, 1L);
    }


    public static void skinChange(final CraftPlayer cp, final String value, final String signature) {
        SkinsRestorer.getInstance().getFactory().applySkin(cp, new Property("textures", value, signature));
        final double health = cp.getHealth();
        (new BukkitRunnable() {
            public void run() {
                GameProfile profile = cp.getProfile();
                profile.getProperties().put("textures", new Property("textures", value, signature));
                Collection<Property> prop = profile.getProperties().get("textures");
                cp.getProfile().getProperties().putAll("textures", prop);
                PacketPlayOutEntityDestroy pds = new PacketPlayOutEntityDestroy(cp.getEntityId());
                PlayerUtils.sendPacket(pds);
                PacketPlayOutPlayerInfo tab = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{cp.getHandle()});
                PlayerUtils.sendPacket(tab);
                cp.setHealth(0.0D);
            }
        }).runTaskLater(MegaWalls.getInstance(), 1L);
        (new BukkitRunnable() {
            public void run() {
                cp.spigot().respawn();
                cp.setHealth(health);
                cp.teleport(MegaWalls.getInstance().getGame().getLobbyLocation());
                PacketPlayOutPlayerInfo tabadd = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{cp.getHandle()});
                PlayerUtils.sendPacket(tabadd);
                PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
                Iterator<? extends Player> var3 = Bukkit.getOnlinePlayers().iterator();

                Player p;
                while (var3.hasNext()) {
                    p = var3.next();
                    if (!p.getName().equals(cp.getName())) {
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
                    }
                }

                var3 = Bukkit.getOnlinePlayers().iterator();

                while (var3.hasNext()) {
                    p = var3.next();
                    p.hidePlayer(cp);
                    p.showPlayer(cp);
                }

            }
        }).runTaskLater(MegaWalls.getInstance(), 20L);
    }

    private static void sendPacket(Packet<?> packet) {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public static void realDamage(final Entity p, final Player damager, double damage) {
        GamePlayer player = GamePlayer.get(damager.getPlayer().getUniqueId());
        GamePlayer player1 = GamePlayer.get(p.getUniqueId());
        final double hp = ((Damageable) p).getHealth();
        final double ahp = ((CraftPlayer) p).getHandle().getAbsorptionHearts();
        assert player != null;
        player.addDamage((int) damage);
        assert player1 != null;
        player1.addDef((int) damage);
        if (p.isDead()) return;
        double lol = hp + ahp - damage;
        if (lol <= 0) {
            ((CraftPlayer) p).setNoDamageTicks(0);
            ((LivingEntity) p).damage(100, damager);
            ((CraftPlayer) p).getHandle().setAbsorptionHearts(0);
        } else {
            if (ahp >= damage) {
                ((LivingEntity) p).damage(0);
                ((CraftPlayer) p).getHandle().setAbsorptionHearts((float) (ahp - damage));
            } else {
                double real1 = damage - ahp;
                ((LivingEntity) p).damage(0);
                ((LivingEntity) p).setHealth(hp - real1);
                ((CraftPlayer) p).getHandle().setAbsorptionHearts(0);
            }
        }
    }


    public static List<Player> getNearbyPlayers(Entity entity, double radius) {
        GamePlayer gamePlayer = GamePlayer.get(entity.getUniqueId());
        ArrayList<Player> players = new ArrayList<>();
        for (Entity e : entity.getNearbyEntities(radius, radius, radius)) {
            if (CitizensAPI.getNPCRegistry().isNPC(e) || !(e instanceof Player)) continue;
            players.add((Player) e);
        }
        return players;
    }

        public static List<Player> getNearbyPlayers(Location location, double radius) {
            ArrayList<Player> players = new ArrayList<>();
            for (Entity e : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                if (CitizensAPI.getNPCRegistry().isNPC(e) || !(e instanceof Player) || !(e.getLocation().distance(location) <= radius))
                    continue;
                players.add((Player) e);
            }
            return players;
        }

    public static List<Player> getNearbyPlayersH(Location location, double radius, int heal) {
        ArrayList<Player> players = new ArrayList<>();
        for (Entity e : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (CitizensAPI.getNPCRegistry().isNPC(e) || !(e instanceof Player) || !(e.getLocation().distance(location) <= radius) || ((Player) e).getHealth() > heal)
                continue;
            players.add((Player) e);
        }
        return players;
    }

    public static void heal(Player player, double heal) {
        player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
    }

    public static void food(Player player, int level) {
        player.setFoodLevel(Math.min(player.getFoodLevel() + level, 20));
    }

    public static void removeNameTag(Player player) {
        //隐藏玩家头顶tag
        ScoreboardTeam steam = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        steam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
    }

    public static void showNameTag(Player player) {
        //恢复玩家头顶tag
        ScoreboardTeam steam = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        steam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
    }


    public static void hideArmor(Player victim, Player receiver) {
        if (victim.equals(receiver)) return;
        PacketPlayOutEntityEquipment hand = new PacketPlayOutEntityEquipment(victim.getEntityId(), 0, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(victim.getEntityId(), 1, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(victim.getEntityId(), 2, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(victim.getEntityId(), 3, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(victim.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
        PlayerConnection boundTo = ((CraftPlayer) receiver).getHandle().playerConnection;
        boundTo.sendPacket(hand);
        boundTo.sendPacket(helmet);
        boundTo.sendPacket(chest);
        boundTo.sendPacket(pants);
        boundTo.sendPacket(boots);
    }


    public static void showArmor(Player victim, Player receiver) {
        if (victim.equals(receiver)) return;
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();
        PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, entityPlayer.inventory.getArmorContents()[3]);
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, entityPlayer.inventory.getArmorContents()[2]);
        PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, entityPlayer.inventory.getArmorContents()[1]);
        PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, entityPlayer.inventory.getArmorContents()[0]);
        PacketPlayOutEntityEquipment hand = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, entityPlayer.inventory.getItemInHand());
        EntityPlayer boundTo = ((CraftPlayer) receiver).getHandle();
        boundTo.playerConnection.sendPacket(helmet);
        boundTo.playerConnection.sendPacket(chest);
        boundTo.playerConnection.sendPacket(pants);
        boundTo.playerConnection.sendPacket(boots);
        boundTo.playerConnection.sendPacket(hand);
    }

    public static String getPrefix(Player p) {
        String prefix = Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId())).getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public static void refresh(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) player).getHandle()));
        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                ((CraftPlayer) player).getHandle()));
        for (Player p : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(p);
            player.showPlayer(p);
        }

    }
}

