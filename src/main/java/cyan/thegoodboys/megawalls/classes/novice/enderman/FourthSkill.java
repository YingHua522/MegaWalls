package cyan.thegoodboys.megawalls.classes.novice.enderman;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.CollectSkill;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FourthSkill extends CollectSkill {
    public FourthSkill(Classes classes) {
        super("末影方块", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.12;
            }
            case 2: {
                return 0.24;
            }
            case 3: {
                return 0.36;
            }
        }
        return 0.12;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u6316\u6398\u77ff\u77f3\u548c\u6728\u5934\u65f6,");
            lore.add("    \u00a77\u4f60\u6709\u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u7acb\u5373\u6316\u6398");
            lore.add("    \u00a77\u5468\u56f4\u7684\u6240\u6709\u65b9\u5757\u3002");
            return lore;
        }
        lore.add(" \u00a78\u25aa \u00a77\u5f53\u4f60\u6316\u6398\u77ff\u77f3\u548c\u6728\u5934\u65f6,");
        lore.add("    \u00a77\u4f60\u6709\u00a78" + StringUtils.percent(this.getAttribute(level - 1)) + " \u279c");
        lore.add("    \u00a7a" + StringUtils.percent(this.getAttribute(level)) + "\u00a77\u7684\u51e0\u7387\u7acb\u5373\u6316\u6398");
        lore.add("    \u00a77\u5468\u56f4\u7684\u6240\u6709\u65b9\u5757\u3002");
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
        if ((e.getBlock().getType() == Material.STONE || e.getBlock().getType() == Material.COBBLESTONE || e.getBlock().getType() == Material.COAL_ORE || e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) && (double) MegaWalls.getRandom().nextInt(100) <= this.getAttribute(kitStats.getSkill4Level()) * 100.0) {
            Game game = MegaWalls.getInstance().getGame();
            Block block = e.getBlock();
            BlockFace[] blockFaces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
            boolean breaked = false;
            for (BlockFace blockFace : blockFaces) {
                Block relative = block.getRelative(blockFace);
                if (game.isUnbreakable(relative.getLocation()) || relative.getType() == Material.FURNACE || relative.getType() == Material.TRAPPED_CHEST || relative.getType() == Material.BEDROCK) {
                    continue;
                }
                Collection<ItemStack> drops = relative.getDrops();
                Player player = e.getPlayer();
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                for (ItemStack drop : drops) {
                    if (gamePlayer != null) {
                        if (gamePlayer.getPlayer().getInventory().firstEmpty() != -1) {
                            gamePlayer.getPlayer().getInventory().addItem(drop);
                        }else if (gamePlayer.getPlayer().getEnderChest().firstEmpty() != -1) {
                            gamePlayer.getEnderChest().addItem(drop);
                        }else {
                            gamePlayer.getPlayer().getWorld().dropItemNaturally(relative.getLocation(), drop);
                        }
                    }
                }
                relative.setType(Material.AIR);
                breaked = true;
            }
            if (breaked) {
                Objects.requireNonNull(GamePlayer.get(e.getPlayer().getUniqueId())).playSound(Sound.ENDERMAN_HIT, 0.5f, 0.5f);
            }
        }
    }
}

