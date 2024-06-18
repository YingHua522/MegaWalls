/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutChat
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction
 *  net.minecraft.server.v1_8_R3.PlayerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Snowball
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.mythic.automaton.Automaton;
import cyan.thegoodboys.megawalls.effect.EffectManager;
import cyan.thegoodboys.megawalls.effect.KillMessage;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.scoreboard.SidebarBoard;
import cyan.thegoodboys.megawalls.spectator.SpectatorSettings;
import cyan.thegoodboys.megawalls.spectator.SpectatorTarget;
import cyan.thegoodboys.megawalls.stats.CurrencyPackage;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.stats.PlayerStats;
import cyan.thegoodboys.megawalls.timer.ScoreBoardTimer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GamePlayer {
    private static final PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
    private static final PotionEffect NIGHTVISION = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);
    private static final List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
    private final UUID uuid;
    private final String name;
    private final String displayName;
    private boolean isSpectator = false;
    private SpectatorTarget spectatorTarget;
    private GameTeam gameTeam;
    private boolean protect = false;
    private int coins = 0;
    private int kills = 0;
    private int akills = 0;
    private int finalKills = 0;
    private int arrow = 0;
    private int damage = 0;
    private int def = 0;
    private int finalaKills = 0;
    private int protectChallenge = 0;
    private PlayerStats playerStats;
    private List<Block> protectedBlock = new ArrayList<>();
    private Inventory enderChest = null;
    private AssistsMap assistsMap;
    private int energy = 0;
    private PlayerCompass playerCompass;
    private Player lastDamager;
    private long lastDamageTime;
    @Getter
    private ItemStack[] inventoryContents;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.name = this.getPlayer().getName();
        this.displayName = ChatColor.stripColor(this.getPlayer().getDisplayName());
        this.assistsMap = new AssistsMap(this);
        this.playerStats = new PlayerStats(this);
        this.playerCompass = new PlayerCompass(this);
    }

    public static GamePlayer create(UUID uuid) {
        GamePlayer gamePlayer = GamePlayer.get(uuid);
        if (gamePlayer != null) {
            return gamePlayer;
        }
        gamePlayer = new GamePlayer(uuid);
        gamePlayers.add(gamePlayer);
        return gamePlayer;
    }

    public static void remove(UUID uuid) {
        if (GamePlayer.get(uuid) != null) {
            gamePlayers.remove(GamePlayer.get(uuid));
        }
    }

    public static GamePlayer get(UUID uuid) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (!gamePlayer.getUuid().equals(uuid)) continue;
            return gamePlayer;
        }
        return null;
    }

    public void setInventoryContents(org.bukkit.inventory.ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    public static List<GamePlayer> getGamePlayers() {
        return new ArrayList<>(gamePlayers);
    }

    public static List<GamePlayer> getOnlinePlayers() {
        ArrayList<GamePlayer> onlinePlayers = new ArrayList<GamePlayer>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (!gamePlayer.isOnline()) continue;
            onlinePlayers.add(gamePlayer);
        }
        return onlinePlayers;
    }

    public static List<GamePlayer> getSpectators() {
        ArrayList<GamePlayer> spectators = new ArrayList<GamePlayer>();
        for (GamePlayer gamePlayer : gamePlayers) {
            if (!gamePlayer.isSpectator()) continue;
            spectators.add(gamePlayer);
        }
        return spectators;
    }

    public static List<GamePlayer> sortFinalKills() {
        ArrayList<GamePlayer> list = new ArrayList<GamePlayer>(GamePlayer.getOnlinePlayers());
        Collections.sort(list, new Comparator<GamePlayer>() {

            @Override
            public int compare(GamePlayer player1, GamePlayer player2) {
                return player2.getFinalKills() - player1.getFinalKills();
            }
        });
        return list;
    }

    public Player getLastDamager() {
        return this.lastDamager;
    }

    public void setLastDamager(Player lastDamager) {
        this.lastDamager = lastDamager;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public long getLastDamageTime() {
        return this.lastDamageTime;
    }

    public void addDamage(int damage1) {
        this.damage = this.damage + damage1;
    }

    public void addDef(int def) {
        this.def = this.def + def;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUuid());
    }

    public String getDisplayName(GamePlayer gamePlayer) {
        if (this.gameTeam != null && !this.isSpectator) {
            ChatColor chatColor;
            ChatColor chatColor2 = chatColor = gamePlayer == null ? null : gamePlayer.getPlayerStats().getEffectStats().getColor(this.gameTeam.getTeamColor()).getChatColor();
            if (chatColor == null) {
                chatColor = this.gameTeam.getTeamColor().getChatColor();
            }
            return chatColor + this.displayName;
        }
        return "§9" + this.displayName;
    }

    public boolean isOnline() {
        return this.getPlayer() != null && this.getPlayer().isOnline();
    }

    public void sendActionBar(String message) {
        if (!this.isOnline()) {
            return;
        }
        message = ChatColor.translateAlternateColorCodes((char) '&', (String) message);
        PlayerConnection connection = ((CraftPlayer) this.getPlayer()).getHandle().playerConnection;
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a((String) ("{\"text\": \"" + message + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, (byte) 2);
        connection.sendPacket((Packet) ppoc);
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        if (!this.isOnline()) {
            return;
        }
        PlayerConnection connection = ((CraftPlayer) this.getPlayer()).getHandle().playerConnection;
        if (title != null) {
            title = ChatColor.translateAlternateColorCodes((char) '&', (String) title);
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a((String) ("{\"text\": \"" + title + "\"}"));
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket((Packet) packetPlayOutTitle);
        }
        if (subTitle != null) {
            subTitle = ChatColor.translateAlternateColorCodes((char) '&', (String) subTitle);
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a((String) ("{\"text\": \"" + subTitle + "\"}"));
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket((Packet) packetPlayOutSubTitle);
        }
    }

    public void sendMessage(String message) {
        if (!this.isOnline()) {
            return;
        }
        message = ChatColor.translateAlternateColorCodes((char) '&', (String) message);
        this.getPlayer().sendMessage(message);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        if (!this.isOnline()) {
            return;
        }
        this.getPlayer().playSound(this.getPlayer().getLocation(), sound, volume, pitch);
    }

    public void removeSpectator() {
        this.isSpectator = false;
        final Player player = this.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public void toSpectator(String title, String subTitle) {
        this.isSpectator = true;
        this.spectatorTarget = new SpectatorTarget(this, null);
        final Player player = this.getPlayer();
        if (player.isDead()) {
            PlayerUtils.respawn(player);
        }
        player.spigot().setCollidesWithEntities(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFoodLevel(20);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFireTicks(0);
        player.setLevel(0);
        player.setExp(0.0f);
        for (Entity entity : player.getNearbyEntities(40.0, 40.0, 40.0)) {
            Creature livingEntity = null;
            if (!(entity instanceof Creature) || (livingEntity = (Creature) entity).getTarget() == null || !livingEntity.getTarget().equals(this.getPlayer()))
                continue;
            livingEntity.setTarget(null);
        }
        Bukkit.getScheduler().runTaskLater((Plugin) MegaWalls.getInstance(), new Runnable() {

            @Override
            public void run() {
                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setDisplayName("§a§lTeleporter §7(Right Click)").build());
                player.getInventory().setItem(4, new ItemBuilder(Material.DIODE).setDisplayName("§b§lSpectator Settings §7(Right Click)").build());
                player.getInventory().setItem(8, new ItemBuilder(Material.BED).setDisplayName("§c§lLeave Game§7(Right Click)").build());
                for (GamePlayer gamePlayer : GamePlayer.getSpectators()) {
                    if (!gamePlayer.isOnline()) {
                        return;
                    }
                    Player player1 = gamePlayer.getPlayer();
                    SidebarBoard b = SidebarBoard.of(MegaWalls.getInstance(), player1);
                    player1.setScoreboard(b.getScoreboard());
                    ScoreBoardTimer.scoreboards.put(gamePlayer, b);
                    MegaWalls.getInstance().getGame().registerScoreboardTeams(player1);
                }
            }
        }, 15L);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(INVISIBILITY);
        SpectatorSettings settings = SpectatorSettings.get(this);
        if (settings.getOption(SpectatorSettings.Option.NIGHTVISION)) {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
            player.addPotionEffect(NIGHTVISION);
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        this.sendTitle(title, subTitle, 0, 100, 0);
        if (this.gameTeam != null && !this.gameTeam.getAlivePlayers().isEmpty()) {
            this.spectatorTarget.setTarget(this.gameTeam.getAlivePlayers().get(0));
        }
    }

    public void addKills() {
        ++this.kills;
    }

    public void addArrow() {
        ++this.arrow;
    }

    public void noneArrow() {
        this.arrow = 0;
    }

    public void addFinalKills() {
        ++this.finalKills;
    }

    public void addaKills() {
        ++this.akills;
    }

    public void addFinalaKills() {
        ++this.finalaKills;
    }

    public void addCoins(int coins) {
        this.coins = coins;
    }

    public void addProtectChallenge() {
        ++this.protectChallenge;
        if (this.protectChallenge == 10) {
            RewardManager.addChallenge(this, 2, 1);
        }
    }

    public boolean isProtectedBlock(Block block) {
        return this.protectedBlock.contains(block);
    }

    public void addProtectedBlock(Block block) {
        this.protectedBlock.add(block);
    }

    public void removeProtectedBlock(Block block) {
        this.protectedBlock.remove(block);
    }

    public void createEnderChest() {
        if (this.enderChest == null) {
            int size = this.getPlayerStats().getKitStats(this.getPlayerStats().getSelected()).getEnderChest() * 9;
            this.enderChest = Bukkit.createInventory((InventoryHolder) this.getPlayer(), (int) size, (String) "Ender Chest");
        }
    }

    public void setLastDamage(GamePlayer damager, long time) {
        this.assistsMap.setLastDamage(damager, time);
    }

    public void handleKill(final GamePlayer gamePlayer, final Entity entity) {
        KillMessage killMessage = this.getPlayerStats().getEffectStats().getKillMessage();
        EntityDamageEvent lastDamage = entity.getLastDamageCause();
        String reason = "被击杀";
        if (lastDamage != null) {
            if (lastDamage.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                reason = killMessage.getMessage("Attack");
            } else if (lastDamage.getCause() == EntityDamageEvent.DamageCause.POISON) {
                reason = killMessage.getMessage("Poison");
            } else if (lastDamage.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && lastDamage.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                if (lastDamage.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
                    reason = killMessage.getMessage("Magic");
                } else if (lastDamage instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) lastDamage;
                    if (edbe.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) edbe.getDamager();
                        if (projectile instanceof Arrow) {
                            reason = killMessage.getMessage("Arrow");
                        } else if (projectile instanceof Snowball) {
                            reason = killMessage.getMessage("Snowball");
                        }
                    }
                }
            } else {
                reason = killMessage.getMessage("Explode");
            }
        }

        String finalReason = reason;
        if (gamePlayer.getGameTeam().isWitherDead()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                GamePlayer viewer = get(p.getUniqueId());
                if (viewer != null) {
                    viewer.sendMessage(gamePlayer.getDisplayName(viewer) + " §f" + finalReason + ",击杀者: " + this.getDisplayName(viewer));
                }
            }
        } else {
            PlayerUtils.getNearbyPlayers(this.getPlayer().getLocation(), 10.0D).forEach((p) -> {
                GamePlayer viewer = get(p.getUniqueId());
                if (viewer != null) {
                    viewer.sendMessage(gamePlayer.getDisplayName(viewer) + " §f" + finalReason + ",击杀者: " + this.getDisplayName(viewer));
                }
            });
        }

        final Game game = MegaWalls.getInstance().getGame();
        Bukkit.getPluginManager().callEvent(new PlayerKillEvent(game, this, gamePlayer));
        if (gamePlayer.getGameTeam().isWitherDead()) {
            EffectManager.generateHologram(this, gamePlayer, entity.getLocation());
            this.getPlayerStats().addFinalKills(1);
            this.getPlayerStats().giveCoins(new CurrencyPackage(15, "§b§l最终击杀"));
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                if (game.isProtected(entity.getLocation())) {
                    GamePlayer.this.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                    GamePlayer.this.addProtectChallenge();
                }

                GamePlayer.this.addFinalKills();
            }, 10L);
            KitStatsContainer kitStats = this.getPlayerStats().getKitStats(this.getPlayerStats().getSelected());
            kitStats.addFinalKills(1);
            kitStats.giveMasterPoints(1);
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                for (GamePlayer assistor : gamePlayer.getAssistsMap().getAssists(System.currentTimeMillis())) {
                    if (!assistor.equals(GamePlayer.this)) {
                        assistor.getPlayerStats().giveCoins(new CurrencyPackage(15, "§b§l最终击杀 §c§l助攻 §6击杀" + gamePlayer.getDisplayName(assistor)));
                        if (game.isProtected(entity.getLocation())) {
                            assistor.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                            assistor.addProtectChallenge();
                        }

                        assistor.addFinalaKills();
                        KitStatsContainer kitStats2 = assistor.getPlayerStats().getKitStats(assistor.getPlayerStats().getSelected());
                        kitStats2.giveMasterPoints(1);
                        kitStats2.addFinalAssists(1);
                    }
                }

            }, 20L);
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                gamePlayer.getPlayerStats().addGames();
                KitStatsContainer kitStats3 = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
                kitStats3.addGames();
                kitStats3.addPlayTime(System.currentTimeMillis() - MegaWalls.getInstance().getGame().getStartTime());
            }, 30L);
        } else {
            this.getPlayerStats().addKills(1);
            this.getPlayerStats().giveCoins(new CurrencyPackage(4, null));
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                if (game.isProtected(entity.getLocation())) {
                    GamePlayer.this.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                    GamePlayer.this.addProtectChallenge();
                }

                GamePlayer.this.addKills();
            }, 10L);
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                for (GamePlayer assistor : gamePlayer.getAssistsMap().getAssists(System.currentTimeMillis())) {
                    if (!assistor.equals(GamePlayer.this)) {
                        assistor.getPlayerStats().giveCoins(new CurrencyPackage(4, "§c§l助攻 §6击杀" + gamePlayer.getDisplayName(assistor)));
                        if (game.isProtected(entity.getLocation())) {
                            assistor.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                            assistor.addProtectChallenge();
                        }
                        assistor.addaKills();
                    }
                }

            }, 30L);
        }

        if (game.getGameType() == GameType.NORMAL) {
            Classes classes = ClassesManager.getSelected(this);
            if (this.getPlayerStats().getKitStats(classes).getLevel() >= 5) {
                this.getPlayerStats().giveCoins(new CurrencyPackage(1, "(精通IV " + classes.getDisplayName() + ")"));
            }
        }
    }

    public void addEnergy(int energy, PlayerEnergyChangeEvent.ChangeReason changeReason) {
        if (this.energy >= (ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(getPlayer().getUniqueId()))) instanceof Automaton ? 160 : 100) || !this.isOnline()) {
            return;
        }
        PlayerEnergyChangeEvent event = new PlayerEnergyChangeEvent(MegaWalls.getInstance().getGame(), this, changeReason, energy);
        Bukkit.getPluginManager().callEvent((Event) event);
        if (event.getAmount() > 0) {
            this.setEnergy(this.energy + event.getAmount());
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSpectator() {
        return this.isSpectator;
    }

    public SpectatorTarget getSpectatorTarget() {
        return this.spectatorTarget;
    }

    public void setSpectatorTarget(SpectatorTarget spectatorTarget) {
        this.spectatorTarget = spectatorTarget;
    }

    public GameTeam getGameTeam() {
        return this.gameTeam;
    }

    public void setGameTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public boolean isProtect() {
        return this.protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public int getKills() {
        return this.kills;
    }

    public int getArrow() {
        return this.arrow;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getDef() {
        return this.def;
    }

    public int getFinalKills() {
        return this.finalKills;
    }

    public int getaKills() {
        return this.akills;
    }

    public int getFinalaKills() {
        return this.finalaKills;
    }

    public int getProtectChallenge() {
        return this.protectChallenge;
    }

    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public List<Block> getProtectedBlock() {
        return this.protectedBlock;
    }

    public Inventory getEnderChest() {
        return this.enderChest;
    }

    public AssistsMap getAssistsMap() {
        return this.assistsMap;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        if (this.energy > (ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(getPlayer().getUniqueId()))) instanceof Automaton ? 160 : 100)) {
            this.energy = (ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(getPlayer().getUniqueId()))) instanceof Automaton ? 160 : 100);
        }
        if (!this.isOnline()) {
            return;
        }
        this.getPlayer().setLevel(this.energy);
        this.getPlayer().setExp((float) this.energy / 100.0f);
    }

    public PlayerCompass getPlayerCompass() {
        return this.playerCompass;
    }

    public static class PlayerCompass {
        private static final List<GameTeam> teams = new ArrayList<GameTeam>(MegaWalls.getInstance().getGame().getTeams());
        private final GamePlayer gamePlayer;
        private final Player player;
        private int index = 0;

        public PlayerCompass(GamePlayer gamePlayer) {
            this.gamePlayer = gamePlayer;
            this.player = gamePlayer.getPlayer();
        }

        public void previous() {
            --this.index;
            if (this.index < 0) {
                this.index = teams.size() - 1;
            }
        }

        public void next() {
            ++this.index;
            if (this.index >= teams.size()) {
                this.index = 0;
            }
        }

        public void sendClosestPlayer() {
            GameTeam gameTeam = teams.get(this.index);
            Game game = MegaWalls.getInstance().getGame();
            Player closest = null;
            for (GamePlayer gamePlayer1 : gameTeam.getAlivePlayers()) {
                if (gamePlayer1.equals(this.gamePlayer)) {
                    continue;
                }
                Player player1 = gamePlayer1.getPlayer();
                if (gameTeam.isWitherDead() && !game.isDeathMatch() && gameTeam.getRegion().isInRegion(player1.getLocation()) || gameTeam.getReserveWall().isInWall(player1.getLocation())) {
                    continue;
                }
                if (closest != null && !(player1.getLocation().distance(this.player.getLocation()) < closest.getLocation().distance(this.player.getLocation()))) {
                    continue;
                }
                closest = player1;
            }
            EffectStatsContainer effectStats = this.gamePlayer.getPlayerStats().getEffectStats();
            StringBuilder sb = new StringBuilder();
            if (closest == null) {
                sb.append("§c§l没有");
                sb.append(effectStats.getColor(gameTeam.getTeamColor()).getChatColor()).append("§l").append(gameTeam.getTeamColor().getText()).append("队");
                sb.append("§c§l玩家可供追踪！");
            } else {
                int distance = (int) closest.getLocation().distance(this.player.getLocation());
                sb.append("§f追踪: ").append(effectStats.getColor(gameTeam.getTeamColor()).getChatColor()).append("§l").append(gameTeam.getTeamColor().getText());
                sb.append(" §f- 最近的玩家: ").append(effectStats.getColor(gameTeam.getTeamColor()).getChatColor()).append(distance).append("§lm");
                this.player.setCompassTarget(closest.getLocation());
            }

            this.gamePlayer.sendActionBar(sb.toString());
        }

        public GamePlayer getGamePlayer() {
            return this.gamePlayer;
        }

        public Player getPlayer() {
            return this.player;
        }
    }
}

