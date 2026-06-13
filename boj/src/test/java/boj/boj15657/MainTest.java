package boj.boj15657;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15657 N과 M (8) (N and M (8)).
 *
 * <p>Given N distinct natural numbers (each at most 10,000) and a natural number M (1 ≤ M ≤ N ≤ 8),
 * print every length-M sequence built from the given numbers where the same number may be chosen
 * repeatedly and the sequence is non-decreasing (each element is at least the one before it), one
 * per line with elements separated by single spaces, in increasing lexicographic order by numeric
 * value, without repeating any sequence.
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
  void officialSampleTwoListsTheTenNonDecreasingPairsOfGivenNumbers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(4+2-1, 2) = 10 multisets over the sorted values 1, 7, 8, 9; "7 1" is excluded but the
    // repeated pick "7 7" is included.
    assertThat(out.capturedLines())
        .containsExactly("1 1", "1 7", "1 8", "1 9", "7 7", "7 8", "7 9", "8 8", "8 9", "9 9");
  }

  @Test
  @StdIo({"4 4", "1231 1232 1233 1234"})
  void officialSampleThreeListsTheThirtyFiveNonDecreasingQuadruplesOfFourDigitValues(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // C(4+4-1, 4) = 35 multisets of size 4 in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1231 1231 1231 1231",
            "1231 1231 1231 1232",
            "1231 1231 1231 1233",
            "1231 1231 1231 1234",
            "1231 1231 1232 1232",
            "1231 1231 1232 1233",
            "1231 1231 1232 1234",
            "1231 1231 1233 1233",
            "1231 1231 1233 1234",
            "1231 1231 1234 1234",
            "1231 1232 1232 1232",
            "1231 1232 1232 1233",
            "1231 1232 1232 1234",
            "1231 1232 1233 1233",
            "1231 1232 1233 1234",
            "1231 1232 1234 1234",
            "1231 1233 1233 1233",
            "1231 1233 1233 1234",
            "1231 1233 1234 1234",
            "1231 1234 1234 1234",
            "1232 1232 1232 1232",
            "1232 1232 1232 1233",
            "1232 1232 1232 1234",
            "1232 1232 1233 1233",
            "1232 1232 1233 1234",
            "1232 1232 1234 1234",
            "1232 1233 1233 1233",
            "1232 1233 1233 1234",
            "1232 1233 1234 1234",
            "1232 1234 1234 1234",
            "1233 1233 1233 1233",
            "1233 1233 1233 1234",
            "1233 1233 1234 1234",
            "1233 1234 1234 1234",
            "1234 1234 1234 1234");
  }

  // --- Smallest input N = M = 1: the single given number. ---

  @Test
  @StdIo({"1 1", "7"})
  void smallestInputEmitsTheSingleGivenNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("7");
  }

  // --- Non-decreasing constraint: descending picks are excluded, repeats are kept. ---

  @Test
  @StdIo({"2 2", "2 1"})
  void pickingTwoOfTwoNumbersKeepsRepeatedPairsButDropsTheDescendingPair(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Unlike 15656, "2 1" is excluded; unlike 15655, "1 1" and "2 2" are included.
    assertThat(out.capturedLines()).containsExactly("1 1", "1 2", "2 2");
  }

  // --- Ordering is numeric, not textual: mixed-width values expose string sorting. ---

  @Test
  @StdIo({"3 2", "7 10 2"})
  void mixedWidthValuesAreOrderedNumericallyNotAsStrings(StdOut out) throws IOException {
    Main.main(new String[0]);
    // String comparison would put "10 ..." before "2 ..." and "2 10" before "2 7"; numeric order
    // must win.
    assertThat(out.capturedLines()).containsExactly("2 2", "2 7", "2 10", "7 7", "7 10", "10 10");
  }

  @Test
  @StdIo({"2 2", "10000 2"})
  void largestAllowedValueTenThousandMayBeRepeated(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The descending pair "10000 2" is excluded.
    assertThat(out.capturedLines()).containsExactly("2 2", "2 10000", "10000 10000");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo({"8 1", "8 7 6 5 4 3 2 1"})
  void lengthOneAtMaxBoundListsTheEightGivenNumbersInSortedOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo({"8 2", "8 7 6 5 4 3 2 1"})
  void lengthTwoAtMaxBoundEmitsThirtySixNonDecreasingPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8+2-1, 2) = 36 pairs. Single-digit values make plain string order match the required
    // sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(36).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1");
    assertThat(lines[35]).isEqualTo("8 8");
  }

  @Test
  @StdIo({"8 8", "8 7 6 5 4 3 2 1"})
  void largestInputEmitsAllSizeEightMultisetsInLexicographicOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // C(8+8-1, 8) = 6435 multisets of size 8 drawn from the eight given numbers.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(6435).doesNotHaveDuplicates().isSorted();
    assertThat(lines).allSatisfy(line -> assertThat(line.split(" ")).hasSize(8).isSorted());
    assertThat(lines[0]).isEqualTo("1 1 1 1 1 1 1 1");
    assertThat(lines[6434]).isEqualTo("8 8 8 8 8 8 8 8");
  }
}
