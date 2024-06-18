/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes.mythic.snowman;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondSkill extends Skill {
    public static Map<GamePlayer, Integer> snow1 = new HashMap<GamePlayer, Integer>();
    public static Map<GamePlayer, Integer> snow2 = new HashMap<GamePlayer, Integer>();

    public SecondSkill(Classes classes) {
        super("暴风雪", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.5;
            }
            case 2: {
                return 0.75;
            }
            case 3: {
                return 1.0;
            }
        }
        return 0.5;
    }

    public int getAmount(int level) {
        switch (level) {
            case 1: {
                return 1;
            }
            case 2: {
                return 1;
            }
            case 3: {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u654c\u4eba\u65f6\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387");
            lore.add("   \u00a77\u751f\u6210\u00a7a" + this.getAmount(level) + "\u4e2a\u70c8\u7130\u4eba\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u51fb\u6740\u654c\u4eba\u65f6\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u51e0\u7387");
        lore.add("   \u00a77\u751f\u6210\u00a78" + (this.getAmount(level - 1) == this.getAmount(level) ? Integer.valueOf(this.getAmount(level)) : this.getAmount(level - 1) + " \u279c \u00a7a" + this.getAmount(level)) + "\u00a77\u4e2a\u70c8\u7130\u4eba\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    @Override
    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " " + (Snowman.skill2.getOrDefault(gamePlayer, 0) == 2 ? "\u00a7a\u00a7lON" : "\u00a7c\u00a7lOFF");
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        Player player = gamePlayer.getPlayer();
        int a = snow1.getOrDefault(gamePlayer, 0);
        int b = snow2.getOrDefault(gamePlayer, 0);
        if (a > 7) {
            gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 1));
            gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
            snow1.put(gamePlayer, 0);
        }
        if (b > 17) {
            gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.PUMPKIN, 1));
            gamePlayer.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 2));
            snow2.put(gamePlayer, 0);
        }
        return true;
    }
}

