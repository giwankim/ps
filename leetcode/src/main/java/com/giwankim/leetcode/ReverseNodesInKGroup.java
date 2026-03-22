package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class ReverseNodesInKGroup {
  public ListNode reverseKGroup(ListNode head, int k) {
    // Time complexity: O(n), Space complexity: O(1)
    int n = length(head);
    ListNode dummy = new ListNode(-1, head);
    ListNode prev = dummy;
    for (int i = 0; i + k <= n; i += k) {
      ListNode curr = prev.next;
      for (int j = 0; j + 1 < k; j++) {
        ListNode next = curr.next;
        curr.next = next.next;
        next.next = prev.next;
        prev.next = next;
      }
      prev = curr;
    }
    return dummy.next;
  }

  private int length(ListNode head) {
    int result = 0;
    for (ListNode it = head; it != null; it = it.next) {
      result += 1;
    }
    return result;
  }
}
