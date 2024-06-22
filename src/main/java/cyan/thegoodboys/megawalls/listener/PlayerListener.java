/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.Chest
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.CraftItemEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerAchievementAwardedEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerExpChangeEvent
 *  org.bukkit.event.player.PlayerGameModeChangeEvent
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerToggleFlightEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerGameJoinEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.mythic.automaton.Automaton;
import cyan.thegoodboys.megawalls.classes.mythic.lawless.Lawless;
import cyan.thegoodboys.megawalls.classes.mythic.mole.Mole;
import cyan.thegoodboys.megawalls.classes.mythic.phoenix.Phoenix;
import cyan.thegoodboys.megawalls.classes.mythic.snowman.Snowman;
import cyan.thegoodboys.megawalls.game.*;
import cyan.thegoodboys.megawalls.inv.opener.SQL;
import cyan.thegoodboys.megawalls.inventory.InventoryManager;
import cyan.thegoodboys.megawalls.spectator.SpectatorSettings;
import cyan.thegoodboys.megawalls.stats.CurrencyPackage;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.ItemUtils;
import cyan.thegoodboys.megawalls.util.LuckPremsUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener extends BaseListener {
    private final Game game;
    private List<Material> craft = new ArrayList<Material>();

    public PlayerListener(MegaWalls plugin) {
        super(plugin);
        this.game = plugin.getGame();
        this.craft.add(Material.FISHING_ROD);
        this.craft.add(Material.BUCKET);
        this.craft.add(Material.MINECART);
        this.craft.add(Material.BOAT);
    }

    public static HashSet<Material> getTransparentBlocks() {
        HashSet<Material> transparentBlocks = new HashSet<>();
        Collections.addAll(transparentBlocks, Material.values());
        return transparentBlocks;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAdvancementAwarded(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (this.game == null && !e.getPlayer().isOp()) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§c未发现游戏实例,不能进入该服务器！");
            return;
        }
        if (this.game != null && Bukkit.getOnlinePlayers().size() >= this.game.getMaxPlayers()) {
            e.setResult(PlayerLoginEvent.Result.KICK_FULL);
            e.setKickMessage("§c该房间人数已满！");
        }
        /*
        if (this.game != null && game.isStarted()) {
            e.setResult(PlayerLoginEvent.Result.KICK_FULL);
            e.setKickMessage("§c游戏已经开始！");
        }

         */
    }

    @EventHandler
    public void onPlayerDeaths(PlayerDeathEvent event) {
        // 取消击杀消息的发送
        event.setDeathMessage(null);
        if (game.isWaiting() || !game.isStarted()) {
            // 取消物品掉落
            event.setKeepInventory(true);
            event.setDroppedExp(0);
            event.setNewExp(0);
            event.setNewTotalExp(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        SQL.Register();
        Bukkit.getPluginManager().callEvent(new PlayerGameJoinEvent(game));
        final Player player = e.getPlayer();
        final GamePlayer gamePlayer = GamePlayer.create(player.getUniqueId());
        gamePlayer.getPlayerStats().update();
        if (this.game.isWaiting()) {
            e.setJoinMessage(LuckPremsUtils.getPrefix(e.getPlayer()) + gamePlayer.getPlayer().getDisplayName() + " §e加入了游戏！ (§b" + MegaWalls.getIngame().size() + "§e/§b" + this.game.getMaxPlayers() + "§e)!");
            if (this.game.getLobbyLocation() != null) {
                e.getPlayer().teleport(this.game.getLobbyLocation());
            }
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getActivePotionEffects().clear();
            player.setMaxHealth(20.0);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setLevel(0);
            player.setExp(0.0f);
            player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setDisplayName("§a职业选择器 §7(右键点击)").build());
            if (player.hasPermission("MegaWalls.forcestart")) {
                player.getInventory().setItem(7, new ItemBuilder(Material.DIAMOND).setDisplayName("§a开始游戏 §7(右键点击)").build());
            }
            //player.getInventory().setItem(1, new ItemBuilder(Material.PAPER).setDisplayName("§a更新日志 §7(右键点击)").build());
            player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setDisplayName("§a皮肤选择器 §7(右键点击)").build());
            player.getInventory().setItem(8, new ItemBuilder(Material.BED).setDisplayName("§c§l返回大厅 §7(右键点击)").build());
            PlayerListener.this.game.registerScoreboardTeams();
        } else if (this.game.isStarted()) {
            FakePlayer fakePlayer = FakePlayer.getFakePlayerMap().get(gamePlayer);
            if (gamePlayer.isSpectator() || gamePlayer.getGameTeam() == null || (fakePlayer != null && fakePlayer.isKilled() && gamePlayer.getGameTeam().getTeamWither().getBukkitEntity().isDead())) {
                gamePlayer.toSpectator(null, null);
                PlayerUtils.refresh(player);
                if (this.game.getLobbyLocation() != null) {
                    e.getPlayer().teleport(this.game.getLobbyLocation());
                }
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), PlayerListener.this.game::registerScoreboardTeams, 30L);
                if (FakePlayer.getList().contains(gamePlayer)) {
                    FakePlayer.getList().remove(gamePlayer);
                    MegaWalls.updateRejoin(gamePlayer, "超级战墙", System.currentTimeMillis());
                }
                return;
            }
            if (fakePlayer != null && (fakePlayer.isKilled() || !fakePlayer.isKilled() && !gamePlayer.getGameTeam().getTeamWither().getBukkitEntity().isDead())) {
                GamePlayer.getOnlinePlayers().forEach(viewer -> viewer.sendMessage(gamePlayer.getDisplayName(viewer) + " §7已重连加入服务器。"));
                player.teleport(fakePlayer.getLocation().clone());
                fakePlayer.delete();
                PlayerUtils.refresh(player);
                e.getPlayer().setHealth(gamePlayer.getPlayer().getHealth());
                e.getPlayer().getInventory().setContents(gamePlayer.getInventoryContents());
                new BukkitRunnable() {
                    int ticks = 0;

                    public void run() {
                        if (this.ticks == 5 || !player.isOnline()) {
                            this.cancel();
                            return;
                        }
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.hidePlayer(player);
                            player.hidePlayer(p);
                        }
                        ++this.ticks;
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
                PlayerUtils.respawn(player);
                player.setPlayerListName(gamePlayer.getPlayer().getDisplayName());
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), PlayerListener.this.game::registerScoreboardTeams, 30L);
                if (FakePlayer.getList().contains(gamePlayer)) {
                    FakePlayer.getList().remove(gamePlayer);
                    MegaWalls.updateRejoin(gamePlayer, "超级战墙", System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        e.setQuitMessage(null);
        if (this.game.isWaiting()) {
            if (this.game.getPlayerParty(gamePlayer) != null) {
                this.game.getPlayerParty(gamePlayer).removePlayer(gamePlayer);
            }
            if (gamePlayer != null) {
                e.setQuitMessage(LuckPremsUtils.getPrefix(e.getPlayer()) + gamePlayer.getPlayer().getDisplayName() + " §e离开了游戏！");
            }
            GamePlayer.remove(e.getPlayer().getUniqueId());
        } else if (this.game.isStarted() && !Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).isSpectator()) {
            new FakePlayer(gamePlayer);
            PlayerListener.this.game.registerScoreboardTeams();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent e) {
        e.setLeaveMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (this.game.isStarted() && this.game.getCenterArea().isInRegion(e.getTo()) && !this.game.isWallsFall()) {
            e.setCancelled(true);
            e.getPlayer().teleport(e.getFrom());
            e.getPlayer().sendMessage("§c§l你不可以往这个方向前进！");
        }
        GamePlayer gp = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gp != null && gp.getGameTeam() != null && !gp.getGameTeam().isWitherDead() && gp.getGameTeam().getTeamRegion().isInRegion(e.getTo())) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1));
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (damager instanceof Player && damaged instanceof Player) {
            Player damagerPlayer = (Player) damager;
            Player damagedPlayer = (Player) damaged;

            GamePlayer gameDamager = GamePlayer.get(damagerPlayer.getUniqueId());
            GamePlayer gameDamaged = GamePlayer.get(damagedPlayer.getUniqueId());

            if (gameDamager != null && gameDamaged != null) {
                gameDamaged.setLastDamager(damagerPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        final Player player = e.getEntity();
        final GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer != null && !this.game.isWaiting() && !gamePlayer.isSpectator()) {
            e.setDeathMessage(null);
            e.setKeepInventory(false);
            e.setKeepLevel(false);
            PlayerUtils.respawn(player);
            final GameTeam playerTeam = gamePlayer.getGameTeam();
            Player lastDamager = gamePlayer.getLastDamager();
            if (lastDamager != null && System.currentTimeMillis() - gamePlayer.getLastDamageTime() <= 60000) { // 修改为60秒
                GamePlayer killer = GamePlayer.get(lastDamager.getUniqueId());
                if (killer != null) {
                    killer.handleKill(gamePlayer, e.getEntity());
                }
            } else {
                Iterator var5;
                GamePlayer assistor;
                if (!gamePlayer.getGameTeam().isWitherDead()) {
                    for (var5 = gamePlayer.getAssistsMap().getAssists(System.currentTimeMillis()).iterator(); var5.hasNext(); assistor.addKills()) {
                        assistor = (GamePlayer) var5.next();
                        if (System.currentTimeMillis() - assistor.getLastDamageTime() <= 60000) { // 修改为60秒
                            assistor.getPlayerStats().giveCoins(new CurrencyPackage(4, "§c§l助攻 §6击杀" + gamePlayer.getDisplayName(assistor)));
                            if (this.game.isProtected(e.getEntity().getLocation())) {
                                assistor.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                                assistor.addProtectChallenge();
                            }
                        }
                    }
                } else {
                    var5 = gamePlayer.getAssistsMap().getAssists(System.currentTimeMillis()).iterator();
                    KitStatsContainer kitStats2;
                    while (var5.hasNext()) {
                        assistor = (GamePlayer) var5.next();
                        if (System.currentTimeMillis() - assistor.getLastDamageTime() <= 60000) { // 修改为60秒
                            assistor.getPlayerStats().giveCoins(new CurrencyPackage(15, "§b§l最终击杀 §c§l助攻"));
                            if (this.game.isProtected(e.getEntity().getLocation())) {
                                assistor.getPlayerStats().giveCoins(new CurrencyPackage(9, "(防守奖励)"));
                                assistor.addProtectChallenge();
                            }
                            assistor.addFinalaKills();
                            kitStats2 = assistor.getPlayerStats().getKitStats(assistor.getPlayerStats().getSelected());
                            kitStats2.giveMasterPoints(1);
                            kitStats2.addFinalAssists(1);
                        }
                    }
                    gamePlayer.getPlayerStats().addGames();
                    kitStats2 = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
                    kitStats2.addGames();
                    kitStats2.addPlayTime(MegaWalls.getInstance().getGame().getStartTime() - System.currentTimeMillis());
                }
            }
            if (playerTeam.isWitherDead()) {
                gamePlayer.toSpectator("§c§l你死了！", "§f你的凋灵已死，你不能重生 :(");
            } else {
                gamePlayer.setProtect(true);
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                    player.teleport(playerTeam.getSpawnLocation());
                    ClassesManager.giveItems(gamePlayer);
                    player.sendMessage("§a因为我方凋灵存活所以,§l你重生了");
                }, 2L);
                Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                    player.sendMessage("§c你的重生保护效果已消失！");
                    gamePlayer.setProtect(false);
                }, 200L);
            }
            if (playerTeam.isDead()) {
                GamePlayer.getOnlinePlayers().forEach((viewer) -> {
                    EffectStatsContainer effectStats = viewer.getPlayerStats().getEffectStats();
                    viewer.sendMessage(effectStats.getColor(playerTeam.getTeamColor()).getChatColor() + playerTeam.getTeamColor().getText() + "队§e§l已被团灭！");
                    viewer.sendTitle(effectStats.getColor(playerTeam.getTeamColor()).getChatColor() + playerTeam.getTeamColor().getText() + "队", "§f被淘汰了", 5, 40, 5);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().startsWith("%")) {
            e.setCancelled(true);
        }
        if (game == null) {
            e.setFormat("§7" + e.getPlayer().getDisplayName() + ": " + e.getMessage());
        }
        if (game != null && game.isStarted()) {
            GamePlayer gp = GamePlayer.get(e.getPlayer().getUniqueId());
            if (gp != null) {
                if (gp.isSpectator()) {
                    e.setFormat("§7[旁观者]" + (gp.getGameTeam() != null ? " " + gp.getGameTeam().getTeamColor().getChatPrefix() : "") + LuckPremsUtils.getPrefix(e.getPlayer()) + gp.getPlayer().getDisplayName() + "§f: " + e.getMessage());
                    e.getRecipients().removeIf(recipient -> !Objects.requireNonNull(GamePlayer.get(recipient.getUniqueId())).isSpectator());
                    return;
                }
                if (e.getMessage().startsWith("!")) {
                    e.setMessage(e.getMessage().replace("!", ""));
                    e.setFormat("§6[喊话] " + gp.getGameTeam().getTeamColor().getChatPrefix() + LuckPremsUtils.getPrefix(e.getPlayer()) + gp.getPlayer().getDisplayName() + ": " + e.getMessage());
                    return;
                }
                if (gp.getGameTeam().isInTeam(gp)) {
                    GameTeam team = gp.getGameTeam();
                    e.setFormat(team.getTeamColor().getChatColor() + team.getTeamColor().getChatPrefix() + LuckPremsUtils.getPrefix(e.getPlayer()) + gp.getPlayer().getDisplayName() + ":§f " + e.getMessage());
                    e.getRecipients().removeIf(recipient -> !Objects.requireNonNull(GamePlayer.get(recipient.getUniqueId())).getGameTeam().isInTeam(gp));
                }
            }
        } else if (game != null && !game.isStarted()) {
            GamePlayer gp = GamePlayer.get(e.getPlayer().getUniqueId());
            if (gp != null) {
                e.setFormat(LuckPremsUtils.getPrefix(e.getPlayer()) + gp.getPlayer().getDisplayName() + ": " + e.getMessage());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void noHungry(FoodLevelChangeEvent event) {
        if (!MegaWalls.getInstance().getGame().isWallsFall()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftItem(CraftItemEvent e) {
        if (this.craft.contains(e.getRecipe().getResult().getType())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && (gamePlayer.isSpectator() || !this.game.isStarted())) {
            e.setCancelled(true);
            return;
        }
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (ClassesManager.isClassesItem(itemStack)) {
            e.setCancelled(true);
            gamePlayer.getPlayer().sendMessage("§c无法将职业套装物品移出物品栏！");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && (gamePlayer.isSpectator() || !this.game.isStarted())) {
            e.setCancelled(true);
            return;
        }
        if (e.getItem() instanceof ExperienceOrb || e.getItem().hasMetadata("Arrow")) {
            e.setCancelled(true);
            e.getItem().remove();
            return;
        }
        ItemStack itemStack = e.getItem().getItemStack();
        Item item = e.getItem();
        if (ClassesManager.isClassesItem(itemStack)) {
            e.setCancelled(true);
            e.getItem().remove();
            return;
        }
        if ((itemStack.getType() == Material.POTION) && !ClassesManager.isClassesItem(itemStack)) {
            if (gamePlayer != null) {
                for (ItemStack playerItemStack : gamePlayer.getPlayer().getInventory().getContents()) {
                    if (playerItemStack == null || !(playerItemStack.getItemMeta() instanceof PotionMeta) || !itemStack.isSimilar(playerItemStack))
                        continue;
                    e.setCancelled(true);
                    item.remove();
                    playerItemStack.setAmount(playerItemStack.getAmount() + itemStack.getAmount());
                    break;
                }
            }
        }
        if ((itemStack.getType() == Material.MILK_BUCKET) && !ClassesManager.isClassesItem(itemStack)) {
            if (gamePlayer != null) {
                for (ItemStack playerItemStack : gamePlayer.getPlayer().getInventory().getContents()) {
                    if (playerItemStack == null || !itemStack.isSimilar(playerItemStack)) continue;
                    e.setCancelled(true);
                    item.remove();
                    playerItemStack.setAmount(playerItemStack.getAmount() + itemStack.getAmount());
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerExpChange(PlayerExpChangeEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && (gamePlayer.isSpectator() || this.game.isStarted())) {
            e.setAmount(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION) {
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> e.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (gamePlayer != null && gamePlayer.isSpectator()) {
                e.setCancelled(true);
                ItemStack item = player.getItemInHand();
                if (item == null || item.getType() == Material.AIR) {
                    return;
                }
                if (item.getType() == Material.BED) {
                    MegaWalls.getInstance().tpToLobby(player);
                    return;
                }
                if (item.getType() == Material.COMPASS) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        this.game.openSpectatorInventory(gamePlayer);
                        return;
                    }
                    if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && gamePlayer.getSpectatorTarget() != null) {
                        gamePlayer.getSpectatorTarget().tp();
                        return;
                    }
                } else if (item.getType() == Material.DIODE) {
                    InventoryManager.SPECTATORSETTINGS.open(player);
                    return;
                }
            } else if (this.game.isWaiting()) {
                ItemStack item = player.getItemInHand();
                if (item == null || item.getType() == Material.AIR) {
                    return;
                }
                if (e.getItem().getType() == Material.IRON_SWORD) {
                    e.setCancelled(true);
                    InventoryManager.CLASSESSELECTOR.open(player);
                    return;
                }
                if (e.getItem().getType() == Material.SKULL_ITEM) {
                    e.setCancelled(true);
                    if (gamePlayer != null) {
                        //皮肤
                        //SkinMenu.createSkinSelectionMenu(ClassesManager.getSelected(gamePlayer), InventoryManager.CLASSESSELECTOR, gamePlayer).open(player);
                    }
                }
                if (e.getItem().getType() == Material.EMERALD) {
                    e.setCancelled(true);
                    InventoryManager.SHOPMENU.open(player);
                    return;
                }
                if (e.getItem().getType() == Material.NETHER_STAR) {
                    e.setCancelled(true);
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        player1.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setDisplayName("§aClasses Selector §7(Right Click)").build());
                    }
                    return;
                }
                if (e.getItem().getType() == Material.BARRIER && player.hasPermission("MegaWalls.forcestart")) {
                    e.setCancelled(true);
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        Game game = MegaWalls.getInstance().getGame();
                        GameParty gameParty = game.getPlayerParty(GamePlayer.get(player1.getUniqueId()));
                        if (!gameParty.isLeader(Objects.requireNonNull(GamePlayer.get(player1.getUniqueId()))))
                            continue;
                        player1.getInventory().setItem(0, new ItemBuilder(Material.GOLD_SWORD).setDisplayName("§a禁用职业§7(Right Click)").build());
                    }
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        player1.getInventory().setItem(6, new ItemBuilder(Material.AIR).build());
                        player1.getInventory().setItem(5, new ItemBuilder(Material.AIR).build());
                    }
                    player.getInventory().setItem(5, new ItemBuilder(Material.NETHER_STAR).setDisplayName("§a开启职业选择§7(Right Click)").build());
                    return;
                }
                if (e.getItem().getType() == Material.DIAMOND_SWORD && player.hasPermission("MegaWalls.forcestart")) {
                    e.setCancelled(true);
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        player1.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setDisplayName("§aClasses Selector §7(Right Click)").build());
                        player1.getInventory().setItem(6, new ItemBuilder(Material.AIR).build());
                        player1.getInventory().setItem(5, new ItemBuilder(Material.AIR).build());
                    }
                    return;
                }
                if (e.getItem().getType() == Material.DIAMOND && player.hasPermission("MegaWalls.forcestart")) {
                    e.setCancelled(true);
                    this.game.forceStart();
                    return;
                }
                if (e.getItem().getType() == Material.REDSTONE_COMPARATOR && player.hasPermission("MegaWalls.forcestart")) {
                    e.setCancelled(true);
                    InventoryManager.GAMEMENU.open(player);
                    return;
                }
                if (e.getItem().getType() == Material.PAPER) {
                    e.setCancelled(true);
                    InventoryManager.MUSICMENU.open(player);
                    return;
                }
                if (item.getType() == Material.BED) {
                    e.setCancelled(true);
                    MegaWalls.getInstance().tpToLobby(player);
                    return;
                }
            } else if (this.game.isStarted()) {
                ItemStack item;
                if (!(this.game.isWallsFall() || e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null || e.getClickedBlock().getType() != Material.TRAPPED_CHEST && e.getClickedBlock().getType() != Material.FURNACE && e.getClickedBlock().getType() != Material.BURNING_FURNACE)) {
                    Block block = e.getClickedBlock();
                    if (this.game.getPlayerParty(gamePlayer) != null && this.game.getPlayerParty(gamePlayer).isProtectedBlock(block)) {
                        if (block.getType() == Material.TRAPPED_CHEST && block.getRelative(BlockFace.UP).getType() != null && block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                            player.openInventory(((Chest) block.getState()).getBlockInventory());
                            return;
                        }
                        return;
                    }
                    if (gamePlayer != null && !gamePlayer.isProtectedBlock(block)) {
                        e.setCancelled(true);
                        gamePlayer.sendMessage("§cYou can't use this " + (block.getType() == Material.TRAPPED_CHEST ? "Trapped Chest" : "Chest") + "！");
                        return;
                    }
                    if (block.getType() == Material.TRAPPED_CHEST && block.getRelative(BlockFace.UP).getType() != null && block.getRelative(BlockFace.UP).getType() != Material.AIR) {
                        player.openInventory(((Chest) block.getState()).getBlockInventory());
                        return;
                    }
                }
                if ((item = player.getItemInHand()) == null || item.getType() == Material.AIR) {
                    return;
                }
                if (e.getItem().getType() == Material.ENDER_CHEST) {
                    e.setCancelled(true);
                    if (gamePlayer != null) {
                        player.openInventory(gamePlayer.getEnderChest());
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT,2f,0.5f);
                        gamePlayer.playSound(Sound.CHEST_OPEN, 2f, 0.5f);
                    }
                    return;
                } else if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && ItemUtils.isSword(item)) {
                    Classes classes;
                    if (gamePlayer != null && gamePlayer.getEnergy() >= (ClassesManager.getSelected(gamePlayer) instanceof Snowman ? 60 : 100) && (classes = ClassesManager.getSelected(gamePlayer)).getMainSkill().use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes))) {
                        e.setCancelled(true);
                        gamePlayer.setEnergy((ClassesManager.getSelected(gamePlayer) instanceof Automaton ? gamePlayer.getEnergy() - 100 : ClassesManager.getSelected(gamePlayer) instanceof Snowman ? gamePlayer.getEnergy() - 60 : 0));
                        gamePlayer.sendActionBar(classes.getSkillTip(gamePlayer));
                        gamePlayer.sendMessage("§a你的 " + classes.getMainSkill().getName() + " 技能已就绪");
                        if (gamePlayer.getPlayerStats().getKitStats(classes).getLevel() >= 3) {
                            ClassesManager.playSkillEffect(player);
                        }
                    }
                } else if ((e.getAction() == Action.LEFT_CLICK_AIR) && e.getItem().getType() == Material.BOW) {
                    Classes classes;
                    if (gamePlayer != null && ((ClassesManager.getSelected(gamePlayer) instanceof Phoenix || (ClassesManager.getSelected(gamePlayer) instanceof Lawless)) || (ClassesManager.getSelected(gamePlayer) instanceof Snowman))) {
                        return;
                    }
                    if (gamePlayer != null && gamePlayer.getEnergy() >= 100 && (classes = ClassesManager.getSelected(gamePlayer)).getMainSkill().use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes))) {
                        e.setCancelled(true);
                        gamePlayer.setEnergy(ClassesManager.getSelected(gamePlayer) instanceof Automaton ? gamePlayer.getEnergy() - 100 : 0);
                        gamePlayer.sendActionBar(classes.getSkillTip(gamePlayer));
                        gamePlayer.sendMessage("§a你的 " + classes.getMainSkill().getName() + " 技能已就绪");
                        if (gamePlayer.getPlayerStats().getKitStats(classes).getLevel() >= 3) {
                            ClassesManager.playSkillEffect(player);
                        }
                    }
                }
                if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && ItemUtils.isSpade(item) && ClassesManager.getSelected(gamePlayer) instanceof Mole) {
                    Classes classes;
                    if (gamePlayer.getEnergy() == 100 && (classes = ClassesManager.getSelected(gamePlayer)).getMainSkill().use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes))) {
                        e.setCancelled(true);
                        gamePlayer.setEnergy(0);
                        gamePlayer.sendActionBar(classes.getSkillTip(gamePlayer));
                        gamePlayer.sendMessage("§aYour " + classes.getMainSkill().getName() + " Skills activated");
                        if (gamePlayer.getPlayerStats().getKitStats(classes).getLevel() >= 3) {
                            ClassesManager.playSkillEffect(player);
                        }
                    }
                }
                if (e.getItem().getType() == Material.COMPASS) {
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                        if (gamePlayer != null) {
                            gamePlayer.getPlayerCompass().next();
                        }
                        e.setCancelled(true);
                        return;
                    }
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
                        e.setCancelled(true);
                        if (gamePlayer != null) {
                            gamePlayer.getPlayerCompass().previous();
                        }
                        return;
                    }
                }
            }
        }
        if (gamePlayer != null && gamePlayer.isSpectator()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && (Objects.requireNonNull(GamePlayer.get(e.getEntity().getUniqueId())).isSpectator() || !this.game.isStarted())) {
            e.setCancelled(true);
        }
    }


