/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.blaze;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.classes.mythic.snowman.Snowman;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Blaze extends Classes {
    public static Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> skill4Cooldown = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer,Integer> damage = new HashMap<>();

    public Blaze() {
        super("Blaze", "烈焰人", "烈焰人", ChatColor.RED, Material.BLAZE_POWDER, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.REMOTE, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 32).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.ARROW, 16).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Blaze.this.nameColor + Blaze.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<>();
                switch (level) {
                    case 1: {
                        lore.add(" §8▪ §7铁 Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 力量 I");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 火焰保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Arrow §8x32");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add("    §8▪ 火焰保护 I");
                        lore.add("    §a+ 弹射物保护 I");
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Arrow §8x32 ➜ §ax56");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I ➜ §aII");
                        lore.add("    §8▪ 火焰保护 I ➜ §aII");
                        lore.add("    §8▪ 弹射物保护 I");
                        lore.add(" §8▪ §7Potion of Speed §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Arrow §8x56 ➜ §ax64");
                        lore.add(" §8▪ §7Arrow §8x56 ➜ §ax16");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7Bow");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 力量 I ➜ §aII");
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II");
                        lore.add("    §8▪ 火焰保护 II");
                        lore.add("    §8▪ 弹射物保护 I ➜ §aII");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Arrow §8x64 ➜ §ax40");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 Leggings");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 II ➜ §aIII");
                        lore.add("    §8▪ 火焰保护 II");
                        lore.add("    §8▪ 弹射物保护 II");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你的献祭之殇高于一切.");
        lore.add("§7你喜欢燃着的东西,就比如,敌人.");
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
        return 4;
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (skill4Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill4Cooldown.put(gamePlayer, skill4Cooldown.get(gamePlayer) - 1);
            }
//            if (gamePlayer.getEnergy() >= 24) {
//                gamePlayer.addEnergy(4, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
//            }
            if (!MegaWalls.getInstance().getGame().isDeathMatch() && MegaWalls.getInstance().getGame().isWallsFall()) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (MegaWalls.getInstance().getGame().isDeathMatch() && gamePlayer.getEnergy() >= 0 && Blaze.damage.getOrDefault(gamePlayer, 0) >= 2) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            this.secondSkill.use(killer, killer.getPlayerStats().getKitStats(classes));
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        GamePlayer player;
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
            Classes classes2 = null;
            if (damager != null) {
                classes2 = ClassesManager.getSelected(damager);
            }
            if (classes2 != null && classes2.equals(this)) {
                int attackCount = ThirdSkill.meleeAttackCount.getOrDefault(damager, 0);
                // 增加近战攻击次数
                attackCount++;
                ThirdSkill.meleeAttackCount.put(damager, attackCount);
                // 更新上次攻击的时间
                ThirdSkill.lastAttackTime.put(damager, System.currentTimeMillis());
                // 如果近战攻击次数达到8，对敌人施加燃烧效果，并给玩家添加生命恢复效果
                if (attackCount >= 8) {
                    Player player2 = damager.getPlayer();
                    if (player2.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        player2.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                    player2.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0)); // 生命恢复15秒
                    e.getEntity().setFireTicks(60); // 燃烧3秒
                    // 重置近战攻击次数
                    ThirdSkill.meleeAttackCount.put(damager, 0);
                }
                // 增加玩家受到的伤害次数
                if (Blaze.damage.getOrDefault(damager, 0) < 2) {
                    Blaze.damage.put(damager, Blaze.damage.getOrDefault(damager, 0) + 1);
                }
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ClassesManager.getSelected(Objects.requireNonNull(player = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId()))).equals(this)) {
            Player shooter = player.getPlayer();
            GamePlayer gp = GamePlayer.get(shooter.getUniqueId());
            if (gp != null) {
                Classes classes = ClassesManager.getSelected(gp);
                if (classes.equals(this)) {
                    if (Blaze.damage.getOrDefault(gp, 0) < 2) {
                        Blaze.damage.put(gp, Blaze.damage.getOrDefault(gp, 0) + 1);
                    }
                    int attackCount = ThirdSkill.meleeAttackCount.getOrDefault(player, 0);
                    // 弓箭命中敌人，将近战攻击次数增加2
                    ThirdSkill.meleeAttackCount.put(player, attackCount + 2);
                    if (shooter.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        shooter.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                    shooter.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1)); // 生命恢复5秒
                }
            }
        }
    }

    @EventHandler
    public void hit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof SmallFireball && e.getEntity().getShooter() instanceof Player && e.getEntity().hasMetadata("blaze")) {
            Player p = (Player) e.getEntity().getShooter();
            float damage = (float) 2.6;
            for (Player player : PlayerUtils.getNearbyPlayers(e.getEntity(), 3)) {
                if (Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(GamePlayer.get(p.getUniqueId())))
                    continue;
                ;
                PlayerUtils.realDamage(player, p, damage);
                player.setNoDamageTicks(0);
            }
            e.getEntity().remove();
        }
    }

    @EventHandler
    public void hit2(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof SmallFireball) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity().getShooter() instanceof Player && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(((Player) e.getEntity().getShooter()).getUniqueId()))).equals(this) && e.getEntity().getType() == EntityType.ARROW) {
            e.getEntity().setMetadata("Arrow", MegaWalls.getFixedMetadataValue());
        }
    }
}

