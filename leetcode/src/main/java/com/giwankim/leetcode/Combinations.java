package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class Combinations {
  /**
   * @implNote Time {@code O(k * C(n, k))}, auxiliary space {@code O(k)} excluding the output,
   *     where {@code C(n, k) = n! / (k! * (n - k)!)}.
   *     <p>The for-loop backtracking visits exactly {@code C(n, k)} leaves — one per
   *     strictly-increasing length-{@code k} subsequence of {@code [1, n]} — and each leaf
   *     snapshots the shared {@link ArrayList} into a fresh list for {@code O(k)} work, dominating
   *     the running time. The {@code start = i + 1} advance ensures each combination is reached
   *     exactly once. Auxiliary space is the recursion depth (capped at {@code k}, since the stack
   *     only grows when a value is appended to {@code current}) plus the {@code O(k)}
   *     {@code current} buffer; the output list itself holds {@code O(k * C(n, k))} integers.
   */
  public List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    combination(n, k, 1, new ArrayList<>(), result);
    return result;
  }

  private void combination(
      int n, int k, int start, List<Integer> current, List<List<Integer>> result) {
    if (current.size() == k) {
      result.add(new ArrayList<>(current));
      return;
    }
    for (int i = start; i <= n; i++) {
      current.add(i);
      combination(n, k, i + 1, current, result);
      current.removeLast();
    }
  }
}
