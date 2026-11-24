package leetcode.p3401_3500;

/**
 * <a href="https://leetcode.com/problems/maximum-subarray-with-equal-products/">3411. Maximum
 * Subarray With Equal Products</a>
 */
public class MaximumSubarrayWithEqualProducts {
  /**
   * @implNote Time {@code O(n^3 log V)}: each of the {@code O(n^2)} subarrays recomputes its
   *     product, gcd, and lcm from scratch in {@code O(n)} steps, and every gcd or lcm step runs
   *     Euclid's algorithm in {@code O(log V)}. Space {@code O(1)}, where {@code n = nums.length}
   *     and {@code V = 10} bounds the values.
   */
  public int maxLength(int[] nums) {
    int n = nums.length;
    int ans = 0;
    for (int i = 0; i < n - 1; i++) {
      for (int j = i + 1; j < n; j++) {
        if (prod(nums, i, j) == lcm(nums, i, j) * gcd(nums, i, j)) {
          ans = Math.max(ans, j - i + 1);
        }
      }
    }
    return ans;
  }

  private int prod(int[] nums, int start, int end) {
    int res = 1;
    for (int i = start; i <= end; i++) {
      res *= nums[i];
    }
    return res;
  }

  private int gcd(int[] nums, int start, int end) {
    int res = nums[start];
    for (int i = start + 1; i <= end; i++) {
      res = gcd(res, nums[i]);
    }
    return res;
  }

  private int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  private int lcm(int[] nums, int start, int end) {
    int res = nums[start];
    for (int i = start; i <= end; i++) {
      res = lcm(res, nums[i]);
    }
    return res;
  }

  private int lcm(int a, int b) {
    return a / gcd(a, b) * b;
  }
}
