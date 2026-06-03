package boj.boj3020;

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
 * BOJ 3020 개똥벌레 (Firefly) -- COCI '06 Regional #3, prefix/suffix counting over levels.
 *
 * <p>A cave is {@code N} metres long ({@code N} even) and {@code H} metres high, filled with
 * {@code N} obstacles standing in columns. The first obstacle is a <em>stalagmite</em> (grows up
 * from the floor); thereafter stalactites (hang down from the ceiling) and stalagmites alternate. A
 * firefly picks one of the {@code H} integer levels {@code 1..H} and flies straight through,
 * destroying every obstacle that spans that level. Report the minimum number of obstacles it must
 * destroy and how many distinct levels achieve that minimum.
 *
 * <p><b>Reach (the inclusive boundaries are the whole problem).</b> A stalagmite of size {@code s}
 * occupies floor levels {@code 1..s}, so it blocks level {@code h} iff {@code h <= s}. A stalactite
 * of size {@code s} occupies ceiling levels {@code H-s+1..H}, so it blocks level {@code h} iff
 * {@code h >= H-s+1}. Both endpoints are inclusive; the {@code H-s+1} mapping is the classic
 * off-by-one. {@link #stalagmiteBlocksExactlyUpToItsSize(StdOut)} and
 * {@link #stalactiteBlocksExactlyDownToHMinusSizePlusOne(StdOut)} pin these two edges.
 *
 * <p>Input: line 1 is {@code "N H"}; the next {@code N} lines each give one obstacle's size, in
 * column order. Output: a single line {@code "min count"} -- the minimum obstacle count over all
 * levels and the number of levels attaining it.
 *
 * <p>Constraints (official statement): {@code 2 <= N <= 200,000} with {@code N} even; {@code 2 <= H
 * <= 500,000}; every size is a positive integer strictly less than {@code H} (so a stalagmite never
 * touches the ceiling and a stalactite never touches the floor); time limit 1 s, memory 128 MB.
 *
 * <p><b>Why O(N + H), not O(N * H).</b> Scanning all {@code H} levels for each of the {@code N}
 * obstacles is {@code O(N * H) = 10^11} in the worst case and times out. The intended solution
 * tallies, per size, how many stalagmites/stalactites have that size, then takes a suffix sum over
 * the stalagmite tally ({@code floor[h] = #stalagmites with size >= h}) and a prefix sum over the
 * stalactite tally ({@code ceil[h] = #stalactites reaching down to h}); each level's total is
 * {@code floor[h] + ceil[h]}. {@link #allUnitObstaclesAtMaxScaleLeaveAClearBand()} guards the time
 * bound and the {@code H}-sized array allocation.
 *
 * <p><b>No overflow trap (contrast with the prefix-sum siblings BOJ 2015/2143/25332).</b> The count
 * at a single level is at most {@code N = 200,000}, and the number of tying levels is at most
 * {@code H = 500,000}; both fit a signed 32-bit {@code int} comfortably (limit
 * {@code 2,147,483,647}), so no {@code long} is required.
 * {@link #fullOverlapAtMaxScalePushesPerLevelCountToHalfN()} drives the per-level count to its
 * {@code N/2} extreme to document that bound.
 *
 * <p><b>Why there is no parity test.</b> The statement stresses that the first obstacle is always a
 * stalagmite, which looks like a trap. It is not observable in the output: reflecting the cave top
 * to bottom ({@code h -> H+1-h}) is a bijection on levels that turns each stalagmite of size
 * {@code s} into a stalactite of the same size, so flipping which family is "floor" merely reverses
 * the per-level count histogram. The minimum and its multiplicity are invariant, so an
 * implementation that assigns the wrong parity still prints the correct answer -- no test can (or
 * should) catch it. The suite therefore exercises the reach boundaries, the {@code min == 0} clear
 * path, and the tie count instead.
 */
class MainTest {

  // --- The two published samples, verbatim from the official statement. ---

  // Sample 1: N=6, H=7, columns [1,5,3,3,5,1]. Stalagmites (cols 1,3,5) = {1,3,5}; stalactites
  // (cols 2,4,6) = {5,3,1}. Per-level totals over h=1..7 are [3,2,3,2,3,2,3]; the minimum 2 occurs
  // at levels 2, 4, 6 -> "2 3".
  @Test
  @StdIo({"6 7", "1", "5", "3", "3", "5", "1"})
  void officialSample1MinimumTwoOnThreeLevels(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3");
  }

  // Sample 2: N=14, H=5, the worked example from the figure. Per-level totals over h=1..5 are
  // [7,8,10,8,7]; the minimum 7 occurs at levels 1 and 5 (the 4th level destroys 8, as the
  // statement notes) -> "7 2".
  @Test
  @StdIo({"14 5", "1", "3", "4", "2", "2", "4", "3", "4", "3", "3", "3", "2", "3", "3"})
  void officialSample2MinimumSevenOnTwoLevels(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7 2");
  }

  // --- Smallest legal input (N = 2): exactly one stalagmite and one stalactite. ---

  // N=2, H=3, sizes [1,1]: the lone stalagmite blocks only level 1, the lone stalactite only level
  // 3 (h >= 3-1+1), leaving level 2 clear -> minimum 0 on a single level -> "0 1".
  @Test
  @StdIo({"2 3", "1", "1"})
  void smallestCaveLeavesAClearMiddleLevel(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 1");
  }

  // --- Inclusive-reach boundaries: each test isolates one off-by-one edge. ---

  // N=2, H=5, sizes [2,1]: the stalagmite of size 2 must block level 2 *exactly* (h <= s) and stop
  // there, so levels 3 and 4 are the only clear ones (the stalactite of size 1 blocks level 5).
  // Minimum 0 on levels 3,4 -> "0 2". An implementation using a strict h < s would wrongly free
  // level 2 as well and report "0 3".
  @Test
  @StdIo({"2 5", "2", "1"})
  void stalagmiteBlocksExactlyUpToItsSize(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 2");
  }

  // N=2, H=5, sizes [1,2]: the stalactite of size 2 reaches down to level H-s+1 = 4 *exactly*, so
  // levels 2 and 3 stay clear (the stalagmite of size 1 blocks level 1). Minimum 0 on levels 2,3 ->
  // "0 2". An implementation using h >= H-s (one level too low) would also block level 3 and report
  // "0 1".
  @Test
  @StdIo({"2 5", "1", "2"})
  void stalactiteBlocksExactlyDownToHMinusSizePlusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 2");
  }

  // --- Overlap forces a positive minimum and pins the shared boundary level. ---

  // N=2, H=3, sizes [2,2]: the stalagmite covers levels 1,2 and the stalactite covers levels 2,3,
  // so level 2 (the shared boundary) is hit by both. Totals [1,2,1]; minimum 1 at the two end
  // levels -> "1 2". Were either reach exclusive, level 2 would drop to a count of 1 and the
  // minimum would spread to all three levels ("1 3").
  @Test
  @StdIo({"2 3", "2", "2"})
  void overlappingObstaclesShareTheBoundaryLevel(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2");
  }

  // --- Tie counting across a non-symmetric histogram (several obstacles of each family). ---

  // N=4, H=6, columns [2,4,2,1]: stalagmites (cols 1,3) = {2,2} cover levels 1,2; stalactites
  // (cols 2,4) = {4,1} cover {3,4,5,6} and {6}. Totals over h=1..6 are [2,2,1,1,1,2] --
  // deliberately
  // not a palindrome -- so the minimum 1 occupies the middle band levels 3,4,5 -> "1 3". Guards the
  // occurrence count when the minimum sits away from the extremes.
  @Test
  @StdIo({"4 6", "2", "4", "2", "1"})
  void minimumCountedAcrossNonSymmetricHistogram(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 3");
  }

  // --- Scale + performance, driven through the stdin/stdout helper. ---

  // N = 200,000, H = 500,000, every size 1: the 100,000 stalagmites all pile onto level 1 and the
  // 100,000 stalactites all pile onto level H, leaving the entire band of levels 2..H-1 (that is
  // H-2 = 499,998 levels) untouched. Minimum 0 on 499,998 levels -> "0 499998". This is the
  // O(N * H) = 10^11 worst case that defeats a per-level rescan but is O(N + H) with cumulative
  // tallies; it also forces an H-sized array (index up to H) to be allocated.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void allUnitObstaclesAtMaxScaleLeaveAClearBand() throws IOException {
    int n = 200_000;
    int h = 500_000;
    int[] sizes = filled(n, 1);
    assertThat(runMain(buildInput(h, sizes))).isEqualTo("0 " + (h - 2));
  }

  // N = 200,000, H = 500,000, every size H-1 = 499,999: each stalagmite covers levels 1..H-1 and
  // each stalactite covers levels 2..H, so the interior levels 2..H-1 are hit by all N obstacles
  // while the two extreme levels are hit by only the N/2 obstacles of one family. Minimum
  // N/2 = 100,000 at levels 1 and H -> "100000 2". Drives the per-level count to its maximum
  // (N = 200,000 at interior levels) to confirm an int counter suffices.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void fullOverlapAtMaxScalePushesPerLevelCountToHalfN() throws IOException {
    int n = 200_000;
    int h = 500_000;
    int[] sizes = filled(n, h - 1);
    assertThat(runMain(buildInput(h, sizes))).isEqualTo((n / 2) + " 2");
  }

  // --- Randomised cross-check against an independent brute-force oracle. Short even lengths and
  // small heights with sizes spanning the full 1..H-1 range make clear bands, shared boundaries,
  // and multi-way ties frequent, catching reach off-by-ones and miscounts the hand cases might
  // miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(3020);
    for (int trial = 0; trial < 400; trial++) {
      int h = 2 + rnd.nextInt(7); // height 2..8
      int n = 2 * (1 + rnd.nextInt(6)); // even length 2..12
      int[] sizes = new int[n];
      for (int i = 0; i < n; i++) {
        sizes[i] = 1 + rnd.nextInt(h - 1); // positive and strictly less than H
      }
      String input = buildInput(h, sizes);
      String expected = bruteForceMinAndCount(h, sizes);
      assertThat(runMain(input))
          .as("H=%d sizes=%s", h, java.util.Arrays.toString(sizes))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: stamp every obstacle's full vertical extent onto a per-level counter, then
   * read off the minimum and how many levels attain it. Obviously correct, far too slow for the
   * judge, trustworthy for tiny inputs.
   *
   * @implNote {@code O(N * H)} time and {@code O(H)} space, where {@code N} is {@code sizes.length}
   *     and {@code H} is {@code height}; even-indexed columns are stalagmites (floor), odd-indexed
   *     are stalactites (ceiling), matching the statement's "first obstacle is a stalagmite" rule.
   */
  private static String bruteForceMinAndCount(int height, int[] sizes) {
    int[] count = new int[height + 1]; // levels 1..height
    for (int i = 0; i < sizes.length; i++) {
      int s = sizes[i];
      if (i % 2 == 0) {
        for (int level = 1; level <= s; level++) { // stalagmite occupies floor levels 1..s
          count[level]++;
        }
      } else {
        for (int level = height - s + 1; level <= height; level++) { // stalactite: H-s+1..H
          count[level]++;
        }
      }
    }
    int min = Integer.MAX_VALUE;
    for (int level = 1; level <= height; level++) {
      min = Math.min(min, count[level]);
    }
    int levels = 0;
    for (int level = 1; level <= height; level++) {
      if (count[level] == min) {
        levels++;
      }
    }
    return min + " " + levels;
  }

  /** Builds BOJ 3020 input: {@code "N H"}, then each obstacle size on its own line, in order. */
  private static String buildInput(int height, int[] sizes) {
    StringBuilder sb = new StringBuilder();
    sb.append(sizes.length).append(' ').append(height).append('\n');
    for (int size : sizes) {
      sb.append(size).append('\n');
    }
    return sb.toString();
  }

  private static int[] filled(int length, int value) {
    int[] arr = new int[length];
    java.util.Arrays.fill(arr, value);
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
