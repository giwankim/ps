package boj.boj11660;

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
 * BOJ 11660 구간 합 구하기 5 (Range Sum Query 5) -- the 2D sibling of 11659.
 *
 * <p>An {@code N x N} table is given, followed by {@code M} queries. Each query is four integers
 * {@code x1 y1 x2 y2} and asks for the sum of the rectangular block from {@code (x1, y1)} to
 * {@code (x2, y2)}, inclusive. Critically, {@code (x, y)} denotes <em>row x, column y</em>.
 *
 * <p>A 2D prefix-sum table {@code P} with {@code P[i][j]} the sum of the block from {@code (1, 1)}
 * to {@code (i, j)} answers every query in {@code O(1)} via inclusion-exclusion:
 *
 * <pre>{@code P[x2][y2] - P[x1-1][y2] - P[x2][y1-1] + P[x1-1][y1-1]}</pre>
 *
 * The {@code x1 = 1} and {@code y1 = 1} cases rely on the zero border {@code P[0][*]} and
 * {@code P[*][0]}; mishandling them is the off-by-one core of this problem.
 *
 * <p>Constraints: {@code 1 <= N <= 1024}; {@code 1 <= M <= 100,000}; each table value is a natural
 * number in {@code [1, 1000]}; {@code x1 <= x2} and {@code y1 <= y2}. The largest possible answer,
 * {@code 1024 * 1024 * 1000 = 1,048,576,000}, still fits a signed 32-bit {@code int} (the limit is
 * {@code 2,147,483,647}).
 *
 * <p>Output is {@code M} lines: the query answers in order.
 *
 * <p>Note on fixtures: the official sample table is symmetric ({@code value = row + col - 1}), so
 * it cannot detect a row/column swap. The asymmetric {@code 1..9} table below (where {@code value =
 * (row - 1) * 3 + col}) is used to pin down the {@code (x, y) = (row, column)} convention.
 */
class MainTest {

  // --- Official sample from the statement. ---

