package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/count-commas-in-range-ii/">3871. Count Commas in Range
 * II</a>
 */
public class CountCommasInRangeII {
  /**
   * The comma after the {@code k}-th group of three digits appears in every number with more than
   * {@code 3k} digits, that is, in every number at or above {@code 10^(3k)}. Summing over comma
   * positions instead of over numbers, each threshold {@code 10^(3k)} at or below {@code n}
   * contributes one comma for every number in {@code [10^(3k), n]}, which is {@code n - 10^(3k) +
   * 1} of them, and thresholds above {@code n} contribute nothing. The loop needs no partial band
   * and no special case for {@code 10^15}: at that input a fifth threshold simply joins the sum
   * with a single number. Problem 3870's {@code n - 999} is the first term of this sum, since its
   * constraint never reaches the second threshold.
   *
   * @implNote Time {@code O(log n)}, one loop iteration per power of {@code 1000} at or below
   *     {@code n}, so five under the constraint {@code n <= 10^15}; the sixth threshold
   *     {@code 10^18} is computed, rejected, and still fits a {@code long}. Space {@code O(1)}.
   */
  public long countCommas(long n) {
    long result = 0L;
    long pow = 1_000L;
    while (n >= pow) {
      result += n - pow + 1;
      pow *= 1_000L;
    }
    return result;
  }
}
