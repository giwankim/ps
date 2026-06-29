package boj.boj23831;

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
 * BOJ 23831 나 퇴사임? ("Am I Getting Fired?") -- a constrained scheduling DP: over {@code N} days pick
 * one of four study locations each day to maximize total satisfaction without breaking the
 * dormitory rules that would get Wooseok fired.
 *
 * <p>Each day Wooseok chooses exactly one of four locations, each worth a per-day satisfaction: the
 * <b>reading room</b> ({@code p}), the <b>small study room</b> ({@code q}), the <b>lounge</b>
 * ({@code r}), or <b>recuperation</b> in his room ({@code s}). The reading room and the small study
 * room both count as "studying". The three rules, any of which gets him fired (so an optimal plan
 * must obey all three), are:
 *
 * <ul>
 *   <li><b>Recuperation cap:</b> recuperation may be chosen at most {@code A} times in total.
 *   <li><b>No back-to-back lounge:</b> the lounge may not be used on two consecutive days (doing so
 *       is treated as gaming).
 *   <li><b>Study floor:</b> the reading room and small study room together must be used at least
 *       {@code B} times in total (otherwise he is judged to have lost the will to study).
 * </ul>
 *
 * <p><b>I/O contract.</b> Line 1 is {@code N}. Line 2 is {@code "A B"} -- the recuperation cap and
 * the required minimum number of study days. Each of the next {@code N} lines holds four integers
 * {@code "p q r s"}: that day's satisfaction for the reading room, small study room, lounge, and
 * recuperation, in that order. The program prints a single line: the maximum total satisfaction
 * achievable while obeying every rule.
 *
 * <p><b>Constraints.</b> Triangulated from contest editorials while acmicpc.net was unreachable
 * (the problem is SASA Programming Contest 2021 Open F): {@code N <= 100}, and the caps {@code A}
 * and {@code B} are each at most {@code 100}. The reference solutions store the DP in {@code int}
 * tables guarded by a small (~{@code -10^6}) sentinel, so the per-day satisfaction values are small
 * and the answer comfortably fits a signed 32-bit {@code int} -- there is no large-number overflow
 * dimension to this problem. The hand cases below use small non-negative values; the oracles below
 * accumulate in {@code long} regardless, so the cross-checks cannot overflow.
 *
 * <p><b>Feasibility.</b> A legal schedule always exists exactly when {@code B <= N}: studying every
 * single day uses zero recuperation, no lounge, and {@code N >= B} study days. Every test input
 * here keeps {@code B <= N}, so the answer is always well defined. (When {@code B > N} no plan can
 * avoid being fired; the problem guarantees this never occurs, so it is not exercised.)
 *
 * <p><b>Why a plain per-day greedy fails.</b> Taking each day's highest-value room ignores all
 * three rules at once: it would double up the lounge
 * ({@link #consecutiveLoungeDaysAreForbidden(StdOut)}), blow the recuperation cap
 * ({@link #recuperationCapLimitsTotalRecuperationDays(StdOut)}), and skip the study floor
 * ({@link #studyRequirementOverridesHigherValueRooms(StdOut)}). It also fails to <em>save</em> a
 * scarce resource for its best day
 * ({@link #recuperationCapIsSpentOnTheBestDayNotTheFirst(StdOut)}). Correctness needs the global DP
 * over {@code (day, used-lounge-yesterday, recuperation-count, study-count)}.
 *
 * <p><b>Oracles.</b> Small instances are cross-checked against {@link #bruteForceMaxSatisfaction},
 * which enumerates every one of the {@code 4^N} day-by-day location assignments and re-scans each
 * for legality -- algorithmically independent of any recurrence, so agreement is genuine evidence.
 * The maximum-size instance, where {@code 4^N} enumeration is hopeless, is checked against
 * {@link #dpMaxSatisfaction}, an independently written memoized DP; the randomized sweep
 * additionally asserts the two oracles agree with each other, so the large-case oracle is itself
 * trustworthy.
 */
class MainTest {

  // --- Single day: isolate the four choices and the A/B gates on the smallest possible input. ---

  // N = 1, A = 0, B = 0, day = [reading 10, small 20, lounge 30, recuperation 40]. Recuperation is
  // the richest room but A = 0 forbids it entirely, and B = 0 imposes no study floor, so the best
  // *allowed* room is the lounge at 30 (legal on day 1 since there is no previous day). A solver
  // that ignored the A = 0 recuperation gate would wrongly print 40.
  @Test
  @StdIo({"1", "0 0", "10 20 30 40"})
  void singleDayPicksBestAllowedRoomWhenRecuperationIsCapped(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("30");
  }

  // Same day, now A = 1: recuperation is permitted once, so the global maximum is the recuperation
  // value 40. Pins that a positive cap actually unlocks the recuperation choice.
  @Test
  @StdIo({"1", "1 0", "10 20 30 40"})
  void recuperationBecomesAvailableWhenCapIsPositive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("40");
  }

  // Same day, now B = 1 with A = 0: the single day must be a study day, so only the reading room
  // (10) or small study room (20) are legal -- the lounge (30) and recuperation (40) would leave
  // the
  // study count at 0 < B. The answer is max(10, 20) = 20, forcing a lower-value but legal room.
  @Test
  @StdIo({"1", "0 1", "10 20 30 40"})
  void studyFloorForcesAStudyRoomOnTheOnlyDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- No-back-to-back-lounge rule. ---

  // N = 2, both days = [0, 0, lounge 100, 0]. The lounge is worth 100 each day but cannot be taken
  // on consecutive days, so at most one of the two days is a lounge (100) and the other is a
  // zero-value study/recuperation day -> 100. A solver that allowed consecutive lounges prints 200.
  @Test
  @StdIo({"2", "0 0", "0 0 100 0", "0 0 100 0"})
  void consecutiveLoungeDaysAreForbidden(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // N = 3, every day = [0, 0, lounge 100, 0]. Days 1 and 3 are a legal non-consecutive lounge pair
  // (day 2 is a zero study day between them) for 200. This is the complement of the previous case:
  // a solver that over-restricted the lounge to "at most once ever" would print only 100.
  @Test
  @StdIo({"3", "0 0", "0 0 100 0", "0 0 100 0", "0 0 100 0"})
  void nonConsecutiveLoungeDaysAreBothAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  // --- Recuperation cap rule. ---

  // N = 3, A = 1, every day = [0, 0, 0, recuperation 100]. Recuperation is the only valuable room
  // but the cap allows it just once (100); the other two days are forced to zero-value rooms ->
  // 100.
  // A solver that did not count recuperation against the cap would print 300.
  @Test
  @StdIo({"3", "1 0", "0 0 0 100", "0 0 0 100", "0 0 0 100"})
  void recuperationCapLimitsTotalRecuperationDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // N = 2, A = 1: day 1 recuperation is worth 5, day 2 recuperation is worth 100. The single
  // recuperation must be saved for day 2 (100), taking a zero room on day 1, rather than spent
  // eagerly on day 1 (which would force a zero room on day 2 for only 5). The expected answer is
  // the
  // larger value, 100 -- a forward-greedy that grabs the first legal recuperation surfaces here as
  // 5.
  @Test
  @StdIo({"2", "1 0", "0 0 0 5", "0 0 0 100"})
  void recuperationCapIsSpentOnTheBestDayNotTheFirst(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // --- Study floor rule. ---

  // N = 3, A = 0, B = 2, every day = [reading 1, 0, lounge 100, 0]. The study floor of 2 forces at
  // least two study days (worth 1 each); the remaining day can be a lounge (100), for 1 + 1 + 100 =
  // 102. A solver that ignored B would chase the lounges (e.g. lounge-study-lounge = 201) and
  // exceed
  // the true optimum, so the expected value being the *smaller* 102 pins that the floor is
  // enforced.
  @Test
  @StdIo({"3", "0 2", "1 0 100 0", "1 0 100 0", "1 0 100 0"})
  void studyRequirementOverridesHigherValueRooms(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("102");
  }

  // N = 3, A = 5, B = 3: the study floor equals the number of days, so every day must be a study
  // day
  // and the lounge/recuperation values (100 each) are entirely unreachable. The answer is the sum
  // of
  // each day's better study room: max(5,1) + max(1,6) + max(7,2) = 5 + 6 + 7 = 18.
  @Test
  @StdIo({"3", "5 3", "5 1 100 100", "1 6 100 100", "7 2 100 100"})
  void studyFloorEqualToDaysForcesStudyEveryDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("18");
  }

  // --- Slack constraints reduce to picking each day's best room. ---

  // N = 2, A = 100, B = 0: both caps are slack, so with no lounge taken the answer is simply the
  // sum
  // of per-day maxima -- max(1,2,3,4) recuperation on day 1 (4) plus max(40,30,20,10) reading on
  // day
  // 2 (40) = 44. Confirms the unconstrained baseline an over-eager rule check might depress.
  @Test
  @StdIo({"2", "100 0", "1 2 3 4", "40 30 20 10"})
  void slackConstraintsReduceToPerDayMaxima(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("44");
  }

  // --- Randomized cross-check: many small schedules against the independent brute-force oracle.
  // ---

  // Random schedules (N in 1..7, every legal A and B in 0..N, satisfaction 0..20) are each answered
  // and compared to a brute force that enumerates all 4^N location assignments. The small N keeps
  // the
  // exponential enumeration cheap while mixing cap pressure, study-floor pressure, and lounge
  // spacing
  // across hundreds of shapes, exercising the rule interactions far more thoroughly than any hand
  // case. Each trial also asserts the brute-force and memoized-DP oracles agree, validating the
  // oracle the maximum-size test relies on. A fixed seed keeps the run reproducible across JVMs.
  @Test
  void randomSmallSchedulesMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(23831L);
    for (int trial = 0; trial < 400; trial++) {
      int n = 1 + rng.nextInt(7); // 1..7
      int a = rng.nextInt(n + 1); // 0..n
      int b = rng.nextInt(n + 1); // 0..n, so a legal schedule always exists
      int[] p = new int[n];
      int[] q = new int[n];
      int[] r = new int[n];
      int[] s = new int[n];
      for (int i = 0; i < n; i++) {
        p[i] = rng.nextInt(21);
        q[i] = rng.nextInt(21);
        r[i] = rng.nextInt(21);
        s[i] = rng.nextInt(21);
      }
      long brute = bruteForceMaxSatisfaction(p, q, r, s, a, b);
      assertThat(dpMaxSatisfaction(p, q, r, s, a, b))
          .as(
              "oracle cross-check p=%s q=%s r=%s s=%s a=%d b=%d",
              Arrays.toString(p), Arrays.toString(q), Arrays.toString(r), Arrays.toString(s), a, b)
          .isEqualTo(brute);
      assertThat(runMain(buildInput(p, q, r, s, a, b)))
          .as(
              "p=%s q=%s r=%s s=%s a=%d b=%d",
              Arrays.toString(p), Arrays.toString(q), Arrays.toString(r), Arrays.toString(s), a, b)
          .isEqualTo(Long.toString(brute));
    }
  }

  // --- The maximum-length schedule is parsed and solved within the time limit. ---

  // A full N = 100 schedule with mid-range caps (A = 40, B = 60, both leaving the lounge and
  // recuperation choices live) and deterministic pseudo-random satisfaction in 0..1000,
  // cross-checked
  // against the independent memoized-DP oracle since 4^100 enumeration is impossible. The intended
  // DP
  // is polynomial in N, A and B; the @Timeout guards against an accidentally exponential
  // implementation, and the oracle guards correctness at the largest legal input.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumLengthScheduleIsSolvedWithinTheTimeLimit() throws IOException {
    int n = 100;
    int a = 40;
    int b = 60;
    int[] p = new int[n];
    int[] q = new int[n];
    int[] r = new int[n];
    int[] s = new int[n];
    Random rng = new Random(100L); // fixed seed -> reproducible
    for (int i = 0; i < n; i++) {
      p[i] = rng.nextInt(1001);
      q[i] = rng.nextInt(1001);
      r[i] = rng.nextInt(1001);
      s[i] = rng.nextInt(1001);
    }
    String expected = Long.toString(dpMaxSatisfaction(p, q, r, s, a, b));
    assertThat(runMain(buildInput(p, q, r, s, a, b))).isEqualTo(expected);
  }

  /**
   * Sentinel for an infeasible sub-schedule; small enough that no legal total ever falls below it.
   */
  private static final long INFEASIBLE = Long.MIN_VALUE / 4;

  /**
   * Independent brute-force oracle: the most satisfaction obtainable over the {@code N} days,
   * enumerating every one of the {@code 4^N} location assignments (reading / small study / lounge /
   * recuperation per day) and keeping the best that obeys all three rules -- at most {@code a}
   * recuperation days, no two consecutive lounge days, and at least {@code b} study days. Modelling
   * the physical day-by-day choice directly, rather than tabulating a recurrence, makes agreement
   * with {@link Main} an independent cross-check.
   *
   * @implNote {@code O(4^N * N)} time, where {@code N = p.length}; callers must keep {@code N}
   *     small (here at most 7). The running total is a {@code long} so the oracle never overflows.
   */
  private static long bruteForceMaxSatisfaction(int[] p, int[] q, int[] r, int[] s, int a, int b) {
    int n = p.length;
    long assignments = 1L << (2 * n); // 4^N
    long best = INFEASIBLE;
    for (long code = 0; code < assignments; code++) {
      long c = code;
      int study = 0;
      int rest = 0;
      boolean prevLounge = false;
      boolean valid = true;
      long sum = 0;
      for (int i = 0; i < n; i++) {
        int choice = (int) (c & 3);
        c >>= 2;
        if (choice == 0) {
          sum += p[i];
          study++;
          prevLounge = false;
        } else if (choice == 1) {
          sum += q[i];
          study++;
          prevLounge = false;
        } else if (choice == 2) {
          if (prevLounge) {
            valid = false;
            break;
          }
          sum += r[i];
          prevLounge = true;
        } else {
          rest++;
          if (rest > a) {
            valid = false;
            break;
          }
          sum += s[i];
          prevLounge = false;
        }
      }
      if (valid && study >= b) {
        best = Math.max(best, sum);
      }
    }
    return best;
  }

  /**
   * Independent memoized-DP oracle used where enumeration is infeasible: the most satisfaction over
   * days {@code day..N-1} given whether the lounge was used yesterday ({@code prevLounge}), how
   * many recuperation days have been spent ({@code rest}), and how many study days have been banked
   * ({@code study}). The study count is clamped at {@code b} because once the floor is met further
   * study days cannot change feasibility -- this bounds the state space. Written separately from
   * {@link Main} so it can stand in as an expected-value source at the maximum input size.
   *
   * @implNote {@code O(N * a * b)} time and space (the lounge flag and the four daily choices are
   *     constant factors), where {@code N = p.length}; accumulates in {@code long} so the oracle
   *     cannot overflow.
   */
  private static long dpMaxSatisfaction(int[] p, int[] q, int[] r, int[] s, int a, int b) {
    int n = p.length;
    // next[prev][rest][study] = best satisfaction over days [day..n-1] given that the lounge was
    // used yesterday iff prev == 1, rest recuperation days are already spent, and study study days
    // are already banked (clamped at b). Seed it with the day == n base case, then sweep backwards.
    long[][][] next = new long[2][a + 1][b + 1];
    for (int prev = 0; prev < 2; prev++) {
      for (int rest = 0; rest <= a; rest++) {
        for (int study = 0; study <= b; study++) {
          next[prev][rest][study] = study >= b ? 0 : INFEASIBLE;
        }
      }
    }
    for (int day = n - 1; day >= 0; day--) {
      long[][][] cur = new long[2][a + 1][b + 1];
      for (int prev = 0; prev < 2; prev++) {
        for (int rest = 0; rest <= a; rest++) {
          for (int study = 0; study <= b; study++) {
            int banked = Math.min(study + 1, b);
            long best = add(p[day], next[0][rest][banked]); // reading room (a study day)
            best = Math.max(best, add(q[day], next[0][rest][banked])); // small study room
            if (prev == 0) {
              best = Math.max(best, add(r[day], next[1][rest][study])); // lounge
            }
            if (rest < a) {
              best = Math.max(best, add(s[day], next[0][rest + 1][study])); // recuperation
            }
            cur[prev][rest][study] = best;
          }
        }
      }
      next = cur;
    }
    return next[0][0][0];
  }

  /** Adds {@code value} to a sub-result, leaving the {@link #INFEASIBLE} sentinel saturated. */
  private static long add(int value, long subResult) {
    return subResult <= INFEASIBLE ? INFEASIBLE : subResult + value;
  }

  /**
   * Builds BOJ 23831 input: {@code N} on line 1, {@code "A B"} on line 2, then one {@code "p q r
   * s"} line per day.
   */
  private static String buildInput(int[] p, int[] q, int[] r, int[] s, int a, int b) {
    StringBuilder sb = new StringBuilder();
    sb.append(p.length).append('\n');
    sb.append(a).append(' ').append(b).append('\n');
    for (int i = 0; i < p.length; i++) {
      sb.append(p[i])
          .append(' ')
          .append(q[i])
          .append(' ')
          .append(r[i])
          .append(' ')
          .append(s[i]);
      sb.append('\n');
    }
    return sb.toString();
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
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
