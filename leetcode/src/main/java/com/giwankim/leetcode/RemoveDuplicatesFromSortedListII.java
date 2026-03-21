package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveDuplicatesFromSortedListII {
  public ListNode deleteDuplicates(ListNode head) {
    // Time Complexity: O(n), Space Complexity: O(1)
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
