package com.giwankim.leetcode;

public class MergeSortedArray {

  public void merge(int[] nums1, int m, int[] nums2, int n) {
    // Time complexity: O(n + m), Space complexity: O(1)
    int i = m - 1;
    int j = n - 1;
    int k = n + m - 1;

    while (i >= 0 || j >= 0) {
      if (i < 0) {
        nums1[k] = nums2[j];
        j -= 1;
      } else if (j < 0) {
        nums1[k] = nums1[i];
        i -= 1;
      } else if (nums1[i] < nums2[j]) {
        nums1[k] = nums2[j];
        j -= 1;
      } else {
        nums1[k] = nums1[i];
        i -= 1;
      }
      k -= 1;
    }
  }
}
