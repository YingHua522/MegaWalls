/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDamageEvent$DamageModifier
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.novice.enderman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Enderman extends Classes {
    public static final Map<GamePlayer, Integer> skillCooldown = new HashMap<>();
    public static final Map<GamePlayer, Integer> skill3 = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();

    public Enderman() {
        super("Enderman", "末影人", "末影人", ChatColor.DARK_PURPLE, Material.ENDER_PEARL, (byte) 0, ClassesType.NOVICE, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.AGILITY, ClassesInfo.Orientation.CHARGER}, ClassesInfo.Difficulty.TWO);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDkxNTU5OTY3MzEsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0ZGIyNzEwNmZhYTYwYzRhOWE0ZDNlMmE0MDVlOGM2ODYzNWJjYzZkNjViMDhjZDI5NzllNjA2MTNkNTMwMiJ9fX0=", "KTYuXTQQ63ew7EksUSwP5XEQtdUkZh7G8ZoX4mBh10fWX0OXJIURP0c7PxW7BiPfh3o5Fo5zSeUIEHcbW9tjvhxmGkjONviLkA13VehIGMIo5h5akdkhEajLJgxJyOJSlLxicFhOoEy4cVDZwmAgwQrqidyH8ipOw9Hm4RbL2mKRX3Q+oEwyc4eDetIGbLTsoeueUq88QvtfISK2f3vDftIN92mznZcP3/k/tT3Y/7CO2iGEmjpMADhrbgxCk7AgjdWhZoz43xTJED631j5JuQbnO7/eaiQXIqpBAWTWFhRX/m3b7W8znbmAemgG5LjfertqFQNseICMmvFzUjDUHbYZ82Nmvu+ii1KoIOhXGQG1mI7ri/oejdXCvg8kAfp0HrNE9GdzvPItqBDC7OCwNzLkOI7T75hXhyABppUdty52GJ2i0GHQl679Vhs5bRhnRjlyPCUvhFDrgLr4cZgXaWmaDiEJ5DjxAiJOV9UDvXOxovb1mQlhoTCytQVhXSeuKLsNlVpoWKgEPRIrDWlqUadpKtsL14+t0p9zg+69FCmpYF1nWLPxKCC3zmFU3ixr3PVIzrUJ5VOBlee7xBx7pVeIuodYKcKduQNLkHjoV8o/gtaj9Bc3ohjVgygcRazrtePmm/ZbOCtrUjyC+pFmDgnIZE0m2KkLkcEdawzB5Zo=");
        this.setPrestigeSkin("","");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Boots").setLore("§5 75%摔落保护").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Boots").setLore("§5 75%摔落保护").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Boots").setLore("§5 75%摔落保护").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Boots").setLore("§5 75%摔落保护").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Boots").setLore("§5 75%摔落保护").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Enderman.this.nameColor + Enderman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<String>();
                switch (level) {
                    case 1: {
                        lore.add(" §8▪ §7Iron Sword");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Iron Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §a+ §7Potion of Heal 8❤");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 4: {
                        lore.add(" §a+ §7Potion of Speed II");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7Iron §8➜ §a钻石 §7Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Potion of Heal 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7Potion of Speed II §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
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
        return 20;
    }

    @Override
    public int energyBow() {
        return 20;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + thirdSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put(gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }

    @EventHandler
    public void onPlayerEnergyChange(PlayerEnergyChangeEvent e) {
        GamePlayer player = e.getPlayer();
        Classes classes = ClassesManager.getSelected(player);
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this) && killer.getGameTeam().isWitherDead()) {
            this.secondSkill.use(killer, killer.getPlayerStats().getKitStats(classes));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final PlayerDeathEvent e) {
        if (MegaWalls.getInstance().getGame().isWaiting()) {
            return;
        }
        final GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
        assert player != null;
        Classes classes = ClassesManager.getSelected(player);
        if (classes.equals(this) && !player.getGameTeam().isWitherDead() && (double) MegaWalls.getRandom().nextInt(300) <= this.secondSkill.getAttribute(player.getPlayerStats().getKitStats(classes).getSkill2Level()) * 100.0) {
            Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
                for (ItemStack itemStack : e.getDrops()) {
                    if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.DIAMOND || itemStack.getType() == Material.IRON_INGOT || ClassesManager.isClassesItem(itemStack))
                        continue;
                    player.getPlayer().getInventory().addItem(itemStack);
                }
            }, 5L);
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
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL && ClassesManager.getSelected(Objects.requireNonNull(GamePlayer.get(e.getEntity().getUniqueId()))).equals(this)) {
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, -e.getDamage() * 0.75);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            if (damager != null && (damager.isSpectator() || damager.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId())))) {
                return;
            }
            Classes classes = null;
            if (damager != null) {
                classes = ClassesManager.getSelected(damager);
            }
            if (classes != null && classes.equals(this)) {
                this.thirdSkill.use(damager, damager.getPlayerStats().getKitStats(classes));
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
                this.thirdSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
            }
        }

    }
}

