package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeIntervalsTest {
  @ParameterizedTest
  @MethodSource
  void merge(int[][] intervals, int[][] expected) {
    int[][] actual = new MergeIntervals().merge(intervals);
    assertThat(actual).isDeepEqualTo(expected);
  }

  private static Stream<Arguments> merge() {
    return Stream.of(
        Arguments.of(
            new int[][] {{1, 3}, {2, 6}, {8, 10}, {15, 18}},
            new int[][] {{1, 6}, {8, 10}, {15, 18}}),
        Arguments.of(new int[][] {{1, 4}, {4, 5}}, new int[][] {{1, 5}}));
  }
}
