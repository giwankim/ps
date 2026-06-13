package boj.boj1182;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1182 부분수열의 합 (Sum of Subsequences).
 *
 * <p>Given a sequence of N integers (1 ≤ N ≤ 20, each absolute value at most 100,000) and a target
 * S (|S| ≤ 1,000,000), count the non-empty subsequences whose elements sum to exactly S. A
 * subsequence is identified by the positions it selects, so equal values at different positions
 * form distinct subsequences. The empty subsequence is never counted, which only changes the answer
 * when S = 0. The count is printed on a single line.
 */
class MainTest {

  // --- Official sample. ---

  @Test
  @StdIo({"5 0", "-7 -3 -2 5 8"})
  void officialSampleCountsTheSingleZeroSumSubsequence(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Only {-3, -2, 5} sums to 0; the empty subsequence is excluded.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Single element (the N = 1 lower bound). ---

  @Test
  @StdIo({"1 5", "5"})
  void singleElementEqualToTargetCountsOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1 5", "3"})
  void singleElementDifferentFromTargetCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1 0", "0"})
  void singleZeroElementSatisfiesZeroTargetWhileEmptySetDoesNot(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The size-1 subsequence {0} counts; the size-0 empty subsequence never does.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1 0", "5"})
  void singlePositiveElementNeverReachesZeroTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Excluding the empty set leaves only {5}, which does not sum to 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Counting over small positive sequences. ---

  @Test
  @StdIo({"3 3", "1 2 3"})
  void countsBothSubsequencesThatReachThePositiveTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // {3} and {1, 2} both sum to 3.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Equal values are distinguished by position, not collapsed by value. ---

  @Test
  @StdIo({"3 2", "1 1 1"})
  void equalValuesPickedInPairsAreCountedByPosition(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Choosing any two of the three 1s gives 2: C(3, 2) = 3 distinct subsequences.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"3 1", "1 1 1"})
  void equalValuesPickedSinglyAreCountedByPosition(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Choosing any one of the three 1s gives 1: C(3, 1) = 3 distinct subsequences.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- No subsequence reaches the target. ---

  @Test
  @StdIo({"3 100", "1 2 3"})
  void targetLargerThanTheTotalSumCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The whole sequence sums to 6, so no subsequence can reach 100.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Negative values and a negative target. ---

  @Test
  @StdIo({"3 -5", "-1 -2 -3"})
  void negativeTargetReachedByNegativeElements(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Only {-2, -3} sums to -5.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Larger sequences spanning subsequences of several sizes. ---

  @Test
  @StdIo({"4 10", "1 2 3 4"})
  void onlyTheFullSequenceReachesItsTotalSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 1 + 2 + 3 + 4 = 10, and every proper subsequence sums to less, so the answer is 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4 5", "1 2 3 4"})
  void twoDistinctSubsequencesReachTheTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // {1, 4} and {2, 3} both sum to 5.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"5 5", "1 2 3 4 5"})
  void distinctValuesGiveThreeSubsequencesForTheTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // {5}, {1, 4} and {2, 3} all sum to 5.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- S = 0 must exclude the empty subsequence (the classic pitfall of this problem). ---

  @Test
  @StdIo({"4 0", "1 -1 2 -2"})
  void cancellingPairsReachZeroWithoutCountingTheEmptySet(StdOut out) throws IOException {
    Main.main(new String[0]);
    // {1, -1}, {2, -2} and {1, -1, 2, -2} sum to 0; counting the empty set would wrongly give 4.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"6 0", "1 -1 2 -2 3 -3"})
  void mixedSignsAtZeroTargetCountNineSubsequences(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A broader backtracking check: nine non-empty subsequences of the three ± pairs sum to 0.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  @Test
  @StdIo({"4 0", "0 0 0 0"})
  void everyNonEmptySubsequenceOfZerosCountsAtZeroTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // All 2^4 - 1 = 15 non-empty subsequences sum to 0; counting the empty set would give 16.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Upper bounds: N = 20 and the value / target limits. ---

  @Test
  @StdIo({"20 1", "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"})
  void twentyOnesPickingExactlyOneToReachTheTarget(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Picking any single 1 of the 20 reaches the target: C(20, 1) = 20.
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  @Test
  @StdIo({"20 10", "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1"})
  void twentyOnesPickingTenYieldsTheCentralBinomialCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The widest search (2^20 subsequences) with the largest count here: C(20, 10) = 184756.
    assertThat(out.capturedString().trim()).isEqualTo("184756");
  }

  @Test
  @StdIo({"10 1000000", "100000 100000 100000 100000 100000 100000 100000 100000 100000 100000"})
  void largestTargetReachedOnlyByAllMaximalValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Ten values of 100,000 sum to the maximum target 1,000,000 only when all ten are chosen.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }
}
