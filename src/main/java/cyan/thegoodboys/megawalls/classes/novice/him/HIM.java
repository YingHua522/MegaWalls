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
package cyan.thegoodboys.megawalls.classes.novice.him;

import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LightningStrike;
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

public class HIM extends Classes {
    public static final Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static final Map<GamePlayer, Integer> skillCooldown = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill3 = new HashMap<>();

    public HIM() {
        super("HIM", "Herobrine", "HBR", ChatColor.YELLOW, Material.DIAMOND_SWORD, (byte) 0, ClassesType.NOVICE, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.WARRIOR, ClassesInfo.Orientation.HURT}, ClassesInfo.Difficulty.ONE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1MzMzNDI3NTE3MTksInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM2NWVkMjgyOWM4M2UxMTlhODBkZmIyMjIxNjQ0M2U4NzhlZjEwNjQ5YzRhMzU0Zjc0YmY0NWFkMDZiYzFhNyJ9fX0=", "gvRz+z/AM/p9iJeifKTCEjIVoQbQq0OHPstiFFdX9cE6uxw+C0X0JuwoxHVEUkvKlzPQJGVPLh5VSQY4NL3wCEkyOwoN3FDWIewjzhwtmyM4BlzbHDRT6C/4ICFw3azLZi6f7EkPNcL9O8yks+ebyLxXEgJEFSmY7nMsjdRuLOVu7X9UoSbKLwNR8Rua9LEKtdhpVcQx+rLD9T4VRZctfhunPDVnXfQTqvq4gC1lb6nQPIwSnVCdH5eY4bnOO1n/vV7enOO0mMgjtQLxFFM1OKoBRDxh70pNgAmVUxunUA1xrfG+pZF5HM6nMh1FDKD7NTNrZ7O4EWpawT2Q8+EVLgdcMYNMlIW1xb4pOMceDpaAlOw3LOfROGK6cz4OpevuXU3WaXb97cuq8B0SrlVJI7xnL3sgEzyf+1MIQ6O0NBo7SAa0vVXEVu+Y65bawZh0HYpwngkTkMJR8wulNQ2HcKzSClZKsqP7M/qneHCJ4lnQsGFXj5LEp1EkJZ7WGTcIo5L4Fo7hl0FiGtTdesBEBc/OQOB2dIZfOZFInRaTMALuaB3VrAg+atYtTeU9twl0qmrq/0L5FbszkTh6xqpnGmlNWuRx1yKuG8+lD3f0sejHIPXC/Akw3YQjWFUTs3e8h18zD6mtZxu4CKFc6hVsnMsGr0tEIa/eASkUGJSNy0s=");
        this.setEquipmentPackage(new EquipmentPackage(this) {
            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(HIM.this.nameColor + HIM.this.getDisplayName() + "Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.WATER_WORKER, 1).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
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
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax3");
                        break;
                    }
                    case 3: {
                        lore.add(" §a+ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §a+ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Potion of Speed II §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 水下速掘 I");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Potion of Speed II §8x2 ➜ §ax3");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Helmet");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 水下速掘 I");
                        lore.add("    §8▪ 保护 I §8➜ §aII");
                        lore.add("    §a+ 弹射保护 I");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Potion of Speed II §8x2 ➜ §ax3");
                        lore.add("    §8▪ 0:15");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        return new ArrayList<>();
    }

    @Override
    public int unlockCost() {
        return 0;
    }

    @Override
    public int energyMelee() {
        return 25;
    }

    @Override
    public int energyBow() {
        return 25;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        StringBuilder sb = new StringBuilder(this.mainSkill.getSkillTip(gamePlayer));
        if (skill2.getOrDefault(gamePlayer, 0) > 0) {
            sb.append(" ").append(this.secondSkill.getSkillTip(gamePlayer));
        }
        sb.append(" ").append(this.thirdSkill.getSkillTip(gamePlayer));
        return sb.toString();
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill2.getOrDefault(gamePlayer, 0) > 0) {
                skill2.put(gamePlayer, skill2.get(gamePlayer) - 1);
            }
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put(gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            this.secondSkill.use(killer, killer.getPlayerStats().getKitStats(classes));
        }
    }

    @EventHandler
    public void hit2(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LightningStrike) {
            e.setCancelled(true);
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
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            assert damager != null;
            if (damager.isSpectator() || damager.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                return;
            }
            if (entity != null && entity.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            Classes classes = ClassesManager.getSelected(damager);
            if (classes.equals(this)) {
                this.thirdSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
                if (skill2.getOrDefault(damager, 0) > 0) {
                    e.setDamage(e.getDamage() * 1.95);
                }
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            assert shooter != null;
            if (shooter.isSpectator() || shooter.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                return;
            }
            Classes classes = ClassesManager.getSelected(shooter);
            if (classes.equals(this)) {
                this.thirdSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
            }
        }
    }
}

