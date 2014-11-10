package me.itsmiiolly.ollypgm.map;

import java.io.File;
import java.util.List;

import me.itsmiiolly.ollypgm.util.XMLUtils;

import org.bukkit.Difficulty;
import org.bukkit.World.Environment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;

import com.google.common.collect.Lists;

/**
 * Represents a map that can be played. A {@link OPGMMap} is not responsible for handling module instances, as they are match-specific and thus managed in a {@link OPGMMatch}.
 * @author molenzwiebel
 */
public class OPGMMap {
    private OPGMMapInfo mapInfo;

    private File location;
    private Document xmlDocument;

    public OPGMMap(File location) {
        this(location, new File(location, "map.xml"));
    }

    public OPGMMap(File location, File xmlLocation) {
        try {
            this.xmlDocument = new SAXBuilder().build(xmlLocation);
            this.location = location;
            
            this.parseMapInfo();
        } catch (JDOMParseException spe) {
            spe.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException("Could not load map at location " + location.getPath(), e);
        }
    }

    private void parseMapInfo() {
        Element root = xmlDocument.getRootElement();
        OPGMMapInfo.Builder builder = new OPGMMapInfo.Builder();
        
        builder = builder
                .name(XMLUtils.ensureNotNull(root, "name"))
                .version(XMLUtils.ensureNotNull(root, "version"))
                .objective(XMLUtils.ensureNotNull(root, "objective"));
        
        List<OPGMContributor> authors = XMLUtils.readContributorList(root, "authors", "author");
        if (authors.size() < 1) throw new IllegalArgumentException("Map has no authors defined.");
        List<OPGMContributor> contributors = XMLUtils.readContributorList(root, "contributors", "contributor");
        
        List<String> rules = Lists.newArrayList();
        for (Element parent : root.getChildren("rules")) {
            for (Element rule : parent.getChildren("rule")) {
                rules.add(rule.getTextNormalize());
            }
        }

        Difficulty difficulty = XMLUtils.stringToEnum(root.getChildTextNormalize("difficulty"), Difficulty.class, Difficulty.NORMAL);
        Environment dimension = XMLUtils.stringToEnum(root.getChildTextNormalize("dimension"), Environment.class, Environment.NORMAL);
        boolean friendlyFire = XMLUtils.parseBool(root.getChildTextNormalize("friendlyfire"), false);
        
        this.mapInfo = builder.authors(authors).contributors(contributors).rules(rules).difficulty(difficulty).dimension(dimension).friendlyFire(friendlyFire).build();
    }

    /**
     * @return the information for this {@link OPGMMap}
     */
    public OPGMMapInfo getMapInfo() {
        return this.mapInfo;
    }
    
    /**
     * @return the xml instance for this map
     */
    public Document getDocument() {
        return this.xmlDocument;
    }
}
