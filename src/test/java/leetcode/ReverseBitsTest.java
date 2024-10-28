package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseBitsTest {

  @ParameterizedTest
  @MethodSource
  void reverseBits(int n, int expected) {
    int actual = new ReverseBits().reverseBits(n);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> reverseBits() {
    return Stream.of(
        Arguments.of(0b00000010100101000001111010011100, 964176192),
        Arguments.of(0b11111111111111111111111111111101, 0b10111111111111111111111111111111));
  }
}
