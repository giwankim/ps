package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permutations {
  /**
   * @implNote Time {@code O(n * n!)}, auxiliary space {@code O(n)}, where {@code n =
   *     nums.length}.
   */
  public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    permute(nums, 0, result);
    return result;
  }

  private void permute(int[] nums, int index, List<List<Integer>> result) {
    if (index == nums.length) {
      result.add(Arrays.stream(nums).boxed().toList());
      return;
    }
    for (int i = index; i < nums.length; i++) {
      swap(nums, index, i);
      permute(nums, index + 1, result);
      swap(nums, i, index);
    }
  }

  private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }
}
