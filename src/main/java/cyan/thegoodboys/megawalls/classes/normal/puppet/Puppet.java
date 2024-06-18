/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.EventHandler
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.normal.puppet;

import cyan.thegoodboys.megawalls.api.event.PlayerKillEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puppet extends Classes {
    public static final Map<GamePlayer, Integer> skillCooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<GamePlayer, Integer>();

    public Puppet() {
        super("Puppet", "铁傀儡", "铁傀儡", ChatColor.GRAY, Material.IRON_CHESTPLATE, (byte) 0, ClassesType.NORMAL, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.TANK, ClassesInfo.Orientation.WARRIOR}, ClassesInfo.Difficulty.ONE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 3).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 3).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 1).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Potion of Regen III (0:12秒)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 240, 2)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 3).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 1).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Potion of Regen III (0:12秒)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 240, 2)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 3).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 1).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Potion of Regen III (0:12秒)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 240, 2)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DURABILITY, 3).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Chestplate").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 1).setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Potion of Regen III (0:12s)").addPotion(new PotionEffect(PotionEffectType.REGENERATION, 240, 2)).build());
                        ItemStack item = new ItemStack(Material.POTION);
                        item.setAmount(2);
                        item.setDurability((byte) 2);
                        PotionMeta im = (PotionMeta) item.getItemMeta();
                        im.setDisplayName(Puppet.this.nameColor + Puppet.this.getDisplayName() + " Splash Potion of Slow I (0:05s)");
                        im.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0), true);
                        im.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 3), true);
                        item.setItemMeta(im);
                        Potion pot = new Potion(1);
                        pot.setSplash(true);
                        pot.apply(item);
                        items.add(item);

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
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 Axe");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 效率 I");
                        lore.add(" §8▪ §7Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" §a+ §7铁 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 保护 I");
                        lore.add(" §8▪ §7铁 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §a+ §7Potion of Regen III §8x2");
                        lore.add("    §8▪ 0:12");
                        lore.add(" §8▪ §7Steak §8x1 ➜ §ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7Steak §8x2 ➜ §ax3");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7钻石 Chestplate");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                        lore.add(" §8▪ §7钻石 Boots");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §a+ 保护 I");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你喜欢近战.");
        lore.add("§7无论如何,你想生存.");
        lore.add("§7你想要2件钻石装备.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 5000;
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
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.collectSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skillCooldown.getOrDefault(gamePlayer, 0) > 0) {
                skillCooldown.put((GamePlayer) gamePlayer, skillCooldown.get(gamePlayer) - 1);
            }
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2Cooldown.put((GamePlayer) gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
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
            this.thirdSkill.use(killer, killer.getPlayerStats().getKitStats(classes));
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        ThrownPotion potion = e.getPotion();
        if (potion.getShooter() instanceof Player) {
            Player player = (Player) potion.getShooter();
            GamePlayer gp = GamePlayer.get(player.getUniqueId());
            if (gp != null) {
                Classes classes = ClassesManager.getSelected(gp);
                for (LivingEntity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player) {
                        Player affectedPlayer = (Player) entity;
                        GamePlayer affectedGamePlayer = GamePlayer.get(affectedPlayer.getUniqueId());
                        if (classes.equals(this) && gp.getGameTeam().isInTeam(affectedGamePlayer)) {
                            e.setIntensity(entity, 0.0);
                            affectedPlayer.sendMessage("§a你免疫了自己傀儡的药水伤害！");
                        }
                    }
                }
            }
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
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            if (player != null && (player.isSpectator() || player.getGameTeam().isInTeam(shooter))) {
                return;
            }
            if (player != null) {
                Classes classes = ClassesManager.getSelected(player);
                if (classes.equals(this)) {
                    this.secondSkill.use(player, player.getPlayerStats().getKitStats(classes));
                }
            }
        }
    }

    private List<Player> getNearbyPlayers(Player player) {
        ArrayList<Player> players = new ArrayList<Player>();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        for (Player other : PlayerUtils.getNearbyPlayers((Entity) player, (double) 3)) {
            GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
            if (gamePlayer != null && gameOther != null && (gameOther.isSpectator() || gamePlayer.getGameTeam().isInTeam(gameOther) || other.getLocation().distance(player.getLocation()) > (double) 3))
                continue;
            players.add(other);
        }
        return players;
    }
}

