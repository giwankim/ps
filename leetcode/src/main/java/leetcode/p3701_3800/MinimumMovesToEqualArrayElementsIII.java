package leetcode.p3701_3800;

/**
 * <a href="https://leetcode.com/problems/minimum-moves-to-equal-array-elements-iii/">3736. Minimum
 * Moves to Equal Array Elements III</a>
 */
public class MinimumMovesToEqualArrayElementsIII {
  /** @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int minMoves(int[] nums) {
    int max = Integer.MIN_VALUE;
    for (int num : nums) {
      max = Math.max(max, num);
    }
    int ans = 0;
    for (int num : nums) {
      ans += max - num;
    }
    return ans;
  }
}
