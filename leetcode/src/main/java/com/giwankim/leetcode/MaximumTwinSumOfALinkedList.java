package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;
import java.util.ArrayList;
import java.util.List;

public class MaximumTwinSumOfALinkedList {
  public int pairSum(ListNode head) {
    int result = 0;
    List<Integer> values = new ArrayList<>();
    while (head != null) {
      values.add(head.val);
      head = head.next;
    }

    int l = 0;
    int r = values.size() - 1;
    while (l < r) {
      result = Math.max(result, values.get(l) + values.get(r));
      l += 1;
      r -= 1;
    }
    return result;
  }
}
