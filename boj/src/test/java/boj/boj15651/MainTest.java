package boj.boj15651;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15651 N과 M (3) (N and M (3)).
 *
 * <p>Given natural numbers N and M (1 ≤ M ≤ N ≤ 7), print every sequence of length M built from
 * numbers in 1..N where the same number may be chosen repeatedly, one per line with elements
 * separated by single spaces, in increasing lexicographic order, without repeating any sequence.
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
  void officialSampleTwoListsAllSixteenPairsOfNumbersUpToFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 4^2 = 16 pairs; repeated picks like "1 1" are included.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 1", "1 2", "1 3", "1 4", "2 1", "2 2", "2 3", "2 4", "3 1", "3 2", "3 3", "3 4",
            "4 1", "4 2", "4 3", "4 4");
  }

  @Test
  @StdIo("3 3")
  void officialSampleThreeListsAllTwentySevenTriplesOfNumbersUpToThree(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 3^3 = 27 triples in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 1 1", "1 1 2", "1 1 3", "1 2 1", "1 2 2", "1 2 3", "1 3 1", "1 3 2", "1 3 3",
            "2 1 1", "2 1 2", "2 1 3", "2 2 1", "2 2 2", "2 2 3", "2 3 1", "2 3 2", "2 3 3",
            "3 1 1", "3 1 2", "3 1 3", "3 2 1", "3 2 2", "3 2 3", "3 3 1", "3 3 2", "3 3 3");
  }

  // --- Smallest input N = M = 1: a single one-element sequence. ---

  @Test
  @StdIo("1 1")
  void smallestInputEmitsTheSingleSequenceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  // --- Repetition: the same number may be chosen more than once. ---

  @Test
  @StdIo("2 2")
  void pickingTwoOfTwoNumbersIncludesTheRepeatedPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Unlike the distinct-pick variants, "1 1" and "2 2" are valid sequences here.
    assertThat(out.capturedLines()).containsExactly("1 1", "1 2", "2 1", "2 2");
  }

  // --- Upper constraint bound N = 7. ---

  @Test
  @StdIo("7 1")
  void lengthOneAtMaxBoundListsEachOfTheSevenNumbersOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7");
  }

  @Test
  @StdIo("7 2")
  void lengthTwoAtMaxBoundEmitsSevenSquaredPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 7^2 = 49 pairs. Single-digit elements make plain string order match the required sequence
    // order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(49).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1");
    assertThat(lines[48]).isEqualTo("7 7");
  }

  @Test
  @StdIo("7 7")
  void largestInputEmitsAllSevenToTheSeventhSequencesInLexicographicOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 7^7 = 823543 sequences of 1..7 with repetition.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(823543).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1 1 1 1 1 1");
    assertThat(lines[823542]).isEqualTo("7 7 7 7 7 7 7");
  }
}
