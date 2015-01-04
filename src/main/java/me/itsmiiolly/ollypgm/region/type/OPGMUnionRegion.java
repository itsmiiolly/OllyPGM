package me.itsmiiolly.ollypgm.region.type;

import java.util.List;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a region that returns true if any of it subregions contains the point.
 * @author molenzwiebel
 */
public class OPGMUnionRegion implements OPGMRegion {
    private List<OPGMRegion> subRegions;

    public OPGMUnionRegion(List<OPGMRegion> subRegions) {
        this.subRegions = subRegions;
    }

    @Override
    public boolean contains(Vector point) {
        for (OPGMRegion r : this.subRegions)
            if (r.contains(point)) return true;
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMUnionRegion [subRegions=" + subRegions + "]";
    }
}
