package cyan.thegoodboys.megawalls.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class CustomMick {
    private static final Map<Player, List<ArmorStand>> milkBarrierEntities = new HashMap<>();

    public static void activateMilkBarrier(Player player) {
        if (!milkBarrierEntities.containsKey(player)) {
            milkBarrierEntities.put(player, new ArrayList<>());
        }

        for (int i = 0; i < 5; i++) {
            ArmorStand entity = createMilkBarrierEntity(player);
            milkBarrierEntities.get(player).add(entity);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                moveMilkBarrierEntities(player);
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
    }

    public static void moveMilkBarrierEntities(Player player) {
        if (!milkBarrierEntities.containsKey(player)) {
            return;
        }

        List<ArmorStand> entities = milkBarrierEntities.get(player);
        if (entities != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    double timeFactor = System.currentTimeMillis() % 10000 / 5000.0 * Math.PI;
                    for (int i = 0; i < entities.size(); i++) {
                        ArmorStand armorStand = entities.get(i);
                        double angle = 4 * Math.PI * i / 6 + timeFactor;
                        Location loc = player.getLocation().add(1 * Math.sin(angle), 1.0, 1 * Math.cos(angle));
                        // 计算朝向向量
                        // 使用朝向向量设置盔甲架的朝向
                        armorStand.teleport(new Location(player.getWorld(), loc.getX(), loc.getY(), loc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                    }
                }
            }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        }
    }

    private static ArmorStand createMilkBarrierEntity(Player player) {
        Location location = player.getLocation();
        ArmorStand armorStand = player.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setMetadata("mick", MegaWalls.getFixedMetadataValue());
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setHelmet(createSkull());
        return armorStand;
    }

    private static ItemStack createMilkBucket() {
        return new ItemStack(Material.MILK_BUCKET,1);
    }


    private static ItemStack createSkull() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UxZmM2M2QzMDNlYjVmMzY2YWVjYWU2ZDI1MGQ0ZTJkNzc5YTlmNWVmOGRlYWZmNWIzYmM5NTMwN2ZmOSJ9fX0="));

        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gp);
        } catch (Exception ignored) {
        }

        item.setItemMeta(meta);
        return item;
    }


    public static void deactivateMilkBarrier(Player player) {
        if (!milkBarrierEntities.containsKey(player)) {
            return;
        }
        List<ArmorStand> entities = milkBarrierEntities.get(player);
        if (!entities.isEmpty()) {
            entities.get(0).remove();
            entities.remove(0);
        }
    }

    public static void deactivateAllMilkBarriers(Player player) {
        if (!milkBarrierEntities.containsKey(player)) {
            return;
        }
        List<ArmorStand> entities = milkBarrierEntities.get(player);
        for (ArmorStand entity : entities) {
            entity.remove();
        }
        entities.clear();
    }
}
