/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.skeleton;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("灵巧自如", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.33;
            }
            case 2: {
                return 6.67;
            }
            case 3: {
                return 10.0;
            }
        }
        return 3.33;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7射中敌人时,获得速度II效果");
            lore.add("   §7和生命恢复I效果。");
            lore.add("   §7只有速度有冷却时间。");
            lore.add(" ");
            lore.add("§7冷却时间:§a14秒");
            return lore;
        }
        lore.add(" ");
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
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        if (Skeleton.speedCooldown.getOrDefault(gamePlayer, 0) == 0) {
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
            gamePlayer.getPlayer().setFoodLevel(gamePlayer.getPlayer().getFoodLevel() + 10);
            Skeleton.speedCooldown.put(gamePlayer, 14);
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Skeleton.speedCooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§c§l" + Skeleton.speedCooldown.get(gamePlayer) + "秒");
    }
}

