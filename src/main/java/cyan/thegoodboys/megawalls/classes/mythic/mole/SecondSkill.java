/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.mole;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondSkill extends Skill {
    public static Map<GamePlayer, Integer> exp1 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> exp2 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> exp3 = new HashMap<GamePlayer, Integer>();

    public SecondSkill(Classes classes) {
        super("捷径", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.1;
            }
            case 2: {
                return 0.2;
            }
            case 3: {
                return 0.3;
            }
        }
        return 0.1;
    }

    public int getSpeed(int level) {
        switch (level) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
        }
        return 1;
    }

    public int getDig(int level) {
        switch (level) {
            case 1: {
                return 1;
            }
            case 2:
            case 3: {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u53ef\u5f00\u91c7\u7684\u65b9\u5757\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
            lore.add("   \u00a77\u83b7\u5f97\u901f\u5ea6\u00a7a" + StringUtils.level(this.getSpeed(level)) + "\u00a77\u548c\u6025\u8feb\u00a7a" + StringUtils.level(this.getDig(level)) + "\u00a77\u6548\u679c,\u6301\u7eed4\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u53ef\u5f00\u91c7\u7684\u65b9\u5757\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387");
        lore.add("   \u00a77\u83b7\u5f97\u901f\u5ea6" + (this.getSpeed(level - 1) == this.getSpeed(level) ? "\u00a78" + StringUtils.level(this.getSpeed(level)) : "\u00a78" + StringUtils.level(this.getSpeed(level - 1)) + " \u279c \u00a7a" + StringUtils.level(this.getSpeed(level))) + "\u00a77\u548c\u6025\u8feb" + (this.getDig(level - 1) == this.getDig(level) ? "\u00a78" + StringUtils.level(this.getDig(level)) : "\u00a78" + StringUtils.level(this.getDig(level - 1)) + " \u279c \u00a7a" + StringUtils.level(this.getDig(level))) + "\u00a77\u6548\u679c,\u6301\u7eed4\u79d2\u3002");
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
        int max = 4;
        int dig = Mole.skill2.getOrDefault(gamePlayer, max);
        if (dig == max) {
            Mole.skill2.put(gamePlayer, 1);
            return true;
        }
        int i = dig + 1;
        Mole.skill2.put(gamePlayer, i);
        if (i == max) {
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, this.getSpeed(level) - 1));
            if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 80, this.getDig(level) - 1));

        }
        int a = exp1.getOrDefault(gamePlayer, 0);
        int b = exp2.getOrDefault(gamePlayer, 0);
        int c = exp3.getOrDefault(gamePlayer, 0);
        // 检查玩家背包是否有空间
        if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
            if (a >= 249) {
                gamePlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.APPLE).setDisplayName("&e垃圾苹果").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp1.put(gamePlayer, 0);
            }
            if (b >= 59) {
                gamePlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.PUMPKIN_PIE).setDisplayName("&e垃圾派").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp2.put(gamePlayer, 0);
            }
            if (c >= 29) {
                gamePlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.COOKIE).setDisplayName("&e垃圾曲奇").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp3.put(gamePlayer, 0);
            }
        }else if (gamePlayer.getEnderChest().firstEmpty() != -1) {
            if (a >= 249) {
                gamePlayer.getEnderChest().addItem(new ItemBuilder(Material.APPLE).setDisplayName("&e垃圾苹果").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp1.put(gamePlayer, 0);
            }
            if (b >= 59) {
                gamePlayer.getEnderChest().addItem(new ItemBuilder(Material.PUMPKIN_PIE).setDisplayName("&e垃圾派").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp2.put(gamePlayer, 0);
            }
            if (c >= 29) {
                gamePlayer.getEnderChest().addItem(new ItemBuilder(Material.COOKIE).setDisplayName("&e垃圾曲奇").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp3.put(gamePlayer, 0);
            }
        }else {
            if (a >= 249) {
                gamePlayer.getPlayer().getWorld().dropItemNaturally(gamePlayer.getPlayer().getLocation(),new ItemBuilder(Material.APPLE).setDisplayName("&e垃圾苹果").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp1.put(gamePlayer, 0);
            }
            if (b >= 59) {
                gamePlayer.getPlayer().getWorld().dropItemNaturally(gamePlayer.getPlayer().getLocation(),new ItemBuilder(Material.PUMPKIN_PIE).setDisplayName("&e垃圾派").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp2.put(gamePlayer, 0);
            }
            if (c >= 29) {
                gamePlayer.getPlayer().getWorld().dropItemNaturally(gamePlayer.getPlayer().getLocation(),new ItemBuilder(Material.COOKIE).setDisplayName("&e垃圾曲奇").addEnchantment(Enchantment.DURABILITY, 10).build());
                exp3.put(gamePlayer, 0);
            }
        }
        return true;
    }

    @Override

    public String getSkillTip(GamePlayer gamePlayer) {
        return ("&e&lShortcut &7" + Mole.skill2.getOrDefault(gamePlayer, 0) + "/4" + " &e&lJunk Cookie &7" + exp3.getOrDefault(gamePlayer, 0) + "/30 " + " &e&lJunk Pie&7 " + exp2.getOrDefault(gamePlayer, 0) + "/60 " + " &e&lJunk Apple&7 " + exp1.getOrDefault(gamePlayer, 0) + "/250" + " &7REC " + Mole.rec.getOrDefault(gamePlayer, 2) + "/3").toUpperCase();
    }
}

