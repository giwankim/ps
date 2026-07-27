package jungol.jungol1060;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.IntBinaryOperator;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Jungol 1060 최소비용신장트리 (Minimum Cost Spanning Tree), Advanced #10 그래프알고리즘1.
 *
 * <p>N academies (3 ≤ N ≤ 100) are to be joined into one network. The first line holds N; the rest
 * of the input is an N×N matrix of integers no greater than 100,000, where {@code mat[i][j]} is the
 * cost of running a line from academy i to academy j. An academy may share the lines of whatever it
 * is already connected to, so the cheapest network is a minimum spanning tree over the complete
 * graph the matrix describes. Print its total cost.
 *
 * <p>Only that total is printed, so ties between equally cheap trees stay invisible and every test
 * below asserts one exact number. The matrices are all symmetric, with a zero diagonal and strictly
 * positive costs off it—the graph is complete, and a zero away from the diagonal would be ambiguous
 * between a free line and a missing one.
 *
 * <p>With N ≤ 100 there are at most 4,950 distinct lines and the answer is at most 99 × 100,000 =
 * 9,900,000, so the total fits an {@code int} and a dense O(N²) Prim scan sits comfortably inside
 * the 1 s / 32 MB limits.
 */
class MainTest {

  // --- Official sample: lines 1-2 (5), 2-4 (3), 3-4 (1) and 4-5 (1) total 10. ---

  @Test
  @StdIo({"5", "0 5 10 8 7", "5 0 5 3 6", "10 5 0 1 3", "8 3 1 0 1", "7 6 3 1 0"})
  void officialSampleWiresFiveAcademiesForTen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Smallest city the constraints allow. Any two of a triangle's three lines span it,
  // so the answer is simply the two cheapest, and the third must go unused. ---

