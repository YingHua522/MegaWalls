/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.squid;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("喷墨", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 2.0;
            }
            case 2: {
                return 3.0;
            }
            case 3: {
                return 4.0;
            }
        }
        return 2.0;
    }

    public double getSeconds(int level) {
        switch (level) {
            case 1: {
                return 0.8;
            }
            case 2: {
                return 1.6;
            }
            case 3: {
                return 2.4;
            }
        }
        return 0.8;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7当你喝完药水的时候,");
            lore.add("   §7令§a" + this.getAttribute(level) + "§7格范围内的敌人失明§a" + this.getSeconds(level) + "§7秒。");
            return lore;
        }
        lore.add(" §8▪ §7当你喝完药水的时候,");
        lore.add("   §7令§8" + this.getAttribute(level - 1) + " ➜");
        lore.add("   §a" + this.getAttribute(level) + "§7格范围内的敌人失明§8" + this.getSeconds(level - 1) + " ➜ §a" + this.getSeconds(level) + "§7秒。");
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
        int level = kitStats.getSkill2Level();
        for (Player player : this.getNearbyPlayers(gamePlayer.getPlayer(), this.getAttribute(level))) {
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (this.getSeconds(level) * 20.0), 0));
            gamePlayer.getPlayer().getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 0.5f);
            ParticleUtils.play(EnumParticle.SMOKE_LARGE, player.getLocation(), 0.5, 0.5, 0.5, 0.1, 50);
            for (Player victim : PlayerUtils.getNearbyPlayers(player,5)) {
                if (gamePlayer.getGameTeam().isInTeam(gamePlayer)) {
                    continue;
                }
                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
                ParticleUtils.play(EnumParticle.SMOKE_LARGE, victim.getEyeLocation(), 0.5, 0.5, 0.5, 0.1, 50);
            }
        }
        return true;
    }

    private List<Player> getNearbyPlayers(Player player, double radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

