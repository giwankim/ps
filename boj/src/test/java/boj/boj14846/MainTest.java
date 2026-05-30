package boj.boj14846;

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
 * BOJ 14846 직사각형과 쿼리 (Rectangle and Queries) -- a 2D prefix-sum problem.
 *
 * <p>Given an {@code N x N} matrix whose every entry is a natural number in {@code [1, 10]}, each
 * query supplies an axis-aligned sub-rectangle by its top-left corner {@code (r1, c1)} and
 * bottom-right corner {@code (r2, c2)} (1-indexed, inclusive) and asks how many <em>distinct</em>
 * values appear inside it. Because only ten values are possible, the canonical solution keeps a
 * prefix-count {@code P[r][c][v]} -- the number of times value {@code v} occurs in the rectangle
 * {@code (1,1)..(r,c)} -- and answers each query in {@code O(10)} via 2D inclusion-exclusion:
 *
 * <pre>{@code
 * count(v) = P[r2][c2][v] - P[r1-1][c2][v] - P[r2][c1-1][v] + P[r1-1][c1-1][v];
 * }</pre>
 *
 * a value is "present" when that count is positive, and the answer is the number of present values
 * (so it always lies in {@code [1, 10]}: a non-empty rectangle has at least one value, never more
 * than ten).
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 300}); the next {@code N} lines hold the matrix,
 * {@code N} space-separated values per line, rows numbered top-to-bottom and columns left-to-right
 * starting at {@code (1, 1)}; then a line with the query count {@code Q}; then {@code Q} lines,
 * each {@code r1 c1 r2 c2} with the row coordinate first. Output: {@code Q} lines, one
 * distinct-value count per query, in input order.
 *
 * <p>The first query coordinate indexes the row dimension and the second the column dimension, so a
 * transpose bug is invisible on a symmetric matrix;
 * {@link #queryAcrossASingleRowCountsValuesInThatRowOnly} and
 * {@link #queryDownASingleColumnCountsValuesInThatColumnOnly} pin the orientation against a
 * deliberately asymmetric grid. Values can be the two-character token {@code "10"}, which
 * {@link #tenIsTreatedAsASingleValueDistinctFromOne} guards against a digit-by-digit parse.
 *
 * <p>Expected answers are hand-derived from the problem spec (recovered from independent accepted
 * solutions while acmicpc.net was unreachable) and, for the maximal grid, cross-checked against an
 * independent {@link #naiveDistinctCount naive oracle}.
 */
class MainTest {

  // --- Smallest possible input: N = 1, the single cell is the whole matrix and the only query. ---

  @Test
  @StdIo({"1", "5", "1", "1 1 1 1"})
  void singleCellMatrixAnswersItsOnlyQueryWithOneDistinctValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The lone cell holds 5; a one-cell rectangle always has exactly one distinct value.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Single-cell queries inside a larger grid: always one distinct value, since values >= 1. ---

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "2 2 2 2"})
  void singleCellQueryInLargerMatrixCountsExactlyOneDistinctValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Cell (2,2) = 6, on its own: one distinct value.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Two-digit value parsing: 10 is one token, distinct from 1. ---

  @Test
  @StdIo({"2", "1 10", "1 1", "1", "1 1 2 2"})
  void tenIsTreatedAsASingleValueDistinctFromOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Cells are {1, 10, 1, 1} -> {1, 10} = 2 distinct. A digit-by-digit parse that read "10" as a
    // 1 (and a stray 0) would instead collapse everything to {1} and report 1.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Whole-matrix query and the value-range extremes (1 distinct .. 10 distinct). ---

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "1 1 4 4"})
  void wholeMatrixQueryCountsEveryDistinctValuePresent(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The grid contains each of 1..10 at least once: the maximum possible answer, 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"3", "7 7 7", "7 7 7", "7 7 7", "1", "1 1 3 3"})
  void matrixOfAllIdenticalValuesHasExactlyOneDistinctValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Nine cells, all 7: distinct count is 1, not 9 -- it counts values, not occurrences.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Orientation: the first query coordinate is the row, the second is the column. The grid
  // below puts value r in every cell of row r, so a horizontal slice is uniform but a vertical
  // slice spans every value -- the two cases below would swap under a transpose bug. ---

  @Test
  @StdIo({"3", "1 1 1", "2 2 2", "3 3 3", "1", "1 1 1 3"})
  void queryAcrossASingleRowCountsValuesInThatRowOnly(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1..1, columns 1..3 -> all of row 1 -> {1} = 1 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3", "1 1 1", "2 2 2", "3 3 3", "1", "1 1 3 1"})
  void queryDownASingleColumnCountsValuesInThatColumnOnly(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1..3, columns 1..1 -> column 1 -> {1, 2, 3} = 3 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Sub-rectangles see only the values within their bounds. ---

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "1 1 2 2"})
  void subRectangleCountsOnlyTheValuesWithinItsBounds(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1..2, columns 1..2 -> {1, 2, 5, 6} = 4 distinct (8, 9, 10, ... are outside).
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "1 1 3 2"})
  void aDifferentSubRectangleOfTheSameMatrixYieldsItsOwnDistinctCount(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Rows 1..3, columns 1..2 -> {1, 2, 5, 6, 9, 10} = 6 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Duplicates inside the rectangle collapse to a single count. ---

  @Test
  @StdIo({"3", "1 1 2", "2 2 1", "1 2 2", "1", "1 1 3 3"})
  void repeatedValuesWithinARectangleAreCountedOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Many cells but only two values appear -> {1, 2} = 2 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Inclusion-exclusion boundaries. A rectangle anchored at the top-left subtracts prefix rows
  // and columns that fall on the zero border (r1-1 = 0, c1-1 = 0); an interior rectangle uses all
  // four prefix terms with none vanishing. ---

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "1 1 2 3"})
  void rectangleAnchoredAtTopLeftExercisesThePrefixZeroBorder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 1..2, columns 1..3 -> {1, 2, 3, 5, 6, 7} = 6. Touches row 1 and column 1, so the
    // implementation must never index prefix[-1].
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "2 2 3 3"})
  void interiorRectangleUsesAllFourInclusionExclusionTerms(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Rows 2..3, columns 2..3 -> {6, 7, 10, 1} = 4. No edge is touched, so every prefix term is
    // nonzero and must combine with the right signs.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Edge rows/columns at the far side of the grid (r2 = N, c2 = N). ---

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "4 1 4 4"})
  void bottomEdgeRowQueryCountsTheLastRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Row 4 -> {3, 4, 5, 6} = 4 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "1", "1 4 4 4"})
  void rightEdgeColumnQueryCountsTheLastColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Column 4 -> {4, 8, 2, 6} = 4 distinct.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- All four corners as single cells, in one shot, to confirm each extreme coordinate is in
  // range and answers independently. ---

  @Test
  @StdIo({
    "4",
    "1 2 3 4",
    "5 6 7 8",
    "9 10 1 2",
    "3 4 5 6",
    "4",
    "1 1 1 1",
    "1 4 1 4",
    "4 1 4 1",
    "4 4 4 4"
  })
  void everyCornerCellCountsAsASingleValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Corner cells (1,1)=1, (1,4)=4, (4,1)=3, (4,4)=6 -- each a single cell, so each answer is 1.
    assertThat(out.capturedString().trim()).isEqualTo("1\n1\n1\n1");
  }

  // --- Multiple queries: order preserved, each answered independently of the others. ---

  @Test
  @StdIo({
    "4",
    "1 2 3 4",
    "5 6 7 8",
    "9 10 1 2",
    "3 4 5 6",
    "4",
    "1 1 4 4",
    "1 1 1 1",
    "1 1 2 2",
    "3 1 3 4"
  })
  void multipleQueriesAreAnsweredInInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    // whole grid = 10; (1,1) alone = 1; rows1..2/cols1..2 = {1,2,5,6} = 4; row 3 = {9,10,1,2} = 4.
    assertThat(out.capturedString().trim()).isEqualTo("10\n1\n4\n4");
  }

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "3", "1 1 2 2", "1 1 2 2", "1 1 2 2"})
  void repeatedIdenticalQueriesEachReturnTheSameCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Queries carry no state: the same rectangle yields {1, 2, 5, 6} = 4 every time.
    assertThat(out.capturedString().trim()).isEqualTo("4\n4\n4");
  }

  @Test
  @StdIo({"4", "1 2 3 4", "5 6 7 8", "9 10 1 2", "3 4 5 6", "2", "1 1 2 3", "2 2 3 3"})
  void overlappingQueriesAreComputedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The rectangles share cells (2,2) and (2,3) but are answered apart:
    // rows1..2/cols1..3 = {1,2,3,5,6,7} = 6; rows2..3/cols2..3 = {6,7,10,1} = 4.
    assertThat(out.capturedString().trim()).isEqualTo("6\n4");
  }

  // --- Maximal grid (N = 300). Driven through the stdin/stdout helper like the project's other
  // larger-input tests, and cross-checked against an independent oracle. ---

  @Test
  void fullQueryOverAMaximalGridContainingEveryValueReturnsTen() throws IOException {
    int n = 300;
    int[][] matrix = gridWhereValueCyclesThroughAllTen(n);
    // ((i + j) % 10) + 1 sweeps every residue as i + j runs over consecutive integers, so all ten
    // values appear and the whole-grid answer is the maximum, 10.
    assertThat(runMain(buildInput(matrix, new int[][] {{1, 1, n, n}}))).isEqualTo("10");
  }

  @Test
  void boundaryAndInteriorQueriesOnAMaximalGridMatchTheNaiveOracle() throws IOException {
    int n = 300;
    int[][] matrix = gridWhereValueCyclesThroughAllTen(n);
    int[][] queries = {
      {1, 1, n, n}, // whole grid
      {1, 1, 1, 1}, // top-left single cell
      {n, n, n, n}, // bottom-right single cell
      {1, 1, 1, n}, // entire first row
      {1, 1, n, 1}, // entire first column
      {n, 1, n, n}, // entire last row
      {1, n, n, n}, // entire last column
      {150, 150, 160, 170}, // interior rectangle
      {100, 50, 250, 200}, // large off-center rectangle
    };
    assertThat(runMain(buildInput(matrix, queries))).isEqualTo(expectedFor(matrix, queries));
  }

  @Test
  void manyQueriesAreEachConsumedAndAnsweredInOrder() throws IOException {
    int n = 300;
    int[][] matrix = gridWhereValueCyclesThroughAllTen(n);
    int queryCount = 2_000;
    int[][] queries = new int[queryCount][];
    for (int k = 0; k < queryCount; k++) {
      // Deterministic, reproducible rectangles spread across the grid; coprime strides with 300
      // keep the corners varied. min/max guarantees r1 <= r2 and c1 <= c2.
      int a = (k * 7) % n + 1;
      int b = (k * 13) % n + 1;
      int c = (k * 11) % n + 1;
      int d = (k * 17) % n + 1;
      queries[k] = new int[] {Math.min(a, c), Math.min(b, d), Math.max(a, c), Math.max(b, d)};
    }
    assertThat(runMain(buildInput(matrix, queries))).isEqualTo(expectedFor(matrix, queries));
  }

  /** Builds an {@code N x N} grid whose values cycle through all of {@code 1..10}. */
  private static int[][] gridWhereValueCyclesThroughAllTen(int n) {
    int[][] matrix = new int[n][n];
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        matrix[i - 1][j - 1] = ((i + j) % 10) + 1;
      }
    }
    return matrix;
  }

  /** Newline-joined oracle answers for {@code queries}, mirroring the expected stdout. */
  private static String expectedFor(int[][] matrix, int[][] queries) {
    StringBuilder expected = new StringBuilder();
    for (int q = 0; q < queries.length; q++) {
      if (q > 0) {
        expected.append('\n');
      }
      int[] query = queries[q];
      expected.append(naiveDistinctCount(matrix, query[0], query[1], query[2], query[3]));
    }
    return expected.toString();
  }

  /**
   * Independent {@code O((r2 - r1) * (c2 - c1))} oracle: scans every cell of the rectangle and
   * counts the values seen. Coordinates are 1-indexed and inclusive.
   */
  private static int naiveDistinctCount(int[][] matrix, int r1, int c1, int r2, int c2) {
    boolean[] seen = new boolean[11]; // values 1..10
    int count = 0;
    for (int r = r1; r <= r2; r++) {
      for (int c = c1; c <= c2; c++) {
        int value = matrix[r - 1][c - 1];
        if (!seen[value]) {
          seen[value] = true;
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Builds BOJ 14846 input: {@code N}, the {@code N} matrix rows, {@code Q}, then one query a line.
   */
  private static String buildInput(int[][] matrix, int[][] queries) {
    StringBuilder sb = new StringBuilder();
    sb.append(matrix.length).append('\n');
    for (int[] row : matrix) {
      for (int j = 0; j < row.length; j++) {
        if (j > 0) {
          sb.append(' ');
        }
        sb.append(row[j]);
      }
      sb.append('\n');
    }
    sb.append(queries.length).append('\n');
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
