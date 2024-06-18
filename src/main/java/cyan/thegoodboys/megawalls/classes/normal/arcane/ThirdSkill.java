/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.classes.normal.arcane;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThirdSkill extends Skill {
    public ThirdSkill(Classes classes) {
        super("奥数爆炸", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 3;
            }
        }
        return 0.14;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u88ab\u653b\u51fb\u65f6,\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u53d1\u52a8\u5965\u672f\u7206\u70b8");
            lore.add("   \u00a77\u5bf9\u4f60\u5468\u56f4\u7684\u654c\u4eba\u9020\u62101\u6b21\u4f24\u5bb3");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u88ab\u653b\u51fb\u65f6,\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u53d1\u52a8\u5965\u672f\u7206\u70b8");
        lore.add("   \u00a77\u5bf9\u4f60\u5468\u56f4\u7684\u654c\u4eba\u9020\u62101\u6b21\u4f24\u5bb3");
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
        int maxAttack = 5; // 受到5次攻击后爆炸
        Player player = gamePlayer.getPlayer();
        int attack = Arcane.skill3.getOrDefault(gamePlayer, maxAttack);
        if (attack == maxAttack) {
            Arcane.skill3.put(gamePlayer, 1);
            return true;
        }
        int i = attack + 1;
        Arcane.skill3.put(gamePlayer, i);
        if (i == maxAttack) {
            gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(), Sound.FIREWORK_BLAST2, 1.0f, 1.0f);
            for (Player player1 : this.getNearbyPlayers(gamePlayer.getPlayer(), 5)) { // 5x5范围内的敌人
                PlayerUtils.realDamage(player1, player, 2); // 对敌人造成2点伤害
                player1.setNoDamageTicks(0);
            }
            new BukkitRunnable() {
                public void run() {
                    Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).addEnergy(34, PlayerEnergyChangeEvent.ChangeReason.MAGIC); // 效果激活时恢复34点能量
                    cancel();
                }
            }.runTaskTimer(MegaWalls.getInstance(), 5L, 0);
        }

        return true;
    }

    public String getSkillTip(GamePlayer gamePlayer) {
        int skillLevel = gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)).getSkill3Level();
        int attack = Arcane.skill3.getOrDefault(gamePlayer, (int) this.getAttribute(skillLevel));
        return this.getClasses().getNameColor() + "\u00a7l" + this.getName().toUpperCase() + " " + (attack == 5 ? "\u00a7a\u00a7l\u2713" : "\u00a77" + attack + "/5");
    }

    private List<Player> getNearbyPlayers(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther))
                continue;
            players.add(other);
        }
        return players;
    }
}

