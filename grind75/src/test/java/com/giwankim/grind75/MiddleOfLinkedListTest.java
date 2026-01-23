package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.ListNode;
import org.junit.jupiter.api.Test;

class MiddleOfLinkedListTest {

  MiddleOfLinkedList middleOfLinkedList = new MiddleOfLinkedList();

  @Test
  void singleton() {
    ListNode head = new ListNode(1);
    assertThat(middleOfLinkedList.middleNode(head)).isEqualTo(head);
  }

  @Test
  void oddLength() {
    ListNode head =
        new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
    ListNode expected = head.next.next;

    ListNode actual = middleOfLinkedList.middleNode(head);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void evenLength() {
    ListNode head =
        new ListNode(
            1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, new ListNode(6))))));
    ListNode expected = head.next.next.next;

    ListNode actual = middleOfLinkedList.middleNode(head);

    assertThat(actual).isEqualTo(expected);
  }
}
