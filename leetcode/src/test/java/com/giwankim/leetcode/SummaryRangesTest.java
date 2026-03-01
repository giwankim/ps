package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SummaryRangesTest {
  SummaryRanges sut = new SummaryRanges();

  @Test
  void pureRanges() {
    int[] nums = {0, 1, 2, 4, 5};
    List<String> expected = List.of("0->2", "4->5");
    List<String> actual = sut.summaryRanges(nums);
    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource
  void summaryRanges(int[] nums, List<String> expected) {
    List<String> actual = sut.summaryRanges(nums);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> summaryRanges() {
    return Stream.of(
        Arguments.of(new int[] {0, 1, 2, 4, 5, 7}, List.of("0->2", "4->5", "7")),
        Arguments.of(new int[] {0, 2, 3, 4, 6, 8, 9}, List.of("0", "2->4", "6", "8->9")));
  }
}
