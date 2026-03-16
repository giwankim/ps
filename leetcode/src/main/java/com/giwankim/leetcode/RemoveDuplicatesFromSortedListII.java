package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveDuplicatesFromSortedListII {
  public ListNode deleteDuplicates(ListNode head) {
    int[] counts = new int[201];
    for (ListNode it = head; it != null; it = it.next) {
      counts[it.val + 100]++;
    }

    ListNode dummy = new ListNode();
    ListNode tail = dummy;
    for (ListNode it = head; it != null; it = it.next) {
      if (counts[it.val + 100] > 1) {
        continue;
      }
      tail.next = it;
      tail = tail.next;
    }
    tail.next = null;

    return dummy.next;
  }
}
