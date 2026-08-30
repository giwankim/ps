package leetcode.p2001_2100;

/**
 * <a href="https://leetcode.com/problems/removing-minimum-and-maximum-from-array/">2091. Removing
 * Minimum and Maximum From Array</a>
 */
public class RemovingMinimumAndMaximumFromArray {
  /**
   * @implNote Time {@code O(n)} for the single scan locating the two extreme indices, auxiliary
   *     space {@code O(1)} since only the indices and the three scenario costs are kept, where
   *     {@code n = nums.length}.
   */
  public int minimumDeletions(int[] nums) {
    int n = nums.length;
    int min = 0;
    int max = 0;
    for (int i = 0; i < n; i++) {
      if (nums[i] < nums[min]) {
        min = i;
      }
      if (nums[i] > nums[max]) {
        max = i;
      }
    }
    int front = 1 + Math.max(min, max);
    int back = Math.max(n - max, n - min);
    int mix = 1 + Math.min(min, max) + Math.min(n - max, n - min);
    return Math.min(Math.min(front, back), mix);
  }
}
