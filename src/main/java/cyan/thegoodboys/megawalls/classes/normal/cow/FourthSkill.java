/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.normal.cow;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthSkill extends CollectSkill {

    public static Map<GamePlayer,Integer> stoneMultiplier = new HashMap<>();

    public FourthSkill(Classes classes) {
        super("超级杀菌", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.01;
            }
            case 2: {
                return 0.02;
            }
            case 3: {
                return 0.03;
            }
        }
        return 0.01;
    }

    public int getBreakStone(int level) {
        switch (level) {
            case 1: {
                return 100;
            }
            case 2: {
                return 80;
            }
            case 3: {
                return 64;
            }
        }
        return 100;
    }


    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u77f3\u5934\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u6389\u843d\u725b\u5976\u6876\u3002");
            lore.add("   \u00a77\u996e\u7528\u725b\u5976\u6876\u53ef\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u548c\u751f\u547d\u6062\u590dI,");
            lore.add("   \u00a77\u6301\u7eed5\u79d2\u3002\u725b\u5976\u6876\u53ef\u88ab\u4e22\u51fa\u3002");
            lore.add("   \u00a77\u5bf9\u4e8e\u975e\u5976\u725b\u804c\u4e1a\u7684\u73a9\u5bb6,\u751f\u547d\u6062\u590d\u52a0\u500d\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u77f3\u5934\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u6389\u843d\u725b\u5976\u6876\u3002");
        lore.add("   \u00a77\u996e\u7528\u725b\u5976\u6876\u53ef\u83b7\u5f97\u6297\u6027\u63d0\u5347I\u548c\u751f\u547d\u6062\u590dI,");
        lore.add("   \u00a77\u6301\u7eed5\u79d2\u3002\u725b\u5976\u6876\u53ef\u88ab\u4e22\u51fa\u3002");
        lore.add("   \u00a77\u5bf9\u4e8e\u975e\u5976\u725b\u804c\u4e1a\u7684\u73a9\u5bb6,\u751f\u547d\u6062\u590d\u52a0\u500d\u3002");
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
    public String getSkillTip(GamePlayer gamePlayer) {
        int ironOreCount = stoneMultiplier.getOrDefault(gamePlayer, 0);
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + "§7§l" + ironOreCount + "/" + this.getBreakStone(this.getPlayerLevel(gamePlayer));
    }

    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.STONE) {
            GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
            if (gamePlayer != null) {
                int level = this.getBreakStone(kitStats.getSkill4Level());
                stoneMultiplier.put(gamePlayer, stoneMultiplier.getOrDefault(gamePlayer, 0) + 1);
                if (stoneMultiplier.get(gamePlayer) >= level) {
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.COW_IDLE, 1.0f, 1.0f);
                    ItemStack milkBucket = new ItemBuilder(Material.MILK_BUCKET,2).setDisplayName("超级杀菌牛奶").setLore("§7引用获得以下效果:","§a给予队伍生命恢复").build();
                    if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                        gamePlayer.getPlayer().getInventory().addItem(milkBucket);
                    } else if (gamePlayer.getPlayer().getEnderChest().firstEmpty() != -1) {
                        gamePlayer.getEnderChest().addItem(milkBucket);
                    } else {
                        gamePlayer.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), milkBucket);
                    }
                    stoneMultiplier.put(gamePlayer, 0);
                }
            }
        }
    }
}

