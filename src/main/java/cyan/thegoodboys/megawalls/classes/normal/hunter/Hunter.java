/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.classes.normal.hunter;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.api.event.WallFallEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.CustomPig;
import cyan.thegoodboys.megawalls.util.EntityTypes;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.ParticleUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Hunter extends Classes {
    public static final Map<GamePlayer, Integer> skill = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, String> skill3 = new HashMap<GamePlayer, String>();
    public static final Map<GamePlayer, Integer> pigCooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> pigTimer = new HashMap<>();
    public static final Map<GamePlayer, Integer> hit = new HashMap<>();

    public Hunter() {
        super("Hunter", "猎人", "猎人", ChatColor.GREEN, Material.BOW, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.REMOTE, ClassesInfo.Orientation.WARRIOR}, ClassesInfo.Difficulty.THREE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Golden Apple").build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Golden Apple").build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLDEN_APPLE, 2).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Golden Apple").build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.GOLDEN_APPLE, 2).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Golden Apple").build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).addEnchantment(Enchantment.ARROW_INFINITE, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.GOLDEN_APPLE, 5).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Golden Apple").build());
                        items.add(new ItemBuilder(Material.ARROW).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.CARROT_STICK).setDisplayName(Hunter.this.nameColor + Hunter.this.getDisplayName() + " Carrot Stick").build());
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
                        lore.add(" §8▪ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Golden Apple");
                        lore.add(" §8▪ §7Arrow");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 弹射物保护 I");
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 力量 I");
                        lore.add("    §8▪ 无限 I");
                        lore.add(" §8▪ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add("    §8▪ 弹射物保护 I");
                        lore.add(" §8▪ §7Golden Apple §8x1 ➜ §ax2");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 弹射物保护 I");
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 锋利 I");
                        lore.add(" §8▪ §7钻石 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add("    §8▪ 弹射物保护 I ➜ §aII");
                        lore.add(" §8▪ §7Golden Apple §8x2 ➜ §ax3");
                        lore.add(" §a+ §7胡萝卜钓竿");
                    }
                }
                return lore;
            }
        });
    }

    public static Vector getHorizontalDirection(Player player, double mult) {
        Vector vector = new Vector();
        double rotX = player.getLocation().getYaw();
        double rotY = 0;
        vector.setY(-Math.sin(Math.toRadians(rotY)));
        double xz = Math.cos(Math.toRadians(rotY));
        vector.setX((-xz * Math.sin(Math.toRadians(rotX))) * mult);
        vector.setZ((xz * Math.cos(Math.toRadians(rotX))) * mult);
        return vector;
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你喜欢精准Arrow法.");
        lore.add("§7你想要一些小宠物来帮助你.");
        lore.add("§7你想拥有夜视能力.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 40000;
    }

    @Override
    public int energyMelee() {
        return 4;
    }

    @Override
    public int energyBow() {
        return 8;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " §a§lPIG " + (pigCooldown.getOrDefault(gamePlayer, 0) == 0 ? "§a§l✓" : "§e" + pigCooldown.get(gamePlayer) + "§es");
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill.getOrDefault(gamePlayer, 0) > 0) {
                skill.put(gamePlayer, skill.get(gamePlayer) - 1);
                if (skill.get(gamePlayer) == 0) {
                    gamePlayer.sendMessage("§a你的鹰眼已关闭。");
                }
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
            }
            if (pigCooldown.getOrDefault(gamePlayer, 0) > 0) {
                pigCooldown.put((GamePlayer) gamePlayer, pigCooldown.get(gamePlayer) - 1);
            }
            if (pigTimer.getOrDefault(gamePlayer, 0) > 0) {
                pigTimer.put(gamePlayer, pigTimer.get(gamePlayer) - 1);
            }
            if (pigTimer.getOrDefault(gamePlayer, 0) <= 0) {
                Entity passenger = gamePlayer.getPlayer().getVehicle();
                if (passenger instanceof Pig) {
                    passenger.remove();
                    gamePlayer.sendMessage("§c你的猪已经消失了！");
                }
                pigTimer.remove(gamePlayer);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 25) > 0) {
                skill3Cooldown.put((GamePlayer) gamePlayer, skill3Cooldown.getOrDefault(gamePlayer, 25) - 1);
                if (skill3Cooldown.getOrDefault(gamePlayer, 25) == 10) {
                    this.thirdSkill.use((GamePlayer) gamePlayer, gamePlayer.getPlayerStats().getKitStats(this));
                }
                if (skill3Cooldown.getOrDefault(gamePlayer, 25) == 0) {
                    skill3Cooldown.put((GamePlayer) gamePlayer, 25);
                    skill3.clear();
                }
            }

            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
            gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            if (MegaWalls.getInstance().getGame().isWallsFall() && !MegaWalls.getInstance().getGame().isDeathMatch()) {
                gamePlayer.addEnergy(1, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (MegaWalls.getInstance().getGame().isWallsFall() && MegaWalls.getInstance().getGame().isDeathMatch() && gamePlayer.getEnergy() >= 0 && Hunter.hit.getOrDefault(gamePlayer, 0) >= 2) {
                gamePlayer.addEnergy(1, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
        });
    }

    @EventHandler
    public void onWallFall(WallFallEvent e) {
        ThirdSkill.potions.remove("HasteI 15s");
    }

    @EventHandler
    public void onProjectileLaunch(final ProjectileLaunchEvent e) {
        GamePlayer gamePlayer;
        Classes classes;
        if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player && ClassesManager.getSelected(Objects.requireNonNull(gamePlayer = GamePlayer.get(((Player) e.getEntity().getShooter()).getUniqueId()))).equals(this)) {
            if (e.getEntity().getType() == EntityType.ARROW) {
                e.getEntity().setMetadata("Arrow", MegaWalls.getFixedMetadataValue());
            }
            boolean max = e.getEntity().getVelocity().getX() > 2.0 || e.getEntity().getVelocity().getX() < -2.0;
            if (e.getEntity().getVelocity().getY() > 2.0 || e.getEntity().getVelocity().getY() < -2.0) {
                max = true;
            }
            if (e.getEntity().getVelocity().getZ() > 2.0 || e.getEntity().getVelocity().getZ() < -2.0) {
                max = true;
            }
            if (skill.getOrDefault(gamePlayer, 0) > 0 && max) {
                e.getEntity().setMetadata("NoAddEnergy!", MegaWalls.getFixedMetadataValue());
                ParticleUtils.play(EnumParticle.SUSPENDED, gamePlayer.getPlayer().getLocation(), 0.5, 0.5, 0.5, 1, 500);
                new BukkitRunnable() {
                    public void run() {
                        if (!gamePlayer.isOnline() || e.getEntity().isDead() || e.getEntity().isOnGround()) {
                            this.cancel();
                            return;
                        }
                        Player target = null;
                        for (Player other : PlayerUtils.getNearbyPlayers(e.getEntity().getLocation(), 8.0)) {
                            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
                            if (gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || target != null && !(other.getLocation().distance(e.getEntity().getLocation()) < target.getLocation().distance(e.getEntity().getLocation()))))
                                continue;
                            target = other;
                        }
                        if (target != null) {
                            Location loc = e.getEntity().getLocation().clone();
                            Location targetLoc = target.getLocation().clone();
                            e.getEntity().setVelocity(new Vector(targetLoc.getX() - loc.getX(), targetLoc.getY() - loc.getY(), targetLoc.getZ() - loc.getZ()).multiply(0.8));
                            PlayerUtils.heal(gamePlayer.getPlayer(), 0.5);
                        }
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        GamePlayer player;
        Classes classes;
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL && ClassesManager.getSelected(Objects.requireNonNull(player = GamePlayer.get(e.getEntity().getUniqueId()))).equals(this) && pigTimer.getOrDefault(GamePlayer.get(e.getEntity().getUniqueId()), 0) > 0) {
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, -e.getDamage() * 0);
            pigTimer.put(GamePlayer.get(e.getEntity().getUniqueId()), 0);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(GamePlayer.get(e.getDamager().getUniqueId())))) {
                return;
            }
            if (player != null) {
                Classes classes = ClassesManager.getSelected(player);
                if (classes.equals(this)) {
                    this.secondSkill.use(player, player.getPlayerStats().getKitStats(classes));
                    Hunter.startAddEnergyTimer(player, 5, 3);
                    if (classes.equals(this) && Hunter.hit.getOrDefault(player, 0) < 2) {
                        Hunter.hit.put(player, Hunter.hit.getOrDefault(player, 0) + 1);
                    }
                }
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
        if (gamePlayer != null) {
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem().getType() == Material.CARROT_STICK) {
                if (pigCooldown.getOrDefault(gamePlayer, 0) > 0) {
                    gamePlayer.sendMessage("§c你不能在冷却时间内召唤猪！");
                    return;
                }
                pigTimer.put(gamePlayer, 10);
                pigCooldown.put(gamePlayer, 30);
                CustomPig pig = new CustomPig(((CraftWorld) Bukkit.getWorld("world")).getHandle());
                Pig bukkitPig = (Pig) pig.getBukkitEntity();
                pig.setGamePlayer(gamePlayer);
                bukkitPig.setSaddle(true); // 给猪添加马鞍
                EntityTypes.spawnEntity(pig, gamePlayer.getPlayer().getLocation());
                // 使用延迟任务来设置玩家为猪的乘客
                bukkitPig.setPassenger(gamePlayer.getPlayer());
                gamePlayer.sendMessage("§a你召唤了一只猪！");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (gamePlayer.getPlayer().getVehicle() instanceof Pig) {
                            Pig pig = (Pig) gamePlayer.getPlayer().getVehicle();
                            Vector direction = gamePlayer.getPlayer().getLocation().getDirection();
                            direction.setY(pig.getVelocity().getY());
                            Location pigLocation = pig.getLocation();
                            Block blockInFront = pigLocation.add(direction).getBlock();
                            if (blockInFront.getType() != Material.AIR && blockInFront.getLocation().getY() > pigLocation.getY()) {
                                direction.setY(1.5); // 增加猪的跳跃高度
                            }
                            // 检查猪前方的方块是否为可跳跃的方块
                            Block blockBelow = blockInFront.getRelative(BlockFace.DOWN);
                            if (!blockBelow.getType().isSolid()) {
                                // 如果是，设置猪的速度向量的Y分量为一个正值，使其跳跃
                                direction.setY(1.5); // 增加猪的跳跃高度
                            }
                            direction.multiply(0.5);
                            pig.setVelocity(direction);
                        } else {
                            // 如果玩家不再骑猪，移除猪并取消任务
                            bukkitPig.remove();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
            }
        }
    }
}

