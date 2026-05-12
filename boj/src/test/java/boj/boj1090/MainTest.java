package boj.boj1090;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  // Step 1: One checker — k=1 only. A lone checker is already a cluster of size 1, so 0 moves.
  @Test
  @StdIo({"1", "5 5"})
  void singleCheckerNeedsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 2: Two checkers stacked on the same cell (sample 3 from the problem) —
  // already together for both k=1 and k=2.
  @Test
  @StdIo({"2", "4 7", "4 7"})
  void twoCoincidentCheckersAreFreeForAllK(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0");
  }

  // Step 3: Two checkers with horizontal gap 1 — k=2 costs 1 move (one slides one cell).
  @Test
  @StdIo({"2", "1 1", "2 1"})
  void twoHorizontallyAdjacentCheckersCostOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1");
  }

  // Step 4: Two checkers separated diagonally — cost is Manhattan distance
  // |3-1| + |5-1| = 6, establishing that the answer separates into x- and y-axes.
  @Test
  @StdIo({"2", "1 1", "3 5"})
  void twoCheckersDiagonallySumAxisDistances(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 6");
  }

  // Step 5: Three collinear checkers — k=2 must pick the closest pair (gap 1),
  // and k=3 must meet at the median x=2: |1-2|+|2-2|+|4-2| = 3.
  // This shows the k=2 answer is NOT the same as "use the same subset as k=3".
  @Test
  @StdIo({"3", "1 1", "2 1", "4 1"})
  void threeCollinearCheckersPickPairThenMedian(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1 3");
  }

  // Step 6: Diamond around (15,15) — sample 1 from the problem. All six pairs have
  // Manhattan distance 2, every triple costs 3 at the center, and the full set costs 4.
  @Test
  @StdIo({"4", "15 14", "15 16", "14 15", "16 15"})
  void diamondLayoutSampleOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 2 3 4");
  }

  // Step 7: Four collinear checkers — sample 2. The k=3 optimum drops the far
  // outlier (9), giving sum 3 at x=2; k=4 must include it and pays sum 10 at any
  // median x in [2,4]. Pins down that the optimal k-subset can shrink as k decreases.
  @Test
  @StdIo({"4", "1 1", "2 1", "4 1", "9 1"})
  void collinearSpreadSampleTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1 3 10");
  }

  // Step 8: All checkers stacked — every k is 0. Verifies the output has exactly
  // N entries and that the answer for large k is not accidentally skipped.
  @Test
  @StdIo({"5", "10 10", "10 10", "10 10", "10 10", "10 10"})
  void allCheckersOnSameCellAreFreeForAllK(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 0 0 0");
  }

  // Step 9: Duplicates contribute independently — k=2 is free (the duplicate pair),
  // k=3 pays 1 to drag the third checker onto the duplicate cell.
  @Test
  @StdIo({"3", "1 1", "1 1", "2 1"})
  void duplicatesGiveFreeLowKAndCheapMedian(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 1");
  }

  // Step 10: Square corners — every pair of adjacent corners costs 4, every triple
  // costs 8 at a corner, and all four cost 16 at any meeting point in the square.
  // Confirms axis-separability holds for non-collinear inputs.
  @Test
  @StdIo({"4", "1 1", "1 5", "5 1", "5 5"})
  void unitSquareCornersHaveSymmetricCosts(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 4 8 16");
  }

  // Step 11: An outlier in y forces k=4 to pay a 99-cost y move, but k=3 can drop
  // it and stay on the cluster at y=100 for sum 2. This is the load-bearing test:
  // a solver that picks the k checkers with the smallest x-spread (which would
  // include (1,1) for k=3 — the three smallest xs) would compute 101, not 2. The
  // x- and y-subsets must be chosen together.
  @Test
  @StdIo({"4", "1 1", "2 100", "3 100", "4 100"})
  void subsetSelectionMustCoupleXAndY(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1 2 103");
  }

  // Step 12: Coordinates at the spec boundary (1 and 1,000,000) on both axes —
  // verifies the implementation reads and accumulates the full coordinate range
  // (|10^6 − 1| × 2 = 1,999,998) without overflow or off-by-one in parsing.
  @Test
  @StdIo({"2", "1 1", "1000000 1000000"})
  void coordinatesAtSpecBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1999998");
  }

  // Step 13: Maximum N (50) with all checkers stacked — stresses the loop bound
  // and the output construction (50 space-separated zeros, no trailing junk).
  @Test
  void maximumNAllAtSameCellAllZeros() throws IOException {
    int n = 50;
    StringBuilder input = new StringBuilder();
    input.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      input.append("1 1\n");
    }

    StringBuilder expected = new StringBuilder("0");
    for (int i = 1; i < n; i++) {
      expected.append(" 0");
    }

    assertThat(runMain(input.toString())).isEqualTo(expected.toString());
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return new String(out.toByteArray(), StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
