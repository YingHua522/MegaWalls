/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.util.Vector
 */
package cyan.thegoodboys.megawalls.util;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocationUtils {
    public static Location getLocation(Location location, double toX, double toY, double toZ) {
        return new Location(location.getWorld(), location.getX() + toX, location.getY() + toY, location.getZ() + toZ);
    }

    public static Location getFixedLocation(Location location, BlockFace blockFace) {
        for (int i = 0; i < location.getWorld().getMaxHeight() && location.getBlock().isEmpty(); ++i) {
            location.add(0.0, blockFace == BlockFace.UP ? 1.0 : -1.0, 0.0);
        }
        return location.add(0.0, blockFace == BlockFace.UP ? -1.0 : 1.0, 0.0);
    }

    public static List<Location> getCircle(Location location, double radius, int points) {
        ArrayList<Location> locations = new ArrayList<Location>();
        double increment = Math.PI * 2 / (double) points;
        for (int i = 0; i < points; ++i) {
            double angle = (double) i * increment;
            double x = location.getX() + Math.cos(angle) * radius;
            double z = location.getZ() + Math.sin(angle) * radius;
            locations.add(new Location(location.getWorld(), x, location.getY(), z));
        }
        return locations;
    }

    public static List<Block> getSphere(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;
        for (int x = X - radius; x <= X + radius; ++x) {
            for (int y = Y - radius; y <= Y + radius; ++y) {
                for (int z = Z - radius; z <= Z + radius; ++z) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) > radiusSquared) continue;
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> getSphere1(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (distance < (radius - 1) * (radius - 1))) {
                        blocks.add(new Location(location.getWorld(), x, y, z).getBlock());
                    }
                }
            }
        }

        return blocks;
    }

    public static List<Block> getCube(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        int X = location.getBlockX() - radius / 2;
        int Y = location.getBlockY() - radius / 2;
        int Z = location.getBlockZ() - radius / 2;
        for (int x = X; x < X + radius; ++x) {
            for (int y = Y; y < Y + radius; ++y) {
                for (int z = Z; z < Z + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> getCube1(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX() - radius / 2;
        int Y = location.getBlockY() - radius / 3;
        int Z = location.getBlockZ() - radius / 2;
        for (int x = X; x < X + radius; ++x) {
            for (int y = Y; y < Y + radius; ++y) {
                for (int z = Z; z < Z + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> getCubeByHigh(Location location, int high) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        for (int y = Y; y < Y + high; ++y) {
            blocks.add(location.getWorld().getBlockAt(X, y, Z));
        }
        return blocks;
    }

    public static Vector getBackVector(Location location) {
        float f1 = (float) (location.getZ() + Math.sin(Math.toRadians(location.getYaw() + 90.0f)));
        float f2 = (float) (location.getX() + Math.cos(Math.toRadians(location.getYaw() + 90.0f)));
        return new Vector((double) f2 - location.getX(), 0.0, (double) f1 - location.getZ());
    }

    public static boolean isSafeSpot(Location location) {
        Block blockCenter = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Block blockAbove = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        Block blockBelow = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
        if ((blockCenter.getType().isTransparent() || blockCenter.isLiquid() && !blockCenter.getType().equals((Object) Material.LAVA) && !blockCenter.getType().equals((Object) Material.STATIONARY_LAVA)) && (blockAbove.getType().isTransparent() || blockAbove.isLiquid() && !blockAbove.getType().equals((Object) Material.LAVA) && !blockCenter.getType().equals((Object) Material.STATIONARY_LAVA))) {
            return blockBelow.getType().isSolid() || blockBelow.getType().equals((Object) Material.WATER) || blockBelow.getType().equals((Object) Material.STATIONARY_WATER);
        }
        return false;
    }

    public static Block getRelativeBlock(Location location, double x, double y, double z) {
        return new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z).getBlock();
    }

    public static boolean getBlocksAtLocation(Location location) {
        Set<Block> materials = Sets.newHashSet();
        materials.add(LocationUtils.getRelativeBlock(location, 0, 0, 0));
        materials.add(LocationUtils.getRelativeBlock(location, 0.66, 0, -0.66));
        materials.add(LocationUtils.getRelativeBlock(location, 0.66, 0, 0.66));
        materials.add(LocationUtils.getRelativeBlock(location, -0.66, 0, 0.66));
        materials.add(LocationUtils.getRelativeBlock(location, -0.66, 0, -0.66));
        materials.add(LocationUtils.getRelativeBlock(location, 0, 0, -0.66));
        materials.add(LocationUtils.getRelativeBlock(location, 0, 0, 0.66));
        materials.add(LocationUtils.getRelativeBlock(location, -0.66, 0, 0));
        materials.add(LocationUtils.getRelativeBlock(location, 0.66, 0, 0));
        return true;
    }

}

