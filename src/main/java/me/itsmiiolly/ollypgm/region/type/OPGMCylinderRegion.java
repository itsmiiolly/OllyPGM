package me.itsmiiolly.ollypgm.region.type;

import me.itsmiiolly.ollypgm.region.OPGMRegion;

import org.bukkit.util.Vector;

/**
 * Represents a cylindrical region. This is very similar to {@link OPGMCircleRegion} only this region has a specified height and checks for Y values.
 * @author molenzwiebel
 */
public class OPGMCylinderRegion implements OPGMRegion {
    public Vector base;
    public int radius;
    public int height;

    public OPGMCylinderRegion(Vector b, int r, int h) {
        if (h == 0) h = 1;
        if (r <= 0 || h <= 0)
            throw new IllegalArgumentException("A cylinder region needs a positive, above zero number for radius and height!");
        
        this.base = b;
        this.height = h;
        this.radius = r;
    }

    @Override
    public boolean contains(Vector point) {
        //This code is exactly the same as OPGMCircleRegion, only has a Y check added.
        if (point.getY() < this.base.getY() || point.getY() > (this.base.getY() + this.height))
            return false;
        
        double diffx = point.getX() - this.base.getX();
        double diffz = point.getZ() - this.base.getZ();
        return (diffx * diffx) + (diffz * diffz) <= (this.radius * this.radius);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OPGMCylinderRegion [base=" + base + ", radius=" + radius + ", height=" + height + "]";
    }
}
