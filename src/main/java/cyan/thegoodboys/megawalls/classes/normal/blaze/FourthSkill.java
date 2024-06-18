/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.blaze;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourthSkill extends CollectSkill {
    public static Map<GamePlayer, Integer> digCount = new HashMap<>();

    public FourthSkill(Classes classes) {
        super("化石燃料", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.0;
            }
            case 2: {
                return 2.0;
            }
            case 3: {
                return 1.0;
            }
        }
        return 3.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u51e0\u7387\u83b7\u5f97");
            lore.add("    \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed10\u79d2\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u77ff\u65f6\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u51e0\u7387\u83b7\u5f97");
        lore.add("    \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed10\u79d2\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill4Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
    }

    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if ((e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE) && Blaze.skill4Cooldown.getOrDefault(gamePlayer, 0) == 0) {
            digCount.put(gamePlayer, digCount.getOrDefault(gamePlayer, 0) + 1);
            if (gamePlayer != null && digCount.get(gamePlayer) >= getAttribute(getPlayerLevel(gamePlayer))) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 2));
                ParticleEffect.FLAME.display(0.5f, 0.5f, 0.5f, 0.0f, 10, e.getBlock().getLocation().add(0.5, 0.5, 0.5), 5.0);
                Blaze.skill4Cooldown.put(gamePlayer, 10);
                digCount.put(gamePlayer, 0);
            }
        }
    }

    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Blaze.skill4Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§c§l" + Blaze.skill4Cooldown.get(gamePlayer) + "s");
    }
}

