package boj.boj15654;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15654 N과 M (5) (N and M (5)).
 *
 * <p>Given N distinct natural numbers (each at most 10,000) and a natural number M (1 ≤ M ≤ N ≤ 8),
 * print every length-M sequence that picks M distinct numbers from the given ones, one per line
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
  void officialSampleTwoListsTheTwelveOrderedPairsOfDistinctGivenNumbers(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // P(4, 2) = 12 ordered pairs over the sorted values 1, 7, 8, 9.
    assertThat(out.capturedLines())
        .containsExactly(
            "1 7", "1 8", "1 9", "7 1", "7 8", "7 9", "8 1", "8 7", "8 9", "9 1", "9 7", "9 8");
  }

  @Test
  @StdIo({"4 4", "1231 1232 1233 1234"})
  void officialSampleThreeListsAllTwentyFourPermutationsOfFourDigitValues(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 4! = 24 permutations in lexicographic order.
    assertThat(out.capturedLines())
        .containsExactly(
            "1231 1232 1233 1234",
            "1231 1232 1234 1233",
            "1231 1233 1232 1234",
            "1231 1233 1234 1232",
            "1231 1234 1232 1233",
            "1231 1234 1233 1232",
            "1232 1231 1233 1234",
            "1232 1231 1234 1233",
            "1232 1233 1231 1234",
            "1232 1233 1234 1231",
            "1232 1234 1231 1233",
            "1232 1234 1233 1231",
            "1233 1231 1232 1234",
            "1233 1231 1234 1232",
            "1233 1232 1231 1234",
            "1233 1232 1234 1231",
            "1233 1234 1231 1232",
            "1233 1234 1232 1231",
            "1234 1231 1232 1233",
            "1234 1231 1233 1232",
            "1234 1232 1231 1233",
            "1234 1232 1233 1231",
            "1234 1233 1231 1232",
            "1234 1233 1232 1231");
  }

  // --- Smallest input N = M = 1: the single given number. ---

  @Test
  @StdIo({"1 1", "7"})
  void smallestInputEmitsTheSingleGivenNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("7");
  }

  // --- Distinct picks: each given number is used at most once per sequence. ---

  @Test
  @StdIo({"2 2", "1 2"})
  void pickingBothOfTwoNumbersExcludesRepeatedPicks(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Unlike the with-repetition variants, "1 1" and "2 2" are not valid sequences here.
    assertThat(out.capturedLines()).containsExactly("1 2", "2 1");
  }

  // --- Ordering is numeric, not textual: mixed-width values expose string sorting. ---

  @Test
  @StdIo({"3 2", "7 10 2"})
  void mixedWidthValuesAreOrderedNumericallyNotAsStrings(StdOut out) throws IOException {
    Main.main(new String[0]);
    // String comparison would put "10" before "2" and "2 10" before "2 7"; numeric order must
    // win.
    assertThat(out.capturedLines()).containsExactly("2 7", "2 10", "7 2", "7 10", "10 2", "10 7");
  }

  @Test
  @StdIo({"2 1", "10000 2"})
  void largestAllowedValueTenThousandSortsAfterASmallValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "10000");
  }

  // --- Upper constraint bound N = 8. ---

  @Test
  @StdIo({"8 1", "8 7 6 5 4 3 2 1"})
  void lengthOneAtMaxBoundListsTheEightGivenNumbersInSortedOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "2", "3", "4", "5", "6", "7", "8");
  }

  @Test
  @StdIo({"8 8", "8 7 6 5 4 3 2 1"})
  void largestInputEmitsAllPermutationsOfTheEightGivenNumbersInLexicographicOrder(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // 8! = 40320 permutations. Single-digit values make plain string order match the required
    // sequence order.
    String[] lines = out.capturedLines();
    assertThat(lines).hasSize(40320).doesNotHaveDuplicates().isSorted();
    assertThat(lines)
        .allSatisfy(line -> assertThat(line.split(" ")).hasSize(8).doesNotHaveDuplicates());
    assertThat(lines[0]).isEqualTo("1 2 3 4 5 6 7 8");
    assertThat(lines[40319]).isEqualTo("8 7 6 5 4 3 2 1");
  }
}
