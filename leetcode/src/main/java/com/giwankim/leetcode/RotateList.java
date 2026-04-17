package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RotateList {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public ListNode rotateRight(ListNode head, int k) {
    if (head == null) {
      return null;
    }

    // circular list
    int n = 1;
    ListNode tail = head;
    while (tail.next != null) {
      tail = tail.next;
      n += 1;
    }
    tail.next = head;

    k %= n;

    for (int i = 0; i < n - k; i++) {
      tail = tail.next;
    }

    ListNode newHead = tail.next;
    tail.next = null;
    return newHead;
  }
}
