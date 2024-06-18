/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.squid;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainSkill extends Skill {
    private static final Set<Material> set = new HashSet<Material>();

    static {
        for (Material material : Material.values()) {
            set.add(material);
        }
    }

    public MainSkill(Classes classes) {
        super("鱿鱼飞溅", classes);
    }

    public static void createCircle(final Location location, final int ticks, final boolean potion) {
        new BukkitRunnable() {
            int i = 0;
            float radius = 0f;

            public void run() {
                int particles = 50;
                if (radius < 1.5) {
                    radius += 0.5;
                }
                ParticleEffect.OrdinaryColor color = new ParticleEffect.OrdinaryColor(Color.AQUA);
                if (potion) {
                    color = new ParticleEffect.OrdinaryColor(Color.BLACK);
                }
                for (int i = 0; i < particles; i++) {
                    double angle, x, z;
                    angle = 2 * Math.PI * i / particles;
                    x = Math.cos(angle) * radius;
                    z = Math.sin(angle) * radius;
                    location.add(x, 0, z);
                    ParticleEffect.REDSTONE.display(color, location, 100);
                    location.subtract(x, 0, z);
                }
                i++;
                if (i >= ticks) cancel();
            }
        }.runTaskTimer((Plugin) MegaWalls.getInstance(), 1, 1);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.3667;
            }
            case 2: {
                return 0.45;
            }
            case 3: {
                return 0.5333;
            }
            case 4: {
                return 0.6167;
            }
            case 5: {
                return 0.7;
            }
        }
        return 0.3667;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7将敌人传送至你的位置并且");
            lore.add("   §7对3格以内的所有敌人造成3点伤害。");
            lore.add("   §7你会恢复你造成的伤害的§a" + StringUtils.percent(this.getAttribute(level)) + "§7。");
            lore.add("   §7最多恢复7点生命。");
            lore.add(" ");
            lore.add("§7冷却时间:§a2秒");
            return lore;
        }
        lore.add(" §8▪ §7将敌人传送至你的位置并且");
        lore.add("   §7对3格以内的所有敌人造成3点伤害。");
        lore.add("   §7你会恢复你造成的伤害的§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜");
        lore.add("   §a" + StringUtils.percent(this.getAttribute(level)) + "§7。");
        lore.add("   §7最多恢复7点生命。");
        lore.add(" ");
        lore.add("§7冷却时间:§a2秒");
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
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        if (Squid.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("\u00a7cYour ability is still cooldown for §e" + Squid.skillCooldown.getOrDefault(gamePlayer, 0) + " seconds§!");
            return false;
        }
        int play = 0;
        Player target = null;
        for (Block block : player.getLineOfSight(set, 4)) {
            for (Player player2 : this.getNearbyPlayers(block.getLocation(), player, 3)) {
                if (target != null && !(player2.getLocation().distance(player.getLocation()) < target.getLocation().distance(player.getLocation())))
                    continue;
                target = player2;
            }
        }
        if (target == null) {
            player.sendMessage("§cNo player within skill range!");
            return false;
        }
        for (Player player1 : this.getNearbyPlayers(player, 5)) {
            play++;
            PlayerUtils.realDamage(player1, player, 3.5);
            player1.setNoDamageTicks(2);
            player1.setVelocity(new Vector(0, 0.6, 0));
            new BukkitRunnable() {
                public void run() {
                    player1.setVelocity(player.getLocation().toVector().subtract(player1.getLocation().toVector()).normalize().multiply(0.8));
                    cancel();
                }
            }.runTaskTimer(MegaWalls.getInstance(), 5L, 0);
        }
        int finalPlay = play;
        new BukkitRunnable() {
            public void run() {
                PlayerUtils.heal(player, (finalPlay * 3.5) * 0.75);
                gamePlayer.addEnergy(finalPlay < 8 ? finalPlay * 10 : 80, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                cancel();
            }
        }.runTaskTimer(MegaWalls.getInstance(), 5L, 0);
        ParticleUtils.play(EnumParticle.WATER_SPLASH, player.getLocation(), 0.5, 0.5, 0.5, 1, 500);
        player.getWorld().playSound(player.getLocation(), Sound.SPLASH2, 0.5f, 1);
        Squid.skillCooldown.put(gamePlayer, 2);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Squid.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + Squid.skillCooldown.get(gamePlayer) + "s");
    }

    private List<Player> getNearbyPlayers(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(player.getLocation()) > (double) radius)
                continue;
            players.add(other);
        }
        return players;
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius)
                continue;
            players.add(other);
        }
        return players;
    }
}

