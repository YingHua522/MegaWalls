/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.shark;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.classes.normal.squid.Squid;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MainSkill
        extends Skill {
    private Map<Location, Player> blocks = new HashMap<Location, Player>();

    public MainSkill(Classes classes) {
        super("深海来者", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.3667;
            }
            case 2: {
                return 0.45;
            }
            case 3: {
                return 0.5333;
            }
            case 4: {
                return 0.6167;
            }
            case 5: {
                return 0.7;
            }
        }
        return 0.3667;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77生成一个7x7的水池");
            lore.add("   \u00a77\u5bf93获得8秒生命恢复效果");
            return lore;
        }
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
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player1 = gamePlayer.getPlayer();
        Location player = player1.getLocation().clone();
        this.setBlock(this.getLocation(0, 0, player), player1);
        this.setBlock(this.getLocation(0, 1, player), player1);
        this.setBlock(this.getLocation(0, 2, player), player1);
        this.setBlock(this.getLocation(0, -1, player), player1);
        this.setBlock(this.getLocation(0, -2, player), player1);
        this.setBlock(this.getLocation(1, 0, player), player1);
        this.setBlock(this.getLocation(1, 1, player), player1);
        this.setBlock(this.getLocation(1, 2, player), player1);
        this.setBlock(this.getLocation(1, -1, player), player1);
        this.setBlock(this.getLocation(1, -2, player), player1);
        this.setBlock(this.getLocation(2, 0, player), player1);
        this.setBlock(this.getLocation(2, 1, player), player1);
        this.setBlock(this.getLocation(2, 2, player), player1);
        this.setBlock(this.getLocation(2, -1, player), player1);
        this.setBlock(this.getLocation(2, -2, player), player1);
        this.setBlock(this.getLocation(-1, 0, player), player1);
        this.setBlock(this.getLocation(-1, 1, player), player1);
        this.setBlock(this.getLocation(-1, 2, player), player1);
        this.setBlock(this.getLocation(-1, -1, player), player1);
        this.setBlock(this.getLocation(-1, -2, player), player1);
        this.setBlock(this.getLocation(-2, 0, player), player1);
        this.setBlock(this.getLocation(-2, 1, player), player1);
        this.setBlock(this.getLocation(-2, 2, player), player1);
        this.setBlock(this.getLocation(-2, -1, player), player1);
        this.setBlock(this.getLocation(-2, -2, player), player1);
        if (player1.hasPotionEffect(PotionEffectType.SPEED)) {
            player1.removePotionEffect(PotionEffectType.SPEED);
        }
        player1.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
        if (player1.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            player1.removePotionEffect(PotionEffectType.FAST_DIGGING);
        }
        player1.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, 0));
        if (player1.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player1.removePotionEffect(PotionEffectType.REGENERATION);
        }
        player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 0));
        for (Player player2 : getNearbyPlayers(gamePlayer.getPlayer())) {
            PlayerUtils.realDamage(player2, player1, 2);
        }
        List<Player> enchantedPlayers = new ArrayList<>();
        new BukkitRunnable() {
            int seconds = 0;

            public void run() {
                if (this.seconds == 7) {
                    this.cancel();
                    for (Player player : new ArrayList<>(enchantedPlayers)) {
                        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                        if (gamePlayer != null && player.getInventory().getBoots() != null && player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER)) {
                            player.getInventory().getBoots().removeEnchantment(Enchantment.DEPTH_STRIDER);
                            enchantedPlayers.remove(player);
                        }
                    }
                    return;
                }
                for (Player team : getTeammates(gamePlayer.getPlayer())) {
                    GamePlayer gameTeam = GamePlayer.get(team.getUniqueId());
                    if (gameTeam != null && team.getInventory().getBoots() != null && !(ClassesManager.getSelected(gameTeam) instanceof Shark || ClassesManager.getSelected(gameTeam) instanceof Squid)) {
                        team.getInventory().getBoots().addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 2);
                        if (!enchantedPlayers.contains(team)) {
                            enchantedPlayers.add(team);
                        }
                    }
                }
                ++this.seconds;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 20L);
        return true;
    }


    private List<Player> getNearbyPlayers(Player player) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, 5)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(player.getLocation()) > (double) 5))
                continue;
            players.add(other);
        }
        return players;
    }

    private void setBlock(Block block, Player player) {
        if (block.getType() == Material.AIR || block.getType() == Material.WATER) {
            if (!this.blocks.containsKey(block.getLocation())) {
                this.blocks.put(block.getLocation(), player);
            }
            block.setType(Material.WATER);
            block.setMetadata(MegaWalls.getInstance().getName(), new FixedMetadataValue(MegaWalls.getInstance(), true));
            block.setMetadata("SharkWater", new FixedMetadataValue(MegaWalls.getInstance(), true));
            new BukkitRunnable() {
                public void run() {
                    block.setType(Material.AIR);
                    this.cancel();

                }
            }.runTaskTimer(MegaWalls.getInstance(), 140L, 0);
        }
    }

    private Block getLocation(int x, int z, Location player) {
        return player.getBlock().getRelative(x, 0, z);
    }

    private List<Player> getTeammates(Player player) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, 5)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || !Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

