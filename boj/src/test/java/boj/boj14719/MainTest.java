package boj.boj14719;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 14719 빗물 (Rainwater) -- the classic "trapping rain water" problem in a 2D world.
 *
 * <p>Blocks are stacked in {@code W} columns; column {@code i} holds {@code height[i]} blocks.
 * Enough rain falls to fill every enclosed gap, and the question is the total trapped volume (one
 * cell = one unit). Above each column the water rises to {@code min(maxLeft, maxRight)} -- the
 * shorter of the tallest wall to its left and the tallest wall to its right -- so that column
 * contributes {@code max(0, min(maxLeft, maxRight) - height[i])}. The two outermost columns have no
 * wall beyond them and therefore trap nothing.
 *
 * <p>Input: the first line is {@code H W} (vertical then horizontal size); the second line is
 * {@code W} integers, the block heights from left to right. Output: a single line with the total
 * trapped water, or {@code 0} when none collects.
 *
 * <p>Constraints: {@code 1 <= H, W <= 500}; every height is an integer in {@code [0, H]}. {@code H}
 * is only a bound on the inputs -- it never enters the formula -- so the heaviest possible answer
 * is a full-width tank {@code [H, 0, ..., 0, H]}, which holds {@code (W - 2) * H <= 498 * 500 =
 * 249,000}, comfortably inside a signed 32-bit {@code int}.
 *
 * <p>Two properties shape these fixtures. First, the {@code min(maxLeft, maxRight)} rule: water is
 * capped by the <em>shorter</em> wall, so an implementation that reads only one side, or takes the
 * taller, is caught by the asymmetric-wall cases below. Second, the {@code max(0, ...)} clamp: an
 * interior peak taller than its effective walls must contribute exactly zero, never a negative.
 * (Total trapped water is invariant under reversing the profile, so a left/right transposition is
 * invisible to the total; the asymmetric-wall tests target the related "one side only" bug
 * instead.)
 */
class MainTest {

  // --- Official samples from the statement. ---

