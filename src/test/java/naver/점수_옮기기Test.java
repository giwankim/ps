package naver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class 점수_옮기기Test {

  @ParameterizedTest
  @MethodSource
  void moveNumbers(int cap, int k, int[] score, int m, int expected) {
    점수_옮기기 moveNumbers = new 점수_옮기기();
    assertThat(moveNumbers.moveNumbers(cap, k, score, m)).isEqualTo(expected);
  }

  static Stream<Arguments> moveNumbers() {
    return Stream.of(
        Arguments.of(100, 70, new int[] {95, 90, 80, 80, 80, 70, 70, 30, 10}, 4, 3),
        Arguments.of(100, 82, new int[] {100, 97, 97, 92, 87, 77, 77, 72, 72}, 4, 4),
        Arguments.of(2000, 1998, new int[] {2000, 2000, 2000, 2000, 1999}, 5, -1));
  }
}
