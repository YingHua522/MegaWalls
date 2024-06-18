/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.hunter;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.WorldBoards;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ThirdSkill extends Skill {
    public static final Map<String, PotionEffect> potions = new HashMap<>();
    private Map.Entry<String, PotionEffect> entry;

    public ThirdSkill(Classes classes) {
        super("自然之力", classes);
        potions.put("&bSpeed I 11s", new PotionEffect(PotionEffectType.SPEED, 220, 0));
        potions.put("&7Resistance I 6s", new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0));
        potions.put("&eHasteI 15s", new PotionEffect(PotionEffectType.FAST_DIGGING, 300, 0));
        potions.put("&cStrength I 5s", new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0));
        potions.put("&dRegenration II 10s", new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
        potions.put("&bSpeed II 6s", new PotionEffect(PotionEffectType.SPEED, 120, 2));
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 35.0;
            }
            case 2: {
                return 30.0;
            }
            case 3: {
                return 20.0;
            }
        }
        return 50.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7每§a" + this.getAttribute(level) + "§7秒获得一个随机增益效果。");
            lore.add("   §7可能是持续11秒的速度I,6秒的");
            lore.add("   §7抗性提升I,15秒的急迫I,6秒的");
            lore.add("   §7力量I,10秒的生命恢复II或者6秒");
            lore.add("   §7的速度II。高墙倒下后将不再获得");
            lore.add("   §7急迫I效果。");
            return lore;
        }
        lore.add(" §8▪ §7每§8" + this.getAttribute(level - 1) + " ➜");
        lore.add("   §a" + this.getAttribute(level) + "§7秒获得一个随机增益效果。");
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
        if (Hunter.skill3Cooldown.getOrDefault(gamePlayer, 25) > 10) {
            return false;
        } else if (Hunter.skill3Cooldown.getOrDefault(gamePlayer, 25) == 10) {
            ArrayList<Map.Entry<String, PotionEffect>> list = new ArrayList<>(potions.entrySet());
            this.entry = list.get(new Random().nextInt(list.size() - 1));
            Player player = gamePlayer.getPlayer();
            Hunter.skill3.put(gamePlayer, entry.getKey());
            new BukkitRunnable() {
                public void run() {
                    player.addPotionEffect(entry.getValue());
                    sound(gamePlayer);
                    cancel();
                }
            }.runTaskTimer(MegaWalls.getInstance(), 200L, 0L);
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        if (this.entry.getValue().getType() == PotionEffectType.INCREASE_DAMAGE) {
            WorldBoards.giveRedScreenEffect(gamePlayer.getPlayer());
        }else {
            WorldBoards.clearRedScreenEffect(gamePlayer.getPlayer());
        }
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Hunter.skill3.containsKey(gamePlayer) ? "§f(" + Hunter.skill3.get(gamePlayer) + "§f)§c§l" + Hunter.skill3Cooldown.getOrDefault(gamePlayer, 0) + "s" : "§c§l" + Hunter.skill3Cooldown.getOrDefault(gamePlayer, 0) + "s");
    }

    public void sound(GamePlayer gamePlayer) {
        if (this.entry.getValue().getType() == PotionEffectType.INCREASE_DAMAGE) {
            gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(), Sound.GHAST_FIREBALL, 1, 1);
        }else if (this.entry.getValue().getType() == PotionEffectType.SPEED) {
            gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(), Sound.FIREWORK_BLAST2, 1, 1);
        }else if (this.entry.getValue().getType() == PotionEffectType.DAMAGE_RESISTANCE) {
            gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(),Sound.CAT_HIT,1,1);
        }else if (this.entry.getValue().getType() == PotionEffectType.REGENERATION) {
            gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(),Sound.CAT_HIT,1,1);
        }
    }
}

