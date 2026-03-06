package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AddTwoNumbersTest {
  AddTwoNumbers sut = new AddTwoNumbers();

  @ParameterizedTest
  @MethodSource
  void addTwoNumbers(int[] l1, int[] l2, int[] expected) {
    ListNode actual = sut.addTwoNumbers(ListNode.of(l1), ListNode.of(l2));
    assertThat(actual).isEqualTo(ListNode.of(expected));
  }

  static Stream<Arguments> addTwoNumbers() {
    return Stream.of(
        Arguments.of(new int[] {0}, new int[] {0}, new int[] {0}),
        Arguments.of(new int[] {1}, new int[] {2}, new int[] {3}),
        Arguments.of(new int[] {1, 2}, new int[] {3, 4, 5}, new int[] {4, 6, 5}),
        Arguments.of(new int[] {2, 4, 3}, new int[] {5, 6, 4}, new int[] {7, 0, 8}),
        Arguments.of(
            new int[] {9, 9, 9, 9, 9, 9, 9},
            new int[] {9, 9, 9, 9},
            new int[] {8, 9, 9, 9, 0, 0, 0, 1}));
  }
}
