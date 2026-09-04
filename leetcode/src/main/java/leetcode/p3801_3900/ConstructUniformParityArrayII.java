package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/construct-uniform-parity-array-ii/">3876. Construct
 * Uniform Parity Array II</a>
 */
public class ConstructUniformParityArrayII {
  /**
   * Subtracting an even value keeps parity and subtracting an odd value flips it, and only a
   * strictly smaller value may be subtracted. The minimum has nothing smaller to subtract, so it
   * keeps its parity and every other value must match it. An odd minimum sits below every even, so
   * each even subtracts it and turns odd while the odds keep themselves: all-odd is reachable. An
   * even minimum forces all-even, but the smallest odd sees only evens below it and never flips, so
   * the array must already be all even.
   *
   * @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = nums1.length}.
   */
  public boolean uniformArray(int[] nums1) {
    boolean allEven = true;
    int min = Integer.MAX_VALUE;
    for (int num : nums1) {
      min = Math.min(min, num);
      if ((num & 1) == 1) {
        allEven = false;
      }
    }
    if ((min & 1) == 1) {
      return true;
    }
    return allEven;
  }
}
