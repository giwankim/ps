package leetcode;

import datatype.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class PalindromeLinkedList {
  public boolean isPalindrome(ListNode head) {
    Deque<Integer> deque = new ArrayDeque<>();
    ListNode node = head;
    while (node != null) {
      deque.push(node.val);
      node = node.next;
    }
    while (deque.size() > 1) {
      if (!Objects.equals(deque.pollFirst(), deque.pollLast())) {
        return false;
      }
    }
    return true;
  }
}
