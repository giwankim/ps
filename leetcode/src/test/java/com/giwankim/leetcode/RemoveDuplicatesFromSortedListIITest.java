package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveDuplicatesFromSortedListIITest {
  RemoveDuplicatesFromSortedListII sut = new RemoveDuplicatesFromSortedListII();

  @Test
  void emptyList() {
    assertThat(sut.deleteDuplicates(null)).isNull();
  }

  @Test
  void distinctElements() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 2, 3))).isEqualTo(ListNode.of(1, 2, 3));
  }

  @ParameterizedTest
  @MethodSource
  void duplicates(ListNode head, ListNode expected) {
    assertThat(sut.deleteDuplicates(head)).isEqualTo(expected);
  }

  static Stream<Arguments> duplicates() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3, 3, 4, 4, 5), ListNode.of(1, 2, 5)),
        Arguments.of(ListNode.of(1, 1, 1, 2, 3), ListNode.of(2, 3)));
  }
}
