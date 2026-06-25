package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CountingBitsTest {

  @ParameterizedTest
  @MethodSource
  void countBits(int n, int[] expected) {
    int[] actual = new CountingBits().countBits(n);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> countBits() {
    return Stream.of(
        Arguments.of(2, new int[] {0, 1, 1}), Arguments.of(5, new int[] {0, 1, 1, 2, 1, 2}));
  }
}
