package leetcode;

import java.util.ArrayList;
import java.util.List;

public class MergeSortedArray {
  public void merge(int[] nums1, int m, int[] nums2, int n) {
    List<Integer> result = new ArrayList<>();

    int i = 0;
    int j = 0;
    while (i < m && j < n) {
      if (nums1[i] <= nums2[j]) {
        result.add(nums1[i]);
        i += 1;
      } else {
        result.add(nums2[j]);
        j += 1;
      }
    }
    while (i < m) {
      result.add(nums1[i]);
      i += 1;
    }
    while (j < n) {
      result.add(nums2[j]);
      j += 1;
    }

    for (int k = 0; k < result.size(); k++) {
      nums1[k] = result.get(k);
    }
  }
}
