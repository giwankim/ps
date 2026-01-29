package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class ReorderList {
  public void reorderList(ListNode head) {
    // find middle
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }

    // reverse back half
    ListNode front = head;
    ListNode back = reverse(slow);

    // interlace
    while (back.next != null) {
      ListNode temp = front.next;
      front.next = back;
      front = temp;

      temp = back.next;
      back.next = front;
      back = temp;
    }
  }

  public ListNode reverse(ListNode head) {
    ListNode iter = head;
    ListNode prev = null;
    while (iter != null) {
      ListNode temp = iter.next;
      iter.next = prev;
      prev = iter;
      iter = temp;
    }
    return prev;
  }
}
