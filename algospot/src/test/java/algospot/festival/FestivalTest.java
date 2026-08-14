package algospot.festival;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Algospot FESTIVAL -- rent the hall for a contiguous run of <b>at least</b> {@code L} days and
 * minimize the average cost per day.
 *
 * <p>The "at least" is the whole problem. Booking exactly {@code L} days is always legal, but
 * appending cheap days beyond {@code L} can pull the average down further, so the search runs over
 * every window of length {@code L} or more -- not the fixed-width window the phrase "L days"
 * suggests.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code C} ({@code C <= 100}), the number of test cases. Each
 * test case is a line with {@code N} and {@code L} ({@code 1 <= L <= N <= 1000}) followed by a line
 * of {@code N} daily costs, each a natural number at most 100. Each test case prints one line with
 * the minimum average. Answers within 1e-7 absolute or relative error are accepted; the solution
 * formats 11 decimal places, which the assertions below pin literally.
 *
 * <p><b>TDD ladder.</b> Work down the file one rung at a time. Each rung is the smallest input that
 * fails against the minimal code satisfying every rung above it, so the first failure is always the
 * next thing to write. The staircase was verified by building those intermediate implementations
 * and observing each rung break exactly the one below it.
 *
 * <ol>
 *   <li>one day &rarr; the fixed-point output format
 *   <li>one day, different cost &rarr; the number must come from input
 *   <li>two days, {@code L = 1} &rarr; look at every day, not just the first
 *   <li>two days, {@code L = 2} &rarr; {@code L} is a floor on the window length
 *   <li>six days, {@code L = 3}, cheap tail &rarr; windows <em>longer</em> than {@code L}
 *   <li>two cases &rarr; the loop over {@code C}
 * </ol>
 *
 * <p>Rung 5 is the one the problem is really about, and the one a plausible wrong solution fails:
 * every three-day window of {@code [1,2,3,1,2,3]} averages exactly 2.0, while the four days
 * {@code [1,2,3,1]} average 1.75.
 *
 * <p>The ladder stops at six rungs because nothing after them is forced. A seventh rung for prefix
 * sums was written, measured, and withdrawn: re-adding every window from scratch costs about eight
 * seconds at the judge's maximum input, which fits inside its 20-second limit. That would have been
 * a test asserting a preference rather than a requirement, so it is a GUARD below instead.
 */
class FestivalTest {

  // RUNG 1 -- forces: printing an average at all, in the judge's fixed-point shape.
  // Fails on: an empty main. N = L = 1 is the smallest legal instance, so there is exactly one
  // window and no choice to make; all this pins is the format.
  @Test
  @StdIo({"1", "1 1", "5"})
  void singleDayFestivalCostsThatDay(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("5.00000000000");
  }

  // RUNG 2 -- forces: the number to come from the input.
  // Fails on: rung-1 code that prints the literal 5.00000000000. The triangulation step.
  @Test
  @StdIo({"1", "1 1", "7"})
  void singleDayFestivalUsesWhicheverCostIsGiven(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("7.00000000000");
  }

  // RUNG 3 -- forces: considering every day rather than the first.
  // Fails on: rung-2 code that reads one cost and prints it. With L = 1 each day is a legal booking
  // on its own, and the cheaper of the two is the second, so a solver anchored at day one is wrong.
  @Test
  @StdIo({"1", "2 1", "5 3"})
  void cheapestSingleDayWinsWhenOneDayIsEnough(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("3.00000000000");
  }

  // RUNG 4 -- forces: L to act as a floor on the window length.
  // Fails on: rung-3 code that just takes the cheapest day. The same two costs now require both
  // days to be booked, so the answer rises from 3 to (5 + 3) / 2 = 4. This is the first rung where
  // the answer is worse than the greedy one, which is what makes it hard to pass by accident.
  @Test
  @StdIo({"1", "2 2", "5 3"})
  void bookingMustCoverAtLeastLDays(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("4.00000000000");
  }

  // RUNG 5 -- forces: windows LONGER than L, the point of the problem.
  // Fails on: rung-4 code that only measures windows of exactly L days. Every three-day window of
  // [1, 2, 3, 1, 2, 3] averages exactly 2.0, so a fixed-width solver reports 2.00000000000; the
  // four days [1, 2, 3, 1] average 7/4 = 1.75. The statement's own worked example is this shape.
  @Test
  @StdIo({"1", "6 3", "1 2 3 1 2 3"})
  void windowLongerThanLDaysCanBeatEveryExactLWindow(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("1.75000000000");
  }

