package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.support.ListNode;
import org.junit.jupiter.api.Test;

class ReverseLinkedListTest {

  ReverseLinkedList reverseLinkedList = new ReverseLinkedList();

  @Test
  void emptyList() {
    assertThat(reverseLinkedList.reverseList(null)).isNull();
  }

  @Test
  void twoNodes() {
    ListNode head = new ListNode(1, new ListNode(2));
    ListNode expected = new ListNode(2, new ListNode(1));

    assertThat(reverseLinkedList.reverseList(head))
        .isEqualTo(expected);
  }

  @Test
  void reverseList() {
    ListNode head =
        new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
    ListNode expected =
        new ListNode(5, new ListNode(4, new ListNode(3, new ListNode(2, new ListNode(1)))));

    ListNode actual = reverseLinkedList.reverseList(head);

    assertThat(actual).isEqualTo(expected);
  }
}
