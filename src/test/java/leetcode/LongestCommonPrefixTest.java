package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LongestCommonPrefixTest {
  @ParameterizedTest
  @MethodSource
  void longestCommonPrefix(String[] strs, String expected) {
    String actual = new LongestCommonPrefix().longestCommonPrefix(strs);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> longestCommonPrefix() {
    return Stream.of(
        Arguments.of(new String[] {"a", "ab", "abc", "abcd"}, "a"),
        Arguments.of(new String[] {"flower", "flow", "flight"}, "fl"),
        Arguments.of(new String[] {"dog", "racecar", "car"}, ""));
  }
}
