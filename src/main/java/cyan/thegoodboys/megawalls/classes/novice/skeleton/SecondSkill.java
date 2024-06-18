/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes.novice.skeleton;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("箭矢回收", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.2;
            }
            case 2: {
                return 0.4;
            }
            case 3: {
                return 0.8;
            }
        }
        return 0.6;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7Arrow击中敌人时,有§a" + StringUtils.percent(this.getAttribute(level)) + "§7几率");
            lore.add("   §7获得两只Arrow并恢复饱食度。");
            return lore;
        }
        lore.add(" §8▪ §7Arrow击中敌人时,有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜");
        lore.add("   §a" + StringUtils.percent(this.getAttribute(level)) + "§7几率");
        lore.add("   §7获得两支Arrow并恢复饱食度。");
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
        if ((double) MegaWalls.getRandom().nextInt(100) <= this.getAttribute(kitStats.getSkill2Level()) * 100.0) {
            gamePlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.ARROW, 2).build());
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return null;
    }
}

