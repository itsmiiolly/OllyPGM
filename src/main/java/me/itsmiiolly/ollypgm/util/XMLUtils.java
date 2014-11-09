package me.itsmiiolly.ollypgm.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import me.itsmiiolly.ollypgm.map.OPGMContributor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;
import org.jdom2.Attribute;
import org.jdom2.Element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class XMLUtils {
    private static HashMap<String, Enchantment> enchantmentNamings = Maps.newHashMap();

    static {
        enchantmentNamings.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantmentNamings.put("fire protection", Enchantment.PROTECTION_FIRE);
        enchantmentNamings.put("feather falling", Enchantment.PROTECTION_FALL);
        enchantmentNamings.put("projectile protection", Enchantment.PROTECTION_PROJECTILE);
        enchantmentNamings.put("blast protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantmentNamings.put("power", Enchantment.ARROW_DAMAGE);
        enchantmentNamings.put("flame", Enchantment.ARROW_FIRE);
        enchantmentNamings.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantmentNamings.put("infinity", Enchantment.ARROW_INFINITE);
        enchantmentNamings.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantmentNamings.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantmentNamings.put("bane of arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantmentNamings.put("efficiency", Enchantment.DIG_SPEED);
        enchantmentNamings.put("unbreaking", Enchantment.DURABILITY);
        enchantmentNamings.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantmentNamings.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantmentNamings.put("knockback", Enchantment.KNOCKBACK);
        enchantmentNamings.put("fire aspect", Enchantment.FIRE_ASPECT);
        enchantmentNamings.put("thorns", Enchantment.THORNS);
        enchantmentNamings.put("aqua affinity", Enchantment.WATER_WORKER);
        enchantmentNamings.put("respiration", Enchantment.OXYGEN);
    }

    private static ItemStack applyColor(ItemStack item, String color) {
        ItemMeta m = item.getItemMeta();
        if (m instanceof LeatherArmorMeta) {
            LeatherArmorMeta lm = (LeatherArmorMeta) m;
            Color javaColor = hex2Rgb("#" + color);
            lm.setColor(org.bukkit.Color.fromRGB(javaColor.getRed(), javaColor.getGreen(),
                    javaColor.getBlue()));
            item.setItemMeta(lm);
        }
        return item;
    }

    public static Element copyAttributes(Element from, Element to) {
        Element result = to.clone();
        for (Attribute attribute : from.getAttributes()) {
            if (result.getAttribute(attribute.getName()) == null) {
                result.setAttribute(attribute.getName(), attribute.getValue());
            }
        }
        return result;
    }

    public static List<Element> flattenElements(Element root, String parentTagName,
            String childTagName) {
        List<Element> result = Lists.newArrayList();
        for (Element parent : root.getChildren(parentTagName)) {
            result.addAll(flattenElements(copyAttributes(root, parent), parentTagName, childTagName));
        }
        for (Element child : root.getChildren(childTagName)) {
            result.add(copyAttributes(root, child));
        }
        return result;
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(
                colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * Parses a String 2D vector into a 2DVector object
     * 
     * @param vec String 2D vector
     * @return Parsed 2DVector
     */
    public static Vector parse2DVector(String vec) {
        if (vec.split(",").length != 2)
            throw new IllegalArgumentException("2D Vectors should contain 2 numbers. Given " + vec);
        String[] data = vec.split(",");
        return new Vector(parseDouble(data[0]), 0, parseDouble(data[1]));
    }

    /**
     * Parses a String boolean to a Boolean Object
     * 
     * @param bool String boolean to parse
     * @param defaultValue Default Boolean if parsing fails
     * @return A parsed Boolean
     */
    public static boolean parseBool(String bool, boolean defaultValue) {
        if (bool == null) return defaultValue;
        if (bool.equalsIgnoreCase("on") || bool.equalsIgnoreCase("true")
                || bool.equalsIgnoreCase("yes")) return true;
        if (bool.equalsIgnoreCase("off") || bool.equalsIgnoreCase("false")
                || bool.equalsIgnoreCase("no")) return false;
        return defaultValue;
    }

    /**
     * Parses a string chat color into a ChatColor object
     * 
     * @param c The String chat color
     * @param defaultColor The ChatColor to return if c matched no results in the ChatColor Enum
     * @return The Parsed ChatColor obj
     */
    public static ChatColor parseColor(String c, ChatColor defaultColor) {
        return stringToEnum(c, ChatColor.class, defaultColor);
    }

    /**
     * Parses a double from a string value, with the default value of 1
     * 
     * @param doub the string with the double
     * @return the converted double
     */
    public static double parseDouble(String doub) {
        return parseDouble(doub, 1);
    }

    /**
     * Parses a String double into a Double Object
     * 
     * @param doub String double to parse
     * @param defaultValue The default value to return if parsing fails
     * @return A parsed Double value
     */
    public static double parseDouble(String doub, double defaultValue) {
        if (doub == null) return defaultValue;
        if (doub.equals("oo") || doub.equals("∞")) return Integer.MAX_VALUE;
        if (doub.equals("-oo") || doub.equals("-∞")) return Integer.MIN_VALUE;
        if (doub.length() > 0 && doub.charAt(0) == '@') doub = doub.substring(1);
        double i;
        try {
            i = Double.parseDouble(doub);
        } catch (Exception ex) {
            i = defaultValue;
        }
        return i;
    }

    /**
     * Parses the provided string and tries to find a matching enchant. This tries both the enum and the in-game names.
     * @param name the enchantment name
     * @return the enchantment instance
     */
    public static Enchantment parseEnchantment(String name) {
        if (Enchantment.getByName(name.replace(" ", "_").toUpperCase()) != null)
            return Enchantment.getByName(name.replace(" ", "_").toUpperCase());
        if (enchantmentNamings.containsKey(name.replace("_", " ").toLowerCase()))
            return enchantmentNamings.get(name.replace("_", " ").toLowerCase());
        throw new IllegalArgumentException("Could not find enchantment " + name + "!");
    }

    /**
     * Parses a String integer to an Integer Object
     * 
     * @param integer String integer to parse
     * @param defaultValue The Integer to return if parsing fails
     * @return A parsed Integer Object
     */
    public static int parseInt(String integer, int defaultValue) {
        if (integer == null) return defaultValue;
        if (integer.equals("oo") || integer.equals("∞")) return Integer.MAX_VALUE;
        if (integer.equals("-oo") || integer.equals("-∞")) return Integer.MIN_VALUE;
        int i;
        try {
            i = Integer.parseInt(integer);
        } catch (Exception ex) {
            i = defaultValue;
        }
        return i;
    }

    /**
     * Parses an element instance into its corresponding ItemStack
     * @param e the element
     * @return the itemstack
     */
    public static ItemStack parseItem(Element e) {
        Material mat = Material.matchMaterial(e.getTextNormalize());

        if (mat == null) return null;
        ItemStack toReturn = new ItemStack(mat, 1);

        ItemMeta im = Bukkit.getItemFactory().getItemMeta(mat);
        toReturn.setItemMeta(im);

        if (e.getAttributeValue("amount") != null)
            toReturn.setAmount(Integer.parseInt(e.getAttributeValue("amount")));
        else
            toReturn.setAmount(1);

        if (e.getAttributeValue("damage") != null)
            toReturn.setDurability((short) Integer.parseInt(e.getAttributeValue("damage")));
        else
            toReturn.setDurability((short) 0);

        if (e.getAttributeValue("enchantment") != null) {
            for (int i = 0; i < e.getAttributeValue("enchantment").split(";").length; i++) {
                String[] rawEnch = e.getAttributeValue("enchantment").split(";")[i].split(":");
                String enchantment = rawEnch[0].replace(" ", "_").toUpperCase();
                String level = "1";
                if (rawEnch.length == 2) level = rawEnch[1];
                Enchantment enchant = parseEnchantment(enchantment);
                toReturn.addUnsafeEnchantment(enchant, Integer.parseInt(level));
            }
        }

        if (e.getAttributeValue("color") != null)
            toReturn = applyColor(toReturn, e.getAttributeValue("color"));
        return toReturn;
    }

    /**
     * Parses a String
     * 
     * @param str String to parse
     * @param defaultValue Value to return if all parsing fails
     * @return A parsed String Object
     */
    public static String parseString(String str, String defaultValue) {
        if (str == null || str.length() == 0) return defaultValue;
        return str;
    }
    
    /**
     * Gets a child text value, but ensures it exists
     * @param element the element
     * @param childName the child name
     * @return the text value
     * @throws IllegalArgumentException when getChildTextNormalize(childName) returns null
     */
    public static String ensureNotNull(Element element, String childName) {
        String value = element.getChildTextNormalize(childName);
        if (value == null) throw new IllegalArgumentException("Setting "+childName+" was required but null.");
        return value;
    }
    
    /**
     * Parses a String vector into a Vector object
     * 
     * @param vec String vector
     * @return Parsed Vector obj
     */
    public static Vector parseVector(String vec) {
        if (vec.split(",").length != 3)
            throw new IllegalArgumentException("Vectors should contain 3 numbers. Given " + vec);
        String[] data = vec.split(",");
        return new Vector(parseDouble(data[0]), parseDouble(data[1]), parseDouble(data[2]));
    }

    /**
     * Parses a String val through an Enum class and returns a match or returns defaultValue
     * 
     * @param value The value to attempt to match
     * @param enuma Enum class to match value in
     * @param defaultValue Value to return if a match isn't found
     * @return A parsed object
     */
    public static <T extends Enum<T>> T stringToEnum(String value, Class<T> enuma, T defaultValue) {
        if (value == null || enuma == null) return defaultValue;
        T valueFound = Enum.valueOf(enuma, value.replace(" ", "_").toUpperCase());
        if (valueFound != null) return valueFound;
        return defaultValue;
    }
    
    /**
     * Reads a list of elements into a list of contributors
     * @param root the root xml element
     * @param topLevelTag the top level tag for the list of contributors (eg, authors)
     * @param tag the element tag for the list of contributors (eg, author)
     * @return the list of contributors
     */
    public static List<OPGMContributor> readContributorList(Element root, String topLevelTag, String tag) {
        List<OPGMContributor> contribs = Lists.newArrayList();
        for (Element parent : root.getChildren(topLevelTag)) {
            for (Element child : parent.getChildren(tag)) {
                String name = child.getValue();
                if (child.getAttribute("contribution") != null) {
                    contribs.add(new OPGMContributor(name, child.getAttributeValue("contribution")));
                } else if (child.getAttribute("contrib") != null) {
                    contribs.add(new OPGMContributor(name, child.getAttributeValue("contrib")));
                } else {
                    contribs.add(new OPGMContributor(name, null));
                }
            }
        }
        return contribs;
    }
}
