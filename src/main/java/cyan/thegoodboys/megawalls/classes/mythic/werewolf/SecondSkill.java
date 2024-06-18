/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.werewolf;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("嗜血", classes);
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
                return 6.0;
            }
            case 3: {
                return 8.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u8fde\u7eed3\u6b21\u8fd1\u6218\u653b\u51fb\u6216Arrow\u77e2\u547d\u4e2d,");
            lore.add("   \u00a77\u800c\u81ea\u8eab\u672a\u53d7\u5230\u4f24\u5bb3\u65f6,\u5c06\u83b7\u5f97\u6301\u7eed");
            lore.add("   \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6I\u548c\u6297\u6027\u63d0\u5347II\u6548\u679c\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u8fde\u7eed3\u6b21\u8fd1\u6218\u653b\u51fb\u6216Arrow\u77e2\u547d\u4e2d,");
        lore.add("   \u00a77\u800c\u81ea\u8eab\u672a\u53d7\u5230\u4f24\u5bb3\u65f6,\u5c06\u83b7\u5f97\u6301\u7eed");
        lore.add("   \u00a78" + this.getAttribute(level - 1) + " \u279c");
        lore.add("   \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2\u7684\u901f\u5ea6I\u548c\u6297\u6027\u63d0\u5347II\u6548\u679c\u3002");
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
        long lastDamageTime = Werewolf.lastDamageTime.getOrDefault(gamePlayer, 0);
        if (System.currentTimeMillis() - lastDamageTime < 1000) {
            return false;
        }

        Werewolf.skill2.put(gamePlayer, Werewolf.skill2.getOrDefault(gamePlayer, 0) + 1);
        if (Werewolf.skill2.get(gamePlayer) >= 3) {
            gamePlayer.sendMessage("§e你的嗜血技能让你获得" + this.getAttribute(this.getPlayerLevel(gamePlayer)) + "秒的速度I和抗性提升I效果。");
            Player player = gamePlayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
            if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) this.getAttribute(this.getPlayerLevel(gamePlayer)) * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) this.getAttribute(this.getPlayerLevel(gamePlayer)) * 20, 0));

            Werewolf.skill2.put(gamePlayer, 0);
        }
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        int damage = Werewolf.skill2.getOrDefault(gamePlayer, 0);
        if (damage == 0) {
            return null;
        }
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + damage;
    }
}

