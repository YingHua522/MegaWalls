/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.normal.cow;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.CustomMick;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {

    public SecondSkill(Classes classes) {
        super("牛奶屏障", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.25;
            }
            case 2: {
                return 0.375;
            }
            case 3: {
                return 0.5;
            }
        }
        return 0.25;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u8840\u91cf\u4f4e\u4e8e20\u7684\u65f6\u5019,");
            lore.add("   \u00a77\u4f60\u5934\u9876\u4f1a\u4e0d\u65ad\u6389\u51fa\u5976\u6876,\u6301\u7eed20\u79d2");
            lore.add("   \u00a77\u63a5\u4e0b\u67654\u6b21\u4efb\u4f55\u6765\u6e90\u7684\u4f24\u5bb3\u90fd\u5c06\u88ab\u51cf\u5c11\u00a7a" + StringUtils.percent(this.getAttribute(level)));
            lore.add("   \u00a77\u6bcf\u6b21\u51cf\u4f24\u4f60\u989d\u5916\u6062\u590d2\u8840\u91cf\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u8840\u91cf\u4f4e\u4e8e20\u7684\u65f6\u5019,");
        lore.add("   \u00a77\u4f60\u5934\u9876\u4f1a\u4e0d\u65ad\u6389\u51fa\u5976\u6876,\u6301\u7eed20\u79d2");
        lore.add("   \u00a77\u63a5\u4e0b\u67654\u6b21\u4efb\u4f55\u6765\u6e90\u7684\u4f24\u5bb3\u90fd\u5c06\u88ab\u51cf\u5c11\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)));
        lore.add("   \u00a77\u6bcf\u6b21\u51cf\u4f24\u4f60\u989d\u5916\u6062\u590d2\u8840\u91cf\u3002");
        lore.add(" ");
        lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a40\u79d2");
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
        if (Cow.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
            return false;
        }
        final Player player = gamePlayer.getPlayer();
        new BukkitRunnable() {
            private int ticks = 0;

            public void run() {
                if (!player.isOnline() || this.ticks >= 400) {
                    List<ArmorStand> standsForPlayer = Cow.playerArmorStands.get(player);
                    if (standsForPlayer != null) {
                        for (ArmorStand armorStand : standsForPlayer) {
                            armorStand.remove();
                            standsForPlayer.remove(armorStand);
                        }
                    }
                    this.cancel();
                    return;
                }
                this.ticks += 5;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 5L);
        CustomMick.activateMilkBarrier(gamePlayer.getPlayer());
        Cow.skill2Cooldown.put(gamePlayer, 40);
        Cow.skill2Damage.put(gamePlayer, 0);
        return true;
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (Cow.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "\u00a7a\u00a7l\u2713" : "\u00a7c\u00a7l" + Cow.skill2Cooldown.get(gamePlayer) + "\u79d2");
    }
}

