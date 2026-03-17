package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveNthFromEndOfListTest {
  RemoveNthFromEndOfList sut = new RemoveNthFromEndOfList();

  @Test
  void removeOnlyNode() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1), 1)).isNull();
  }

  @Test
  void removeTailOfTwoNodeList() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1, 2), 1)).isEqualTo(ListNode.of(1));
  }

  @Test
  void removeHeadOfTwoNodeList() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1, 2), 2)).isEqualTo(ListNode.of(2));
  }

  @Test
  void removeTail() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1, 2, 3), 1)).isEqualTo(ListNode.of(1, 2));
  }

  @Test
  void removeHead() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1, 2, 3), 3)).isEqualTo(ListNode.of(2, 3));
  }

  @ParameterizedTest
  @MethodSource
  void removeMiddleNode(ListNode head, int n, ListNode expected) {
    assertThat(sut.removeNthFromEnd(head, n)).isEqualTo(expected);
  }

  static Stream<Arguments> removeMiddleNode() {
    return Stream.of(
        Arguments.argumentSet(
            "remove second node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            2,
            ListNode.of(1, 2, 3, 5)),
        Arguments.argumentSet(
            "remove third node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            3,
            ListNode.of(1, 2, 4, 5)),
        Arguments.argumentSet(
            "remove fourth node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            4,
            ListNode.of(1, 3, 4, 5)));
  }
}
