package me.itsmiiolly.ollypgm.module;

import java.util.List;

import me.itsmiiolly.ollypgm.match.OPGMMatch;
import me.itsmiiolly.ollypgm.module.OPGMModuleRegistry.OPGMRegisteredModule;

import org.jdom2.Document;

import com.google.common.collect.Lists;

/**
 * Manages every instance of a {@link OPGMModule} for a {@link OPGMMatch}
 * @author molenzwiebel
 */
public class OPGMModuleManager {
    private OPGMMatch match;    
    private List<OPGMModule> modules;
    
    public OPGMModuleManager(OPGMMatch match, Document xmlDocument, OPGMModuleRegistry moduleRegistry) {
        try {
            this.match = match;
            this.modules = Lists.newArrayList();
            
            for (OPGMRegisteredModule registeredModule : moduleRegistry.getRegisteredModules()) {
                OPGMModule module = registeredModule.create(match);
                module.parse(xmlDocument);
                modules.add(module);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error while creating modules from XML for match "+match, ex);
        }
    }
    
    /**
     * Gets the module instance for the provided moduleClass
     * @param moduleClass the module class
     * @return the module instance
     */
    public <M extends OPGMModule> M getModule(Class<M> moduleClass) {
        for (OPGMModule module : modules) {
            if (module.getClass().equals(moduleClass)) {
                //To prevent those annoying "Unchecked cast" errors when I did in fact check.
                return moduleClass.cast(module);
            }
        }
        
        throw new RuntimeException("Module class "+moduleClass.getSimpleName()+"does not exist in the module manager for match "+match);
    }
}
