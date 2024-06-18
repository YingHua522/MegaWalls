/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.block.Chest
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.squid;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {

    public FourthSkill(Classes classes) {
        super("海之眷顾", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int var1) {
        return 0;
    }

    public int getAttributes(int level) {
        switch (level) {
            case 1: {
                return 10;
            }
            case 2: {
                return 8;
            }
            case 3: {
                return 4;
            }
        }
        return 10;
    }

    public List<String> getInfo(int level) {
        List<String> lore = new ArrayList();
        if (level == 1) {
            lore.add(" §8▪ §7有§a" + StringUtils.percent(this.getAttributes(level)) + "§7的几率在箱子中找到");
        } else {
            lore.add(" §8▪ §7有§8" + StringUtils.percent(this.getAttributes(level - 1)) + " ➜");
            lore.add("    §a" + StringUtils.percent(this.getAttributes(level)) + "§7的几率在箱子中找到");
        }
        lore.add("    §71:00伤害吸收II药水。");
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
    public void onBlockBreak(final KitStatsContainer kitStats, final BlockBreakEvent e) {
        GamePlayer gp = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gp != null) {
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                if (e.getBlock().getType() == Material.TRAPPED_CHEST) {
                    kitStats.incrementChestCount(); // 增加箱子数量
                    double level = getAttributes(kitStats.getSkill4Level()); // 获取技能等级
                    if (kitStats.getChestCount() >= level) { // 如果箱子数量达到技能等级
                        Chest chest = (Chest) e.getBlock().getState();
                        ItemBuilder itemBuilder = (new ItemBuilder(Material.POTION, 3, (byte) 8262)).setDisplayName("§b鱿鱼药");
                        itemBuilder.addPotion(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1));
                        chest.getBlockInventory().addItem(itemBuilder.build());
                        kitStats.resetChestCount(); // 重置箱子数量
                    }
                }
            }, 3L);
        }
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        KitStatsContainer kitStatsContainer = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + "§e§l" + kitStatsContainer.getChestCount() + "/" + this.getAttributes(this.getPlayerLevel(gamePlayer));
    }
}

