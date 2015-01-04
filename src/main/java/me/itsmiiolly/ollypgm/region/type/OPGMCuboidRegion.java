package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a cuboid region with a min and a max.
 * @author molenzwiebel
 */
public class OPGMCuboidRegion implements OPGMRegion {
    private Vector min;
    private Vector max;

    public OPGMCuboidRegion(Vector v1, Vector v2) {
        this.max = Vector.getMaximum(v1, v2);
        this.min = Vector.getMinimum(v1, v2);
    }

    @Override
    public boolean contains(Vector point) {
        return point.isInAABB(min, max);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMCuboidRegion [min=" + min + ", max=" + max + "]";
    }
}
