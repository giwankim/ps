package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TrappingRainWaterTest {
  @ParameterizedTest
  @MethodSource
  void trap(int[] height, int expected) {
    int actual = new TrappingRainWater().trap(height);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> trap() {
    return Stream.of(
        Arguments.of(new int[] {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, 6),
        Arguments.of(new int[] {4, 2, 0, 3, 2, 5}, 9));
  }
}
