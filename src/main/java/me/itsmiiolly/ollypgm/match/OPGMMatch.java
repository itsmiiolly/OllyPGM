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
    private int id;
    private Plugin plugin;
    private World matchWorld;
    
    private OPGMMatchState state;
    
    private OPGMMap map;
    private OPGMModuleManager moduleManager;
    
    public OPGMMatch(int id, OPGMMap map, World world, Plugin plugin, OPGMModuleRegistry moduleRegistry) {
        try {
            this.id = id;
            this.plugin = plugin;
            this.map = map;
            this.matchWorld = world;
            
            this.moduleManager = new OPGMModuleManager(this, map.getDocument(), moduleRegistry);
            
            this.state = OPGMMatchState.LOADED;
        } catch (Exception ex) {
            throw new RuntimeException("Could not create match for map "+map.getMapInfo().getDisplayName(), ex);
        }
    }
    
    /**
     * @return the id for this match
     */
    public int getMatchID() {
        return id;
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
    
    /**
     * @return the state this match is currently in
     * @see OPGMMatchState
     */
    public OPGMMatchState getState() {
        return state;
    }
    
    /**
     * Sets the match state to the provided one. OPGMMatch does not validate illegal transitions (FINISHED -> STARTING for example), so it is assumed that it is a valid transition.
     * @param state the new state
     */
    public void setState(OPGMMatchState newState) {
        this.state = newState;
    }
    
    /**
     * Every valid state for a match to be in
     * @author molenzwiebel
     */
    public enum OPGMMatchState {
        /**
         * The match and world are loaded but the starting countdown has not yet started. Players can be in the match at this point
         */
        LOADED,
        /**
         * The match is starting. Players can be in the match at this point
         */
        STARTING,
        /**
         * The match is currently playing and will end with the command or when the criteria are reached. Players can be in the match at this point
         */
        ACTIVE,
        /**
         * The match is done and cannot be started again. Players can be in the match at this point
         */
        FINISHED,
        /**
         * The match world has been unloaded. Players cannot be in the match at this point.<br>
         * <b>Note that this state is rarely seen, if at all. Once a match is set to UNLOADED it is ready for garbage collection and will not exist for much longer</b>
         */
        UNLOADED
    }
}
