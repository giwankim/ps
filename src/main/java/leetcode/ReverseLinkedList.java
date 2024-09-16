package leetcode;

import datatype.ListNode;

public class ReverseLinkedList {
  public ListNode reverseList(ListNode head) {
    return reverseList(head, null);
  }

  public ListNode reverseList(ListNode head, ListNode prev) {
    if (head == null) {
      return prev;
    }
    ListNode next = head.next;
    head.next = prev;
    return reverseList(next, head);
  }
}
