package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidPalindromeIITest {
  @ParameterizedTest
  @CsvSource(textBlock = """
      aba, true
      abca, true
      abc, false
      """)
  void validPalindrome(String s, boolean expected) {
    boolean actual = new ValidPalindromeII().validPalindrome(s);
    assertThat(actual).isEqualTo(expected);
  }
}
