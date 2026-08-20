package leetcode.p0001_0100;

import leetcode.support.ListNode;

/**
 * <a href="https://leetcode.com/problems/remove-nth-node-from-end-of-list/">19. Remove Nth Node
 * From End of List</a>
 */
public class RemoveNthFromEndOfList {
  /** @implNote Time {@code O(|linked list|)}, space {@code O(1)}. */
  public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(-1, head);

    ListNode fast = dummy;
    while (n-- > 0) {
      fast = fast.next;
    }

    ListNode slow = dummy;
    while (fast.next != null) {
      fast = fast.next;
      slow = slow.next;
    }

    slow.next = slow.next.next;
    return dummy.next;
  }
}
