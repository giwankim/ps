package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class LinkedListCycle {
  public boolean hasCycle(ListNode head) {
    // Time Complexity: O(n), Space Complexity: O(1)
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
      if (slow == fast) {
        return true;
      }
    }
    return false;
  }
}
