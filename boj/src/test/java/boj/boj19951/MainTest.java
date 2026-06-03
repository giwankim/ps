package boj.boj19951;

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
 * BOJ 19951 태상이의 훈련소 생활 -- Gold V, range-add via prefix sum.
 *
 * <p>A drill ground is a single row of {@code N} cells numbered {@code 1..N}, each with a height
 * {@code H_i}. Then {@code M} instructions arrive, each a triple {@code (a, b, k)} meaning "add
 * {@code k} to the height of every cell in {@code [a, b]}" -- {@code k >= 0} covers (raises) and
 * {@code k < 0} digs (lowers). After all instructions are applied, print the final height of each
 * cell, separated by spaces, on one line.
 *
 * <p>Input: line 1 is {@code "N M"}; line 2 is the {@code N} space-separated heights; the next
 * {@code M} lines are the instructions {@code "a b k"}. Output: the {@code N} final heights,
 * space-separated.
 *
 * <p>Constraints: {@code 1 <= N <= 100,000}; {@code 1 <= M <= 100,000}; {@code -10,000 <= H_i <=
 * 10,000}; each instruction has {@code 1 <= a <= b <= N} and {@code |k| <= 100}; all values are
 * integers.
 *
 * <p><b>Why O(N + M), not O(N * M).</b> Applying each instruction cell-by-cell is {@code O(N * M) =
 * 10^10} in the worst case (every instruction covering the whole array) and times out. The intended
 * solution is a difference array (imos): record {@code diff[a] += k} and {@code diff[b + 1] -= k},
 * then a single prefix-sum pass turns {@code diff} into the per-cell total delta, which is added to
 * the original heights. {@link #everyInstructionCoversWholeArrayAtMaxScale()} guards the time
 * bound; the {@code b + 1} write also forces the diff array to tolerate index {@code N + 1} -- see
 * {@link #instructionEndingAtTheLastCellDoesNotOverflow(StdOut)}.
 *
 * <p><b>No overflow trap (contrast with the prefix-sum siblings).</b> A single cell's total change
 * is bounded by {@code M * max|k| = 100,000 * 100 = 10^7}, and with {@code |H_i| <= 10^4} the final
 * magnitude stays under {@code 1.001 * 10^7}, well inside a signed 32-bit {@code int} (limit
 * {@code 2,147,483,647}). Unlike BOJ 2015/2143/25332, no {@code long} is required;
 * {@link #repeatedUpdatesToOneCellReachMaxMagnitudeWithinIntRange()} pins this down.
 */
class MainTest {

  // --- Smallest input (N = M = 1): one cell, one instruction. Cover raises, dig lowers. ---

  // H=[10], instruction "1 1 5": the lone cell is covered with +5 -> 15.
  @Test
  @StdIo({"1 1", "10", "1 1 5"})
  void singleCellCoveredRaisesHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // H=[10], instruction "1 1 -3": the lone cell is dug by 3 -> 7. Confirms k < 0 subtracts.
  @Test
  @StdIo({"1 1", "10", "1 1 -3"})
  void singleCellDugLowersHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- The cover/dig/no-op semantics across a multi-cell range. ---

  // H=[1,2,3,4,5], "1 3 2": cells 1..3 each rise by 2 -> [3,4,5,4,5]; cells 4,5 untouched.
  @Test
  @StdIo({"5 1", "1 2 3 4 5", "1 3 2"})
  void coverIncreasesEveryCellInTheRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 4 5 4 5");
  }

  // H=[5,5,5], "1 3 -2": cells 1..3 each fall by 2 -> [3,3,3].
  @Test
  @StdIo({"3 1", "5 5 5", "1 3 -2"})
  void digDecreasesEveryCellInTheRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 3 3");
  }

  // H=[1,2,3], "1 3 0": k = 0 is a covering instruction that adds nothing -> heights unchanged.
  // |k| <= 100 permits k = 0, and diff[a]+=0 / diff[b+1]-=0 must leave the array as-is.
  @Test
  @StdIo({"3 1", "1 2 3", "1 3 0"})
  void zeroValuedInstructionLeavesHeightsUnchanged(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3");
  }

  // --- Range boundaries: the classic difference-array off-by-one lives at b + 1. ---

  // N=3, H=[0,0,0], "1 3 5": the range ends at the last cell, so the closing write lands at
  // diff[N + 1] = diff[4]. A diff array sized only for indices 1..N would throw here; the suite
  // demands index N + 1 be writable. Result: every cell rises by 5 -> [5,5,5].
  @Test
  @StdIo({"3 1", "0 0 0", "1 3 5"})
  void instructionEndingAtTheLastCellDoesNotOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 5 5");
  }

  // N=4, H=[0,0,0,0], "1 2 3": range starts at the first cell (open at diff[1]) and closes at
  // diff[3], so cells 1,2 rise by 3 and cells 3,4 stay at 0 -> [3,3,0,0].
  @Test
  @StdIo({"4 1", "0 0 0 0", "1 2 3"})
  void instructionStartingAtTheFirstCellOpensAtIndexOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 3 0 0");
  }

  // N=5, H all 0, "3 3 7": a length-1 range (a == b) touches exactly one interior cell -- diff[3]
  // += 7 and diff[4] -= 7 cancel one apart -> [0,0,7,0,0]. Catches an implementation that
  // mishandles
  // single-cell ranges.
  @Test
  @StdIo({"5 1", "0 0 0 0 0", "3 3 7"})
  void singleCellRangeAffectsOnlyThatCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 7 0 0");
  }

  // --- Multiple instructions: disjoint, overlapping, and cancelling. ---

  // N=6, H all 0, "1 2 3" then "4 5 -2": the ranges do not touch, so cell 3 and cell 6 stay 0 while
  // 1..2 rise by 3 and 4..5 fall by 2 -> [3,3,0,-2,-2,0]. Confirms independent ranges do not bleed.
  @Test
  @StdIo({"6 2", "0 0 0 0 0 0", "1 2 3", "4 5 -2"})
  void disjointInstructionsAffectOnlyTheirOwnRanges(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 3 0 -2 -2 0");
  }

  // N=5, H all 0, "1 3 2" then "2 4 3": the overlap 2..3 receives both deltas.
  // cell1=2, cell2=2+3=5, cell3=2+3=5, cell4=3, cell5=0 -> [2,5,5,3,0].
  @Test
  @StdIo({"5 2", "0 0 0 0 0", "1 3 2", "2 4 3"})
  void overlappingInstructionsAccumulateOnTheOverlap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 5 5 3 0");
  }

  // H=[7,8,9], "1 3 5" then "1 3 -5": covering then digging the same range by the same amount nets
  // zero, returning the original heights -> [7,8,9]. The deltas must be additive, not last-wins.
  @Test
  @StdIo({"3 2", "7 8 9", "1 3 5", "1 3 -5"})
  void coverThenDigSameRangeCancels(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7 8 9");
  }

  // H=[1,2,3,4,5] with three instructions "1 3 2", "1 4 -1", "2 5 2". Per-cell delta:
  //   cell1: +2-1        = +1 -> 2
  //   cell2: +2-1+2      = +3 -> 5
  //   cell3: +2-1+2      = +3 -> 6
  //   cell4:    -1+2     = +1 -> 5   (a published walk-through wrongly reports 6 here)
  //   cell5:       +2    = +2 -> 7
  // -> [2,5,6,5,7]. An integration case over several overlapping ranges with mixed signs.
  @Test
  @StdIo({"5 3", "1 2 3 4 5", "1 3 2", "1 4 -1", "2 5 2"})
  void multipleInstructionsAccumulatePerCell(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 5 6 5 7");
  }

  // --- Negative initial heights crossing zero under cover/dig. ---

  // H=[-3,0,4], "1 2 5" then "2 3 -2":
  //   cell1: -3 + 5      = 2
  //   cell2:  0 + 5 - 2  = 3
  //   cell3:  4 - 2      = 2
  // -> [2,3,2]. Starting heights may be negative (down to -10,000) and individual cells may move
  // across zero in either direction.
  @Test
  @StdIo({"3 2", "-3 0 4", "1 2 5", "2 3 -2"})
  void negativeInitialHeightsAreUpdatedCorrectly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3 2");
  }

  // --- Scale + performance, driven through the stdin/stdout helper. ---

  // N = M = 100,000 with every instruction "1 N 1": the whole array is covered M times, so each
  // cell (starting at 0) ends at M = 100,000. This is the O(N * M) = 10^10 worst case that defeats
  // a
  // naive per-cell update but is O(N + M) with a difference array. Also a full-width output check.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void everyInstructionCoversWholeArrayAtMaxScale() throws IOException {
    int n = 100_000;
    int m = 100_000;
    int[] heights = filled(n, 0);
    int[][] instructions = new int[m][];
    for (int i = 0; i < m; i++) {
      instructions[i] = new int[] {1, n, 1};
    }
    String expected = repeatedValueLine(n, m); // each cell rose by exactly m
    assertThat(runMain(buildInput(heights, instructions))).isEqualTo(expected);
  }

  // One cell pushed to its extreme: N = 1, M = 100,000 instructions of "1 1 100" on H = [10,000].
  // The final height is 10,000 + 100 * 100,000 = 10,010,000 -- the maximum magnitude this problem
  // can produce. It fits a signed 32-bit int (< 2.1 * 10^9), so an int accumulator is correct and
  // no long is needed; this case would still pass with int and is here to document that bound.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void repeatedUpdatesToOneCellReachMaxMagnitudeWithinIntRange() throws IOException {
    int m = 100_000;
    int[] heights = {10_000};
    int[][] instructions = new int[m][];
    for (int i = 0; i < m; i++) {
      instructions[i] = new int[] {1, 1, 100};
    }
    long expected = 10_000L + 100L * m;
    assertThat(runMain(buildInput(heights, instructions))).isEqualTo(Long.toString(expected));
  }

  // --- Randomised cross-check against an independent brute-force oracle. Small arrays, short
  // instruction lists, negative/zero/positive heights and mixed-sign k make overlapping ranges,
  // boundary touches, and cancellations frequent -- catching off-by-one range bugs and sign
  // mistakes the hand cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(19951);
    for (int trial = 0; trial < 400; trial++) {
      int n = 1 + rnd.nextInt(10); // 1..10
      int m = 1 + rnd.nextInt(10); // 1..10
      int[] heights = new int[n];
      for (int i = 0; i < n; i++) {
        heights[i] = rnd.nextInt(41) - 20; // -20..20
      }
      int[][] instructions = new int[m][];
      for (int i = 0; i < m; i++) {
        int a = 1 + rnd.nextInt(n);
        int b = 1 + rnd.nextInt(n);
        if (a > b) {
          int tmp = a;
          a = b;
          b = tmp;
        }
        int k = rnd.nextInt(201) - 100; // -100..100
        instructions[i] = new int[] {a, b, k};
      }
      String input = buildInput(heights, instructions);
      String expected = bruteForceHeights(heights, instructions);
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: apply every instruction cell-by-cell to a copy of the heights and join the
   * result. Obviously correct, far too slow for the judge ({@code O(N * M)}), trustworthy for tiny
   * inputs.
   *
   * @implNote {@code O(M * R)} time where {@code R} is the maximum range width; cells are kept as
   *     {@code long} so the oracle never overflows even though the judged answer fits an
   *     {@code int}.
   */
  private static String bruteForceHeights(int[] heights, int[][] instructions) {
    long[] cells = new long[heights.length];
    for (int i = 0; i < heights.length; i++) {
      cells[i] = heights[i];
    }
    for (int[] ins : instructions) {
      int a = ins[0];
      int b = ins[1];
      int k = ins[2];
      for (int c = a; c <= b; c++) {
        cells[c - 1] += k;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cells.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(cells[i]);
    }
    return sb.toString();
  }

  /** Builds BOJ 19951 input: {@code "N M"}, then the heights, then one {@code "a b k"} per line. */
  private static String buildInput(int[] heights, int[][] instructions) {
    StringBuilder sb = new StringBuilder();
    sb.append(heights.length).append(' ').append(instructions.length).append('\n');
    for (int i = 0; i < heights.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(heights[i]);
    }
    sb.append('\n');
    for (int[] ins : instructions) {
      sb.append(ins[0]).append(' ').append(ins[1]).append(' ').append(ins[2]).append('\n');
    }
    return sb.toString();
  }

  /** A single line of {@code count} copies of {@code value}, space-separated. */
  private static String repeatedValueLine(int count, long value) {
    String token = Long.toString(value);
    StringBuilder sb = new StringBuilder(count * (token.length() + 1));
    for (int i = 0; i < count; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(token);
    }
    return sb.toString();
  }

  private static int[] filled(int length, int value) {
    int[] arr = new int[length];
    Arrays.fill(arr, value);
    return arr;
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