  @Test
  @StdIo({"4 4", "3 0 1 4"})
  void officialSampleOneFillsTheTwoInteriorDips(StdOut out) throws IOException {
    Main.main(new String[0]);
    // col 1 (0): min(3, 4) - 0 = 3; col 2 (1): min(3, 4) - 1 = 2. Total 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"4 8", "3 1 2 3 4 1 1 2"})
  void officialSampleTwoSumsAStepProfile(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Left rim 3, peak 4: cols 1..3 add 2+1+0, cols 5..6 add 1+1 (capped by the right rim 2). Total
    // 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"3 5", "0 0 0 2 0"})
  void officialSampleThreeHasNoEnclosingWallSoNothingCollects(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single block sits at the right with only zeros around it -- no basin forms. Total 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Width boundaries: water needs an interior column flanked by two walls. ---

  @Test
  @StdIo({"5 1", "3"})
  void singleColumnWorldTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // W = 1: no interior column exists, so the answer is 0 regardless of height.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3 2", "3 3"})
  void twoColumnWorldTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // W = 2: both columns are outer walls with no gap between them. Total 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2 3", "2 0 2"})
  void smallestPossibleBasinTrapsOneColumnOfWater(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The minimal trapping shape: min(2, 2) - 0 = 2 over the single middle column.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Monotone and flat profiles: every shape here is an "open slope" that holds no water. ---

  @Test
  @StdIo({"4 5", "0 1 2 3 4"})
  void strictlyIncreasingProfileTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every column's left wall is shorter than itself, so min(left, right) never exceeds height.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"4 5", "4 3 2 1 0"})
  void strictlyDecreasingProfileTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Mirror of the increasing case: the right wall is always the shorter side and too low.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3 4", "3 3 3 3"})
  void flatProfileOfEqualColumnsTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // No column dips below its neighbors, so there is nowhere for water to sit.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"3 4", "0 0 0 0"})
  void allZeroProfileTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Empty world: no blocks, no walls, nothing to hold water against. Total 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Single basins: the water rises to the lower rim across the whole gap. ---

  @Test
  @StdIo({"5 3", "5 0 5"})
  void deepNarrowBasinFillsToTheRim(StdOut out) throws IOException {
    Main.main(new String[0]);
    // min(5, 5) - 0 = 5 over the one interior column.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"3 5", "3 0 0 0 3"})
  void wideFlatBottomedBasinFillsEveryInteriorColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Three interior zeros, each capped at min(3, 3) = 3: 3 * 3 = 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  @Test
  @StdIo({"5 5", "5 2 0 2 5"})
  void steppedSymmetricBasinFillsToTheOuterRim(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both rims are 5, so the inner ledges (2) and floor (0) fill to 5: 3 + 5 + 3 = 11.
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Asymmetric walls: the defining trap -- water is capped by the SHORTER wall. ---

  @Test
  @StdIo({"5 3", "5 0 3"})
  void tallerLeftWallIsCappedByTheShorterRightWall(StdOut out) throws IOException {
    Main.main(new String[0]);
    // min(5, 3) - 0 = 3, NOT 5. Reading only the left wall would wrongly give 5.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"5 3", "3 0 5"})
  void tallerRightWallIsCappedByTheShorterLeftWall(StdOut out) throws IOException {
    Main.main(new String[0]);
    // min(3, 5) - 0 = 3, NOT 5. Reading only the right wall would wrongly give 5.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Interior peaks: the max(0, ...) clamp -- a column above its walls contributes nothing. ---

  @Test
  @StdIo({"5 3", "2 5 2"})
  void centralPeakTallerThanItsWallsTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // min(2, 2) - 5 = -3, clamped to 0. A missing clamp would emit a negative total.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"5 5", "4 2 5 1 3"})
  void peakBetweenTwoBasinsContributesZeroWhileItsNeighborsFill(StdOut out) throws IOException {
    Main.main(new String[0]);
    // col 1: min(4,5)-2=2; col 2 (the peak 5): min(4,3)-5 -> 0; col 3: min(5,3)-1=2. Total 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Multiple basins and irregular staircases. ---

  @Test
  @StdIo({"3 5", "3 0 3 0 3"})
  void twoBasinsSeparatedByADividerAreSummedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Left basin min(3,3)-0=3; the divider (3) holds nothing; right basin 3. Total 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"5 6", "4 1 2 1 3 5"})
  void irregularRisingStaircaseFillsEachDipToTheRunningRim(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Left rim 4 caps cols 1..4: (4-1)+(4-2)+(4-1)+(4-3) = 3+2+3+1 = 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- Plateau walls: runs of equal-height wall columns must not confuse the scan. ---

  @Test
  @StdIo({"5 6", "5 5 0 0 5 5"})
  void basinBetweenPlateauWallsFillsTheFlatFloor(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The two interior zeros fill to min(5,5)=5: 5 + 5 = 10; the doubled rim columns add nothing.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Value bounds: heights pinned to the ends of [0, H], and the smallest worlds. ---

  @Test
  @StdIo({"4 3", "4 0 4"})
  void wallsAtTheMaximumHeightFillTheBasinToTheTop(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Heights equal to H exactly: min(4, 4) - 0 = 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"3 5", "0 3 0 3 0"})
  void leadingAndTrailingZeroColumnsFormNoWallsSoOnlyTheMiddleFills(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The outer zeros are not walls; only the middle dip between the two 3s fills: 3. Total 3.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"1 5", "1 0 1 0 1"})
  void minimumHeightWorldStillTrapsBetweenUnitWalls(StdOut out) throws IOException {
    Main.main(new String[0]);
    // H = 1, heights restricted to {0, 1}: two unit-deep dips each fill by 1. Total 2.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"1 1", "1"})
  void smallestPossibleWorldTrapsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    // H = W = 1: a single unit block in a one-wide world holds no water. Total 0.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Upper bounds and fuzzing (generated input, driven through the stdin/stdout helper like the
  // project's other larger-input tests, with an independent O(W^2) oracle). ---

  @Test
  void fullWidthTankHoldsTheLargestPossibleVolume() throws IOException {
    int w = 500;
    int[] heights = new int[w]; // all interior columns are 0...
    heights[0] = 500;
    heights[w - 1] = 500; // ...between two maximum-height rims.
    // Heaviest legal answer: (W - 2) * H = 498 * 500 = 249,000, still within a signed 32-bit int.
    assertThat(runMain(buildInput(500, heights))).isEqualTo("249000");
  }

  @Test
  void fullWidthFlatProfileAtMaximumHeightTrapsNothing() throws IOException {
    int w = 500;
    int[] heights = new int[w];
    java.util.Arrays.fill(heights, 500);
    // Every column is the maximum height: a flat top holds no water even at full scale.
    assertThat(runMain(buildInput(500, heights))).isEqualTo("0");
  }

  @Test
  void fullWidthIncreasingProfileTrapsNothing() throws IOException {
    int w = 500;
    int[] heights = new int[w];
    for (int i = 0; i < w; i++) {
      heights[i] = i + 1; // 1..500, each <= H = 500.
    }
    // A long open ramp: the prefix maximum is always the column itself, so nothing collects.
    assertThat(runMain(buildInput(500, heights))).isEqualTo("0");
  }

  @Test
  void fullWidthDecreasingProfileTrapsNothing() throws IOException {
    int w = 500;
    int[] heights = new int[w];
    for (int i = 0; i < w; i++) {
      heights[i] = w - i; // 500..1, each <= H = 500.
    }
    // The mirror ramp; catches a suffix-maximum that is seeded or scanned incorrectly at scale.
    assertThat(runMain(buildInput(500, heights))).isEqualTo("0");
  }

  @Test
  void fullWidthSawtoothMatchesTheNaiveOracle() throws IOException {
    int w = 500;
    int[] heights = new int[w];
    for (int i = 0; i < w; i++) {
      heights[i] = (i % 2 == 0) ? 500 : 0; // alternating max wall / empty trough.
    }
    assertThat(runMain(buildInput(500, heights)))
        .isEqualTo(Long.toString(naiveTrappedWater(heights)));
  }

  @Test
  void largePseudoRandomProfileMatchesTheNaiveOracle() throws IOException {
    int h = 500;
    int w = 500;
    int[] heights = new int[w];
    Random rng = new Random(14719L); // fixed seed -> deterministic, reproducible across JVMs.
    for (int i = 0; i < w; i++) {
      heights[i] = rng.nextInt(h + 1); // uniform in [0, H].
    }
    // Irregular walls at full scale: the linear-scan answer must match the brute-force oracle.
    assertThat(runMain(buildInput(h, heights)))
        .isEqualTo(Long.toString(naiveTrappedWater(heights)));
  }

  /**
   * Builds BOJ 14719 input: an {@code "H W"} header line followed by the space-separated heights.
   */
  private static String buildInput(int h, int[] heights) {
    StringBuilder sb = new StringBuilder();
    sb.append(h).append(' ').append(heights.length).append('\n');
    for (int i = 0; i < heights.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(heights[i]);
    }
    return sb.append('\n').toString();
  }

  /**
   * Independent {@code O(W^2)} oracle: for each column the trapped water is {@code min(maxLeft,
   * maxRight) - height}, where both maxima include the column itself (so the outer columns
   * naturally contribute zero) and every term is non-negative.
   */
  private static long naiveTrappedWater(int[] heights) {
    int w = heights.length;
    long total = 0;
    for (int i = 0; i < w; i++) {
      int maxLeft = 0;
      for (int j = 0; j <= i; j++) {
        maxLeft = Math.max(maxLeft, heights[j]);
      }
      int maxRight = 0;
      for (int j = i; j < w; j++) {
        maxRight = Math.max(maxRight, heights[j]);
      }
      total += Math.min(maxLeft, maxRight) - heights[i];
    }
    return total;
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