  @Test
  @StdIo({"4 3", "1 2 3 4", "2 3 4 5", "3 4 5 6", "4 5 6 7", "2 2 3 4", "3 4 3 4", "1 1 4 4"})
  void officialSampleComputesEachRectangleSumInQueryOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // (2,2)-(3,4): rows 2-3, cols 2-4 = (3+4+5)+(4+5+6) = 27.
    // (3,4)-(3,4): single cell row 3 col 4 = 6.
    // (1,1)-(4,4): whole table = 10+14+18+22 = 64.
    assertThat(out.capturedString().trim()).isEqualTo("27\n6\n64");
  }

  // --- Smallest possible input: N = 1, a single cell that is also the only query. ---

  @Test
  @StdIo({"1 1", "7", "1 1 1 1"})
  void singleCellTableAnswersItsOnlyQuery(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Single-cell queries on the asymmetric 1..9 table: each prefix-border combination. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 1 1"})
  void topLeftSingleCellReadsBothPrefixBorders(StdOut out) throws IOException {
    Main.main(new String[0]);
    // x1 = y1 = 1: only P[1][1] survives; P[0][*] and P[*][0] must be 0, never indexed at -1.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "3 3 3 3"})
  void bottomRightSingleCellReturnsTheFinalElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // (N,N): P[3][3] - P[2][3] - P[3][2] + P[2][2] = 9.
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "2 2 2 2"})
  void interiorSingleCellReturnsThatElement(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The center cell, value 5, with all four prefix terms non-zero.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Whole-table query: only the P[N][N] term remains. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 3 3"})
  void wholeTableQuerySumsEveryCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 1+2+...+9 = 45.
    assertThat(out.capturedString().trim()).isEqualTo("45");
  }

  // --- Single-row strips: x1 == x2. On the asymmetric table these differ from the column
  // strips below, so each catches a row/column transposition bug. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 1 3"})
  void topRowStripSumsTheFirstRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Row 1 = 1+2+3 = 6. A row/column swap would read column 1 = 1+4+7 = 12.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "2 1 2 3"})
  void middleRowStripSumsTheSecondRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Row 2 = 4+5+6 = 15.
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "3 1 3 3"})
  void bottomRowStripSumsTheLastRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Row 3 = 7+8+9 = 24.
    assertThat(out.capturedString().trim()).isEqualTo("24");
  }

  // --- Single-column strips: y1 == y2. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 3 1"})
  void leftColumnStripSumsTheFirstColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Column 1 = 1+4+7 = 12. A row/column swap would read row 1 = 1+2+3 = 6.
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 3 3 3"})
  void rightColumnStripSumsTheLastColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Column 3 = 3+6+9 = 18.
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // --- Prefix-border coverage: each of the four inclusion-exclusion terms exercised in turn. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "2 2 3 3"})
  void interiorRectangleUsesAllFourPrefixTerms(StdOut out) throws IOException {
    Main.main(new String[0]);
    // x1>1 and y1>1: P[3][3]-P[1][3]-P[3][1]+P[1][1]. Cells 5,6,8,9 = 28.
    assertThat(out.capturedString().trim()).isEqualTo("28");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 2 3 3"})
  void startRowOneZeroesTheTopPrefixTerms(StdOut out) throws IOException {
    Main.main(new String[0]);
    // x1 = 1 so P[x1-1][*] = P[0][*] = 0; columns 2-3 over all rows = (2+3)+(5+6)+(8+9) = 33.
    assertThat(out.capturedString().trim()).isEqualTo("33");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "2 1 3 3"})
  void startColumnOneZeroesTheLeftPrefixTerms(StdOut out) throws IOException {
    Main.main(new String[0]);
    // y1 = 1 so P[*][y1-1] = P[*][0] = 0; rows 2-3 over all columns = (4+5+6)+(7+8+9) = 39.
    assertThat(out.capturedString().trim()).isEqualTo("39");
  }

  // --- Asymmetric rectangles: a wide query and its transpose give different sums, so swapping
  // the row span with the column span is caught. ---

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 2 3"})
  void wideRectangleSpansTwoRowsAndThreeColumns(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1-2, cols 1-3 = (1+2+3)+(4+5+6) = 21. Its transpose (1,1,3,2) sums to 27.
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  @Test
  @StdIo({"3 1", "1 2 3", "4 5 6", "7 8 9", "1 1 3 2"})
  void tallRectangleSpansThreeRowsAndTwoColumns(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1-3, cols 1-2 = (1+2)+(4+5)+(7+8) = 27. Its transpose (1,1,2,3) sums to 21.
    assertThat(out.capturedString().trim()).isEqualTo("27");
  }

  // --- Interior rectangle touching no border, on the 4x4 sample table. ---

  @Test
  @StdIo({"4 1", "1 2 3 4", "2 3 4 5", "3 4 5 6", "4 5 6 7", "2 2 3 3"})
  void interiorRectangleTouchingNoBorderIsComputedByInclusionExclusion(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Rows 2-3, cols 2-3 of [value = row+col-1] = 3+4+4+5 = 16.
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- Value bounds: each cell is a natural number in [1, 1000]. ---

  @Test
  @StdIo({"3 1", "1 1 1", "1 1 1", "1 1 1", "2 2 3 3"})
  void allMinimumValuesMakeTheSumEqualTheRectangleArea(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every cell 1, so any sum equals its cell count: a 2x2 block = 4.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"2 1", "1000 1000", "1000 1000", "1 1 2 2"})
  void allMaximumValuesSumAtTheTopOfTheRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    // 4 cells * 1000 = 4000.
    assertThat(out.capturedString().trim()).isEqualTo("4000");
  }

  // --- Multiple queries: order preserved, each answered independently of the others. ---

  @Test
  @StdIo({"3 4", "1 2 3", "4 5 6", "7 8 9", "1 1 1 1", "3 3 3 3", "1 1 3 3", "2 2 2 2"})
  void multipleQueriesAreAnsweredInTheGivenOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Corner 1; corner 9; whole table 45; center 5.
    assertThat(out.capturedString().trim()).isEqualTo("1\n9\n45\n5");
  }

  @Test
  @StdIo({"3 3", "1 2 3", "4 5 6", "7 8 9", "1 1 2 2", "1 1 2 2", "1 1 2 2"})
  void repeatedIdenticalQueriesEachReturnTheSameSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Queries carry no state: rows 1-2, cols 1-2 = 1+2+4+5 = 12 every time.
    assertThat(out.capturedString().trim()).isEqualTo("12\n12\n12");
  }

  // --- Upper bounds (generated input, driven through the stdin/stdout helper like the project's
  // other larger-input tests). ---

  @Test
  void maximumTableOfAllThousandsSumsWithoutIntOverflow() throws IOException {
    int n = 1024;
    int[][] matrix = new int[n][n];
    for (int[] row : matrix) {
      Arrays.fill(row, 1000);
    }
    // Largest possible answer: 1024 * 1024 * 1000 = 1,048,576,000, still within a signed 32-bit
    // int.
    int[][] queries = {{1, 1, n, n}};
    assertThat(runMain(buildInput(matrix, queries))).isEqualTo("1048576000");
  }

  @Test
  void everyQueryIsConsumedInOrderAcrossOneHundredThousandQueries() throws IOException {
    int n = 10;
    int[][] matrix = new int[n][n];
    for (int[] row : matrix) {
      Arrays.fill(row, 1); // all ones, so each block sum equals its area.
    }
    // M at its maximum, cycling through five rectangles whose answers form a recognizable pattern.
    int[][] cycle = {{1, 1, 1, 1}, {1, 1, 1, n}, {1, 1, n, 1}, {1, 1, n, n}, {2, 3, 5, 7}};
    int m = 100_000;
    int[][] queries = new int[m][];
    StringBuilder expected = new StringBuilder();
    for (int k = 0; k < m; k++) {
      int[] q = cycle[k % cycle.length];
      queries[k] = q;
      if (k > 0) {
        expected.append('\n');
      }
      expected.append(naiveRangeSum(matrix, q[0], q[1], q[2], q[3]));
    }
    assertThat(runMain(buildInput(matrix, queries))).isEqualTo(expected.toString());
  }

  @Test
  void assortedQueriesOnALargeAsymmetricTableMatchTheNaiveOracle() throws IOException {
    int n = 1024;
    int[][] matrix = new int[n][n];
    for (int r = 1; r <= n; r++) {
      for (int c = 1; c <= n; c++) {
        // Asymmetric in (row, column) yet always within [1, 1000], so a transposition still shows.
        matrix[r - 1][c - 1] = ((r * 31 + c * 7) % 1000) + 1;
      }
    }
    int[][] queries = {
      {1, 1, 1, 1},
      {n, n, n, n},
      {1, 1, n, n},
      {1, 1, 1, n}, // full top row
      {1, 1, n, 1}, // full left column
      {12, 3, 1000, 777}, // large off-center asymmetric block
      {200, 500, 800, 900}
    };
    StringBuilder expected = new StringBuilder();
    for (int q = 0; q < queries.length; q++) {
      if (q > 0) {
        expected.append('\n');
      }
      int[] query = queries[q];
      expected.append(naiveRangeSum(matrix, query[0], query[1], query[2], query[3]));
    }
    assertThat(runMain(buildInput(matrix, queries))).isEqualTo(expected.toString());
  }

  /**
   * Builds BOJ 11660 input: {@code "N M"} header, {@code N} table rows, then one line per query as
   * {@code "x1 y1 x2 y2"}. {@code matrix} must be square.
   */
  private static String buildInput(int[][] matrix, int[][] queries) {
    int n = matrix.length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(queries.length).append('\n');
    for (int[] row : matrix) {
      for (int c = 0; c < n; c++) {
        if (c > 0) {
          sb.append(' ');
        }
        sb.append(row[c]);
      }
      sb.append('\n');
    }
    for (int[] query : queries) {
      sb.append(query[0])
          .append(' ')
          .append(query[1])
          .append(' ')
          .append(query[2])
          .append(' ')
          .append(query[3])
          .append('\n');
    }
    return sb.toString();
  }

  /**
   * Independent {@code O((x2 - x1) * (y2 - y1))} oracle for the 2D prefix-sum implementation. All
   * of {@code x1, y1, x2, y2} are 1-indexed and inclusive, with {@code x} the row and {@code y} the
   * column.
   */
  private static long naiveRangeSum(int[][] matrix, int x1, int y1, int x2, int y2) {
    long sum = 0;
    for (int r = x1; r <= x2; r++) {
      for (int c = y1; c <= y2; c++) {
        sum += matrix[r - 1][c - 1];
      }
    }
    return sum;
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
