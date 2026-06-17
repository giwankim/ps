package boj.boj1553;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1553 도미노 찾기 (Finding Dominoes) -- count how many ways a fixed 8x7 digit grid can be tiled by
 * the 28-piece domino set.
 *
 * <p>A standard double-six domino set has 28 distinct pieces: one for every unordered pair
 * {@code (a, b)} with {@code 0 <= a <= b <= 6}. Each piece is 1x2, split into two 1x1 faces showing
 * those two numbers. An 8x7 grid (56 cells) holds one digit 0-6 per cell. Cover the whole grid so
 * that every domino spans two adjacent cells whose digits equal the piece's two faces; a piece may
 * be rotated but each of the 28 may be used at most once. Because 56 cells equal exactly 28
 * dominoes, any full cover uses every piece exactly once. The answer is the number of distinct
 * covers.
 *
 * <p>Input: 8 lines of 7 digits each, every digit in 0-6. Output: the number of covers on one line.
 *
 * <p>Sweeping the grid in row-major order, the first still-empty cell can only be covered by a
 * domino extending right or down -- its left and upper neighbors were already placed -- so a
 * backtracking search that tries just those two placements, skipping a piece already used,
 * enumerates every cover exactly once with no double counting. With only 56 cells the search is
 * tiny.
 *
 * <p>The three official samples pin the three regimes: a grid with many covers
 * ({@link #officialSampleOneCountsSixtyCovers}, 60), an impossible grid
 * ({@link #allOnesGridHasNoCoverBecauseOnlyOneDoubleOnePieceExists}, 0), and a grid forced into a
 * single cover ({@link #officialSampleThreeHasExactlyOneCover}, 1). Two further grids whose counts
 * were cross-checked against the in-file brute-force oracle pin the small-count band
 * ({@link #constructedGridHasExactlyTwoCovers}, {@link #constructedGridHasExactlyThreeCovers}).
 *
 * <p>The randomized cases deal the 28 distinct pieces onto a random perfect domino tiling -- a grid
 * that is therefore coverable at least once -- and cross-check the program against an independent
 * oracle that recounts every cover, a route constant or hand-picked grids cannot reach.
 */
class MainTest {

  // --- The three official samples from the statement (recovered from the archived problem page).
  // ---

  @Test
  @StdIo({"0000000", "0123456", "1111112", "1234562", "2222333", "3456345", "3444556", "6456566"})
  void officialSampleOneCountsSixtyCovers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // First official sample: the 28 pieces fit this grid in 60 different ways.
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }

  @Test
  @StdIo({"1111111", "1111111", "1111111", "1111111", "1111111", "1111111", "1111111", "1111111"})
  void allOnesGridHasNoCoverBecauseOnlyOneDoubleOnePieceExists(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Second official sample: every adjacent pair reads (1,1), so every domino would have to be the
    // single (1,1) piece. One placement exhausts it and the other 27 dominoes have nothing left to
    // use, so no cover exists.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"0054450", "6645056", "0151226", "6522303", "0246343", "6411432", "0324531", "6215131"})
  void officialSampleThreeHasExactlyOneCover(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Third official sample: the digits admit a single arrangement of the 28 pieces.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Small-count band: grids built from a valid tiling, counts verified by the oracle below. ---

  @Test
  @StdIo({"4100624", "4604362", "3451363", "3456155", "3220304", "1120661", "0512513", "2256504"})
  void constructedGridHasExactlyTwoCovers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Laid out from a valid tiling that admits one alternative rearrangement of its pieces, for two
    // covers in all -- the smallest count above a single forced solution.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"2206626", "5000635", "4632414", "4533402", "0631225", "3433115", "2655411", "1450016"})
  void constructedGridHasExactlyThreeCovers(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A second constructed grid, coverable in exactly three ways, separating "a few" from the lone
    // forced cover above.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Randomized scale, cross-checked against an independent recounting oracle. ---

  @Test
  void randomSolvableGridMatchesOracle() throws IOException {
    int[][] grid = randomSolvableGrid(new Random(1553L));
    // Dealing the 28 pieces onto a random perfect tiling guarantees the grid is coverable at least
    // once; the program must agree with the oracle on exactly how many covers there are.
    assertThat(runMain(buildInput(grid))).isEqualTo(Integer.toString(solve(grid)));
  }

  @Test
  void secondRandomSolvableGridMatchesOracle() throws IOException {
    int[][] grid = randomSolvableGrid(new Random(15531L));
    // A second seed lays out a different solvable grid, exercising the count over another piece
    // arrangement.
    assertThat(runMain(buildInput(grid))).isEqualTo(Integer.toString(solve(grid)));
  }

  /**
   * Independent oracle: count every way to cover the 8x7 grid with the 28-piece set. Sweeping in
   * row-major order, the first uncovered cell is paired with its right or down neighbor when that
   * forms a piece not yet used; reaching the end of the sweep counts one cover.
   */
  private static int solve(int[][] grid) {
    return count(grid, new boolean[8][7], new boolean[7][7], 0);
  }

  private static int count(int[][] grid, boolean[][] filled, boolean[][] used, int idx) {
    if (idx == 8 * 7) {
      return 1;
    }
    int r = idx / 7;
    int c = idx % 7;
    if (filled[r][c]) {
      return count(grid, filled, used, idx + 1);
    }
    int total = 0;
    int a = grid[r][c];
    filled[r][c] = true;
    for (int[] dir : new int[][] {{0, 1}, {1, 0}}) {
      int nr = r + dir[0];
      int nc = c + dir[1];
      if (nr < 8 && nc < 7 && !filled[nr][nc]) {
        int b = grid[nr][nc];
        if (!used[a][b]) {
          used[a][b] = used[b][a] = true;
          filled[nr][nc] = true;
          total += count(grid, filled, used, idx + 1);
          filled[nr][nc] = false;
          used[a][b] = used[b][a] = false;
        }
      }
    }
    filled[r][c] = false;
    return total;
  }

  /**
   * A grid guaranteed to have at least one cover: build a random perfect domino tiling of the 8x7
   * board, then deal the 28 distinct pieces onto its dominoes in random orientation. The greedy
   * row-major matching can dead-end on the last row, so it is retried with fresh choices until it
   * lays down all 28 dominoes.
   */
  private static int[][] randomSolvableGrid(Random rng) {
    while (true) {
      int[] owner = new int[8 * 7];
      Arrays.fill(owner, -1);
      List<int[]> dominoes = new ArrayList<>();
      boolean ok = true;
      for (int idx = 0; idx < 8 * 7 && ok; idx++) {
        if (owner[idx] != -1) {
          continue;
        }
        int r = idx / 7;
        int c = idx % 7;
        List<Integer> options = new ArrayList<>();
        if (c + 1 < 7 && owner[idx + 1] == -1) {
          options.add(idx + 1);
        }
        if (r + 1 < 8 && owner[idx + 7] == -1) {
          options.add(idx + 7);
        }
        if (options.isEmpty()) {
          ok = false;
          break;
        }
        int mate = options.get(rng.nextInt(options.size()));
        owner[idx] = owner[mate] = dominoes.size();
        dominoes.add(new int[] {idx, mate});
      }
      if (!ok || dominoes.size() != 28) {
        continue;
      }
      List<int[]> pieces = new ArrayList<>();
      for (int x = 0; x <= 6; x++) {
        for (int y = x; y <= 6; y++) {
          pieces.add(new int[] {x, y});
        }
      }
      Collections.shuffle(pieces, rng);
      int[][] grid = new int[8][7];
      for (int k = 0; k < 28; k++) {
        int[] cells = dominoes.get(k);
        int a = pieces.get(k)[0];
        int b = pieces.get(k)[1];
        if (rng.nextBoolean()) {
          int tmp = a;
          a = b;
          b = tmp;
        }
        grid[cells[0] / 7][cells[0] % 7] = a;
        grid[cells[1] / 7][cells[1] % 7] = b;
      }
      return grid;
    }
  }

  /** Builds BOJ 1553 input: 8 lines of 7 digits each. */
  private static String buildInput(int[][] grid) {
    StringBuilder sb = new StringBuilder();
    for (int[] row : grid) {
      for (int v : row) {
        sb.append((char) ('0' + v));
      }
      sb.append('\n');
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
