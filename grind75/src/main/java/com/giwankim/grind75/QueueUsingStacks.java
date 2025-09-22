package com.giwankim.grind75;

import java.util.ArrayDeque;
import java.util.Deque;

public class QueueUsingStacks {
  static class MyQueue {
    private Deque<Integer> in;
    private Deque<Integer> out;

    public MyQueue() {
      in = new ArrayDeque<>();
      out = new ArrayDeque<>();
    }

    public void push(int x) {
      in.push(x);
    }

    public int pop() {
      rebalance();
      return out.pop();
    }

    public int peek() {
      rebalance();
      return out.peek();
    }

    private void rebalance() {
      if (!out.isEmpty()) {
        return;
      }
      while (!in.isEmpty()) {
        out.push(in.pop());
      }
    }

    public boolean empty() {
      return in.isEmpty() && out.isEmpty();
    }
  }
}
