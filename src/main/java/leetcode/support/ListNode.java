package leetcode.support;

import java.util.Objects;

public class ListNode {
  public int val;
  public ListNode next;

  public ListNode() {
    this(Integer.MIN_VALUE, null);
  }

  public ListNode(int val) {
    this(val, null);
  }

  public ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  public static ListNode of(int... values) {
    ListNode head = new ListNode();
    ListNode it = head;
    for (int value : values) {
      it.next = new ListNode(value);
      it = it.next;
    }
    return head.next;
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
