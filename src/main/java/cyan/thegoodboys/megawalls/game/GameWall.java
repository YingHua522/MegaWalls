/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWall {
    private Location minCorner = null;
    private Location maxCorner = null;
    private World world = null;
    private final List<Block> blocks = new ArrayList<Block>();
    private boolean falldown = false;

    public GameWall(Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return;
        }
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())) {
            return;
        }
        this.world = pos1.getWorld();
        this.setMinMax(pos1, pos2);
        this.loadBlocks();
    }

    public Location getMin() {
        return this.minCorner;
    }

    public Location getMax() {
        return this.maxCorner;
    }

    private void setMinMax(Location pos1, Location pos2) {
        this.minCorner = this.getMinimumCorner(pos1, pos2);
        this.maxCorner = this.getMaximumCorner(pos1, pos2);
    }

    private Location getMinimumCorner(Location pos1, Location pos2) {
        return new Location(this.world, (double) Math.min(pos1.getBlockX(), pos2.getBlockX()), (double) Math.min(pos1.getBlockY(), pos2.getBlockY()), (double) Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    private Location getMaximumCorner(Location pos1, Location pos2) {
        return new Location(this.world, Math.max(pos1.getBlockX(), pos2.getBlockX()), (double) Math.max(pos1.getBlockY(), pos2.getBlockY()), (double) Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    private void loadBlocks() {
        for (int x = this.minCorner.getBlockX(); x <= this.maxCorner.getBlockX(); ++x) {
            for (int y = this.minCorner.getBlockY(); y <= this.maxCorner.getBlockY(); ++y) {
                for (int z = this.minCorner.getBlockZ(); z <= this.maxCorner.getBlockZ(); ++z) {
                    Location loc = new Location(this.world, x, y, z);
                    if (loc.getBlock().getType() == Material.AIR) continue;
                    this.blocks.add(loc.getBlock());
                }
            }
        }
    }

    public boolean isInWall(Location location) {
        if (!location.getWorld().equals(this.world)) {
            return false;
        }
        return location.getBlockX() >= this.minCorner.getBlockX() && location.getBlockX() <= this.maxCorner.getBlockX() && location.getBlockY() >= this.minCorner.getBlockY() && location.getBlockY() <= this.maxCorner.getBlockY() && location.getBlockZ() >= this.minCorner.getBlockZ() && location.getBlockZ() <= this.maxCorner.getBlockZ();
    }

    public void falldown() {
        if (this.falldown) {
            return;
        }
        this.falldown = true;
        Iterator<Block> blockIterator = blocks.iterator();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    if (!blockIterator.hasNext()) {
                        this.cancel();
                        return;
                    }
                    Block block = blockIterator.next();
                    block.setType(Material.AIR);
                }
            }
        }.runTaskTimer(MegaWalls.getInstance(), 0L, 1L);
    }
}

