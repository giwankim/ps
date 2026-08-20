package leetcode.p0101_0200;

/**
 * <a href="https://leetcode.com/problems/factorial-trailing-zeroes/">172. Factorial Trailing
 * Zeroes</a>
 */
public class FactorialTrailingZeroes {
  /**
   * @implNote Time {@code O(log n)} (one iteration per power of five up to {@code n}), space
   *     {@code O(1)}.
   */
  public int trailingZeroes(int n) {
    int result = 0;
    while (n > 0) {
      n /= 5;
      result += n;
    }
    return result;
  }
}
