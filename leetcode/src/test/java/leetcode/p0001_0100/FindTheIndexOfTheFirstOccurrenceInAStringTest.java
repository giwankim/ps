package leetcode.p0001_0100;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FindTheIndexOfTheFirstOccurrenceInAStringTest {
  FindTheIndexOfTheFirstOccurrenceInAString sut = new FindTheIndexOfTheFirstOccurrenceInAString();

  @ParameterizedTest
  @CsvSource(
      value = {"sadbutsad:sad:0", "leetcode:leeto:-1"},
      delimiter = ':')
  void needleInAHaystack(String haystack, String needle, int expected) {
    assertThat(sut.strStr(haystack, needle)).isEqualTo(expected);
  }
}
