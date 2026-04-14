package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SnakesAndLadders {
  public int snakesAndLadders(int[][] board) {
    int n = board.length;
    List<List<Integer>> cells = getCells(n);
    int result = 0;
    boolean[] visited = new boolean[n * n + 1];
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(1);
    visited[1] = true;
    while (!queue.isEmpty()) {
      int size = queue.size();
      while (size-- > 0) {
        int curr = queue.poll();
        if (curr == n * n) {
          return result;
        }
        for (int next = curr + 1; next <= Math.min(curr + 6, n * n); next++) {
          int r = cells.get(next).getFirst();
          int c = cells.get(next).getLast();
          int destination = next;
          if (board[r][c] != -1) {
            destination = board[r][c];
          }
          if (visited[destination]) {
            continue;
          }
          queue.offer(destination);
          visited[destination] = true;
        }
      }
      result += 1;
    }
    return -1;
  }

  private static List<List<Integer>> getCells(int n) {
    List<List<Integer>> cells = new ArrayList<>();
    cells.add(Collections.emptyList());
    boolean isReverse = false;
    for (int i = n - 1; i >= 0; i--) {
      if (!isReverse) {
        for (int j = 0; j < n; j++) {
          cells.add(List.of(i, j));
        }
      } else {
        for (int j = n - 1; j >= 0; j--) {
          cells.add(List.of(i, j));
        }
      }
      isReverse = !isReverse;
    }
    return cells;
  }
}
