package leetcode.p3501_3600;

/**
 * <a href="https://leetcode.com/problems/number-of-unique-xor-triplets-i/">3513. Number of Unique
 * XOR Triplets I</a>
 */
public class NumberOfUniqueXORTripletsI {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int uniqueXorTriplets(int[] nums) {
    if (nums.length <= 2) {
      return nums.length;
    }
    int result = 1;
    while (result <= nums.length) {
      result <<= 1;
    }
    return result;
  }
}
