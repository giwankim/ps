package boj.boj1438;

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
 * BOJ 1438 가장 작은 직사각형 (Smallest Rectangle).
 *
 * <p>Given N distinct points with non-negative integer coordinates, find the minimum area of an
 * axis-aligned, integer-corner rectangle whose <em>interior</em> contains at least N/2 points.
 * Points lying on the border do not count as interior, so the tightest rectangle around points
 * spanning {@code [xa, xb] x [ya, yb]} has area {@code (xb - xa + 2) * (yb - ya + 2)}.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"6", "10 5", "11 5", "13 5", "10 15", "11 16", "13 17"})
  void officialSampleOneBottomRowOfThreeIsTheTightestHalf(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 3 of 6. The collinear bottom row (10,5),(11,5),(13,5): (13-10+2)*(5-5+2) = 5*2.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"2", "100 100", "200 200"})
  void officialSampleTwoNeedOnlyOnePointSoTheBoxIsAUnitMargin(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 1 of 2. A single point gives (0+2)*(0+2) = 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"8", "5 7", "6 6", "6 8", "7 5", "7 9", "8 6", "8 8", "9 7"})
  void officialSampleThreeDiamondOfEightNeedsAFourByFourBox(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 4 of 8. Box x in [6,8], y in [6,8] holds the four corner points: (2+2)*(2+2) = 16.
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Smallest input N = 2: only one point is ever required, so the answer is always 4. ---

  @Test
  @StdIo({"2", "0 0", "10000 10000"})
  void originAndMaxCoordinatePointStillNeedOnlyOneForAreaFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Exercises the coordinate bounds (0 and 10,000) and a rectangle corner at (-1,-1).
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The +2 margin: a degenerate (zero-area) box must never be reported. ---

  @Test
  @StdIo({"4", "0 0", "2 0", "0 5", "2 5"})
  void twoPointsSharingARowStillNeedVerticalMarginSoAreaIsNotZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. Cheapest pair shares a row, e.g. (0,0),(2,0): (2+2)*(0+2) = 8, not 0.
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Collinear points: the cheapest half is the closest adjacent run. ---

  @Test
  @StdIo({"4", "0 5", "1 5", "2 5", "3 5"})
  void fourHorizontalPointsTakeTheCheapestAdjacentPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. Adjacent x differ by 1: (1+2)*(0+2) = 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"4", "5 0", "5 1", "5 2", "5 3"})
  void fourVerticalPointsTakeTheCheapestAdjacentPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. Adjacent y differ by 1: (0+2)*(1+2) = 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Choosing the minimum means not over-enclosing: take the pair, not the whole block. ---

  @Test
  @StdIo({"4", "0 0", "0 1", "1 0", "1 1"})
  void twoByTwoGridNeedsOnlyAnAdjacentPairNotTheWholeBlock(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. An adjacent pair is 2*3 = 6; the full [0,1]x[0,1] box would be 3*3 = 9.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- No two points share an axis, so even the closest pair pays on both dimensions. ---

  @Test
  @StdIo({"4", "0 0", "1 1", "2 2", "3 3"})
  void diagonalStaircasePairsCostNineBecauseNeitherAxisIsShared(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. Closest pair, e.g. (0,0),(1,1): (1+2)*(1+2) = 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- A tight cluster of exactly N/2 beats an equally-sized distant cluster. ---

  @Test
  @StdIo({"6", "0 0", "1 0", "0 1", "1000 1000", "1001 1000", "1000 1001"})
  void tightClusterOfHalfIsTheCheapestEnclosure(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 3 of 6. Either L-shaped cluster sits in a [.,.+1]x[.,.+1] box: (1+2)*(1+2) = 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- A point at the origin forces the rectangle to extend into negative coordinates. ---

  @Test
  @StdIo({"4", "0 0", "0 1", "5 5", "6 6"})
  void originAdjacentPairExtendsRectangleIntoNegativeCornersForAreaSix(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. (0,0),(0,1) -> corners (-1,-1)..(1,2): (0+2)*(1+2) = 6, beating (5,5),(6,6)=9.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Maximum coordinate spread: the cheapest half is an edge of the grid, not a diagonal. ---

  @Test
  @StdIo({"4", "0 0", "0 10000", "10000 0", "10000 10000"})
  void fourCornersOfTheGridTakeTheCheapestEdgeStrip(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Need 2 of 4. An edge pair shares an axis: (0+2)*(10000+2) = 20004; a diagonal would be huge.
    assertThat(out.capturedString().trim()).isEqualTo("20004");
  }

  // --- Upper size bound N = 100, built with generators (mirrors the project's larger-input tests).

  @Test
  void hundredPointsOnADiagonalNeedFiftyConsecutiveForA51By51Box() throws IOException {
    // Points (i,i) for i in 0..99; need 50. The cheapest 50 are consecutive on the diagonal,
    // forcing a span of 49 on each axis: (49+2)*(49+2) = 2601.
    assertThat(runMain(diagonalPoints(100))).isEqualTo("2601");
  }

  @Test
  void hundredPointsOnAHorizontalLineNeedFiftyConsecutiveForAThinStrip() throws IOException {
    // Points (i,0) for i in 0..99; need 50. The cheapest 50 span 49 in x and 0 in y:
    // (49+2)*(0+2) = 102.
    assertThat(runMain(horizontalPoints(100))).isEqualTo("102");
  }

  /** Builds input for {@code n} points where point {@code i} sits at {@code (i, i)}. */
  private static String diagonalPoints(int n) {
    StringBuilder sb = new StringBuilder().append(n).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(i).append(' ').append(i).append('\n');
    }
    return sb.toString();
  }

  /** Builds input for {@code n} points where point {@code i} sits at {@code (i, 0)}. */
  private static String horizontalPoints(int n) {
    StringBuilder sb = new StringBuilder().append(n).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(i).append(' ').append(0).append('\n');
    }
    return sb.toString();
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
