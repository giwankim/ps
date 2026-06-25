package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class NonOverlapIntervalsTest {

  @ParameterizedTest
  @MethodSource
  void eraseOverlapIntervals(int[][] intervals, int expected) {
    int actual = new NonOverlapIntervals().eraseOverlapIntervals(intervals);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> eraseOverlapIntervals() {
    return Stream.of(
        Arguments.of(new int[][] {{1, 2}, {2, 3}, {3, 4}, {1, 3}}, 1),
        Arguments.of(new int[][] {{1, 2}, {1, 2}, {1, 2}}, 2),
        Arguments.of(new int[][] {{1, 2}, {2, 3}}, 0));
  }
}
