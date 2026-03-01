package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ContainsDuplicateIITest {
  ContainsDuplicateII sut = new ContainsDuplicateII();

  @ParameterizedTest
  @MethodSource
  void containsNearbyDuplicate(int[] nums, int k, boolean expected) {
    boolean actual = sut.containsNearbyDuplicate(nums, k);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> containsNearbyDuplicate() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 3, 1}, 3, true),
        Arguments.of(new int[] {1, 0, 1, 1}, 1, true),
        Arguments.of(new int[] {1, 2, 3, 1, 2, 3}, 2, false));
  }
}
