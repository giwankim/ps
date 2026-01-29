package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SlidingWindowMaximum {
  public int[] maxSlidingWindow(int[] nums, int k) {
    List<Integer> result = new ArrayList<>();
    // store indices nums[deque] is decreasing so head is always maximum
    Deque<Integer> deque = new ArrayDeque<>();
    for (int i = 0; i < nums.length; i++) {
      // trim window to size k
      while (!deque.isEmpty() && deque.peek() <= i - k) {
        deque.poll();
      }
      // remove elements from the back of deque to maintain decreasing invariant
      while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
        deque.pollLast();
      }

      deque.addLast(i);

      // add max to result
      if (i >= k - 1) {
        result.add(nums[deque.peek()]);
      }
    }
    return result.stream().mapToInt(Integer::intValue).toArray();
  }
}
