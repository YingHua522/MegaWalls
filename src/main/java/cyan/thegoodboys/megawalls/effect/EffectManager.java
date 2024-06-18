/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.gmail.filoghost.holographicdisplays.api.Hologram
 *  com.gmail.filoghost.holographicdisplays.api.HologramsAPI
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.effect;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

public class EffectManager {
    private static final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
    private static final List<HologramEffect> hologramEffects = new ArrayList<HologramEffect>();
    private static final List<KillMessage> killMessages = new ArrayList<KillMessage>();

    static {
        EffectManager.registerHologramEffect(HologramEffect.DEFAULT);
        EffectManager.registerHologramEffect("2", "#ZHAN", 1);
        EffectManager.registerHologramEffect("3", "Good", 1);
        EffectManager.registerHologramEffect("4", "Mamma Mia！", 1);
        EffectManager.registerHologramEffect("5", "It was so simple!", 1);
        EffectManager.registerHologramEffect("6", "#Free Carry", 1);
        EffectManager.registerHologramEffect("7", "L", 5);
        EffectManager.registerKillMessage(KillMessage.DEFAULT);
        HashMap<String, String> messages = new HashMap<String, String>();
        messages.put("Arrow", "\u4e2d\u7bad\u65e0\u6570");
        messages.put("Snowball", "\u88ab\u51bb\u4f4f\u4e86");
        messages.put("Attack", "\u88ab\u903c\u5165\u4e86\u672b\u8def");
        messages.put("Poison", "\u8f93\u4e86\u4e00\u573a\u996e\u9152\u6bd4\u8d5b");
        messages.put("Explode", "\u88ab\u70b8\u836f\u70b8\u6b7b");
        messages.put("Magic", "\u8f93\u6389\u4e86\u5bf9\u51b3");
        EffectManager.registerKillMessage("2", "\u897f\u90e8", 5, messages);
        messages = new HashMap();
        messages.put("Arrow", "\u88ab\u51fb\u5012");
        messages.put("Snowball", "\u6298\u621f\u6c89\u6c99");
        messages.put("Attack", "\u88ab\u5316\u4e3a\u7070\u70ec");
        messages.put("Poison", "\u88ab\u878d\u5316");
        messages.put("Explode", "\u88ab\u711a\u70e7");
        messages.put("Magic", "\u88ab\u84b8\u53d1");
        EffectManager.registerKillMessage("3", "\u706b\u7130", 5, messages);
        messages = new HashMap();
        messages.put("Arrow", "\u88ab\u4e18\u6bd4\u7279\u4e4b\u7bad\u5c04\u4e2d");
        messages.put("Snowball", "\u53d7\u5230\u51b7\u6de1");
        messages.put("Attack", "\u88ab\u62b1\u5f97\u592a\u7d27");
        messages.put("Poison", "\u996e\u4e0b\u4e86\u7231\u60c5\u836f\u6c34");
        messages.put("Explode", "\u88ab\u7231\u60c5\u70b8\u5f39\u51fb\u4e2d");
        messages.put("Magic", "\u4e0d\u654c\u4f60");
        EffectManager.registerKillMessage("4", "\u7231\u60c5", 5, messages);
        messages = new HashMap();
        messages.put("Arrow", "\u88ab\u8fdc\u8ddd\u79bb\u6740\u6b7b");
        messages.put("Snowball", "\u88ab\u516c\u6b63\u5730\u7ec8\u7ed3");
        messages.put("Attack", "\u88ab\u51c0\u5316\u4e86");
        messages.put("Poison", "\u88ab\u795e\u5723\u4e4b\u6c34\u6740\u6b7b");
        messages.put("Explode", "\u88ab\u65bd\u4ee5\u590d\u4ec7\u7684\u6b63\u4e49");
        messages.put("Magic", "\u88ab\u5f52\u4e3a\u5c18\u571f");
        EffectManager.registerKillMessage("5", "\u5723\u9a91\u58eb", 5, messages);
        messages = new HashMap();
        messages.put("Arrow", "\u88ab\u5c04\u6740");
        messages.put("Snowball", "\u88ab\u96ea\u7403\u7838\u6b7b");
        messages.put("Attack", "\u88ab\u585e\u8fdb\u4e86\u6234\u7ef4 \u00b7 \u743c\u65af\u7684\u7bb1\u5b50");
        messages.put("Poison", "\u88ab\u6717\u59c6\u9152\u6740\u6b7b");
        messages.put("Explode", "\u88ab\u52a0\u519c\u70ae\u51fb\u4e2d");
        messages.put("Magic", "\u88ab\u9b54\u6cd5\u51fb\u6740");
        EffectManager.registerKillMessage("6", "\u6d77\u76d7", 5, messages);
        messages = new HashMap();
        messages.put("Arrow", "\u88ab\u70e4\u6210\u4e86\u8089\u4e32");
        messages.put("Snowball", "\u88ab\u6492\u4e0a\u4e86\u8fa3\u6912\u7c89");
        messages.put("Attack", "\u88ab\u5207\u6210\u7247");
        messages.put("Poison", "\u88ab\u716e\u7126\u4e86");
        messages.put("Explode", "\u88ab\u6df1\u5ea6\u6cb9\u70b8");
        messages.put("Magic", "\u88ab\u716e\u5f00\u4e86");
        EffectManager.registerKillMessage("7", "\u70e7\u70e4", 5, messages);
    }

    public static HologramEffect getHologramEffect(String name) {
        for (HologramEffect hologramEffect : hologramEffects) {
            if (!hologramEffect.getName().equals(name)) continue;
            return hologramEffect;
        }
        return HologramEffect.DEFAULT;
    }

    public static KillMessage getKillMessage(String name) {
        for (KillMessage killMessage : killMessages) {
            if (!killMessage.getName().equals(name)) continue;
            return killMessage;
        }
        return KillMessage.DEFAULT;
    }

    public static void registerHologramEffect(HologramEffect hologramEffect) {
        hologramEffects.add(hologramEffect);
    }

    public static void registerHologramEffect(String name, String line, int price) {
        hologramEffects.add(new HologramEffect(name, line, price));
    }

    public static void registerKillMessage(KillMessage killMessage) {
        killMessages.add(killMessage);
    }

    public static void registerKillMessage(String name, String displayName, int price, Map<String, String> messages) {
        killMessages.add(new KillMessage(name, displayName, price, messages));
    }

    public static void generateHologram(GamePlayer killer, GamePlayer player, Location location) {
        HologramEffect hologramEffect = killer.getPlayerStats().getEffectStats().getHologramEffect();
        if (hologramEffect.equals(HologramEffect.DEFAULT)) {
            return;
        }
        final Hologram hologram = HologramsAPI.createHologram((Plugin) MegaWalls.getInstance(), (Location) location.add(0.0, 2.0, 0.0));
        hologram.appendTextLine(killer.getDisplayName(null));
        hologram.appendTextLine("\u00a7e" + hologramEffect.getLine());
        hologram.appendTextLine(player.getDisplayName(null));
        hologram.appendTextLine("\u00a77" + format.format(new Date()));
        new BukkitRunnable() {

            public void run() {
                hologram.delete();
            }
        }.runTaskLater((Plugin) MegaWalls.getInstance(), 200L);
    }

    public static List<HologramEffect> getHologramEffects() {
        return hologramEffects;
    }

    public static List<KillMessage> getKillMessages() {
        return killMessages;
    }
}

