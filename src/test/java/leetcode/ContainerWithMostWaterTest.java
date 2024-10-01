package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ContainerWithMostWaterTest {

  @ParameterizedTest
  @MethodSource
  void maxArea(int[] height, int expected) {
    int actual = new ContainerWithMostWater().maxArea(height);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> maxArea() {
    return Stream.of(
        Arguments.of(new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7}, 49), Arguments.of(new int[] {1, 1}, 1));
  }
}
