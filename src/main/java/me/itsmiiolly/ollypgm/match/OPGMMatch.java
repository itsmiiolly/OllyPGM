package me.itsmiiolly.ollypgm.match;

import me.itsmiiolly.ollypgm.map.OPGMMap;
import me.itsmiiolly.ollypgm.module.OPGMModule;
import me.itsmiiolly.ollypgm.module.OPGMModuleManager;
import me.itsmiiolly.ollypgm.module.OPGMModuleRegistry;

import org.bukkit.plugin.Plugin;

public class OPGMMatch {
    private Plugin plugin;
    
    private OPGMMap map;
    private OPGMModuleManager moduleManager;
    
    public OPGMMatch(OPGMMap map, Plugin plugin, OPGMModuleRegistry moduleRegistry) {
        try {
            this.plugin = plugin;
            this.map = map;
            
            this.moduleManager = new OPGMModuleManager(this, map.getDocument(), moduleRegistry);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create match for map "+map.getMapInfo().getDisplayName(), ex);
        }
    }
    
    /**
     * @return the map that this match is currently playing
     */
    public OPGMMap getMap() {
        return this.map;
    }

    /**
     * @return the plugin responsible for handling this match
     */
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    /**
     * @see {@link OPGMModuleManager#getModule(Class)}
     */
    public <M extends OPGMModule> M getModule(Class<M> moduleClass) {
        return this.moduleManager.getModule(moduleClass);
    }
}
