/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LightningStrike
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.novice.him;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.CustomZombie;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainSkill extends Skill {
    public MainSkill(Classes classes) {
        super("雷神之怒", classes);
    }

    @Override
    public int maxedLevel() {
        return 5;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 3.2;
            }
            case 2: {
                return 3.7;
            }
            case 3: {
                return 4.1;
            }
            case 4: {
                return 4.5;
            }
            case 5: {
                return 5.0;
            }
        }
        return 3.2;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" \u00a78\u25aa \u00a77\u91ca\u653e\u96f7\u795e\u4e4b\u6012,");
        if (level == 1) {
            lore.add("   \u00a77\u5bf9\u9644\u8fd1\u7684\u654c\u4eba\u9020\u6210\u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
            lore.add(" ");
            lore.add("\u00a77\u51b7\u5374\u65f6\u95f4:\u00a7a1\u79d2");
            return lore;
        }
        lore.add("   \u00a77\u5bf9\u9644\u8fd1\u7684\u654c\u4eba\u9020\u6210\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u70b9\u4f24\u5bb3\u3002");
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
        final Player player = gamePlayer.getPlayer();
        if (HIM.skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
            player.sendMessage("§cYour ability is still cooldown for §e" + HIM.skillCooldown.getOrDefault(gamePlayer, 0) + " seconds§!");
            return false;
        }
        List<Player> players = this.getNearbyPlayers(player, 5);
        if (players.isEmpty()) {
            player.sendMessage("§c附近没有玩家!");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (Player other2 : players) {
            GamePlayer gameOther = GamePlayer.get(other2.getUniqueId());
            if (gamePlayer.getGameTeam() != null && gamePlayer.getGameTeam().isInTeam(gameOther)) continue;
            LightningStrike lightningStrike = other2.getWorld().strikeLightning(other2.getLocation());
            lightningStrike.setMetadata(MegaWalls.getMetadataValue(), new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
            other2.playSound(other2.getLocation(), Sound.ENDERMAN_DEATH, 1f, 0.5f);
            CustomZombie zombie = new CustomZombie(((CraftWorld) Bukkit.getWorld("world")).getHandle());
            EntityTypes.spawnEntity(zombie, other2.getLocation());
            zombie.getBukkitEntity().remove();
            Bukkit.getScheduler().runTaskAsynchronously(MegaWalls.getInstance(), () -> ParticleEffect.LAVA.display(0.0f, 0.0f, 0.0f, 1.0f, 5, other2.getLocation(), 10.0));
            for (Player player1 : players) {
                PlayerUtils.realDamage(player1, player, 4);
            }
            if (!other2.equals(player)) {
                sb.append(other2.getName()).append("§e, ");
            }
        }
        String text = sb.substring(0, sb.toString().length() - 4);
        gamePlayer.sendMessage("§e你的的雷神之怒击中了" + text + "§e！");
        HIM.skillCooldown.put(gamePlayer, 2);
        return true;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (HIM.skillCooldown.getOrDefault(gamePlayer, 0) == 0 ? (gamePlayer.getEnergy() == 100 ? "§a§l✓" : "§c§l✕") : "§c§l" + HIM.skillCooldown.get(gamePlayer) + "s");
    }

    private List<Player> getNearbyPlayers(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(player, radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }
}

