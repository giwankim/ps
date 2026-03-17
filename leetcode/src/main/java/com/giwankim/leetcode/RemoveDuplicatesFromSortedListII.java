package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class RemoveDuplicatesFromSortedListII {
  public ListNode deleteDuplicates(ListNode head) {
    // Time Complexity: O(n), Space Complexity: O(1)
    ListNode dummy = new ListNode();
    ListNode tail = dummy;
    ListNode it = head;
    while (it != null) {
      if (it.next != null && it.val == it.next.val) {
        // skip duplicates
        int val = it.val;
        while (it != null && it.val == val) {
          it = it.next;
        }
        continue;
      }
      tail.next = it;
      tail = tail.next;
      it = it.next;
    }
    tail.next = null;
    return dummy.next;
  }
}
