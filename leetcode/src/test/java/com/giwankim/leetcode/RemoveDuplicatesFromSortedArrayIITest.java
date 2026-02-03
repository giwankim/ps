package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveDuplicatesFromSortedArrayIITest {
  RemoveDuplicatesFromSortedArrayII sut = new RemoveDuplicatesFromSortedArrayII();

  @ParameterizedTest
  @MethodSource
  void removeDuplicates(int[] nums, int[] expectedNums) {
    int k = sut.removeDuplicates(nums);
    assertThat(k).isEqualTo(expectedNums.length);
    assertThat(Arrays.copyOf(nums, k)).isEqualTo(expectedNums);
  }

  static Stream<Arguments> removeDuplicates() {
    return Stream.of(
        Arguments.of(new int[] {1, 2, 2, 3}, new int[] {1, 2, 2, 3}),
        Arguments.of(new int[] {1, 1, 1, 2, 2, 3}, new int[] {1, 1, 2, 2, 3}),
        Arguments.of(new int[] {0, 0, 1, 1, 1, 1, 2, 3, 3}, new int[] {0, 0, 1, 1, 2, 3, 3}));
  }
}
