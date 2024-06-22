/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.zombie;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Zombie extends Classes {
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3 = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();

    public Zombie() {
        super("Zombie", "僵尸", "僵尸", ChatColor.DARK_GREEN, Material.ROTTEN_FLESH, (byte) 0, ClassesType.NOVICE, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.ASSIST}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDkxMTMzOTk3OTIsInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYzQ4ODY1ZmRhZmU4NjA5MzkyMTgxNjcxMjIwZjlhNjFlYjljMWU2ZWU5YjQ3NDkzYzA4YjllYzE1Mjg0MGNmIn19fQ==", "HW1oBtR1Tcyb2XNf7VpeO2r6qsZ9hkSRmDzO3RwKgRd45q3ZnNxhMzC+wOf78IXygzRg791caKaOwSlGeZI7UdwbTr/0gMe4eEHNqnWFTmSUwgCB9z3Zm9HY3uNd8sprUHYPMwzgCHY1BLdSFYnTj/ox6kPZCfHRbPzCxXws1xDBkX7VyKZ8i4vdDxXTYRUxzlj7N1D2CgUz419WzLwwZKMDv6cfb6igaW4TsbidzKrwWarWoNk4ndSGWNsFgSvtIXCDhaerv43zlDDsJvtHsF8wMLJOA5/vPNwSrNuHltyMxiRtfppcsbjDFLrYxe2XQopt/DDDKytvLqkYDixU2vHzmIa6vRqxrhkcG54Mc17kq+kYrKaV9G09UwSDWUbzrujFoToF7mhEwAyMiioIoicU/dBBeCcUSsTy22VeNwDChuBpejNkR9zh51FeHwkiuEGExyQwB8j3FF6kP9oUBX2oJSp+l4G/ptomVsErprzsUDbXPy4wLwiq1yQoU2svBwWRwUmN5fx3bUjmshbfvMWOlMBT0APV5qDRIg39FDLAzzNR4mEnarecozDyZbJAbonTgOZAN/lLBhCnoyPBrdJC0fQ74abxDKdyfOVrP78HCrzQqFA00QqBGDNbV9z+dpHXgfG7bWba6N7loET1DO9uxPOPIxTTONXRDAKS1Qo=");
        this.setPrestigeSkin("","");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " PickAxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Heal (10\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Zombie.this.nameColor + Zombie.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" \u00a78\u25aa \u00a77Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u4fdd\u62a4 I");
                        lore.add(" \u00a7a+ \u00a77Potion of Heal 8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x1 \u279c \u00a7ax3");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u00a78\u279c \u00a7aII");
                        lore.add(" \u00a7a+ \u00a77Potion of Speed II");
                        lore.add("    \u00a78\u25aa 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 II \u00a78\u279c \u00a7aIII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Speed II \u00a788\u2764 \u279c \u00a7a10\u2764");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Speed II \u00a78x1 \u279c \u00a7ax2");
                        lore.add("    \u00a78\u25aa 0:15");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Chestplate");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 III \u00a78\u279c \u00a7aII");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        return new ArrayList<String>();
    }

    @Override
    public int unlockCost() {
        return 0;
    }

    @Override
    public int energyMelee() {
        return 13;
    }

    @Override
    public int energyBow() {
        return 13;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        StringBuffer sb = new StringBuffer(this.mainSkill.getSkillTip(gamePlayer));
        sb.append(" " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer));
        return sb.toString();
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill3.getOrDefault(gamePlayer, 0) > 0) {
                skill3.put(gamePlayer, skill3.get(gamePlayer) - 1);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (!MegaWalls.getInstance().getGame().isWallsFall()) {
                Player player = gamePlayer.getPlayer();
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20, 2));
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                    gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        GamePlayer gamePlayer;
        Classes classes;
        if (e.getItem().getType() == Material.POTION && (classes = ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Potion of Heal")) {
            PlayerUtils.heal(e.getPlayer(), 2);
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
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(damager))) {
                return;
            }
            if (player != null && player.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            Classes classes = null;
            if (player != null) {
                classes = ClassesManager.getSelected(player);
            }
            if (classes != null && classes.equals(this)) {
                this.secondSkill.use(player, player.getPlayerStats().getKitStats(classes));
            }
            if (classes != null && classes.equals(this)) {
                if (skill3Cooldown.getOrDefault(damager, 0) > 0) {
                    e.setDamage(e.getDamage() * 1.75);
                }
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(shooter))) {
                return;
            }
            Classes classes = null;
            if (player != null) {
                classes = ClassesManager.getSelected(player);
            }
            if (classes != null && classes.equals(this)) {
                this.thirdSkill.use(player, player.getPlayerStats().getKitStats(classes));
            }
        }
    }
}

