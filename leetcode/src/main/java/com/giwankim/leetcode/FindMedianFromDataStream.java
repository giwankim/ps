package com.giwankim.leetcode;

import java.util.PriorityQueue;
import java.util.Queue;

public class FindMedianFromDataStream {
  static class MedianFinder {
    private final Queue<Integer> upper;
    private final Queue<Integer> lower;

    public MedianFinder() {
      upper = new PriorityQueue<>();
      lower = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
    }

    /**
     * @implNote Time {@code O(log n)}, space {@code O(1)} amortized, where {@code n} = number of
     *     elements stored.
     */
    public void addNum(int num) {
      upper.offer(num);
      lower.offer(upper.poll());
      if (lower.size() > upper.size() + 1) {
        upper.offer(lower.poll());
      }
    }

    /**
     * @implNote Time {@code O(1)}, space {@code O(1)}.
     */
    public double findMedian() {
      if (lower.size() > upper.size()) {
        return lower.peek();
      }
      return (lower.peek() + upper.peek()) / 2.0;
    }
  }
}
