package leetcode;

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
