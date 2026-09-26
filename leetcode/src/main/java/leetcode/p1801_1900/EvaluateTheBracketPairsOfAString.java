package leetcode.p1801_1900;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://leetcode.com/problems/evaluate-the-bracket-pairs-of-a-string/">1807. Evaluate
 * the Bracket Pairs of a String</a>
 */
public class EvaluateTheBracketPairsOfAString {
  /**
   * @implNote Time {@code O(n + m)} expected, space {@code O(n + m)}, where {@code n = s.length()}
   *     and {@code m = knowledge.size()}: one pass hashes every key into {@code map}, then one
   *     left-to-right pass over {@code s} copies each plain character and swaps each bracket pair
   *     for its value via an {@code O(1)} expected lookup. Keys and values are at most 10
   *     characters, so each substring, hash, and append is {@code O(1)}, and because a bracket pair
   *     spans at least 3 characters, {@code result} stays within {@code 10n / 3} characters.
   */
  public String evaluate(String s, List<List<String>> knowledge) {
    Map<String, String> map = new HashMap<>();
    for (var kv : knowledge) {
      map.put(kv.getFirst(), kv.getLast());
    }

    StringBuilder result = new StringBuilder();
    int n = s.length();
    int i = 0;
    while (i < n) {
      if (s.charAt(i) == '(') {
        int j = s.indexOf(')', i + 1);
        String key = s.substring(i + 1, j);
        result.append(map.getOrDefault(key, "?"));
        i = j + 1;
      } else {
        result.append(s.charAt(i++));
      }
    }

    return result.toString();
  }
}
