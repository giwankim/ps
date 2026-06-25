package leetcode;

import java.util.Arrays;

public class SearchInsertPosition {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int searchInsert(int[] nums, int target) {
    int lo = 0;
    int hi = nums.length;
    while (lo < hi) {
      int mid = lo + (hi - lo) / 2;
      if (nums[mid] >= target) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    return lo;
  }

  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int searchInsert2(int[] nums, int target) {
    int index = Arrays.binarySearch(nums, target);
    return index >= 0 ? index : -index - 1;
  }
}
