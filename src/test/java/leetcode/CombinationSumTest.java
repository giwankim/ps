package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CombinationSumTest {
  private CombinationSum sut;

  @BeforeEach
  void setUp() {
    sut = new CombinationSum();
  }

  @Test
  void emptyCombinationSum() {
    List<List<Integer>> actual = sut.combinationSum(new int[] {2}, 1);
    assertThat(actual).isEmpty();
  }

  @Test
  void singleton() {
    List<List<Integer>> actual = sut.combinationSum(new int[] {1}, 1);
    assertThat(actual).containsExactly(List.of(1));
  }

  @ParameterizedTest
  @MethodSource
  void combinationSum(int[] candidates, int target, List<List<Integer>> expected) {
    List<List<Integer>> actual = sut.combinationSum(candidates, target);
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  static Stream<Arguments> combinationSum() {
    return Stream.of(
        Arguments.of(new int[] {2, 3, 6, 7}, 7, List.of(List.of(2, 2, 3), List.of(7))),
        Arguments.of(
            new int[] {2, 3, 5}, 8, List.of(List.of(2, 2, 2, 2), List.of(2, 3, 3), List.of(3, 5))));
  }
}
