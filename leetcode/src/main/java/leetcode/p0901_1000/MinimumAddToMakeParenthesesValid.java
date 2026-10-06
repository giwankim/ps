package leetcode.p0901_1000;

/**
 * <a href="https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/">921. Minimum Add
 * to Make Parentheses Valid</a>
 */
public class MinimumAddToMakeParenthesesValid {
  /**
   * @implNote Time {@code O(n)} for copying and scanning the characters. Auxiliary space
   *     {@code O(n)} for the array created by {@code s.toCharArray()}, plus {@code O(1)} for the
   *     open-paren balance and the count of unmatched closers, where {@code n = s.length()}.
   */
  public int minAddToMakeValid(String s) {
    int result = 0;
    int balance = 0;
    for (char c : s.toCharArray()) {
      if (c == '(') {
        balance++;
      } else if (c == ')') {
        balance--;
        if (balance < 0) {
          result++;
          balance = 0;
        }
      }
    }
    result += balance;
    return result;
  }
}
