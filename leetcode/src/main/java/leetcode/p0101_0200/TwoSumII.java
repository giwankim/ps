package leetcode.p0101_0200;

/**
 * <a href="https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/">167. Two Sum II - Input
 * Array Is Sorted</a>
 */
public class TwoSumII {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public int[] twoSum(int[] numbers, int target) {
    int i = 0;
    int j = numbers.length - 1;
    while (i < j) {
      int diff = numbers[i] + numbers[j] - target;
      if (diff == 0) {
        return new int[] {i + 1, j + 1};
      } else if (diff < 0) {
        i++;
      } else {
        j--;
      }
    }
    return new int[0];
  }
}
