/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.pigman;

import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Pigman extends Classes {
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3 = new HashMap<>();

    public Pigman() {
        super("Pigman", "猪人", "猪人", ChatColor.LIGHT_PURPLE, Material.GRILLED_PORK, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.WARRIOR, ClassesInfo.Orientation.ASSIST}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 1).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Heal (10❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Pigman.this.nameColor + Pigman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Speed §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Chestplate");
                        lore.add("    §8▪ 耐久 X");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你更爱近战.");
        lore.add("§7你想要生存.");
        lore.add("§7你也想帮助队友生存.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 25000;
    }

    @Override
    public int energyMelee() {
        return 10;
    }

    @Override
    public int energyBow() {
        return 10;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        String t = this.secondSkill.getSkillTip(gamePlayer);
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + (t == null ? "" : " " + t);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put((GamePlayer) gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (skill.getOrDefault(gamePlayer, 0) > 0) {
                skill.put((GamePlayer) gamePlayer, skill.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes != null && classes.equals(this) && ((Player) e.getEntity()).getHealth() - e.getFinalDamage() < 20.0) {
            this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId()))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Potion of Heal")) {
            PlayerUtils.heal(e.getPlayer(), 2);
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
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            if (damager != null && (damager.isSpectator() || damager.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId())))) {
                return;
            }
            Classes classes = null;
            if (damager != null) {
                classes = ClassesManager.getSelected(damager);
            }
            if (classes != null && classes.equals(this)) {
                this.secondSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
                if (skill.getOrDefault(damager, 0) > 0) {
                    e.setDamage(e.getDamage() * 1.2);
                }
            }
        }
    }
}

