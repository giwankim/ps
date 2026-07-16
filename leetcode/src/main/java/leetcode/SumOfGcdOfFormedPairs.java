package leetcode;

import java.util.Arrays;

public class SumOfGcdOfFormedPairs {
  /**
   * @implNote Time {@code O(n log n + n log V)} where {@code n = nums.length} and {@code V =
   *     max(nums)} — the sort contributes {@code O(n log n)}, while the prefix scan and the
   *     two-pointer pairing each run up to {@code n} Euclid reductions of {@code O(log V)}.
   *     Auxiliary space {@code O(n)} for the prefix-max and prefix-gcd arrays.
   */
  public long gcdSum(int[] nums) {
    int n = nums.length;
    int[] max = new int[n];
    int[] prefixGcd = new int[n];
    max[0] = nums[0];
    prefixGcd[0] = nums[0];
    for (int i = 1; i < n; i++) {
      max[i] = Math.max(max[i - 1], nums[i]);
      prefixGcd[i] = gcd(nums[i], max[i]);
    }

    Arrays.sort(prefixGcd);

    long result = 0;
    int lo = 0;
    int hi = n - 1;
    while (lo < hi) {
      result += gcd(prefixGcd[lo], prefixGcd[hi]);
      lo++;
      hi--;
    }
    return result;
  }

  private static int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }
}
