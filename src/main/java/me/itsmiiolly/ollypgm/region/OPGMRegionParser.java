package me.itsmiiolly.ollypgm.region;

import java.util.List;

import me.itsmiiolly.ollypgm.region.type.OPGMBlockRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMCircleRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMCuboidRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMCylinderRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMIntersectRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMNegativeRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMRectangleRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMSphereRegion;
import me.itsmiiolly.ollypgm.region.type.OPGMUnionRegion;
import me.itsmiiolly.ollypgm.util.parsing.ElementParser;
import me.itsmiiolly.ollypgm.util.parsing.ElementParser.ValueParser.Vector2DParser;
import me.itsmiiolly.ollypgm.util.parsing.ParsingAnnotations.Parse;
import me.itsmiiolly.ollypgm.util.parsing.ParsingAnnotations.Parser;

import org.bukkit.util.Vector;
import org.jdom2.Element;

import com.google.common.collect.Lists;

/**
 * Class responsible for converting XML elements to OPGMRegions
 * @author molenzwiebel
 */
public class OPGMRegionParser {
    private OPGMRegionManager regionManager;

    public OPGMRegionParser(OPGMRegionManager regionManager) {
        this.regionManager = regionManager;
    }
    
    /**
     * Parses the specific element <b>AND adds it to the manager</b>. This should be one of the known element types, or null will be returned
     * @param e the element to parse into a region
     * @return the parsed region
     */
    public OPGMRegion parse(Element e) {
        if (e == null) return null;

        OPGMRegion r = parseElement(e);
        // As <region> is already referring to an already registered region,
        // don't add it to the manager again, or you'll get double regions
        if (e.getName().equals("region")) return r;

        // Register the region
        if (e.getAttributeValue("name") != null) regionManager.add(e.getAttributeValue("name"), r);
        else regionManager.add(r);

        return r;
    }
    
    /**
     * Parses all the children of the specified element, and returns a list of regions.
     *
     * @param e the parent element
     * @return the generated regions
     */
    public List<OPGMRegion> parseChildren(Element e) {
        List<OPGMRegion> foundRegions = Lists.newArrayList();
        for (Element en : e.getChildren()) {
            foundRegions.add(parse(en));
        }
        return foundRegions;
    }
    
    /**
     * Tries to find and invoke the parsing method for the specified element
     *
     * @param e the element
     * @return the parsed region
     */
    private OPGMRegion parseElement(Element e) {
        return ElementParser.findSuitableParserAndParse(this, e);
    }

    /**
     * Parses {@code <region>} elements
     * @param e the element
     * @return the region
     */
    @Parser("region")
    public OPGMRegion parseRegion(@Parse("name") String name) {
        return regionManager.getRegion(name);
    }

    /**
     * Parses {@code <block>} elements. Recognizes both {@code <block>1,1,1</block>} and {@code <block location="1,1,1"></block>}
     * @param e the element
     * @return the region
     */
    @Parser("block")
    public OPGMRegion parseBlock(@Parse(aliases = {"location"}) Vector loc) {
        return new OPGMBlockRegion(loc);
    }

    /**
     * Parses {@code <circle>} elements. Recognizes both {@code base} and {@code center} as valid attribute names for the center.
     * @param e the element
     * @return the region
     */
    @Parser("circle")
    public OPGMRegion parseCircle(@Parse("radius") double radius, @Parse(value = "base", aliases = {"center"}, parser = Vector2DParser.class) Vector base) {
        return new OPGMCircleRegion(base.getX(), base.getZ(), (int) radius);
    }

    /**
     * Parses {@code <rectangle>} elements
     * @param e the element
     * @return the region
     */
    @Parser("rectangle")
    public OPGMRegion parseRectangle(@Parse(value = "min", parser = Vector2DParser.class) Vector min, @Parse(value = "max", parser = Vector2DParser.class) Vector max) {
        return new OPGMRectangleRegion(min.getBlockX(), max.getBlockX(), min.getBlockZ(), max.getBlockZ());
    }

    /**
     * Parses {@code <sphere>} elements
     * @param e the element
     * @return the region
     */
    @Parser("sphere")
    public OPGMRegion parseSphere(@Parse("base") Vector base, @Parse("radius") double radius) {
        return new OPGMSphereRegion(base, (int) radius);
    }

    /**
     * Parses {@code <cuboid>} elements
     * @param e the element
     * @return the region
     */
    @Parser("cuboid")
    public OPGMRegion parseCuboid(@Parse("min") Vector min, @Parse("max") Vector max) {
        return new OPGMCuboidRegion(min, max);
    }

    /**
     * Parses {@code <cylinder>} elements
     * @param e the element
     * @return the region
     */
    @Parser("cylinder")
    public OPGMRegion parseCylinder(@Parse("base") Vector base, @Parse("radius") double radius, @Parse("height") double height) {
        return new OPGMCylinderRegion(base, (int) radius, (int) height);
    }

    /**
     * Parses {@code <intersect>} elements
     * @param e the element
     * @return the region
     */
    @Parser("intersect")
    public OPGMRegion parseIntersect(Element el) {
        List<OPGMRegion> regions = this.parseChildren(el);
        return new OPGMIntersectRegion(regions.toArray(new OPGMRegion[0]));
    }

    /**
     * Parses {@code <negative>} elements
     * @param e the element
     * @return the region
     */
    @Parser("negative")
    public OPGMRegion parseNegative(Element el) {
        return new OPGMNegativeRegion(parseUnion(el));
    }

    /**
     * Parses {@code <union>} elements
     * @param el the element
     * @return the region
     */
    @Parser("union")
    public OPGMRegion parseUnion(Element el) {
        List<OPGMRegion> regions = parseChildren(el);
        //We don't need an union region if there's just one region
        if (regions.size() == 1) return regions.get(0);
        return new OPGMUnionRegion(parseChildren(el));
    }
}
