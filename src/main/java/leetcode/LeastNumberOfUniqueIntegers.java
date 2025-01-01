package leetcode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class LeastNumberOfUniqueIntegers {
  public int findLeastNumOfUniqueInts(int[] arr, int k) {
    Map<Integer, Integer> freqMap = new HashMap<>();
    for (int num : arr) {
      freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
    }

    PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
    minHeap.addAll(freqMap.entrySet());

    while (k > 0 && !minHeap.isEmpty()) {
      Map.Entry<Integer, Integer> entry = minHeap.poll();
      int frequency = entry.getValue();
      if (frequency <= k) {
        k -= frequency;
      } else {
        entry.setValue(frequency - k);
        minHeap.add(entry);
        k = 0;
      }
    }

    return minHeap.size();
  }
}
