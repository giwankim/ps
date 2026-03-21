package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseLinkedListIITest {
  ReverseLinkedListII sut = new ReverseLinkedListII();

  // Step 1: Trivial — single node, left == right == 1
  @Test
  void singleton() {
    assertThat(sut.reverseBetween(ListNode.of(1), 1, 1)).isEqualTo(ListNode.of(1));
  }

  // Step 2: No-op — left == right on a longer list (no reversal needed)
  @Test
  void leftEqualsRight() {
    assertThat(sut.reverseBetween(ListNode.of(1, 2, 3), 2, 2)).isEqualTo(ListNode.of(1, 2, 3));
  }

  // Step 3: Simplest actual reversal — swap two elements
  @Test
  void twoElementSwap() {
    assertThat(sut.reverseBetween(ListNode.of(1, 2), 1, 2)).isEqualTo(ListNode.of(2, 1));
  }

  // Step 4: Reverse a prefix (left=1, right < n) — head changes, tail reconnects
  @ParameterizedTest
  @MethodSource
  void reversePrefix(ListNode head, int right, ListNode expected) {
    assertThat(sut.reverseBetween(head, 1, right)).isEqualTo(expected);
  }

  static Stream<Arguments> reversePrefix() {
    return Stream.of(
        Arguments.argumentSet("first 2 of 4", ListNode.of(1, 2, 3, 4), 2, ListNode.of(2, 1, 3, 4)),
        Arguments.argumentSet(
            "first 3 of 5", ListNode.of(1, 2, 3, 4, 5), 3, ListNode.of(3, 2, 1, 4, 5)));
  }

  // Step 5: Reverse a suffix (left > 1, right = n) — head stays, predecessor reconnects
  @ParameterizedTest
  @MethodSource
  void reverseSuffix(ListNode head, int left, int right, ListNode expected) {
    assertThat(sut.reverseBetween(head, left, right)).isEqualTo(expected);
  }

  static Stream<Arguments> reverseSuffix() {
    return Stream.of(
        Arguments.argumentSet("last 2 of 3", ListNode.of(1, 2, 3), 2, 3, ListNode.of(1, 3, 2)),
        Arguments.argumentSet(
            "last 3 of 5", ListNode.of(1, 2, 3, 4, 5), 3, 5, ListNode.of(1, 2, 5, 4, 3)));
  }

  // Step 6: Reverse a middle segment (left > 1, right < n) — general case
  @ParameterizedTest
  @MethodSource
  void reverseMiddle(ListNode head, int left, int right, ListNode expected) {
    assertThat(sut.reverseBetween(head, left, right)).isEqualTo(expected);
  }

  static Stream<Arguments> reverseMiddle() {
    return Stream.of(
        Arguments.argumentSet(
            "2 of 3 adjacent", ListNode.of(1, 2, 3, 4, 5), 2, 3, ListNode.of(1, 3, 2, 4, 5)),
        Arguments.argumentSet(
            "3 of 5 middle", ListNode.of(1, 2, 3, 4, 5), 2, 4, ListNode.of(1, 4, 3, 2, 5)),
        Arguments.argumentSet(
            "3 of 7 middle",
            ListNode.of(1, 2, 3, 4, 5, 6, 7),
            3,
            5,
            ListNode.of(1, 2, 5, 4, 3, 6, 7)));
  }

  // Step 7: Reverse the entire list (left=1, right=n)
  @ParameterizedTest
  @MethodSource
  void entireList(ListNode head, int right, ListNode expected) {
    assertThat(sut.reverseBetween(head, 1, right)).isEqualTo(expected);
  }

  static Stream<Arguments> entireList() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3), 3, ListNode.of(3, 2, 1)),
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), 5, ListNode.of(5, 4, 3, 2, 1)));
  }
}
