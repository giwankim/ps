package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinStack {
  // Time complexity: O(1), Space complexity: O(n)
  private final Deque<Pair<Integer, Integer>> stack;

  public MinStack() {
    stack = new ArrayDeque<>();
  }

  public void push(int val) {
    if (stack.isEmpty()) {
      stack.push(new Pair<>(val, val));
      return;
    }
    stack.push(new Pair<>(val, Math.min(val, getMin())));
  }

  public void pop() {
    stack.pop();
  }

  public int top() {
    return stack.peek().first;
  }

  public int getMin() {
    return stack.peek().second;
  }

  private record Pair<A, B>(A first, B second) {}
}
