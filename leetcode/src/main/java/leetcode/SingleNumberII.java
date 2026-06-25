package leetcode;

public class SingleNumberII {
  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length}. */
  public int singleNumber(int[] nums) {
    int result = 0;
    for (int i = 0; i < 32; i++) {
      int columnSum = 0;
      for (int num : nums) {
        columnSum += (num >> i) & 1;
      }
      columnSum %= 3;
      result |= columnSum << i;
    }
    return result;
  }

  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = nums.length}. */
  public int singleNumber2(int[] nums) {
    int ones = 0;
    int twos = 0;
    for (int num : nums) {
      ones = (ones ^ num) & ~twos;
      twos = (twos ^ num) & ~ones;
    }
    return ones;
  }
}
