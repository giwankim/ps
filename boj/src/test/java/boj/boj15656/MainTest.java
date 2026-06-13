package boj.boj15656;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15656 N과 M (7) (N and M (7)).
 *
 * <p>Given N distinct natural numbers (each at most 10,000) and a natural number M (1 ≤ M ≤ N ≤ 7),
 * print every length-M sequence built from the given numbers where the same number may be chosen
 * repeatedly, one per line with elements separated by single spaces, in increasing lexicographic
 * order by numeric value, without repeating any sequence.
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
  void officialSampleTwoListsAllSixteenPairsOfGivenNumbers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 4^2 = 16 pairs over the sorted values 1, 7, 8, 9; repeated picks like "1 1" are included.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 1", "1 7", "1 8", "1 9", "7 1", "7 7", "7 8", "7 9", "8 1", "8 7", "8 8", "8 9",
            "9 1", "9 7", "9 8", "9 9");
  }

  @Test
  @StdIo({"3 3", "1231 1232 1233"})
  void officialSampleThreeListsAllTwentySevenTriplesOfFourDigitValues(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 3^3 = 27 triples in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1231 1231 1231",
            "1231 1231 1232",
            "1231 1231 1233",
            "1231 1232 1231",
            "1231 1232 1232",
            "1231 1232 1233",
            "1231 1233 1231",
            "1231 1233 1232",
            "1231 1233 1233",
            "1232 1231 1231",
            "1232 1231 1232",
            "1232 1231 1233",
            "1232 1232 1231",
            "1232 1232 1232",
            "1232 1232 1233",
            "1232 1233 1231",
            "1232 1233 1232",
            "1232 1233 1233",
            "1233 1231 1231",
            "1233 1231 1232",
            "1233 1231 1233",
            "1233 1232 1231",
            "1233 1232 1232",
            "1233 1232 1233",
            "1233 1233 1231",
            "1233 1233 1232",
            "1233 1233 1233");
  }

  // --- Smallest input N = M = 1: the single given number. ---

  @Test
  @StdIo({"1 1", "7"})
  void smallestInputEmitsTheSingleGivenNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("7");
  }

  // --- Repetition: the same given number may be chosen more than once. ---

  @Test
  @StdIo({"2 2", "2 1"})
  void pickingTwoOfTwoNumbersIncludesTheRepeatedPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Unlike 15654, "1 1" and "2 2" are valid; unlike 15655, "2 1" is valid as well.
    assertThat(out.capturedLines()).containsExactly("1 1", "1 2", "2 1", "2 2");
  }

  // --- Ordering is numeric, not textual: mixed-width values expose string sorting. ---

  @Test
  @StdIo({"3 2", "7 10 2"})
  void mixedWidthValuesAreOrderedNumericallyNotAsStrings(StdOut out) throws IOException {
    Main.main(new String[0]);
    // String comparison would put "10 ..." before "2 ..." and "2 10" before "2 7"; numeric order
    // must win.
    assertThat(out.capturedLines())
        .containsExactly("2 2", "2 7", "2 10", "7 2", "7 7", "7 10", "10 2", "10 7", "10 10");
  }

  @Test
  @StdIo({"2 2", "10000 2"})
  void largestAllowedValueTenThousandMayBeRepeated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2 2", "2 10000", "10000 2", "10000 10000");
  }

  // --- Upper constraint bound N = 7. ---

  @Test
  @StdIo({"7 1", "7 6 5 4 3 2 1"})
  void lengthOneAtMaxBoundListsTheSevenGivenNumbersInSortedOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7");
  }

  @Test
  @StdIo({"7 2", "7 6 5 4 3 2 1"})
  void lengthTwoAtMaxBoundEmitsSevenSquaredPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 7^2 = 49 pairs. Single-digit values make plain string order match the required sequence
    // order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(49).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1");
    assertThat(lines[48]).isEqualTo("7 7");
  }

  @Test
  @StdIo({"7 7", "7 6 5 4 3 2 1"})
  void largestInputEmitsAllSevenToTheSeventhSequencesInLexicographicOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 7^7 = 823543 sequences of the seven given numbers with repetition.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(823543).doesNotHaveDuplicates().isSorted();
    assertThat(lines[0]).isEqualTo("1 1 1 1 1 1 1");
    assertThat(lines[823542]).isEqualTo("7 7 7 7 7 7 7");
  }
}
