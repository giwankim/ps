package boj.boj14501;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 14501 퇴사 (Quitting Work).
 *
 * <p>Baekjoon plans to quit on day N+1, leaving days 1 through N to run consultations. Day i holds
 * one consultation that takes Ti days to finish and pays Pi (1 ≤ N ≤ 15, 1 ≤ Ti ≤ 5, 1 ≤ Pi ≤
 * 1,000). Starting the day-i consultation occupies days i through i + Ti − 1, so it may be taken
 * only when it finishes on or before day N (it must be done before he leaves on day N+1), and
 * chosen consultations may not overlap. Read N and the N (Ti, Pi) pairs from standard input and
 * print the maximum total profit obtainable.
 */
class MainTest {

  // --- Smallest schedule: a single day. ---

  @Test
  @StdIo({"1", "1 1000"})
  void singleDayScheduleTakesItsOnlyConsultation(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The lone consultation takes one day and finishes exactly on day N = 1, so it is taken.
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  @Test
  @StdIo({"1", "2 1000"})
  void singleDayConsultationThatCannotFinishYieldsZeroProfit(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A two-day consultation cannot finish within a one-day schedule (1 + 2 − 1 = 2 > 1), so the
    // best achievable profit is zero.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The quit-day deadline: a consultation must finish on or before day N. ---

  @Test
  @StdIo({"2", "1 100", "2 200"})
  void aConsultationThatWouldFinishAfterQuittingIsExcludedEvenIfItPaysMost(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    // Day 2's consultation pays the most (200) but needs days 2–3, finishing after he quits on day
    // 3; only day 1's consultation (100) is reachable.
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"2", "2 200", "1 50"})
  void aConsultationFinishingExactlyOnTheLastDayIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Day 1's two-day consultation occupies days 1–2 and finishes exactly on day N = 2, so it is
    // eligible; taking it (200) beats day 2's single-day consultation (50).
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  @Test
  @StdIo({"2", "5 100", "5 100"})
  void everyConsultationOverflowingTheScheduleYieldsZeroProfit(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Both five-day consultations run past day 2, so none can be scheduled and the profit is zero.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Choosing which consultations to take. ---

  @Test
  @StdIo({"3", "3 30", "1 25", "1 25"})
  void skippingAHighValueLongJobToTakeTwoShorterOnesEarnsMore(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Greedily taking day 1's three-day job (30) blocks the rest; skipping it for the two one-day
    // jobs on days 2 and 3 (25 + 25) earns 50.
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }

  @Test
  @StdIo({"3", "2 100", "2 100", "1 100"})
  void overlappingConsultationsCannotBothBeTaken(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Day 1 (days 1–2), day 2 (days 2–3) and day 3 (day 3) all pay 100, but day 2 overlaps both
    // neighbors; the best non-overlapping pick is day 1 plus day 3 for 200, not the naive sum 300.
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  // --- Official samples. ---

  @Test
  @StdIo({"7", "3 10", "5 20", "1 10", "1 20", "2 15", "4 40", "2 200"})
  void officialSampleOneTakesDaysOneFourAndFiveForFortyFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Days 1 (10), 4 (20) and 5 (15) fit without overlap; days 6 and 7 cannot finish by day 7.
    assertThat(out.capturedString().trim()).isEqualTo("45");
  }

  @Test
  @StdIo({"10", "1 1", "1 2", "1 3", "1 4", "1 5", "1 6", "1 7", "1 8", "1 9", "1 10"})
  void officialSampleTwoTakesEverySingleDayJobForFiftyFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Every consultation lasts one day, so all ten are taken: 1 + 2 + ... + 10 = 55.
    assertThat(out.capturedString().trim()).isEqualTo("55");
  }

  @Test
  @StdIo({"10", "5 10", "5 9", "5 8", "5 7", "5 6", "5 10", "5 9", "5 8", "5 7", "5 6"})
  void officialSampleThreeFitsOnlyTwoFiveDayJobsForTwenty(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Five-day jobs leave room for at most two (days 1–5 then 6–10); the best pair is 10 + 10 = 20.
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  @Test
  @StdIo({"10", "5 50", "4 40", "3 30", "2 20", "1 10", "1 10", "2 20", "3 30", "4 40", "5 50"})
  void officialSampleFourSkipsTheUnfinishableTailForNinety(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Days 1 (1–5, 50), 6 (6, 10) and 8 (8–10, 30) combine for 90; days 9 and 10 overrun day 10.
    assertThat(out.capturedString().trim()).isEqualTo("90");
  }

  // --- Upper constraint bound N = 15. ---

  @Test
  @StdIo({
    "15", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000",
    "1 1000", "1 1000", "1 1000", "1 1000", "1 1000", "1 1000"
  })
  void maxLengthScheduleOfSingleDayJobsSumsEveryProfit(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The largest schedule (N = 15) of one-day, top-value jobs takes all of them: 15 × 1000 =
    // 15000.
    assertThat(out.capturedString().trim()).isEqualTo("15000");
  }
}
