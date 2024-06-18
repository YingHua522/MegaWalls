package cyan.thegoodboys.megawalls.classes.mythic.mole;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Mole extends Classes {
    public static Map<GamePlayer, Integer> hit = new HashMap<>();
    public static Map<GamePlayer, Integer> skill2 = new HashMap<>();
    public static Map<GamePlayer, Integer> skillCooldown = new HashMap<>();
    public static Map<GamePlayer, Integer> rec = new HashMap<>();

    public Mole() {
        super("Mole", "鼹鼠", "鼹鼠", ChatColor.YELLOW, Material.GOLD_SPADE, (byte) 0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.AGILITY, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.THREE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.GOLD_HELMET).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Potion of Heal (2\u2764 \u548c \u6cbb\u7597 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 0)).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 1).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.GOLD_HELMET).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Potion of Heal (6\u2764 \u548c \u6cbb\u7597 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
                        items.add(new ItemBuilder(Material.IRON_SPADE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 2).addEnchantment(Enchantment.DAMAGE_ALL, 3).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.GOLD_HELMET).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Potion of Heal (6❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 1)).build());
                        items.add(new ItemBuilder(Material.DIAMOND_SPADE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.GOLD_HELMET).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Potion of Heal (8❤ 和 治疗 I)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.DIAMOND_SPADE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Spade").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.GOLD_HELMET).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Potion of Heal (9❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 3)).build());
                        items.add(new ItemBuilder(Material.DIAMOND_SPADE).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Shovel").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 2).addEnchantment(Enchantment.DAMAGE_ALL, 2).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Mole.this.nameColor + Mole.this.getDisplayName() + " Steak").build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<String>();
                switch (level) {
                    case 1: {
                        lore.add(" \u00a78\u25aa \u00a77\u91d1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Leggings");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 2\u2764 \u548c \u6cbb\u7597 I");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Spade");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I");
                        lore.add("    \u00a78\u25aa \u950b\u5229 III");
                        lore.add(" \u00a78\u25aa \u00a77Steak");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Leggings");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u279c \u00a7aIII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 2\u2764 \u279c \u00a7a6\u2764");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Spade");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I \u279c \u00a7aII");
                        lore.add("    \u00a78\u25aa \u950b\u5229 III");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x1 \u279c \u00a7ax2");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Leggings");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 III \u279c \u00a7aI");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 6\u2764 \u00a78x1 \u279c \u00a7ax2");
                        lore.add(" \u00a7a+ \u00a77\u94bb\u77f3 Spade");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I");
                        lore.add("    \u00a78\u25aa \u950b\u5229 II");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Leggings");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 I \u279c \u00a7aII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 6\u2764 \u279c \u00a7a8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x2 \u279c \u00a7ax3");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Leggings");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u4fdd\u62a4 II \u279c \u00a7aIII");
                        lore.add(" \u00a78\u25aa \u00a77\u94bb\u77f3 Spade");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I \u279c \u00a7aII");
                        lore.add("    \u00a78\u25aa \u950b\u5229 II");
                    }
                }
                return lore;
            }
        });
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("\u00a77\u4f60\u559c\u6b22\u7528Spade\u6218\u6597.");
        lore.add("\u00a77\u4f60\u60f3\u8981\u5b66\u4f1aTNT\u7279\u6280.");
        lore.add("\u00a77\u4f60\u60f3\u5728\u6218\u524d\u51c6\u5907\u597d\u4e00\u5207.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 3;
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
        return this.mainSkill.getSkillTip(gamePlayer) + this.secondSkill.getSkillTip(gamePlayer);
    }


    @Override
    public void run() {
        for (GamePlayer gp : GamePlayer.getOnlinePlayers()) {
            //判定玩家不是旁观者，或这个鼹鼠职业不是该玩家 跳出循环从头执行
            if (gp.isSpectator() || !ClassesManager.getSelected(gp).equals(this)) {
                continue;
            }
            // 如果玩家手持物品不是指南针
            if (gp.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gp.sendActionBar(this.getSkillTip(gp));
            }
            if (!MegaWalls.getInstance().getGame().isDeathMatch()) {
                gp.addEnergy(3, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            //如果玩家技能小于=0  或攻击是否为2次
            if (MegaWalls.getInstance().getGame().isDeathMatch() && gp.getEnergy() >= 0 && hit.getOrDefault(gp, 0) >= 2) {
                gp.addEnergy(3, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (Mole.skillCooldown.getOrDefault(gp, 0) > 0) {
                Mole.skillCooldown.put(gp, skillCooldown.get(gp) - 1);
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
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.APPLE) {
            if (e.getPlayer().getFoodLevel() == 20) {
                e.getPlayer().setFoodLevel(19);
                e.setCancelled(true);
            }
        }
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.PUMPKIN_PIE) {
            if (e.getPlayer().getFoodLevel() == 20) {
                e.getPlayer().setFoodLevel(19);
                e.setCancelled(true);
            }
        }
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem().getType() == Material.COOKIE) {
            if (e.getPlayer().getFoodLevel() == 20) {
                e.getPlayer().setFoodLevel(19);
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        GamePlayer gamePlayer;
        Classes classes;
        if (e.getItem().getType() == Material.APPLE && (classes = ClassesManager.getSelected(Objects.requireNonNull(gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId())))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Junk Apple")) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0));
            this.thirdSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
        }
        if (e.getItem().getType() == Material.PUMPKIN_PIE && ClassesManager.getSelected(Objects.requireNonNull(gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId()))).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Junk Pie")) {
            gamePlayer.addEnergy(7, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
        }
        if (e.getItem().getType() == Material.COOKIE && ClassesManager.getSelected(gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId())).equals(this) && e.getItem().hasItemMeta() && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().contains("Junk Cookie")) {
            gamePlayer.addEnergy(3, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
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
            if (entity.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            Classes classes = ClassesManager.getSelected(damager);
            if (classes.equals(this) && hit.getOrDefault(damager, 0) < 2) {
                hit.put(damager, hit.getOrDefault(damager, 0) + 1);
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            assert shooter != null;
            if (shooter.isSpectator() || shooter.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                return;
            }
            Classes classes = ClassesManager.getSelected(shooter);
            if (classes.equals(this) && hit.getOrDefault(shooter, 0) < 2) {
                hit.put(shooter, hit.getOrDefault(shooter, 0) + 1);
            }
        }
    }
}