  // RUNG 6 -- forces: the loop over C.
  // Fails on: rung-5 code that answers one case and stops, printing a single line. The two answers
  // differ, so the order is pinned too.
  @Test
  @StdIo({"2", "1 1", "5", "1 1", "7"})
  void everyTestCaseGetsItsOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("5.00000000000", "7.00000000000");
  }

  // GUARD -- the largest input the judge can send: C = 100 cases of N = 1000 days, inside the
  // judge's own 20-second limit.
  //
  // Not a rung, and measuring is what settled that. The obvious next step after rung 6 would be
  // "compute a window's total in O(1) from prefix sums", but re-adding each window from scratch is
  // only O(C * N^3 / 6) ~ 1.7e10 int additions, which Java runs in about eight seconds -- inside
  // the limit. So the constraints do not force prefix sums, and a test claiming they did would be
  // asserting a preference as a requirement. The solution uses them anyway and finishes in well
  // under a second; this guard pins that headroom without pretending to be a step.
  //
  // Every cost is at least 1 and day 0 costs exactly 1, so with L = 1 the answer is exactly 1.0 --
  // an expectation derived from the construction, not from running the solution.
  @Test
  @Timeout(value = 20, unit = TimeUnit.SECONDS)
  @StdIo
  void largestLegalInputIsAnsweredWithinTheJudgesLimit(StdOut out) throws IOException {
    // LADDER-GEN: festival_max_days
    // LADDER-CHECK: festival_all_ones
    runOn(maxSizedCases(100, 1000));

    assertThat(out.capturedLines()).hasSize(100).containsOnly("1.00000000000");
  }

  // GUARD -- the judge's official sample, verbatim from problems/FESTIVAL/samples/sample-01: the
  // same six costs booked against L = 3 and then L = 2. Not a rung; the characterization test.
  @Test
  @StdIo({"2", "6 3", "1 2 3 1 2 3", "6 2", "1 2 3 1 2 3"})
  void officialSampleIsReproducedExactly(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("1.75000000000", "1.50000000000");
  }

  // GUARD -- L = N, where the whole array is the only legal window: (4 + 1 + 1 + 2) / 4 = 2.
  // Not a rung: rung 4 already forces the floor on window length. Kept as the boundary where the
  // candidate set collapses to one, which an off-by-one in the start-index bound would empty.
  @Test
  @StdIo({"1", "4 4", "4 1 1 2"})
  void wholeRangeIsTheOnlyOptionWhenLEqualsN(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("2.00000000000");
  }

  // GUARD -- an answer that does not terminate in decimal: (1 + 1 + 2) / 3 = 4/3.
  // Not a rung: rung 5's 1.75 already rules out integer arithmetic. Kept because it is the case
  // that would expose float rather than double -- a float 4/3 prints as 1.33333331347 at eleven
  // decimals, well outside the judge's 1e-7 tolerance.
  @Test
  @StdIo({"1", "3 3", "1 1 2"})
  void repeatingDecimalIsPrintedToElevenPlaces(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("1.33333333333");
  }

  // GUARD -- every day costs the same, so every window ties and the answer is that cost.
  // Not a rung. It pins the degenerate case where no window is strictly better than another, which
  // is where a comparison written with the wrong strictness would show up as a missing candidate.
  @Test
  @StdIo({"1", "5 2", "7 7 7 7 7"})
  void allEqualCostsAverageToThatCost(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("7.00000000000");
  }

  /**
   * Builds {@code cases} test cases of {@code n} days with {@code L = 1}. Costs cycle through
   * 1..100 so day 0 always costs exactly 1, making 1.0 the minimum average of every case by
   * construction.
   */
  private static String maxSizedCases(int cases, int n) {
    StringBuilder sb = new StringBuilder();
    sb.append(cases).append('\n');
    for (int c = 0; c < cases; c++) {
      sb.append(n).append(" 1\n");
      for (int i = 0; i < n; i++) {
        if (i > 0) {
          sb.append(' ');
        }
        sb.append(i * 37 % 100 + 1);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /**
   * Feeds generated input to {@link Main}. {@code @StdIo} only accepts compile-time constants, so
   * an instance too large to spell out as literals is piped in here instead; {@code System.in} is
   * restored afterwards because a bare {@code @StdIo} captures stdout only and leaves stdin alone.
   */
  private static void runOn(String input) throws IOException {
    InputStream original = System.in;
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      Main.main(new String[0]);
    } finally {
      System.setIn(original);
    }
  }
}
