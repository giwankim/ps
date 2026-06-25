package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AddBoldTagTest {
  @ParameterizedTest
  @MethodSource
  void addBoldTag(String s, String[] words, String expected) {
    String actual = new AddBoldTag().addBoldTag(s, words);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> addBoldTag() {
    return Stream.of(
        Arguments.of("abcxyz123", new String[] {"abc", "123"}, "<b>abc</b>xyz<b>123</b>"),
        Arguments.of("aaabbb", new String[] {"aa", "b"}, "<b>aaabbb</b>"),
        Arguments.of("aaabbcc", new String[] {"aaa", "aab", "bc"}, "<b>aaabbc</b>c"));
  }
}
