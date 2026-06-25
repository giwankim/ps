package leetcode;

import leetcode.support.ListNode;

public class PalindromeLinkedList {
  public boolean isPalindrome(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
    }
    if (fast != null) {
      slow = slow.next;
    }

    ListNode rev = reverse(slow);

    while (rev != null) {
      if (rev.val != head.val) {
        return false;
      }
      rev = rev.next;
      head = head.next;
    }
    return true;
  }

  private ListNode reverse(ListNode head) {
    ListNode prev = null;
    while (head != null) {
      ListNode next = head.next;
      head.next = prev;
      prev = head;
      head = next;
    }
    return prev;
  }
}
