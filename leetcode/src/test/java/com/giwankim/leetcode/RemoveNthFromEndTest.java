package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoveNthFromEndTest {
  private RemoveNthFromEnd sut;

  @BeforeEach
  void setUp() {
    sut = new RemoveNthFromEnd();
  }

  @Test
  void removeNthFromEnd() {
    ListNode head = ListNode.of(1, 2, 3, 4, 5);
    ListNode expected = ListNode.of(1, 2, 3, 5);

    ListNode actual = sut.removeNthFromEnd(head, 2);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void removeLastNode() {
    ListNode head = ListNode.of(1, 2);
    ListNode expected = ListNode.of(1);

    ListNode actual = sut.removeNthFromEnd(head, 1);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void removeOneResultsInEmptyList() {
    ListNode head = ListNode.of(1);
    ListNode expected = ListNode.of();

    ListNode actual = sut.removeNthFromEnd(head, 1);

    assertThat(actual).isEqualTo(expected);
  }
}
