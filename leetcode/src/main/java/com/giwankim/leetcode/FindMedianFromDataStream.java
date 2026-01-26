package com.giwankim.leetcode;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class FindMedianFromDataStream {

  static class MedianFinder {

    private Queue<Integer> topHeap;
    private Queue<Integer> bottomHeap;

    public MedianFinder() {
      topHeap = new PriorityQueue<>();
      bottomHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    public void addNum(int num) {
      topHeap.offer(num);
      bottomHeap.offer(topHeap.poll());
      if (bottomHeap.size() > topHeap.size() + 1) {
        topHeap.offer(bottomHeap.poll());
      }
    }

    public double findMedian() {
      if (topHeap.size() == bottomHeap.size()) {
        return (topHeap.peek() + bottomHeap.peek()) / 2.0;
      }
      return bottomHeap.peek();
    }
  }
}
