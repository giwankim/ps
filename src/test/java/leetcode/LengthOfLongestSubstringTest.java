package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LengthOfLongestSubstringTest {
  @ParameterizedTest
  @MethodSource
  void lengthOfLongestSubstring(String s, int expected) {
    int actual = new LengthOfLongestSubstring().lengthOfLongestSubstring(s);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> lengthOfLongestSubstring() {
    return Stream.of(
        Arguments.of("abcabcbb", 3),
        Arguments.of("bbbbb", 1),
        Arguments.of("pwwkew", 3),
        Arguments.of("", 0),
        Arguments.of(" ", 1),
        Arguments.of("tmmzuxt", 5));
  }
}
