package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountCommasInRangeIITest {
  CountCommasInRangeII sut = new CountCommasInRangeII();

  // ===========================================================================================
  // Nothing below 1000 is formatted with a comma (Steps 1-2).
  // ===========================================================================================

  // Step 1: the constraint floor is 1 <= n, so a single "1" is the smallest legal input and it
  //         carries no comma. The band arithmetic only starts at 1000, so this step forces the
  //         clamp: a solver who reuses problem I's closed form n - 999 without clamping answers
  //         -998 here.
  @Test
  void n1ReturnsZero() {
    assertThat(sut.countCommas(1)).isZero();
  }

  // Step 2: 999 is the sharp edge of "numbers with fewer than 4 digits contain no commas". Every
  //         number in 1..999 is comma-free, so the total is still 0. Kills the d / 3 misreading
  //         (a comma per completed group of three digits rather than per gap between groups),
  //         which credits each of the 900 three-digit numbers with a comma and answers 900.
  @Test
  void n999ReturnsZero() {
    assertThat(sut.countCommas(999)).isZero();
  }

  // ===========================================================================================
  // One comma per number across the four-to-six-digit band (Steps 3-5).
  // ===========================================================================================

  // Step 3: 1000 is written "1,000", the first number in the range to use a comma. Pins the
  //         threshold to the number itself: a band size computed as hi - lo without the + 1
  //         answers 0 here.
  @Test
  void n1000ReturnsOne() {
    assertThat(sut.countCommas(1000)).isEqualTo(1);
  }

  // Step 4: the total accumulates over the range. "1,000" plus "1,001" is two commas, so the
  //         answer is neither a flag saying whether any comma occurs nor the comma count of n
  //         alone. Both of those readings answer 1.
  @Test
  void n1001ReturnsTwo() {
    assertThat(sut.countCommas(1001)).isEqualTo(2);
  }

  // Step 5: 999,999 is the last number with a single comma. Four-, five-, and six-digit numbers
  //         all have exactly one gap between digit groups, so the band 1000..999999 contributes
  //         999,000. Up to here the answer coincides with problem I's n - 999. The d / 3
  //         misreading credits each six-digit number with two commas and answers 1,899,900; a
  //         band size of hi - lo without the + 1 answers 998,999.
  @Test
  void n999999Returns999000() {
    assertThat(sut.countCommas(999_999)).isEqualTo(999_000);
  }

  // ===========================================================================================
  // The second comma, where this problem leaves problem I behind (Steps 6-10).
  // ===========================================================================================

  // Step 6: 1,000,000 is the first number with two commas, so it adds 2 to the running total and
  //         the answer is 999,002. This is the one step that separates 3871 from 3870: problem I's
  //         closed form n - 999 answers 999,001, which is why the ceiling rose from 10^5 to 10^15.
  //         An inclusive band [10^3, 10^6] that also counts 10^6 in the one-comma band answers
  //         999,003, and counting [1, n) instead of [1, n] answers 999,000.
  @Test
  void n1e6Returns999002() {
    assertThat(sut.countCommas(1_000_000)).isEqualTo(999_002);
  }

  // Step 7: every seven-digit number adds two commas, so consecutive answers in this band differ
  //         by 2 rather than 1: 1,000,001 answers 999,004. A solver who treats the second comma as
  //         a one-off at 10^6 and then returns to one per number answers 999,003; n - 999 answers
  //         999,002.
  @Test
  void n1e6Plus1Returns999004() {
    assertThat(sut.countCommas(1_000_001)).isEqualTo(999_004);
  }

  // Step 8: a partial block in the middle of the two-comma band. 999,000 for the whole one-comma
  //         band plus (1,234,567 - 999,999) * 2 = 234,568 * 2 = 469,136 gives 1,468,136. Pins
  //         min(n, hi) - lo + 1 as the size of the last, partial band: hi - lo without the + 1
  //         answers 1,468,133, an inclusive band end answers 1,468,137, and n - 999 answers
  //         1,233,568.
  @Test
  void nMidTwoCommaBandUsesPartialBlock() {
    assertThat(sut.countCommas(1_234_567)).isEqualTo(1_468_136);
  }

  // Step 9: the whole seven-digit block: 9,000,000 numbers at two commas each is 18,000,000, plus
  //         999,000 from the one-comma band, is 18,999,000. Here more than half of all commas are
  //         second commas, so n - 999 is not even close at 9,999,000.
  @Test
  void n9999999Returns18999000() {
    assertThat(sut.countCommas(9_999_999)).isEqualTo(18_999_000);
  }

  // Step 10: 999,999,999 closes the two-comma band, which spans seven, eight, and nine digits:
  //          999,000 + 999,000,000 * 2 = 1,998,999,000. This is the largest answer in the suite
  //          that still fits an int (Integer.MAX_VALUE is 2,147,483,647), so an int accumulator
  //          survives this step and breaks only in Step 12. An inclusive band end answers
  //          1,998,999,001.
  @Test
  void nJustBelow1e9EndsTwoCommaBand() {
    assertThat(sut.countCommas(999_999_999)).isEqualTo(1_998_999_000);
  }

  // ===========================================================================================
  // Third and fourth commas, and arithmetic that no longer fits an int (Steps 11-15). From 10^10
  // upward the timeouts separate a per-number scan, which is at least 10^10 digit counts and
  // cannot finish, from the intended per-band arithmetic, which is five iterations.
  // ===========================================================================================

  // Step 11: 10^9 is written "1,000,000,000" with three commas, so it adds 3 to the two-comma
  //          total of Step 10 and answers 1,998,999,003. An inclusive band end, which by now has
  //          double-counted both 10^6 and 10^9, answers 1,998,999,006; n - 999 answers
  //          999,999,001.
  @Test
  void n1e9StartsThirdComma() {
    assertThat(sut.countCommas(1_000_000_000L)).isEqualTo(1_998_999_003L);
  }

  // Step 12: 10^10 is the first input whose answer exceeds Integer.MAX_VALUE: 1,998,999,003 plus
  //          9,000,000,000 * 3 = 28,998,999,003. A solver who accumulates in an int, or who casts
  //          the band size 9,000,000,001 to int before multiplying, wraps to -1,065,772,069. The
  //          method returns long for exactly this reason.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n1e10ExceedsIntRange() {
    assertThat(sut.countCommas(10_000_000_000L)).isEqualTo(28_998_999_003L);
  }

  // Step 13: 999,999,999,999 closes the three-comma band, which spans ten, eleven, and twelve
  //          digits: 999,000 + 1,998,000,000 + 999,000,000,000 * 3 = 2,998,998,999,000. A band
  //          size of 999,000,000,000 cannot be held in an int at all, so any int-typed
  //          intermediate is wrong here even before multiplying. An inclusive band end answers
  //          2,998,998,999,003 and hi - lo without the + 1 answers 2,998,998,998,994.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void nJustBelow1e12EndsThreeCommaBand() {
    assertThat(sut.countCommas(999_999_999_999L)).isEqualTo(2_998_998_999_000L);
  }

  // Step 14: 10^12 is written "1,000,000,000,000" with four commas and adds 4 to the total of
  //          Step 13, giving 2,998,998,999,004. An inclusive band end answers 2,998,998,999,010
  //          and hi - lo without the + 1 answers 2,998,998,998,994.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n1e12StartsFourthComma() {
    assertThat(sut.countCommas(1_000_000_000_000L)).isEqualTo(2_998_998_999_004L);
  }

  // Step 15: a fifteen-digit input that is not a power of ten, in the middle of the four-comma
  //          band. The three full bands give 2,998,998,999,000, and the partial fourth band adds
  //          (123,456,789,012,345 - 999,999,999,999) * 4 = 122,456,789,012,346 * 4 =
  //          489,827,156,049,384, for a total of 492,826,155,048,384. An inclusive band end
  //          answers 492,826,155,048,390, hi - lo without the + 1 answers 492,826,155,048,374,
  //          and counting [1, n) answers 492,826,155,048,380.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void nMidFourCommaBandUsesPartialBlock() {
    assertThat(sut.countCommas(123_456_789_012_345L)).isEqualTo(492_826_155_048_384L);
  }

  // ===========================================================================================
  // The official examples (Steps 16-17).
  // ===========================================================================================

  // Step 16: LeetCode example 1. "1,000", "1,001" and "1,002" carry one comma each, so the answer
  //          is 3. The explanation enumerates the three formatted strings to rule out n / 1000,
  //          which answers 1, and n - 1000, which answers 2.
  @Test
  void leetCodeExample1() {
    assertThat(sut.countCommas(1002)).isEqualTo(3);
  }

  // Step 17: LeetCode example 2. Every number up to 998 has fewer than four digits, so the answer
  //          is 0 rather than anything negative or any per-number digit tally. An unclamped
  //          n - 999 answers -1 and the d / 3 misreading answers 899.
  @Test
  void leetCodeExample2() {
    assertThat(sut.countCommas(998)).isZero();
  }

  // ===========================================================================================
  // The constraint ceiling (Steps 18-19). n <= 10^15, so the answer approaches 4 * 10^15 and
  // needs a long, and a per-number scan would take 10^15 steps. Only per-band arithmetic
  // finishes.
  // ===========================================================================================

  // Step 18: 10^15 - 1 is the last fifteen-digit number and the last with four commas. Summing
  //          the four full bands, 999,000 + 1,998,000,000 + 2,997,000,000,000 +
  //          3,996,000,000,000,000, gives 3,998,998,998,999,000. An inclusive band end answers
  //          3,998,998,998,999,006 and hi - lo without the + 1 answers 3,998,998,998,998,990.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void nJustBelow1e15ReturnsFourCommaTotal() {
    assertThat(sut.countCommas(999_999_999_999_999L)).isEqualTo(3_998_998_998_999_000L);
  }

  // Step 19: the largest legal input. 10^15 is the only sixteen-digit number in range and is
  //          written "1,000,000,000,000,000" with five commas, so the answer is Step 18 plus 5:
  //          3,998,998,998,999,005. A band loop hard-coded to four comma groups, or to the fifteen
  //          digits the constraint mentions, misses the fifth comma and answers
  //          3,998,998,998,999,000, as does counting [1, n). An inclusive band end answers
  //          3,998,998,998,999,015.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n1e15CarriesFiveCommas() {
    assertThat(sut.countCommas(1_000_000_000_000_000L)).isEqualTo(3_998_998_998_999_005L);
  }

  // ===========================================================================================
  // Hygiene (Steps 20-21).
  // ===========================================================================================

  // Step 20: one instance answers many inputs, deliberately out of order with the largest in the
  //          middle. An implementation that accumulates into a field instead of a local, or that
  //          memoizes the previous n and returns a stale total, passes every single-call step
  //          above and fails here.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputsOutOfOrder() {
    assertThat(sut.countCommas(1002)).isEqualTo(3);
    assertThat(sut.countCommas(1_000_000_000_000_000L)).isEqualTo(3_998_998_998_999_005L);
    assertThat(sut.countCommas(998)).isZero();
    assertThat(sut.countCommas(1_000_000)).isEqualTo(999_002);
  }

  // Step 21: the method is a pure function of n, so asking twice gives the same answer twice.
  //          Catches a total that keeps growing across calls, which Step 20 could in principle
  //          miss if the leftover state happened to cancel out.
  @Test
  void repeatedCallsWithSameInputAgree() {
    assertThat(sut.countCommas(1_234_567)).isEqualTo(1_468_136);
    assertThat(sut.countCommas(1_234_567)).isEqualTo(1_468_136);
  }
}
