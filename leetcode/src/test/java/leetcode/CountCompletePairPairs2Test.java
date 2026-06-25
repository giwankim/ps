package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CountCompletePairPairs2Test {

  @ParameterizedTest
  @MethodSource("cases")
  void countCompleteDayPairs(int[] hours, long expected) {
    long days = new CountCompletePairPairs2().countCompleteDayPairs(hours);
    assertThat(days).isEqualTo(expected);
  }

  static Stream<Arguments> cases() {
    return Stream.of(
        Arguments.of(new int[] {24}, 0L),
        Arguments.of(new int[] {12, 12, 30, 24, 24}, 2L),
        Arguments.of(new int[] {72, 48, 24, 3}, 3L));
  }
}
