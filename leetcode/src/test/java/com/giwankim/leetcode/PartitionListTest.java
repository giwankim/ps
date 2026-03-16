package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartitionListTest {
  PartitionList sut = new PartitionList();

  @Test
  void emptyList() {
    assertThat(sut.partition(null, 1)).isNull();
  }

  @ParameterizedTest
  @MethodSource
  void partition(ListNode head, int x, ListNode expected) {
    assertThat(sut.partition(head, x)).isEqualTo(expected);
  }

  static Stream<Arguments> partition() {
    return Stream.of(
        Arguments.argumentSet("singleton: x is less than head", ListNode.of(1), 0, ListNode.of(1)),
        Arguments.argumentSet("singleton: x is equal to head", ListNode.of(1), 1, ListNode.of(1)),
        Arguments.argumentSet(
            "singleton: x is greater than head", ListNode.of(1), 2, ListNode.of(1)),
        Arguments.argumentSet(
            "interleaved across partition boundary",
            ListNode.of(1, 4, 3, 2, 5, 2),
            3,
            ListNode.of(1, 2, 2, 4, 3, 5)),
        Arguments.argumentSet("smaller after larger", ListNode.of(2, 1), 2, ListNode.of(1, 2)),
        Arguments.argumentSet("all less than x", ListNode.of(1, 2, 3), 5, ListNode.of(1, 2, 3)),
        Arguments.argumentSet(
            "all greater or equal to x", ListNode.of(3, 4, 5), 2, ListNode.of(3, 4, 5)),
        Arguments.argumentSet("all equal to x", ListNode.of(3, 3, 3), 3, ListNode.of(3, 3, 3)),
        Arguments.argumentSet(
            "already partitioned", ListNode.of(1, 2, 4, 5), 3, ListNode.of(1, 2, 4, 5)),
        Arguments.argumentSet(
            "reverse sorted", ListNode.of(5, 4, 3, 2, 1), 3, ListNode.of(2, 1, 5, 4, 3)));
  }
}
