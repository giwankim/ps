package leetcode.p0001_0100;

import leetcode.support.ListNode;

/**
 * <a href="https://leetcode.com/problems/reverse-linked-list-ii/">92. Reverse Linked List II</a>
 */
public class ReverseLinkedListII {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public ListNode reverseBetween(ListNode head, int left, int right) {
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
