package leetcode;

public class NumberOf1Bits {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}. */
  public int hammingWeight(int n) {
    int result = 0;
    while (n > 0) {
      result += n & 1;
      n >>>= 1;
    }
    return result;
  }

  /** @implNote Time {@code O(1)}, space {@code O(1)}. */
  public int hammingWeight2(int n) {
    return Integer.bitCount(n);
  }

  /**
   * @implNote Time {@code O(k)}, space {@code O(1)}, where {@code k} is the number of set bits in
   *     {@code n}.
   */
  public int hammingWeight3(int n) {
    int result = 0;
    while (n > 0) {
      result += 1;
      n &= n - 1;
    }
    return result;
  }
}
