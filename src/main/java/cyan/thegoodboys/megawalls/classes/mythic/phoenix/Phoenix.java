/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.vehicle.VehicleExitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.phoenix;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Phoenix extends Classes {
    public static Map<GamePlayer, GamePlayer> target = new HashMap<GamePlayer, GamePlayer>();
    public static Map<GamePlayer, Integer> skillCooldown = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> attackTick = new HashMap<>();
    public static List<GamePlayer> skill3 = new ArrayList<>();

    public Phoenix() {
        super("Phoenix", "凤凰", "凤凰", ChatColor.RED, Material.SULPHUR, (byte) 0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.REMOTE, ClassesInfo.Orientation.ASSIST}, ClassesInfo.Difficulty.FOUR);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 2).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Cooked Fish").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 4).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Cooked Fish").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 4).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Cooked Fish").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 6).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Cooked Fish").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 6).setDisplayName(Phoenix.this.nameColor + Phoenix.this.getDisplayName() + " Cooked Fish").build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<String>();
                switch (level) {
                    case 1: {
                        lore.add(" §8▪ §7铁 Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 无限 I");
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Arrow");
                        lore.add(" §8▪ §7煮熟的 鲑鱼 §8x2");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 无限 I");
                        lore.add("    §a+ 力量 I");
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Potion of Speed II");
                        lore.add(" §8▪ 8▪ 0:15");
                        lore.add(" §8▪ §7煮熟的 鲑鱼 §8x2 ➜ §ax4");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §a+ 弹射物保护 I");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Potion of Speed II  §8x1 ➜ §ax2");
                        lore.add(" §8▪ 8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 无限 I");
                        lore.add("    §8▪ 力量 I §8➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7煮熟的 鲑鱼 §8x4 ➜ §ax6");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I §8➜ §aII");
                        lore.add("    §8▪ 弹射物保护 I");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add("    §8▪ 弹射物保护 I");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想帮助你的队友.");
        lore.add("§7你想要有机会复活.");
        lore.add("§7你很有耐心.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 3;
    }

    @Override
    public int energyMelee() {
        return 8;
    }

    @Override
    public int energyBow() {
        return 12;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put((GamePlayer) gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }

            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            if (damager != null && (damager.isSpectator() || damager.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId())))) {
                return;
            }
            Classes classes2 = null;
            if (damager != null) {
                classes2 = ClassesManager.getSelected(damager);
            }
            if (damager != null && classes2.equals(this) && damager.getPlayer().getInventory().getItemInHand().getType() == Material.BOW) {
                e.setCancelled(true);
                secondSkill.use(damager, damager.getPlayerStats().getKitStats(classes2));
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            Arrow arrow1 = (Arrow) e.getDamager();
            assert arrow1 != null;
            String arrow = arrow1.getCustomName();
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            Classes classes2 = null;
            if (shooter != null) {
                classes2 = ClassesManager.getSelected(shooter);
            }
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            if (entity != null && entity.isSpectator()) {
                return;
            }
            if (classes2 != null && classes2.equals(this) && "qwq".equals(arrow)) {
                e.setCancelled(true);
                arrow1.remove();
                PlayerUtils.realDamage(e.getEntity(), shooter.getPlayer(), 2);
                new BukkitRunnable() {
                    public void run() {
                        for (Player nearby : getNearbyPlayers(arrow1.getLocation(), shooter.getPlayer(), 3)) {
                            if (nearby == e.getEntity()) continue;
                            PlayerUtils.realDamage(nearby, shooter.getPlayer(), 2);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (nearby.getHealth() <= 0) {
                                        nearby.damage(10, shooter.getPlayer());
                                    }
                                }
                            }.runTaskLater(MegaWalls.getInstance(), 1L);
                            nearby.setNoDamageTicks(0);
                        }
                        for (Player nearby : getTeammates(arrow1.getLocation(), shooter.getPlayer())) {
                            PlayerUtils.heal(nearby, 2);
                            nearby.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
                        }
                        cancel();
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 2L, 0L);
            }
        }
    }

    private List<Player> getTeammates(Location location, Player team) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player other : PlayerUtils.getNearbyPlayers(location, 3)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || !Objects.requireNonNull(GamePlayer.get(team.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }

    private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && (gameOther.isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gameOther)))
                continue;
            players.add(other);
        }
        return players;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerEnergyChange(PlayerEnergyChangeEvent e) {
        GamePlayer gamePlayer = e.getPlayer();
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this) || target.containsValue(gamePlayer)) {
            e.setAmount(e.getAmount() + (int) ((double) (e.getAmount() * this.getSecondSkill().getPlayerLevel(gamePlayer)) * 0.05));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (e.getPlayer().getItemInHand() == null) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        Classes classes = ClassesManager.getSelected(gamePlayer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        if (gamePlayer != null && gamePlayer.isSpectator()) {
            return;
        }
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (gamePlayer != null && gamePlayer.getGameTeam().isWitherDead() && gamePlayer.getPlayer().getHealth() - e.getFinalDamage() <= 1.0 && (classes.equals(this))) {
            if (ThirdSkill.gamePlayers.contains(gamePlayer)) {
                return;
            }
            e.setCancelled(true);
            ThirdSkill.use(gamePlayer);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes != null && classes.equals(this) && (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) && e.getItem().getType() == Material.BOW && Phoenix.attackTick.getOrDefault(gamePlayer, 0) == 0) {
            this.secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
            Phoenix.attackTick.put(gamePlayer, 5);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVehicleExit(VehicleExitEvent e) {
        if (e.getExited() instanceof Player && skill3.contains(GamePlayer.get(e.getExited().getUniqueId()))) {
            e.setCancelled(true);
        }
    }
}

