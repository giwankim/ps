package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveDuplicatesFromSortedListII {
  /**
   * @implNote Time {@code O(n)}, space {@code O(1)}.
   */
  public ListNode deleteDuplicates(ListNode head) {
    ListNode dummy = new ListNode(-1, head);
    ListNode prev = dummy;
    ListNode it = head;
    while (it != null) {
      if (it.next != null && it.val == it.next.val) {
        while (it.next != null && it.val == it.next.val) {
          it = it.next;
        }
        prev.next = it.next;
      } else {
        prev = prev.next;
      }
      it = it.next;
    }
    return dummy.next;
  }
}
