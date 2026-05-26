package boj.boj15961;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15961 회전 초밥 (Rotating Sushi).
 *
 * <p>A circular belt holds N plates of sushi; type c is handed out as a coupon. Eating any k
 * consecutive plates (the belt wraps) yields {@code distinct(window) + (window contains c ? 0 : 1)}
 * kinds, because the free coupon sushi adds a new kind only when type c is not already in the
 * window. The answer is the maximum of that value over all N circular windows.
 *
 * <p>Constraints: 2 ≤ N ≤ 3,000,000, 2 ≤ d ≤ 3,000, 2 ≤ k ≤ 3,000 (k ≤ N), 1 ≤ c ≤ d, each plate in
 * 1..d.
 */
class MainTest {

  // --- Official sample 1. k = 4, coupon type 30. The window (2,7,9,25) has 4 distinct kinds and
  // does NOT contain 30, so the coupon adds a 5th kind -- beating every window that already holds
  // 30 (which top out at 4). The defining case for the conditional +1. ---

  @Test
  @StdIo({"8 30 4 30", "7", "9", "7", "30", "2", "7", "9", "25"})
  void officialSampleOneCouponAddsNewKind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("5");
  }

  // --- Official sample 2. Same belt reshuffled, k = 4, coupon type 7. Type 7 sits inside every
  // 4-distinct window, so the coupon is always wasted and the answer stays 4. Pairs with sample 1
  // to show the coupon branch flipping the result. ---

  @Test
  @StdIo({"8 50 4 7", "2", "7", "9", "25", "7", "9", "7", "30"})
  void officialSampleTwoCouponAlreadyPresent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Every plate is the same type and the coupon names a different type: each window has 1
  // distinct kind and never contains c, so the coupon always adds one -> answer 2. Exercises the
  // "coupon helps" branch in its purest form. ---

  @Test
  @StdIo({"5 3 3 2", "1", "1", "1", "1", "1"})
  void allPlatesIdenticalCouponAddsDifferentKind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("2");
  }

  // --- Every plate is the same type and the coupon names THAT type: the coupon is a duplicate of
  // what is already on the belt, so it adds nothing -> answer 1. The global minimum result, and the
  // mirror of the test above on the same belt. ---

  @Test
  @StdIo({"5 3 3 1", "1", "1", "1", "1", "1"})
  void allPlatesIdenticalCouponMatchesThatKind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("1");
  }

  // --- Smallest possible window (k = 2). Alternating 1,2,1,2 with coupon type 3 (never present):
  // any pair holds 2 distinct kinds and the coupon adds a 3rd -> answer 3 = k + 1, the theoretical
  // ceiling for k = 2. ---

  @Test
  @StdIo({"4 3 2 3", "1", "2", "1", "2"})
  void minimumWindowOfTwoReachesKPlusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("3");
  }

  // --- The window with all distinct plates AND no coupon kind hits the absolute ceiling k + 1.
  // Four
  // distinct plates, k = 3, coupon type 5 (never on the belt): every window has 3 distinct kinds
  // and
  // the coupon always adds one -> answer 4. Guards against capping the result at k. ---

  @Test
  @StdIo({"4 5 3 5", "1", "2", "3", "4"})
  void couponReachesKPlusOneWhenWindowAllDistinctAndCouponAbsent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- k = N: the customer eats the entire belt, so all N circular windows are the same full set.
  // Belt {1,2,3,1} has 3 distinct kinds; coupon type 4 is absent -> 3 + 1 = 4. Confirms the k = N
  // boundary still applies the coupon. ---

  @Test
  @StdIo({"4 4 4 4", "1", "2", "3", "1"})
  void wholeBeltWhenKEqualsNCouponAbsent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- k = N again, same belt, but coupon type 2 IS in the full set, so it adds nothing -> answer
  // 3. The k = N counterpart to the test above, isolating the coupon branch when the window is the
  // whole belt. ---

  @Test
  @StdIo({"4 4 4 2", "1", "2", "3", "1"})
  void wholeBeltWhenKEqualsNCouponPresent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("3");
  }

  // --- The optimal window straddles the belt's end. Belt 3,1,1,1,1,2 with k = 3 and coupon type 1
  // (present in every window, so no bonus ever applies). The only 3-distinct windows are
  // (1,2,3) = positions 4,5,0 and (2,3,1) = positions 5,0,1; both wrap past the end. Non-wrapping
  // windows max out at 2. Guards the circular (i + k) % N indexing. ---

  @Test
  @StdIo({"6 3 3 1", "3", "1", "1", "1", "1", "2"})
  void bestWindowWrapsAroundBelt(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("3");
  }

  // --- The coupon, not the raw distinct count, decides the winner. Belt 4,1,2,1,2,3 with k = 3 and
  // coupon type 4: the window (1,2,3) has 3 distinct kinds and omits 4 -> 4, beating window
  // (4,1,2) which also has 3 distinct kinds but already holds 4 -> 3. Two equal distinct counts,
  // split by the coupon. ---

  @Test
  @StdIo({"6 4 3 4", "4", "1", "2", "1", "2", "3"})
  void couponDecidesBetweenEqualDistinctWindows(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Duplicates inside a window must not inflate the distinct count. Belt 1,1,2,2,3 with k = 4:
  // the window (1,1,2,2) packs 4 plates but only 2 distinct kinds (+1 coupon type 3 -> 3), tying
  // the 3-distinct windows rather than beating them -> answer 3. Catches counting plates instead of
  // kinds. ---

  @Test
  @StdIo({"5 3 4 3", "1", "1", "2", "2", "3"})
  void duplicatesWithinWindowDoNotInflateDistinct(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("3");
  }

  // --- Smallest legal belt (N = k = 2) where the coupon helps: both plates are type 1, coupon type
  // 2 is absent -> 1 + 1 = 2. Exercises the lower bound of every constraint at once. ---

  @Test
  @StdIo({"2 2 2 2", "1", "1"})
  void minimumSizedBeltCouponAddsKind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("2");
  }

  // --- Smallest legal belt where nothing helps: both plates are type 1 and the coupon is also type
  // 1 -> answer 1. The minimal input producing the global minimum output. ---

  @Test
  @StdIo({"2 2 2 1", "1", "1"})
  void minimumSizedBeltAllOneKind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("1");
  }

  // --- A larger hand-computed belt so a wrong sliding-window update stands out. Belt
  // 1,2,3,1,2,4,1,2,3,1 with k = 5 and coupon type 4: several windows reach 4 distinct kinds, and
  // every window either already holds 4 distinct kinds or omits the coupon type 4 to gain one --
  // both routes cap at 4 -> answer 4. ---

  @Test
  @StdIo({"10 5 5 4", "1", "2", "3", "1", "2", "4", "1", "2", "3", "1"})
  void largerHandComputedBelt(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(linesOf(out)).containsExactly("4");
  }

  // --- Performance at the maximum belt size, single kind. N = 3,000,000, k = 3,000, every plate
  // type 1, coupon type 2 (absent) -> every window is {1} and the coupon adds one -> answer 2. An
  // O(N*k) scan (~9*10^9 ops) blows the 1s judge limit; the intended O(N) sliding window is
  // instant.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeSingleKindBeltAvoidsBruteForce() throws IOException {
    assertThat(runMain(constantBeltInput(3_000_000, 3_000, 3_000, 2, 1))).isEqualTo("2");
  }

  // --- Performance with full window churn at scale. N = 3,000,000 plates cycling 1..3,000 with
  // k = 3,000 and coupon type 1: every length-3,000 window covers exactly one full period, i.e. all
  // 3,000 kinds, and the coupon is always present -> answer 3,000. Forces the frequency bookkeeping
  // to add and drop kinds correctly across the whole belt, not just on a constant input. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largePeriodicBeltCoversEveryKind() throws IOException {
    assertThat(runMain(periodicBeltInput(3_000_000, 3_000, 3_000, 1, 3_000))).isEqualTo("3000");
  }

  // --- Randomized cross-check against an independent O(N*k) oracle. Small belts and alphabets make
  // duplicates, wrap-around optima, and both coupon branches frequent, with k sampled across its
  // full 2..N range. Catches add/drop, boundary, and off-by-one window bugs the hand cases miss.
  // ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(42);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(11); // 2..12 plates
      int d = 2 + rnd.nextInt(5); // 2..6 kinds
      int k = 2 + rnd.nextInt(n - 1); // 2..n
      int c = 1 + rnd.nextInt(d); // 1..d
      int[] plates = new int[n];
      for (int i = 0; i < n; i++) {
        plates[i] = 1 + rnd.nextInt(d);
      }
      String input = inputFor(n, d, k, c, plates);
      String expected = Integer.toString(bruteForceMaxKinds(n, k, c, plates));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  // Reference O(N*k) solution: for every circular window count distinct kinds and add one when the
  // coupon type is absent. Obviously correct but too slow for the judge; trustworthy for tiny N.
  private static int bruteForceMaxKinds(int n, int k, int c, int[] plates) {
    int best = 0;
    for (int start = 0; start < n; start++) {
      Set<Integer> seen = new HashSet<>();
      for (int j = 0; j < k; j++) {
        seen.add(plates[(start + j) % n]);
      }
      int kinds = seen.size() + (seen.contains(c) ? 0 : 1);
      best = Math.max(best, kinds);
    }
    return best;
  }

  private static String inputFor(int n, int d, int k, int c, int[] plates) {
    StringBuilder sb = new StringBuilder();
    sb.append(n)
        .append(' ')
        .append(d)
        .append(' ')
        .append(k)
        .append(' ')
        .append(c)
        .append('\n');
    for (int plate : plates) {
      sb.append(plate).append('\n');
    }
    return sb.toString();
  }

  private static String constantBeltInput(int n, int d, int k, int c, int plate) {
    StringBuilder sb = new StringBuilder(n * 2 + 16);
    sb.append(n)
        .append(' ')
        .append(d)
        .append(' ')
        .append(k)
        .append(' ')
        .append(c)
        .append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(plate).append('\n');
    }
    return sb.toString();
  }

  private static String periodicBeltInput(int n, int d, int k, int c, int period) {
    StringBuilder sb = new StringBuilder(n * 5 + 16);
    sb.append(n)
        .append(' ')
        .append(d)
        .append(' ')
        .append(k)
        .append(' ')
        .append(c)
        .append('\n');
    for (int i = 0; i < n; i++) {
      sb.append((i % period) + 1).append('\n');
    }
    return sb.toString();
  }

  private static String[] linesOf(StdOut out) {
    return out.capturedString().replace("\r\n", "\n").trim().split("\n");
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
