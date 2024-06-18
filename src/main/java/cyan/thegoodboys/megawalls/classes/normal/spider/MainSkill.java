/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.normal.spider;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("跳跃", classes);
    }

    public static List<Block> getSphere(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;
        for (int x = X - radius; x <= X + radius; ++x) {
            for (int z = Z - radius; z <= Z + radius; ++z) {
                if ((X - x) * (X - x) + (Z - z) * (Z - z) > radiusSquared) continue;
                blocks.add(location.getWorld().getBlockAt(x, Y, z));
            }
        }
        return blocks;
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.0;
            }
            case 2: {
                return 3.5;
            }
            case 3: {
                return 4.0;
            }
            case 4: {
                return 4.5;
            }
            case 5: {
                return 5;
            }
        }
        return 3.0;
    }

    public int getHits(int level) {
        switch (level) {
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
            case 4: {
                return 3;
            }
            case 5: {
                return 4;
            }
        }
        return 1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5411\u524d\u8df3\u8dc3\u81f3\u7a7a\u4e2d,\u83b7\u5f975\u79d2\u7684\u751f\u547d");
            lore.add("   \u00a77\u6062\u590dI\u6548\u679c\u3002\u843d\u5730\u65f6,\u5468\u56f4\u654c\u4eba\u53d7");
            lore.add("   \u00a77\u5230\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u548c\u6301\u7eed4\u79d2\u7684\u7f13\u6162I\u6548\u679c,");
            lore.add("   \u00a77\u4f60\u5219\u83b7\u5f97\u00a7a" + this.getHits(level) + "\u00a77\u6b21\u6bd2\u6db2\u4e4b\u51fb\u7684\u673a\u4f1a\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5411\u524d\u8df3\u8dc3\u81f3\u7a7a\u4e2d,\u83b7\u5f975\u79d2\u7684\u751f\u547d");
        lore.add("   \u00a77\u6062\u590dI\u6548\u679c\u3002\u843d\u5730\u65f6,\u5468\u56f4\u654c\u4eba\u53d7");
        lore.add("   \u00a77\u5230\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u548c\u6301\u7eed4\u79d2\u7684\u7f13\u6162I\u6548\u679c,");
        lore.add("   \u00a77\u4f60\u5219\u83b7\u5f97\u00a78" + this.getHits(level - 1) + (this.getHits(level - 1) == this.getHits(level) ? "" : " \u279c \u00a7a" + this.getHits(level)) + "\u00a77\u6b21\u6bd2\u6db2\u4e4b\u51fb\u7684\u673a\u4f1a\u3002");
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
        int mode = Spider.mode.getOrDefault(gamePlayer, 0);
        int mode1 = 0;
        if (mode <= 0) {
            mode1 = (int) 2.0;
        }
        if (mode >= 1) {
            mode1 = (int) 3.0;
        }
        final Player player = gamePlayer.getPlayer();
        player.setNoDamageTicks(20);
        player.setVelocity(player.getLocation().getDirection().multiply(mode1));
        player.setVelocity(new Vector(player.getVelocity().getX(), 1.6D, player.getVelocity().getZ()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 40, 0));
        Spider.hit.clear();
        new BukkitRunnable() {
            private final List<FallingBlock> fallingBlocks = new ArrayList<>();

            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                if (!player.isOnGround()) {
                    return;
                }
                for (Player player1 : MainSkill.this.getNearbyPlayers(player, 4)) {
                    PlayerUtils.realDamage(player1, player, 5);
                    player1.setVelocity(new Vector(0, -1, 0));
                    player1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 0));
                }
                ParticleUtils.play(EnumParticle.EXPLOSION_LARGE, player.getLocation(), 0.1, 0.1, 0.1, 0, 8);
                ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, player.getLocation().add(0.0, 1.0, 0.0), 5.0);
                Location location = player.getLocation().add(0.0, 2.0, 0.0);
                ParticleEffect.LAVA.display(0.0f, 0.0f, 0.0f, 1.0f, 20, location, 10.0);
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, (float) 0.7, (float) 0.5);
                location.getWorld().playSound(location, Sound.SPIDER_DEATH, 1.0f, 0.0f);
                FallingBlock fb = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb2 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb3 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb4 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb5 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb6 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb7 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                FallingBlock fb8 = location.getWorld().spawnFallingBlock(location, Material.WEB, (byte) 0);
                this.fallingBlocks.add(fb);
                this.fallingBlocks.add(fb2);
                this.fallingBlocks.add(fb3);
                this.fallingBlocks.add(fb4);
                this.fallingBlocks.add(fb5);
                this.fallingBlocks.add(fb6);
                this.fallingBlocks.add(fb7);
                this.fallingBlocks.add(fb8);
                double vector = 0.4;
                double vector2 = 0.3;
                fb.setVelocity(new Vector(vector, 0.0, 0.0));
                fb2.setVelocity(new Vector(0.0, 0.0, vector));
                fb3.setVelocity(new Vector(-vector, 0.0, 0.0));
                fb4.setVelocity(new Vector(0.0, 0.0, -vector));
                fb5.setVelocity(new Vector(vector2, 0.0, vector2));
                fb6.setVelocity(new Vector(-vector2, 0.0, vector2));
                fb7.setVelocity(new Vector(vector2, 0.0, -vector2));
                fb8.setVelocity(new Vector(-vector2, 0.0, -vector2));
                FallingBlock[] fallingBlocks = new FallingBlock[]{fb, fb2, fb3, fb4, fb5, fb6, fb7, fb8};
                for (FallingBlock fallingBlock : fallingBlocks) {
                    fallingBlock.setMetadata(MegaWalls.getMetadataValue(), MegaWalls.getFixedMetadataValue());
                    fallingBlock.setDropItem(false);
                }
                // 遍历周围的方块
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            // 获取每个方块的位置
                            Location blockLocation = player.getLocation().clone().add(x, y, z);
                            //获取玩家位置方块的xyz
                            Block block = blockLocation.getBlock();
                            if (MegaWalls.getInstance().getGame().isProtected(blockLocation) || MegaWalls.getInstance().getGame().isUnbreakable(blockLocation) || (block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.BARRIER) || block.getType() == Material.BEDROCK) {
                                continue;
                            }
                            // 破坏方块
                            blockLocation.getBlock().setType(Material.AIR);
                        }
                    }
                }
                Spider.skill2.put(gamePlayer, 4);
                this.cancel();
            }
        }.runTaskTimer(MegaWalls.getInstance(), 10L, 0L);
        return true;
    }

    private List<Player> getNearbyPlayers(Player player, double radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

