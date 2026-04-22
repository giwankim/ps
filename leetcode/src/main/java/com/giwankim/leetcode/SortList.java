package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class SortList {
  /**
   * @implNote Time {@code O(n log n)}, space {@code O(log n)}, where {@code n = list length}.
   */
  public ListNode sortList(ListNode head) {
    if (head == null || head.next == null) {
      return head;
    }
    ListNode right = split(head);
    ListNode leftSorted = sortList(head);
    ListNode rightSorted = sortList(right);
    return merge(leftSorted, rightSorted);
  }

  private ListNode split(ListNode head) {
    ListNode dummy = new ListNode(-1, head);
    ListNode slow = dummy;
    ListNode fast = dummy;

    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }

    ListNode left = slow.next;
    slow.next = null;

    return left;
  }

  private ListNode merge(ListNode a, ListNode b) {
    ListNode dummy = new ListNode();
    ListNode tail = dummy;

    while (a != null && b != null) {
      if (a.val <= b.val) {
        tail.next = a;
        a = a.next;
      } else {
        tail.next = b;
        b = b.next;
      }
      tail = tail.next;
    }

    tail.next = a != null ? a : b;

    return dummy.next;
  }
}
