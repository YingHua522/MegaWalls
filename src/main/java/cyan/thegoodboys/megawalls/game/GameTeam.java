/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.DamageSource
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Wither
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.config.FileConfig;
import cyan.thegoodboys.megawalls.effect.TeamDisplayColor;
import cyan.thegoodboys.megawalls.game.team.TeamColor;
import cyan.thegoodboys.megawalls.game.team.TeamWither;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class GameTeam {
    private final List<GamePlayer> gamePlayers = new ArrayList<>();
    private TeamColor teamColor = null;
    private Location spawnLocation = null;
    private Location witherLocation = null;
    private GameRegion region = null;
    private GameRegion castleRegion = null;
    private GameWall reserveWall = null;
    private GameWall clearLobby = null;
    private GameRegion teamRegion = null;
    private int maxPlayers = 0;
    private TeamWither teamWither = null;
    private boolean witherDead = false;
    private int witherDie = 100;


    public static GameTeam build(FileConfig config, TeamColor teamColor, CommandSender sender) {
        GameTeam gameTeam = null;

        try {
            gameTeam = new GameTeam();
            gameTeam.teamColor = teamColor;
            gameTeam.maxPlayers = MegaWalls.getInstance().getConfig().getInt("team-size", 20);
            gameTeam.spawnLocation = config.getLocation("teams." + teamColor.name() + ".spawn");
            gameTeam.witherLocation = config.getLocation("teams." + teamColor.name() + ".wither");
            gameTeam.region = new GameRegion(config.getBlockLocation("teams." + teamColor.name() + ".region.loc1"), config.getBlockLocation("teams." + teamColor.name() + ".region.loc2"));
            gameTeam.castleRegion = new GameRegion(config.getBlockLocation("teams." + teamColor.name() + ".castle.loc1"), config.getBlockLocation("teams." + teamColor.name() + ".castle.loc2"));
            gameTeam.reserveWall = new GameWall(config.getBlockLocation("teams." + teamColor.name() + ".reserve.loc1"), config.getBlockLocation("teams." + teamColor.name() + ".reserve.loc2"));
            gameTeam.clearLobby = new GameWall(config.getBlockLocation("lobby-clear.loc1"), config.getBlockLocation("lobby-clear.loc2"));
            gameTeam.teamRegion = new GameRegion(config.getBlockLocation("teams." + teamColor.name() + ".teamreserve.loc1"), config.getBlockLocation("teams." + teamColor.name() + ".teamreserve.loc2"));
        } catch (Exception var5) {
            sender.sendMessage("§c加载队伍时出错: §b" + var5.getMessage());
        }

        return gameTeam;
    }

    public void spawnWither() {
        if (this.teamWither == null) {
            if (!this.witherLocation.getChunk().isLoaded()) {
                this.witherLocation.getChunk().load();
            }
            this.teamWither = new TeamWither(((CraftWorld) Bukkit.getWorld("world")).getHandle());
            this.teamWither.setTeam(this);
            double maxHealth = MegaWalls.getInstance().getConfig().getDouble("wither-health", 500.0D);
            ((Wither) this.teamWither.getBukkitEntity()).setMaxHealth(maxHealth);
            ((Wither) this.teamWither.getBukkitEntity()).setHealth(maxHealth);
            EntityTypes.spawnEntity(this.teamWither, this.witherLocation);

            this.startWither();
            // 执行凋灵扣血的runnable
            Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new Runnable() {
                private int second = 0;

                public void run() {
                    if (GameTeam.this.teamWither != null && !GameTeam.this.isWitherDead()) {
                        if (this.second == 5) {
                            Game game = MegaWalls.getInstance().getGame();
                            if (game != null && GameTeam.this.teamWither.getHealth() >= 3.0F && game.isWallsFall() && !game.isWitherAlive()) {
                                GameTeam.this.teamWither.setHealth(GameTeam.this.teamWither.getHealth() - 2.0F);
                                for (GamePlayer gp : GamePlayer.getGamePlayers()) {
                                    if (gp != null && gp.getPlayer() != null && !CitizensAPI.getNPCRegistry().isNPC(gp.getPlayer())) {
                                        gp.getPlayer().playSound(GameTeam.this.teamWither.getGameTeam().getWitherLocation(), Sound.WITHER_HURT, 3, 0);
                                    }
                                }
                            } else if (game != null && GameTeam.this.teamWither.getHealth() >= 0.0F && game.isWallsFall() && game.isWitherAlive() && witherDie <= 0) {
                                GameTeam.this.teamWither.setHealth(GameTeam.this.teamWither.getHealth() - 4.0F);
                                for (GamePlayer gp : GamePlayer.getGamePlayers()) {
                                    if (gp != null && gp.getPlayer() != null && !CitizensAPI.getNPCRegistry().isNPC(gp.getPlayer())) {
                                        gp.getPlayer().playSound(GameTeam.this.teamWither.getGameTeam().getWitherLocation(), Sound.WITHER_HURT, 3, 0);
                                    }
                                }
                            }

                            if (MegaWalls.getInstance().getGame().isWitherAngry()) {
                                for (Player other : PlayerUtils.getNearbyPlayers(GameTeam.this.witherLocation, 10.0D)) {
                                    GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
                                    if (gameOther != null && !gameOther.isSpectator() && GameTeam.this.isInTeam(gameOther)) {
                                        other.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
                                        other.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                                        other.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 2));
                                    }
                                }
                            }

                            this.second = 0;
                        }

                        if (MegaWalls.getInstance().getGame().isWitherAngry()) {
                            GameTeam.this.teamWither.setCustomName(GameTeam.this.getTeamColor().getChatColor() + GameTeam.this.getTeamColor().getText() + "队凋灵 §l暴怒中");
                        } else {
                            GameTeam.this.teamWither.setCustomName(GameTeam.this.getTeamColor().getChatColor() + GameTeam.this.getTeamColor().getText() + "队凋灵");
                        }
                    }
                    ++this.second;
                }
            }, 0L, 20L);
        }
    }

    public void startWither() {
        Game game = MegaWalls.getInstance().getGame();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (witherDie <= 0) {
                    cancel();
                }
                if (game.isWitherAlive()) {
                    witherDie--;
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0, 20);
    }

    // 获取队伍的存活玩家
    public List<GamePlayer> getAlivePlayers() {
        List<GamePlayer> alivePlayers = new ArrayList<>();
        for (GamePlayer gamePlayer : this.gamePlayers) {
            if (gamePlayer.isOnline() && !gamePlayer.isSpectator()) {
                alivePlayers.add(gamePlayer);
            }
        }
        return alivePlayers;
    }

    // 获取队伍的存活玩家
    public boolean isInTeam(GamePlayer gamePlayer) {
        return this.gamePlayers.contains(gamePlayer);
    }

    // 添加玩家到队伍
    public boolean addPlayer(GamePlayer gamePlayer) {
        if (!this.isFull() && !this.isInTeam(gamePlayer)) {
            this.gamePlayers.add(gamePlayer);
            gamePlayer.setGameTeam(this);
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return this.gamePlayers.size() >= this.getMaxPlayers();
    }

    public boolean isDead() {
        return this.gamePlayers.stream().noneMatch(gamePlayer -> gamePlayer.isOnline() && !gamePlayer.isSpectator());
    }

    public void teleportToSpawn() {
        this.gamePlayers.stream().filter(GamePlayer::isOnline).forEach(gamePlayer -> gamePlayer.getPlayer().teleport(this.spawnLocation));
    }

    public List<GamePlayer> getGamePlayers() {
        return this.gamePlayers;
    }

    public TeamColor getTeamColor() {
        return this.teamColor;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public Location getWitherLocation() {
        return this.witherLocation;
    }

    public GameRegion getRegion() {
        return this.region;
    }

    public GameRegion getCastleRegion() {
        return this.castleRegion;
    }

    public GameRegion getTeamRegion() {
        return teamRegion;
    }

    public GameWall getReserveWall() {
        return this.reserveWall;
    }

    public GameWall getClearLobby() {
        return clearLobby;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public TeamWither getTeamWither() {
        return this.teamWither;
    }

    public boolean isWitherDead() {
        return this.witherDead;
    }

    public void setWitherDead(boolean witherDead) {
        this.witherDead = witherDead;
    }
}

