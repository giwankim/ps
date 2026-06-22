package boj.boj5569;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 5569 출근 경로 ("Commute Route", JOI 2010 qualifier #5) -- count the constrained shortest routes
 * across a grid of city roads.
 *
 * <p>The city has {@code w} north-south roads (numbered {@code 1..w} from the west) and {@code h}
 * east-west roads (numbered {@code 1..h} from the south); they cross at {@code w*h} intersections.
 * Sang-geun commutes from the south-westmost intersection (home) to the north-eastmost intersection
 * (office) along a <b>shortest</b> route, so every block he walks goes either one step east or one
 * step north. The single extra rule: he refuses to turn at two intersections in a row -- after
 * changing direction at one intersection he must go straight through the next one. Print the number
 * of distinct legal routes modulo {@code 100000}.
 *
 * <p>Input is one line with two integers {@code w h}; constraints {@code 2 <= w, h <= 100}. Output
 * is the route count mod {@code 100000}.
 *
 * <ul>
 *   <li><b>Ignoring the no-two-turns rule.</b> Without the constraint this is the classic lattice
 *       path count {@code C(w+h-2, w-1)}. A {@code 3x3} grid has {@code C(4,2) = 6} monotone paths
 *       but only {@code 4} legal ones -- the two pure zigzags {@code ENEN}/{@code NENE} turn at
 *       every intersection and are removed ({@link #threeByThreeRemovesTheTwoZigzags(StdOut)}). At
 *       {@code 4x4} it is {@code 8} versus {@code C(6,3) = 20}
 *       ({@link #fourByFourIsEightNotTwenty(StdOut)}).
 *   <li><b>Forgetting the modulo (or only reducing at the very end).</b> The count grows fast: a
 *       {@code 15x15} grid has {@code 143688} legal routes, so the printed answer is {@code 43688}
 *       ({@link #fifteenSquareIsWhereTheModuloFirstBites()}). A {@code 100x100} grid's raw count
 *       has 41 digits and overflows {@code long}, so the modulo must be applied <i>inside</i> the
 *       recurrence, not bolted on at the end ({@link #maxSquareGridForcesInLoopModulo()}).
 *   <li><b>Mishandling the thin grid.</b> When {@code w == 2} (or {@code h == 2}) only a single
 *       east (resp. north) move exists. Placing it in the interior of the long run turns on both
 *       sides -- two consecutive turns -- so only "the lone move first" or "the lone move last"
 *       survive: exactly {@code 2} routes for every length
 *       ({@link #tallThinGridIsAlwaysTwo(StdOut)}, {@link #wideThinGridIsAlwaysTwo(StdOut)},
 *       {@link #maxThinGridIsStillTwo()}).
 *   <li><b>The base case at the start.</b> The first move out of home is never a turn (there is no
 *       prior direction), and home itself must not be treated as a turn. The minimal {@code 2x2}
 *       grid pins this: both of its routes (east-then-north, north-then-east) have a single turn
 *       and are legal, so the answer is {@code 2} ({@link #minimalGridHasTwoRoutes(StdOut)}).
 *   <li><b>Off-by-one between roads and blocks, or swapping {@code w} and {@code h}.</b> The exact
 *       hand-checked constants for several grids pin the road-vs-block counting, and because the
 *       route count is symmetric in {@code w} and {@code h}, paired inputs document that the two
 *       are read in a consistent order ({@link #symmetricThreeByFour(StdOut)} /
 *       {@link #symmetricFourByThree(StdOut)}).
 * </ul>
 *
 * <p>The hand-picked constants are cross-checked two independent ways. {@link #bruteForcePaths}
 * just enumerates every monotone route and filters out illegal ones -- a completely different
 * method from the recurrence -- and drives an exhaustive sweep of the small grids
 * ({@link #everySmallGridMatchesBruteForceEnumeration()}). {@link #dpReference} is the modular DP
 * and drives a full-range random cross-check at the judge's scale where enumeration is infeasible
 * ({@link #randomGridsMatchTheModularDpReference()}); the two references agree wherever they
 * overlap.
 */
class MainTest {

  // --- Official sample cases from the problem statement (BOJ 5569 / JOI 2010 예선 5). ---
  // These two grids and their answers are the worked examples printed in the problem itself, so
  // they are pinned here as first-class tests, independent of the deeper diagnostic families below.
  // The same two inputs also recur further down (3x4 inside the symmetry family, 15x15 inside the
  // modulo family), where they exist for other reasons; keeping the canonical samples here as well
  // means a future reshuffle of those families cannot silently drop the problem's own examples.

  // 예제 입력 1: "3 4" -> 예제 출력 1: "5". The statement draws the five legal commute routes on the
  // 3x4 grid; f(3,4) = 5. (Plain lattice counting would give C(5,2) = 10, so this also pins that
  // the
  // no-two-consecutive-turns rule is enforced.)
  @Test
  @StdIo({"3 4"})
  void problemExampleOneThreeByFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // 예제 입력 2: "15 15" -> 예제 출력 2: "43688". The raw count is 143688 routes, and the statement
  // shows 143688 mod 100000 = 43688 -- the worked example for the modulo. Driven through runMain so
  // a
  // @Timeout guards against an accidentally exponential search at this size.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void problemExampleTwoFifteenSquare() throws IOException {
    assertThat(runMain("15 15")).isEqualTo("43688");
  }

  // --- Base case: the smallest grid. ---

  // A 2x2 grid (one block each way) has exactly two routes: east-then-north and north-then-east.
  // Each makes a single turn at the corner, and a lone turn is always legal, so the answer is 2.
  // This pins the start handling: the first move is not a turn and home is not a turn.
  @Test
  @StdIo({"2 2"})
  void minimalGridHasTwoRoutes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: the no-two-consecutive-turns rule actually prunes paths. ---

  // 3x3 needs two east and two north moves. Of the C(4,2) = 6 monotone routes, the two pure zigzags
  // ENEN and NENE turn at every interior intersection (two turns in a row) and are illegal; the
  // other four -- EENN, ENNE, NEEN, NNEE -- each turn at most once in a row. So the answer is 4,
  // not
  // 6. A solution that ignores the constraint prints 6 and fails here.
  @Test
  @StdIo({"3 3"})
  void threeByThreeRemovesTheTwoZigzags(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // 4x4 is 8 legal routes against C(6,3) = 20 unconstrained monotone routes -- a wide gap that any
  // plain Pascal's-triangle path count gets badly wrong.
  @Test
  @StdIo({"4 4"})
  void fourByFourIsEightNotTwenty(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // 3x5 has C(6,2) = 15 monotone routes but only 6 legal ones, a non-square check on the same
  // constraint-pruning behavior.
  @Test
  @StdIo({"3 5"})
  void threeByFiveIsSixNotFifteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Family: the growing-square diagonal, exact hand-verified counts. ---

  // f(5,5) = 18: past the point where the count stops looking like a power of two (2, 4, 8, then
  // 18),
  // so a solution that guessed a closed form rather than running the recurrence diverges here.
  @Test
  @StdIo({"5 5"})
  void fiveBySquareIsEighteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // f(6,6) = 42.
  @Test
  @StdIo({"6 6"})
  void sixBySquareIsFortyTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("42");
  }

  // f(7,7) = 100.
  @Test
  @StdIo({"7 7"})
  void sevenBySquareIsOneHundred(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // --- Family: thin grids are always 2, regardless of the long dimension. ---

  // With w = 2 there is exactly one east move among the north moves. If it sits in the interior of
  // the north run it turns on both sides (two turns in a row), so only "east first" and "east last"
  // are legal: 2 routes, independent of h.
  @Test
  @StdIo({"2 7"})
  void tallThinGridIsAlwaysTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // The mirror image with h = 2: a single north move among the east moves, again only 2 legal
  // routes. Together with the tall case this also documents that w and h are read symmetrically.
  @Test
  @StdIo({"7 2"})
  void wideThinGridIsAlwaysTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family: symmetry in w and h (the count is invariant under swapping them). ---

  // f(3,4) = 5.
  @Test
  @StdIo({"3 4"})
  void symmetricThreeByFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // f(4,3) = 5 -- the same grid with w and h swapped must give the same answer.
  @Test
  @StdIo({"4 3"})
  void symmetricFourByThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // f(4,5) = 11.
  @Test
  @StdIo({"4 5"})
  void symmetricFourByFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // f(5,4) = 11 -- the swap of 4x5.
  @Test
  @StdIo({"5 4"})
  void symmetricFiveByFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Family: the modulo (and in-loop reduction at scale). Driven through runMain so a @Timeout
  // can guard against an accidentally exponential search at the judge's input sizes. ---

  // 15x15 is the smallest square grid whose legal-route count (143688) exceeds 100000, so it is the
  // first place the modulo changes the printed answer to 43688. A solution that omits "% 100000"
  // prints 143688 and fails.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void fifteenSquareIsWhereTheModuloFirstBites() throws IOException {
    assertThat(runMain("15 15")).isEqualTo("43688");
  }

  // A mid-scale square, f(50,50) mod 100000 = 60770. Exercises the recurrence well beyond the tiny
  // hand cases while still being an exact cross-checked constant.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void midScaleFiftySquare() throws IOException {
    assertThat(runMain("50 50")).isEqualTo("60770");
  }

  // The maximum square grid. The raw count has 41 digits, far beyond long, so the modulo must be
  // applied inside the recurrence; reducing only at the end overflows and prints garbage. The
  // expected printed value is 71084.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSquareGridForcesInLoopModulo() throws IOException {
    assertThat(runMain("100 100")).isEqualTo("71084");
  }

  // The largest asymmetric grid, 100x99, mod value 86233.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxAsymmetricGrid() throws IOException {
    assertThat(runMain("100 99")).isEqualTo("86233");
  }

  // 99x100 -- the swap of the previous case -- must print the same 86233, confirming the symmetry
  // holds at full scale and the two dimensions are not accidentally transposed in the answer.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxAsymmetricGridSwappedIsEqual() throws IOException {
    assertThat(runMain("99 100")).isEqualTo("86233");
  }

  // The thin-grid invariant at the maximum length: 100x2 is still exactly 2.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxThinGridIsStillTwo() throws IOException {
    assertThat(runMain("100 2")).isEqualTo("2");
    assertThat(runMain("2 100")).isEqualTo("2");
  }

  // --- Exhaustive and randomized cross-checks against the two independent references. ---

  // Every grid small enough to enumerate (at most 16 total moves) must match the brute-force route
  // enumeration. A single sweep that, in one shot, pins the constraint pruning, the thin-grid
  // invariant, the base case, and the symmetry across a dense block of grids -- checked against a
  // method that shares no logic with the recurrence. The counts here all stay below 100000, so the
  // exact enumeration equals the modular answer directly.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everySmallGridMatchesBruteForceEnumeration() throws IOException {
    for (int w = 2; w <= 12; w++) {
      for (int h = 2; h <= 12; h++) {
        if ((w - 1) + (h - 1) <= 16) {
          String expected = Long.toString(bruteForcePaths(w, h));
          assertThat(runMain(w + " " + h)).as("w=%d h=%d", w, h).isEqualTo(expected);
        }
      }
    }
  }

  // A pseudo-random sample drawn from across the full [2, 100] x [2, 100] range, cross-checked
  // against the modular DP reference. Fixed seed -> deterministic across JVMs. Reaches the large,
  // modulo-active grids that the small enumeration sweep cannot, exercising parsing, I/O, and the
  // recurrence at the judge's scale.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomGridsMatchTheModularDpReference() throws IOException {
    Random rng = new Random(5569L);
    for (int trial = 0; trial < 200; trial++) {
      int w = 2 + rng.nextInt(99); // 2..100
      int h = 2 + rng.nextInt(99); // 2..100
      String expected = Integer.toString(dpReference(w, h));
      assertThat(runMain(w + " " + h)).as("w=%d h=%d", w, h).isEqualTo(expected);
    }
  }

  /** The modulus fixed by the statement. */
  private static final int MOD = 100000;

  /**
   * Independent reference 1: brute-force enumeration of every legal route, sharing no logic with
   * the intended recurrence. It walks all monotone east/north routes and keeps only those that
   * never turn at two intersections in a row, returning the exact count (no modulo). Used only on
   * small grids, where the exact count is well below {@link #MOD} and therefore equals the printed
   * answer.
   *
   * @implNote Exponential: it explores on the order of {@code C(w+h-2, w-1)} routes -- where
   *     {@code w} and {@code h} are the grid dimensions -- so callers must keep the grid small.
   */
  private static long bruteForcePaths(int w, int h) {
    return countRoutes(w - 1, h - 1, -1, false);
  }

  /**
   * Recursively counts legal completions from a partial route.
   *
   * @param e remaining east moves
   * @param n remaining north moves
   * @param lastMove the previous move (0 = east, 1 = north, -1 = none yet)
   * @param prevWasTurn whether the previous move was itself a turn (so another turn now is illegal)
   */
  private static long countRoutes(int e, int n, int lastMove, boolean prevWasTurn) {
    if (e == 0 && n == 0) {
      return 1;
    }
    long total = 0;
    if (e > 0) {
      boolean turn = lastMove == 1; // arriving by north, now heading east
      if (!(turn && prevWasTurn)) {
        total += countRoutes(e - 1, n, 0, turn);
      }
    }
    if (n > 0) {
      boolean turn = lastMove == 0; // arriving by east, now heading north
      if (!(turn && prevWasTurn)) {
        total += countRoutes(e, n - 1, 1, turn);
      }
    }
    return total;
  }

  /**
   * Independent reference 2: the modular four-state DP, used to cross-check the large grids that
   * brute force cannot reach. State {@code dp[i][j][d][r]} counts routes reaching intersection
   * {@code (i, j)} having arrived in direction {@code d} (0 = east, 1 = north), where {@code r}
   * records whether that arrival was a straight continuation; a turn into a cell is permitted only
   * from a straight predecessor, which encodes the no-two-turns rule. Validated against
   * {@link #bruteForcePaths} on every grid where both apply.
   *
   * @implNote {@code O(w * h)} time and space -- where {@code w} and {@code h} are the grid
   *     dimensions.
   */
  private static int dpReference(int w, int h) {
    int[][][][] dp = new int[w + 1][h + 1][2][2];
    for (int i = 2; i <= w; i++) {
      dp[i][1][0][0] = 1;
    }
    for (int j = 2; j <= h; j++) {
      dp[1][j][1][0] = 1;
    }
    for (int i = 2; i <= w; i++) {
      for (int j = 2; j <= h; j++) {
        dp[i][j][0][0] = (dp[i - 1][j][0][0] + dp[i - 1][j][0][1]) % MOD;
        dp[i][j][0][1] = dp[i - 1][j][1][0];
        dp[i][j][1][0] = (dp[i][j - 1][1][0] + dp[i][j - 1][1][1]) % MOD;
        dp[i][j][1][1] = dp[i][j - 1][0][0];
      }
    }
    int ans = 0;
    for (int a = 0; a < 2; a++) {
      for (int b = 0; b < 2; b++) {
        ans += dp[w][h][a][b];
      }
    }
    return ans % MOD;
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
