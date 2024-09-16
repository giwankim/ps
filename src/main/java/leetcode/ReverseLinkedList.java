package leetcode;

import datatype.ListNode;

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
