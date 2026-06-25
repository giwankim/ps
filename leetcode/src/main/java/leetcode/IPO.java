package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class IPO {
  /** @implNote Time {@code O(n log n)}, space {@code O(n)}, where {@code n = profits.length}. */
  public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
    PriorityQueue<Integer> byProfit =
        new PriorityQueue<>((a, b) -> Integer.compare(profits[b], profits[a]));
    PriorityQueue<Integer> byCapital =
        new PriorityQueue<>((a, b) -> Integer.compare(capital[a], capital[b]));
    for (int i = 0; i < profits.length; i++) {
      if (capital[i] <= w) {
        byProfit.offer(i);
      } else {
        byCapital.offer(i);
      }
    }
    while (k-- > 0 && !byProfit.isEmpty()) {
      w += profits[byProfit.poll()];
      while (!byCapital.isEmpty() && capital[byCapital.peek()] <= w) {
        byProfit.offer(byCapital.poll());
      }
    }
    return w;
  }

  /** @implNote Time {@code O(n log n)}, space {@code O(n)}, where {@code n = profits.length}. */
  public int findMaximizedCapital2(int k, int w, int[] profits, int[] capital) {
    int n = profits.length;

    List<Integer> byCapital = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      byCapital.add(i);
    }
    byCapital.sort((a, b) -> Integer.compare(capital[a], capital[b]));

    PriorityQueue<Integer> byProfit =
        new PriorityQueue<>((a, b) -> Integer.compare(profits[b], profits[a]));
    int next = 0;
    while (k-- > 0) {
      while (next < n && capital[byCapital.get(next)] <= w) {
        byProfit.offer(byCapital.get(next));
        next++;
      }
      if (byProfit.isEmpty()) {
        break;
      }
      w += profits[byProfit.poll()];
    }
    return w;
  }
}
