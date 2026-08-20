package leetcode.p3001_3100;

import java.util.ArrayList;
import java.util.List;

public class DistributeElementsIntoTwoArraysI {
  /**
   * @implNote Time {@code O(n)} for the distribution pass plus the concatenation, auxiliary space
   *     {@code O(n)} for the two lists, where {@code n = nums.length}.
   */
  public int[] resultArray(int[] nums) {
    List<Integer> arr1 = new ArrayList<>();
    arr1.add(nums[0]);
    List<Integer> arr2 = new ArrayList<>();
    arr2.add(nums[1]);
    for (int i = 2; i < nums.length; i++) {
      if (arr1.getLast() > arr2.getLast()) {
        arr1.add(nums[i]);
      } else {
        arr2.add(nums[i]);
      }
    }
    int[] result = new int[nums.length];
    int i = 0;
    for (int x : arr1) {
      result[i++] = x;
    }
    for (int x : arr2) {
      result[i++] = x;
    }
    return result;
  }
}
