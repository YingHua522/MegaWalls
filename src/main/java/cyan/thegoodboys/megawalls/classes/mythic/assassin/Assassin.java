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
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.assassin;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Assassin extends Classes {
    public static final List<GamePlayer> skill = new ArrayList<>();
    public static final Map<GamePlayer, Integer> skillTimer = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill2cd = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill3cd = new HashMap<>();
    public static Map<GamePlayer, Integer> hit = new HashMap<>();
    public static Map<GamePlayer, Boolean> AssassinNameTag = new HashMap<>();

    public Assassin() {
        super("Assassin", "刺客", "刺客", ChatColor.DARK_RED, Material.STAINED_GLASS, (byte) 15, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.HURT, ClassesInfo.Orientation.AGILITY}, ClassesInfo.Difficulty.FOUR);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 5, (byte) 4).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Potion of Heal III and Speed II(0:06\u79d2)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 2)).addPotion(new PotionEffect(PotionEffectType.SPEED, 200, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 5, (byte) 4).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Potion of Heal III and Speed II(0:06\u79d2)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 2)).addPotion(new PotionEffect(PotionEffectType.SPEED, 200, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 5, (byte) 4).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Potion of Heal III and Speed II(0:06\u79d2)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 2)).addPotion(new PotionEffect(PotionEffectType.SPEED, 200, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 5, (byte) 4).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Potion of Heal III and Speed II(0:06\u79d2)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 2)).addPotion(new PotionEffect(PotionEffectType.SPEED, 200, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 5, (byte) 4).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Potion of Heal III and Speed II(0:06\u79d2)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 2)).addPotion(new PotionEffect(PotionEffectType.SPEED, 200, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Assassin.this.nameColor + Assassin.this.getDisplayName() + " Steak").build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int var1) {
                ArrayList<String> lore = new ArrayList<>();
                switch (var1) {
                    case 1: {
                        lore.add("§8• §7铁剑");
                        lore.add(" §8• 耐久 X");
                        lore.add("§8• §7铁胸甲");
                        lore.add(" §8• 耐久 X");
                        lore.add("§8• §7治疗药水 6♥");
                        lore.add("§8• §7速度药水 II 和 跳跃提升 V");
                        lore.add(" §8• 0:15");
                        lore.add("§8• §7牛排");
                        break;
                    }
                    case 2: {
                        lore.add("§8• §7铁胸甲");
                        lore.add("    §8• 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add("§8• §7牛排 §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add("§8• §7铁胸甲");
                        lore.add("    §8• 耐久 X");
                        lore.add("    §8• 保护 I ➜ §aII");
                        lore.add("§8• §7治疗药水 6♥ ➜ §a8♥");
                        lore.add("§8• §7速度药水 II 和 跳跃提升 V §8x1 ➜ §ax2");
                        lore.add("    §8• 0:15");
                        lore.add("§8• §7牛排 §8x2 ➜ §ax3");
                        break;
                    }
                    case 4: {
                        lore.add("§8• §7铁 ➜ §a钻石 §7剑");
                        lore.add("    §8• 耐久 X");
                        lore.add("§8• §7铁胸甲");
                        lore.add("    §8• 耐久 X");
                        lore.add("    §8• 保护 II ➜ §aIII");
                        lore.add("§8• §7牛排 §8x3 ➜ §ax4");
                        break;
                    }
                    case 5: {
                        lore.add("§8• §7铁 ➜ §a钻石 §7胸甲");
                        lore.add("    §8• 耐久 X");
                        lore.add("§8• §7速度药水 II 和 跳跃提升 V §8x2 ➜ §ax3");
                        lore.add("    §8• 0:15");
                        lore.add("§8• §7牛排 §8x4 ➜ §ax5");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想要有技能来保证你持续点击左键.");
        lore.add("§7你想要冲所有队伍的凋灵.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 5;
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
        return this.secondSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (!MegaWalls.getInstance().getGame().isDeathMatch()) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (MegaWalls.getInstance().getGame().isDeathMatch() && gamePlayer.getEnergy() >= 0 && hit.getOrDefault(gamePlayer, 0) >= 2) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (skillTimer.getOrDefault(gamePlayer, 0) > 0) {
                skillTimer.put(gamePlayer, skillTimer.get(gamePlayer) - 1);
            }
            if (skill2cd.getOrDefault(gamePlayer, 0) > 0) {
                skill2cd.put(gamePlayer, skill2cd.get(gamePlayer) - 1);
            }
            if (skill2.getOrDefault(gamePlayer, 0) > 0) {
                skill2.put(gamePlayer, skill2.get(gamePlayer) - 1);
            }
            if (skill3cd.getOrDefault(gamePlayer, 0) > 0) {
                skill3cd.put(gamePlayer, skill3cd.get(gamePlayer) - 1);
            }
            gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
        });
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
        if (e.getEntity() instanceof Player && (classes = ClassesManager.getSelected(Objects.requireNonNull(player = GamePlayer.get(e.getEntity().getUniqueId())))).equals(this)) {
            int heal = player.getDef();
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                if (player.getDef() - heal >= 9 && Assassin.skill3cd.getOrDefault(player, 0) == 0) {
                    Assassin.this.thirdSkill.use(player, player.getPlayerStats().getKitStats(classes));
                }
            }, 20L);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(damager))) {
                return;
            }
            if (player != null && player.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            assert damager != null;

            Classes classes = ClassesManager.getSelected(damager);
            Classes classes1 = ClassesManager.getSelected(player);
            if (classes.equals(this)) {
                if (skill.contains(damager)) {
                    Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                        Assassin.skill.remove(damager);
                        Assassin.skillTimer.put(damager, 0);
                        PlayerUtils.realDamage(player.getPlayer(), damager.getPlayer(), 2);
                        PlayerUtils.refresh(damager.getPlayer());
                        Game game = MegaWalls.getInstance().getGame();
                        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), new Runnable() {
                            public void run() {
                                game.registerScoreboardTeams();
                            }
                        }, 1L);
                        damager.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                        damager.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        damager.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                    }, 1L);
                }
            } else if (classes1.equals(this) && Assassin.skill2.getOrDefault(player, 0) == 0 && player.getPlayer().isSneaking()) {
                e.setCancelled(true);
                Location loc = damager.getPlayer().getLocation();
                Vector vec = loc.getDirection();
                vec.multiply(-0.7);
                loc.add(vec.getX(), -1, vec.getZ());
                Location loc2 = loc.add(0, 1, 0);
                if (!loc.getBlock().isEmpty() && !loc2.getBlock().isEmpty() && !loc.getBlock().isLiquid() && !loc2.getBlock().isLiquid()) {
                    player.getPlayer().teleport(damager.getPlayer());
                } else {
                    player.getPlayer().teleport(loc);
                }
                player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                Assassin.skill2.put(player, 10);
            }
            if (classes.equals(this) && hit.getOrDefault(damager, 0) < 2) {
                hit.put(damager, hit.getOrDefault(damager, 0) + 1);
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            Classes classes2 = null;
            if (shooter != null) {
                classes2 = ClassesManager.getSelected(shooter);
            }
            assert shooter != null;
            if (shooter.isSpectator() || shooter.getGameTeam().isInTeam(player)) {
                return;
            }
            if (classes2.equals(this) && hit.getOrDefault(shooter, 0) < 2) {
                hit.put(shooter, hit.getOrDefault(shooter, 0) + 1);
            }
            Classes classes = null;
            if (player != null) {
                classes = ClassesManager.getSelected(player);
            }
            if (classes != null && classes.equals(this) && shooter.getPlayer().getLocation().distance(player.getPlayer().getLocation()) < 21 && Assassin.skill2cd.getOrDefault(player, 0) == 0) {
                e.setCancelled(true);
                player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
                player.getPlayer().getInventory().addItem(new ItemBuilder(Material.ARROW, 2).build());
                player.sendMessage("§a你接住了敌方射来的箭矢。");
                skill2cd.put(player, 5);
                if (player.getPlayer().isSneaking()) {
                    Location loc = shooter.getPlayer().getLocation();
                    Vector vec = loc.getDirection();
                    vec.multiply(-0.7);
                    loc.add(vec.getX(), -1, vec.getZ());
                    Location loc2 = loc.add(0, 1, 0);
                    if (!loc.getBlock().isEmpty() && !loc2.getBlock().isEmpty() && !loc.getBlock().isLiquid() && !loc2.getBlock().isLiquid()) {
                        player.getPlayer().teleport(shooter.getPlayer());
                    } else {
                        player.getPlayer().teleport(loc);
                    }
                    player.getPlayer().getWorld().playSound(player.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    Assassin.skill2cd.put(player, 10);
                }
            }

        }
    }
}


