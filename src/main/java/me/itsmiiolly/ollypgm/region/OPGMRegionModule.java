package me.itsmiiolly.ollypgm.region;

import me.itsmiiolly.ollypgm.match.OPGMMatch;
import me.itsmiiolly.ollypgm.module.OPGMModule;

import org.jdom2.Document;
import org.jdom2.Element;

/**
 * Module responsible for parsing and managing all the regions in the XML.
 * @author molenzwiebel
 */
public class OPGMRegionModule extends OPGMModule {
    private OPGMRegionManager regionManager;
    private OPGMRegionParser regionParser;

    public OPGMRegionModule(OPGMMatch match) {
        super(match);

        this.regionManager = new OPGMRegionManager();
        this.regionParser = new OPGMRegionParser(regionManager);
    }

    @Override
    public void parse(Document document) throws Exception {
        Element regionRootElement = document.getRootElement().getChild("regions");
        if (regionRootElement != null) {
            regionParser.parseChildren(regionRootElement);
        }
    }

    /**
     * @return the region manager
     */
    public OPGMRegionManager getRegionManager() {
        return regionManager;
    }

    /**
     * @return the region parser
     */
    public OPGMRegionParser getRegionParser() {
        return regionParser;
    }
}
