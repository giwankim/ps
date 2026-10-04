package leetcode.p0601_0700;

/**
 * <a href="https://leetcode.com/problems/valid-parenthesis-string/">678. Valid Parenthesis
 * String</a>
 */
public class ValidParenthesisString {
  /**
   * @implNote Time {@code O(n)} for copying and scanning the characters. Auxiliary space
   *     {@code O(n)} for the array created by {@code s.toCharArray()}, plus {@code O(1)} for the
   *     balance bounds, where {@code n = s.length()}.
   */
  public boolean checkValidString(String s) {
    int lo = 0;
    int hi = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        lo++;
        hi++;
      } else if (c == ')') {
        lo--;
        hi--;
      } else {
        lo--;
        hi++;
      }
      lo = Math.max(lo, 0);
      if (lo > hi) {
        break;
      }
    }
    return lo <= 0 && hi >= 0;
  }
}
