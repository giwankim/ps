package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseStringTest {
  @ParameterizedTest
  @MethodSource
  void reverseString(char[] s, char[] expected) {
    ReverseString solution = new ReverseString();
    solution.reverseString(s);
    assertThat(s).isEqualTo(expected);
  }

  private static Stream<Arguments> reverseString() {
    return Stream.of(
        Arguments.of(new char[] {'h', 'e', 'l', 'l', 'o'}, new char[] {'o', 'l', 'l', 'e', 'h'}),
        Arguments.of(
            new char[] {'H', 'a', 'n', 'n', 'a', 'h'}, new char[] {'h', 'a', 'n', 'n', 'a', 'H'}));
  }
}
