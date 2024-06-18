/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.classes;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class EquipmentPackage implements Upgradeable {
    private final Classes classes;

    public EquipmentPackage(Classes classes) {
        this.classes = classes;
    }

    @Override
    public String getName() {
        return "职业套装";
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        return 0.0;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addEquipLevel();
        kitStats.updateInventory(null);
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getEquipLevel();
    }

    @Override
    public Material getIconType() {
        return this.getClasses().getIconType();
    }

    @Override
    public byte getIconData() {
        return this.getClasses().getIconData();
    }

    @Override
    public int getCost(int level) {
        switch (level) {
            case 2:
                return 500;
            case 3:
                return 2000;
            case 4:
                return 7000;
            case 5:
                return 15000;
            default:
                return 999999;
        }
    }

    public abstract List<ItemStack> getEquipments(int var1);

    public Classes getClasses() {
        return this.classes;
    }
}

