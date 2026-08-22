package leetcode.p3101_3200;

/**
 * <a
 * href="https://leetcode.com/problems/kth-smallest-amount-with-single-denomination-combination/">3116.
 * Kth Smallest Amount With Single Denomination Combination</a>
 */
public class KthSmallestAmountWithSingleDenominationCombination {
  /**
   * @implNote Time {@code O(2^n * n * log C)} since the binary search over the amounts calls
   *     {@code count} once per step. Auxiliary space {@code O(1)}, where {@code n = coins.length}
   *     and {@code C = min(coins) * k} is the search range — the multiples of the smallest coin
   *     alone supply {@code k} amounts at or below it, so the answer cannot exceed it. No coin
   *     passes 25, capping {@code C} at {@code 5 * 10^10} and the search at 36 steps.
   */
  public long findKthSmallest(int[] coins, int k) {
    int min = coins[0];
    for (int coin : coins) {
      min = Math.min(min, coin);
    }
    long ans = -1;
    long lo = 1L;
    long hi = (long) min * k + 1L;
    while (lo <= hi) {
      long mid = lo + (hi - lo) / 2;
      if (count(coins, mid) >= k) {
        ans = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return ans;
  }

  /**
   * @implNote Time {@code O(2^n * n)} from folding a least common multiple across the set bits of
   *     every non-empty subset, the inclusion-exclusion count of the amounts at most {@code x}.
   *     Each fold's {@code gcd} is {@code O(1)} because the constraints cap the coins at 25.
   *     Auxiliary space {@code O(1)}, where {@code n = coins.length}.
   */
  private long count(int[] coins, long x) {
    int n = coins.length;
    long result = 0L;
    for (int mask = 1; mask < (1 << n); mask++) {
      int sgn = (Integer.bitCount(mask) & 1) > 0 ? 1 : -1;
      long lcm = 1L;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) > 0) {
          lcm = lcm / gcd(lcm, coins[i]) * coins[i];
        }
        if (lcm > x) {
          break;
        }
      }
      result += sgn * (x / lcm);
    }
    return result;
  }

  /** @implNote Time {@code O(log min(a, b))} for the Euclid reduction, space {@code O(1)}. */
  private long gcd(long a, long b) {
    while (b != 0) {
      long temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }
}
