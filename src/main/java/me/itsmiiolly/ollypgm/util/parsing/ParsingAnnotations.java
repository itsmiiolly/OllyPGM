package me.itsmiiolly.ollypgm.util.parsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.itsmiiolly.ollypgm.util.parsing.ElementParser.ValueParser;
import me.itsmiiolly.ollypgm.util.parsing.ElementParser.ValueParser.NullParser;

/**
 * Wrapper class for all parsing annotations
 * @author molenzwiebel
 */
public final class ParsingAnnotations {
    
    /**
     * Represents an attribute that needs to be parsed.
     * @author molenzwiebel
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface Parse {
        /**
         * The name of the attribute. Leave empty for the element contents
         * @return
         */
        String value() default "";

        /**
         * Any aliases for the attribute.
         * @return
         */
        String[] aliases() default {};

        /**
         * The parser for the value. Most values are automatically parsed by the values in ElementParser#defaultParsers.
         * @return
         */
        Class<? extends ValueParser> parser() default NullParser.class;
    }
    
    /**
     * Tells the parser that the annotated method is responsible for parsing elements with the specified name.
     * @author molenzwiebel
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Parser {
        /**
         * The name of the element it parses. Will error if the element provided is not the same
         *
         * @return the name
         */
        public String value();
    }
}