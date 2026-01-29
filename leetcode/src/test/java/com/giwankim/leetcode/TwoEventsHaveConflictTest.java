package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class TwoEventsHaveConflictTest {

  @ParameterizedTest
  @MethodSource
  void haveConflict(String[] event1, String[] event2, boolean expected) {
    boolean actual = new TwoEventsHaveConflict().haveConflict(event1, event2);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> haveConflict() {
    return Stream.of(
        Arguments.of(new String[] {"01:15", "02:00"}, new String[] {"02:00", "03:00"}, true),
        Arguments.of(new String[] {"01:00", "02:00"}, new String[] {"01:20", "03:00"}, true),
        Arguments.of(new String[] {"10:00", "11:00"}, new String[] {"14:00", "15:00"}, false));
  }
}
