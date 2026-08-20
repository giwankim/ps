package leetcode.p2001_2100;

import leetcode.support.ListNode;

/**
 * <a href="https://leetcode.com/problems/delete-the-middle-node-of-a-linked-list/">2095. Delete the
 * Middle Node of a Linked List</a>
 */
public class DeleteTheMiddleNodeOfALinkedList {
  public ListNode deleteMiddle(ListNode head) {
    if (head.next == null) {
      return null;
    }
    ListNode slow = head;
    ListNode fast = head.next;
    while (fast.next != null && fast.next.next != null) {
      fast = fast.next.next;
      slow = slow.next;
    }
    slow.next = slow.next.next;
    return head;
  }
}
