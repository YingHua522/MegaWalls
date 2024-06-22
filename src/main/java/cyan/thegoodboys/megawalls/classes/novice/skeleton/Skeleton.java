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
package cyan.thegoodboys.megawalls.classes.novice.skeleton;

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

public class Skeleton extends Classes {
    public static final Map<GamePlayer, Integer> speedCooldown = new HashMap<GamePlayer, Integer>();

    public Skeleton() {
        super("Skeleton", "骷髅", "骷髅", ChatColor.AQUA, Material.BONE, (byte) 0, ClassesType.NOVICE, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.REMOTE, ClassesInfo.Orientation.AGILITY}, ClassesInfo.Difficulty.THREE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("eyJ0aW1lc3RhbXAiOjE1NDkyMDIyNjExMTUsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUxNmQwNDVlNzNlYmM1N2RhNDY2YTJmN2I0Mjg4MTU5MWI1OTkwMDEwNmEyOGY4ZmZhMGNiZGFhOGE5MTQ5In19fQ==", "UpX2geR+fVfuG5MTLf5ttJQVUJebckJSxtJk76l3iwtV3ceIPGhFqF1RPC27WY9UmZJmFZde3v8E1bw66Tt7Au5Ol4X/D2YC+tl1gxkLSJUNaG12O03x/od5uohux1OPtgof21UIVgyewh6C5OAVegmRHFnx7obeWyuTboymbIkwIsffkVx0sbzgNaFQkcrSVUQYa4OTZ5Bp4zLpDeRGG9XjlCmurnaGOPHJT8N298TD1qAHEHYAuMZQIEze4RHzux01DH8b8lVA0HW5mN7OS4QjGAMGeUhOLYKptCo2bkem+u0Rc6RxC2h2oPA1yUCf95hG8QUAy/b9qn2Y4B+EnUBwRZjJrojtX7n/QSThGlCegeASyZdehcrUnE5/xAcGhwQh68ympbVQmY4YmxzZfvGe2kxOI8fHFMmrkGQLFXwu+6ceeE9y09gjbujK3VM4hIvWYaBBfFWIX4VIJsrTVT1oIGmpV/jjZbcKXWAOFkG+j87hgqZjBXAPMJZ6uLJS5D/BBR83JjU5a8w0eX1pnMBXwTZae7hd27IQVEoePXnr9Zw4HNsxlpvU5Bclv7aR5mX1OtDH7LI6ktWlYw1v6+B7+X8iAG76zcYHQUQXyyWseZAeKu6KgbCLTCdFKcPH5/ksIRmfENQq/9hItRA3UZCdSgAUTWtV3U9xQkTWT+c=");
        this.setPrestigeSkin("","");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 0).build());
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.ARROW, 30).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Arrow").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.ARROW, 45).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.ARROW, 60).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.IRON_HELMET).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 3).build());
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_AXE).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Axe").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DIG_SPEED, 1).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Heal (8\u2764)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Helmet").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Skeleton.this.nameColor + Skeleton.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                    }
                }
                return items;
            }

            @Override
            public List<String> getInfo(int level) {
                ArrayList<String> lore = new ArrayList<String>();
                switch (level) {
                    case 1: {
                        lore.add(" \u00a78\u25aa \u00a77Bow");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u529b\u91cf I");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Sword");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 Axe");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u6548\u7387 I");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x2");
                        lore.add(" \u00a78\u25aa \u00a77Arrow \u00a78x30");
                        break;
                    }
                    case 2: {
                        lore.add(" \u00a78\u25aa \u00a77Bow");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u529b\u91cf I \u279c \u00a7aII");
                        lore.add(" \u00a7a+ \u00a77Potion of Heal 8\u2764");
                        lore.add(" \u00a78\u25aa \u00a77Steak \u00a78x2 \u279c \u00a7ax3");
                        lore.add(" \u00a78\u25aa \u00a77Arrow \u00a78x30 \u279c \u00a7ax45");
                        break;
                    }
                    case 3: {
                        lore.add(" \u00a7a+ \u00a77\u94c1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u5f39\u5c04\u7269\u4fdd\u62a4 II");
                        lore.add(" \u00a7a+ \u00a77Potion of Speed II \u00a78x2");
                        lore.add("    \u00a78\u25aa 0:15");
                        lore.add(" \u00a78\u25aa \u00a77Arrow \u00a78x45 \u279c \u00a7ax60");
                        break;
                    }
                    case 4: {
                        lore.add(" \u00a78\u25aa \u00a77Bow");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u529b\u91cf II \u279c \u00a7aIII");
                        lore.add(" \u00a7a+ \u00a77\u94c1 Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u5f39\u5c04\u7269\u4fdd\u62a4 II \u279c \u00a7aIII");
                        lore.add(" \u00a78\u25aa \u00a77Potion of Heal 8\u2764 \u00a78x1 \u279c \u00a7ax2");
                        lore.add(" \u00a78\u25aa \u00a77Arrow \u00a78x60 \u279c \u00a7ax64");
                        break;
                    }
                    case 5: {
                        lore.add(" \u00a78\u25aa \u00a77\u94c1 \u00a78\u279c \u00a7a\u94bb\u77f3 \u00a77Helmet");
                        lore.add("    \u00a78\u25aa \u8010\u4e45 X");
                        lore.add("    \u00a78\u25aa \u5f39\u5c04\u7269\u4fdd\u62a4 III");
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
        return 0;
    }

    @Override
    public int energyBow() {
        return 20;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (speedCooldown.getOrDefault(gamePlayer, 0) > 0) {
                speedCooldown.put(gamePlayer, speedCooldown.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip((GamePlayer) gamePlayer));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            if (shooter != null && (shooter.isSpectator() || shooter.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId())))) {
                return;
            }
            if (shooter != null) {
                Classes classes = ClassesManager.getSelected(shooter);
                if (classes.equals(this)) {
                    this.secondSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
                    this.thirdSkill.use(shooter, shooter.getPlayerStats().getKitStats(classes));
                    startAddEnergyTimer(shooter, 20, 1);
                }
            }
        }
    }

}

