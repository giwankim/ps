package boj.boj8983;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 8983 사냥꾼 (Hunter, KOI 2013) -- count how many animals a hunter can shoot, solved by binary
 * search over the sorted shooting points, a nearest-neighbor sibling of
 * {@code boj2110}/{@code boj20033}'s "binary search" family.
 *
 * <p>{@code M} shooting points sit at distinct {@code x}-coordinates {@code x1, ..., xM} on a line;
 * {@code N} animals live at points {@code (a, b)}. The hunter's gun has range {@code L}, and the
 * distance from a shooting point {@code xi} to an animal {@code (a, b)} is defined as {@code |xi -
 * a| + b} -- the horizontal gap plus the animal's height. An animal is huntable iff <em>some</em>
 * shooting point lies within range, i.e. {@code min over i of (|xi - a| + b) <= L}. Output the
 * count of huntable animals.
 *
 * <p>The whole problem collapses to one observation: for a <em>fixed</em> animal {@code b} is
 * constant across every point, so the distance {@code |xi - a| + b} is minimized by the point
 * nearest to {@code a} in {@code x}. Sort the points once, then for each animal binary-search the
 * insertion position of {@code a} and test the point just below and just above it -- the two
 * candidates for "nearest" -- against {@code L}. That is the intended {@code O(N log M)} solution;
 * the naive all-pairs scan is {@code O(N * M)} and times out at the 100,000x100,000 ceiling.
 *
 * <p>Input: line 1 is {@code "M N L"} -- the shooting-point count {@code M} ({@code 1 <= M <=
 * 100,000}), the animal count {@code N} ({@code 1 <= N <= 100,000}), and the range {@code L}
 * ({@code 1 <= L <= 1,000,000,000}). Line 2 holds the {@code M} shooting-point
 * {@code x}-coordinates, space-separated, distinct, and <em>in arbitrary order</em> (so the
 * solution must sort them). The next {@code N} lines each hold an animal's {@code "a b"} (its
 * {@code x} then its {@code y}). All coordinates are positive integers {@code <= 1,000,000,000}.
 * Output: the single huntable count, a non-negative integer.
 *
 * <p>The spec and both worked samples were recovered from the oj.uz mirror of the KOI statement and
 * cross-checked against the BOJ problem page; the two published samples ({@code 4 8 4 / 6 1 4 9 /
 * ... -> 6} and {@code 1 5 3 / 3 / ... -> 5}) are pinned by
 * {@link #officialSampleOneCountsSixHuntableAnimals} and
 * {@link #officialSampleTwoCatchesEveryAnimalFromOnePoint}; every other expected value is
 * hand-derived from the distance rule above.
 *
 * <p>The fixtures pin the things a hasty nearest-point search gets wrong:
 *
 * <ul>
 *   <li><b>The height counts toward the distance.</b> {@code b} is added to the horizontal gap, so
 *       an animal sitting directly on a point is still out of reach if it is taller than {@code L}
 *       ({@link #distanceAddsTheAnimalsHeight}).
 *   <li><b>The range bound is inclusive.</b> Distance exactly {@code L} is huntable; one more is
 *       not ({@link #animalExactlyAtRangeIsHuntable}, {@link #animalJustBeyondRangeIsNotHuntable}).
 *   <li><b>Both neighbors of the search position must be checked.</b> The nearest point can be the
 *       one just below {@code a} ({@link #nearestPointMayBeToTheLeft}) or just above it
 *       ({@link #nearestPointMayBeToTheRight}); checking only one side misprints.
 *   <li><b>The edges of the point array.</b> An animal left of every point has only a right
 *       neighbor ({@link #animalLeftOfAllPointsUsesTheLeftmostPoint}); one right of every point has
 *       only a left neighbor ({@link #animalRightOfAllPointsUsesTheRightmostPoint}) -- neither may
 *       read off the end of the array.
 *   <li><b>Sort the points first.</b> They arrive shuffled, so a binary search over the raw input
 *       order navigates to the wrong neighbor ({@link #unsortedPointsAreSortedBeforeSearching}).
 *   <li><b>The aggregate is a count.</b> Zero when nothing is in range
 *       ({@link #noAnimalIsHuntableWhenAllExceedRange}), {@code N} when everything is
 *       ({@link #everyAnimalIsHuntableAcrossMultiplePoints}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N log M)} search against
 * the {@code O(N * M)} all-pairs walk at the 100,000x100,000 ceiling, and
 * {@link #randomizedInputsMatchBruteForceOracle} cross-checks the judged output against a brute
 * force that tests every (animal, point) pair directly -- the literal definition of the problem,
 * sharing no sort-and-binary-search logic with a judge solution. The {@code Main} under test is an
 * empty stub that reads nothing and prints nothing, so every assertion here is RED until the search
 * is implemented.
 */
class MainTest {

  // --- Official sample 1: four points 6 1 4 9 (note: unsorted), eight animals, range 4. Sorted
  // points are {1,4,6,9}. Six animals are reachable; the two misses are (4,5) -- height 5 already
  // exceeds L=4 -- and (8,4), whose nearest point is 9 at distance |9-8|+4 = 5 > 4. The end-to-end
  // smoke test, and already enough to demand sorting and a mix of hit/miss animals. ---

  @Test
  @StdIo({"4 8 4", "6 1 4 9", "7 2", "3 3", "4 5", "5 1", "2 2", "1 4", "8 4", "9 4"})
  void officialSampleOneCountsSixHuntableAnimals(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Official sample 2: a single point at x=3, range 3, five animals -- every one lands at
  // distance exactly 3 (e.g. (2,2) -> 1+2, (1,1) -> 2+1, (5,1) -> 2+1, (3,3) -> 0+3), so all five
  // are huntable. Pins the M=1 case and the inclusive boundary all at once. ---

  @Test
  @StdIo({"1 5 3", "3", "2 2", "1 1", "5 1", "4 2", "3 3"})
  void officialSampleTwoCatchesEveryAnimalFromOnePoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- The height is part of the distance. One point at x=5, one animal sitting directly on it at
  // (5,10) with range 9: the horizontal gap is 0 but the distance is 0+10 = 10 > 9, so the animal
  // is
  // out of reach. A solution that forgets to add b would wrongly count it. Answer 0. ---

  @Test
  @StdIo({"1 1 9", "5", "5 10"})
  void distanceAddsTheAnimalsHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The range bound is inclusive: distance exactly L is a hit. Point x=5, animal (5,3), range 3
  // -> distance 0+3 = 3 <= 3, huntable. Answer 1. ---

  @Test
  @StdIo({"1 1 3", "5", "5 3"})
  void animalExactlyAtRangeIsHuntable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- One unit past the bound is a miss. The mirror of the previous case: point x=5, animal
  // (5,4),
  // range 3 -> distance 0+4 = 4 > 3, not huntable. Together these pin the comparison as `<=`, not
  // `<`. Answer 0. ---

  @Test
  @StdIo({"1 1 3", "5", "5 4"})
  void animalJustBeyondRangeIsNotHuntable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The nearest point is the one *below* the animal's x. Points {1,10}, animal (3,2), range 4:
  // the left point 1 reaches it (|1-3|+2 = 4 <= 4) while the right point 10 does not (|10-3|+2 =
  // 9).
  // The insertion position of a=3 in {1,10} points at index 1 (value 10, the upper neighbor), so a
  // search that only tests that upper neighbor misses the real nearest point. Answer 1. ---

  @Test
  @StdIo({"2 1 4", "1 10", "3 2"})
  void nearestPointMayBeToTheLeft(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The mirror: the nearest point is the one *above* the animal's x. Points {1,10}, animal
  // (8,2), range 4: the right point 10 reaches it (|10-8|+2 = 4 <= 4) while the left point 1 does
  // not
  // (|1-8|+2 = 9). A search that only tests the lower neighbor misses here. Paired with the
  // previous
  // test, this forces *both* neighbors to be examined. Answer 1. ---

  @Test
  @StdIo({"2 1 4", "1 10", "8 2"})
  void nearestPointMayBeToTheRight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- An animal to the left of every point: only a right neighbor exists. Points {5,9}, animal
  // (2,1), range 4 -> nearest is 5 at distance |5-2|+1 = 4 <= 4, huntable. The insertion position
  // is
  // index 0, so there is no lower neighbor to read; a search must not index -1. Answer 1. ---

  @Test
  @StdIo({"2 1 4", "5 9", "2 1"})
  void animalLeftOfAllPointsUsesTheLeftmostPoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- An animal to the right of every point: only a left neighbor exists. Points {5,9}, animal
  // (12,1), range 4 -> nearest is 9 at distance |9-12|+1 = 4 <= 4, huntable. The insertion position
  // is M (past the end), so a search must step back to index M-1 rather than read off the array.
  // The opposite edge from the previous test. Answer 1. ---

  @Test
  @StdIo({"2 1 4", "5 9", "12 1"})
  void animalRightOfAllPointsUsesTheRightmostPoint(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- A single point and a far-off animal: points {5}, animal (100,1), range 10 -> distance
  // |5-100|+1 = 96 > 10, not huntable. The minimal "nothing in range" case, and a guard that a lone
  // point out of reach yields 0 rather than a stray count. Answer 0. ---

  @Test
  @StdIo({"1 1 10", "5", "100 1"})
  void animalBeyondTheSolePointIsNotHuntable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Points arrive unsorted and must be sorted before searching. Points {9,1,6,4} (sorted
  // {1,4,6,9}), animal (3,3), range 4: only point 4 reaches it (|4-3|+3 = 4 <= 4; 1 -> 5, 6 -> 6, 9
  // -> 9). A binary search over the *raw* order {9,1,6,4} navigates by value comparisons that the
  // unsorted array does not satisfy, landing on the wrong neighbor. Answer 1. ---

  @Test
  @StdIo({"4 1 4", "9 1 6 4", "3 3"})
  void unsortedPointsAreSortedBeforeSearching(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Nothing is in range: two points {5,20}, range 2, three animals -- (5,5) is too tall (0+5 >
  // 2), (12,1) is too far from both (|5-12|+1 = 8, |20-12|+1 = 9), (100,2) is far off the right
  // (|20-100|+2 = 82). The count floors at 0. ---

  @Test
  @StdIo({"2 3 2", "5 20", "5 5", "12 1", "100 2"})
  void noAnimalIsHuntableWhenAllExceedRange(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Everything is in range: three points {1,10,20}, range 2, three animals each sitting on or
  // beside a different point -- (1,1) -> 0+1, (10,2) -> 0+2, (21,1) -> |20-21|+1 = 2. All three are
  // huntable, so the count tops out at N. Pins that each animal is counted independently against
  // its
  // own nearest point. Answer 3. ---

  @Test
  @StdIo({"3 3 2", "1 10 20", "1 1", "10 2", "21 1"})
  void everyAnimalIsHuntableAcrossMultiplePoints(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Values are split on "one or more spaces": the header "1  1  3" and the animal line "2  2"
  // each carry doubled blanks. A parser that tokenizes on whitespace runs (StringTokenizer / split
  // on \\s+) reads them fine; one that splits on a single literal space reads empty tokens and
  // breaks. Point x=3, animal (2,2), range 3 -> |3-2|+2 = 3 <= 3, huntable. Answer 1. ---

  @Test
  @StdIo({"1  1  3", "3", "2  2"})
  void multipleSpacesBetweenValuesAreTolerated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The far end of the coordinate range: a point at x=1 and an animal at (1,000,000,000, 1)
  // with
  // range 1,000,000,000. The distance is |1 - 1000000000| + 1 = 999,999,999 + 1 = 1,000,000,000,
  // exactly the range -- huntable. Pins that 10-digit coordinates are read without truncation and
  // that the near-billion horizontal gap is computed correctly at the inclusive bound. Answer 1.
  // ---

  @Test
  @StdIo({"1 1 1000000000", "1", "1000000000 1"})
  void tenDigitCoordinatesAtTheRangeLimit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Upper bounds on M and N: 100,000 points at coordinates 1..100,000 and 100,000 animals, the
  // i-th sitting on point i at (i, 1) with range 1 -- each animal is exactly distance 1 from its
  // own
  // point, so all 100,000 are huntable. The O(N log M) search runs ~17 probes per animal (~1.7e6
  // ops); the O(N * M) all-pairs walk would do 1e10 and never finish. Doubles as a parse stress at
  // scale. Answer 100000. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int m = 100_000;
    int n = 100_000;
    int[] points = new int[m];
    for (int i = 0; i < m; i++) {
      points[i] = i + 1; // coordinates 1..100000, distinct
    }
    int[][] animals = new int[n][2];
    for (int i = 0; i < n; i++) {
      animals[i][0] = i + 1; // animal i sits on point i+1
      animals[i][1] = 1; // height 1, so distance to its own point is exactly 1
    }
    assertThat(runMain(hunterInput(points, animals, 1))).isEqualTo("100000");
  }

  // --- Small random instances checked against the brute-force definition of the problem. Up to 8
  // distinct points and 8 distinct animals on a [1,30] x [1,20] field with range in [1,20] --
  // ranges
  // tight enough that hits and misses both occur often. The oracle tests every (animal, point) pair
  // directly and counts the animals with at least one in-range point, the literal problem
  // definition, sharing no sort-or-binary-search logic with a judge solution, so the two agree only
  // when both are right. ---

  @Test
  void randomizedInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(8983);
    for (int trial = 0; trial < 500; trial++) {
      int m = 1 + rnd.nextInt(8); // M in [1, 8]
      int n = 1 + rnd.nextInt(8); // N in [1, 8]
      int[] points = distinctPoints(rnd, m, 30); // distinct x in [1, 30]
      int[][] animals = distinctAnimals(rnd, n, 30, 20); // distinct (x in [1,30], y in [1,20])
      int l = 1 + rnd.nextInt(20); // L in [1, 20]
      String expected = Integer.toString(bruteForceCount(points, animals, l));
      assertThat(runMain(hunterInput(points, animals, l)))
          .as("points=%s animals=%s L=%d", Arrays.toString(points), Arrays.deepToString(animals), l)
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: the brute-force definition of the huntable count. For each animal it scans
   * every shooting point and counts the animal if any point is within range, i.e. {@code |x - a| +
   * b <= L}. Uses neither sorting nor binary search, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(N * M)} time, where {@code N} is {@code animals.length} (the animal count)
   *     and {@code M} is {@code points.length} (the shooting-point count) -- acceptable only
   *     because the randomized fixtures keep both small. Distances are accumulated in {@code long}
   *     so the oracle itself never overflows, independent of how a judge solution sizes its
   *     arithmetic.
   */
  private static int bruteForceCount(int[] points, int[][] animals, int l) {
    int count = 0;
    for (int[] animal : animals) {
      for (int x : points) {
        long distance = Math.abs((long) x - animal[0]) + animal[1];
        if (distance <= l) {
          count++;
          break; // one in-range point is enough; move to the next animal
        }
      }
    }
    return count;
  }

  /**
   * Draws {@code m} distinct shooting-point coordinates from {@code [1, bound]} in random order via
   * a partial Fisher-Yates shuffle -- distinct (no overlapping points) and unsorted (to exercise
   * the sort).
   */
  private static int[] distinctPoints(Random rnd, int m, int bound) {
    int[] pool = new int[bound];
    for (int i = 0; i < bound; i++) {
      pool[i] = i + 1; // coordinates are positive: 1..bound
    }
    for (int i = bound - 1; i > 0; i--) {
      int j = rnd.nextInt(i + 1);
      int tmp = pool[i];
      pool[i] = pool[j];
      pool[j] = tmp;
    }
    return Arrays.copyOf(pool, m);
  }

  /**
   * Draws {@code n} distinct animal positions, each with {@code x} in {@code [1, xBound]} and
   * {@code y} in {@code [1, yBound]} -- distinct positions, matching the statement's "no
   * overlapping animals" guarantee.
   */
  private static int[][] distinctAnimals(Random rnd, int n, int xBound, int yBound) {
    Set<Long> seen = new HashSet<>();
    int[][] animals = new int[n][2];
    int placed = 0;
    while (placed < n) {
      int x = 1 + rnd.nextInt(xBound);
      int y = 1 + rnd.nextInt(yBound);
      if (seen.add((long) x * (yBound + 1) + y)) { // pack (x,y) into one key
        animals[placed][0] = x;
        animals[placed][1] = y;
        placed++;
      }
    }
    return animals;
  }

  /**
   * Renders the instance as BOJ 8983 input: {@code "M N L"} on line 1, the {@code M} point
   * coordinates space-separated on line 2, then one {@code "a b"} animal per line.
   */
  private static String hunterInput(int[] points, int[][] animals, int l) {
    StringBuilder sb = new StringBuilder();
    sb.append(points.length)
        .append(' ')
        .append(animals.length)
        .append(' ')
        .append(l)
        .append('\n');
    for (int i = 0; i < points.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(points[i]);
    }
    sb.append('\n');
    for (int[] animal : animals) {
      sb.append(animal[0]).append(' ').append(animal[1]).append('\n');
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
