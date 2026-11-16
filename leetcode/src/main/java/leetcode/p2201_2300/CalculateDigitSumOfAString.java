package leetcode.p2201_2300;

/**
 * <a href="https://leetcode.com/problems/calculate-digit-sum-of-a-string/">2243. Calculate Digit
 * Sum of a String</a>
 */
public class CalculateDigitSumOfAString {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)} for the rebuilt string, where {@code n =
   *     s.length()}. Each round is linear in the current length, and every group either shortens
   *     the string or lowers its total digit sum by a multiple of 9, so the round lengths decay
   *     geometrically and sum to {@code O(n)}.
   */
  public String digitSum(String s, int k) {
    while (s.length() > k) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < s.length(); i += k) {
        int sum = 0;
        for (int j = 0; j < k && i + j < s.length(); j++) {
          sum += s.charAt(i + j) - '0';
        }
        sb.append(sum);
      }
      s = sb.toString();
    }
    return s;
  }
}
