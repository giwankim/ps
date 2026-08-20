package leetcode.p1401_1500;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <a href="https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/">1481.
 * Least Number of Unique Integers after K Removals</a>
 */
public class LeastNumberOfUniqueIntegers {
  public int findLeastNumOfUniqueInts(int[] arr, int k) {
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : arr) {
      freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    PriorityQueue<Integer> pq = new PriorityQueue<>(freqMap.values());

    while (k > 0 && !pq.isEmpty()) {
      int freq = pq.poll();
      if (freq <= k) {
        k -= freq;
      } else {
        pq.add(freq - k);
        k = 0;
      }
    }

    return pq.size();
  }
}
