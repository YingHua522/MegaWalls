/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes.normal.shaman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.ShamanWolf;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("狼群", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        return 0.0;
    }

    public int getSeconds(int level) {
        switch (level) {
            case 1: {
                return 3;
            }
            case 2: {
                return 4;
            }
            case 3: {
                return 5;
            }
        }
        return 3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" \u00a78\u25aa \u00a77\u653b\u51fb\u4e00\u4f4d\u73a9\u5bb6\u65f6,\u670917%\u7684\u51e0\u7387\u83b7\u5f97");
        lore.add("   \u00a7a" + this.getSeconds(level) + "\u00a77\u79d2\u7684\u901f\u5ea6II\u6548\u679c\u5e76\u7ed9\u654c\u65b9\u865a\u5f31I\u6548\u679c\u3002");
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
        int attackCount = Shaman.attackCount.getOrDefault(gamePlayer, 0) + 1;
        if (attackCount >= 6) {
            final ShamanWolf shamanWolf = new ShamanWolf(((CraftWorld) Bukkit.getWorld("world")).getHandle());
            shamanWolf.setGamePlayer(gamePlayer);
            shamanWolf.getBukkitEntity().setMetadata("damage", new FixedMetadataValue(MegaWalls.getInstance(), 0.65));
            EntityTypes.spawnEntity(shamanWolf, gamePlayer.getPlayer().getLocation());
            List<ShamanWolf> wolves = Shaman.wolves.getOrDefault(gamePlayer, new ArrayList<>());
            if (wolves.size() >= 3) {
                ShamanWolf oldestWolf = wolves.get(0);
                oldestWolf.getBukkitEntity().remove();
                wolves.remove(0);
            }
            wolves.add(shamanWolf);
            Shaman.wolves.put(gamePlayer, wolves);
            attackCount = 0;
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), shamanWolf.getBukkitEntity()::remove, 12 * 20);
        }
        Shaman.attackCount.put(gamePlayer, attackCount);
        return true;
    }
}

