package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveNthFromEnd {
  public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    ListNode first = dummy;
    while (n-- > 0) {
      first = first.next;
    }

    ListNode prev = dummy;
    while (first.next != null) {
      first = first.next;
      prev = prev.next;
    }

    prev.next = prev.next.next;

    return dummy.next;
  }
}
