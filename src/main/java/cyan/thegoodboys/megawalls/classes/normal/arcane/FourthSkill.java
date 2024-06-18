/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.BlockBreakEvent
 */
package cyan.thegoodboys.megawalls.classes.normal.arcane;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.LocationUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("奥数采矿", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 16.0;
            }
            case 2: {
                return 18.0;
            }
            case 3: {
                return 20.0;
            }
        }
        return 16.0;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u77ff\u77f3\u4f1a\u7ed9\u4f60\u00a7a" + this.getAttribute(level) + "\u00a77\u80fd\u91cf\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u6316\u6398\u77ff\u77f3\u4f1a\u7ed9\u4f60\u00a78" + this.getAttribute(level - 1) + " \u279c \u00a7a" + this.getAttribute(level) + "\u00a77\u80fd\u91cf\u3002");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill4Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill4Level();
    }

    @Override
    public void onBlockBreak(KitStatsContainer kitStats, BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE) {
            GamePlayer gamePlayer = GamePlayer.get(e.getPlayer().getUniqueId());
            if (gamePlayer != null) {
                // 在墙倒塌前，你将获得40能量，并能够破坏3x3范围内的石头
                    gamePlayer.addEnergy(40, PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                    for (Block block : LocationUtils.getCube(e.getBlock().getLocation(), 1)) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

