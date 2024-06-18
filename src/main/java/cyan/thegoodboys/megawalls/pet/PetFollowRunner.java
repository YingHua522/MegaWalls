/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.pet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class PetFollowRunner {
    public final ArmorStand stand;
    public final ArmorStand nameTag;
    private final Player owner;
    private BukkitRunnable run;
    private int runnable = 0;
    private boolean upmotion;
    private double currmotion;

    public PetFollowRunner(Player owner) {
        this.owner = owner;
        this.stand = (ArmorStand) owner.getWorld().spawnEntity(owner.getLocation().add(0, 1, 0), EntityType.ARMOR_STAND);
        ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner("FabledShadowFury").build();
        this.stand.setItemInHand(itemStack);
        this.stand.setCustomNameVisible(false);
        this.stand.setGravity(false);
        this.stand.setMarker(true);
        this.stand.setVisible(false);
        this.stand.setRightArmPose(new EulerAngle(0, 0, 0.0));
        this.nameTag = (ArmorStand) this.stand.getWorld().spawnEntity(this.makeNameTagLocation().add(0, 1, 0), EntityType.ARMOR_STAND);
        this.nameTag.setCustomNameVisible(true);
        this.nameTag.setMarker(true);
        this.nameTag.setGravity(false);
        this.nameTag.setCustomName("§7[Lvl 200] §6" + owner.getName() + "'s " + "§6Golden Dragon");
        this.nameTag.setVisible(false);
        this.idle();
    }

    public static ItemStack CustomHeadTexture(String url2, String customUUID) {
        ItemStack skull = new ItemStack(Material.SKULL, 1);
        if (url2 == null || url2.isEmpty()) {
            return skull;
        }
        ItemMeta skullMeta = skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.fromString(customUUID), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url2).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

    private Location makeNameTagLocation() {
        Location loc = this.stand.getLocation();
        loc.setPitch(0.0f);
        Vector dir = loc.getDirection();
        dir.multiply(-0.3);
        loc = loc.add(dir);
        loc = loc.add(0.0, 1.0, 0.0);
        return loc;
    }

    public void updatePet() {
        this.nameTag.setCustomName("§7[Lvl 200] §e" + this.owner.getName() + "'s " + "§eGolden Dragon");
    }

    public void remove() {
        this.stand.remove();
        this.nameTag.remove();
        this.run.cancel();
    }

    private void idle() {
        this.run = new BukkitRunnable() {
            public void run() {
                if (PetFollowRunner.this.stand.getNearbyEntities(1.3, 1.3, 1.3).contains(PetFollowRunner.this.owner)) {
                    Location loc = PetFollowRunner.this.stand.getLocation();
                    int newrunnable = PetFollowRunner.this.runnable - 1;
                    --PetFollowRunner.this.runnable;
                    if (newrunnable <= 0) {
                        if (PetFollowRunner.this.runnable == 0) {
                            PetFollowRunner.this.currmotion = PetFollowRunner.this.upmotion ? 0.04 : -0.04;
                        }
                        double newMotion = PetFollowRunner.this.upmotion ? PetFollowRunner.this.currmotion - 0.002 : PetFollowRunner.this.currmotion + 0.002;
                        PetFollowRunner.this.currmotion = newMotion;
                        PetFollowRunner.this.stand.teleport(loc.add(0.0, newMotion, 0.0));
                        PetFollowRunner.this.nameTag.teleport(PetFollowRunner.this.makeNameTagLocation());
                        if (newMotion <= -0.04 && PetFollowRunner.this.upmotion) {
                            PetFollowRunner.this.upmotion = false;
                            PetFollowRunner.this.runnable = 5;
                        } else if (newMotion >= 0.04 && !PetFollowRunner.this.upmotion) {
                            PetFollowRunner.this.upmotion = true;
                            PetFollowRunner.this.runnable = 5;
                        }
                    } else if (PetFollowRunner.this.upmotion) {
                        PetFollowRunner.this.stand.teleport(loc.add(0.0, 0.04, 0.0));
                        PetFollowRunner.this.nameTag.teleport(PetFollowRunner.this.makeNameTagLocation());
                    } else {
                        PetFollowRunner.this.stand.teleport(loc.add(0.0, -0.04, 0.0));
                        PetFollowRunner.this.nameTag.teleport(PetFollowRunner.this.makeNameTagLocation());
                    }
                } else {
                    PetFollowRunner.this.runnable = 5;
                    PetFollowRunner.this.upmotion = true;
                    PetFollowRunner.this.run.cancel();
                    PetFollowRunner.this.follow();
                }
            }
        };
        this.run.runTaskTimer((Plugin) MegaWalls.getInstance(), 1L, 0L);
    }

    private void follow() {
        this.run = new BukkitRunnable() {

            public void run() {
                if (!PetFollowRunner.this.stand.getWorld().getNearbyEntities(PetFollowRunner.this.stand.getEyeLocation(), 1.0, 0.1, 1.0).contains(PetFollowRunner.this.owner)) {
                    PetFollowRunner.this.stand.setHeadPose(PetFollowRunner.this.setHeadPos(PetFollowRunner.this.stand, PetFollowRunner.this.stand.getLocation().setDirection(PetFollowRunner.this.owner.getLocation().add(0.0, 0.5, 0.0).toVector().subtract(PetFollowRunner.this.stand.getEyeLocation().getDirection())).getYaw(), PetFollowRunner.this.getPitchFromTo(PetFollowRunner.this.stand.getEyeLocation(), PetFollowRunner.this.owner.getLocation().add(0.0, 0.5, 0.0))));
                    Location loc = PetFollowRunner.this.stand.getEyeLocation();
                    loc.setPitch(PetFollowRunner.this.getPitchFromTo(loc, PetFollowRunner.this.owner.getLocation().add(0.0, 0.5, 0.0)));
                    loc = loc.setDirection(PetFollowRunner.this.owner.getLocation().add(0.0, 0.5, 0.0).toVector().subtract(PetFollowRunner.this.stand.getEyeLocation().toVector()));
                    Vector dir = loc.getDirection();
                    dir.normalize();
                    dir.multiply(0.4);
                    loc.add(dir);
                    PetFollowRunner.this.stand.teleport(loc);
                    PetFollowRunner.this.nameTag.teleport(PetFollowRunner.this.makeNameTagLocation());
                } else {
                    PetFollowRunner.this.run.cancel();
                    PetFollowRunner.this.idle();
                }
            }
        };
        this.run.runTaskTimer((Plugin) MegaWalls.getInstance(), 1L, 0L);
    }

    private float getPitchFromTo(Location armorstandLoc, Location direction) {
        double dx;
        double hypo;
        double dy = direction.getY() - armorstandLoc.getY();
        float angle = (float) Math.toDegrees(Math.cos(dy / (hypo = Math.sqrt(dy * dy + (dx = direction.getX() - armorstandLoc.getX()) * dx))));
        if (angle < 0.0f) {
            angle += 180.0f;
        }
        return angle;
    }

    private EulerAngle setHeadPos(ArmorStand as, double yaw, double pitch) {
        double xint = Math.cos(yaw / Math.PI);
        double zint = Math.sin(yaw / Math.PI);
        double yint = Math.sin(pitch / Math.PI);
        EulerAngle ea = as.getHeadPose();
        ea.setX(xint);
        ea.setY(yint);
        ea.setZ(zint);
        return ea;
    }

}

