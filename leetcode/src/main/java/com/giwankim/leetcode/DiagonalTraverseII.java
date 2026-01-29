package com.giwankim.leetcode;

import java.util.*;

public class DiagonalTraverseII {
  public int[] findDiagonalOrder(List<List<Integer>> nums) {
    List<Integer> diagonals = new ArrayList<>();

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[] {0, 0});

    while (!queue.isEmpty()) {
      int[] coordinates = queue.poll();
      int i = coordinates[0];
      int j = coordinates[1];
      diagonals.add(nums.get(i).get(j));
      if (j == 0 && i + 1 < nums.size()) {
        queue.offer(new int[] {i + 1, j});
      }
      if (j + 1 < nums.get(i).size()) {
        queue.offer(new int[] {i, j + 1});
      }
    }

    int[] result = new int[diagonals.size()];
    for (int i = 0; i < diagonals.size(); i++) {
      result[i] = diagonals.get(i);
    }

    return result;
  }
}
