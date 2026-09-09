package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/count-commas-in-range-ii/">3871. Count Commas in Range
 * II</a>
 */
public class CountCommasInRangeII {
  /**
   * A {@code d}-digit number carries {@code (d - 1) / 3} commas, one per gap between groups of
   * three digits, so the count is constant across each band of three digit widths: 4 to 6 digits
   * carry one, 7 to 9 carry two, 10 to 12 carry three, 13 to 15 carry four, and {@code 10^15}
   * itself, the only 16-digit input the constraint admits, carries five. Each branch adds the full
   * contribution of every band below {@code n} to that of the partial band {@code n} falls in,
   * whose size is {@code n} minus the last number of the previous band.
   *
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public long countCommas(long n) {
    if (n < 1_000) {
      return 0;
    }
    if (n < 1_000_000) {
      return n - 999;
    }
    if (n < 1_000_000_000) {
      return 999_000 + 2 * (n - 999_999);
    }
    if (n < 1_000_000_000_000L) {
      return 999_000 + 2 * 999_000_000 + 3 * (n - 999_999_999);
    }
    if (n < 1_000_000_000_000_000L) {
      return 999_000 + 2 * 999_000_000 + 3 * 999_000_000_000L + 4 * (n - 999_999_999_999L);
    }
    return 999_000 + 2 * 999_000_000 + 3 * 999_000_000_000L + 4 * (999_000_000_000_000L) + 5;
  }
}
