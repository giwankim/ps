package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReorderListTest {

  @ParameterizedTest
  @MethodSource
  void reorderList(ListNode head, ListNode expected) {
    new ReorderList().reorderList(head);
    assertThat(head).isEqualTo(expected);
  }

  private static Stream<Arguments> reorderList() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3, 4), ListNode.of(1, 4, 2, 3)),
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(1, 5, 2, 4, 3)));
  }

  @ParameterizedTest
  @MethodSource
  void reverse(ListNode head, ListNode expected) {
    ListNode actual = new ReorderList().reverse(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> reverse() {
    return Stream.of(
        Arguments.of(ListNode.of(), ListNode.of()),
        Arguments.of(ListNode.of(1), ListNode.of(1)),
        Arguments.of(ListNode.of(1, 2), ListNode.of(2, 1)),
        Arguments.of(ListNode.of(1, 2, 3), ListNode.of(3, 2, 1)),
        Arguments.of(ListNode.of(1, 2, 3), ListNode.of(3, 2, 1)));
  }
}
