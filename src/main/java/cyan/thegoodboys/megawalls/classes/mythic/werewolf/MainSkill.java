/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.werewolf;


import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("狼人变身", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 7.2;
            }
            case 2: {
                return 8.4;
            }
            case 3: {
                return 9.6;
            }
            case 4: {
                return 10.8;
            }
            case 5: {
                return 13.0;
            }
        }
        return 3.2;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7获得持续§a" + this.getAttribute(level) + "§7秒的速度 II 效果。");
            lore.add("   §7在这期间,你会恢复你造成伤害的30%");
            lore.add("   §7并且攻击敌人时造成2点真实伤害");
            return lore;
        }
        lore.add(" §8▪ §7获得持续§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§7秒的速度 II 效果。");
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
    public boolean use(final GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        if (Werewolf.skill.contains(gamePlayer)) {
            gamePlayer.sendMessage("§c你已经激活了狼人变身!");
            return false;
        }
        if (Werewolf.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            gamePlayer.sendMessage("§c你的技能冷却还有 §e" + Werewolf.skillCooldown.getOrDefault(gamePlayer, 0) + " 秒§!");
            return false;
        }
        List<Player> damaged = new ArrayList<>();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6 * 20, 1));
        Werewolf.skill.add(gamePlayer);
        Werewolf.skillCooldown.put(gamePlayer, 6);
        player.getWorld().playSound(player.getLocation(), Sound.WOLF_GROWL, 1.0f, 1.0f);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            player.getWorld().playSound(player.getLocation(), Sound.WOLF_GROWL, 1.0f, 3.0f);
        }, 11L);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            player.getWorld().playSound(player.getLocation(), Sound.WOLF_GROWL, 1.0f, 4.0f);
        }, 22L);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            player.getWorld().playSound(player.getLocation(), Sound.WOLF_GROWL, 1.0f, 5.0f);
        }, 33L);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            Werewolf.skill.remove(gamePlayer);
            Werewolf.skillDamage.remove(gamePlayer);
            player.getWorld().playSound(player.getLocation(), Sound.WOLF_DEATH, 1.0f, 1.0f);
            ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
            int attackedPlayers = 0;
            for (Player player1 : getNearbyPlayers(player.getLocation(), player, 3)) {
                if (damaged.contains(player1)) continue;
                GamePlayer gameOther = GamePlayer.get(player.getUniqueId());
                if (Objects.requireNonNull(GamePlayer.get(player1.getUniqueId())).getGameTeam().isInTeam(gameOther))
                    continue;
                PlayerUtils.realDamage(player1, player, Werewolf.skillhit.size() + attackedPlayers * 1.67);
                PlayerUtils.heal(player, Werewolf.skillhit.size());
                damaged.add(player1);
                attackedPlayers++;
            }
        }, (long) this.getAttribute(this.getPlayerLevel(gamePlayer)) * 20L);
        return true;
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(location) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Werewolf.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + Werewolf.skillCooldown.get(gamePlayer) + "s");
    }
}

