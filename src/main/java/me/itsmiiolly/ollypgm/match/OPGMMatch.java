package me.itsmiiolly.ollypgm.match;

import java.util.Collection;

import me.itsmiiolly.ollypgm.OPGMPlayer;
import me.itsmiiolly.ollypgm.map.OPGMMap;
import me.itsmiiolly.ollypgm.module.OPGMModule;
import me.itsmiiolly.ollypgm.module.OPGMModuleManager;
import me.itsmiiolly.ollypgm.module.OPGMModuleRegistry;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class OPGMMatch {
    private Plugin plugin;
    private World matchWorld;
    
    private OPGMMap map;
    private OPGMModuleManager moduleManager;
    
    public OPGMMatch(OPGMMap map, World world, Plugin plugin, OPGMModuleRegistry moduleRegistry) {
        try {
            this.plugin = plugin;
            this.map = map;
            this.matchWorld = world;
            
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
     * @return the world this match is playing in
     */
    public World getMatchWorld() {
        return this.matchWorld;
    }
    
    /**
     * @return every player participating in the match, regardless if they are observing or playing
     */
    public Collection<OPGMPlayer> getParticipatingPlayers() {
        return OPGMPlayer.getPlayers(this);
    }
    
    /**
     * @see {@link OPGMModuleManager#getModule(Class)}
     */
    public <M extends OPGMModule> M getModule(Class<M> moduleClass) {
        return this.moduleManager.getModule(moduleClass);
    }
}
