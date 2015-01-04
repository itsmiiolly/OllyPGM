package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a circular region spanning the entire height of the world. This class does not check Y levels, only X and Z to improve performance.
 * @author molenzwiebel
 */
public class OPGMCircleRegion implements OPGMRegion {
    private double baseX;
    private double baseZ;
    private int radius;

    public OPGMCircleRegion(double x, double z, int r) {
        this.baseX = x;
        this.baseZ = z;
        this.radius = r;
    }

    @Override
    public boolean contains(Vector point) {
        double diffx = point.getX() - this.baseX;
        double diffz = point.getZ() - this.baseZ;
        //Use pythagoras to calculate the distance between the center and the specified point. If the distance is less than the radius, its inside.
        //We use diffx * diffx instead of Math.pow(diffx, 2) because the overhead of using pow for small exponents can be avoided.
        return (diffx * diffx) + (diffz * diffz) <= (this.radius * this.radius);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMCircleRegion [baseX=" + baseX + ", baseZ=" + baseZ + ", radius=" + radius + "]";
    }
}
