package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a rectangular region that does not check for Y value (spans the whole height of the world)
 * @author molenzwiebel
 */
public class OPGMRectangleRegion implements OPGMRegion {
    private double minX;
    private double maxX;
    private double minZ;
    private double maxZ;

    public OPGMRectangleRegion(double minX, double maxX, double minZ, double maxZ) {
        this.minX = Math.min(minX, maxX);
        this.maxX = Math.max(minX, maxX);
        this.minZ = Math.min(minZ, maxZ);
        this.maxZ = Math.max(minZ, maxX);
    }

    @Override
    public boolean contains(Vector point) {
        return (this.minX <= point.getX()) && (this.maxX >= point.getX()) && (this.minZ <= point.getZ()) && (this.maxZ >= point.getZ());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMRectangleRegion [minX=" + minX + ", maxX=" + maxX + ", minZ=" + minZ + ", maxZ=" + maxZ + "]";
    }
}
