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
package cyan.thegoodboys.megawalls.classes.mythic.dragon;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MainSkill
extends Skill {
    public MainSkill(Classes classes) {
        super("炽热吐息", classes);
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
        lore.add("   \u00a77\u6bcf个\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
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
        final int fireballs = 20;
        final ArrayList fired = new ArrayList();
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection().multiply(10);
        new BukkitRunnable(){
            int count = 0;
                public void run() {
                    if (count < fireballs) {
                        final SmallFireball smallFireball = player.launchProjectile(SmallFireball.class);
                        smallFireball.setMetadata("dragon", MegaWalls.getFixedMetadataValue());
                        smallFireball.setVelocity(player.getLocation().add(direction).getDirection());
                        smallFireball.setShooter(player);
                        smallFireball.setYield(0);
                        smallFireball.getWorld().playSound(smallFireball.getLocation(),Sound.LAVA_POP,1,1);
                        count++;
                    } else {
                        this.cancel();
                    }
                }
        }.runTaskTimer((Plugin)MegaWalls.getInstance(), 0L, 2L);
        return true;
    }
}

