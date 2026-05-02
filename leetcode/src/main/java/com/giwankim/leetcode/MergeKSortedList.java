package com.giwankim.leetcode;

import com.giwankim.leetcode.support.ListNode;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MergeKSortedList {
  /**
   * @implNote Time {@code O(N log k)}, space {@code O(k)}.
   *     <p>{@code k} = {@code lists.length}, {@code N} = total number of nodes across all
   *     lists. Each node is polled from the heap exactly once and at most one successor is
   *     offered back, so the heap size never exceeds {@code k} and each {@code poll} /
   *     {@code offer} costs {@code O(log k)}.
   */
  public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
    for (ListNode list : lists) {
      if (list != null) {
        pq.offer(list);
      }
    }

    ListNode dummy = new ListNode();
    ListNode tail = dummy;
    while (!pq.isEmpty()) {
      ListNode node = pq.poll();
      if (node.next != null) {
        pq.offer(node.next);
      }
      tail.next = node;
      tail = tail.next;
    }
    return dummy.next;
  }
}
