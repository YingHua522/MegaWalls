/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cyan.thegoodboys.megawalls.classes;

import com.google.gson.JsonElement;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.mythic.assassin.Assassin;
import cyan.thegoodboys.megawalls.classes.mythic.automaton.Automaton;
import cyan.thegoodboys.megawalls.classes.mythic.dragon.Dragon;
import cyan.thegoodboys.megawalls.classes.mythic.lawless.Lawless;
import cyan.thegoodboys.megawalls.classes.mythic.mole.Mole;
import cyan.thegoodboys.megawalls.classes.mythic.phoenix.Phoenix;
import cyan.thegoodboys.megawalls.classes.mythic.snowman.Snowman;
import cyan.thegoodboys.megawalls.classes.mythic.werewolf.Werewolf;
import cyan.thegoodboys.megawalls.classes.normal.arcane.Arcane;
import cyan.thegoodboys.megawalls.classes.normal.blaze.Blaze;
import cyan.thegoodboys.megawalls.classes.normal.cow.Cow;
import cyan.thegoodboys.megawalls.classes.normal.creeper.Creeper;
import cyan.thegoodboys.megawalls.classes.normal.dreadlord.Dreadlord;
import cyan.thegoodboys.megawalls.classes.normal.hunter.Hunter;
import cyan.thegoodboys.megawalls.classes.normal.oldspider.OldSpider;
import cyan.thegoodboys.megawalls.classes.normal.pigman.Pigman;
import cyan.thegoodboys.megawalls.classes.normal.pirate.Pirate;
import cyan.thegoodboys.megawalls.classes.normal.puppet.Puppet;
import cyan.thegoodboys.megawalls.classes.normal.shaman.Shaman;
import cyan.thegoodboys.megawalls.classes.normal.shark.Shark;
import cyan.thegoodboys.megawalls.classes.normal.spider.Spider;
import cyan.thegoodboys.megawalls.classes.normal.squid.Squid;
import cyan.thegoodboys.megawalls.classes.novice.enderman.Enderman;
import cyan.thegoodboys.megawalls.classes.novice.him.HIM;
import cyan.thegoodboys.megawalls.classes.novice.random.Random;
import cyan.thegoodboys.megawalls.classes.novice.skeleton.Skeleton;
import cyan.thegoodboys.megawalls.classes.novice.zombie.Zombie;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.inv.opener.SQL;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class ClassesManager {
    private static final List<Classes> classesList = new ArrayList<Classes>();

    private static final List<Classes> classesBanList = new ArrayList<Classes>();
    private static boolean registered = false;

    public static Classes getClassesByName(String ClassesName) {
        for (Classes classes : classesList) {
            if (!classes.getName().equalsIgnoreCase(ClassesName)) continue;
            return classes;
        }
        return null;
    }

    public static void registerClasses(Classes classes) {
        classesList.add(classes);
    }

    public static void removeClasses(Classes classes) {
        classesBanList.add(classes);
    }

    public static boolean isBanned(Classes classes) {
        if (classesBanList.contains(classes)) {
            return true;
        } else {
            return false;
        }
    }



    public static void registerAll() {
        if (registered) {
            return;
        }
        SQL.Register();
        // 注册所有类
        ClassesManager.registerClasses(new Cow());
        ClassesManager.registerClasses(new Skeleton());
        ClassesManager.registerClasses(new Blaze());
        ClassesManager.registerClasses(new Enderman());
        ClassesManager.registerClasses(new Shaman());
        ClassesManager.registerClasses(new HIM());
        ClassesManager.registerClasses(new Zombie());
        ClassesManager.registerClasses(new Creeper());
        ClassesManager.registerClasses(new Pigman());
        ClassesManager.registerClasses(new Puppet());
        ClassesManager.registerClasses(new Spider());
        ClassesManager.registerClasses(new Squid());
        ClassesManager.registerClasses(new Dreadlord());
        ClassesManager.registerClasses(new Arcane());
        ClassesManager.registerClasses(new Pirate());
        ClassesManager.registerClasses(new Mole());
        ClassesManager.registerClasses(new Hunter());
        ClassesManager.registerClasses(new Werewolf());
        ClassesManager.registerClasses(new Phoenix());
        ClassesManager.registerClasses(new Shark());
        ClassesManager.registerClasses(new Snowman());
        ClassesManager.registerClasses(new Lawless());
        ClassesManager.registerClasses(new Automaton());
        ClassesManager.registerClasses(new Assassin());
        ClassesManager.registerClasses(new OldSpider());
        ClassesManager.registerClasses(new Dragon());
        registered = true;
    }

    public static void start() {
        classesList.forEach(classes -> {
            Bukkit.getPluginManager().registerEvents((Listener) classes, MegaWalls.getInstance());
            Bukkit.getScheduler().runTaskTimer((Plugin) MegaWalls.getInstance(), classes, 0L, 20L);
        });
    }

    public static List<Classes> getNormalClasses() {
        ArrayList<Classes> list = new ArrayList<>();
        for (Classes classes : classesList) {
            if (classes.getClassesType() != ClassesType.NORMAL && classes.getClassesType() != ClassesType.NOVICE)
                continue;
            list.add(classes);
        }
        return list;
    }

    public static List<Classes> getMythicClasses() {
        ArrayList<Classes> list = new ArrayList<Classes>();
        for (Classes classes : classesList) {
            if (classes.getClassesType() != ClassesType.MYTHIC) continue;
            list.add(classes);
        }
        return list;
    }

    public static List<Classes> getClasses() {
        return classesList;
    }

    public static List<Classes> sort(List<Classes> list) {
        Collections.sort(list, new Comparator<Classes>() {

            @Override
            public int compare(Classes classes1, Classes classes2) {
                if (classes1.getClassesType() == classes2.getClassesType()) {
                    return classes1.getDifficulty().getPriority() - classes2.getDifficulty().getPriority();
                }
                return classes1.getClassesType().getPriority() - classes2.getClassesType().getPriority();
            }
        });
        return list;
    }

    public static Classes getSelected(GamePlayer gamePlayer) {
        Classes cached = gamePlayer.getPlayerStats().getCacheSelected();
        return cached == null ? gamePlayer.getPlayerStats().getSelected() : cached;
    }

    public static boolean isClassesItem(ItemStack itemStack) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName() != null) {
            String displayname = itemStack.getItemMeta().getDisplayName();
            for (Classes classes : classesList) {
                return displayname.contains("Sword") || displayname.contains("Spade") || displayname.contains("Bow") || displayname.contains("Pickaxe") || displayname.contains("Axe") || displayname.contains("Shovel") || displayname.contains("Steak") || displayname.contains("Ender Chest") || displayname.contains("Helmet") || displayname.contains("Chestplate") || displayname.contains("Leggings") || displayname.contains("Boots") || displayname.contains("Potion of Speed") || displayname.contains("Potion of Heal") || displayname.contains("Splash Potion of Speed") || displayname.contains("Splash Potion of Slow") || displayname.contains("Potion of Regen") || displayname.contains("Arrow") || displayname.contains("Compass") || displayname.contains("Bread") || displayname.contains("Cooked Fish") || displayname.contains("Carrot Stick") || displayname.contains("Golden Apple") || displayname.contains("剑") || displayname.contains("弓") || displayname.contains("镐") || displayname.contains("斧") || displayname.contains("锹") || displayname.contains("牛排") || displayname.contains("末影箱") || displayname.contains("头盔") || displayname.contains("胸甲") || displayname.contains("护腿") || displayname.contains("靴子") || displayname.contains("药水") || displayname.contains("箭") || displayname.contains("指南针") || displayname.contains("面包") || displayname.contains("鲑鱼") || displayname.contains("钓竿") || displayname.contains("金苹果");
            }
        }
        return false;
    }

    public static void playSkillEffect(final Player player) {
    }

    public static void giveItems(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(gamePlayer.getPlayerStats().getSelected());
        int level = kitStats.getEquipLevel();
        if (kitStats.getInventory() == null) {
            for (ItemStack itemStack : gamePlayer.getPlayerStats().getSelected().getEquipmentPackage().getEquipments(level)) {
                if (ItemUtils.isHelmet(itemStack)) {
                    player.getInventory().setHelmet(itemStack);
                    continue;
                }
                if (ItemUtils.isChestplate(itemStack)) {
                    player.getInventory().setChestplate(itemStack);
                    continue;
                }
                if (ItemUtils.isLeggings(itemStack)) {
                    player.getInventory().setLeggings(itemStack);
                    continue;
                }
                if (ItemUtils.isBoots(itemStack)) {
                    player.getInventory().setBoots(itemStack);
                    continue;
                }
                player.getInventory().addItem(new ItemStack[]{itemStack});
            }
        } else {
            for (Map.Entry entry : kitStats.getInventory().entrySet()) {
                int slot = Integer.parseInt((String) entry.getKey());
                player.getInventory().setItem(slot, ItemUtils.read(Base64.decode(((JsonElement) entry.getValue()).getAsString())));
            }
            for (ItemStack itemStack : gamePlayer.getPlayerStats().getSelected().getEquipmentPackage().getEquipments(level)) {
                if (ItemUtils.isHelmet(itemStack)) {
                    player.getInventory().setHelmet(itemStack);
                }
                if (ItemUtils.isChestplate(itemStack)) {
                    player.getInventory().setChestplate(itemStack);
                }
                if (ItemUtils.isLeggings(itemStack)) {
                    player.getInventory().setLeggings(itemStack);
                }
                if (ItemUtils.isBoots(itemStack)) {
                    player.getInventory().setBoots(itemStack);
                }
            }
        }
    }
}

