package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a single block as a region.
 * @author molenzwiebel
 */
public class OPGMBlockRegion implements OPGMRegion {
    private Vector location;

    public OPGMBlockRegion(Vector l) {
        this.location = l;
    }

    @Override
    public boolean contains(Vector point) {
        return point.equals(location);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMBlockRegion [location=" + location + "]";
    }
}
