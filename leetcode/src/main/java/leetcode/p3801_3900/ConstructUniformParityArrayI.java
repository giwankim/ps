package leetcode.p3801_3900;

/**
 * <a href="https://leetcode.com/problems/construct-uniform-parity-array-i/">3875. Construct Uniform
 * Parity Array I</a>
 */
public class ConstructUniformParityArrayI {
  /**
   * The answer is always {@code true}. With no odd value, {@code nums1} is already all even. With
   * at least one odd value {@code o}, every odd keeps itself and every even {@code e} takes
   * {@code e - o}, which is odd, so an all-odd {@code nums2} always exists.
   *
   * @implNote Time {@code O(1)}, space {@code O(1)}.
   */
  public boolean uniformArray(int[] nums1) {
    return true;
  }
}
