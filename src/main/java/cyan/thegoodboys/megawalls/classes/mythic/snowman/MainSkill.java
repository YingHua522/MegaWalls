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
package cyan.thegoodboys.megawalls.classes.mythic.snowman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("寒冰之刺", classes);
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
        ArrayList<String> lore = new ArrayList<>();
        if (level == 1) {
            lore.add(" §8▪ §7发射3枚冰柱穿透敌人，");
            lore.add("   §7造成§a" + this.getAttribute(level) + "§7点伤害。");
            return lore;
        }
        lore.add(" §8▪ §7发射3枚冰柱穿透敌人，");
        lore.add("   §7造成§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§7点伤害。");
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
        new BukkitRunnable() {
            int i = 0;
            int hits = 0;

            @Override
            public void run() {
                Location origin;
                Location loc = player.getLocation();
                ArrayList<Player> damaged = new ArrayList<Player>();
                player.playSound(loc, Sound.FIREWORK_LAUNCH, 1.0f, 10.0f);
                Snowman.damage.clear();
                loc.add(0, 1, 0);
                origin = loc.clone();
                Vector to1 = loc.getDirection().normalize().multiply(0.3);
                Vector to = loc.getDirection().normalize().multiply(1.0);
                EulerAngle angle = new EulerAngle(0, 0, 12.0);
                loc.add(to1);
                ArmorStand firstStand = origin.getWorld().spawn(loc, ArmorStand.class);
                firstStand.setVisible(false);
                firstStand.setArms(true);
                firstStand.setItemInHand(new ItemStack(Material.ICE));
                firstStand.setSmall(true);
                firstStand.setMarker(true);
                firstStand.setRightArmPose(angle);

                loc.add(to1);

                ArmorStand secondStand = origin.getWorld().spawn(loc, ArmorStand.class);
                secondStand.setVisible(false);
                secondStand.setArms(true);
                secondStand.setItemInHand(new ItemStack(Material.PACKED_ICE));
                secondStand.setSmall(true);
                secondStand.setMarker(true);
                secondStand.setRightArmPose(angle);

                loc.add(to1);

                ArmorStand thirdStand = origin.getWorld().spawn(loc, ArmorStand.class);
                thirdStand.setVisible(false);
                thirdStand.setArms(true);
                thirdStand.setItemInHand(new ItemStack(Material.ICE));
                thirdStand.setSmall(true);
                thirdStand.setMarker(true);
                thirdStand.setRightArmPose(angle);

                loc.add(to1);

                ArmorStand fourthStand = origin.getWorld().spawn(loc, ArmorStand.class);
                fourthStand.setVisible(false);
                fourthStand.setArms(true);
                fourthStand.setItemInHand(new ItemStack(Material.PACKED_ICE));
                fourthStand.setSmall(true);
                fourthStand.setMarker(true);
                fourthStand.setRightArmPose(angle);

                final boolean[] didHitBlock = {false};

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (firstStand.getLocation().getBlock().getType() != Material.AIR) {
                            didHitBlock[0] = true;

                            firstStand.setGravity(false);
                            secondStand.setGravity(false);
                            thirdStand.setGravity(false);
                            fourthStand.setGravity(false);
                            firstStand.remove();
                            secondStand.remove();
                            thirdStand.remove();
                            fourthStand.remove();

                            this.cancel();
                        }
                        firstStand.setVelocity(to);
                        secondStand.setVelocity(to);
                        thirdStand.setVelocity(to);
                        fourthStand.setVelocity(to);
                        for (Player target : getNearbyPlayers(firstStand.getLocation(), player, 1)) {
                            if (damaged.contains(target)) continue;
                            PlayerUtils.realDamage(target, player, 4);
                            damaged.add(target);
                            target.setNoDamageTicks(0);
                            Snowman.hit.put(GamePlayer.get(target.getUniqueId()), Snowman.hit.getOrDefault(GamePlayer.get(target.getUniqueId()), 0) + 1);
                            if (Snowman.hit.getOrDefault(GamePlayer.get(target.getUniqueId()), 0) == 2) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                            }
                            firstStand.remove();
                            secondStand.remove();
                            thirdStand.remove();
                            fourthStand.remove();
                        }

                        if (firstStand.getLocation().distance(origin) > 50) {
                            firstStand.remove();
                            secondStand.remove();
                            thirdStand.remove();
                            fourthStand.remove();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 0, 1);
                i++;
                if (i == 3) {
                    Snowman.hit.clear();
                    cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 6);

        return true;
    }

    public void delay(Runnable run, int ticks) {
        delay(run, (long) ticks);
    }

    public void delay(Runnable run, long ticks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                run.run();
            }
        }.runTaskLater(MegaWalls.getInstance(), ticks);
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }
}

