package boj.boj15652;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15652 N과 M (4) (N and M (4)).
 *
 * <p>Given natural numbers N and M (1 ≤ M ≤ N ≤ 8), print every length-M sequence built from
 * numbers in 1..N where the same number may be chosen repeatedly and the sequence is non-decreasing
 * (each element is at least the one before it), one per line with elements separated by single
 * spaces, in increasing lexicographic order, without repeating any sequence.
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
  void officialSampleTwoListsTheTenNonDecreasingPairsOfNumbersUpToFour(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // C(4+2-1, 2) = 10 multisets; "2 1" is excluded but the repeated pick "2 2" is included.
    assertThat(out.capturedLines())
        .containsExactly("1 1", "1 2", "1 3", "1 4", "2 2", "2 3", "2 4", "3 3", "3 4", "4 4");
  }

  @Test
  @StdIo("3 3")
  void officialSampleThreeListsTheTenNonDecreasingTriplesOfNumbersUpToThree(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // C(3+3-1, 3) = 10 triples in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 1 1", "1 1 2", "1 1 3", "1 2 2", "1 2 3", "1 3 3", "2 2 2", "2 2 3", "2 3 3",
            "3 3 3");
  }

  // --- Smallest input N = M = 1: a single one-element sequence. ---

  @Test
  @StdIo("1 1")
  void smallestInputEmitsTheSingleSequenceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  // --- Non-decreasing constraint: descending picks are excluded, repeats are kept. ---

  @Test
  @StdIo("2 2")
  void pickingTwoOfTwoNumbersKeepsRepeatedPairsButDropsTheDescendingPair(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Unlike 15651, "2 1" is excluded; unlike 15650, "1 1" and "2 2" are included.
    assertThat(out.capturedLines()).containsExactly("1 1", "1 2", "2 2");
  }

  @Test
  @StdIo("3 2")
  void pickingTwoOfThreeNumbersEmitsTheSixMultisetsInLexicographicOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // C(3+2-1, 2) = 6 multisets of size 2 drawn from {1, 2, 3}.
    assertThat(out.capturedLines()).containsExactly("1 1", "1 2", "1 3", "2 2", "2 3", "3 3");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo("8 1")
  void lengthOneAtMaxBoundListsEachOfTheEightNumbersOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo("8 2")
  void lengthTwoAtMaxBoundEmitsThirtySixNonDecreasingPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8+2-1, 2) = 36 pairs. Single-digit elements make plain string order match the required
    // sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(36).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1");
    assertThat(lines[35]).isEqualTo("8 8");
  }

  @Test
  @StdIo("8 8")
  void largestInputEmitsAllSizeEightMultisetsInLexicographicOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8+8-1, 8) = 6435 multisets of size 8 drawn from {1..8}.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(6435).doesNotHaveDuplicates().isSorted();
    assertThat(lines).allSatisfy(line -> assertThat(line.split(" ")).hasSize(8).isSorted());
    assertThat(lines[0]).isEqualTo("1 1 1 1 1 1 1 1");
    assertThat(lines[6434]).isEqualTo("8 8 8 8 8 8 8 8");
  }
}
