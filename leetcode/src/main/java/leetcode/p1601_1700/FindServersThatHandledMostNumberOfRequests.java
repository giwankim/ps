package leetcode.p1601_1700;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/find-servers-that-handled-most-number-of-requests/">1606.
 * Find Servers That Handled Most Number of Requests</a>
 */
public class FindServersThatHandledMostNumberOfRequests {
  public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
    int[] available = new int[k];
    int[] reqs = new int[k];
    int n = arrival.length;
    for (int i = 0; i < n; i++) {
      // find the next available server
      int next = i % k;
      while (available[next] > arrival[i]) {
        next = (next + 1) % k;
        // infinite loop, all servers are busy?
        if (next == i % k) {
          next = -1;
          break;
        }
      }
      if (next != -1) {
        available[next] = arrival[i] + load[i];
        reqs[next]++;
      }
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
