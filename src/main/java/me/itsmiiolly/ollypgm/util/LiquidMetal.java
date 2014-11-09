package me.itsmiiolly.ollypgm.util;

import java.util.Collection;

/**
 * Direct port of the JavaScript LiquidMetal. https://github.com/rmm5t/liquidmetal
 * 
 * @author Steve Anton
 */
public class LiquidMetal {
  /**
   * Class to provide context about what we're matching
   * 
   * @param <E> Object we are matching
   */
  public static abstract class StringProvider<E> {
    /**
     * Gets the context from the object
     * 
     * @param c Object to take in
     * @return Return a String to give info about the object, ex: c.toString() or c.getName()
     */
    public abstract String get(E c);
  }
  public static final double SCORE_NO_MATCH = 0.0;
  public static final double SCORE_MATCH = 1.0;
  public static final double SCORE_TRAILING = 0.8;
  public static final double SCORE_TRAILING_BUT_STARTED = 0.9;

  public static final double SCORE_BUFFER = 0.85;

  private static final double[] buildScoreArray(String string, String abbreviation) {
    double[] scores = new double[string.length()];
    String lower = string.toLowerCase();
    String chars = abbreviation.toLowerCase();

    int lastIndex = -1;
    boolean started = false;
    for (int i = 0; i < chars.length(); i++) {
      char c = chars.charAt(i);
      int index = lower.indexOf(c, lastIndex + 1);

      if (index == -1) return null; // signal no match
      if (index == 0) started = true;

      if (isNewWord(string, index)) {
        scores[index - 1] = 1.0;
        fillArray(scores, SCORE_BUFFER, lastIndex + 1, index - 1);
      } else if (isUpperCase(string, index)) {
        fillArray(scores, SCORE_BUFFER, lastIndex + 1, index);
      } else {
        fillArray(scores, SCORE_NO_MATCH, lastIndex + 1, index);
      }

      scores[index] = SCORE_MATCH;
      lastIndex = index;
    }

    double trailingScore = started ? SCORE_TRAILING_BUT_STARTED : SCORE_TRAILING;
    fillArray(scores, trailingScore, lastIndex + 1, scores.length);
    return scores;
  }

  private static final void fillArray(double[] array, double value, int from, int to) {
    for (int i = from; i < to; i++) {
      array[i] = value;
    }
  }

  /**
   * Gets closest match from a query against a Collection of data
   * 
   * @param query String to find closest match to
   * @param coll Collection to search from
   * @param minScore The minimum score needed to have any match at all (defines closest)
   * @return Object closest match, null if not found
   */
  public static <E> E fuzzyMatch(Iterable<E> coll, String query, StringProvider<E> provider,
      double minScore) {
    E closestMatch = null;
    double closestMatchScore = 0.0D;

    for (E obj : coll) {
      String itemString = provider.get(obj);
      if (LiquidMetal.score(itemString, query) > closestMatchScore) {
        closestMatch = obj;
        closestMatchScore = LiquidMetal.score(itemString, query);
      } else if (LiquidMetal.score(itemString, query) == closestMatchScore) closestMatch = null;
    }

    return closestMatchScore < minScore ? null : closestMatch;
  }

  private static final boolean isNewWord(String string, int index) {
    if (index == 0) return false;
    char c = string.charAt(index - 1);
    return (c == ' ' || c == '\t');
  }

  private static final boolean isUpperCase(String string, int index) {
    char c = string.charAt(index);
    return ('A' <= c && c <= 'Z');
  }

  /**
   * Makes a Collection (List, ArrayList, etc) pretty and readable by using commas and words
   * 
   * @param col Collection to prettify
   * @return A pretty String that represents col
   */
  public static String prettifyCol(Collection<?> col) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < col.size(); i++)
      if (i != 0 && i == col.size() - 1)
        sb.append(" and " + col.toArray()[i]);
      else if (i != 0)
        sb.append(", " + col.toArray()[i]);
      else
        sb.append(col.toArray()[i]);
    return sb.toString();
  }

  public static final double score(String string, String abbreviation) {
    if (abbreviation.length() == 0) return SCORE_TRAILING;
    if (abbreviation.length() > string.length()) return SCORE_NO_MATCH;

    double[] scores = buildScoreArray(string, abbreviation);

    // complete miss:
    if (scores == null) {
      return 0;
    }

    double sum = 0.0;
    for (double score : scores) {
      sum += score;
    }

    return (sum / scores.length);
  }

  /**
   * Tests LiquidMetal.score(), @link LiquidMetalTest
   */
  public static final void test() {
    System.out.print(score("FooBar", "foo")); // => 0.950
    System.out.print(score("FooBar", "fb")); // => 0.917
    System.out.print(score("Foo Bar", "fb")); // => 0.929
    System.out.print(score("Foo Bar", "baz")); // => 0.0
    System.out.print(score("Foo Bar", "")); // => 0.8
  }

}
