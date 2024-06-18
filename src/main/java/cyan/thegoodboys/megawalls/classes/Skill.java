/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package cyan.thegoodboys.megawalls.classes;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Material;

public abstract class Skill implements Upgradeable {
    public static final String TICK = "§a§l✓";
    public static final String FORK = "§c§l✕";
    private final String name;
    private final Classes classes;

    public Skill(String name, Classes classes) {
        this.name = name;
        this.classes = classes;
    }

    @Override
    public Material getIconType() {
        return this.getClasses().getIconType();
    }

    @Override
    public byte getIconData() {
        return this.getClasses().getIconData();
    }

    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        return true;
    }

    public String getSkillTip(GamePlayer gamePlayer) {
        return this.classes.getNameColor() + "§l" + this.name.toUpperCase() + " " + (gamePlayer.getEnergy() == 100 ? TICK : FORK);
    }

    @Override
    public int getCost(int level) {
        if (this.maxedLevel() == 5) {
            switch (level) {
                case 2:
                    return 1000;
                case 3:
                    return 2000;
                case 4:
                    return 4000;
                case 5:
                    return 7500;
                default:
                    return 999999;
            }
        } else {
            switch (level) {
                case 2:
                    return 2000;
                case 3:
                    return 5000;
                default:
                    return 999999;
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Classes getClasses() {
        return this.classes;
    }
}

