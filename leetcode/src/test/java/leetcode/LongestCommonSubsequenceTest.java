package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestCommonSubsequenceTest {

  @ParameterizedTest
  @MethodSource
  void longestCommonSubsequence(String text1, String text2, int expected) {
    int actual = new LongestCommonSubsequence().longestCommonSubsequence(text1, text2);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> longestCommonSubsequence() {
    return Stream.of(
        Arguments.of("abcde", "ace", 3),
        Arguments.of("abc", "abc", 3),
        Arguments.of("abc", "def", 0));
  }
}
