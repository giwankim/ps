package leetcode.p3701_3800;

/**
 * <a href="https://leetcode.com/problems/maximize-sum-of-squares-of-digits/">3723. Maximize Sum of
 * Squares of Digits</a>
 */
public class MaximizeSumOfSquaresOfDigits {
  /**
   * @implNote Time {@code O(num)} — one pass writes each digit as the largest the remaining sum
   *     allows — and space {@code O(num)} for the builder holding the answer.
   */
  public String maxSumOfSquares(int num, int sum) {
    StringBuilder ans = new StringBuilder();
    for (int i = 0; i < num; i++) {
      int digit = Math.min(9, sum);
      ans.append(digit);
      sum -= digit;
    }
    if (sum > 0) {
      return "";
    }
    return ans.toString();
  }
}
