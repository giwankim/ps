package com.giwankim.leetcode;

import java.util.*;

public class RearrangeStringKDistanceApart {
  public String rearrangeString(String s, int k) {
    Map<Character, Integer> freqMap = new HashMap<>();
    for (char c : s.toCharArray()) {
      freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
    }

    Queue<Map.Entry<Character, Integer>> maxHeap =
        new PriorityQueue<>(
            (a, b) -> {
              if (Objects.equals(a.getValue(), b.getValue())) {
                return Character.compare(a.getKey(), b.getKey());
              }
              return Integer.compare(b.getValue(), a.getValue());
            });
    maxHeap.addAll(freqMap.entrySet());

    Queue<Map.Entry<Character, Integer>> waitQueue = new LinkedList<>();

    StringBuilder sb = new StringBuilder();

    while (!maxHeap.isEmpty()) {
      Map.Entry<Character, Integer> entry = maxHeap.poll();
      sb.append(entry.getKey());
      entry.setValue(entry.getValue() - 1);
      waitQueue.offer(entry);

      if (waitQueue.size() < k) {
        continue;
      }

      Map.Entry<Character, Integer> front = waitQueue.poll();
      if (front.getValue() > 0) {
        maxHeap.offer(front);
      }
    }

    return sb.length() == s.length() ? sb.toString() : "";
  }
}
