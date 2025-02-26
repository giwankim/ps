package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RomanToIntegerTest {
  @ParameterizedTest
  @MethodSource
  void romanToInt(String s, int expected) {
    var sut = new RomanToInteger();
    int actual = sut.romanToInt(s);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> romanToInt() {
    return Stream.of(
        Arguments.of("III", 3), Arguments.of("LVIII", 58), Arguments.of("MCMXCIV", 1994));
  }
}
