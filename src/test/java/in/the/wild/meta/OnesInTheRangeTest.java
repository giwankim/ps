package in.the.wild.meta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OnesInTheRangeTest {
  @ParameterizedTest
  @MethodSource
  void numOfOnes(int[] nums, int s, int e, int expected) {
    int actual = new OnesInTheRange(nums).numOfOnes(s, e);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> numOfOnes() {
    return Stream.of(
        Arguments.of(new int[] {0, 0, 1, 0, 1}, 2, 4, 2),
        Arguments.of(new int[] {0, 1, 1, 0, 0, 1, 1, 1}, 2, 6, 3),
        Arguments.of(new int[] {0, 1, 1, 0, 0, 1, 1, 1}, 1, 7, 5));
  }
}
