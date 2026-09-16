package leetcode.p1601_1700;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/">1621.
 * Number of Sets of K Non-Overlapping Line Segments</a>
 */
public class NumberOfSetsOfKNonOverlappingLineSegments {
  public static final int MOD = 1_000_000_007;

  private int n;
  private int[][] dp;
  private int[][] psum;

  /**
   * @implNote Time {@code O(n * k)}: filling {@code psum} from row {@code n - 1} down visits
   *     {@code O(n * k)} cells, and each resolves {@link #count(int, int)} in {@code O(1)} because
   *     row {@code i + 1} of both {@code dp} and {@code psum} is already complete. Both tables take
   *     {@code O(n * k)} space, and the recursion never goes deeper than one memo hit.
   */
  public int numberOfSets(int n, int k) {
    this.n = n;
    dp = new int[n][k + 1];
    for (int[] r : dp) {
      Arrays.fill(r, -1);
    }

    psum = new int[n + 1][k + 1];
    for (int i = n - 1; i >= 0; i--) {
      for (int r = 0; r <= k; r++) {
        psum[i][r] = (count(i, r) + psum[i + 1][r]) % MOD;
      }
    }

    return count(0, k);
  }

  private int count(int start, int remain) {
    if (remain == 0) {
      return 1;
    }
    if (start == n) {
      return 0;
    }
    if (dp[start][remain] != -1) {
      return dp[start][remain];
    }
    int result = count(start + 1, remain);
    result = (result + psum[start + 1][remain - 1]) % MOD;
    dp[start][remain] = result;
    return result;
  }
}
