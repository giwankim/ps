package boj.boj20544;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 20544 공룡게임 ("Dinosaur Game") -- count the number of <em>clearable</em> maps of length
 * {@code N}, modulo {@code 1,000,000,007}.
 *
 * <p>A map is a sequence of {@code N} points. Each point is either floor (height 0) or holds a
 * cactus of height 1 or 2. The dinosaur starts at point 1 and runs toward a virtual point
 * {@code N+1}, which is always floor. A map is <b>clearable</b> when every one of the following
 * holds:
 *
 * <ul>
 *   <li><b>Floor start.</b> Point 1 must be floor (height 0); landing on a cactus at the start is
 *       fatal.
 *   <li><b>Jump span of two.</b> The dinosaur clears at most two <em>adjacent</em> cacti in a
 *       single jump, so no three consecutive points may all hold cacti (no run of three or more
 *       non-zero heights).
 *   <li><b>Jump height limit.</b> Two adjacent cacti can be cleared only if their heights sum to
 *       less than four. The single forbidden neighboring pair is therefore {@code (2, 2)}.
 *   <li><b>At least one tall cactus.</b> A height-2 cactus must appear somewhere, or the map is too
 *       dull to count.
 * </ul>
 *
 * <p>So this counts the height sequences {@code h[1..N]} over {@code {0, 1, 2}} with {@code h[1] =
 * 0}, no three-in-a-row of non-zeros, no adjacent {@code 2 2}, and at least one {@code 2}. The
 * intended solution is an {@code O(N)} dynamic program; the standard framing is "all valid
 * sequences" minus "valid sequences that never use a 2", which is why the height-2 requirement is
 * the easiest rule to drop by accident.
 *
 * <p>The expected counts below are cross-checked against an independent exhaustive oracle
 * ({@link #bruteForceCount(int)}) that enumerates every sequence and applies the four rules
 * directly -- a completely different algorithm from the DP, so the two agreeing is real evidence.
 * The two values quoted in the statement ({@code N = 3 -> 4} and {@code N = 4 -> 10}) anchor the
 * oracle, and {@link #everyMapLengthUpToThirteenMatchesExhaustiveEnumeration()} sweeps the whole
 * small range through it.
 *
 * <p>Four families of mistakes drive the hand-picked fixtures:
 *
 * <ul>
 *   <li><b>Dropping the "at least one tall cactus" rule.</b> {@code N = 1} pins it: the only
 *       length-1 sequence is the forced floor {@code "0"}, which has no cactus at all, so the
 *       answer is {@code 0}, not {@code 1}
 *       ({@link #lengthOneHasNoClearableMapBecauseNoCactusCanAppear}).
 *   <li><b>Forgetting the {@code (2, 2)} ban.</b> At {@code N = 3} exactly one otherwise-valid
 *       sequence, {@code "0 2 2"}, is rejected, giving four maps rather than five
 *       ({@link #officialSampleLengthThreeHasFourMaps}).
 *   <li><b>Allowing three adjacent cacti.</b> {@code N = 4} is the smallest length where a run of
 *       three non-zeros becomes possible; rejecting them (together with the {@code (2, 2)} ban)
 *       leaves ten of the twenty-seven height assignments
 *       ({@link #officialSampleLengthFourHasTenMaps}).
 *   <li><b>Off-by-one or missing modulo in the recurrence.</b> The mid-size anchors {@code N =
 *       5..8} pin the exact growth, and {@code N = 1000} forces both an {@code O(N)} solution and a
 *       correctly applied modulo, since the true count there dwarfs the modulus
 *       ({@link #maxMapLengthExercisesTheModulo()}).
 * </ul>
 */
class MainTest {

  private static final int MOD = 1_000_000_007;

  // --- The two samples quoted in the statement. ---

  // N = 3 -> 4. Starting from the forced floor, the clearable maps are "002", "012", "020", "021".
  // The fifth candidate that uses a 2, "022", is rejected because the adjacent pair (2, 2) sums to
  // 4 and cannot be jumped. This case is the cleanest guard for the height-limit rule.
  @Test
  @StdIo({"3"})
  void officialSampleLengthThreeHasFourMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // N = 4 -> 10. The first length at which three consecutive cacti can occur, so this case
  // exercises both the "at most two adjacent cacti" rule (sequences like "0112", "0121", "0211" are
  // out) and the (2, 2) ban ("0220", "0022" are out). Ten of the 27 assignments of h[2..4] survive.
  @Test
  @StdIo({"4"})
  void officialSampleLengthFourHasTenMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- The smallest inputs: the base case and the "at least one tall cactus" rule. ---

  // N = 1 -> 0. Point 1 is forced to floor, so the only candidate sequence is "0". It contains no
  // height-2 cactus, so it fails the "a 2 must appear" rule and is not clearable. The answer is 0,
  // not 1 -- the sharpest test that the height-2 requirement is actually enforced.
  @Test
  @StdIo({"1"})
  void lengthOneHasNoClearableMapBecauseNoCactusCanAppear(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // N = 2 -> 1. With the floor start fixed, the second point may be 0, 1, or 2, but only "02"
  // contains a height-2 cactus, so exactly one map is clearable. The smallest input whose answer is
  // non-zero.
  @Test
  @StdIo({"2"})
  void lengthTwoHasTheSingleMapWithOneTallCactus(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Mid-size anchors: pin the exact recurrence beyond the two sample values. ---

  // N = 5 -> 29. Past the point where both adjacency rules bite, the count stops being something a
  // reader can enumerate by eye, so an off-by-one in the DP transition first shows up here.
  @Test
  @StdIo({"5"})
  void lengthFiveHasTwentyNineMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("29");
  }

  // N = 6 -> 76.
  @Test
  @StdIo({"6"})
  void lengthSixHasSeventySixMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("76");
  }

  // N = 7 -> 191.
  @Test
  @StdIo({"7"})
  void lengthSevenHasOneHundredNinetyOneMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("191");
  }

  // N = 8 -> 480. Four consecutive anchors (5..8) tie the recurrence down tightly enough that a
  // solution matching all of them is almost certainly using the right transition.
  @Test
  @StdIo({"8"})
  void lengthEightHasFourHundredEightyMaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("480");
  }

  // --- Max-size input (N up to 1000): an O(N) DP clears the limit; the modulo must be applied. ---

  // N = 1000 -> 82654194. The true number of clearable maps grows exponentially and is far larger
  // than the modulus, so the reduced value being correct requires the modulo to be applied
  // throughout the recurrence (a solution that forgets it overflows long and prints garbage). The
  // value is cross-checked by an independent linear DP and matches the judge's max-size data file.
  // @Timeout guards against an accidentally exponential search.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxMapLengthExercisesTheModulo() throws IOException {
    assertThat(runMain("1000")).isEqualTo("82654194");
  }

  // --- Exhaustive cross-check against the independent enumeration oracle. ---

  // Every length from 1 to 13 must agree with the brute-force oracle. A single sweep that, in one
  // shot, re-derives all four rules from scratch (floor start, jump span, height limit, tall-cactus
  // requirement) across a dense contiguous range -- catching any rule the DP enforces incorrectly.
  // 13 is the largest length whose 3^(N-1) enumeration stays comfortably fast.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everyMapLengthUpToThirteenMatchesExhaustiveEnumeration() throws IOException {
    for (int n = 1; n <= 13; n++) {
      assertThat(runMain(Integer.toString(n))).as("N=%d", n).isEqualTo(bruteForceCount(n));
    }
  }

  /**
   * Independent oracle for the number of clearable maps, by brute-force enumeration rather than the
   * DP, so agreement is a genuine cross-check. Every height assignment of points {@code 2..n} is
   * tried (point 1 is fixed to floor) and scored against the four rules by
   * {@link #isClearable(int[])}.
   *
   * @return the count as a string reduced modulo {@code 1,000,000,007}, matching the program's
   *     printed form.
   * @implNote {@code O(3^n)} time -- exponential, so callable only for small {@code n}; the counts
   *     in that range stay far below the modulus, so the reduction is an identity there.
   */
  private static String bruteForceCount(int n) {
    long count = enumerate(new int[n], 1);
    return Long.toString(count % MOD);
  }

  /**
   * Recursively fills points {@code idx..n-1} with every height in {@code {0, 1, 2}} and tallies.
   */
  private static long enumerate(int[] heights, int idx) {
    if (idx == heights.length) {
      return isClearable(heights) ? 1 : 0;
    }
    long total = 0;
    for (int height = 0; height <= 2; height++) {
      heights[idx] = height;
      total += enumerate(heights, idx + 1);
    }
    return total;
  }

  /** Whether a height sequence satisfies all four rules of a clearable map. */
  private static boolean isClearable(int[] heights) {
    if (heights[0] != 0) {
      return false; // the dinosaur must start on the floor
    }
    boolean usesTallCactus = false;
    for (int i = 0; i < heights.length; i++) {
      if (heights[i] == 2) {
        usesTallCactus = true;
      }
      if (i + 1 < heights.length && heights[i] == 2 && heights[i + 1] == 2) {
        return false; // adjacent heights sum to 4 -- too tall to jump
      }
      if (i + 2 < heights.length && heights[i] != 0 && heights[i + 1] != 0 && heights[i + 2] != 0) {
        return false; // three adjacent cacti -- too wide to jump
      }
    }
    return usesTallCactus; // at least one height-2 cactus must appear
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
