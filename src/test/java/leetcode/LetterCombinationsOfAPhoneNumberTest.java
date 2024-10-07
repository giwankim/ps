package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LetterCombinationsOfAPhoneNumberTest {

  @ParameterizedTest
  @MethodSource
  void letterCombinations(String digits, List<String> expected) {
    List<String> actual = new LetterCombinationsOfAPhoneNumber().letterCombinations(digits);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> letterCombinations() {
    return Stream.of(
        Arguments.of("23", List.of("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf")),
        Arguments.of("", Collections.emptyList()),
        Arguments.of("2", List.of("a", "b", "c")));
  }
}
