package boj.boj1749;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1749 점수따먹기 (Score Gobbling) -- the maximum-sum submatrix problem, i.e. the 2D generalization
 * of Kadane's algorithm.
 *
 * <p>An {@code N x M} matrix is given; each cell holds an integer in {@code [-10000, 10000]}. The
 * answer is the largest sum achievable by any single <em>non-empty</em> axis-aligned rectangular
 * submatrix (a contiguous block of rows crossed with a contiguous block of columns). Exactly one
 * integer is printed.
 *
 * <p>Three properties drive the edge cases below:
 *
 * <ul>
 *   <li><b>The all-negative trap.</b> When every cell is negative, the best rectangle is the single
 *       least-negative cell. Any solution that seeds its running maximum with {@code 0} (or treats
 *       the empty rectangle as valid) returns {@code 0} here and is wrong. This is the single most
 *       important case in the suite.
 *   <li><b>Contiguity.</b> A rectangle cannot skip a bad interior row or column; it can only avoid
 *       one by ending before it. So a strongly negative middle column forces the answer to lie
 *       entirely on one side of it.
 *   <li><b>Transpose invariance.</b> A matrix and its transpose have the <em>same</em> maximum
 *       submatrix sum (every {@code a x b} rectangle maps to a {@code b x a} rectangle over the
 *       same elements). Hence the output value alone cannot detect a row/column swap inside the
 *       search -- only a swap of {@code N} and {@code M} while <em>reading</em> a non-square input,
 *       which desyncs parsing, changes the result. The wide/tall pair below pins that down.
 * </ul>
 *
 * <p>Constraints (triangulated while acmicpc.net was unreachable; the clearest mirror states
 * {@code 1 < N, M < 200} while reference solutions size arrays {@code [201][201]}): each dimension
 * is at most 199 under the strict reading, so a {@code 2 x 2} matrix is the smallest input valid
 * under either reading and {@code 199} is the largest dimension valid under either reading. The
 * largest attainable sum, {@code 199 * 199 * 10000 = 396,010,000}, still fits a signed 32-bit
 * {@code int} (limit {@code 2,147,483,647}); so does the most negative, {@code -396,010,000}. The
 * single-cell / single-row / single-column fixtures sit at or below the strict lower bound and are
 * marked as degenerate-dimension robustness checks: they are valid input under the inclusive
 * reading and exercise the 1D reduction of the algorithm.
 *
 * <p>Input format: the first line is {@code "N M"}; the next {@code N} lines each hold {@code M}
 * space-separated integers. Output: a single line with the maximum submatrix sum.
 *
 * <p>The tests are ordered as an iterative TDD ladder -- whole-matrix selection, the all-negative
 * trap, row/column exclusion, negative-bridging, interior rectangles, a classic anchor example,
 * non-square parsing, value extremes, degenerate dimensions, then large oracle-checked inputs --
 * each expected value independently confirmed by a brute-force enumeration of every rectangle.
 */
class MainTest {

  // --- Whole-matrix selection: the simplest behavior. Every cell positive, so no proper
  // sub-rectangle can beat taking all of it. ---

