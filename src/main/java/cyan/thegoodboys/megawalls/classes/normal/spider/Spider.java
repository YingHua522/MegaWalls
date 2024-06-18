/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.spider;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spider extends Classes {
    public static Map<GamePlayer, Integer> skill2 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> hit = new HashMap<>();
    public static Map<GamePlayer, Integer> mode = new HashMap<>();

    public Spider() {
        super("Spider", "蜘蛛", "蜘蛛", ChatColor.DARK_PURPLE, Material.WEB, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.AGILITY, ClassesInfo.Orientation.CHARGER}, ClassesInfo.Difficulty.THREE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 4).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Shovel").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Spider.this.nameColor + Spider.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
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
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Boots");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Spade");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I");
                        lore.add(" \u00a78\u25aa \u00a77Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Boots");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u4fdd\u62a4 I");
                        lore.add(" \u00a7a+ \u00a77Potion of Speed II");
                        lore.add("    \u00a78\u25aa 0:15");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x1 \u279c \u00a7ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Boots");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u279c \u00a7aII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Speed \u00a78x1 \u279c \u00a7ax2");
                        lore.add("    \u00a78\u25aa 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Boots");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764 \u00a78x1 \u279c \u00a7ax2");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x2 \u279c \u00a7ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Sword");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u8282\u80a2\u6740\u624b IV");
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Boots");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a7a+ \u4fdd\u62a4 I");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a77\u4f60\u559c\u6b22\u4f0f\u51fb\u843d\u5355\u7684\u654c\u4eba.");
        lore.add("\u00a77\u4f60\u559c\u6b22\u5728\u9ad8\u7a7a\u4f5c\u6218.");
        lore.add("\u00a77\u4f60\u4e0d\u6015\u8718\u86db.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 15000;
    }

    @Override
    public int energyMelee() {
        return 8;
    }

    @Override
    public int energyBow() {
        return 8;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        String second = this.secondSkill.getSkillTip(gamePlayer);
        return this.mainSkill.getSkillTip(gamePlayer) + (second == null ? "" : " " + second);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (MegaWalls.getInstance().getGame().isWallsFall() && !MegaWalls.getInstance().getGame().isDeathMatch()) {
                gamePlayer.addEnergy(4, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (MegaWalls.getInstance().getGame().isWallsFall() && MegaWalls.getInstance().getGame().isDeathMatch() && gamePlayer.getEnergy() >= 0 && hit.getOrDefault(gamePlayer, 0) >= 2) {
                gamePlayer.addEnergy(4, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player) || e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this)) {
            if (skill2.getOrDefault(gamePlayer, 0) == 0) {
                return;
            }
            Player player = (Player) e.getEntity();
            skill2.put(gamePlayer, skill2.get(gamePlayer) - 1);
            ParticleEffect.HEART.display(0.0f, 0.0f, 0.0f, 1.0f, 3, player.getLocation(), 10.0);
            player.getWorld().playSound(player.getLocation(), Sound.SPIDER_IDLE, 1.0f, 0.0f);
            PlayerUtils.heal(player, (double) skill2.getOrDefault(gamePlayer, 0) * 1.5);
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity()) || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        final GamePlayer gamePlayer = GamePlayer.get(e.getDamager().getUniqueId());
        final GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
        assert player != null;
        if (player.isSpectator() || player.getGameTeam().isInTeam(gamePlayer)) {
            return;
        }
        if (gamePlayer != null) {
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (player.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            if (classes != null && classes.equals(this)) {
                final int times = skill2.getOrDefault(gamePlayer, 0);
                if (times == 0) {
                    return;
                }
                skill2.put(gamePlayer, skill2.get(gamePlayer) - 1);
                e.setDamage(e.getDamage() + e.getDamage() * 0.15);
            }
            if (classes != null && classes.equals(this)) {
                secondSkill.use(player, gamePlayer.getPlayerStats().getKitStats(classes));
            }
            if (classes != null && classes.equals(this) && hit.getOrDefault(gamePlayer, 0) < 2) {
                hit.put(gamePlayer, hit.getOrDefault(gamePlayer, 0) + 1);
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            final Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
            final GamePlayer gamePlayer2 = GamePlayer.get(shooter.getUniqueId());
            if (gamePlayer2 != null) {
                Classes classes = ClassesManager.getSelected(gamePlayer2);
                if (classes != null && classes.equals(this) && hit.getOrDefault(gamePlayer2, 0) < 2) {
                    hit.put(gamePlayer2, hit.getOrDefault(gamePlayer2, 0) + 1);
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
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem().getType() == Material.IRON_SPADE) {
            if (mode.getOrDefault(gamePlayer, 0) <= 0) {
                mode.put(gamePlayer, 2);
                player.sendMessage("§aMode:长跳");
            } else if (mode.getOrDefault(gamePlayer, 0) >= 1) {
                mode.put(gamePlayer, 0);
                player.sendMessage("§aMode:普通跳跃");
            }
        }
    }
}