//    @EventHandler
//    public void onPlayerInteracts(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        ItemStack item = event.getItem();
//
//        // 检查玩家是否左键点击并且手中是否持有弓
//        if (event.getAction() == Action.LEFT_CLICK_AIR && item != null && item.getType() == Material.BOW) {
//            // 发射三个凋零头颅
//            for (int i = 0; i < 3; i++) {
//                launchWitherSkull(player);
//            }
//        }
//    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        if (Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).isSpectator()) {
            e.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).isSpectator() || !this.game.isStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && (gamePlayer.isSpectator() || !this.game.isStarted())) {
            if (CitizensAPI.getNPCRegistry().isNPC(e.getRightClicked())) {
                return;
            }
            if (gamePlayer.isSpectator() && this.game.isStarted() && e.getRightClicked() instanceof Player && SpectatorSettings.get(gamePlayer).getOption(SpectatorSettings.Option.FIRSTPERSON)) {
                e.setCancelled(true);
                if (Objects.requireNonNull(GamePlayer.get(e.getRightClicked().getUniqueId())).isSpectator()) {
                    return;
                }
                gamePlayer.sendTitle("§aNow Watching §7" + e.getRightClicked().getName(), "", 0, 20, 0);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().setSpectatorTarget(e.getRightClicked());
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && gamePlayer.isSpectator() && this.game.isStarted() && SpectatorSettings.get(gamePlayer).getOption(SpectatorSettings.Option.FLY)) {
            e.setCancelled(true);
            if (e.getPlayer().isOnGround()) {
                e.getPlayer().getLocation().setY(e.getPlayer().getLocation().getY() + 0.1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null && gamePlayer.isSpectator() && this.game.isStarted() && SpectatorSettings.get(gamePlayer).getOption(SpectatorSettings.Option.FIRSTPERSON) && e.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            gamePlayer.sendTitle("§eExit Spectator", "", 0, 20, 0);
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }
    }

}


