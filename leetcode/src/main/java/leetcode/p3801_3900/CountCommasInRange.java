package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/count-commas-in-range/">3870. Count Commas in Range</a>
 */
public class CountCommasInRange {
  /**
   * Integers below {@code 1000} have at most three digits and no comma. Under the constraint
   * {@code n <= 10^5}, every integer in {@code [1000, n]} has four to six digits and therefore
   * exactly one comma, so the answer is the size of that interval, {@code n - 999}, or {@code 0}
   * when {@code n < 1000}.
   *
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public int countCommas(int n) {
    if (n < 1000) {
      return 0;
    }
    return n - 1000 + 1;
  }
}
