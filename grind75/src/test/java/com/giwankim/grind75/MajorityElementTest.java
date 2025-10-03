package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MajorityElementTest {

  @ParameterizedTest
  @MethodSource("cases")
  void majorityElement(int[] nums, int expected) {
    int actual = new MajorityElement().majorityElement(nums);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(new int[] {3, 2, 3}, 3), Arguments.of(new int[] {2, 2, 1, 1, 1, 2, 2}, 2));
  }
}
