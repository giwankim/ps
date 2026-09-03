package leetcode.p3801_3900;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/construct-uniform-parity-array-ii/">3876. Construct
 * Uniform Parity Array II</a>
 */
public class ConstructUniformParityArrayII {
  /**
   * Subtracting an even value keeps parity and subtracting an odd value flips it, and only a
   * strictly smaller value may be subtracted. The smallest odd value therefore never turns even
   * (everything below it is even), so all-even is reachable only when nothing is odd. For all-odd,
   * every even needs an odd below it, and the smallest even is the hardest to serve: once it clears
   * the smallest odd, every even can subtract that same odd, while the odds keep themselves.
   *
   * @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = nums1.length}.
   */
  public boolean uniformArray(int[] nums1) {
    if (Arrays.stream(nums1).allMatch(it -> (it & 1) == 0)
        || Arrays.stream(nums1).allMatch(it -> (it & 1) == 1)) {
      return true;
    }
    // must make num2 all odd
    int minOdd = Integer.MAX_VALUE;
    int minEven = Integer.MAX_VALUE;
    for (int num : nums1) {
      if ((num & 1) == 1) {
        minOdd = Math.min(minOdd, num);
      } else {
        minEven = Math.min(minEven, num);
      }
    }
    return minEven > minOdd;
  }
}
