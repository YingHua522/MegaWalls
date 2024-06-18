/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
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

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("垃圾食品", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.07;
            }
            case 2: {
                return 0.14;
            }
            case 3: {
                return 0.21;
            }
        }
        return 0.07;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u7528Spade\u6316\u6398\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u6389\u843d");
            lore.add("   \u00a77\u5783\u573e\u98df\u54c1\u3002\u4f60\u670920%\u51e0\u7387\u56de");
            lore.add("   \u00a77\u6536\u5403\u6389\u7684Golden Apple\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u7528Spade\u6316\u6398\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387\u6389\u843d");
        lore.add("   \u00a77\u5783\u573e\u98df\u54c1\u3002\u4f60\u670920%\u51e0\u7387\u56de");
        lore.add("   \u00a77\u6536\u5403\u6389\u7684Golden Apple\u3002");
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
        int maxRec = 3;
        int rec = Mole.rec.getOrDefault(gamePlayer, 2);
        if (rec == maxRec) {
            Mole.rec.put(gamePlayer, 1);
            return true;
        }
        int i = rec + 1;
        Mole.rec.put(gamePlayer, i);
        if (i == maxRec) {
            gamePlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.APPLE).setDisplayName("&eJunk Apple").addEnchantment(Enchantment.DURABILITY, 10).build());
        }
        return true;
    }
}

