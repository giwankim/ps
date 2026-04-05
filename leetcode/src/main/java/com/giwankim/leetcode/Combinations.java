package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class Combinations {
  public List<List<Integer>> combine(int n, int k) {
    // Time complexity: O(n! / (k! * (n - k)!)), Space complexity: O(k)
    List<List<Integer>> result = new ArrayList<>();
    backtrack(0, n, k, new ArrayList<>(), result);
    return result;
  }

  private void backtrack(
      int i, int n, int k, List<Integer> combination, List<List<Integer>> result) {
    if (combination.size() == k) {
      result.add(new ArrayList<>(combination));
      return;
    }
    if (i == n) {
      return;
    }
    combination.add(i + 1);
    backtrack(i + 1, n, k, combination, result);
    combination.removeLast();
    backtrack(i + 1, n, k, combination, result);
  }
}
