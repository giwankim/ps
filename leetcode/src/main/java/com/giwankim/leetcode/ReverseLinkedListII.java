package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class ReverseLinkedListII {
  public ListNode reverseBetween(ListNode head, int left, int right) {
    // Time complexity: O(n), Space complexity: O(1)
    ListNode dummy = new ListNode(-1, head);
    ListNode prev = dummy;
    for (int i = 0; i + 1 < left; i++) {
      prev = prev.next;
    }
    ListNode curr = prev.next;
    for (int i = 0; i < right - left; i++) {
      ListNode nextNode = curr.next;
      // Point curr past nextNode
      curr.next = nextNode.next;
      // Insert nextNode right after prev
      nextNode.next = prev.next;
      prev.next = nextNode;
    }
    return dummy.next;
  }
}
