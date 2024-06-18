/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.blaze;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("献祭爆裂", classes);
    }

    private static List<Player> getNearbyPlayers(Location location, Player player, double radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > radius))
                continue;
            players.add(other);
        }
        return players;
    }

    public static void shoot(final Player player) {
        final Map<Player, Integer> hitCount = new HashMap<>();
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                Location origin;
                Location loc = player.getLocation();
                ArrayList<Player> damaged = new ArrayList<Player>();
                player.getWorld().playSound(loc, Sound.GHAST_FIREBALL, 1.0f, 1.0f);
                loc.add(0, 1, 0);
                origin = loc.clone();
                Vector to1 = loc.getDirection().normalize().multiply(0.3);
                Vector to = loc.getDirection().normalize().multiply(1.0);
                EulerAngle angle = new EulerAngle(0, 0, 12.0);
                loc.add(to1);
                ArmorStand fourthStand = origin.getWorld().spawn(loc, ArmorStand.class);
                fourthStand.setVisible(false);
                fourthStand.setArms(true);
                fourthStand.setItemInHand(new ItemStack(Material.NETHERRACK));
                fourthStand.setSmall(true);
                fourthStand.setMarker(true);
                fourthStand.setRightArmPose(angle);
                Blaze.damage.clear();
                final boolean[] didHitBlock = {false};

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fourthStand.getLocation().getBlock().getType() != Material.AIR) {
                            didHitBlock[0] = true;

                            fourthStand.setGravity(false);
                            fourthStand.remove();

                            this.cancel();
                        }
                        fourthStand.setVelocity(to);
                        ParticleEffect.FLAME.display(fourthStand.getLocation(), 0, 2);
                        for (Player target : getNearbyPlayers(fourthStand.getLocation(), player, 2.5)) {
                            if (damaged.contains(target)) continue;
                            PlayerUtils.realDamage(target, player, 3);
                            damaged.add(target);
                            target.setNoDamageTicks(0);
                            hitCount.put(target, hitCount.getOrDefault(target, 0) + 1);
                            if (hitCount.get(target) == 3) {
                                PlayerUtils.realDamage(target, player, 3); // 添加火球伤害
                            }
                            fourthStand.remove();
                        }

                        if (fourthStand.getLocation().distance(origin) > 50) {
                            fourthStand.remove();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 0, 1);
                i++;
                if (i == 3) {
                    cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 6);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1.33;
            }
            case 2: {
                return 1.67;
            }
            case 3: {
                return 2.0;
            }
            case 4: {
                return 2.33;
            }
            case 5: {
                return 2.67;
            }
        }
        return 1.33;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7发射3颗火球,每颗");
            lore.add("   §7爆炸造成§a" + this.getAttribute(level) + "§7点伤害。");
            return lore;
        }
        lore.add(" §8▪ §7发射3颗火球,每颗");
        lore.add("   §7爆炸造成§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§7点伤害。");
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
    public boolean use(final GamePlayer gamePlayer, final KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 1, 1);
        new BukkitRunnable() {
            int i = 3;

            @Override
            public void run() {
                i--;
                if (i == 0) {
                    shoot(player);
                    cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 15);
        return true;
    }
}