  @Test
  @StdIo({"3", "0 7 3", "7 0 4", "3 4 0"})
  void triangleCityTakesItsTwoCheapestLines(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Lines cost 7, 3 and 4; the 7 is left out.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"3", "0 1 1", "1 0 1", "1 1 0"})
  void triangleCityWithIdenticalCostsPaysForTwoLines(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Shapes that force one particular tree: a cheap chain, then a cheap hub. Both
  // pin down that exactly N-1 lines are bought, and which ones. ---

  @Test
  @StdIo({"4", "0 1 100 100", "1 0 1 100", "100 1 0 1", "100 100 1 0"})
  void cheapChainWiresFourAcademiesEndToEnd(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Only 1-2, 2-3 and 3-4 cost 1, and the three of them already span the city.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"4", "0 2 2 2", "2 0 9 9", "2 9 0 9", "2 9 9 0"})
  void cheapHubDrawsEveryLineFromOneAcademy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Cycles and bridges: the cheapest N-1 lines are not always a tree, and a lone
  // academy has to be paid for however dear its cheapest line is. ---

  @Test
  @StdIo({"4", "0 1 1 8", "1 0 1 8", "1 1 0 8", "8 8 8 0"})
  void cheapTriangleStillPaysFullPriceForTheFourthAcademy(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Three lines cost 1, but they close a cycle on academies 1-3; the third of them is
    // useless and academy 4 must be bought in at 8.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({
    "6",
    "0 1 1 100 100 100",
    "1 0 1 100 100 100",
    "1 1 0 100 100 100",
    "100 100 100 0 1 1",
    "100 100 100 1 0 1",
    "100 100 100 1 1 0"
  })
  void distantClusterBridgeIsPaidExactlyOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Two cheap triangles cost 2 each to span, joined by a single 100 crossing.
    assertThat(out.capturedString().trim()).isEqualTo("104");
  }

  @Test
  @StdIo({"5", "0 1 2 3 50", "1 0 4 5 60", "2 4 0 6 70", "3 5 6 0 80", "50 60 70 80 0"})
  void outlyingAcademyJoinsThroughItsOwnCheapestLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Academies 1-4 form a hub at 1 for 1 + 2 + 3, and academy 5 comes in at 50.
    assertThat(out.capturedString().trim()).isEqualTo("56");
  }

  @Test
  @StdIo({"4", "0 50 60 70", "50 0 1 2", "60 1 0 3", "70 2 3 0"})
  void expensiveFirstAcademyIsStillWiredByItsCheapestLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The outlier is academy 1 this time: 2-3 and 2-4 cost 1 and 2, then 1-2 costs 50.
    assertThat(out.capturedString().trim()).isEqualTo("53");
  }

  // --- A network whose tree is neither a chain nor a hub, and the same network with the
  // academies renumbered, since nothing may depend on the order the rows arrive in. ---

  @Test
  @StdIo({
    "6",
    "0 4 9 8 7 20",
    "4 0 5 12 6 21",
    "9 5 0 3 11 22",
    "8 12 3 0 2 23",
    "7 6 11 2 0 1",
    "20 21 22 23 1 0"
  })
  void sixAcademyNetworkBuildsATreeThatIsNeitherChainNorHub(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The tree is 5-6 (1), 4-5 (2), 3-4 (3), 1-2 (4) and 2-3 (5): branches at both ends.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  @Test
  @StdIo({
    "6",
    "0 22 20 1 21 23",
    "22 0 9 11 5 3",
    "20 9 0 7 4 8",
    "1 11 7 0 6 2",
    "21 5 4 6 0 12",
    "23 3 8 2 12 0"
  })
  void renumberingTheAcademiesLeavesTheCostUnchanged(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Same network as above, relabeled 6 3 1 5 2 4, so the same 15 lines get bought.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Every line priced the same: whatever tree comes out, it holds N-1 lines. ---

  @Test
  @StdIo({"4", "0 5 5 5", "5 0 5 5", "5 5 0 5", "5 5 5 0"})
  void identicalCostsEverywherePayForExactlyThreeLines(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Input shape: the statement promises N² space-separated costs, not one row per
  // line, so the scan must run on tokens rather than on line boundaries. ---

  @Test
  @StdIo({"5", "0 5 10 8 7 5 0 5 3 6", "  10   5 0 1 3", "8 3 1 0 1 7 6 3 1 0"})
  void matrixWrappedAcrossOddLineBreaksReadsTheSame(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The official sample's 25 costs, rewrapped and padded.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Cost ceiling: 100,000 per line on the smallest city, then on the largest, where
  // 99 full-price lines make the biggest total the problem can produce. ---

  @Test
  @StdIo({"3", "0 100000 100000", "100000 0 100000", "100000 100000 0"})
  void maximumCostLinesOnTheSmallestCity(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200000");
  }

  @Test
  void hundredAcademiesAllAtMaximumCostReachTheLargestPossibleTotal() throws IOException {
    assertThat(runMain(matrix(100, (i, j) -> 100_000))).isEqualTo("9900000");
  }

  // --- Full-size networks with a known tree: a distance matrix, whose unit-cost lines
  // chain the academies in order, and a hub that every academy joins for 1. ---

  @Test
  void hundredAcademiesOnADistanceMatrixFollowTheCheapChain() throws IOException {
    assertThat(runMain(matrix(100, (i, j) -> Math.abs(i - j)))).isEqualTo("99");
  }

  @Test
  void hundredAcademiesRoundASingleCheapHubPayOneEach() throws IOException {
    assertThat(runMain(matrix(100, (i, j) -> i == 1 || j == 1 ? 1 : 100_000))).isEqualTo("99");
  }

  private static String matrix(int n, IntBinaryOperator cost) {
    StringBuilder input = new StringBuilder().append(n).append('\n');
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        input.append(i == j ? 0 : cost.applyAsInt(i, j)).append(j == n ? '\n' : ' ');
      }
    }
    return input.toString();
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
