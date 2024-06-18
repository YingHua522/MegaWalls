/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cyan.thegoodboys.megawalls.classes.mythic.lawless;

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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lawless extends Classes {
    public static final Map<GamePlayer, Integer> fall = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> skill2Cooldown = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Integer> Break = new HashMap<GamePlayer, Integer>();
    public static final Map<GamePlayer, Map<GamePlayer, Integer>> arrow = new HashMap<>();
    public static final Map<GamePlayer, Integer> arrowcd = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> killpotion = new HashMap<>();
    public static Map<GamePlayer, Boolean> givestart = new HashMap<>();
    private static Map<GamePlayer, Map<GamePlayer, BukkitTask>> clearHitCountTasks = new HashMap<>();

    public Lawless() {
        super("Lawless", "不法者", "不法者", ChatColor.GOLD, Material.ARROW, (byte) 0, ClassesType.MYTHIC, new ClassesInfo.Orientation[]{ClassesInfo.Orientation.AGILITY, ClassesInfo.Orientation.REMOTE}, ClassesInfo.Difficulty.THREE);
        this.setMainSkill(new MainSkill(this));
        this.setSecondSkill(new SecondSkill(this));
        this.setThirdSkill(new ThirdSkill(this));
        this.setCollectSkill(new FourthSkill(this));
        this.setDefaultSkin("7", "");
        this.setEquipmentPackage(new EquipmentPackage(this) {

            @Override
            public List<ItemStack> getEquipments(int level) {
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                items.add(new ItemBuilder(Material.COMPASS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Compass").build());
                items.add(new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Pickaxe").setUnbreakable(true).addEnchantment(Enchantment.DIG_SPEED, 3).addEnchantment(Enchantment.DURABILITY, 3).build());
                items.add(new ItemBuilder(Material.ENDER_CHEST).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Ender Chest").build());
                switch (level) {
                    case 1: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_FALL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.ARROW, 16).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 1).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 2: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 1).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 24).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 3: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 1, (byte) 5).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 32).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 4: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.IRON_BOOTS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 40).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Steak").build());
                        break;
                    }
                    case 5: {
                        items.add(new ItemBuilder(Material.IRON_SWORD).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Sword").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.DAMAGE_ALL, 1).build());
                        items.add(new ItemBuilder(Material.BOW).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Bow").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
                        items.add(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Boots").setUnbreakable(true).addEnchantment(Enchantment.DURABILITY, 10).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchantment(Enchantment.PROTECTION_FALL, 2).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 5).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Heal (8❤)").addPotion(new PotionEffect(PotionEffectType.HEAL, 1, 2)).build());
                        items.add(new ItemBuilder(Material.POTION, 2, (byte) 2).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Potion of Speed II (0:15s)").addPotion(new PotionEffect(PotionEffectType.SPEED, 300, 1)).build());
                        items.add(new ItemBuilder(Material.ARROW, 48).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Arrow").build());
                        items.add(new ItemBuilder(Material.COOKED_BEEF, 3).setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Steak").build());
                        ItemStack item = new ItemStack(Material.POTION);
                        item.setAmount(5);
                        item.setDurability((byte) 2);
                        PotionMeta im = (PotionMeta) item.getItemMeta();
                        im.setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " Splash Potion of Speed I (0:05s)");
                        im.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 300, 0), true);
                        im.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0), true);
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
                        lore.add(" §8▪ §7铁 剑");
                        lore.add("    §8▪ 锋利 I");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7弓");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7铁 鞋子");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 摔落保护 II");
                        lore.add(" §8▪ §7治疗药水 6❤");
                        lore.add(" §8▪ §7牛排");
                        break;
                    }
                    case 2: {
                        lore.add(" §8▪ §7铁 鞋子");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 摔落保护 §8I ➜ §aII");
                        lore.add(" §8▪ §7牛排 §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7速度药水 II");
                        lore.add("    §8▪ 0:15");
                        break;
                    }
                    case 3: {
                        lore.add(" §8▪ §7治疗药水 6❤ ➜ §a8❤");
                        lore.add(" §8▪ §7速度药水 II §8x1 ➜ §ax2");
                        lore.add("    §8▪ 0:15");
                        lore.add(" §8▪ §7牛排 §8x2 ➜ §ax3");
                        break;
                    }
                    case 4: {
                        lore.add(" §8▪ §7治疗药水 8❤ §8x1 ➜ §ax2");
                        lore.add(" §8▪ §7牛排 §8x3 ➜ §ax4");
                        break;
                    }
                    case 5: {
                        lore.add(" §8▪ §7铁 §8➜ §a钻石 §7鞋子");
                        lore.add("    §8▪ 耐久 X");
                        lore.add(" §8▪ §7弓");
                        lore.add("    §8▪ 耐久 X");
                        lore.add("    §8▪ 力量 I");
                    }
                }
                return lore;
            }
        });
    }

    public static int getHitCount(GamePlayer shooter, GamePlayer hitPlayer) {
        synchronized (arrow) {
            Map<GamePlayer, Integer> playerHitCounts = arrow.get(shooter);
            if (playerHitCounts != null) {
                Integer count = playerHitCounts.get(hitPlayer);
                return count == null ? 0 : count;
            }
            return 0;
        }
    }

    public static void clearHitCount(GamePlayer shooter, GamePlayer hitPlayer) {
        synchronized (arrow) {
            Map<GamePlayer, Integer> playerHitCounts = arrow.get(shooter);
            if (playerHitCounts != null) {
                playerHitCounts.remove(hitPlayer);
                if (playerHitCounts.isEmpty()) {
                    arrow.remove(shooter);
                }
            }
        }
    }

    private static void cancelClearHitCountTask(GamePlayer shooter, GamePlayer hitPlayer) {
        Map<GamePlayer, BukkitTask> playerTasks = clearHitCountTasks.get(shooter);
        if (playerTasks != null) {
            BukkitTask task = playerTasks.get(hitPlayer);
            if (task != null) {
                task.cancel();
                playerTasks.remove(hitPlayer);
            }
        }
    }

    @Override
    public List<String> getInfo() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7你想要近战与远程的混合体.");
        lore.add("§7你想要一个逃跑的紧急按钮.");
        lore.add("§7你能习惯2个技能.");
        return lore;
    }

    @Override
    public int unlockCost() {
        return 5;
    }

    @Override
    public int energyMelee() {
        return 17;
    }

    @Override
    public int energyBow() {
        return 17;
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.mainSkill.getSkillTip(gamePlayer) + " " + this.secondSkill.getSkillTip(gamePlayer) + " " + this.thirdSkill.getSkillTip(gamePlayer);
    }

    @Override
    public void run() {

        GamePlayer.getOnlinePlayers().stream().filter(gamePlayer -> !gamePlayer.isSpectator() && ClassesManager.getSelected(gamePlayer).equals(this)).forEach(gamePlayer -> {
            if (skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
                skill2Cooldown.put(gamePlayer, skill2Cooldown.get(gamePlayer) - 1);
            }

            if (fall.getOrDefault(gamePlayer, 0) > 0) {
                fall.put((GamePlayer) gamePlayer, fall.get(gamePlayer) - 1);
            }
            if (gamePlayer.getPlayer().getItemInHand().getType() != Material.COMPASS) {
                gamePlayer.sendActionBar(this.getSkillTip(gamePlayer));
            }
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
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL && fall.getOrDefault(GamePlayer.get(e.getEntity().getUniqueId()), 0) > 0) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerKillEvent e) {
        GamePlayer killer = e.getKiller();
        Classes classes = ClassesManager.getSelected(killer);
        if (classes.equals(this)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    killer.setEnergy(100);
                    cancel();
                }
            }.runTaskLater(MegaWalls.getInstance(), 20);
            killpotion.put(killer, killpotion.getOrDefault(killer, 0) + 1);
            if (!givestart.getOrDefault(killer, false)) {
                ItemStack item = new ItemStack(Material.POTION);
                item.setAmount(2);
                item.setDurability((byte) 2);
                PotionMeta im = (PotionMeta) item.getItemMeta();
                im.setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " 不法者药");
                im.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 300, 0), true);
                im.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0), true);
                item.setItemMeta(im);
                Potion pot = new Potion(1);
                pot.setSplash(true);
                pot.apply(item);
                killer.getPlayer().getInventory().addItem(item);
                killpotion.put(killer, 5);
                givestart.put(killer, true);
            }
            int newKillCount = killpotion.getOrDefault(killer, 0);
            if (newKillCount >= 5) {
                ItemStack item = new ItemStack(Material.POTION);
                item.setAmount(2);
                item.setDurability((byte) 2);
                PotionMeta im = (PotionMeta) item.getItemMeta();
                im.setDisplayName(Lawless.this.nameColor + Lawless.this.getDisplayName() + " 不法者药");
                im.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 300, 0), true);
                im.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0), true);
                item.setItemMeta(im);
                Potion pot = new Potion(1);
                pot.setSplash(true);
                pot.apply(item);
                killer.getPlayer().getInventory().addItem(item);
                killpotion.put(killer, 0);
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
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            GamePlayer shooter = GamePlayer.get(((Player) ((Arrow) e.getDamager()).getShooter()).getUniqueId());
            GamePlayer player = GamePlayer.get(e.getEntity().getUniqueId());
            assert shooter != null;
            assert player != null;
            if (player.isSpectator() || player.getGameTeam().isInTeam(shooter)) {
                return;
            }
            Classes classes = ClassesManager.getSelected(shooter);
            if (classes.equals(this) && getHitCount(shooter, player) < 6) {
                incrementHitCount(shooter, player);
                arrowcd.put(player, 1200);
                shooter.sendMessage("§7" + player.getName() + " §ahad §e" + getHitCount(shooter, player) + " §aarrow now.");
            }
        } else if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            GamePlayer damager = GamePlayer.get(e.getDamager().getUniqueId());
            GamePlayer entity = GamePlayer.get(e.getEntity().getUniqueId());
            if (damager.isSpectator() || damager.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                return;
            }
            Classes classes = ClassesManager.getSelected(damager);
            if (entity.getPlayer().getNoDamageTicks() > 10) {
                return;
            }
            if (classes.equals(this) && Break.getOrDefault(damager, 0) == 1) {
                Break.put(damager, 0);
            } else if (classes.equals(this) && fall.getOrDefault(damager, 0) > 0) {
                damager.addEnergy(fall.getOrDefault(damager, 0) * 14, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
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
        if (classes.equals(this) && (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) && e.getItem().getType() == Material.BOW) {
            if (gamePlayer.getEnergy() > 59) {
                if (skill2Cooldown.getOrDefault(gamePlayer, 0) <= 0) {
                    this.secondSkill.use(gamePlayer, gamePlayer.getPlayerStats().getKitStats(classes));
                    gamePlayer.setEnergy(gamePlayer.getEnergy() - 60);
                }
            }
        }
    }

    private synchronized void incrementHitCount(GamePlayer shooter, GamePlayer hitPlayer) {
        arrow.putIfAbsent(shooter, new HashMap<>());
        arrow.get(shooter).putIfAbsent(hitPlayer, 0);
        arrow.get(shooter).put(hitPlayer, arrow.get(shooter).get(hitPlayer) + 1);
        cancelClearHitCountTask(shooter, hitPlayer);
        // 创建一个新的计时任务，在一分钟之后清除命中次数
        BukkitTask task = Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            clearHitCount(shooter, hitPlayer);
        }, 20 * 60); // 1分钟，转换为ticks（每tick为50毫秒）
        // 将新的计时任务存储起来
        if (!clearHitCountTasks.containsKey(shooter)) {
            clearHitCountTasks.put(shooter, new HashMap<>());
        }
        clearHitCountTasks.get(shooter).put(hitPlayer, task);
    }
}