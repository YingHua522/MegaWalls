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
 *  org.bukkit.event.entity.EntityDamageEvent$DamageModifier
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.cow;

import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.CustomMick;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Cow extends Classes {
    public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2Damage = new HashMap<GamePlayer, Integer>();
    static final Map<Player, List<ArmorStand>> playerArmorStands = new HashMap<>();

    public Cow() {
        super("Cow", "牛", "牛", ChatColor.LIGHT_PURPLE, Material.MILK_BUCKET, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.ASSIST}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.MILK_BUCKET).build());
                        items.add(new ItemBuilder(Material.BREAD, 2).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bread").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.MILK_BUCKET).build());
                        items.add(new ItemBuilder(Material.BREAD, 4).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bread").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.MILK_BUCKET, 2).build());
                        items.add(new ItemBuilder(Material.BREAD, 4).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bread").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.MILK_BUCKET, 2).build());
                        items.add(new ItemBuilder(Material.BREAD, 6).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bread").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.MILK_BUCKET, 21).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Milk").build());
                        items.add(new ItemBuilder(Material.BREAD, 6).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Bread").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Heal (10\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Cow.this.nameColor + Cow.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<String>();
                switch (level) {
                    case 1: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Sword");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77\u725b\u5976\u6876");
                        lore.add(" \u00a78\u25aa \u00a77Bread \u00a78x2");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u4fdd\u62a4 I");
                        lore.add(" \u00a7a+ \u00a77Potion of Speed II");
                        lore.add("    \u00a78\u25aa 0:15");
                        lore.add(" \u00a78\u25aa \u00a77Bread \u00a78x2 \u279c \u00a7ax4");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u279c \u00a7aII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Speed \u00a78x1 \u279c \u00a7ax2");
                        lore.add("    \u00a78\u25aa 0:15");
                        lore.add(" \u00a78\u25aa \u00a77\u725b\u5976\u6876 \u00a78x1 \u279c \u00a7ax2");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u279c \u00a7aII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764 \u00a78x1 \u279c \u00a7ax2");
                        lore.add(" \u00a78\u25aa \u00a77Bread \u00a78x4 \u279c \u00a7ax6");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 III \u279c \u00a7aI");
                        lore.add(" \u00a78\u25aa \u00a77\u725b\u5976\u6876 \u00a78x2 \u279c \u00a7ax3");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a77\u4f60\u60f3\u5e2e\u52a9\u961f\u53cb.");
        lore.add("\u00a77\u4f60\u8fd8\u60f3\u8981\u9632\u5fa1\u548c\u751f\u5b58.");
        lore.add("\u00a77\u54de~");
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
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) < 0) {
                CustomMick.deactivateAllMilkBarriers(gamePlayer.getPlayer());
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            e.setCancelled(true);
            ItemStack itemStack = e.getPlayer().getItemInHand().clone();
            itemStack.setAmount(itemStack.getAmount() - 1);
            e.getPlayer().setItemInHand(e.getPlayer().getItemInHand().getAmount() > 1 ? itemStack : null);
            GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
            Classes classes = null;
            if (gamePlayer != null) {
                classes = ClassesManager.getSelected(gamePlayer);
            }
            if (classes != null && classes.equals(this)) {
                this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume1(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId()))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Potion of Heal")) {
            PlayerUtils.heal(e.getPlayer(), 2);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) {
            return;
        }
        if (((Player) e.getEntity()).getNoDamageTicks() > 10) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes != null && classes.equals(this)) {
            Player player = (Player) e.getEntity();
            if (player.getHealth() - e.getFinalDamage() < 20.0) {
                this.secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(ClassesManager.getSelected(gamePlayer)));
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0 && skill2Damage.getOrDefault(gamePlayer, 0) < 5) {
                e.setDamage(e.getDamage() * 0.5);
                PlayerUtils.heal(player, 2.0);
                skill2Damage.put(gamePlayer, skill2Damage.getOrDefault(gamePlayer, 0) + 1);
                gamePlayer.getPlayer().getWorld().playSound(gamePlayer.getPlayer().getLocation(), Sound.CAT_MEOW, 1f, (float) (0.1 * skill2Damage.getOrDefault(gamePlayer, 0)));
                CustomMick.deactivateMilkBarrier(gamePlayer.getPlayer());
            }
        }
    }

}

