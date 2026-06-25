package leetcode;

import leetcode.support.ListNode;

public class MaximumTwinSumOfALinkedList {
  public int pairSum(ListNode head) {
    ListNode dummy = new ListNode();
    dummy.next = head;
    ListNode slow = dummy;
    ListNode fast = dummy;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }

    ListNode firstHalf = head;
    ListNode secondHalf = reverse(slow.next);
    slow.next = null;

    int result = 0;
    while (firstHalf != null && secondHalf != null) {
      result = Math.max(result, firstHalf.val + secondHalf.val);
      firstHalf = firstHalf.next;
      secondHalf = secondHalf.next;
    }
    return result;
  }

  private static ListNode reverse(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;
    while (curr != null) {
      ListNode next = curr.next;
      curr.next = prev;
      prev = curr;
      curr = next;
    }
    return prev;
  }
}
