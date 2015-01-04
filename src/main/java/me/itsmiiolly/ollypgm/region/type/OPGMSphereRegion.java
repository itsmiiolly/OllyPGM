package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a spherical region with a center and a radius.
 * @author molenzwiebel
 */
public class OPGMSphereRegion implements OPGMRegion {
    private Vector center;
    private int radius;

    public OPGMSphereRegion(Vector c, int r) {
        this.center = c;
        this.radius = r;
    }

    @Override
    public boolean contains(Vector point) {
        return point.isInSphere(center, radius);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMSphereRegion [center=" + center + ", radius=" + radius + "]";
    }
}
