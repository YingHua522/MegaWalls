/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.creeper;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Creeper extends Classes {
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<GamePlayer, Integer>();

    public Creeper() {
        super("Creeper", "爬行者", "爬行者", ChatColor.GREEN, Material.TNT, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.HURT, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.FOUR);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 5).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 6).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.TNT, 64).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Creeper.this.nameColor + Creeper.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 爆炸保护 II");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 爆炸保护 II ➜ §aIII");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 爆炸保护 III ➜ §aIV");
                        lore.add(" §8▪ §7Potion of Speed §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 爆炸保护 IV ➜ §aV");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 爆炸保护 V ➜ §aVI");
                        lore.add("    §8▪ 保护 I");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想在连击时加入一些小爆炸.");
        lore.add("§7你想掌控你的生命.");
        lore.add("§7你喜欢给那些毫无防备的矿工们惊喜(惊吓).");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 25000;
    }

    @Override
    public int energyMelee() {
        return 20;
    }

    @Override
    public int energyBow() {
        return 20;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer != null) {
            Classes classes = ClassesManager.getSelected(gamePlayer);
            // 检查玩家是否使用了这个技能
            if (classes.equals(this)) {
                secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
            assert gamePlayer != null;
            Player player = gamePlayer.getPlayer();
            Classes classes2 = ClassesManager.getSelected(gamePlayer);
            if (classes2.equals(this)) {
                if (e.getDamager().getType() == EntityType.PRIMED_TNT && e.getDamager().hasMetadata("cretnt") && Objects.equals(gamePlayer.getPlayer().getName(), e.getDamager().getCustomName()) && player.getHealth() > 20) {
                    if (Creeper.skill3Cooldown.getOrDefault(GamePlayer.get(e.getEntity().getUniqueId()), 0) > 0) {
                        return;
                    }
                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) (4 * 20.0), 1));
                    if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (4 * 20.0), 0));
                    Creeper.skill3Cooldown.put(GamePlayer.get(e.getEntity().getUniqueId()), 5);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        if (gamePlayer != null) {
            final Classes classes = ClassesManager.getSelected(gamePlayer);
            if (classes.equals(this) && e.getBlock().getType() == Material.TNT && MegaWalls.getInstance().getGame().isWallsFall() && !e.getPlayer().isSneaking() && !(Creeper.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0)) {
                e.setCancelled(true);
                final Player player = e.getPlayer();
                TNTPrimed tntPrimed = player.getWorld().spawn(e.getBlock().getLocation().add(0.0, 0.1, 0.0), TNTPrimed.class);
                tntPrimed.setCustomName(gamePlayer.getPlayer().getName());
                tntPrimed.setMetadata("cretnt", new FixedMetadataValue(MegaWalls.getInstance(), gamePlayer.getGameTeam()));
                tntPrimed.setFuseTicks(20);
                tntPrimed.setIsIncendiary(false);
                ItemStack item = player.getItemInHand();
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.setItemInHand(null);
                }
                Creeper.skill2Cooldown.put(gamePlayer, 3);
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        Player player;
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes != null && classes.equals(this) && ((Player) e.getEntity()).getHealth() < 20.0) {
            this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)));
        }
    }
}

