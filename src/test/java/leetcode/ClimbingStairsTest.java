package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ClimbingStairsTest {

  @ParameterizedTest
  @CsvSource(textBlock = """
      1, 1
      2, 2
      3, 3
      """)
  void climbStairs(int n, int expected) {
    int actual = new ClimbingStairs().climbStairs(n);
    assertThat(actual).isEqualTo(expected);
  }
}
