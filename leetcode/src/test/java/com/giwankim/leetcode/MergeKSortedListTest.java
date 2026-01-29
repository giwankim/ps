package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeKSortedListTest {

  @ParameterizedTest
  @MethodSource
  void mergeKLists(ListNode[] lists, ListNode expected) {
    ListNode actual = new MergeKSortedList().mergeKLists(lists);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> mergeKLists() {
    return Stream.of(
        Arguments.of(
            new ListNode[] {ListNode.of(1, 4, 5), ListNode.of(1, 3, 4), ListNode.of(2, 6)},
            ListNode.of(1, 1, 2, 3, 4, 4, 5, 6)),
        Arguments.of(new ListNode[0], ListNode.of()),
        Arguments.of(new ListNode[] {ListNode.of()}, ListNode.of()),
        Arguments.of(
            new ListNode[] {ListNode.of(1, 3, 5), ListNode.of(2, 4, 6)},
            ListNode.of(1, 2, 3, 4, 5, 6)));
  }
}
