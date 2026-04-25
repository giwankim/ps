package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
  /**
   * @implNote Time {@code O(N^(T/M + 1))}, space {@code O(T/M)} excluding the output, where
   *     {@code N = candidates.length}, {@code T = target}, and {@code M = min(candidates)}.
   *     Each recursive call branches by trying every remaining candidate (factor up to
   *     {@code N}) and subtracts at least {@code M} from the target, capping the depth at
   *     {@code T/M}; overshooting branches are discarded by the {@code target < 0} guard on
   *     entry. Auxiliary space is the recursion stack plus the shared {@code current} list
   *     of the same depth.
   */
  public List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    combinationSum(candidates, target, 0, new ArrayList<>(), result);
    return result;
  }

  private void combinationSum(
      int[] candidates, int target, int index, List<Integer> current, List<List<Integer>> result) {
    if (target == 0) {
      result.add(List.copyOf(current));
      return;
    }
    if (target < 0) {
      return;
    }
    for (int pick = index; pick < candidates.length; pick++) {
      current.add(candidates[pick]);
      combinationSum(candidates, target - candidates[pick], pick, current, result);
      current.removeLast();
    }
  }
}
