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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillMessage
        implements Upgradeable {
    public static final KillMessage DEFAULT;

    static {
        HashMap<String, String> messages = new HashMap<String, String>();
        messages.put("Arrow", "\u88ab\u5c04\u6740");
        messages.put("Snowball", "\u88ab\u96ea\u7403\u7838\u6b7b");
        messages.put("Attack", "\u88ab\u51fb\u6740");
        messages.put("Poison", "\u88ab\u6bd2\u836f\u6740\u6b7b");
        messages.put("Explode", "\u88ab\u70b8\u6b7b");
        messages.put("Magic", "\u88ab\u9b54\u6cd5\u6740\u6b7b");
        DEFAULT = new KillMessage("Default", "\u9ed8\u8ba4", 0, messages);
    }

    private String name;
    private String displayName;
    private int price;
    private Map<String, String> messages;

    public KillMessage(String name, String displayName, int price, Map<String, String> messages) {
        this.name = name;
        this.displayName = displayName;
        this.price = price;
        this.messages = messages;
    }

    public String getMessage(String cause) {
        return this.messages.getOrDefault(cause, "\u88ab\u51fb\u6740");
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
        ArrayList<String> list = new ArrayList<String>();
        list.add("\u00a77\u89e3\u9501\u00a7c" + this.displayName + "\u00a77\u51fb\u6740\u4fe1\u606f\u3002");
        list.add(" ");
        for (String message : this.messages.values()) {
            list.add("\u00a7e\u73a9\u5bb6 \u00a7f" + message + ",\u51fb\u6740\u8005: \u00a7e\u4f60");
        }
        return list;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        gamePlayer.getPlayerStats().getEffectStats().updateKillMessage(this.name);
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), (String) ("lp user " + gamePlayer.getName() + " permission set MegaWalls.killmessage." + this.name));
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return 0;
    }

    @Override
    public Material getIconType() {
        return Material.PAPER;
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

    public String getDisplayName() {
        return this.displayName;
    }

    public int getPrice() {
        return this.price;
    }
}

