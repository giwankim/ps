package leetcode.p0801_0900;

/** <a href="https://leetcode.com/problems/score-of-parentheses/">856. Score of Parentheses</a> */
public class ScoreOfParentheses {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = s.length()}: one pass tracks
   *     the nesting depth, and each {@code "()"} core closed at depth {@code d} contributes
   *     {@code 2^(d - 1)}. The scan reads through {@code charAt} and keeps two counters, so nothing
   *     scales with the input.
   */
  public int scoreOfParentheses(String s) {
    int result = 0;
    int depth = 0;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '(') {
        depth++;
      } else {
        if (s.charAt(i - 1) == '(') {
          result += 1 << (depth - 1);
        }
        depth--;
      }
    }
    return result;
  }
}
