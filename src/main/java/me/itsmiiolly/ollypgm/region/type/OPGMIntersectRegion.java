package me.itsmiiolly.ollypgm.region.type;

import java.util.Arrays;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a region that only returns true if every subregion contains the point (resulting in an intersection).
 * @author molenzwiebel
 */
public class OPGMIntersectRegion implements OPGMRegion {
    private OPGMRegion[] regions;

    public OPGMIntersectRegion(OPGMRegion[] r) {
        this.regions = r;
    }

    @Override
    public boolean contains(Vector point) {
        for (OPGMRegion r : this.regions)
            if (!r.contains(point)) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMIntersectRegion [regions=" + Arrays.toString(regions) + "]";
    }
}
