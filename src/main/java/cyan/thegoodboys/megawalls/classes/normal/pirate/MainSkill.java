/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.normal.pirate;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
        super("加农炮火", classes);
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
            lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u70ae\u5f39,\u51fb\u4e2d\u65f6\u9020\u6210\u00a7a" + this.getAttribute(level) + "\u00a77\u7684\u4f24\u5bb3,");
            lore.add("   \u00a77\u5728\u7206\u70b8\u524d\u5bf9\u6bcf\u4e2a\u73a9\u5bb6\u9020\u6210\u4e00\u534a\u4f24\u5bb3,");
            lore.add("   \u00a77\u5e76\u51fb\u9000\u9644\u8fd1\u7684\u73a9\u5bb6\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u53d1\u5c04\u70ae\u5f39,\u51fb\u4e2d\u65f6\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u7684\u4f24\u5bb3,");
        lore.add("   \u00a77\u5728\u7206\u70b8\u524d\u5bf9\u6bcf\u4e2a\u73a9\u5bb6\u9020\u6210\u4e00\u534a\u4f24\u5bb3,");
        lore.add("   \u00a77\u5e76\u51fb\u9000\u9644\u8fd1\u7684\u73a9\u5bb6\u3002");
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
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection().multiply(10);
        final WitherSkull witherSkull = (WitherSkull) player.launchProjectile(WitherSkull.class);
        witherSkull.setMetadata(MegaWalls.getMetadataValue(), (MetadataValue) new FixedMetadataValue((Plugin) MegaWalls.getInstance(), (Object) gamePlayer.getGameTeam()));
        witherSkull.setVelocity(player.getLocation().add(direction).getDirection());
        witherSkull.setYield(0);
        new BukkitRunnable() {
            final ArrayList<Player> damaged = new ArrayList<Player>();

            public void run() {
                if (witherSkull.isDead() || witherSkull.isOnGround()) {
                    witherSkull.setYield(0);
                    double level = getAttribute(kitStats.getSkillLevel());
                    for (Player player1 : PlayerUtils.getNearbyPlayers(witherSkull, 4.0)) {
                        GamePlayer gamePlayer1 = GamePlayer.get(player1.getUniqueId());
                        if (gamePlayer1 != null && (gamePlayer1.isSpectator() || gamePlayer.getGameTeam().isInTeam(gamePlayer1)))
                            continue;
                        if (this.damaged.contains(player1)) continue;
                        PlayerUtils.realDamage(player1, player, level);
                        this.damaged.add(player1);
                    }
                    if (!witherSkull.isDead()) {
                        witherSkull.remove();
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        return true;
    }
}

