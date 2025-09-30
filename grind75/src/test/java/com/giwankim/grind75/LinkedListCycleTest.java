package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.ListNode;
import org.junit.jupiter.api.Test;

class LinkedListCycleTest {

  LinkedListCycle linkedListCycle = new LinkedListCycle();

  @Test
  void cycle() {
    /*
      3 -> 2 -> 0 -> -4
           ^          |
           |__________|
    */
    ListNode tail = new ListNode(-4);
    ListNode head = new ListNode(3, new ListNode(2, new ListNode(0, tail)));
    tail.next = head.next;

    boolean actual = linkedListCycle.hasCycle(head);

    assertThat(actual).isTrue();
  }

  @Test
  void lengthOneCycle() {
    /*
      1 -> 2
      ^    |
      |____|
    */
    ListNode tail = new ListNode(2);
    ListNode head = new ListNode(1, tail);
    tail.next = head;

    boolean actual = linkedListCycle.hasCycle(head);

    assertThat(actual).isTrue();
  }

  @Test
  void noCycleSingleton() {
    assertThat(linkedListCycle.hasCycle(new ListNode(1))).isFalse();
  }
}
