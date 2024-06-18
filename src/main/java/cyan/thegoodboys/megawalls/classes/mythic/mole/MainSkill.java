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
package cyan.thegoodboys.megawalls.classes.mythic.mole;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.timer.OneTickTimer;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("挖挖挖", classes);
    }

    public static void shoot(final Player player) {
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                i++;
                if (i == 4) {
                    cancel();
                }
            }
        }.runTaskTimer((Plugin) MegaWalls.getInstance(), 0, 7);
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
            lore.add(" §8▪ §7向前冲刺最多§a" + this.getAttribute(level) + "§7格方块,");
            lore.add("   §7能打碎所有你前进方向的所有方块。");
            lore.add("   §7所有被该技能击中的敌人将");
            lore.add("   §7受到§a" + this.getAttribute(level) + "§4点伤害。");
            return lore;
        }
        lore.add(" §8▪ §7向前冲刺最多§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§7格方块,");
        lore.add("   §7能打碎所有你前进方向的所有方块。");
        lore.add("   §7所有被该技能击中的敌人将");
        lore.add("   §7受到§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§4点伤害。");
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
        if (Mole.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("§c你的技能冷却还有 §e" + Mole.skillCooldown.getOrDefault(gamePlayer, 0) + " 秒§!");
            return false;
        }
        player.setNoDamageTicks(12);
        shoot(player);
        OneTickTimer.noVelcityCheck.put(gamePlayer, 12);
        new BukkitRunnable() {
            final List<Player> damaged = new ArrayList<Player>();
            int seconds = 0;
            int ticks = 0;
            int blocksBroken = 0;

            public void run() {
                if (this.seconds == 12 || !gamePlayer.isOnline()) {
                    this.cancel();
                    return;
                }
                for (Block block1 : LocationUtils.getSphere(player.getLocation(), 3)) {
                    player.setVelocity(player.getEyeLocation().getDirection().multiply(0.7));
                    if (MegaWalls.getInstance().getGame().isProtected(block1.getLocation()) || MegaWalls.getInstance().getGame().isUnbreakable(block1.getLocation()) || (block1.getType() == Material.FURNACE || block1.getType() == Material.BURNING_FURNACE || block1.getType() == Material.TRAPPED_CHEST || block1.getType() == Material.BARRIER) || block1.getType() == Material.BEDROCK)
                        continue;
                    Collection<ItemStack> drops = block1.getDrops();
                    if (!drops.isEmpty()) {
                        ItemStack[] items = drops.toArray(new ItemStack[0]);
                        if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(items);
                        } else if (gamePlayer.getEnderChest().firstEmpty() != -1) {
                            gamePlayer.getEnderChest().addItem(items);
                        } else {
                            for (ItemStack item : items) {
                                gamePlayer.getPlayer().getWorld().dropItemNaturally(block1.getLocation(), item);
                            }
                        }
                    }
                    block1.setType(Material.AIR);
                    blocksBroken++;
                }
                for (Player nearby : MainSkill.this.getNearbyPlayers(player)) {
                    if (this.damaged.contains(nearby)) continue;
                    PlayerUtils.realDamage(nearby, player, 5);
                    this.damaged.add(nearby);
                }
                ++this.seconds;
                ++this.ticks;
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
        Mole.skillCooldown.put(gamePlayer, 8);
        Mole.hit.put(gamePlayer, 0);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Mole.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + Mole.skillCooldown.get(gamePlayer) + "秒");
    }

    private List<Player> getNearbyPlayers(Player player) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, 5)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

