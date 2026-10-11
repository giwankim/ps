package leetcode.p2701_2800;

/**
 * <a href="https://leetcode.com/problems/sum-of-squares-of-special-elements/">2778. Sum of Squares
 * of Special Elements</a>
 */
public class SumOfSquaresOfSpecialElements {
  /**
   * @implNote Time {@code O(n)} — one pass over the 1-based indices, squaring the element at each
   *     index that divides {@code n}. Auxiliary space {@code O(1)}, where {@code n = nums.length}.
   */
  public int sumOfSquares(int[] nums) {
    int ans = 0;
    int n = nums.length;
    for (int i = 1; i <= n; i++) {
      if (n % i == 0) {
        ans += nums[i - 1] * nums[i - 1];
      }
    }
    return ans;
  }
}
