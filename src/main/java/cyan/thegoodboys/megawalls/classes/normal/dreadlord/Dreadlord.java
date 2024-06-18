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
package cyan.thegoodboys.megawalls.classes.normal.dreadlord;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
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

public class Dreadlord extends Classes {
    public static Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static Map<GamePlayer, Integer> skill3 = new HashMap<>();
    public static Map<GamePlayer, Integer> skill2cooldown = new HashMap<GamePlayer, Integer>();

    public Dreadlord() {
        super("Dreadlord", "恐惧魔王", "恐惧魔", ChatColor.DARK_RED, Material.NETHER_BRICK_ITEM, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.CHARGER, ClassesInfo.Orientation.WARRIOR}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 1).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_UNDEAD, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_UNDEAD, 1).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dreadlord.this.nameColor + Dreadlord.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u706b\u7130\u4fdd\u62a4 I");
                        lore.add(" \u00a7a+ \u00a77Potion of Speed II");
                        lore.add("    \u00a78\u25aa 0:15");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x1 \u279c \u00a7ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Sword");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u706b\u7130\u4fdd\u62a4 I");
                        lore.add("    \u00a7a+ \u7206\u70b8\u4fdd\u62a4 I");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Speed \u00a78x1 \u279c \u00a7ax2");
                        lore.add("    \u00a78\u25aa 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u706b\u7130\u4fdd\u62a4 I");
                        lore.add("    \u00a78\u25aa \u7206\u70b8\u4fdd\u62a4 I");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764 \u00a78x1 \u279c \u00a7ax2");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x2 \u279c \u00a7ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Sword");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u4ea1\u7075\u6740\u624b I");
                        lore.add("    \u00a7a+ \u950b\u5229 I");
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u706b\u7130\u4fdd\u62a4 I");
                        lore.add("    \u00a78\u25aa \u7206\u70b8\u4fdd\u62a4 I \u279c \u00a7aII");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a77\u4f60\u559c\u6b22\u8fd1\u6218\u4e0e\u8fdc\u7a0b\u7684\u6df7\u5408.");
        lore.add("\u00a77\u4f60\u662f\u65b9\u5757\u6316\u6398\u9ad8\u624b.");
        lore.add("\u00a77\u4f60\u662f\u4e00\u540d\u6cd5\u5e08.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 15000;
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
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill3.getOrDefault(gamePlayer, 0) > 0) {
                skill3.put(gamePlayer, skill3.get(gamePlayer) - 1);
            }
            if (skill2cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2cooldown.put(gamePlayer, skill2cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        String third = this.thirdSkill.getSkillTip(gamePlayer);
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + (third == null ? "" : " " + third) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }


    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            this.thirdSkill.use(killer, killer.getPlayerStats().getKitStats(classes));
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
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            assert entity != null;
            if (entity.isSpectator() || entity.getGameTeam().isInTeam(damager)) {
                return;
            }
            assert damager != null;
            Classes classes = ClassesManager.getSelected(damager);
            if (entity.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            if (classes.equals(this) && !damager.isSpectator()) {
                this.secondSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
                if (skill3.getOrDefault(damager, 0) > 0) {
                    e.setDamage(e.getDamage() * 1.95);
                    this.secondSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
                }

            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            Classes classes = null;
            if (shooter != null) {
                classes = ClassesManager.getSelected(shooter);
            }
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            if (shooter != null && entity != null && (entity.isSpectator() || shooter.getGameTeam().isInTeam(entity))) {
                return;
            }
            if (classes != null && classes.equals(this)) {
                this.secondSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
            }
        }
    }
}

