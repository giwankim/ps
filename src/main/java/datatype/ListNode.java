package datatype;

import java.util.Objects;

public class ListNode {
  public int val;
  public ListNode next;

  ListNode() {
    val = Integer.MIN_VALUE;
  }

  ListNode(int val) {
    this.val = val;
  }

  ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  public static ListNode createList(int... values) {
    ListNode head = new ListNode(values[0]);
    ListNode prev = head;
    for (int i = 1; i < values.length; i++) {
      prev.next = new ListNode(values[i]);
      prev = prev.next;
    }
    return head;
  }

  @Override
  public String toString() {
    return val + "->" + next;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ListNode listNode)) {
      return false;
    }
    return val == listNode.val && Objects.equals(next, listNode.next);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val, next);
  }
}
