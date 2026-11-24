package leetcode.p3401_3500;

/**
 * <a href="https://leetcode.com/problems/maximum-subarray-with-equal-products/">3411. Maximum
 * Subarray With Equal Products</a>
 */
public class MaximumSubarrayWithEqualProducts {
  /**
   * @implNote Time {@code O(n^2 log V)}: for each left end, the product, gcd, and lcm are extended
   *     one element at a time as the right end advances, so each of the {@code O(n^2)} subarrays
   *     costs a single gcd and lcm step, each running Euclid's algorithm in {@code O(log V)}. Space
   *     {@code O(1)}, where {@code n = nums.length} and {@code V = 10} bounds the values.
   */
  public int maxLength(int[] nums) {
    int n = nums.length;
    int ans = 0;
    for (int i = 0; i < n - 1; i++) {
      int prod = nums[i];
      int lcm = nums[i];
      int gcd = nums[i];
      for (int j = i + 1; j < n; j++) {
        prod *= nums[j];
        lcm = lcm(lcm, nums[j]);
        gcd = gcd(gcd, nums[j]);
        if (prod == lcm * gcd) {
          ans = Math.max(ans, j - i + 1);
        }
      }
    }
    return ans;
  }

  private int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  private int lcm(int a, int b) {
    return a / gcd(a, b) * b;
  }
}
