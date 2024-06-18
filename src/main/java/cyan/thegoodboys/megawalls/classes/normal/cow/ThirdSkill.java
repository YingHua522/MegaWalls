/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.cow;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThirdSkill
        extends Skill {
    public ThirdSkill(Classes classes) {
        super("提神一抿", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 5.0;
            }
            case 2: {
                return 7.5;
            }
            case 3: {
                return 5.0;
            }
        }
        return 5.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u559d\u725b\u5976\u5c06\u4f1a\u7ed9\u4e88\u4f60\u548c\u5468\u56f4\u961f\u53cb");
            lore.add("   \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a7a" + this.getAttribute(level) + "\u00a77\u79d2,");
            lore.add("   \u00a77\u540c\u65f6\u8865\u5145\u9965\u997f\u5ea6\u548c\u9971\u98df\u5ea6\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u559d\u725b\u5976\u5c06\u4f1a\u7ed9\u4e88\u4f60\u548c\u5468\u56f4\u961f\u53cb");
        lore.add("   \u00a77\u751f\u547d\u6062\u590dI\u6548\u679c,\u6301\u7eed\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u79d2,");
        lore.add("   \u00a77\u540c\u65f6\u8865\u5145\u9965\u997f\u5ea6\u548c\u9971\u98df\u5ea6\u3002");
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
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (this.getAttribute(kitStats.getSkill3Level()) * 20.0), 0));
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(kitStats.getSkill3Level()) * 20.0), 1));
        player.setFoodLevel(20);
        for (Player teammate : this.getTeammates(player)) {
            if (teammate.hasPotionEffect(PotionEffectType.REGENERATION)) {
                teammate.removePotionEffect(PotionEffectType.REGENERATION);
            }
            teammate.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (int) (this.getAttribute(kitStats.getSkill3Level()) * 20.0), 2));
            teammate.setFoodLevel(20);
        }
        player.playSound(player.getLocation(), Sound.COW_IDLE, 1.0f, 1.0f);
        return true;
    }

    private List<Player> getTeammates(Player player) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, 5)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null) {
                Classes classes = ClassesManager.getSelected(gameOther);
                if (gameOther.isSpectator() || !Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther) || classes instanceof Cow)
                    continue;
                players.add(other);
            }
        }
        return players;
    }
}

