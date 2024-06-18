/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 */
package cyan.thegoodboys.megawalls.effect;

import cyan.thegoodboys.megawalls.classes.Upgradeable;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class HologramEffect
        implements Upgradeable {
    public static final HologramEffect DEFAULT = new HologramEffect("Default", "None", 0);
    private String name;
    private String line;
    private int price;

    public HologramEffect(String name, String line, int price) {
        this.name = name;
        this.line = line;
        this.price = price;
    }

    @Override
    public int maxedLevel() {
        return 0;
    }

    @Override
    public double getAttribute(int level) {
        return 0.0;
    }

    @Override
    public List<String> getInfo(int level) {
        return Arrays.asList("\u00a77\u6240\u6709\u7684\u51cb\u96f6\u6b7b\u4ea1\u540e,\u6bcf\u51fb", "\u00a77\u6740\u4e00\u4e2a\u654c\u4eba\u90fd\u4f1a\u5728\u5176\u575f\u5893", "\u00a77\u4e0a\u653e\u7f6e\u4e00\u5f20\u5168\u606f\u56fe\u3002");
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        gamePlayer.getPlayerStats().getEffectStats().updateHologramEffect(this.name);
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), (String) ("lp user " + gamePlayer.getName() + " permission set MegaWalls.hologrameffect." + this.name));
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return 0;
    }

    @Override
    public Material getIconType() {
        return Material.SIGN;
    }

    @Override
    public byte getIconData() {
        return 0;
    }

    @Override
    public int getCost(int level) {
        return this.price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getLine() {
        return this.line;
    }

    public int getPrice() {
        return this.price;
    }
}

