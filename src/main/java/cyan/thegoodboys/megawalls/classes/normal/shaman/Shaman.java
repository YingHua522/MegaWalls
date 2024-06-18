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
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.shaman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.ShamanWolf;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shaman extends Classes {
    public static Map<GamePlayer, Integer> skill3 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> wind = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, List<ShamanWolf>> wolves = new HashMap<>();
    public static Map<GamePlayer, Integer> attackCount = new HashMap<>();
    public static Map<GamePlayer, Integer> ironOreCount = new HashMap<>();

    public Shaman() {
        super("Shaman", "萨满", "萨满", ChatColor.GREEN, Material.ENCHANTMENT_TABLE, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.WARRIOR, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.ONE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FALL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Shaman.this.nameColor + Shaman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 摔落保护 I");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add("    §8▪ 摔落保护 I");
                        lore.add(" §8▪ §7Potion of Speed II §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 摔落保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7钻石 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add("    §8▪ 摔落保护 II");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你热爱妨碍敌人.");
        lore.add("§7你想拥有一只小狗.");
        lore.add("§7你讨厌挖煤.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 5000;
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
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
            if (gamePlayer != null && (gamePlayer.isSpectator() || gamePlayer.getGameTeam().isInTeam(GamePlayer.get(e.getDamager().getUniqueId())))) {
                return;
            }
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (classes.equals(this)) {
                this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
            }
            GamePlayer damagerPlayer = GamePlayer.get(e.getDamager().getUniqueId());
            if (gamePlayer.isSpectator() || gamePlayer.getGameTeam().isInTeam(GamePlayer.get(e.getDamager().getUniqueId()))) {
                return;
            }
            if (damagerPlayer != null) {
                classes = ClassesManager.getSelected(damagerPlayer);
            }
            if (classes.equals(this) && MegaWalls.getRandom().nextInt(100) <= 17) {
                int seconds = 0;
                if (damagerPlayer != null) {
                    seconds = ((SecondSkill) this.secondSkill).getSeconds(damagerPlayer.getPlayerStats().getKitStats(classes).getSkill2Level());
                }
                Player damager = (Player) e.getDamager();
                if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
                    damager.removePotionEffect(PotionEffectType.SPEED);
                }
                damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, seconds * 20, 1));
                Player entity = (Player) e.getEntity();
                if (entity.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                    entity.removePotionEffect(PotionEffectType.WEAKNESS);
                }
                entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, seconds * 20, 0));
            }
            if (damagerPlayer != null) {
                if (classes.equals(this)) {
                    this.secondSkill.use(damagerPlayer, damagerPlayer.getPlayerStats().getKitStats(classes));
                }
            }
        }
    }
}

