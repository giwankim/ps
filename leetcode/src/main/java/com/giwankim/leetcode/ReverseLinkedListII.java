package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;
import java.util.ArrayList;
import java.util.List;

public class ReverseLinkedListII {
  public ListNode reverseBetween(ListNode head, int left, int right) {
    // Time complexity: O(n), Space complexity: O(n)
    List<ListNode> nodes = new ArrayList<>();
    for (ListNode it = head; it != null; it = it.next) {
      nodes.add(it);
    }
    reverse(nodes, left - 1, right - 1);
    for (int i = 0; i + 1 < nodes.size(); i++) {
      nodes.get(i).next = nodes.get(i + 1);
    }
    nodes.getLast().next = null;
    return nodes.getFirst();
  }

  private void reverse(List<ListNode> nodes, int left, int right) {
    while (left < right) {
      ListNode tmp = nodes.get(left);
      nodes.set(left, nodes.get(right));
      nodes.set(right, tmp);
      left++;
      right--;
    }
  }
}
