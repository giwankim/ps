package jungol.jungol5402;

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

/**
 * Jungol 5402 오리 무전기 (Moocast), USACO 2016 December Gold problem 1.
 *
 * <p>N ducks (1 ≤ N ≤ 1000) sit at integer coordinates in 0..25,000, one per line after N. Spending
 * X buys every duck a radio of range √X, so two ducks talk directly exactly when their squared
 * distance is at most X. Messages relay through intermediate ducks, so the flock is served as soon
 * as the whole graph is connected. Print the least integer X that connects everyone.
 *
 * <p>Working in squared distances keeps every quantity an exact integer—X itself is a squared
 * distance, so no square root is ever needed. Raising X only adds edges, so the question is the
 * smallest threshold that connects the complete graph: a minimum bottleneck spanning tree, which
 * any MST already is. The answer is therefore the heaviest edge the MST uses, which is neither the
 * MST's total weight nor the flock's widest separation. With N = 1000 the graph holds 499,500
 * edges, and the largest weight possible is 2 × 25,000² = 1,250,000,000—inside a signed 32-bit int,
 * but not by much.
 */
class MainTest {

  // --- Official sample. The three right-hand ducks cluster tightly and duck 1 joins
  // them across a gap of 17. ---

  @Test
  @StdIo({"4", "1 3", "5 4", "7 2", "6 1"})
  void officialSample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  // --- Degenerate flocks: a single duck has nobody to reach, and ducks stacked on one
  // spot already hear each other at range zero. ---

  @Test
  @StdIo({"1", "0 0"})
  void loneDuckBuysNoRangeAtAll(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "25000 25000"})
  void loneDuckInTheFarCornerStillBuysNoRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2", "5 5", "5 5"})
  void twoDucksSharingASpotBuyNoRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3", "7 9", "7 9", "7 9"})
  void wholeFlockOnOneSpotBuysNoRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The price is the squared distance, never the distance itself. ---

  @Test
  @StdIo({"2", "0 0", "3 4"})
  void twoDucksPayTheSquareOfTheirSeparation(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The 3-4-5 triangle: printing the distance would give 5 instead of 25.
    assertThat(out.capturedString().trim()).isEqualTo("25");
  }

  @Test
  @StdIo({"2", "0 0", "1 1"})
  void diagonalNeighborsPayTwoBecauseTheAnswerNeedNotBeAPerfectSquare(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // √2 is irrational, so any detour through a distance and back rounds badly here.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Relaying: the flock only pays for the widest single hop it must make, not for
  // the distance between its two most separated ducks. ---

  @Test
  @StdIo({"4", "0 0", "1 0", "2 0", "3 0"})
  void evenlySpacedLinePaysForOneHopNotTheWholeSpan(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The end ducks sit 9 apart squared and the hops total 3, but one hop costs 1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4", "0 0", "1 0", "5 0", "6 0"})
  void unevenLinePaysForItsWidestGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Gaps of 1, 16 and 1: the answer is the largest of them, not their sum of 18.
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  @Test
  @StdIo({"3", "0 0", "10 0", "5 1"})
  void relayThroughTheMiddleDuckBeatsTheDirectHop(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Talking end to end directly would cost 100; hopping via the duck just off the
    // midpoint costs 26 on either side.
    assertThat(out.capturedString().trim()).isEqualTo("26");
  }

  @Test
  @StdIo({"6", "0 0", "1 0", "0 1", "100 0", "101 0", "100 1"})
  void twoTightClustersPayOnlyForTheGapBetweenThem(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every duck's nearest neighbor is 1 or 2 away, which leaves two islands; bridging
    // them costs 99² and that single hop sets the price.
    assertThat(out.capturedString().trim()).isEqualTo("9801");
  }

  // --- Input shape: the ducks arrive in no particular order, so nothing may lean on
  // coordinates increasing down the list. ---

  @Test
  @StdIo({"4", "7 2", "1 3", "6 1", "5 4"})
  void shuffledInputDescribesTheSameFlock(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The official sample's ducks, listed in another order.
    assertThat(out.capturedString().trim()).isEqualTo("17");
  }

  @Test
  @StdIo({"4", "3 3", "2 2", "1 1", "0 0"})
  void descendingCoordinatesMeasureTheSameHops(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every delta comes out negative on this diagonal; squaring must wash that out.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Coordinate extremes: opposite corners of the grid are the priciest hop the
  // problem admits, and it lands just inside a signed 32-bit int. ---

  @Test
  @StdIo({"2", "0 0", "25000 25000"})
  void oppositeCornersCostTheLargestPricePossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 2 × 25,000² = 1,250,000,000, against Integer.MAX_VALUE of 2,147,483,647.
    assertThat(out.capturedString().trim()).isEqualTo("1250000000");
  }

  @Test
  @StdIo({"2", "0 25000", "25000 0"})
  void theOtherDiagonalCostsTheSame(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1250000000");
  }

  // --- Full flock: 1000 ducks make 499,500 pairs, so the edge sweep and the sort both
  // run at their worst size here. ---

  @Test
  void fullFlockStrungAlongALinePaysForOneSpacing() throws IOException {
    // 1000 ducks at x = 0, 3, 6, ... 2997 on the axis.
    StringBuilder input = new StringBuilder("1000\n");
    for (int i = 0; i < 1000; i++) {
      input.append(3 * i).append(" 0\n");
    }
    assertThat(runMain(input.toString())).isEqualTo("9");
  }

  @Test
  void fullFlockOnAGridPaysForOneCellStep() throws IOException {
    // A 25 × 40 lattice spaced 100 apart: side neighbors cost 10,000 and the diagonals
    // at 20,000 are never needed.
    StringBuilder input = new StringBuilder("1000\n");
    for (int col = 0; col < 25; col++) {
      for (int row = 0; row < 40; row++) {
        input.append(100 * col).append(' ').append(100 * row).append('\n');
      }
    }
    assertThat(runMain(input.toString())).isEqualTo("10000");
  }

  @Test
  void fullFlockStackedOnOneSpotStillCostsNothing() throws IOException {
    StringBuilder input = new StringBuilder("1000\n");
    for (int i = 0; i < 1000; i++) {
      input.append("12345 6789\n");
    }
    assertThat(runMain(input.toString())).isEqualTo("0");
  }

  @Test
  void loneOutlierAtFullFlockPushesThePriceNearTheIntegerLimit() throws IOException {
    // 999 ducks packed along the axis plus one stranded in the far corner; the whole
    // price is the reach from the nearest packed duck out to the stray.
    StringBuilder input = new StringBuilder("1000\n");
    for (int i = 0; i < 999; i++) {
      input.append(i).append(" 0\n");
    }
    input.append("25000 25000\n");
    assertThat(runMain(input.toString())).isEqualTo("1201096004");
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
