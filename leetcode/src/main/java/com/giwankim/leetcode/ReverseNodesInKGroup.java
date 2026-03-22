package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;

public class ReverseNodesInKGroup {
  public ListNode reverseKGroup(ListNode head, int k) {
    // Time complexity: O(n), Space complexity: O(n)
    int n = length(head);
    ListNode[] nodes = new ListNode[n];
    int i = 0;
    for (ListNode it = head; it != null; it = it.next) {
      nodes[i++] = it;
    }
    for (int j = 0; j + k - 1 < n; j += k) {
      reverse(nodes, j, j + k - 1);
    }
    for (int j = 0; j + 1 < n; j++) {
      nodes[j].next = nodes[j + 1];
    }
    nodes[n - 1].next = null;
    return nodes[0];
  }

  private int length(ListNode head) {
    int result = 0;
    for (ListNode it = head; it != null; it = it.next) {
      result += 1;
    }
    return result;
  }

  private void reverse(ListNode[] nodes, int left, int right) {
    while (left < right) {
      ListNode tmp = nodes[left];
      nodes[left] = nodes[right];
      nodes[right] = tmp;
      left += 1;
      right -= 1;
    }
  }
}
