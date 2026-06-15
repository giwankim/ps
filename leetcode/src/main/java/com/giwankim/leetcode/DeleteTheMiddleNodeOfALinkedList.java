package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class DeleteTheMiddleNodeOfALinkedList {
  public ListNode deleteMiddle(ListNode head) {
    if (head.next == null) {
      return null;
    }
    ListNode slow = head;
    ListNode fast = head.next;
    while (fast.next != null && fast.next.next != null) {
      fast = fast.next.next;
      slow = slow.next;
    }
    slow.next = slow.next.next;
    return head;
  }
}
