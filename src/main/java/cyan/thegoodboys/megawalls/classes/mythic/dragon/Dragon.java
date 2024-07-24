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
package cyan.thegoodboys.megawalls.classes.mythic.dragon;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dragon
extends Classes {
    public static Map<GamePlayer, Boolean> skill2 = new HashMap<GamePlayer, Boolean>();
    private Map<Player, Map<Player, Integer>> hitCounts = new HashMap<>();
    public static Map<GamePlayer, Integer> gold = new HashMap<GamePlayer, Integer>();
    public Dragon() {
        super("Dragon", "龙","龙", ChatColor.RED, Material.DRAGON_EGG, (byte)0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.HURT, ClassesInfo.Orientation.REMOTE}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7","");
        this.setEquipmentPackage(new EquipmentPackage(this){

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Arrow").build());
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Arrow").build());
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Arrow").build());
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Arrow").build());
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.GOLD_BOOTS).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Dragon.this.nameColor + Dragon.this.getDisplayName() + " Arrow").build());
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
        lore.add("\u00a77\u4f60\u559c欢\u8fd1\u6218\u4e0e\u8fdc\u7a0b\u7684\u6df7\u5408.");
        lore.add("\u00a77\u4f60\u662f\u65b9\u5757\u6316\u6398\u9ad8\u624b.");
        lore.add("\u00a77\u4f60\u662f\u4e00\u540d\u6cd5\u5e08.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 0;
    }

    @Override
    public int energyMelee() {
        return 12;
    }

    @Override
    public int energyBow() {
        return 8;
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType()!=Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer)gamePlayer));
            }
        });
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return secondSkill.getSkillTip(gamePlayer)+" "+collectSkill.getSkillTip(gamePlayer);
    }
    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            gold.put(killer, gold.get(killer) + 2);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (!e.isCancelled() && e.getEntity().getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) e.getEntity().getShooter()).getUniqueId());
            Classes classes = ClassesManager.getSelected(shooter);
            if (classes.equals(this) && e.getEntity().getType() == EntityType.ARROW && skill2.getOrDefault(shooter, false)) {
                if (shooter.getEnergy()>=(gold.getOrDefault(shooter,0)>=10? 20 : 25)){
                    shooter.setEnergy(shooter.getEnergy()-(gold.getOrDefault(shooter,0)>=10? 20 : 25));//二技能弓能量
                    e.getEntity().setMetadata("fired", MegaWalls.getFixedMetadataValue());
                }
            }
        }

    }
    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this) && (e.getAction() == Action.LEFT_CLICK_BLOCK||e.getAction() == Action.LEFT_CLICK_AIR) && e.getItem().getType() == Material.BOW) {
            boolean skillState = skill2.getOrDefault(gamePlayer, false);
            if (skillState) {
                skill2.put(gamePlayer, false);
                gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.CLICK, 1, 1);
            } else {
                skill2.put(gamePlayer, true);
                gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.CLICK, 1, 1);
            }
        }
    }
    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            GamePlayer shooter = GamePlayer.get(((Player)((Arrow)e.getDamager()).getShooter()).getUniqueId());
            Classes classes = ClassesManager.getSelected(shooter);
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            if (entity.isSpectator() || shooter.getGameTeam().isInTeam(entity)) {
                return;
            }
            if (classes.equals(this)) {
                if (e.getDamager().hasMetadata("fired")){
                    entity.getPlayer().setFireTicks(100);
                    PlayerUtils.heal(shooter.getPlayer(),4.5);
                    if (shooter.getPlayer().hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                        shooter.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    }
                    shooter.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, gold.getOrDefault(shooter,0)>=15? 100 : 80, 0));

                }
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof SmallFireball) {
            SmallFireball smallFireball = (SmallFireball) e.getDamager();
            Player shooter1 = (Player)smallFireball.getShooter();
            GamePlayer shooter = GamePlayer.get(shooter1.getUniqueId());
            assert shooter != null;
            Classes classes = ClassesManager.getSelected(shooter);
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            assert entity != null;
            if (entity.isSpectator() || shooter.getGameTeam().isInTeam(entity)) {
                return;
            }
            if (classes.equals(this)) {
                PlayerUtils.realDamage(entity.getPlayer(), shooter1, 0.25);
                Map<Player, Integer> counts = hitCounts.computeIfAbsent(entity.getPlayer(), k -> new HashMap<>());
                counts.put(shooter.getPlayer(), counts.getOrDefault(shooter.getPlayer(), 0) + 1);
                if (counts.get(shooter.getPlayer()) >= 10) {
                    entity.getPlayer().setFireTicks(100);
                    PlayerUtils.heal(shooter1.getPlayer(),4.5);
                    counts.put(shooter.getPlayer(), 0);
                }
            }
        }
    }
}

