package me.itsmiiolly.ollypgm.region;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;

/**
 * Manages all regions in a map.
 * @author molenzwiebel
 */
public class OPGMRegionManager {
    private HashMap<String, OPGMRegion> regions;
    
    /**
     * Adds the specified region to the manager. The manager generates a random UUID as the name for this region.
     * @param r the region to add
     * @return the random key that this region is stored under
     */
    public String add(OPGMRegion r) {
        String randomKey = UUID.randomUUID().toString();
        //Very, very, very unlikely but we will account for it.
        if (regions.containsKey(randomKey)) return add(r);
        
        this.regions.put(randomKey, r);
        return randomKey;
    }

    /**
     * Adds the specified <i>named</i> region to the manager.
     * @param key the name (identifier) of the region
     * @param r the region
     */
    public void add(String key, OPGMRegion r) {
        if (regions.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate region " + key + ". (Tried to add " + r + " , existing region: " + regions.get(key) + ")");
        }
        this.regions.put(key, r);
    }

    /**
     * @param key the region identifier
     * @return the region with the specified name/key
     */
    public OPGMRegion getRegion(String key) {
        return regions.get(key);
    }

    /**
     * @return all the regions currently stored in the manager
     */
    public Map<String, OPGMRegion> getRegions() {
        return ImmutableMap.copyOf(regions);
    }
}
