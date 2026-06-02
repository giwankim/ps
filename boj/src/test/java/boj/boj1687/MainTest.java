package boj.boj1687;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1687 행렬 찾기 (Find a Matrix) -- the maximal all-<em>zero</em> rectangle problem.
 *
 * <p>Given an {@code N x M} matrix of 0s and 1s, find the axis-aligned rectangular submatrix that
 * consists entirely of 0s and has the greatest <em>area</em>, and print that area.
 *
 * <p>Constraints (triangulated from blog mirrors while acmicpc.net was unreachable -- the statement
 * text agrees across mirrors): {@code 1 <= N, M <= 333}. Every element is {@code 0} or {@code 1}.
 * Because {@code N, M <= 333}, the maximum possible answer is {@code 333 * 333 = 110,889}, which
 * fits a signed 32-bit {@code int} comfortably -- so, unlike many area problems, there is no
 * overflow trap here; the trap is purely algorithmic.
 *
 * <p><b>Why area, not dimensions.</b> The objective is the cell count of the rectangle, so a
 * {@code 3 x 2} block (area 6) must beat a full {@code 1 x 4} row (area 4) and a {@code 4 x 1}
 * column (area 4). The canonical O(N*M) solution treats each column's run of consecutive 0s ending
 * at the current row as a histogram bar and runs <em>largest-rectangle-in-a-histogram</em> per row;
 * an O(N*M^2) column-pair-plus-prefix-sum scan also passes at this size. A naive solution that only
 * measures the longest 0-run within a single row, or the tallest 0-run within a single column, gets
 * the area wrong -- {@link #interiorBlockBeatsAFullRowOnArea(StdOut)} and
 * {@link #tallThinColumnBeatsAShortWiderRow(StdOut)} pin those two failure modes down. Critically,
 * a run of 0s must <em>reset</em> when a 1 interrupts the column
 * ({@link #blockingOneRowPreventsVerticalStacking(StdOut)}) yet must <em>stack</em> across
 * uninterrupted 0-rows ({@link #allZerosRectangularMatrixCoversEveryCell(StdOut)}).
 */
class MainTest {

  // --- Base cases: a 1x1 matrix is either a single 0 (area 1) or a single 1 (no rectangle). ---

  @Test
  @StdIo({"1 1", "0"})
  void singleZeroCellHasAreaOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1 1", "1"})
  void singleOneCellHasNoZeroRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // A matrix with no 0 anywhere yields area 0 -- the empty-answer path.
  @Test
  @StdIo({"2 2", "11", "11"})
  void allOnesMatrixHasNoZeroRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- An all-zero rectangular (N != M) matrix: every cell is in the answer, and 0-rows stack. ---

  // 2 rows x 3 cols of 0s: the whole 2x3 block is the answer (area 6), and adjacent 0-rows stack.
  @Test
  @StdIo({"2 3", "000", "000"})
  void allZerosRectangularMatrixCoversEveryCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Degenerate shapes reduce to a longest run of 0s. ---

  // One row "00100": the 0-runs are length 2 and 2, so the widest all-zero rectangle is 1x2 = 2.
  @Test
  @StdIo({"1 5", "00100"})
  void singleRowAnswerIsLongestZeroRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // One column 0,0,1,0: vertical 0-runs are length 2 then 1, so the tallest rectangle is 2x1 = 2.
  @Test
  @StdIo({"4 1", "0", "0", "1", "0"})
  void singleColumnAnswerIsLongestZeroRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- A lone interior 0 surrounded by 1s still counts as a 1x1 rectangle (area 1), not 0. ---

  @Test
  @StdIo({"3 3", "111", "101", "111"})
  void isolatedInteriorZeroHasAreaOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A checkerboard has no two adjacent 0s, so no rectangle exceeds a single cell -> area 1.
  @Test
  @StdIo({"2 2", "01", "10"})
  void checkerboardHasNoZeroRectangleLargerThanOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Stack vs reset: an interrupting row of 1s must break the vertical run. ---

  // Rows 0 and 2 are all 0s but row 1 is all 1s. The 1-row forbids a 2x3 (area 6) rectangle; the
  // best all-zero rectangle is a single full row, 1x3 = 3. A solution that fails to reset the run
  // on the blocking row would wrongly report 6.
  @Test
  @StdIo({"3 3", "000", "111", "000"})
  void blockingOneRowPreventsVerticalStacking(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Area beats dimensions: the answer is neither the widest row nor uses the most rows. ---

  // A 4x1 all-zero column (area 4) beats the 1x3 all-zero bottom row (area 3); the only column with
  // 0s in rows 0..2 is column 0. A row-only scan would stop at 3.
  @Test
  @StdIo({"4 3", "011", "011", "011", "000"})
  void tallThinColumnBeatsAShortWiderRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // 3x4 grid whose largest all-zero rectangle is the interior 3x2 block at columns 0-1 (area 6) --
  // touching neither the widest fully-zero row (row 1, area 4) nor a tall single column (height 3,
  // area 3). Only a true rectangle search finds 6; row-only -> 4, column-only -> 3.
  @Test
  @StdIo({"3 4", "0010", "0000", "0001"})
  void interiorBlockBeatsAFullRowOnArea(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Externally verified vector: LeetCode 85's published example, bit-inverted (0<->1). ---

  // LeetCode 85's sample matrix has a maximal all-ONES rectangle of area 6. Inverting every bit
  // turns that into BOJ 1687's maximal all-ZEROS rectangle of the same area 6, giving an answer
  // confirmed by an independent, published source rather than only by hand.
  @Test
  @StdIo({"4 5", "01011", "01000", "00000", "01101"})
  void matchesLeetCodeMaximalRectangleVectorBitInverted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Maximum-size inputs: O(N*M) or O(N*M^2) finishes; a brute-force O(N^2*M^2) scan times out.
  // ---

  // 333x333 all 0s: the whole matrix is the rectangle, area 333*333 = 110,889 (the maximum the
  // constraints allow), confirming the upper-bound answer and that the read path handles full size.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAllZeroMatrixAreaEqualsCellCount() throws IOException {
    int n = 333;
    int m = 333;
    assertThat(runMain(uniformMatrixInput(n, m, '0'))).isEqualTo(Integer.toString(n * m));
  }

  // 333x333 all 1s: no 0 exists, so the answer is 0 even at maximum size -- exercises the no-match
  // path at scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAllOneMatrixHasNoZeroRectangle() throws IOException {
    assertThat(runMain(uniformMatrixInput(333, 333, '1'))).isEqualTo("0");
  }

  // A 333x333 grid of 1s with a single 200x150 all-zero block carved into the interior. The answer
  // is exactly that block, 200*150 = 30,000 -- a large rectangle that is neither full-width nor
  // full-height, stressing the histogram/stacking logic (and timing) at scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeInteriorZeroBlockIsFoundAtScale() throws IOException {
    int n = 333;
    int m = 333;
    int[][] grid = new int[n][m];
    for (int[] row : grid) {
      Arrays.fill(row, 1);
    }
    int topInclusive = 50;
    int bottomExclusive = 250; // 200 rows
    int leftInclusive = 100;
    int rightExclusive = 250; // 150 columns
    for (int r = topInclusive; r < bottomExclusive; r++) {
      for (int c = leftInclusive; c < rightExclusive; c++) {
        grid[r][c] = 0;
      }
    }
    int expected = (bottomExclusive - topInclusive) * (rightExclusive - leftInclusive);
    assertThat(runMain(gridInput(grid))).isEqualTo(Integer.toString(expected));
  }

  // --- Randomised cross-check against an independent brute-force oracle on small grids. Small
  // dimensions and a 0-bias make multi-cell rectangles frequent, catching off-by-one edges and
  // area miscounts the hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(1687);
    for (int trial = 0; trial < 300; trial++) {
      int rows = 1 + rnd.nextInt(8); // 1..8
      int cols = 1 + rnd.nextInt(8); // 1..8
      int[][] grid = new int[rows][cols];
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          grid[r][c] = rnd.nextInt(3) == 0 ? 1 : 0; // ~1/3 ones, biased toward 0s for bigger blocks
        }
      }
      String expected = Integer.toString(bruteForceMaxZeroRectangle(grid));
      assertThat(runMain(gridInput(grid)))
          .as("grid=%s", Arrays.deepToString(grid))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the area of the largest all-zero rectangle, found by checking every
   * rectangle. Obviously correct, far too slow for the judge, trustworthy for tiny grids.
   *
   * @implNote Brute force in {@code O(N^2 * M^2 * (N * M))} time -- it enumerates every (top,
   *     bottom) row pair and (left, right) column pair, then scans the enclosed cells -- where
   *     {@code N} is the row count {@code grid.length} and {@code M} is the column count
   *     {@code grid[0].length}.
   */
  private static int bruteForceMaxZeroRectangle(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    int best = 0;
    for (int top = 0; top < rows; top++) {
      for (int left = 0; left < cols; left++) {
        for (int bottom = top; bottom < rows; bottom++) {
          for (int right = left; right < cols; right++) {
            if (isAllZero(grid, top, left, bottom, right)) {
              best = Math.max(best, (bottom - top + 1) * (right - left + 1));
            }
          }
        }
      }
    }
    return best;
  }

  /** True when every cell of the inclusive rectangle {@code [top..bottom] x [left..right]} is 0. */
  private static boolean isAllZero(int[][] grid, int top, int left, int bottom, int right) {
    for (int r = top; r <= bottom; r++) {
      for (int c = left; c <= right; c++) {
        if (grid[r][c] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Builds a BOJ 1687 input from a 0/1 grid: a {@code "N M"} line then {@code N} space-less rows.
   */
  private static String gridInput(int[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;
    StringBuilder sb = new StringBuilder().append(rows).append(' ').append(cols).append('\n');
    for (int[] row : grid) {
      for (int cell : row) {
        sb.append(cell);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /** Builds a {@code rows x cols} input whose every cell is {@code fill} ('0' or '1'). */
  private static String uniformMatrixInput(int rows, int cols, char fill) {
    char[] lineChars = new char[cols];
    Arrays.fill(lineChars, fill);
    String row = new String(lineChars);
    StringBuilder sb = new StringBuilder().append(rows).append(' ').append(cols).append('\n');
    for (int i = 0; i < rows; i++) {
      sb.append(row).append('\n');
    }
    return sb.toString();
  }

  private static String runMain(String input) throws IOException {
    return capture(input).trim();
  }

  private static String capture(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
