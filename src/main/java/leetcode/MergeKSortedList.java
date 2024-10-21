package leetcode;

import java.util.PriorityQueue;
import leetcode.support.ListNode;

public class MergeKSortedList {
  public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
    for (ListNode node : lists) {
      if (node != null) {
        pq.offer(node);
      }
    }

    ListNode head = new ListNode(0);
    ListNode tail = head;
    while (!pq.isEmpty()) {
      ListNode node = pq.poll();
      tail.next = node;
      tail = tail.next;
      if (node.next != null) {
        pq.offer(node.next);
      }
    }
    return head.next;
  }
}
