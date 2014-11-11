package me.itsmiiolly.ollypgm.module;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import me.itsmiiolly.ollypgm.match.OPGMMatch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Class responsible for handling every {@link OPGMModule} subclass.
 * @author molenzwiebel
 */
public class OPGMModuleRegistry {
    private List<OPGMRegisteredModule> registeredModules = Lists.newArrayList();
    
    /**
     * Registers the provided class in the registry
     * @param module the module class
     */
    public void register(Class<? extends OPGMModule> module) {
        if (registeredModules.contains(OPGMRegisteredModule.fromClass(module))) {
            throw new RuntimeException("Cannot register module "+module.getSimpleName()+" twice!");
        }
        registeredModules.add(OPGMRegisteredModule.fromClass(module));
    }
    
    /**
     * Removes the provided class from the registry. <b>Note that this will not affect any of the instances of module currently active</b>
     * @param module the module to remove
     */
    public void unregister(Class<? extends OPGMModule> module) {
        //We don't care if the module was previously registered, because we are going to remove it anyway
        registeredModules.remove(OPGMRegisteredModule.fromClass(module));
    }
    
    /**
     * @return an immutable copy of all registered modules
     */
    public Collection<OPGMRegisteredModule> getRegisteredModules() {
        return ImmutableList.copyOf(registeredModules);
    }
    
    /**
     * Helper class that stores the constructor along with the module to increase performance
     * @author molenzwiebel
     */
    public static class OPGMRegisteredModule {
        private static HashMap<Class<? extends OPGMModule>, OPGMRegisteredModule> registeredModuleCache = Maps.newHashMap();
        
        private Class<? extends OPGMModule> moduleClass;
        private Constructor<? extends OPGMModule> moduleConstructor;
        
        /**
         * Creates or loads the {@link OPGMRegisteredModule} for the specified {@link OPGMModule} class.
         * @param moduleClass the {@link OPGMModule} class
         * @return the {@link OPGMRegisteredModule}
         */
        private static OPGMRegisteredModule fromClass(Class<? extends OPGMModule> moduleClass) {
            if (registeredModuleCache.containsKey(moduleClass)) {
                return registeredModuleCache.get(moduleClass);
            }
            
            OPGMRegisteredModule instance = new OPGMRegisteredModule();
            instance.moduleClass = moduleClass;
            try {
                instance.moduleConstructor = instance.moduleClass.getConstructor(OPGMMatch.class);
            } catch (Exception ex) {
                throw new RuntimeException("Could not get constructor for module class "+moduleClass.getSimpleName()+". Are you sure that it follows the format OPGMModule(OPGMMatch)?", ex);
            }
            registeredModuleCache.put(moduleClass, instance);
            return instance;
        }
        
        /**
         * Creates a instance of the module class
         * @param match the match
         * @return the module instance
         */
        public OPGMModule create(OPGMMatch match) {
            try {
                return moduleConstructor.newInstance(match);
            } catch (Exception ex) {
                throw new RuntimeException("Could not create module instance for match "+match+", module "+moduleClass.getSimpleName());
            }
        }
    }
}
