package leetcode.p0101_0200;

/** <a href="https://leetcode.com/problems/single-number/">136. Single Number</a> */
public class SingleNumber {
  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length}. */
  public int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
      result ^= num;
    }
    return result;
  }
}
