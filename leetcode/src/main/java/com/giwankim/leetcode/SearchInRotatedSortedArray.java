package com.giwankim.leetcode;

@SuppressWarnings("unused")
public class SearchInRotatedSortedArray {
  public int search(int[] nums, int target) {
    int pivot = getPivot(nums);
    int l = 0;
    int r = nums.length - 1;
    while (l <= r) {
      int mid = l + (r - l) / 2;
      int midPivot = (mid + pivot) % nums.length;
      if (nums[midPivot] < target) {
        l = mid + 1;
      } else if (nums[midPivot] > target) {
        r = mid - 1;
      } else {
        return midPivot;
      }
    }
    return -1;
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
