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
package cyan.thegoodboys.megawalls.classes.mythic.snowman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.*;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.nms.SnowmanFriend;
import cyan.thegoodboys.megawalls.util.BlockData;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.Material.*;

public class Snowman extends Classes {
    public static Map<GamePlayer, Integer> hit = new HashMap<>();
    public static Map<GamePlayer, Integer> damage = new HashMap<>();
    public static Map<GamePlayer, Integer> skill2 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> skill3Cooldown = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> skill4Cooldown = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> snowman = new HashMap<>();

    public Snowman() {
        super("Snowman", "Snowman", "SNO", ChatColor.AQUA, Material.SNOW_BALL, (byte) 0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.CHARGER, ClassesInfo.Orientation.CONTROL}, ClassesInfo.Difficulty.FOUR);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 32).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FIRE, 1).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1).build());
                        items.add(new ItemBuilder(Material.ARROW, 64).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.ARROW, 16).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 2).build());
                        items.add(new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FIRE, 2).addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2).build());
                        items.add(new ItemBuilder(Material.ARROW, 56).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Leggings").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build());
                        items.add(new ItemBuilder(Material.DIAMOND_SPADE).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Shovel").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Steak").build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Snowman.this.nameColor + Snowman.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).addPotion(new PotionEffect(PotionEffectType.ABSORPTION, 300, 0)).build());
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
        return 10;
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
    public void run() {
        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            int mode = skill2.getOrDefault(gamePlayer, 0);
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.WITHER)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.WITHER);
            }
            if (skill3Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill3Cooldown.put(gamePlayer, skill3Cooldown.get(gamePlayer) - 1);
            }
            if (mode >= 1) {
                if (gamePlayer.getEnergy() >= 6) {
                    gamePlayer.setEnergy(gamePlayer.getEnergy() - 6);
                    for (Player player : PlayerUtils.getNearbyPlayers(gamePlayer.getPlayer(), 5)) {
                        if (Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).isSpectator() || Objects.requireNonNull(GamePlayer.get(player.getUniqueId())).getGameTeam().isInTeam(gamePlayer))
                            continue;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                        PlayerUtils.heal(gamePlayer.getPlayer(), 1);
                    }

                } else {
                    gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
                    gamePlayer.getPlayer().sendMessage("§c无能量!");
                    skill2.put(gamePlayer, 0);
                }
            }
            if (skill4Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill4Cooldown.put(gamePlayer, skill4Cooldown.get(gamePlayer) - 1);
            }
            if (!MegaWalls.getInstance().getGame().isDeathMatch()) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (MegaWalls.getInstance().getGame().isDeathMatch() && gamePlayer.getEnergy() >= 0 && Snowman.damage.getOrDefault(gamePlayer, 0) >= 2) {
                gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
        });
    }


    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL && event.getEntity().getShooter() instanceof SnowmanFriend) {
            GamePlayer gamePlayer = GamePlayer.get(((SnowmanFriend) event.getEntity().getShooter()).getUniqueID());
            if (gamePlayer != null) {
                Classes classes = ClassesManager.getSelected(gamePlayer);
                if (classes.equals(this)) {
                    gamePlayer.addEnergy(2, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                }
            }
        }
    }


    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.secondSkill.getSkillTip(gamePlayer) + "  " + this.collectSkill.getSkillTip(gamePlayer) + " §bSIMAFRIEND " + "§7" + snowman.getOrDefault(gamePlayer, 0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDanage(EntityDamageByEntityEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity()) || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        final GamePlayer gamePlayer = GamePlayer.get(e.getDamager().getUniqueId());
        final GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
        if (gamePlayer != null && player != null && (player.isSpectator() || player.getGameTeam().isInTeam(gamePlayer))) {
            return;
        }
        if (gamePlayer != null) {
            Classes classes = ClassesManager.getSelected(gamePlayer);
            if (classes != null && classes.equals(this) && Snowman.damage.getOrDefault(gamePlayer, 0) < 2) {
                Snowman.damage.put(gamePlayer, Snowman.damage.getOrDefault(gamePlayer, 0) + 1);
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            final Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
            final GamePlayer gamePlayer2 = GamePlayer.get(shooter.getUniqueId());
            if (gamePlayer2 != null) {
                Classes classes = ClassesManager.getSelected(gamePlayer2);
                if (classes != null && classes.equals(this) && Snowman.damage.getOrDefault(gamePlayer2, 0) < 2) {
                    Snowman.damage.put(gamePlayer2, Snowman.damage.getOrDefault(gamePlayer2, 0) + 1);
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
        if (classes.equals(this) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getItem().getType() == Material.DIAMOND_SPADE || e.getItem().getType() == BOW)) {
            if (skill2.getOrDefault(gamePlayer, 0) <= 0 && gamePlayer.getEnergy() >= 6) {
                skill2.put(gamePlayer, 2);
                gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
            } else if (skill2.getOrDefault(gamePlayer, 0) >= 1) {
                skill2.put(gamePlayer, 0);
                gamePlayer.playSound(Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
        Classes classes = null;
        if (gamePlayer != null) {
            classes = ClassesManager.getSelected(gamePlayer);
        }
        if (classes != null && classes.equals(this)) {
            int mode = skill2.getOrDefault(gamePlayer, 0);
            if (mode >= 1) {
                Map<Block, BlockData> originalBlocks = new HashMap<>(); // 用于存储原来的方块类型和数据值
                for (Block block1 : LocationUtils.getCube(gamePlayer.getPlayer().getLocation(), 3)) {
                    if (MegaWalls.getInstance().getGame().isUnbreakable(block1.getLocation()) || MegaWalls.getInstance().getGame().isProtected(block1.getLocation()) || (block1.getType() == Material.FURNACE || block1.getType() == BURNING_FURNACE || block1.getType() == Material.TRAPPED_CHEST || block1.getType() == Material.AIR) && !gamePlayer.isProtectedBlock(block1) || block1.getType() == Material.BEDROCK)
                        continue;
                    if (block1.getType() == Material.SNOW_BLOCK || block1.getType() == Material.CHEST || block1.getType() == Material.BURNING_FURNACE || block1.getType() == Material.TRAPPED_CHEST || block1.getType() == FURNACE) { // 如果已经是雪块，就跳过
                        continue;
                    }
                    Block block = block1.getLocation().getBlock();
                    BlockData originalBlockData = new BlockData(block.getType(), block.getData()); // 保存原来的方块类型和数据值
                    originalBlocks.put(block, originalBlockData); // 将原来的方块类型和数据值存储在Map中
                    block1.setType(Material.SNOW_BLOCK);
                    spawnSnowParticles(gamePlayer.getPlayer());
                }
                new BukkitRunnable() {
                    public void run() {
                        for (Map.Entry<Block, BlockData> entry : originalBlocks.entrySet()) {
                            Block block = entry.getKey();
                            BlockData originalBlockData = entry.getValue();
                            if (block.getType() == Material.SNOW_BLOCK) { // 只有当方块仍然是雪块时，才将它变回原来的方块类型和数据值
                                block.setType(originalBlockData.getMaterial());
                                block.setData(originalBlockData.getData());
                            }
                        }
                        this.cancel();
                    }
                }.runTaskTimer(MegaWalls.getInstance(), 140L, 0);
            }
        }
    }

    public void spawnSnowParticles(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        for (double x = loc.getX() - 1; x <= loc.getX() + 1; x += 1) {
            for (double y = loc.getY(); y <= loc.getY() + 2; y += 1) {
                for (double z = loc.getZ() - 1; z <= loc.getZ() + 1; z += 1) {
                    world.playEffect(new Location(world, x, y, z), Effect.SNOW_SHOVEL, 0);
                }
            }
        }
    }
}

