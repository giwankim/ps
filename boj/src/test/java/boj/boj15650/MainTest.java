package boj.boj15650;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15650 N과 M (2) (N and M (2)).
 *
 * <p>Given natural numbers N and M (1 ≤ M ≤ N ≤ 8), print every strictly ascending sequence of
 * length M built from distinct numbers in 1..N, one per line with elements separated by single
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
  void officialSampleTwoListsAllAscendingPairsOfNumbersUpToFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(4, 2) = 6 ascending pairs; descending pairs like "2 1" are excluded.
    assertThat(out.capturedLines()).containsExactly("1 2", "1 3", "1 4", "2 3", "2 4", "3 4");
  }

  @Test
  @StdIo("4 4")
  void officialSampleThreeFullLengthSequenceIsTheSingleAscendingRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    // M = N leaves exactly one ascending arrangement of all four numbers.
    assertThat(out.capturedLines()).containsExactly("1 2 3 4");
  }

  // --- Smallest input N = M = 1: a single one-element sequence. ---

  @Test
  @StdIo("1 1")
  void smallestInputEmitsTheSingleSequenceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  // --- Ascending constraint: descending arrangements of chosen numbers must not appear. ---

  @Test
  @StdIo("2 2")
  void pickingBothOfTwoNumbersKeepsOnlyTheAscendingPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    // "2 1" picks distinct numbers but is not ascending, so only "1 2" remains.
    assertThat(out.capturedLines()).containsExactly("1 2");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo("8 1")
  void lengthOneAtMaxBoundListsEachOfTheEightNumbersOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo("8 4")
  void halfLengthAtMaxBoundEmitsTheSeventyFourElementCombinations(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8, 4) = 70 is the largest output this problem allows. Single-digit elements make plain
    // string order match the required sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(70).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 2 3 4");
    assertThat(lines[69]).isEqualTo("5 6 7 8");
  }

  @Test
  @StdIo("8 8")
  void largestInputEmitsTheSingleAscendingRunOfAllEightNumbers(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1 2 3 4 5 6 7 8");
  }
}
