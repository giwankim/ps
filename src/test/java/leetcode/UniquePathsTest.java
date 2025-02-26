package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UniquePathsTest {
  @ParameterizedTest
  @MethodSource
  void uniquePaths(int m, int n, int expected) {
    var sut = new UniquePaths();
    int actual = sut.uniquePaths(m, n);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> uniquePaths() {
    return Stream.of(Arguments.of(3, 7, 28), Arguments.of(3, 2, 3));
  }
}
