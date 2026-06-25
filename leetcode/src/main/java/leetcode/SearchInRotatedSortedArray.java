package leetcode;

import java.util.Arrays;

public class SearchInRotatedSortedArray {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int search(int[] nums, int target) {
    int minIndex = findMinIndex(nums);
    int leftResult = Arrays.binarySearch(nums, 0, minIndex, target);
    if (leftResult >= 0) {
      return leftResult;
    }
    int rightResult = Arrays.binarySearch(nums, minIndex, nums.length, target);
    if (rightResult >= 0) {
      return rightResult;
    }
    return -1;
  }

  private int findMinIndex(int[] nums) {
    int result = -1;
    int lo = 0;
    int hi = nums.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (nums[mid] <= nums[nums.length - 1]) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }
}
