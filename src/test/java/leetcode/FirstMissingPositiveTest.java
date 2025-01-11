package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FirstMissingPositiveTest {
  @ParameterizedTest
  @MethodSource
  void firstMissingPositive(int[] nums, int expected) {
    var sut = new FirstMissingPositive();
    int actual = sut.firstMissingPositive(nums);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> firstMissingPositive() {
    return Stream.of(
//        Arguments.of(new int[] {1, 2, 0}, 3),
        Arguments.of(new int[]{3, 4, -1, 1}, 2)
//        Arguments.of(new int[] {7, 8, 9, 11, 12}, 1),
//        Arguments.arguments(new int[] {1, 1}, 2)
    );
  }
}
