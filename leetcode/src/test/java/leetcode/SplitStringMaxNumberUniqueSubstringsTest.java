package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SplitStringMaxNumberUniqueSubstringsTest {

  @ParameterizedTest
  @MethodSource
  void maxUniqueSplit(String s, int expected) {
    int actual = new SplitStringMaxNumberUniqueSubstrings().maxUniqueSplit(s);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> maxUniqueSplit() {
    return Stream.of(Arguments.of("ababccc", 5), Arguments.of("aba", 2), Arguments.of("aa", 1));
  }
}
