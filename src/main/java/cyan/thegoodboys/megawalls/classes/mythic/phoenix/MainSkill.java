/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes.mythic.phoenix;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MainSkill
        extends Skill {
    private static final Set<Material> set = new HashSet<Material>();

    static {
        Collections.addAll(set, Material.values());
    }

    public MainSkill(Classes classes) {
        super("灵魂契约", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 12.0;
            }
            case 2: {
                return 13.0;
            }
            case 3: {
                return 14.0;
            }
            case 4: {
                return 15.0;
            }
            case 5: {
                return 16.0;
            }
        }
        return 13.0;
    }

    public int getSpeed(int level) {
        switch (level) {
            case 1:
            case 2: {
                return 0;
            }
            case 3:
            case 4: {
                return 1;
            }
            case 5: {
                return 2;
            }
        }
        return 0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ 与一名队友签订契约,");
            lore.add("   §7签订契约的队友将立刻回复15血和50能量");
            lore.add("   §7范围：16格");
            lore.add("   §7冷却：25秒");
            return lore;
        }
        lore.add(" §8▪ 与一名队友签订契约,");
        lore.add("   §7签订契约的队友将立刻回复15血和50能量");
        lore.add("   §7范围：16格");
        lore.add("   §7冷却：25秒");
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
        if (Phoenix.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("§c技能正在冷却中！");
            return false;
        }
        Player target = null;
        for (Block block : player.getLineOfSight(set, (int) this.getAttribute(kitStats.getSkillLevel()))) {
            for (Player player2 : this.getTeammates(block.getLocation(), player, 2, 999)) {
                if (target != null && !(player2.getLocation().distance(player.getLocation()) < target.getLocation().distance(player.getLocation())))
                    continue;
                target = player2;
            }
        }
        if (target == null) {
            player.sendMessage("§c没有玩家在目标范围内！");
            return false;
        }
        GamePlayer uwu = GamePlayer.get(target.getUniqueId());
        PlayerUtils.heal(target, ((target.getMaxHealth() - target.getHealth()) * 0.5));
        assert uwu != null;
        uwu.addEnergy(50, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
        target.sendMessage("§a你被§e" + player.getPlayer().getName() + "§a的灵魂契约治疗了");
        for (Player player2 : this.getTeammates(target.getLocation(), player, 2, 3)) {
            PlayerUtils.heal(player2, ((player2.getMaxHealth() - player2.getHealth()) * 0.35));
            uwu.addEnergy(35, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            player2.sendMessage("§a你被§e" + player.getPlayer().getName() + "§a的灵魂契约治疗了");
        }
        new BukkitRunnable() {
            public void run() {
                for (Location location : LocationUtils.getCircle(player.getLocation(), 0.5, 20)) {
                    ParticleEffect.HEART.display(0.0f, 0.0f, 0.0f, 0.0f, 1, location, 30.0);
                }
                player.getWorld().playSound(player.getLocation(), Sound.CREEPER_HISS, 1.0f, 1.0f);
            }
        }.runTaskLater((Plugin) MegaWalls.getInstance(), 5L);
        Phoenix.skillCooldown.put(gamePlayer, 25);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName() + " " + (Phoenix.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + Phoenix.skillCooldown.get(gamePlayer) + "秒");
    }

    private List<Player> getTeammates(Location location, Player player, int radius, int size) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            assert gameOther != null;
            if (gameOther.isSpectator() || !GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther) || gameOther == gamePlayer || players.size() > size)
                continue;
            players.add(other);
        }
        return players;
    }
}

