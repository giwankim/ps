package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PalindromeLinkedListTest {
  @ParameterizedTest
  @MethodSource
  void isPalindrome(ListNode head, boolean expected) {
    boolean actual = new PalindromeLinkedList().isPalindrome(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isPalindrome() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 2, 1), true),
        Arguments.of(ListNode.of(1, 2), false),
        Arguments.of(ListNode.of(1, 2, 3, 2, 1), true));
  }
}
