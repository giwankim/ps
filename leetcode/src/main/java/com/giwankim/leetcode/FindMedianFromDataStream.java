package com.giwankim.leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FindMedianFromDataStream {
  static class MedianFinder {
    private final Queue<Integer> upper;
    private final Queue<Integer> lower;

    public MedianFinder() {
      upper = new PriorityQueue<>();
      lower = new PriorityQueue<>(Comparator.reverseOrder());
    }

    /**
     * @implNote Time {@code O(log n)}, space {@code O(1)} amortized, where {@code n} = number of
     *     elements stored.
     */
    public void addNum(int num) {
      if (lower.size() == upper.size()) {
        upper.offer(num);
        lower.offer(upper.poll());
      } else {
        lower.offer(num);
        upper.offer(lower.poll());
      }
    }

    /**
     * @implNote Time {@code O(1)}, space {@code O(1)}.
     */
    public double findMedian() {
      if (lower.size() == upper.size()) {
        return (lower.peek() + upper.peek()) / 2.0;
      }
      return lower.peek();
    }
  }
}
