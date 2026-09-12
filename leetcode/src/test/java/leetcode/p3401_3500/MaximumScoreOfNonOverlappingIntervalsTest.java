package leetcode.p3401_3500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for <a href="https://leetcode.com/problems/maximum-score-of-non-overlapping-intervals/">
 * 3414. Maximum Score of Non-overlapping Intervals</a>.
 */
class MaximumScoreOfNonOverlappingIntervalsTest {
  MaximumScoreOfNonOverlappingIntervals sut = new MaximumScoreOfNonOverlappingIntervals();

  // Smallest valid input: a point interval with positive weight must be selected.
  @Test
  void singlePointIntervalIsSelected() {
    int[][] intervals = {{1, 1, 1}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0);
  }

  @Test
  void consecutivePointIntervalsDoNotOverlap() {
    int[][] intervals = {{1, 1, 2}, {2, 2, 3}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1);
  }

  // Endpoints are inclusive: [1, 2] and [2, 3] cannot both be selected.
  @Test
  void sharedEndpointCountsAsOverlap() {
    int[][] intervals = {{1, 2, 4}, {2, 3, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1);
  }

  @Test
  void nextIntervalMayStartOnePastThePreviousEnd() {
    int[][] intervals = {{1, 2, 4}, {3, 4, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1);
  }

  @Test
  void allOverlappingIntervalsChooseTheLargestWeight() {
    int[][] intervals = {{1, 10, 5}, {2, 9, 9}, {3, 8, 7}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1);
  }

  // The two inner intervals score 8 + 1 = 9, exceeding the enclosing interval's 7.
  @Test
  void disjointInnerIntervalsCanBeatTheirEnclosingInterval() {
    int[][] intervals = {{1, 10, 7}, {2, 3, 8}, {5, 6, 1}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1, 2);
  }

  // Chronological order is [1, 2, 0], but the result uses ascending original indices.
  @Test
  void unsortedInputReturnsOriginalIndicesInAscendingOrder() {
    int[][] intervals = {{9, 10, 3}, {1, 2, 4}, {5, 6, 2}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2);
  }

  @Test
  void fourCompatibleIntervalsMayAllBeSelected() {
    int[][] intervals = {{7, 8, 4}, {1, 2, 3}, {5, 6, 2}, {3, 4, 1}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2, 3);
  }

  // All five are compatible, but only the four with weights 9, 8, 7, and 6 may be taken.
  @Test
  void moreThanFourCompatibleIntervalsStillSelectAtMostFour() {
    int[][] intervals = {{1, 1, 1}, {2, 2, 9}, {3, 3, 8}, {4, 4, 7}, {5, 5, 6}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1, 2, 3, 4);
  }

  // "Up to four" does not require four: 100 beats the four-point total of 80.
  @Test
  void oneIntervalCanBeatACompatibleSetOfFour() {
    int[][] intervals = {{1, 9, 100}, {1, 1, 20}, {3, 3, 20}, {5, 5, 20}, {7, 7, 20}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0);
  }

  // Taking the heaviest interval first loses to the combined score 5 + 5 = 10.
  @Test
  void twoLighterIntervalsCanBeatTheHeaviestInterval() {
    int[][] intervals = {{1, 4, 9}, {1, 2, 5}, {3, 4, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1, 2);
  }

  @Test
  void earliestFinishingIntervalCanBeSkippedForAHigherScore() {
    int[][] intervals = {{1, 2, 1}, {2, 3, 100}, {4, 5, 2}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1, 2);
  }

  @Test
  void leetCodeExample1() {
    int[][] intervals = {{1, 3, 2}, {4, 5, 2}, {1, 5, 5}, {6, 9, 3}, {6, 7, 1}, {8, 9, 1}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(2, 3);
  }

  @Test
  void leetCodeExample2() {
    int[][] intervals = {
      {5, 8, 1}, {6, 7, 7}, {4, 7, 3}, {9, 10, 6}, {7, 8, 2}, {11, 14, 3}, {3, 5, 5}
    };
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(1, 3, 5, 6);
  }

  // Identical intervals overlap; equal scores choose the earlier original index.
  @Test
  void duplicateIntervalsChooseOnlyTheLowestIndexCopy() {
    int[][] intervals = {{3, 5, 7}, {3, 5, 7}, {6, 6, 2}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 2);
  }

  @Test
  void equalWeightsPreferOriginalIndexOverEarlierStartOrEnd() {
    int[][] intervals = {{5, 6, 7}, {1, 5, 7}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0);
  }

  // [0, 3], [1, 2], and [1, 3] all score 10; the first differing index decides.
  @Test
  void lexicographicTieUsesTheFirstIndexRatherThanTheLast() {
    int[][] intervals = {{1, 3, 5}, {1, 1, 5}, {2, 5, 5}, {4, 5, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 3);
  }

  // Both [0, 1, 3, 4] and [0, 2, 3, 4] score 19.
  @Test
  void lexicographicTieComparesTheSecondIndexAfterACommonFirstIndex() {
    int[][] intervals = {{1, 1, 5}, {6, 8, 4}, {6, 8, 4}, {3, 3, 5}, {10, 10, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 3, 4);
  }

  @Test
  void lexicographicTieComparesTheThirdIndexAfterACommonPrefix() {
    int[][] intervals = {{1, 1, 5}, {3, 3, 5}, {8, 9, 5}, {5, 8, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2);
  }

  @Test
  void lexicographicTieComparesTheFourthIndexAfterACommonPrefix() {
    int[][] intervals = {{1, 1, 5}, {3, 3, 5}, {5, 5, 5}, {9, 10, 5}, {7, 9, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2, 3);
  }

  // [0, 2] and [1] both score 10; the longer array wins because 0 < 1.
  @Test
  void tiedScoreCanPreferMoreIntervals() {
    int[][] intervals = {{1, 2, 5}, {1, 4, 10}, {3, 4, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 2);
  }

  // [0] and [1, 2] both score 10; count must not override lexicographic order.
  @Test
  void tiedScoreCanPreferFewerIntervals() {
    int[][] intervals = {{1, 4, 10}, {1, 2, 5}, {3, 4, 5}};
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0);
  }

  // Integer index 2 precedes 10, although their string representations sort oppositely.
  @Test
  void lexicographicComparisonUsesNumericIndices() {
    List<List<Integer>> intervals = new ArrayList<>();
    for (int i = 0; i < 11; i++) {
      intervals.add(new ArrayList<>(List.of(1, 1, i == 2 || i == 10 ? 2 : 1)));
    }
    assertThat(sut.maximumWeight(intervals)).containsExactly(2);
  }

  @Test
  void coordinatesMayReachOneBillion() {
    int[][] intervals = {{1, 1, 7}, {999_999_999, 999_999_999, 8}, {1_000_000_000, 1_000_000_000, 9}
    };
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2);
  }

  // The winning score is 4,000,000,000, exceeding Integer.MAX_VALUE.
  @Test
  void totalWeightMayExceedTheIntegerRange() {
    int[][] intervals = {
      {1, 1, 1_000_000_000},
      {3, 3, 1_000_000_000},
      {5, 5, 1_000_000_000},
      {7, 7, 1_000_000_000}
    };
    assertThat(sut.maximumWeight(asList(intervals))).containsExactly(0, 1, 2, 3);
  }

  // Maximum n, reverse chronological input, and a unique best set at the final four indices.
  @Test
  void maximumLengthDisjointInputChoosesTheFourLargestWeights() {
    List<List<Integer>> intervals = new ArrayList<>(50_000);
    for (int i = 0; i < 50_000; i++) {
      int position = 50_000 - i;
      intervals.add(new ArrayList<>(List.of(position, position, i + 1)));
    }
    assertThat(sut.maximumWeight(intervals)).containsExactly(49_996, 49_997, 49_998, 49_999);
  }

  @Test
  void maximumLengthOverlappingInputBreaksTiesByOriginalIndex() {
    List<List<Integer>> intervals = new ArrayList<>(50_000);
    for (int i = 0; i < 50_000; i++) {
      intervals.add(new ArrayList<>(List.of(1, 1_000_000_000, 1)));
    }
    assertThat(sut.maximumWeight(intervals)).containsExactly(0);
  }

  // Keep both list levels mutable so the tests also permit solutions that sort in place.
  private static List<List<Integer>> asList(int[][] values) {
    List<List<Integer>> intervals = new ArrayList<>(values.length);
    for (int[] value : values) {
      intervals.add(new ArrayList<>(List.of(value[0], value[1], value[2])));
    }
    return intervals;
  }
}
