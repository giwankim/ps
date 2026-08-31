package leetcode.support;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ListNode {
  public int val;
  public ListNode next;

  public ListNode(int val) {
    this(val, null);
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
}
