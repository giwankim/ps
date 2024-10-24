package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NumberOf1BitsTest {

  @ParameterizedTest
  @CsvSource({"11, 3", "128, 1", "2147483645, 30"})
  void hammingWeight(int n, int expected) {
    int actual = new NumberOf1Bits().hammingWeight(n);
    assertThat(actual).isEqualTo(expected);
  }
}
