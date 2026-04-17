package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveNthFromEndOfList {
  /**
   * @implNote Time {@code O(|linked list|)}, space {@code O(1)}.
   */
  public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(-1, head);

    ListNode fast = dummy;
    while (n-- > 0) {
      fast = fast.next;
    }

    ListNode slow = dummy;
    while (fast.next != null) {
      fast = fast.next;
      slow = slow.next;
    }

    slow.next = slow.next.next;
    return dummy.next;
  }
}
