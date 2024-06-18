/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.normal.shaman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.ShamanWolf;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdSkill extends Skill {
    private Map<GamePlayer, List<ShamanWolf>> wolves = new HashMap<GamePlayer, List<ShamanWolf>>();

    public ThirdSkill(Classes classes) {
        super("英雄主义", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 8.0;
            }
            case 3: {
                return 12.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" \u00a78\u25aa \u00a77\u88ab\u653b\u51fb\u65f6,\u670910%\u7684\u51e0\u7387\u53ec\u5524");
        lore.add("   \u00a77\u4e00\u53ea\u4fdd\u62a4\u4f60\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u72fc\u3002");
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
    public boolean use(final GamePlayer gamePlayer, KitStatsContainer kitStats) {
        if (MegaWalls.getRandom().nextInt(100) <= 10) {
            List list = this.wolves.getOrDefault(gamePlayer, new ArrayList<>());
            if (list.size() >= 3) {
                return false;
            }
            this.wolves.put(gamePlayer, list);
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> ThirdSkill.this.wolves.put(gamePlayer, list), (long) this.getAttribute(this.getPlayerLevel(gamePlayer)) * 20L);
        }
        return true;
    }
}

