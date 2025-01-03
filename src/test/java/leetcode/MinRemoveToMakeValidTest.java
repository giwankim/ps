package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MinRemoveToMakeValidTest {
  @ParameterizedTest
  @CsvSource(
      delimiter = '|',
      textBlock =
          """
          lee(t(c)o)de) | lee(t(c)o)de
          a)b(c)d | ab(c)d
          ))(( | ''
          """)
  void minRemoveToMakeValid(String s, String expected) {
    var sut = new MinRemoveToMakeValid();
    String actual = sut.minRemoveToMakeValid(s);
    assertThat(actual).isEqualTo(expected);
  }
}
