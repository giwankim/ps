package boj.boj15649;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15649 N과 M (1) (N and M (1)).
 *
 * <p>Given natural numbers N and M (1 ≤ M ≤ N ≤ 8), print every sequence of length M built from
 * distinct numbers in 1..N, one per line with elements separated by single spaces, in increasing
 * lexicographic order, without repeating any sequence.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo("3 1")
  void officialSampleOneLengthOneSequencesAreTheNumbersOneToThreeInOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3");
  }

  @Test
  @StdIo("4 2")
  void officialSampleTwoListsAllOrderedPairsOfDistinctNumbersUpToFour(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 4 * 3 = 12 ordered pairs; pairs like "1 1" are excluded.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 2", "1 3", "1 4", "2 1", "2 3", "2 4", "3 1", "3 2", "3 4", "4 1", "4 2", "4 3");
  }

  @Test
  @StdIo("4 4")
  void officialSampleThreeListsAllTwentyFourPermutationsOfOneToFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // M = N, so the sequences are exactly the 4! = 24 permutations in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 2 3 4", "1 2 4 3", "1 3 2 4", "1 3 4 2", "1 4 2 3", "1 4 3 2", "2 1 3 4", "2 1 4 3",
            "2 3 1 4", "2 3 4 1", "2 4 1 3", "2 4 3 1", "3 1 2 4", "3 1 4 2", "3 2 1 4", "3 2 4 1",
            "3 4 1 2", "3 4 2 1", "4 1 2 3", "4 1 3 2", "4 2 1 3", "4 2 3 1", "4 3 1 2", "4 3 2 1");
  }

  // --- Smallest input N = M = 1: a single one-element sequence. ---

  @Test
  @StdIo("1 1")
  void smallestInputEmitsTheSingleSequenceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  // --- Distinctness: a number already used in the sequence must not be reused. ---

  @Test
  @StdIo("2 2")
  void pickingBothOfTwoNumbersExcludesTheRepeatedPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // "1 1" and "2 2" reuse a number and must not appear.
    assertThat(out.capturedLines()).containsExactly("1 2", "2 1");
  }

  @Test
  @StdIo("3 3")
  void fullLengthSequencesAreExactlyThePermutationsOfOneToThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines())
        .containsExactly("1 2 3", "1 3 2", "2 1 3", "2 3 1", "3 1 2", "3 2 1");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo("8 1")
  void lengthOneAtMaxBoundListsEachOfTheEightNumbersOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo("8 3")
  void lengthThreeAtMaxBoundEmitsEightTimesSevenTimesSixOrderedTriples(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Single-digit elements make plain string order match the required sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(336).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 2 3");
    assertThat(lines[335]).isEqualTo("8 7 6");
  }

  @Test
  @StdIo("8 8")
  void largestInputEmitsAllFortyThousandPermutationsInLexicographicOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 8! = 40320 permutations of 1..8.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(40320).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 2 3 4 5 6 7 8");
    assertThat(lines[40319]).isEqualTo("8 7 6 5 4 3 2 1");
  }
}
