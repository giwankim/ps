package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class LinkedListCycle {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public boolean hasCycle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
      if (slow == fast) {
        return true;
      }
    }
    return false;
  }
}
