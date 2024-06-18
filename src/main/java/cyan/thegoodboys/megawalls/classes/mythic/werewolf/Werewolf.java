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
package cyan.thegoodboys.megawalls.classes.mythic.werewolf;

import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Werewolf extends Classes {
    public static final List<GamePlayer> skill = new ArrayList<>();
    public static final List<GamePlayer> skillDamage = new ArrayList<>();
    public static final Map<GamePlayer, Integer> skillCooldown = new HashMap<>();
    public static final List<GamePlayer> skillhit = new ArrayList<>();
    public static final Map<GamePlayer, Integer> lastDamageTime = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static final Map<GamePlayer,Integer> skill3 = new HashMap<>();

    public Werewolf() {
        super("Werewolf", "狼人", "狼人", ChatColor.DARK_GREEN, Material.COOKED_BEEF, (byte) 0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.WARRIOR}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Heal (6❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Speed II 和 跳跃 V (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 1).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Heal (6❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Speed II 和 跳跃 V (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Speed II 和 跳跃 V (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Speed II 和 跳跃 V (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 4).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Heal (10❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 3, (byte) 2).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).addPotion(new PotionEffect(PotionEffectType.JUMP, 300, 4)).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 5).setDisplayName(Werewolf.this.nameColor + Werewolf.this.getDisplayName() + " Steak").build());
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
                        lore.add(" §8▪ §7Potion of Heal 6❤");
                        lore.add(" §8▪ §7Potion of Speed II 和 跳跃 V");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 6❤ ➜ §a8❤");
                        lore.add(" §8▪ §7Potion of Speed II 和 跳跃 V §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II ➜ §aIII");
                        lore.add(" §8▪ §7Steak §8x3 ➜ §ax4");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Speed II 和 跳跃 V §8x2 ➜ §ax3");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x4 ➜ §ax5");
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
        lore.add("§7你想要在1v2中取得机会.");
        lore.add("§7有时觉醒,对月长嗥.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 3;
    }

    @Override
    public int energyMelee() {
        return 8;
    }

    @Override
    public int energyBow() {
        return 6;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        if (this.secondSkill.getSkillTip(gamePlayer) == null) {
            return this.mainSkill.getSkillTip(gamePlayer);
        }
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill.contains(gamePlayer)) {
                ParticleEffect.NOTE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, gamePlayer.getPlayer().getLocation().add(0.0, 1.0, 0.0), 10.0);
            }
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put(gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }
            if (Werewolf.skill3.getOrDefault(gamePlayer, 0) > 0) {
                Werewolf.skill3.put(gamePlayer, Werewolf.skill3.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            killer.getPlayer().getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF, e.isFinalKill() ? 4 : 1).build());
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
        assert gamePlayer != null;
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.COOKED_BEEF) {
            if (e.getPlayer().getFoodLevel() == 20) {
                e.setCancelled(true);
                e.getPlayer().setFoodLevel(19);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        GamePlayer gamePlayer;
        Classes classes;
        if (e.getItem().getType() == Material.COOKED_BEEF && (classes = ClassesManager.getSelected(Objects.requireNonNull(gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId())))).equals(this)) {
            if (Werewolf.skill3.getOrDefault(gamePlayer, 0) > 0) {
                return;
            }
            this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
            Werewolf.skill3.put(gamePlayer,5);
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
            if (damager.isSpectator() || damager.getGameTeam().isInTeam(player)) {
                return;
            }
            if (player.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            Classes classes = ClassesManager.getSelected(damager);
            if (classes.equals(this)) {
                if (skill.contains(damager)) {
                    e.setDamage(e.getDamage() + 1);
                    PlayerUtils.heal(damager.getPlayer(), 2);
                    if (!skillhit.contains(player) && skillhit.size() < 7) {
                        skillhit.add(player);
                    }
                }
                this.secondSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            assert shooter != null;
            if (shooter.isSpectator() || shooter.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                return;
            }
            Classes classes = ClassesManager.getSelected(shooter);
            if (classes.equals(this)) {
                this.secondSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume1(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.POTION && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId()))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Potion of Heal")) {
            PlayerUtils.heal(e.getPlayer(), 4);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
        assert gamePlayer != null;
        Classes classes = ClassesManager.getSelected(gamePlayer);
        if (classes.equals(this) && e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            skill2.put(gamePlayer, 0);
            gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
        }
    }
}

