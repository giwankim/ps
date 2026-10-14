package leetcode.p3701_3800;

/**
 * <a href="https://leetcode.com/problems/maximize-sum-of-squares-of-digits/">3723. Maximize Sum of
 * Squares of Digits</a>
 */
public class MaximizeSumOfSquaresOfDigits {
  /**
   * @implNote Time {@code O(num)} — the answer is {@code sum / 9} nines, then the leftover
   *     {@code sum % 9} as one digit when nonzero, then zeros out to {@code num} digits — and space
   *     {@code O(num)} for the builder holding the answer.
   */
  public String maxSumOfSquares(int num, int sum) {
    if (sum > 9 * num) {
      return "";
    }

    StringBuilder ans = new StringBuilder();

    int nines = sum / 9;
    ans.repeat("9", nines);

    sum -= 9 * nines;
    if (sum > 0) {
      ans.append(sum);
    }

    int zeroes = num - ans.length();
    if (zeroes > 0) {
      ans.repeat("0", zeroes);
    }

    return ans.toString();
  }
}
