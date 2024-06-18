/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.lawless;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    Location location;

    public SecondSkill(Classes classes) {
        super("抓钩", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.1;
            }
            case 2: {
                return 0.2;
            }
            case 3: {
                return 0.3;
            }
        }
        return 0.1;
    }

    public int getSpeed(int level) {
        switch (level) {
            case 1:
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
        }
        return 1;
    }

    public int getDig(int level) {
        switch (level) {
            case 1: {
                return 1;
            }
            case 2:
            case 3: {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7消耗§e60§7点能量");
            lore.add("   §7释放一个抓钩,冷却30秒");
            return lore;
        }
        lore.add(" §8▪ §7消耗§e60§7点能量");
        lore.add("   §7释放一个抓钩，冷却30秒");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Lawless.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() > 59 ? "§a§l✓" : "§c§l✕") : "§c§l" + Lawless.skill2Cooldown.get(gamePlayer) + "s") + (Lawless.Break.getOrDefault(gamePlayer, 0) == 0 ? " (好滴)" : " (坏滴)");
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        if (Lawless.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Location loc = player.getLocation();
        Vector vec = loc.getDirection();
        vec.multiply(1.0);
        Location loc2 = loc.add(vec.getX(), 0, vec.getZ());
        FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation(), Material.ANVIL, (byte) 0);
        Bat bat = (Bat) fb.getWorld().spawnEntity(fb.getLocation(), EntityType.BAT);
        fb.setDropItem(false);
        bat.setLeashHolder(player);
        bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0));
        bat.setMetadata("REN", new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
        fb.setVelocity(player.getLocation().getDirection().multiply(1.5));
        new BukkitRunnable() {
            int ticks = 0;

            public void run() {
                bat.teleport(fb.getLocation().add(0, -1, 0));
                ticks++;

                if ((isHalfBlock(fb.getLocation().add(-0.66, 0, 0).getBlock()) || isHalfBlock(fb.getLocation().add(-0.66, 0, 0.66).getBlock()) || isHalfBlock(fb.getLocation().add(-0.66, 0, -0.66).getBlock()) || isHalfBlock(fb.getLocation().add(0.66, 0, 0).getBlock()) || isHalfBlock(fb.getLocation().add(0.66, 0, 0.66).getBlock()) || isHalfBlock(fb.getLocation().add(0.66, 0, -0.66).getBlock()) || isHalfBlock(fb.getLocation().add(0, 0, 0.66).getBlock()) || isHalfBlock(fb.getLocation().add(0, 0, -0.66).getBlock()) || fb.isOnGround()) && ticks >= 10) {
                    Location playerLoc = player.getLocation();
                    Location hookLoc = fb.getLocation().getBlock().getLocation();
                    Location change = hookLoc.subtract(playerLoc);
                    player.setVelocity(new Vector(player.getVelocity().getX(), 3.0D, player.getVelocity().getZ()));
                    player.setVelocity(change.toVector().multiply(change.toVector().getY() > 1 ? 1.0 : 0.7));
                    player.setFallDistance(0);
                    fb.remove();
                    bat.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        if (Lawless.Break.getOrDefault(gamePlayer, 0) == 0) {
            Lawless.fall.put(gamePlayer, 7);
        }
        Lawless.Break.put(gamePlayer, 1);
        Lawless.skill2Cooldown.put(gamePlayer, 20);
        return true;
    }

    private boolean isHalfBlock(Block block) {
        Material type = block.getType();
        return type == Material.STEP || type == Material.WOOD_STEP;
    }
}

