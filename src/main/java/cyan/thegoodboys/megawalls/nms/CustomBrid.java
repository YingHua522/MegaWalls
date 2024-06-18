package cyan.thegoodboys.megawalls.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class CustomBrid {
    public static final Map<Player, List<ArmorStand>> playerArmorStands = new HashMap<>();

    public static void createArmorStand(Player player) {
        List<ArmorStand> standsForPlayer = playerArmorStands.computeIfAbsent(player, k -> new ArrayList<>());
        if (standsForPlayer.size() >= 6) return;
        World world = player.getWorld();
        GamePlayer gp = GamePlayer.get(player.getUniqueId());
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setMetadata("bird", MegaWalls.getFixedMetadataValue());
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setHelmet(createSkull());
        int standCount = standsForPlayer.size();
        double angle = Math.toRadians(standCount * 60);
        Location loc = player.getLocation().add(Math.sin(angle) * 1, 2, Math.cos(angle) * 1);
        armorStand.teleport(loc);
        standsForPlayer.add(armorStand);
    }

    public static void moveArmorStands(Player player) {
        List<ArmorStand> standsForPlayer = playerArmorStands.get(player);
        if (standsForPlayer != null) {
            double timeFactor = System.currentTimeMillis() % 10000 / 5000.0 * Math.PI;
            for (int i = 0; i < standsForPlayer.size(); i++) {
                ArmorStand armorStand = standsForPlayer.get(i);
                double angle = 4 * Math.PI * i / 6 + timeFactor;
                Location loc = player.getLocation().add(1 * Math.sin(angle), 2, 1 * Math.cos(angle));
                // 使用朝向向量设置盔甲架的朝向
                armorStand.teleport(new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        }
    }

    public static void removePlayer(Player target, Player owner) {
        List<ArmorStand> standsForPlayer = playerArmorStands.get(owner);
        List<ArmorStand> standsToRemove = new ArrayList<>();
        if (standsForPlayer != null) {
            for (ArmorStand armorStand : standsForPlayer) {
                if (armorStand.getLocation().distance(target.getLocation()) <= 5.0) {
                    armorStand.remove();
                    standsToRemove.add(armorStand);
                }
            }
            standsForPlayer.removeAll(standsToRemove);
        }
    }

    private static ItemStack createSkull() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQxYTE2OGJjNzJjYjMxNGY3Yzg2ZmVlZjlkOWJjNzYxMjM2NTI0NGNlNjdmMGExMDRmY2UwNDIwMzQzMGMxZCJ9fX0="));

        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gp);
        } catch (Exception ignored) {
        }

        item.setItemMeta(meta);
        return item;
    }

    private void updateAllArmorStands() {
        for (Map.Entry<Player, List<ArmorStand>> entry : playerArmorStands.entrySet()) {
            Player player = entry.getKey();
            moveArmorStands(player);
        }
    }

    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), this::updateAllArmorStands, 0, 1L);
    }
}