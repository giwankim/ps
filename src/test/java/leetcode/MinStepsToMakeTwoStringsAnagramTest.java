package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MinStepsToMakeTwoStringsAnagramTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
      bab | aba | 1
      leetcode | practice | 5
      anagram | mangaar | 0
      """)
  void minSteps(String s, String t, int expected) {
    MinStepsToMakeTwoStringsAnagram sut = new MinStepsToMakeTwoStringsAnagram();
    int actual = sut.minSteps(s, t);
    assertThat(actual).isEqualTo(expected);
  }
}
