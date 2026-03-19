package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RotateList {
  public ListNode rotateRight(ListNode head, int k) {
    // Time complexity: O(n), Space complexity: O(1)
    if (head == null || head.next == null || k == 0) {
      return head;
    }

    int n = length(head);
    k = k % n;

    if (k == 0) {
      return head;
    }

    ListNode it = head;
    for (int i = 0; i < n - k - 1; i++) {
      it = it.next;
    }

    ListNode newHead = it.next;
    it.next = null;

    ListNode tail = newHead;
    while (tail.next != null) {
      tail = tail.next;
    }
    tail.next = head;

    return newHead;
  }

  private int length(ListNode head) {
    int result = 0;
    while (head != null) {
      result += 1;
      head = head.next;
    }
    return result;
  }
}
