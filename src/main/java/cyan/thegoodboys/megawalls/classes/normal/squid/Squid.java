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
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.squid;

import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Squid extends Classes {
    public static final Map<GamePlayer, Integer> skillCooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();

    public Squid() {
        super("Squid", "鱿鱼", "鱿鱼", ChatColor.AQUA, Material.INK_SACK, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.ONE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 2).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Cooked Fish").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 4).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Cooked Fish").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 4).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Cooked Fish").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Heal (6❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DEPTH_STRIDER, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 6).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Cooked Fish").build());
                        items.add(new ItemBuilder(Material.POTION, 3, (byte) 5).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Heal (5❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.DEPTH_STRIDER, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_FISH, 6).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Cooked Fish").build());
                        items.add(new ItemBuilder(Material.POTION, 3, (byte) 5).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Heal (5❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Squid.this.nameColor + Squid.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add("    §8▪ 保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Cooked Fish x2");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7烤制 鱼 §8x2 ➜ §ax4");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II ➜ §aIII");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 III ➜ §aIV");
                        lore.add("    §a+ 深海探索者 I");
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 深海探索者 II");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x2 ➜ §ax3");
                        lore.add(" §8▪ §7烤制 鱼 §8x4 ➜ §ax6");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 IV ➜ §aIII");
                        lore.add("    §8▪ 深海探索者 I ➜ §aII");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想要在掌控局面的同时造成大量伤害.");
        lore.add("§7你喜欢在此之中灌输无尽的恐惧.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 15000;
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
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put((GamePlayer) gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put((GamePlayer) gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getHealth() < 21.0) {
                this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)));
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        GamePlayer gamePlayer;
        Classes classes;
        if (e.getItem().getType() == Material.POTION && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId()))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Potion of Heal")) {
            PlayerUtils.heal(e.getPlayer(), 2);
        } else if (e.getItem().getType() == Material.POTION && (classes = ClassesManager.getSelected(Objects.requireNonNull(gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId())))).equals(this)) {
            this.secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
        }
    }

}

