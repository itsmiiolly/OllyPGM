package me.itsmiiolly.ollypgm.util.parsing;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import me.itsmiiolly.ollypgm.util.XMLUtils;
import me.itsmiiolly.ollypgm.util.parsing.ElementParser.ValueParser.NullParser;
import me.itsmiiolly.ollypgm.util.parsing.ParsingAnnotations.Parse;
import me.itsmiiolly.ollypgm.util.parsing.ParsingAnnotations.Parser;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 * Utility class that automagically parses XML values and attributes based on @Parse annotations.
 * @author molenzwiebel
 */
public class ElementParser {
    private static HashMap<Class<?>, ValueParser> defaultParsers = new HashMap<Class<?>, ValueParser>() {
        private static final long serialVersionUID = -8597939902543650159L;
        {
            put(Integer.class, (value) -> XMLUtils.parseInt(value, 0));
            put(int.class, (value) -> XMLUtils.parseInt(value, 0));

            put(Double.class, (value) -> XMLUtils.parseDouble(value, 0D));
            put(double.class, (value) -> XMLUtils.parseDouble(value, 0D));

            put(String.class, (value) -> value);
            put(Vector.class, XMLUtils::parseVector);
            put(MaterialData.class, (value) -> XMLUtils.parseMaterial(value, Material.AIR));
        }
    };

    /**
     * Tries to find a suitable parser (method annotated with @Parse) for the specified element. If found, parses the element and invokes the parsing method.
     * @param caller the object that contains the @Parse methods
     * @param e the element
     * @return the result of the @Parse method invoke, or null if no @Parse method was found
     */
    @SuppressWarnings("unchecked")
    public static <T> T findSuitableParserAndParse(Object caller, Element e) {
        for (Method m : caller.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Parser.class) && m.getAnnotation(Parser.class).value().equalsIgnoreCase(e.getName())) {
                try {
                    return (T) invoke(m, caller, e);
                } catch (Exception e1) {
                    throw new IllegalArgumentException("Something went wrong during parsing!", e1);
                }
            }
        }
        return null;
    }

    /**
     * Internal method that tries to parse and invoke the provided command.
     * @param m the method to invoke with the parsed arguments
     * @param caller the instance to invoke the method on
     * @param el the element to parse
     * @return the return value of the method invocation
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    //Yes, I know this method is ugly as fuck. Deal with it (•_•) ( •_•)>⌐■-■ (⌐■_■)
    private static Object invoke(Method m, Object caller, Element el) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //If the method just takes an element, don't bother parsing
        if (m.getParameterTypes().length == 1 && m.getParameterTypes()[0] == Element.class) {
            return m.invoke(caller, el);
        }

        Annotation[][] annotations = m.getParameterAnnotations();
        Object[] arguments = new Object[annotations.length];
        int i = 0;
        for (Annotation[] annotation : annotations) {
            //Get @Parser instance
            if (!(annotation[0] instanceof Parse)) continue;
            Parse ann = (Parse) annotation[0];
            
            //Get the parser instance or class.
            Object parser = ann.parser();
            if (parser == NullParser.class) {
                if (defaultParsers.containsKey(m.getParameterTypes()[i])) {
                    parser = defaultParsers.get(m.getParameterTypes()[i]);
                }
            } else {
                throw new IllegalArgumentException("@Parser does not declare parser class and no default parser for " + m.getParameterTypes()[i].getName() + " is specified");
            }
            
            //Get value and check aliases
            String value = ann.value().equals("") ? el.getTextNormalize() : el.getAttributeValue(ann.value());
            if (value == null || value.isEmpty()) {
                for (String option : ann.aliases()) {
                    if (el.getAttributeValue(option) != null) {
                        value = el.getAttributeValue(option);
                    }
                }
            }

            //Validation
            if (value == null) {
                throw new IllegalArgumentException("Element " + el.getName() + " is missing required attribute " + ann.value() + " (or aliases). Whole XML: \n" + new XMLOutputter().outputString(el));
            }

            arguments[i++] = parser instanceof ValueParser ? ((ValueParser) parser).parse(value) : ((Class<? extends ValueParser>) parser).newInstance().parse(value);
        }

        //Invoke and return
        return m.invoke(caller, arguments);
    }

    /**
     * Represents a parser able to convert a string into an object.
     * @author molenzwiebel
     */
    public interface ValueParser {
        public Object parse(String value);

        /**
         * The default parser if not specified. Simply returns null
         * @author molenzwiebel
         */
        public static class NullParser implements ValueParser {
            public Object parse(String value) {
                return null;
            }
        }
        
        /**
         * Parser for 2D vectors
         * @author molenzwiebel
         */
        public static class Vector2DParser implements ValueParser {
            public Object parse(String value) {
                return XMLUtils.parse2DVector(value);
            }            
        }
    }
}
