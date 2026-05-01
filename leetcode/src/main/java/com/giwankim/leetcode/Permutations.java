package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
  /**
   * @implNote Time {@code O(n * n!)}, auxiliary space {@code O(n)} excluding
   *     the output, where {@code n = nums.length}.
   *     <p>The recursion tree branches by {@code (n - index)} at depth
   *     {@code index}, yielding {@code n!} leaves; each leaf copies
   *     {@code nums} into a fresh {@link ArrayList} for {@code O(n)} work,
   *     dominating the running time. Auxiliary space is the recursion depth
   *     ({@code n}) — the in-place swap reuses {@code nums} as the working
   *     permutation, avoiding the "used" set or current-permutation buffer
   *     the non-mutating form would need. The output list itself holds
   *     {@code O(n * n!)} integers.
   */
  public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    permute(nums, 0, result);
    return result;
  }

  private void permute(int[] nums, int index, List<List<Integer>> result) {
    if (index == nums.length) {
      result.add(of(nums));
      return;
    }
    for (int i = index; i < nums.length; i++) {
      swap(nums, index, i);
      permute(nums, index + 1, result);
      swap(nums, i, index);
    }
  }

  /**
   * @implNote Time {@code O(n * n!)}, auxiliary space {@code O(n)} excluding the output, where
   *     {@code n = nums.length}.
   *     <p>The non-mutating variant: an {@code O(1)}-lookup {@code used} flag array marks the
   *     indices already picked, and the recursion appends to a shared {@code current} buffer. The
   *     loop at depth {@code d} scans all {@code n} indices and skips the {@code d} already-used
   *     ones, yielding {@code (n - d)} effective children — the same {@code n!} leaves as the
   *     swap-based form. Each leaf copies {@code current} into a fresh {@link ArrayList} for
   *     {@code O(n)} work; this and the {@code O(n)} per-node loop scan each contribute
   *     {@code O(n * n!)} total, the same overall bound as {@link #permute(int[])} but for a
   *     different reason (the swap form's per-node loop is only {@code O(n!)} since it runs
   *     {@code (n - d)} times rather than {@code n}). Auxiliary space is the {@code O(n)}
   *     {@code used} array plus the {@code O(n)} {@code current} buffer plus the {@code O(n)}
   *     recursion stack; the input array is left untouched. The output list itself holds
   *     {@code O(n * n!)} integers.
   */
  public List<List<Integer>> permute2(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    permute2(nums, new ArrayList<>(), new boolean[nums.length], result);
    return result;
  }

  private void permute2(
      int[] nums, List<Integer> current, boolean[] used, List<List<Integer>> result) {
    if (current.size() == nums.length) {
      result.add(new ArrayList<>(current));
      return;
    }
    for (int i = 0; i < nums.length; i++) {
      if (used[i]) {
        continue;
      }
      current.add(nums[i]);
      used[i] = true;
      permute2(nums, current, used, result);
      current.removeLast();
      used[i] = false;
    }
  }

  private static void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }

  private static List<Integer> of(int[] nums) {
    List<Integer> result = new ArrayList<>(nums.length);
    for (int num : nums) {
      result.add(num);
    }
    return result;
  }
}
