package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MinWindowSubstringTest {

  @ParameterizedTest
  @CsvSource(
      textBlock =
          """
      ADOBECODEBANC, ABC, BANC
      a, a, a
      a, aa, ''
      bdab, ab, ab
      aaaaaaaaaaaabbbbbcdd, abcdd, abbbbbcdd
      """)
  void minWindow(String s, String t, String expected) {
    String actual = new MinWindowSubstring().minWindow(s, t);
    assertThat(actual).isEqualTo(expected);
  }
}
