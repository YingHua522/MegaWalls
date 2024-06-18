/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.mythic.lawless;

import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("撕裂箭矢", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 4.0;
            }
            case 2: {
                return 5.0;
            }
            case 3: {
                return 6.0;
            }
            case 4: {
                return 7.0;
            }
            case 5: {
                return 8.0;
            }
        }
        return 4.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            ;
            lore.add("   \u00a77撕裂敌人身上的箭矢");
            lore.add("   \u00a77并造成\u00a7a" + this.getAttribute(level) + "\u00a77点伤害。");
            return lore;
        }
        lore.add("   \u00a77撕裂敌人身上的箭矢");
        lore.add("   \u00a77并造成\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77点伤害。");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkillLevel();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkillLevel();
    }

    @Override
    public boolean use(final GamePlayer gamePlayer, KitStatsContainer kitStats) {
        final Player player = gamePlayer.getPlayer();
        List<Player> players = this.getNearbyPlayers(player, 5);
        if (players.isEmpty()) {
            player.sendMessage("§c附近没有玩家!");
            return false;
        }
        for (Player player1 : this.getNearbyPlayers(player.getPlayer(), 5)) {
            GamePlayer gamePlayer1 = GamePlayer.get(player1.getUniqueId());
            assert gamePlayer1 != null;
            if (Lawless.getHitCount(gamePlayer, gamePlayer1) >= 1) {
                PlayerUtils.realDamage(player1, player, Lawless.getHitCount(gamePlayer, gamePlayer1) * 2);
                gamePlayer.addEnergy(gamePlayer1.getArrow() * 4, PlayerEnergyChangeEvent.ChangeReason.BOW);
                player.getInventory().addItem(new ItemStack(Material.ARROW, Lawless.getHitCount(gamePlayer, gamePlayer1)));
                Lawless.clearHitCount(gamePlayer, gamePlayer1);
                player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.0f, 1.0f);
            } else {
                player.sendMessage("§c附近没有玩家!");
                return false;
            }
        }

        return true;
    }

    private List<Player> getNearbyPlayers(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther.isSpectator() || GamePlayer.get(player.getUniqueId()).getGameTeam().isInTeam(gameOther))
                continue;
            players.add(other);
        }
        return players;
    }
}

