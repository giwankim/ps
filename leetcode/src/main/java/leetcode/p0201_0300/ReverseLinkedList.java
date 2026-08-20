package leetcode.p0201_0300;

import leetcode.support.ListNode;

/** <a href="https://leetcode.com/problems/reverse-linked-list/">206. Reverse Linked List</a> */
public class ReverseLinkedList {
  public ListNode reverseList(ListNode head) {
    ListNode node = head;
    ListNode prev = null;
    while (node != null) {
      ListNode next = node.next;
      node.next = prev;
      prev = node;
      node = next;
    }
    return prev;
  }
}
