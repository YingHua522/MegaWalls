/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.pirate;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.CustomBrid;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Pirate extends Classes {
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static final Map<GamePlayer, Integer> bird = new HashMap<>();
    public static final Map<GamePlayer, Integer> chest = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> mode = new HashMap<>();
    private static final Set<Material> set = new HashSet<Material>();

    static {
        set.addAll(Arrays.asList(Material.values()));
    }

    static {
        Collections.addAll(set, Material.values());
    }

    public Pirate() {
        super("Pirate", "海盗", "海盗", ChatColor.GOLD, Material.STONE_AXE, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.WARRIOR, ClassesInfo.Orientation.HURT}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW, 32).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Potion of Heal (Regeneration III 0:09)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 180, 2)).addPotion(new PotionEffect(PotionEffectType.ABSORPTION, 1800, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.ARROW, 16).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Potion of Heal (Regeneration III 0:09)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 180, 2)).addPotion(new PotionEffect(PotionEffectType.ABSORPTION, 1800, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Potion of Heal (Regeneration III 0:09)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 180, 2)).addPotion(new PotionEffect(PotionEffectType.ABSORPTION, 1800, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Pirate.this.nameColor + Pirate.this.getDisplayName() + " Steak").build());
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
                        lore.add("    §8▪ 力量 I");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 火焰保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Arrow §8x32");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 火焰保护 I");
                        lore.add("    §a+ 弹射物保护 I");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Arrow §8x32 ➜ §ax56");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add("    §8▪ 火焰保护 I ➜ §aII");
                        lore.add("    §a+ 弹射物保护 I");
                        lore.add(" §8▪ §7Potion of Speed §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Arrow §8x56 ➜ §ax64");
                        lore.add(" §8▪ §7Arrow §8x56 ➜ §ax16");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II");
                        lore.add("    §8▪ 火焰保护 II");
                        lore.add("    §a+ 弹射物保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Arrow §8x64 ➜ §ax40");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II ➜ §aIII");
                        lore.add("    §8▪ 火焰保护 II");
                        lore.add("    §a+ 弹射物保护 II");
                    }
                }
                return lore;
            }
        });
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        GamePlayer gp = GamePlayer.get(event.getPlayer().getUniqueId());
        if (gp != null) {
            Classes classes = ClassesManager.getSelected(gp);
            if (classes.equals(this) && CustomBrid.playerArmorStands.containsKey(event.getPlayer())) {
                for (ArmorStand armorStand : CustomBrid.playerArmorStands.get(event.getPlayer())) {
                    armorStand.remove();
                }
                CustomBrid.playerArmorStands.remove(event.getPlayer());
                Pirate.bird.put(gp, 0);
            }
        }
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想要在战斗中占据上风.");
        lore.add("§7你喜欢宝藏.");
        lore.add("§7Yarr^_^.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 40000;
    }

    @Override
    public int energyMelee() {
        return 12;
    }

    @Override
    public int energyBow() {
        return 12;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer);
    }

    @EventHandler
    public void wither(EntityExplodeEvent e) {
        if (e.getEntity() instanceof WitherSkull) {
            e.setCancelled(true);
        }
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (skill2.getOrDefault(gamePlayer, 20) > 0) {
                skill2.put(gamePlayer, skill2.getOrDefault(gamePlayer, 20) - 1);
            }
            if (skill2.get(gamePlayer) == 0 && Pirate.bird.getOrDefault(gamePlayer, 0) < 6) {
                Pirate.bird.put(gamePlayer, Pirate.bird.getOrDefault(gamePlayer, 0) + 1);
                Pirate.skill2.put(gamePlayer, 20);
                CustomBrid.createArmorStand(gamePlayer.getPlayer());
            }
            if (Pirate.chest.getOrDefault(gamePlayer, 0) > 0) {
                Pirate.chest.put(gamePlayer, Pirate.chest.get(gamePlayer) - 1);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (classes.equals(this) && Pirate.bird.getOrDefault(gamePlayer, 0) > 0 && (mode.getOrDefault(gamePlayer, 0) == 1)) {
                Player target = null;
                for (Player player2 : this.getNearbyPlayers(gamePlayer.getPlayer(), 5)) {
                    if (target != null && !(player2.getLocation().distance(gamePlayer.getPlayer().getLocation()) < target.getLocation().distance(gamePlayer.getPlayer().getLocation())))
                        continue;
                    target = player2;
                }
                if (target == null) {
                    return;
                }
                Player closest = null;
                for (Player gamePlayer1 : getNearbyPlayers(gamePlayer.getPlayer(), 5)) {
                    if (gamePlayer1.equals(gamePlayer.getPlayer()) || Objects.requireNonNull(GamePlayer.get(gamePlayer1.getUniqueId())).getGameTeam().isInTeam(gamePlayer))
                        continue;
                    if (closest != null && !(gamePlayer1.getLocation().distance(gamePlayer1.getLocation()) < closest.getLocation().distance(gamePlayer1.getLocation())))
                        continue;
                    closest = gamePlayer1;
                }
                assert closest != null;
                Player finalClosest = closest;
                Pirate.bird.put(gamePlayer, 0);
                new BukkitRunnable() {
                    public void run() {
                        CustomBrid.removePlayer(finalClosest.getPlayer(), gamePlayer.getPlayer());
                        PlayerUtils.realDamage(finalClosest, gamePlayer.getPlayer(), Pirate.bird.getOrDefault(gamePlayer, 0));
                        finalClosest.getPlayer().getWorld().playSound(finalClosest.getPlayer().getLocation(), Sound.EXPLODE, 1.0f, 0.0f);
                        cancel();
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 20L, 0L);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        GamePlayer gp = GamePlayer.get(event.getEntity().getUniqueId());
        if (gp != null) {
            Classes classes = ClassesManager.getSelected(gp);
            if (classes.equals(this) && CustomBrid.playerArmorStands.containsKey(event.getEntity())) {
                for (ArmorStand armorStand : CustomBrid.playerArmorStands.get(event.getEntity())) {
                    armorStand.remove();
                }
                CustomBrid.playerArmorStands.remove(event.getEntity());
                Pirate.bird.put(gp, 0);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerKillEvent event) {
        GamePlayer killer = event.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this) && Pirate.chest.getOrDefault(killer, 0) <= 0) {
            double dropChance = this.collectSkill.getAttribute(killer.getPlayerStats().getKitStats(classes).getSkill4Level());
            if (MegaWalls.getInstance().getGame().isDeathMatch()) {
                dropChance *= 2.0;
            }
            double random = Math.random();
            if (random < dropChance) {
                ItemStack randomItem = FourthSkill.items.get(new Random().nextInt(FourthSkill.items.size()));
                event.getKiller().getPlayer().getWorld().dropItemNaturally(event.getPlayer().getPlayer().getLocation(), randomItem);
                Pirate.chest.put(killer, 20);
            }
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
        if (classes != null && classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.DIAMOND_PICKAXE) {
            if (Pirate.mode.getOrDefault(gamePlayer, 0) == 2) {
                mode.put(gamePlayer, 0);
                player.sendMessage("§a模式:无");
            } else if (mode.getOrDefault(gamePlayer, 0) == 1) {
                Pirate.mode.put(gamePlayer, 2);
                player.sendMessage("§a模式:待向");
            } else if (Pirate.mode.getOrDefault(gamePlayer, 0) == 0) {
                mode.put(gamePlayer, 1);
                player.sendMessage("§a模式:自动攻击");
            }
        }
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
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(GamePlayer.get(e.getDamager().getUniqueId())))) {
                return;
            }
            Classes classes = null;
            if (player != null) {
                classes = ClassesManager.getSelected(player);
            }
            if (player != null && classes.equals(this) && ((Player) e.getEntity()).getHealth() - e.getFinalDamage() < this.thirdSkill.getAttribute(player.getPlayerStats().getKitStats(classes).getSkill3Level())) {
                this.thirdSkill.use(player, player.getPlayerStats().getKitStats(classes));
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(GamePlayer.get(e.getDamager().getUniqueId())))) {
                return;
            }
            if (player != null) {
                Classes classes = ClassesManager.getSelected(player);
                if (classes.equals(this) && (mode.getOrDefault(player, 0) == 2)) {
                    assert damager != null;
                    new BukkitRunnable() {
                        public void run() {
                            cancel();
                        }
                    }.runTaskTimer(MegaWalls.getInstance(), 20L, 0L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity().getShooter() instanceof Player && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(((Player) e.getEntity().getShooter()).getUniqueId()))).equals(this) && e.getEntity().getType() == EntityType.ARROW) {
            e.getEntity().setMetadata("Arrow", MegaWalls.getFixedMetadataValue());
        }
    }

    private List<Player> getNearbyPlayers(Player player, int radius) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers(player, (double) radius)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gameOther != null && gamePlayer != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(player.getLocation()) > (double) radius))
                continue;
            players.add(other);
        }
        return players;
    }
}

