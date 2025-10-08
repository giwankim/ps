package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AddBinaryTest {

  @ParameterizedTest
  @MethodSource
  void addBinary(String a, String b, String expected) {
    String actual = new AddBinary().addBinary(a, b);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> addBinary() {
    return Stream.of(Arguments.of("11", "1", "100"), Arguments.of("1010", "1011", "10101"));
  }
}
