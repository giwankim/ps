package leetcode.p2901_3000;

/**
 * <a
 * href="https://leetcode.com/problems/shortest-and-lexicographically-smallest-beautiful-string/">2904.
 * Shortest and Lexicographically Smallest Beautiful String</a>
 */
public class ShortestAndLexicographicallySmallestBeautifulString {
  /**
   * @implNote Time {@code O(n^2)} in the worst case: the two window pointers each advance at most
   *     {@code n} times, but every position that closes a window with exactly {@code k} ones may
   *     build and compare a candidate substring of length {@code O(n)}. Auxiliary space
   *     {@code O(n)} for the candidate substrings, where {@code n = s.length()}.
   */
  public String shortestBeautifulSubstring(String s, int k) {
    String result = null;
    int ones = 0;
    int i = 0;
    for (int j = 0; j < s.length(); j++) {
      if (s.charAt(j) == '1') {
        ones++;
      }
      while (ones > k) {
        if (s.charAt(i) == '1') {
          ones--;
        }
        i++;
      }
      if (ones == k) {
        while (s.charAt(i) == '0') {
          i++;
        }
        if (result == null || ((i - j + 1) <= result.length())) {
          String t = s.substring(i, j + 1);
          if (result == null
              || t.length() < result.length()
              || (t.length() == result.length() && t.compareTo(result) < 0)) {
            result = t;
          }
        }
      }
    }
    return result == null ? "" : result;
  }
}
