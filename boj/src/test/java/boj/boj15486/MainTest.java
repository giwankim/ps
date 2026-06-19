package boj.boj15486;

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
 * BOJ 15486 퇴사 2 ("Retirement 2") -- the large-constraint sibling of BOJ 14501 ("Retirement").
 *
 * <p>A counselor plans to retire on day {@code N + 1} and works the remaining {@code N} days. Day
 * {@code i} (counting from 1) carries one consultation that takes {@code T[i]} consecutive days to
 * finish and pays {@code P[i]}. Starting the day-{@code i} consultation occupies days {@code i .. i
 * + T[i] - 1}; the next free day is {@code i + T[i]}. A consultation may be taken only if it
 * finishes on or before the last working day, i.e. {@code i + T[i] - 1 <= N}, equivalently {@code i
 * + T[i] <= N + 1}. Maximize the total pay of a non-overlapping selection.
 *
 * <p>Constraints: {@code 1 <= N <= 1,500,000}, {@code 1 <= T[i] <= 50}, {@code 1 <= P[i] <= 1,000}.
 * The richest possible schedule takes a one-day job every day, so the answer never exceeds {@code N
 * * max(P) = 1,500,000 * 1,000 = 1,500,000,000}, which still fits a signed 32-bit {@code int}.
 *
 * <p>Three families of mistakes drive these fixtures:
 *
 * <ul>
 *   <li><b>The feasibility boundary is {@code i + T[i] <= N + 1}, not {@code <= N}.</b> A one-day
 *       job on the very last day finishes exactly at retirement and is allowed
 *       ({@link #lastDayOneDayJobIsStillAllowed(StdOut)}); a job that overruns by even one day is
 *       not ({@link #aJobLongerThanTheRemainingDaysEarnsNothing(StdOut)}). An off-by-one in this
 *       test is the most common bug.
 *   <li><b>Greedy is wrong; the schedule must be optimized.</b> Taking the single highest-paying
 *       job ({@link #greedilyTakingTheHighestPayingJobIsSuboptimal(StdOut)}) or any job that blocks
 *       more valuable later ones ({@link #aBlockingJobIsSkippedWhenALaterJobPaysMore(StdOut)})
 *       loses.
 *   <li><b>Starting a job blocks the days it spans.</b> A long job forecloses the consultations
 *       underneath it ({@link #skippingEarlyJobsToReachAHighPayingFinale(StdOut)}).
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by two independent oracles: an exhaustive take-or-skip
 * recursion for small inputs ({@link #randomizedSmallInputsMatchBruteForceOracle()}) and a
 * separately written linear DP for the max-scale fuzz
 * ({@link #largeRandomInputMatchesTheLinearOracle()}). The {@code @Timeout} max-size cases force an
 * {@code O(N)} solution: an {@code O(2^N)} or {@code O(N * maxT)}-with-a-bad-constant approach
 * misses the judge limit at {@code N = 1,500,000}.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // Days 1..7 carry (T, P) = (3,10) (5,20) (1,10) (1,20) (2,15) (4,40) (2,200). The best schedule
  // is day 1 (occupies 1..3), day 4 (day 4), day 5 (days 5..6): 10 + 20 + 15 = 45. The fat day-7
  // payout of 200 (T = 2) would finish on day 8 > N = 7, so it cannot be taken.
  @Test
  @StdIo({"7", "3 10", "5 20", "1 10", "1 20", "2 15", "4 40", "2 200"})
  void officialSampleMaximizesToFortyFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("45");
  }

  // --- The feasibility boundary: a job is allowed iff i + T[i] <= N + 1. ---

  // N = 1: the only consultation takes one day and finishes exactly at retirement (1 + 1 = 2 = N +
  // 1), so it is taken for its full pay. The smallest input that yields a non-zero answer.
  @Test
  @StdIo({"1", "1 1000"})
  void singleOneDayJobIsTakenForItsFullPay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // N = 1 but the consultation needs 50 days (1 + 50 = 51 > N + 1 = 2); it can never finish in
  // time,
  // so nothing is earned. Pins the "overruns the horizon" rejection at the minimum size.
  @Test
  @StdIo({"1", "50 1000"})
  void aJobLongerThanTheRemainingDaysEarnsNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The decisive off-by-one. Days 1 and 2 each overrun (1 + 50, 2 + 50 both exceed N + 1 = 4), so
  // the only feasible job is the one-day consultation on the LAST day: 3 + 1 = 4 = N + 1 -> 7. A
  // solution using the wrong bound i + T[i] <= N rejects it (3 + 1 = 4 > 3) and wrongly prints 0.
  @Test
  @StdIo({"3", "50 1000", "50 1000", "1 7"})
  void lastDayOneDayJobIsStillAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // A multi-day job that ends precisely on the last day is allowed: day 1 with T = 3 occupies days
  // 1..3 and finishes at N = 3 (1 + 3 = 4 = N + 1), paying 100. That beats taking the two trailing
  // one-day jobs (1 + 1 = 2). Confirms "<= N + 1" admits the exact fit, not just one-day jobs.
  @Test
  @StdIo({"3", "3 100", "1 1", "1 1"})
  void aJobFinishingExactlyOnTheLastDayIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // Every consultation needs 50 days but only 3 remain, so all overrun and the answer is 0.
  @Test
  @StdIo({"3", "50 1", "50 1", "50 1"})
  void whenEveryJobOverrunsNothingIsEarned(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The schedule must be optimized: greedy and "take everything" both fail. ---

  // The single highest-paying job (day 1, T = 3, pay 20) consumes all three days and blocks the
  // rest. Skipping it to take the two one-day jobs on days 2 and 3 pays 15 + 15 = 30 > 20. A
  // greedy "grab the biggest payout" picks 20 and loses.
  @Test
  @StdIo({"3", "3 20", "1 15", "1 15"})
  void greedilyTakingTheHighestPayingJobIsSuboptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30");
  }

  // N = 2. Day 1 needs two days (occupies 1..2) for pay 5; day 2 is a one-day job paying 10. Taking
  // the long day-1 job blocks day 2, so the optimum is to skip it and take only day 2 -> 10. A
  // solution that always commits to the earliest feasible job returns 5.
  @Test
  @StdIo({"2", "2 5", "1 10"})
  void aBlockingJobIsSkippedWhenALaterJobPaysMore(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // The day-1 job (T = 3, pay 10) would span all three days and block everything. Skipping it frees
  // day 2 (one-day, pay 1) and day 3 (one-day, pay 200) for 1 + 200 = 201. Exercises skipping an
  // early job so two later ones -- including a high-paying finale -- both fit.
  @Test
  @StdIo({"3", "3 10", "1 1", "1 200"})
  void skippingEarlyJobsToReachAHighPayingFinale(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("201");
  }

  // Four back-to-back one-day jobs never overlap, so all are taken: 1 + 2 + 3 + 4 = 10. Confirms
  // the DP carries pay forward across an unbroken chain instead of stopping at the first job.
  @Test
  @StdIo({"4", "1 1", "1 2", "1 3", "1 4"})
  void independentOneDayJobsAreAllTaken(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Max-size inputs (N up to 1,500,000): a linear pass clears the judge limit; the @Timeout
  // guards against super-linear regressions. ---

  // Every day is a one-day job paying the maximum 1,000. None overlap and all are feasible, so the
  // whole row is taken: 1,500,000 * 1,000 = 1,500,000,000 -- the heaviest legal answer, sitting
  // just
  // under Integer.MAX_VALUE (2,147,483,647). Catches an accumulator that is too narrow to hold it.
  @Test
  @Timeout(value = 15, unit = TimeUnit.SECONDS)
  void allOneDayMaxPayingJobsAtMaxSizeSumToTheLargestAnswer() throws IOException {
    int n = 1_500_000;
    int[] t = new int[n];
    int[] p = new int[n];
    java.util.Arrays.fill(t, 1);
    java.util.Arrays.fill(p, 1000);
    assertThat(runMain(buildInput(n, t, p))).isEqualTo("1500000000");
  }

  // A full-size pseudo-random schedule cross-checked against an independently written linear DP.
  // Random T in [1, 50] and P in [1, 1000] mix feasible and overrunning jobs at scale, exercising
  // I/O throughput, parsing, and the recurrence together under the time limit.
  @Test
  @Timeout(value = 15, unit = TimeUnit.SECONDS)
  void largeRandomInputMatchesTheLinearOracle() throws IOException {
    int n = 1_500_000;
    int[] t = new int[n];
    int[] p = new int[n];
    Random rng = new Random(15486L); // fixed seed -> deterministic across JVMs.
    for (int i = 0; i < n; i++) {
      t[i] = 1 + rng.nextInt(50); // 1..50
      p[i] = 1 + rng.nextInt(1000); // 1..1000
    }
    assertThat(runMain(buildInput(n, t, p))).isEqualTo(Long.toString(linearOracle(n, t, p)));
  }

  // --- Randomized cross-check against an exhaustive oracle on small inputs: the truly independent
  // check that catches off-by-one, greedy, and blocking mistakes the hand-picked cases might miss.
  // ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rng = new Random(154860L);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rng.nextInt(12); // 1..12, small enough for the O(2^N) oracle
      int[] t = new int[n];
      int[] p = new int[n];
      for (int i = 0; i < n; i++) {
        t[i] = 1 + rng.nextInt(6); // small T so feasible and overrunning jobs both occur often
        p[i] = 1 + rng.nextInt(1000); // 1..1000
      }
      String expected = Long.toString(bruteForceOracle(n, t, p, 0));
      assertThat(runMain(buildInput(n, t, p)))
          .as("n=%d t=%s p=%s", n, java.util.Arrays.toString(t), java.util.Arrays.toString(p))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent exhaustive oracle: the best total pay reachable from day {@code day} (0-indexed)
   * onward, by either skipping the current consultation or taking it (when it finishes by {@code N
   * + 1}) and jumping past the days it occupies. Obviously correct -- it tries every legal schedule
   * -- and trustworthy only for tiny inputs.
   *
   * @implNote {@code O(2^N)} time in the worst case (one branch per day) -- where {@code N} is the
   *     number of days {@code t.length}. Callers must keep {@code N} small (here {@code <= 12}).
   */
  private static long bruteForceOracle(int n, int[] t, int[] p, int day) {
    if (day >= n) {
      return 0L;
    }
    long skip = bruteForceOracle(n, t, p, day + 1);
    long take = 0L;
    // day is 0-indexed; in 1-indexed terms the job starts on day+1 and the bound is (day+1) + T <=
    // N + 1, i.e. day + t[day] <= n.
    if (day + t[day] <= n) {
      take = p[day] + bruteForceOracle(n, t, p, day + t[day]);
    }
    return Math.max(skip, take);
  }

  /**
   * Independent linear oracle: a separately written copy of the forward DP for cross-checking the
   * solution at full scale. {@code dp[d]} is the most pay obtainable by the start of day {@code d}
   * (0-indexed days, {@code dp} sized {@code N + 1}); the answer is {@code dp[N]}.
   *
   * @implNote {@code O(N)} time and space -- where {@code N} is the number of days
   *     {@code t.length}.
   */
  private static long linearOracle(int n, int[] t, int[] p) {
    long[] dp = new long[n + 1];
    for (int i = 0; i < n; i++) {
      if (dp[i] > dp[i + 1]) {
        dp[i + 1] = dp[i];
      }
      int next = i + t[i];
      if (next <= n && dp[i] + p[i] > dp[next]) {
        dp[next] = dp[i] + p[i];
      }
    }
    return dp[n];
  }

  /**
   * Builds BOJ 15486 input: the count {@code N} on the first line, then {@code "T[i] P[i]"} rows.
   */
  private static String buildInput(int n, int[] t, int[] p) {
    StringBuilder sb = new StringBuilder(n * 8 + 16);
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(t[i]).append(' ').append(p[i]).append('\n');
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
