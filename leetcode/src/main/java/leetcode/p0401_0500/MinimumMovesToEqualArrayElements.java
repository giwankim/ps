package leetcode.p0401_0500;

/**
 * <a href="https://leetcode.com/problems/minimum-moves-to-equal-array-elements/">453. Minimum Moves
 * to Equal Array Elements</a>
 */
public class MinimumMovesToEqualArrayElements {
  /** @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int minMoves(int[] nums) {
    int min = Integer.MAX_VALUE;
    for (int num : nums) {
      min = Math.min(min, num);
    }
    int res = 0;
    for (int num : nums) {
      res += num - min;
    }
    return res;
  }
}
