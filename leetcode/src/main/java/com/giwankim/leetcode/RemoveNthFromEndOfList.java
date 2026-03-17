package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveNthFromEndOfList {
  public ListNode removeNthFromEnd(ListNode head, int n) {
    // Time Complexity: O(|linked list|), Space Complexity: O(1)
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
