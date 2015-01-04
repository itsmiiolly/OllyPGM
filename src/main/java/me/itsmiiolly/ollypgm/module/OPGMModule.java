package me.itsmiiolly.ollypgm.module;

import java.util.List;

import me.itsmiiolly.ollypgm.match.OPGMMatch;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.jdom2.Document;

import com.google.common.collect.Lists;

/**
 * Represents a module that is initialized with a match instance. These module instances are match specific.
 * @author molenzwiebel
 */
public abstract class OPGMModule implements Listener {
    private final OPGMMatch match;
    private final List<Listener> registeredListeners;
    
    public OPGMModule(OPGMMatch match) {
        this.match = match;
        this.registeredListeners = Lists.newArrayList();
    }
    
    /**
     * Registers itself for any events from the Bukkit event system
     */
    protected final void registerEvents() {
        registerEvents(this);
    }
    
    /**
     * Registers the provided listener for any events from the Bukkit event system
     * @param listener
     */
    protected final void registerEvents(Listener listener) {
        if (registeredListeners.contains(listener)) {
            throw new IllegalArgumentException("Listener "+listener+" is already registered!");
        }
                
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(listener, getMatch().getPlugin());
        registeredListeners.add(listener);
    }
    
    /**
     * @return the match for this module instance
     */
    protected final OPGMMatch getMatch() {
        return match;
    }
    
    /**
     * @see {@link OPGMModuleManager#getModule(Class)}
     */
    protected final <M extends OPGMModule> M getModule(Class<M> moduleClass) {
        return match.getModule(moduleClass);
    }
    
    /**
     * Subclasses implement this method to load their module information from the provided XML document.
     * @param document the xml
     * @throws Exception whenever the module encounters an error while parsing
     */
    public abstract void parse(Document document) throws Exception;
    
    /**
     * Called when a {@link OPGMMatch} gets destroyed. Unregisters every listener and command handler.
     * Note that listeners are not automagically removed when a match ends, the module is responsible for checking the match state
     */
    public final void cleanup() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
    }
}
