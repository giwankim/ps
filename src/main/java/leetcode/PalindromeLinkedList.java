package leetcode;

import datatype.ListNode;
import java.util.ArrayDeque;
import java.util.Deque;

public class PalindromeLinkedList {
  public boolean isPalindrome(ListNode head) {
    Deque<Integer> stack = new ArrayDeque<>();
    ListNode node = head;
    while (node != null) {
      stack.push(node.val);
      node = node.next;
    }
    node = head;
    while (node != null) {
      if (node.val != stack.pop()) {
        return false;
      }
      node = node.next;
    }
    return true;
  }
}
