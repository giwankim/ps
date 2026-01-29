package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseLinkedListTest {
  @ParameterizedTest
  @MethodSource
  void reverseList(ListNode head, ListNode expected) {
    ListNode actual = new ReverseLinkedList().reverseList(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> reverseList() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)),
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)));
  }
}