  @Test
  @StdIo({"2 2", "1 2", "3 4"})
  void allPositiveTwoByTwoSelectsTheWholeMatrix(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 1 + 2 + 3 + 4 = 10; any smaller block sums to less.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"3 3", "2 2 2", "2 2 2", "2 2 2"})
  void everyCellPositiveSelectsTheEntireMatrix(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 9 cells * 2 = 18.
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // --- The all-negative trap: the answer is the single least-negative cell, never 0. ---

  @Test
  @StdIo({"2 2", "-1 -2", "-3 -4"})
  void allNegativeMatrixSelectsTheSingleLeastNegativeCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The best non-empty rectangle is the lone cell -1. Seeding the max at 0 would wrongly print 0.
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo({"2 2", "-5 -5", "-5 3"})
  void lonePositiveCellIsSelectedWhenItsNeighborsAreNegative(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Any rectangle larger than the single cell 3 drags in a -5; 3 stands alone.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"2 2", "10 10", "10 -100"})
  void aSingleLargeNegativeIsExcludedFromTheBestRectangle(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The three 10s form an L, not a rectangle; the best rectangle is two 10s (a 1x2 or 2x1) = 20.
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- Zero: a non-empty rectangle is mandatory, so an all-zero matrix answers 0 (not "no
  // rectangle"). ---

  @Test
  @StdIo({"2 2", "0 0", "0 0"})
  void allZeroMatrixYieldsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Excluding a bad band by ending before it (not by skipping it). ---

  @Test
  @StdIo({"3 3", "1 2 3", "4 5 6", "-9 -9 -9"})
  void aBadBottomRowIsExcluded(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Top 2x3 block = 1+2+3+4+5+6 = 21; adding the -9 row only subtracts.
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  @Test
  @StdIo({"3 3", "1 -9 3", "4 -9 6", "2 -9 5"})
  void aBadMiddleColumnIsExcludedAndCannotBeSkipped(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Columns are contiguous, so the left column (7) and right column (14) cannot be combined
    // without the -9 middle. The best single contiguous column range is the right column = 3+6+5 =
    // 14.
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // --- Bridging an interior negative: keep accumulating across a negative when flanked by larger
  // positives (the heart of Kadane). ---

  @Test
  @StdIo({"2 3", "3 -1 3", "-9 -9 -9"})
  void aSingleRowSubmatrixBridgesAnInteriorNegative(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best is the whole top row 3 + (-1) + 3 = 5, which beats either lone 3; the bottom row is
    // shed.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5 2", "-9 -9", "5 5", "-1 -1", "5 5", "-9 -9"})
  void aVerticalBandBridgesAnInteriorNegativeRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 2-4 over both columns: (5+5) + (-1-1) + (5+5) = 18; spanning the -1,-1 row is worth it,
    // and the -9 border rows are shed.
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // --- Interior rectangles: all four inclusion-exclusion prefix terms are non-zero. ---

  @Test
  @StdIo({"4 4", "-9 -9 -9 -9", "-9 5 6 -9", "-9 7 8 -9", "-9 -9 -9 -9"})
  void anInteriorRectangleIsFoundInsideANegativeBorder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The center 2x2 = 5+6+7+8 = 26; extending in any direction hits the -9 border.
    assertThat(out.capturedString().trim()).isEqualTo("26");
  }

  @Test
  @StdIo({"4 4", "-1 -1 -1 -1", "-1 3 -2 -1", "-1 4 5 -1", "-1 -1 -1 -1"})
  void anInteriorRectangleBridgesItsOwnInteriorNegative(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The center 2x2 spans a -2: 3 + (-2) + 4 + 5 = 10, still better than any sub-part or
    // extension.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- A classic, independently documented example (POJ 1050 "To the Max"): the lower-left 3x2
  // block {9,2 / -4,1 / -1,8} sums to 15. ---

  @Test
  @StdIo({"4 4", "0 -2 -7 0", "9 2 -6 2", "-4 1 -4 1", "-1 8 0 -2"})
  void classicMaximumSubrectangleExampleSumsToFifteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // --- Non-square inputs: a wide matrix and its tall transpose share an answer (transpose
  // invariance), yet reading "N M" swapped would mis-parse one of them. ---

  @Test
  @StdIo({"2 5", "1 2 3 -10 4", "-1 -1 -1 -1 4"})
  void wideNonSquareMatrixIsReadRowsThenColumns(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best is the right column over both rows: 4 + 4 = 8 (beats the top run 1+2+3 = 6).
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  @Test
  @StdIo({"5 2", "1 -1", "2 -1", "3 -1", "-10 -1", "4 4"})
  void tallNonSquareMatrixMatchesItsWideTranspose(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The transpose of the previous fixture; the maximum submatrix sum is identical: 8.
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Per-cell value extremes: the stated bounds are [-10000, 10000]. ---

  @Test
  @StdIo({"2 2", "10000 10000", "10000 10000"})
  void everyCellAtMaximumValueSumsToTheWholeMatrix(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 4 * 10000 = 40000.
    assertThat(out.capturedString().trim()).isEqualTo("40000");
  }

  @Test
  @StdIo({"2 2", "-10000 -10000", "-10000 -10000"})
  void everyCellAtMinimumValueSelectsASingleCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    // All negative -> the least-negative single cell, -10000.
    assertThat(out.capturedString().trim()).isEqualTo("-10000");
  }

  // --- Degenerate dimensions (robustness): at or below the strict 1 < N, M lower bound, valid
  // under
  // the inclusive reading. Each reduces the 2D search to one-dimensional Kadane. ---

  @Test
  @StdIo({"1 1", "7"})
  void singleCellMatrixReturnsThatCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"1 5", "-1 3 -2 5 -4"})
  void singleRowMatrixReducesToOneDimensionalKadane(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Best contiguous run is 3 + (-2) + 5 = 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"5 1", "-1", "3", "-2", "5", "-4"})
  void singleColumnMatrixReducesToOneDimensionalKadane(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The column transpose of the single-row case; same answer: 6.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Large inputs, driven through the stdin/stdout helper like the project's other
  // generated-input
  // tests, with expected values from an independent brute-force oracle (or a closed form). ---

  @Test
  void largeMaximumMatrixSelectsTheWholeMatrixWithoutIntegerOverflow() throws IOException {
    int n = 199; // largest dimension valid under either reading of the constraint.
    int[][] grid = new int[n][n];
    for (int[] row : grid) {
      Arrays.fill(row, 10000);
    }
    // All positive -> the whole matrix; 199 * 199 * 10000 = 396,010,000, still within a signed int.
    long expected = (long) n * n * 10000;
    assertThat(runMain(buildInput(grid))).isEqualTo(Long.toString(expected));
  }

  @Test
  void largeAllNegativeMatrixSelectsASingleCell() throws IOException {
    int n = 100;
    int[][] grid = new int[n][n];
    for (int[] row : grid) {
      Arrays.fill(row, -10000);
    }
    // The all-negative trap at scale: the answer is one least-negative cell, not the (-100,000,000)
    // whole-matrix sum and not 0.
    assertThat(runMain(buildInput(grid))).isEqualTo("-10000");
  }

  @Test
  void largeMixedMatrixMatchesTheBruteForceOracle() throws IOException {
    int n = 53; // asymmetric, prime dimensions so a wrong N/M read would mis-parse.
    int m = 47;
    int[][] grid = new int[n][m];
    for (int r = 1; r <= n; r++) {
      for (int c = 1; c <= m; c++) {
        // Deterministic, asymmetric in (row, column), and within [-10000, 10000].
        grid[r - 1][c - 1] = ((r * 37 + c * 17) % 20001) - 10000;
      }
    }
    long expected = maxSubmatrixSum(grid);
    assertThat(runMain(buildInput(grid))).isEqualTo(Long.toString(expected));
  }

  /**
   * Builds BOJ 1749 input: a {@code "N M"} header line followed by {@code N} rows of {@code M}
   * space-separated integers.
   */
  private static String buildInput(int[][] grid) {
    int m = grid[0].length;
    StringBuilder sb = new StringBuilder();
    sb.append(grid.length).append(' ').append(m).append('\n');
    for (int[] row : grid) {
      for (int c = 0; c < m; c++) {
        if (c > 0) {
          sb.append(' ');
        }
        sb.append(row[c]);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /**
   * Independent oracle: the maximum sum over every non-empty rectangular submatrix, computed by
   * enumerating all rectangles against a 2D prefix-sum table.
   *
   * @implNote Brute force in {@code O(N^2 * M^2)} time, where {@code N} and {@code M} are the row
   *     and column counts of {@code grid}; sums accumulate in {@code long} so the oracle itself
   *     never overflows regardless of input size.
   */
  private static long maxSubmatrixSum(int[][] grid) {
    int n = grid.length;
    int m = grid[0].length;
    long[][] prefix = new long[n + 1][m + 1];
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= m; j++) {
        prefix[i][j] =
            grid[i - 1][j - 1] + prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1];
      }
    }
    long best = Long.MIN_VALUE;
    for (int r1 = 1; r1 <= n; r1++) {
      for (int r2 = r1; r2 <= n; r2++) {
        for (int c1 = 1; c1 <= m; c1++) {
          for (int c2 = c1; c2 <= m; c2++) {
            long sum =
                prefix[r2][c2] - prefix[r1 - 1][c2] - prefix[r2][c1 - 1] + prefix[r1 - 1][c1 - 1];
            if (sum > best) {
              best = sum;
            }
          }
        }
      }
    }
    return best;
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
