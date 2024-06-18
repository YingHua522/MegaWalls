/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.zombie;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.WorldBoards;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("狂怒", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.50;
            }
            case 2: {
                return 0.62;
            }
            case 3: {
                return 0.75;
            }
        }
        return 0.08;
    }

    public int getSeconds(int level) {
        switch (level) {
            case 1: {
                return 4;
            }
            case 2: {
                return 5;
            }
            case 3: {
                return 6;
            }
        }
        return 4;
    }

    public int getCooldownSeconds(int level) {
        switch (level) {
            case 1: {
                return 32;
            }
            case 2: {
                return 25;
            }
            case 3: {
                return 18;
            }
        }
        return 32;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7被Arrow矢命中时,获得§a" + StringUtils.percent(this.getAttribute(level)) + "§7近战伤害加成");
            lore.add("   §7以及速度II效果,持续§a" + this.getSeconds(level) + "§7秒。");
            lore.add(" ");
            lore.add("§7冷却时间：§a32秒");
            return lore;
        }
        lore.add(" §8▪ §7被Arrow矢命中时,获得§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜");
        lore.add("   §a" + StringUtils.percent(this.getAttribute(level)) + "§7近战伤害加成");
        lore.add("   §7以及速度II效果,持续§8" + this.getSeconds(level - 1) + " ➜ §a" + this.getSeconds(level) + "§7秒。");
        lore.add("§7冷却时间：§8" + this.getCooldownSeconds(level - 1) + "秒 ➜ §a" + this.getCooldownSeconds(level) + "秒");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill3Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill3Level();
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        if (Zombie.skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        Player player = gamePlayer.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_HURT, 1, 0.5f);
        int level = kitStats.getSkill3Level();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.getSeconds(level) * 20, 1));
        WorldBoards.giveRedScreenEffect(gamePlayer.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Zombie.skill3Cooldown.getOrDefault(gamePlayer, 0) <= 0) {
                    WorldBoards.clearRedScreenEffect(gamePlayer.getPlayer());
                    this.cancel();
                }
                ParticleEffect.VILLAGER_ANGRY.display(0.1f, 0.4f, 0.1f, 1.0f, 1, player.getLocation().add(0.0, 1.0, 0.0), 10.0);
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 5);
        Zombie.skill3.put(gamePlayer, getSeconds(level));
        Zombie.skill3Cooldown.put(gamePlayer, 15);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Zombie.skill3Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§c§l" + Zombie.skill3Cooldown.get(gamePlayer) + "s");
    }
}

