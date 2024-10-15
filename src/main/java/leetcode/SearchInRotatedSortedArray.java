package leetcode;

import java.util.Arrays;

public class SearchInRotatedSortedArray {
  public int search(int[] nums, int target) {
    int pivot = getPivot(nums);
    int result = Arrays.binarySearch(nums, 0, pivot, target);
    if (result >= 0) {
      return result;
    }
    result = Arrays.binarySearch(nums, pivot, nums.length, target);
    return result >= 0 ? result : -1;
  }

  public int getPivot(int[] nums) {
    int l = 0;
    int r = nums.length - 1;
    while (l < r) {
      int mid = l + (r - l) / 2;
      if (nums[mid] < nums[r]) { // is either the pivot or pivot is to the left
        r = mid;
      } else {
        l = mid + 1;
      }
    }
    return l;
  }
}
