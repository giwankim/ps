package com.giwankim.grind75;

import com.giwankim.grind75.support.ListNode;

public class MiddleOfLinkedList {
  public ListNode middleNode(ListNode head) {
    int n = length(head);
    int mid = n / 2;
    ListNode iter = head;
    while (mid > 0) {
      iter = iter.next;
      mid -= 1;
    }
    return iter;
  }

  private int length(ListNode head) {
    int result = 0;
    ListNode iter = head;
    while (iter != null) {
      result += 1;
      iter = iter.next;
    }
    return result;
  }
}
