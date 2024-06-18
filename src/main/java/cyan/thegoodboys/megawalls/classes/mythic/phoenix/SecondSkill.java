/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.BlockIterator
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.mythic.phoenix;

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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecondSkill
        extends Skill {
    private static final Set<Material> set = new HashSet<>();

    public SecondSkill(Classes classes) {
        super("阳炎射线", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.05;
            }
            case 2: {
                return 0.1;
            }
            case 3: {
                return 0.15;
            }
        }
        return 0.05;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7发出一道阳炎射线");
            lore.add(" §7若命中敌人则造成1点伤害");
            lore.add(" §7若未命中则在射线范围内的玩家获得速度I并回复1血");
            return lore;
        }
        lore.add(" §8▪ §7发出一道阳炎射线");
        lore.add(" §7若命中敌人则造成1点伤害");
        lore.add(" §7若未命中则在射线范围内的玩家获得速度I并回复1血");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        if (gamePlayer.getEnergy() < 25) {
            return false;
        }
        List<Player> healthed = new ArrayList<>();
        final Player player = gamePlayer.getPlayer();
        final Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setMetadata(MegaWalls.getMetadataValue(), MegaWalls.getFixedMetadataValue());
        arrow.setCustomName("qwq");
        arrow.setShooter(player);
        arrow.setVelocity(player.getLocation().getDirection().multiply(2.35));
        gamePlayer.setEnergy(gamePlayer.getEnergy() - 25);
        player.getWorld().playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
        new BukkitRunnable() {
            public void run() {
                if (arrow.isOnGround()) {
                    arrow.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1, 1);
                    for (Location loc : getHexagonPoints(arrow.getLocation())) {
                        ParticleEffect.FLAME.display(loc, 1.0f, 20);
                    }
                    for (Player player1 : PlayerUtils.getNearbyPlayers(arrow.getLocation(), 5.0)) {
                        GamePlayer gamePlayer1 = GamePlayer.get(player1.getUniqueId());
                        assert gamePlayer1 != null;
                        if (gamePlayer1.isSpectator() || !gamePlayer1.getGameTeam().isInTeam(gamePlayer)) continue;
                        if (healthed.contains(player1)) continue;
                        PlayerUtils.heal(player1, 2);
                        player1.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                        healthed.add(player1);
                    }
                    if (!arrow.isDead()) {
                        arrow.remove();
                        return;
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 0L);
        return true;
    }

    private List<Location> getHexagonPoints(Location center) {
        List<Location> points = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            double angle = 60 * i;
            double x = center.getX() + (double) 5 * Math.cos(Math.toRadians(angle));
            double z = center.getZ() + (double) 5 * Math.sin(Math.toRadians(angle));
            points.add(new Location(center.getWorld(), x, center.getY(), z));
        }
        return points;
    }
}

