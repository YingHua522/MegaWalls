/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package cyan.thegoodboys.megawalls.game;

import org.bukkit.Location;
import org.bukkit.World;

public class GameRegion {
    private Location minCorner = null;
    private Location maxCorner = null;
    private World world = null;

    public GameRegion(Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return;
        }
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())) {
            return;
        }
        this.world = pos1.getWorld();
        this.setMinMax(pos1, pos2);
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
        return new Location(this.world, (double) Math.max(pos1.getBlockX(), pos2.getBlockX()), (double) Math.max(pos1.getBlockY(), pos2.getBlockY()), (double) Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    public boolean isInRegion(Location location) {
        if (!location.getWorld().equals(this.world)) {
            return false;
        }
        return location.getBlockX() >= this.minCorner.getBlockX() && location.getBlockX() <= this.maxCorner.getBlockX() && location.getBlockY() >= this.minCorner.getBlockY() && location.getBlockY() <= this.maxCorner.getBlockY() && location.getBlockZ() >= this.minCorner.getBlockZ() && location.getBlockZ() <= this.maxCorner.getBlockZ();
    }
}

