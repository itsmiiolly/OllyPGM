package me.itsmiiolly.ollypgm.region;

import org.bukkit.util.Vector;

/**
 * Represents a region defined in the XML. Regions store vectors and do basic calculations to check if a point is inside their region. Every region is encouraged to extend the toString method.
 * @author molenzwiebel
 */
public interface OPGMRegion {
    /**
     * Checks if the region has the specified vector within its bounds.
     * @param point the point to check for
     * @return if the region contains the point.
     */
    public boolean contains(Vector point);
}
