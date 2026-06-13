package boj.boj15655;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15655 N과 M (6) (N and M (6)).
 *
 * <p>Given N distinct natural numbers (each at most 10,000) and a natural number M (1 ≤ M ≤ N ≤ 8),
 * print every length-M strictly ascending sequence that picks M of the given numbers, one per line
 * with elements separated by single spaces, in increasing lexicographic order by numeric value,
 * without repeating any sequence.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"3 1", "4 5 2"})
  void officialSampleOneSortsTheGivenNumbersBeforeListingThem(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Input order 4 5 2 must not leak through; sequences are ordered by value.
    assertThat(out.capturedLines()).containsExactly("2", "4", "5");
  }

  @Test
  @StdIo({"4 2", "9 8 7 1"})
  void officialSampleTwoListsTheSixAscendingPairsOfGivenNumbers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(4, 2) = 6 ascending pairs over the sorted values 1, 7, 8, 9; "7 1" is excluded.
    assertThat(out.capturedLines()).containsExactly("1 7", "1 8", "1 9", "7 8", "7 9", "8 9");
  }

  @Test
  @StdIo({"4 4", "1231 1232 1233 1234"})
  void officialSampleThreeChoosingAllFourValuesYieldsTheSingleSortedSequence(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // C(4, 4) = 1: the only ascending arrangement of all four values.
    assertThat(out.capturedLines()).containsExactly("1231 1232 1233 1234");
  }

  // --- Smallest input N = M = 1: the single given number. ---

  @Test
  @StdIo({"1 1", "7"})
  void smallestInputEmitsTheSingleGivenNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("7");
  }

  // --- Ascending constraint: each pair of values yields exactly one sequence. ---

  @Test
  @StdIo({"2 2", "2 1"})
  void pickingBothOfTwoNumbersYieldsOnlyTheAscendingArrangement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Unlike 15654, "2 1" is excluded; unlike 15652, repeats like "1 1" are not possible.
    assertThat(out.capturedLines()).containsExactly("1 2");
  }

  @Test
  @StdIo({"3 2", "3 1 2"})
  void pickingTwoOfThreeNumbersEmitsTheThreeAscendingPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(3, 2) = 3 pairs; the descending arrangements are excluded.
    assertThat(out.capturedLines()).containsExactly("1 2", "1 3", "2 3");
  }

  // --- Ordering is numeric, not textual: mixed-width values expose string sorting. ---

  @Test
  @StdIo({"3 2", "7 10 2"})
  void mixedWidthValuesAreOrderedNumericallyNotAsStrings(StdOut out) throws IOException {
    Main.main(new String[0]);
    // String comparison would put "2 10" before "2 7"; numeric order must win.
    assertThat(out.capturedLines()).containsExactly("2 7", "2 10", "7 10");
  }

  @Test
  @StdIo({"2 2", "10000 2"})
  void largestAllowedValueTenThousandEndsTheSingleAscendingPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2 10000");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo({"8 1", "8 7 6 5 4 3 2 1"})
  void lengthOneAtMaxBoundListsTheEightGivenNumbersInSortedOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo({"8 4", "8 7 6 5 4 3 2 1"})
  void choosingFourOfEightEmitsAllSeventyAscendingQuadruples(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8, 4) = 70, the widest output any N = 8 input can produce. Single-digit values make plain
    // string order match the required sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(70).doesNotHaveDuplicates().isSorted();
    assertThat(lines)
        .allSatisfy(line ->
            assertThat(line.split(" ")).hasSize(4).doesNotHaveDuplicates().isSorted());
    assertThat(lines[0]).isEqualTo("1 2 3 4");
    assertThat(lines[69]).isEqualTo("5 6 7 8");
  }

  @Test
  @StdIo({"8 8", "8 7 6 5 4 3 2 1"})
  void choosingAllEightValuesYieldsTheSingleFullySortedSequence(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8, 8) = 1: the largest N = M input collapses to one line, the sorted input itself.
    assertThat(out.capturedLines()).containsExactly("1 2 3 4 5 6 7 8");
  }
}
