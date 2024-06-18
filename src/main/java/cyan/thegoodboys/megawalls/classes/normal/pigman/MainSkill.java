/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.pigman;

import com.google.common.util.concurrent.AtomicDouble;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import koji.developerkit.runnable.KRunnable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MainSkill extends Skill {
    public static final Random random = new Random(System.nanoTime());

    public MainSkill(Classes classes) {
        super("灵魂献祭", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 5.0;
            }
            case 3: {
                return 6.0;
            }
            case 4: {
                return 7.0;
            }
            case 5: {
                return 8.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f975\u79d2\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
            lore.add("   \u00a77\u5468\u56f4\u7684\u73a9\u5bb6\u5c06\u53d7\u5230");
            lore.add("   \u00a77\u706b\u7130\u65cb\u6da1\u7684\u5f71\u54cd,\u6301\u7eed5\u79d2");
            lore.add("   \u00a77\u9020\u6210\u6700\u591a\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u83b7\u5f975\u79d2\u6297\u6027\u63d0\u5347I\u6548\u679c\u3002");
        lore.add("   \u00a77\u5468\u56f4\u7684\u73a9\u5bb6\u5c06\u53d7\u5230");
        lore.add("   \u00a77\u706b\u7130\u65cb\u6da1\u7684\u5f71\u54cd,\u6301\u7eed5\u79d2");
        lore.add("   \u00a77\u9020\u6210\u6700\u591a\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3");
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
    public boolean use(final GamePlayer gamePlayer, KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 1.0f, 1.0f);
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
        Pigman.skill.put(gamePlayer, 5);
        List<Player> players = player.getWorld().getPlayers();
        for (Player target : getNearbyPlayers(player.getLocation(), player, 7)) {
            Location loc = player.getEyeLocation();
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setGravity(false);
            stand.setVisible(false);
            AtomicDouble degree = new AtomicDouble(0.0);
            new KRunnable(task -> {
                Location location = this.faceLocation(stand, target.getLocation());
                if (stand.getLocation().distanceSquared(target.getLocation()) <= 0.5) {
                    task.cancel();
                    stand.remove();
                    new KRunnable(nextTask -> {
                        if (target.isDead()) {
                            nextTask.cancel();
                            return;
                        }
                        PlayerUtils.realDamage(target, player, 1);
                    }, 100L).runTaskTimer(MegaWalls.getInstance(), 0L, 20L);
                    return;
                }
                location.add((target.getLocation()).toVector().subtract(stand.getLocation().toVector()).normalize().multiply(0.3));
                move(stand, players, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                if (!((stand.getLocation().add(0, 1, 0)).getBlock().isEmpty())) {
                    stand.remove();
                    player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1.0f, 1.0f);
                    task.cancel();
                }
                double x = 0.4 * Math.cos(Math.toRadians(degree.getAndAdd(10.0)));
                double y = 0.4 * Math.sin(Math.toRadians(degree.get()));
                Vector vec = new Vector(x, 0.0, y);
                vec = this.rotateVectorAroundX(vec, location.getPitch());
                ParticleEffect.FLAME.display(stand.getLocation().add(vec), 0, 2);
            }, 20L * 10).runTaskTimerAsynchronously(MegaWalls.getInstance(), 0L, 1L);
        }
        return true;
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }

        return players;
    }

    public Location faceLocation(Entity entity, Location to) {
        if (entity.getWorld() != to.getWorld()) {
            return entity.getLocation();
        }
        Location fromLocation = entity.getLocation();
        double xDiff = to.getX() - fromLocation.getX();
        double yDiff = to.getY() - fromLocation.getY();
        double zDiff = to.getZ() - fromLocation.getZ();
        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);
        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0;
        if (zDiff < 0.0) {
            yaw += Math.abs(180.0 - yaw) * 2.0;
        }
        Location loc = entity.getLocation();
        loc.setYaw((float) (yaw - 90.0));
        loc.setPitch((float) (pitch - 90.0));
        return loc;
    }

    public Vector rotateVectorAroundX(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);
        double currentY = vector.getY();
        double currentZ = vector.getZ();
        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);
        return new Vector(vector.getX(), currentY * cosine - currentZ * sine, currentY * sine + currentZ * cosine);
    }

    public void move(Entity as, Collection<Player> players, double x, double y, double z, float yaw, float pitch) {
        as.teleport(new Location(as.getWorld(), x, y, z, yaw, pitch));
    }

    public Vector rotateVectorAroundY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);
        double currentX = vector.getX();
        double currentZ = vector.getZ();
        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);
        return new Vector(cosine * currentX - sine * currentZ, vector.getY(), sine * currentX + cosine * currentZ);
    }
}

