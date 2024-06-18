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
package cyan.thegoodboys.megawalls.classes.mythic.phoenix;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {

    public FourthSkill(Classes classes) {
        super("凤凰之泪", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.0;
            }
            case 2: {
                return 8.0;
            }
            case 3: {
                return 6.0;
            }
        }
        return 3.0;
    }

    public int getAttributes(int level) {
        switch (level) {
            case 1: {
                return 3;
            }
            case 2: {
                return 8;
            }
            case 3: {
                return 6;
            }
        }
        return 3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u4f60\u6709\u00a7a" + StringUtils.percent(this.getAttributes(level)) + "\u00a77\u7684\u51e0\u7387\u5728\u7bb1\u5b50\u91cc\u627e\u5230\u751f\u547d\u6062\u590dIII(4\u79d2)\u836f\u6c34");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u4f60\u6709\u00a78" + StringUtils.percent(this.getAttributes(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u5728\u7bb1\u5b50\u91cc\u627e\u5230\u751f\u547d\u6062\u590dIII(4\u79d2)\u836f\u6c34");
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
                    kitStats.incrementPhxChest();
                    double level = getAttributes(kitStats.getSkill4Level());
                    if (kitStats.getPhxChest() >= level) { // 如果箱子数量达到技能等级
                        Chest chest = (Chest) e.getBlock().getState();
                        ItemStack item = new ItemStack(Material.POTION);
                        item.setAmount(3);
                        item.setDurability((byte) 2);
                        PotionMeta im = (PotionMeta) item.getItemMeta();
                        im.setDisplayName("§c凤凰的生命恢复III (3s)");
                        im.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 2), true);
                        item.setItemMeta(im);
                        Potion pot = new Potion(1);
                        pot.setSplash(true);
                        pot.apply(item);
                        chest.getBlockInventory().addItem(item);
                        kitStats.resetPhxChest();
                    }
                }
            }, 3L);
        }
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        KitStatsContainer kitStatsContainer = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + "§e§l" + kitStatsContainer.getPhxChest() + "/" + this.getAttributes(this.getPlayerLevel(gamePlayer));
    }
}