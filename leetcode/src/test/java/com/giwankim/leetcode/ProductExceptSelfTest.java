package com.giwankim.leetcode;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProductExceptSelfTest {
  @ParameterizedTest
  @MethodSource
  void productExceptSelf(int[] nums, int[] expected) {
    int[] actual = new ProductExceptSelf().productExceptSelf(nums);
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> productExceptSelf() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 3, 4}, new int[] {24, 12, 8, 6}),
        Arguments.of(new int[] {-1, 1, 0, -3, 3}, new int[] {0, 0, 9, 0, 0}));
  }
}
