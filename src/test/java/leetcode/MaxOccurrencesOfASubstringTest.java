package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MaxOccurrencesOfASubstringTest {
  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
          aababcaab | 2 | 3 | 4 | 2
          aaaa | 1 | 3 | 3 | 2
          """)
  void maxFreq(String s, int maxLetters, int minSize, int maxSize, int expected) {
    MaxOccurrencesOfASubstring sut = new MaxOccurrencesOfASubstring();
    int actual = sut.maxFreq(s, maxLetters, minSize, maxSize);
    assertThat(actual).isEqualTo(expected);
  }
}
