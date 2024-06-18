/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.pirate;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public static final List<ItemStack> items = new ArrayList<>();

    static {
        items.add(new ItemBuilder(Material.BOW).setDisplayName("§6友好的椰子Bow").addEnchantment(Enchantment.ARROW_KNOCKBACK, 1).build());
        items.add(new ItemBuilder(Material.BOW).setDisplayName("§6火神的力量弓").addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName("§6渔夫崽的苦茶子").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName("§6点燃的剑").addEnchantment(Enchantment.FIRE_ASPECT, 1).setDurability((short) 1).build());
        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName("§6沾血的剑").addEnchantment(Enchantment.DAMAGE_UNDEAD, 3).build());
        items.add(new ItemBuilder(Material.COOKED_BEEF,2).setDisplayName("§6神秘牛排").build());
        items.add(new ItemBuilder(Material.ARROW, 12).build());
    }

    public FourthSkill(Classes classes) {
        super("宝藏掠夺", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.3;
            }
            case 2: {
                return 0.4;
            }
            case 3: {
                return 0.5;
            }
        }
        return 0.3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7当你击杀一名敌人,将有§a" + StringUtils.percent(this.getAttribute(level)) + "§7的");
            lore.add("   §7几率额外掉落宝箱。在死斗模式中,");
            lore.add("   §7几率会提升至§a" + StringUtils.percent(this.getAttribute(level) * 2.0) + "§7。宝箱可能含");
            lore.add("   §7海盗专属物品。");
            return lore;
        }
        lore.add(" §8▪ §7当你击杀一名敌人,将有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ §a" + StringUtils.percent(this.getAttribute(level)) + "§7的");
        lore.add("   §7几率额外掉落宝箱。在死斗模式中,");
        lore.add("   §7几率会提升至§8" + StringUtils.percent(this.getAttribute(level - 1) * 2.0) + " ➜ §a" + StringUtils.percent(this.getAttribute(level) * 2.0) + "§7。宝箱可能含");
        lore.add("   §7海盗专属物品。");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill4Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
    }


    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
    }
}

