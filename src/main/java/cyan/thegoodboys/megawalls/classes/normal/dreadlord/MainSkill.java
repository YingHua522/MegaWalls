/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.normal.dreadlord;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("影爆", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 5.0;
            }
            case 2: {
                return 5.5;
            }
            case 3: {
                return 6.0;
            }
            case 4: {
                return 6.5;
            }
            case 5: {
                return 7.0;
            }
        }
        return 5.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u4e00\u6b21\u53d1\u5c04\u4e09\u4e2a\u51cb\u96f6\u9ab7\u9ac5,");
            lore.add("   \u00a77\u6bcf\u4e2a\u9020\u6210\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u4e00\u6b21\u53d1\u5c04\u4e09\u4e2a\u51cb\u96f6\u9ab7\u9ac5,");
        lore.add("   \u00a77\u6bcf\u4e2a\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
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
        Player player = gamePlayer.getPlayer();
        ArrayList<Player> damaged = new ArrayList<>();
        for (int i = 1; i <= 3; ++i) {
            final WitherSkull witherSkull = player.launchProjectile(WitherSkull.class);
            witherSkull.setMetadata(MegaWalls.getMetadataValue(), new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
            witherSkull.setVelocity(player.getLocation().add(0.0, 0.0, (double) i * 5.0).getDirection().multiply(1.5));
            new BukkitRunnable() {
                final double curve = 0.05; // 控制弧线的曲率
                public void run() {
                    if (witherSkull.isDead() || witherSkull.isOnGround()) {
                        this.cancel();
                        return;
                    }
                    Vector velocity = witherSkull.getVelocity();
                    velocity.setY(velocity.getY() + curve);
                    witherSkull.setVelocity(velocity);
                }
            }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);

            new BukkitRunnable() {
                public void run() {
                    if (witherSkull.isDead() || witherSkull.isOnGround()) {
                        witherSkull.remove();
                        witherSkull.getWorld().createExplosion(witherSkull.getLocation(), 3);
                        for (Player player1 : PlayerUtils.getNearbyPlayers(witherSkull, 4.0)) {
                            GamePlayer gamePlayer1 = GamePlayer.get(player1.getUniqueId());
                            assert gamePlayer1 != null;
                            if (gamePlayer1.isSpectator() || gamePlayer.getGameTeam().isInTeam(gamePlayer1)) continue;
                            if (damaged.contains(player1)) continue;
                            PlayerUtils.realDamage(player1, player, 6);
                            damaged.add(player1);
                        }
                        if (!witherSkull.isDead()) {
                            witherSkull.remove();
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        }
        PlayerUtils.food(player, 3);
        PlayerUtils.heal(player, 3.0);
        final int[] ticks = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks[0] > 60) {
                    this.cancel();
                    return;
                }
                Location loc = player.getLocation();
                Vector direction = loc.getDirection().normalize();
                Vector wingVector = new Vector(-direction.getZ(), 0.0D, direction.getX()).normalize();
                for (double y = 0; y <= 2; y += 0.2) {
                    double x = 0.5 * (4 * Math.pow(y, 2));
                    drawParticle(loc.clone().add(wingVector.clone().multiply(x)), loc.clone().add(wingVector.clone().multiply(-x)));
                }
                ticks[0]++;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 1);
        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1, 0.3F);
        player.playSound(player.getLocation(),Sound.ZOMBIE_IDLE,1,0.3F);
        return true;
    }

    private void drawParticle(Location... locations) {
        for (Location location : locations) {
            ParticleEffect.FLAME.display(0.0f, 0.0f, 0.0f, 0.0f, 10, location, 10.0);
        }
    }

}

