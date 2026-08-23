package atcoder.abc472.c;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 472 C -- On a Diet.
 *
 * <p>Line 1 holds N, M and K (1 &le; M &le; N &le; 2 x 10^5; 1 &le; K &le; 10^15); line 2 holds the
 * snack calories A1 ... AN (1 &le; Ai &le; 10^9). Walking the days in order i = 1, 2, ..., N,
 * Takahashi eats the snack on day i if and only if the calories of the snacks he has <em>actually
 * eaten</em> from day max(i - M + 1, 1) through day i -- counting Ai itself -- come to at most K.
 * Print N lines, {@code Yes} on day i if he eats it and {@code No} otherwise.
 *
 * <p>The rule is a sliding window of exactly M days over the eaten calories, and the tests below
 * separate the five ways that goes wrong.
 *
 * <ul>
 *   <li><b>Only eaten snacks are charged.</b> A refused snack contributes nothing, so a prefix sum
 *       over all of A is the wrong quantity ({@link #aSkippedSnackIsNotChargedToTheWindow}).
 *   <li><b>Ai is inside its own test.</b> The decision is made on the window <em>assuming</em> the
 *       snack is eaten, not on the window standing before it
 *       ({@link #theSnackUnderConsiderationCountsTowardTheTotal}).
 *   <li><b>The window spans exactly M days.</b> A snack is still charged on the Mth day after it
 *       ({@link #aSnackStillCountsOnTheLastDayOfItsWindow}) and free on the one after that
 *       ({@link #aSnackStopsCountingTheDayAfterItsWindow}); the day that leaves is the oldest one
 *       ({@link #theDayThatLeavesTheWindowIsTheOldestOne}), and it leaves every day rather than
 *       once ({@link #theWindowIsFreedRepeatedlyNotJustOnce}). Before day M the window start is
 *       clamped to day 1 ({@link #theEarlyWindowIsShorterThanM}).
 *   <li><b>"At most K" is inclusive.</b> Landing exactly on K still eats
 *       ({@link #aWindowLandingExactlyOnKIsStillEaten}).
 *   <li><b>The arithmetic is 64-bit.</b> K reaches 10^15, past an int outright, and even a
 *       three-snack window can wrap one ({@link #theCalorieTotalOutgrowsAnIntAccumulator}).
 * </ul>
 *
 * <p>Two shapes fall outside a fixed {@code @StdIo} string and are driven through
 * {@link #runMain(String)}: the N = 2 x 10^5 ceiling, where re-summing the window each day is O(N x
 * M) and cannot finish, and randomized trips cross-checked against {@link #oracle}, which
 * recomputes each window from the definition rather than carrying a running total.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"5 3 83", "48 73 59 90 21"})
  void officialSampleOneEatsOnlyTheOpeningAndClosingSnacks(StdOut out) throws IOException {
    // Day 3 is refused over day 1's 48 alone -- day 2 was never eaten, so it charges nothing.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nNo\nNo\nYes");
  }

  @Test
  @StdIo({"7 4 728", "187 816 349 609 255 308 175"})
  void officialSampleTwoAlternatesAcrossSevenDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nYes\nNo\nYes\nNo\nYes");
  }

  @Test
  @StdIo({
    "10 3 1368290936",
    "216519459 804733999 297250023 775422599 287963235 999315644 354987425 974810607 653940822"
        + " 117157941"
  })
  void officialSampleThreeHandlesNineDigitCalories(StdOut out) throws IOException {
    // Day 3's window reaches 1318503481, past what an int holds once day 4's 775422599 joins it.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim())
        .isEqualTo("Yes\nYes\nYes\nNo\nYes\nYes\nNo\nNo\nYes\nYes");
  }

  // --- Only the snacks actually eaten are charged to the window. ---

  @Test
  @StdIo({"3 3 11", "6 9 4"})
  void aSkippedSnackIsNotChargedToTheWindow(StdOut out) throws IOException {
    // Day 2's 9 is refused, so day 3 weighs 6 + 4 = 10 and fits. Summing A over the window
    // instead of the eaten calories weighs 6 + 9 + 4 = 19 and prints No.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nYes");
  }

  // --- The snack under consideration is inside its own test. ---

  @Test
  @StdIo({"2 2 10", "10 1"})
  void theSnackUnderConsiderationCountsTowardTheTotal(StdOut out) throws IOException {
    // The window standing before day 2 is 10, which is already at K: testing that instead of
    // 10 + 1 prints Yes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo");
  }

  // --- "At most K" is inclusive. ---

  @Test
  @StdIo({"2 2 100", "40 60"})
  void aWindowLandingExactlyOnKIsStillEaten(StdOut out) throws IOException {
    // 40 + 60 is K exactly, so a strict {@code < K} guard prints No on day 2.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes");
  }

  @Test
  @StdIo({"2 2 100", "40 61"})
  void aWindowOneCalorieOverKIsRefused(StdOut out) throws IOException {
    // The other side of the same threshold, one calorie along.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo");
  }

  // --- The window spans exactly M days: [i - M + 1, i]. ---

  @Test
  @StdIo({"4 4 10", "10 1 1 1"})
  void aSnackStillCountsOnTheLastDayOfItsWindow(StdOut out) throws IOException {
    // Day 1 fills K by itself and day 4 is the last day it reaches, so nothing else is ever
    // eaten. A window one day too short frees it on day 4 and prints Yes there.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nNo\nNo");
  }

  @Test
  @StdIo({"4 3 10", "10 1 1 1"})
  void aSnackStopsCountingTheDayAfterItsWindow(StdOut out) throws IOException {
    // The same trip with M one smaller: day 4's window opens on day 2, so day 1's 10 is gone and
    // the snack fits. A window one day too long holds onto it and prints No.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nNo\nYes");
  }

  @Test
  @StdIo({"4 2 10", "3 7 4 1"})
  void theDayThatLeavesTheWindowIsTheOldestOne(StdOut out) throws IOException {
    // Days 1 and 2 are both eaten, filling K exactly. On day 3 the day that expires is day 1's 3,
    // leaving 7 + 4 = 11 -- refused. Dropping the newer 7 instead leaves 3 + 4 = 7 and prints Yes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes\nNo\nYes");
  }

  @Test
  @StdIo({"6 2 10", "7 7 7 7 7 7"})
  void theWindowIsFreedRepeatedlyNotJustOnce(StdOut out) throws IOException {
    // No two neighboring 7s fit under 10, so the trip alternates for all six days. A total that
    // accumulates without ever subtracting stalls at 7 and prints No from day 2 onward.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nYes\nNo\nYes\nNo");
  }

  @Test
  @StdIo({"3 3 10", "4 4 4"})
  void theEarlyWindowIsShorterThanM(StdOut out) throws IOException {
    // Days 1 and 2 look back past the start of the trip, so the window start clamps to day 1
    // rather than indexing a day that does not exist.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes\nNo");
  }

  // --- The two ends of the M range. ---

  @Test
  @StdIo({"4 1 5", "5 6 5 5"})
  void whenMIsOneEachDayIsJudgedAlone(StdOut out) throws IOException {
    // The window is the day itself, so a day is eaten exactly when Ai fits under K -- days 3 and
    // 4 are both taken even though together they come to 10.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nYes\nYes");
  }

  @Test
  @StdIo({"4 4 10", "3 3 3 3"})
  void whenMEqualsNTheWholeTripIsOneWindow(StdOut out) throws IOException {
    // M at its ceiling of N: nothing ever leaves, so the running total only climbs.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes\nYes\nNo");
  }

  // --- A snack heavier than K is refused wherever it falls. ---

  @Test
  @StdIo({"3 2 5", "9 9 9"})
  void aSnackBiggerThanKIsRefusedFromTheOpeningDay(StdOut out) throws IOException {
    // Day 1 faces an empty window and is still refused, so the first day is not a free pass. All
    // N lines are printed even though none of them is a Yes.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No\nNo\nNo");
  }

  @Test
  @StdIo({"3 1 5", "1 9 1"})
  void aSnackBiggerThanKIsRefusedEvenOnAnEmptyWindow(StdOut out) throws IOException {
    // With M = 1 nothing else is ever in the window, so day 2 is refused on its own weight and
    // day 3 recovers immediately.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nYes");
  }

  // --- The N, K and Ai boundaries. ---

  @Test
  @StdIo({"1 1 1", "1"})
  void theShortestTripEatsItsOnlySnack(StdOut out) throws IOException {
    // N, M, K and A1 all at their floors.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes");
  }

  @Test
  @StdIo({"1 1 1", "2"})
  void theShortestTripRefusesASnackOverK(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("No");
  }

  @Test
  @StdIo({"4 2 1", "1 1 2 1"})
  void kAtItsFloorAdmitsOneCalorieAtATime(StdOut out) throws IOException {
    // K = 1 and Ai >= 1, so a day is eaten only when its snack is a single calorie and the
    // neighbor inside the window was refused.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nNo\nNo\nYes");
  }

  @Test
  @StdIo({"3 3 2500000000", "1000000000 1000000000 1000000000"})
  void theCalorieTotalOutgrowsAnIntAccumulator(StdOut out) throws IOException {
    // Day 3's window is 3 x 10^9, which wraps an int to -1294967296 and slips under K as a
    // negative number. K itself is past Integer.MAX_VALUE too, so parsing it as an int throws.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes\nNo");
  }

  @Test
  @StdIo({"3 3 1000000000000000", "1000000000 1000000000 1000000000"})
  void kAtItsCeilingLeavesEverySnackAffordable(StdOut out) throws IOException {
    // K at 10^15 with Ai at 10^9. The largest total the constraints allow is 2 x 10^5 x 10^9 =
    // 2 x 10^14, so at this K nothing is ever refused.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("Yes\nYes\nYes");
  }

  // --- N at its ceiling of 2 x 10^5, where re-summing the window every day is too slow. ---

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLongestTripIsAnsweredWithinTheTimeLimit() throws IOException {
    // N = M = 2 x 10^5 with every Ai at 10^9 and K at 10^15: one window covering the whole trip,
    // every snack eaten. Re-summing it each day is 2 x 10^10 additions and cannot finish.
    int n = 200_000;
    long[] a = new long[n];
    Arrays.fill(a, 1_000_000_000L);

    String[] verdicts = runMain(buildInput(n, n, 1_000_000_000_000_000L, a)).split("\n");

    assertThat(verdicts).hasSize(n).containsOnly("Yes");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLongestTripStillExpiresItsWindowOnTime() throws IOException {
    // N = 2 x 10^5, M = 10^5, every Ai at 10^9 and K = 10^9 -- room for one snack per window.
    // Day 1 is eaten and blocks every day through 100000, whose window still opens on day 1;
    // day 100001 is the first whose window opens on day 2, so it eats and blocks the rest.
    int n = 200_000;
    long[] a = new long[n];
    Arrays.fill(a, 1_000_000_000L);

    String[] verdicts = runMain(buildInput(n, 100_000, 1_000_000_000L, a)).split("\n");

    assertThat(verdicts).hasSize(n);
    List<Integer> feastDays = new ArrayList<>();
    for (int i = 0; i < verdicts.length; i++) {
      if (verdicts[i].equals("Yes")) {
        feastDays.add(i + 1);
      }
    }
    assertThat(feastDays).containsExactly(1, 100_001);
  }

  // --- Randomized cross-check against the definitional oracle. ---

  @Test
  void randomShortTripsMatchTheOracle() throws IOException {
    // Small K and calories so that Yes and No both come up often and the window turns over
    // several times inside one trip.
    Random rng = new Random(472_003L); // fixed seed -> reproducible
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rng.nextInt(40);
      int m = 1 + rng.nextInt(n);
      long k = 1 + rng.nextInt(40);
      long[] a = new long[n];
      for (int i = 0; i < n; i++) {
        a[i] = 1 + rng.nextInt(15);
      }
      assertThat(runMain(buildInput(n, m, k, a)))
          .as("N=%d M=%d K=%d A=%s", n, m, k, Arrays.toString(a))
          .isEqualTo(oracle(m, k, a));
    }
  }

  @Test
  void randomTripsWithNineDigitCaloriesMatchTheOracle() throws IOException {
    // The same shapes with Ai near 10^9 and K spread over a few billion, so every window sum sits
    // in the range where 32-bit arithmetic goes wrong.
    Random rng = new Random(472_004L); // fixed seed -> reproducible
    for (int trial = 0; trial < 200; trial++) {
      int n = 1 + rng.nextInt(30);
      int m = 1 + rng.nextInt(n);
      long k = (1 + rng.nextInt(5)) * 1_000_000_000L + rng.nextInt(1_000_000_000);
      long[] a = new long[n];
      for (int i = 0; i < n; i++) {
        a[i] = 900_000_000L + rng.nextInt(100_000_001);
      }
      assertThat(runMain(buildInput(n, m, k, a)))
          .as("N=%d M=%d K=%d A=%s", n, m, k, Arrays.toString(a))
          .isEqualTo(oracle(m, k, a));
    }
  }

  /**
   * Renders one trip in the input format: {@code N M K} on the first line, then A on the second.
   */
  private static String buildInput(int n, int m, long k, long[] a) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(m).append(' ').append(k).append('\n');
    for (int i = 0; i < a.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(a[i]);
    }
    return sb.append('\n').toString();
  }

  /**
   * Independent oracle: replays the rule as it is written, recomputing each day's window by summing
   * the eaten calories over {@code [max(i - M + 1, 1), i]} from scratch. It carries no running
   * total and never subtracts an expiring day, so agreement with {@link Main} is a genuine
   * cross-check of the sliding window rather than the same bookkeeping run twice.
   *
   * @implNote {@code O(N * M)} time and {@code O(N)} space, where {@code N = a.length} is the
   *     length of the trip; callers must keep the trip short. Accumulates in {@code long} so the
   *     oracle itself cannot overflow.
   */
  private static String oracle(int m, long k, long[] a) {
    int n = a.length;
    long[] eaten = new long[n + 1]; // 1-indexed by day; 0 on a day whose snack was refused
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= n; i++) {
      long window = a[i - 1];
      for (int day = Math.max(i - m + 1, 1); day < i; day++) {
        window += eaten[day];
      }
      if (window <= k) {
        eaten[i] = a[i - 1];
        sb.append("Yes");
      } else {
        sb.append("No");
      }
      if (i < n) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  /** Drives {@link Main} over stdin/stdout for inputs too large to spell out in {@code @StdIo}. */
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
