package com.giwankim.grind75;

import com.giwankim.grind75.support.ListNode;

public class ReverseLinkedList {

  public ListNode reverseList(ListNode head) {
    if (head == null) {
      return null;
    }

    ListNode prev = null;
    ListNode iter = head;

    // iterate trhrough linked list and reverse link one-by-one to point to previous node
    while (iter != null) {
      ListNode temp = iter.next; // temporarily store next node
      iter.next = prev;
      prev = iter;
      iter = temp;
    }

    return prev;
  }
}
