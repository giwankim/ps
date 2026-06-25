package leetcode;

public class BitwiseAndOfNumbersRange {
  /**
   * @implNote Time {@code O(k)}, space {@code O(1)}, where {@code k} is the number of set bits in
   *     {@code right}.
   */
  public int rangeBitwiseAnd(int left, int right) {
    while (left < right) {
      right &= right - 1;
    }
    return right;
  }

  /** @implNote Time {@code O(log right)}, space {@code O(1)}. */
  public int rangeBitwiseAnd2(int left, int right) {
    int shifts = 0;
    while (left < right) {
      left >>= 1;
      right >>= 1;
      shifts += 1;
    }
    return left << shifts;
  }
}
