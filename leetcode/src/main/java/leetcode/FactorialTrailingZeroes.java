package leetcode;

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
