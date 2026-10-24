package leetcode.p2401_2500;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CountDaysSpentTogetherTest {
  CountDaysSpentTogether sut = new CountDaysSpentTogether();

  // ===========================================================================================
  // Inclusive endpoints (Steps 1-3).
  // ===========================================================================================

  // Step 1: the floor. Arrival is never after leaving, so every stay is at least one day, and
  //         both endpoints are inclusive — four identical dates share exactly that one day.
  //         Subtracting the later arrival from the earlier leaving without the + 1 answers 0
  @Test
  void sameSingleDayForBothIsOneSharedDay() {
    assertThat(sut.countDaysTogether("06-15", "06-15", "06-15", "06-15")).isEqualTo(1);
  }

  // Step 2: the stays touch at one date — Alice leaves on the day Bob arrives. Inclusive endpoints
  //         make that a shared day, so the answer is 1, not the 0 of a half-open reading
  @Test
  void aliceLeavingOnBobsArrivalDaySharesThatDay() {
    assertThat(sut.countDaysTogether("06-10", "06-15", "06-15", "06-20")).isEqualTo(1);
  }

  // Step 3: the mirror of Step 2 — Bob leaves on the day Alice arrives. A solution that assumes
  //         Alice always arrives first and answers leaveAlice - arriveBob + 1 counts June 10 to
  //         June 20 and answers 11, the two stays end to end
  @Test
  void bobLeavingOnAlicesArrivalDaySharesThatDay() {
    assertThat(sut.countDaysTogether("06-15", "06-20", "06-10", "06-15")).isEqualTo(1);
  }

  // ===========================================================================================
  // Stays that never meet (Steps 4-6).
  // ===========================================================================================

  // Step 4: a gap of several days between the stays. The raw overlap goes negative here — earlier
  //         leaving January 5, later arrival January 10, so 5 - 10 + 1 = -4 — and has to be
  //         clamped at 0
  @Test
  void aliceLeavingWellBeforeBobArrivesSharesNothing() {
    assertThat(sut.countDaysTogether("01-01", "01-05", "01-10", "01-20")).isZero();
  }

  // Step 5: the mirror of Step 4 — Bob's is the early stay. The unclamped formula answers -4
  //         again, and Step 3's Alice-arrives-first shortcut answers 20
  @Test
  void bobLeavingWellBeforeAliceArrivesSharesNothing() {
    assertThat(sut.countDaysTogether("01-10", "01-20", "01-01", "01-05")).isZero();
  }

  // Step 6: the same days of the month, in different months. Comparing only the DD part sees
  //         days 10 to 20 against days 10 to 20 and answers 11. A date is the month and the day
  //         together
  @Test
  void sameDaysOfDifferentMonthsDoNotOverlap() {
    assertThat(sut.countDaysTogether("01-10", "01-20", "02-10", "02-20")).isZero();
  }

  // ===========================================================================================
  // Who arrives first, who leaves last (Steps 7-10).
  // ===========================================================================================

  // Step 7: a partial overlap in the order the statement lists the travelers — Alice arrives
  //         first, Bob leaves last. The shared days run from Bob's arrival to Alice's leaving,
  //         March 10 to 20, which is 11
  @Test
  void aliceArrivingFirstAndBobLeavingLastSharesTheMiddle() {
    assertThat(sut.countDaysTogether("03-01", "03-20", "03-10", "03-31")).isEqualTo(11);
  }

  // Step 8: the mirror of Step 7 — Bob arrives first and Alice leaves last. The shared span is
  //         still March 10 to 20. Hard-coding Step 7's order as leaveAlice - arriveBob + 1
  //         answers 31
  @Test
  void bobArrivingFirstAndAliceLeavingLastSharesTheMiddle() {
    assertThat(sut.countDaysTogether("03-10", "03-31", "03-01", "03-20")).isEqualTo(11);
  }

  // Step 9: containment — Bob's whole stay, March 10 to 12, falls inside Alice's month, so the
  //         shared days are all of Bob's. The staggered formula from Step 8 answers 22, because
  //         Bob leaves first despite arriving last. The overlap always runs from the later
  //         arrival to the earlier leaving, whoever each one belongs to
  @Test
  void bobsStayInsideAlicesCountsAllOfBobs() {
    assertThat(sut.countDaysTogether("03-01", "03-31", "03-10", "03-12")).isEqualTo(3);
  }

  // Step 10: the mirror of Step 9 — Alice's stay inside Bob's. The staggered formula answers 12
  @Test
  void alicesStayInsideBobsCountsAllOfAlices() {
    assertThat(sut.countDaysTogether("03-10", "03-12", "03-01", "03-31")).isEqualTo(3);
  }

  // ===========================================================================================
  // A date is a day of the year (Steps 11-14). Each date has to become one number by adding the
  // lengths of the months before it — the statement's [31, 28, 31, 30, ...] table — and every
  // shortcut around that table breaks on some month boundary.
  // ===========================================================================================

  // Step 11: the shared days straddle a month end — January 31, February 1 and 2. Comparing the
  //          DD parts alone answers 0 (the raw value is 2 - 31 + 1 = -28), treating every month as
  //          30 days answers 2, and a prefix sum that also adds the date's own month answers 0
  @Test
  void overlapAcrossAMonthEndCountsBothMonths() {
    assertThat(sut.countDaysTogether("01-30", "02-02", "01-31", "02-05")).isEqualTo(3);
  }

  // Step 12: the shared days run through the end of February — February 28, March 1 and 2.
  //          February is the short month, so uniform months go wrong in both directions: 31 days
  //          each answers 6, 30 days each answers 5
  @Test
  void februaryHas28Days() {
    assertThat(sut.countDaysTogether("02-27", "03-02", "02-28", "03-05")).isEqualTo(3);
  }

  // Step 13: the Note sentence — the year is not a leap year, so there is no February 29. Both
  //          stay February 28 through March 1, which is 2 days. A calendar library pointed at a
  //          leap year (LocalDate in 2024, say) answers 3
  @Test
  void yearHasNoLeapDay() {
    assertThat(sut.countDaysTogether("02-28", "03-01", "02-28", "03-01")).isEqualTo(2);
  }

  // Step 14: a long overlap, March 10 to September 20, that walks seven entries of the table —
  //          22 + 30 + 31 + 30 + 31 + 31 + 20 = 195. A month length mistyped anywhere in between
  //          shows here. Uniform 31-day months answer 197, uniform 30-day months 191, the
  //          own-month prefix sum from Step 11 answers 194, and comparing DD alone answers 6
  @Test
  void overlapAcrossSevenMonthsSumsEveryMonthLength() {
    assertThat(sut.countDaysTogether("01-15", "09-20", "03-10", "12-25")).isEqualTo(195);
  }

  // ===========================================================================================
  // Official examples (Steps 15-16).
  // ===========================================================================================

  // Step 15: LeetCode Example 1 — Step 7's shape inside one month. The shared days are August 16,
  //          17 and 18, and the explanation lists them to confirm both endpoints count
  @Test
  void leetCodeExample1() {
    assertThat(sut.countDaysTogether("08-15", "08-18", "08-16", "08-19")).isEqualTo(3);
  }

  // Step 16: LeetCode Example 2 — Alice leaves October 31 and Bob arrives November 1, adjacent
  //          days across a month boundary that share nothing. Treating every month as 30 days
  //          answers 1, because October 31 then lands on the same number as November 1, and the
  //          own-month prefix sum from Step 11 collides the same way. Comparing DD alone sees days
  //          1 to 31 for both and answers 31
  @Test
  void leetCodeExample2() {
    assertThat(sut.countDaysTogether("10-01", "10-31", "11-01", "12-31")).isZero();
  }

  // ===========================================================================================
  // The calendar's bounds (Step 17). The input is always four 5-character dates, so there is no
  // input size to stress and no complexity class to separate — Hint 2's walk over all 365 days
  // is as fast as the arithmetic. The bounds that matter are the first and last days of the year.
  // ===========================================================================================

  // Step 17: both stay January 1 through December 31, so the answer is the length of a non-leap
  //          year. A leap-year calendar answers 366, uniform 31-day months 372, uniform 30-day
  //          months 361. The own-month prefix sum from Step 11 answers 365 here — it shifts
  //          January and December by the same 31 days — which is why Steps 11, 14 and 16 exist
  @Test
  void wholeYearTogetherIs365Days() {
    assertThat(sut.countDaysTogether("01-01", "12-31", "01-01", "12-31")).isEqualTo(365);
  }

  // ===========================================================================================
  // Hygiene (Step 18).
  // ===========================================================================================

  // Step 18: one instance answers several pairs of stays, the whole year in the middle and
  //          Example 1 both first and last. Alice's single December 31 inside Bob's whole year is
  //          containment at the far end of the calendar. A solution that keeps a running count or
  //          a per-call day table on the instance answers the repeated Example 1 wrong
  @Test
  void oneInstanceAnswersManyStaysInAnyOrder() {
    assertThat(sut.countDaysTogether("08-15", "08-18", "08-16", "08-19")).isEqualTo(3);
    assertThat(sut.countDaysTogether("05-01", "05-10", "05-20", "05-31")).isZero();
    assertThat(sut.countDaysTogether("01-01", "12-31", "01-01", "12-31")).isEqualTo(365);
    assertThat(sut.countDaysTogether("12-31", "12-31", "01-01", "12-31")).isEqualTo(1);
    assertThat(sut.countDaysTogether("02-28", "03-01", "02-28", "03-01")).isEqualTo(2);
    assertThat(sut.countDaysTogether("08-15", "08-18", "08-16", "08-19")).isEqualTo(3);
  }
}
