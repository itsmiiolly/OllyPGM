package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a region that inverts its subregion.
 * @author molenzwiebel
 */
public class OPGMNegativeRegion implements OPGMRegion {
    private OPGMRegion invertedRegion;

    public OPGMNegativeRegion(OPGMRegion r) {
        this.invertedRegion = r;
    }

    @Override
    public boolean contains(Vector point) {
        return !invertedRegion.contains(point);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMNegativeRegion [invertedRegion=" + invertedRegion + "]";
    }
}
