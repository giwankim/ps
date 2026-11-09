package leetcode.p1601_1700;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * <a href="https://leetcode.com/problems/find-servers-that-handled-most-number-of-requests/">1606.
 * Find Servers That Handled Most Number of Requests</a>
 */
public class FindServersThatHandledMostNumberOfRequests {
  /**
   * @implNote Time {@code O((n + k) log k)}, space {@code O(k)}, where {@code n = arrival.length}:
   *     seeding {@code free} takes {@code k} inserts, and each request does at most one
   *     {@code ceiling}, one {@code remove}, and one {@code offer}. The drain loop is amortized,
   *     since every {@code poll} matches an earlier {@code offer}. Each server is in exactly one of
   *     {@code free} and {@code busy}, so both stay within {@code k} entries.
   */
  public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
    int[] reqs = new int[k];

    TreeSet<Integer> free = new TreeSet<>();
    for (int i = 0; i < k; i++) {
      free.add(i);
    }
    PriorityQueue<int[]> busy = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    for (int i = 0; i < arrival.length; i++) {
      // drain the busy queue
      while (!busy.isEmpty() && busy.peek()[0] <= arrival[i]) {
        int server = busy.poll()[1];
        free.add(server);
      }
      if (free.isEmpty()) {
        continue;
      }
      Integer next = free.ceiling(i % k);
      if (next == null) {
        next = free.first();
      }
      free.remove(next);
      reqs[next]++;
      busy.offer(new int[] {arrival[i] + load[i], next});
    }

    int max = 0;
    for (int req : reqs) {
      max = Math.max(max, req);
    }

    ArrayList<Integer> ans = new ArrayList<>();
    for (int i = 0; i < k; i++) {
      if (reqs[i] == max) {
        ans.add(i);
      }
    }
    return ans;
  }
}
