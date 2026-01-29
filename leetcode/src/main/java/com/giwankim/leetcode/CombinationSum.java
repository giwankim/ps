package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
  public List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, new ArrayList<>(), result, candidates, target);
    return result;
  }

  private void backtrack(
      int start,
      List<Integer> combination,
      List<List<Integer>> result,
      int[] candidates,
      int target) {
    if (target < 0) {
      return;
    }
    if (target == 0) {
      result.add(combination.stream().toList());
      return;
    }
    for (int i = start; i < candidates.length; i++) {
      combination.add(candidates[i]);
      backtrack(i, combination, result, candidates, target - candidates[i]);
      combination.removeLast();
    }
  }
}
