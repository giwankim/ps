package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveElementTest {
  @ParameterizedTest
  @MethodSource
  void removeElement(int[] nums, int val, int expected) {
    RemoveElement sut = new RemoveElement();
    int k = sut.removeElement(nums, val);
    assertThat(k).isEqualTo(expected);
  }

  static Stream<Arguments> removeElement() {
    return Stream.of(
        Arguments.of(new int[] {3, 2, 2, 3}, 3, 2),
        Arguments.of(new int[] {0, 1, 2, 2, 3, 0, 4, 2}, 2, 5));
  }
}
