package com.giwankim.programmers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class 순위_검색Test {

  @Test
  void 순위_겸색() {
    String[] info = {
      "java backend junior pizza 150",
      "python frontend senior chicken 210",
      "python frontend senior chicken 150",
      "cpp backend senior pizza 260",
      "java backend junior chicken 80",
      "python backend senior chicken 50"
    };
    String[] query = {
      "java and backend and junior and pizza 100",
      "python and frontend and senior and chicken 200",
      "cpp and - and senior and pizza 250",
      "- and backend and senior and - 150",
      "- and - and - and chicken 100",
      "- and - and - and - 150"
    };
    int[] expected = {1, 1, 1, 1, 2, 4};

    int[] actual = new 순위_검색().solution(info, query);

    assertThat(actual).containsExactly(expected);
  }

  @ParameterizedTest
  @MethodSource
  void lowerBound(int target, List<Integer> scores, int expected) {
    int actual = new 순위_검색().lowerBound(target, scores);
    assertThat(actual).isEqualTo(expected);
  }

  static Stream<Arguments> lowerBound() {
    return Stream.of(
        Arguments.of(30, List.of(50, 80, 150, 150, 210, 260), 0),
        Arguments.of(270, List.of(50, 80, 150, 150, 210, 260), 6),
        Arguments.of(150, List.of(50, 80, 150, 150, 210, 260), 2),
        Arguments.of(130, List.of(130, 130, 130, 130, 150), 0));
  }
}
