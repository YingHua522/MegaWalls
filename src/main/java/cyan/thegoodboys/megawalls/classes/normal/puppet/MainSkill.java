/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.normal.puppet;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("钢铁之击", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 2.6;
            }
            case 2: {
                return 3.2;
            }
            case 3: {
                return 3.8;
            }
            case 4: {
                return 4.4;
            }
            case 5: {
                return 5.0;
            }
        }
        return 2.6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u57284.5\u5757\u65b9\u5757\u534a\u5f84\u5185");
            lore.add("   \u00a77\u7684\u533a\u57df\u9020\u6210\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a2\u79d2");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u57284.5\u5757\u65b9\u5757\u534a\u5f84\u5185");
        lore.add("   \u00a77\u7684\u533a\u57df\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
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
        final Player player = gamePlayer.getPlayer();
        if (Puppet.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("\u00a7cYour ability is still cooldown for §e" + Puppet.skillCooldown.getOrDefault(gamePlayer, 0) + " seconds§!");
            return false;
        }
        List<Player> players = this.getNearbyPlayers(player, 4.5);
        if (players.isEmpty()) {
            player.sendMessage("\u00a7cNo player within skill range!");
            return false;
        }
        player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 2.0f);
        Location location = player.getLocation().add(0.0, 4.0, 0.0);
        final ArrayList<FallingBlock> fallingBlocks = new ArrayList<FallingBlock>();
        FallingBlock fb5 = location.getWorld().spawnFallingBlock(location, Material.DIAMOND_BLOCK, (byte) 0);
        FallingBlock fb6 = location.getWorld().spawnFallingBlock(location, Material.DIAMOND_BLOCK, (byte) 0);
        FallingBlock fb7 = location.getWorld().spawnFallingBlock(location, Material.DIAMOND_BLOCK, (byte) 0);
        FallingBlock fb8 = location.getWorld().spawnFallingBlock(location, Material.DIAMOND_BLOCK, (byte) 0);
        fallingBlocks.add(fb5);
        fallingBlocks.add(fb6);
        fallingBlocks.add(fb7);
        fallingBlocks.add(fb8);
        double vector2 = 0.1;
        fb5.setVelocity(new Vector(vector2, 0.0, vector2));
        fb6.setVelocity(new Vector(-vector2, 0.0, vector2));
        fb7.setVelocity(new Vector(vector2, 0.0, -vector2));
        fb8.setVelocity(new Vector(-vector2, 0.0, -vector2));
        for (FallingBlock fallingBlock : fallingBlocks) {
            fallingBlock.setMetadata(MegaWalls.getMetadataValue(), MegaWalls.getFixedMetadataValue());
            fallingBlock.setDropItem(false);
        }
        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline() || fb5.isOnGround()) {
                    for (FallingBlock fallingBlock : fallingBlocks) {
                        fallingBlock.remove();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 10L, 1L);
        for (Player player1 : this.getNearbyPlayers(player, 4.5)) {
            PlayerUtils.realDamage(player1, player, 5);
            player1.setNoDamageTicks(1);
            player1.setVelocity(new Vector(0, 0.8, 0));
            new BukkitRunnable() {
                public void run() {
                    player1.setVelocity(player.getLocation().toVector().subtract(player1.getLocation().add(0, 1, 0).toVector()).normalize().multiply(0.8));
                    this.cancel();
                }
            }.runTaskTimer(MegaWalls.getInstance(), 5L, 1L);
            player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2 * 20, 0));
            player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0));
        }
        new BukkitRunnable() {
            public void run() {
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 0.8f, 1.0f);
                this.cancel();
            }
        }.runTaskTimer(MegaWalls.getInstance(), 10L, 1L);
        Puppet.skillCooldown.put(gamePlayer, 2);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName() + " " + (Puppet.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l\u2715") : "\u00a7c\u00a7l" + Puppet.skillCooldown.get(gamePlayer) + "s");
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

    private List<Player> getNearbyPlayers(Player player, double radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther))
                continue;
            players.add(other);
        }
        return players;
    }
}

