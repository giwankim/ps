package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class PartitionList {
  public ListNode partition(ListNode head, int x) {
    // Time complexity: O(n), Space complexity: O(1)
    ListNode head1 = new ListNode();
    ListNode tail1 = head1;
    ListNode head2 = new ListNode();
    ListNode tail2 = head2;

    for (ListNode it = head; it != null; it = it.next) {
      if (it.val < x) {
        tail1.next = it;
        tail1 = tail1.next;
      } else {
        tail2.next = it;
        tail2 = tail2.next;
      }
    }

    tail1.next = head2.next;
    tail2.next = null;

    return head1.next;
  }
}
