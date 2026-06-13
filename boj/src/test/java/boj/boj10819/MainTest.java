package boj.boj10819;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 10819 차이를 최대로 (Maximize the difference).
 *
 * <p>An array A of N integers is given (3 ≤ N ≤ 8, each element in [−100, 100]). Reorder the
 * elements to maximize
 *
 * <pre>|A[0] − A[1]| + |A[1] − A[2]| + ... + |A[N−2] − A[N−1]|</pre>
 *
 * <p>The first input line holds N; the second holds the N integers separated by spaces. Print the
 * single maximum value on one line. Because N is at most 8, every one of the N! ≤ 40320
 * permutations can be evaluated, so the answer is the largest alternating-difference sum over all
 * orderings — interior elements contribute to two terms and end elements to one, so the optimum
 * pushes large and small values into alternating positions.
 */
class MainTest {

  // --- Smallest array: N = 3, the lower bound. ---

  @Test
  @StdIo({"3", "1 2 3"})
  void threeDistinctValuesPlaceAnExtremeInTheMiddle(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The middle position counts twice, so an extreme belongs there: 1, 3, 2 gives
    // |1−3| + |3−2| = 3, beating the median-in-the-middle order 1, 2, 3 (only 2).
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"3", "5 5 5"})
  void allEqualValuesYieldZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every element is identical, so every adjacent difference is 0 no matter the order.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3", "7 7 1"})
  void aDuplicatedValuePlacesTheOddOneInTheMiddle(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Putting the lone 1 between the two 7s gives |7−1| + |1−7| = 12; any order that seats the
    // two 7s next to each other wastes a difference of 0.
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Negative values: elements range over [−100, 100]. ---

  @Test
  @StdIo({"3", "-100 0 100"})
  void negativesAndPositivesAreMixedToMaximizeSpread(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Seating an extreme in the doubled middle position — 0, 100, −100 — gives
    // |0−100| + |100−(−100)| = 300, beating the natural order −100, 0, 100 (only 200).
    assertThat(out.capturedString().trim()).isEqualTo("300");
  }

  @Test
  @StdIo({"5", "3 -7 2 -1 8"})
  void aSetOfMixedSignValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The zig-zag 3, −7, 8, −1, 2 alternates lows and highs:
    // |3−(−7)| + |−7−8| + |8−(−1)| + |−1−2| = 10 + 15 + 9 + 3 = 37.
    assertThat(out.capturedString().trim()).isEqualTo("37");
  }

  // --- Reordering is the whole point: a sorted array is not optimal. ---

  @Test
  @StdIo({"4", "1 2 3 4"})
  void ascendingOrderIsNotOptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Sorted order 1, 2, 3, 4 sums to only 3; the zig-zag 2, 4, 1, 3 gives
    // |2−4| + |4−1| + |1−3| = 2 + 3 + 2 = 7.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Alternation between extremes maximizes the sum. ---

  @Test
  @StdIo({"4", "1 1 100 100"})
  void twoDistinctValuesAlternate(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Interleaving the two values — 1, 100, 1, 100 — spends the full gap on every adjacency:
    // 99 + 99 + 99 = 297.
    assertThat(out.capturedString().trim()).isEqualTo("297");
  }

  @Test
  @StdIo({"4", "-100 100 -100 100"})
  void alternatingTheRangeExtremes(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The biggest possible gap is the full range 200; alternating −100 and 100 hits it on all
    // three adjacencies for 200 × 3 = 600.
    assertThat(out.capturedString().trim()).isEqualTo("600");
  }

  // --- Official sample, and independence from input order. ---

  @Test
  @StdIo({"6", "20 1 15 8 4 10"})
  void officialSampleReordersSixValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The arrangement 8, 20, 1, 15, 4, 10 yields
    // |8−20| + |20−1| + |1−15| + |15−4| + |4−10| = 12 + 19 + 14 + 11 + 6 = 62.
    assertThat(out.capturedString().trim()).isEqualTo("62");
  }

  @Test
  @StdIo({"6", "10 20 8 1 4 15"})
  void theAnswerDependsOnlyOnTheMultisetNotTheInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Same six values as the official sample, supplied in a different order; the program reorders
    // them freely, so the maximum is unchanged at 62.
    assertThat(out.capturedString().trim()).isEqualTo("62");
  }

  // --- Upper bound: the largest array, N = 8. ---

  @Test
  @StdIo({"8", "100 -100 100 -100 100 -100 100 -100"})
  void largestArrayAlternatingExtremesReachesTheTheoreticalMax(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With four copies each of the range extremes, every one of the seven adjacencies can span the
    // full gap of 200: 200 × 7 = 1400, the largest sum any N = 8 input can reach.
    assertThat(out.capturedString().trim()).isEqualTo("1400");
  }

  @Test
  @StdIo({"8", "1 2 3 4 5 6 7 8"})
  void largestArrayOfConsecutiveValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Eight consecutive values zig-zagged — e.g. 4, 6, 1, 7, 2, 8, 3, 5 —
    // sum to 2 + 5 + 6 + 5 + 6 + 5 + 2 = 31.
    assertThat(out.capturedString().trim()).isEqualTo("31");
  }
}
