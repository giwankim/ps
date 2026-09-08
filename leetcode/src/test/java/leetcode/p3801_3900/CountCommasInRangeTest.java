package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountCommasInRangeTest {
  CountCommasInRange sut = new CountCommasInRange();

  // ===========================================================================================
  // Nothing below 1000 is formatted with a comma (Steps 1-5).
  // ===========================================================================================

  // Step 1: the constraint floor is 1 <= n, so a single "1" is the smallest legal input and it
  //         carries no comma. The closed form for this problem is n - 999, and this step is what
  //         forces the clamp: an unclamped n - 999 answers -998 here.
  @Test
  void n1ReturnsZero() {
    assertThat(sut.countCommas(1)).isZero();
  }

  // Step 2: the whole one-digit range 1..9 is still comma-free. A running total that starts from
  //         some seed rather than 0, or that counts numbers instead of commas, answers 9.
  @Test
  void n9ReturnsZero() {
    assertThat(sut.countCommas(9)).isZero();
  }

  // Step 3: two-digit numbers add nothing either. Together with Step 2 this pins that the count
  //         is over commas, not over digits or over numbers seen.
  @Test
  void n99ReturnsZero() {
    assertThat(sut.countCommas(99)).isZero();
  }

  // Step 4: the first three-digit number. "100" is written without a comma even though it has a
  //         full group of three digits. A solver who reads "a comma after every three digits"
  //         as d / 3 separators per d-digit number credits 100 with one comma and answers 1.
  @Test
  void n100ReturnsZero() {
    assertThat(sut.countCommas(100)).isZero();
  }

  // Step 5: the sharp edge of the "fewer than 4 digits contain no commas" rule. Every number in
  //         1..999 is comma-free, so the total is still 0. This is the decisive kill for the
  //         d / 3 misreading, which credits each of the 900 three-digit numbers with a comma
  //         and answers 900.
  @Test
  void n999ReturnsZero() {
    assertThat(sut.countCommas(999)).isZero();
  }

  // ===========================================================================================
  // The comma appears at 1000 and every number after it adds exactly one (Steps 6-9).
  // ===========================================================================================

  // Step 6: 1000 is written "1,000", the first number in the range to use a comma at all. Pins
  //         the threshold to the number itself rather than to the interval below it: the classic
  //         off-by-one n - 1000 answers 0 here.
  @Test
  void n1000ReturnsOne() {
    assertThat(sut.countCommas(1000)).isEqualTo(1);
  }

  // Step 7: the total accumulates. "1,000" plus "1,001" is two commas, so the answer is not a
  //         flag saying whether any comma occurs, and not the comma count of n alone — both of
  //         those readings answer 1.
  @Test
  void n1001ReturnsTwo() {
    assertThat(sut.countCommas(1001)).isEqualTo(2);
  }

  // Step 8: the full block 1000..1999 is a thousand numbers each contributing one comma. An
  //         off-by-one n - 1000 answers 999.
  @Test
  void n1999Returns1000() {
    assertThat(sut.countCommas(1999)).isEqualTo(1000);
  }

  // Step 9: crossing into the next thousand adds one more comma, not a second comma per number.
  //         2000 is "2,000" with a single separator, so the total is 1001. A solver who counts
  //         one comma per completed thousand (n / 1000) answers 2.
  @Test
  void n2000Returns1001() {
    assertThat(sut.countCommas(2000)).isEqualTo(1001);
  }

  // ===========================================================================================
  // Wider numbers still carry exactly one comma (Steps 10-13).
  // ===========================================================================================

  // Step 10: 9999 is "9,999" — four digits, one comma — so the block 1000..9999 contributes
  //          9000 and the total is 9000. Pins that the comma count per number has not grown yet.
  @Test
  void n9999Returns9000() {
    assertThat(sut.countCommas(9999)).isEqualTo(9000);
  }

  // Step 11: the four-to-five digit boundary. 10000 is "10,000", still a single comma, so the
  //          total ticks up by one to 9001. A solver who adds a comma per group of three digits
  //          rather than per gap between groups starts overcounting here.
  @Test
  void n10000Returns9001() {
    assertThat(sut.countCommas(10000)).isEqualTo(9001);
  }

  // Step 12: the whole five-digit block behaves the same way, so 99999 gives 99999 - 999 = 99000.
  //          A solution that special-cases digit widths and forgets one of them lands short here.
  @Test
  void n99999Returns99000() {
    assertThat(sut.countCommas(99999)).isEqualTo(99000);
  }

  // Step 13: the invariant behind the closed form, stated directly on one instance — inside the
  //          comma-using range consecutive answers differ by exactly one, because each new
  //          number brings exactly one new comma. 5000 -> 4001 and 5001 -> 4002.
  @Test
  void consecutiveInputsAboveThresholdDifferByOne() {
    assertThat(sut.countCommas(5000)).isEqualTo(4001);
    assertThat(sut.countCommas(5001)).isEqualTo(4002);
  }

  // ===========================================================================================
  // The official examples (Steps 14-15).
  // ===========================================================================================

  // Step 14: LeetCode example 1. "1,000", "1,001" and "1,002" carry one comma each, so the
  //          answer is 3 — the count of commas, not of numbers containing one thousand. The
  //          explanation exists to rule out n / 1000, which answers 1, and n - 1000, which
  //          answers 2.
  @Test
  void leetCodeExample1() {
    assertThat(sut.countCommas(1002)).isEqualTo(3);
  }

  // Step 15: LeetCode example 2. Every number up to 998 has fewer than four digits, so the answer
  //          is 0 rather than a negative number or a per-number digit tally. An unclamped
  //          n - 999 answers -1 and the d / 3 misreading answers 899.
  @Test
  void leetCodeExample2() {
    assertThat(sut.countCommas(998)).isZero();
  }

  // ===========================================================================================
  // The constraint ceiling (Step 16). n <= 10^5, so a per-number digit scan is about 10^5 cheap
  // steps and finishes in milliseconds, as does the closed form. The timeout is here to catch an
  // accidentally quadratic accumulation — recomputing the running total from scratch for every
  // prefix is roughly 10^10 operations and cannot finish.
  // ===========================================================================================

  // Step 16: the largest legal input. 100000 is written "100,000": six digits but still a single
  //          comma, because commas sit in the gaps between groups and six digits make only one
  //          gap. Reading the rule as d / 3 separators credits this number with two and inflates
  //          the total to 99902; the answer is 100000 - 999 = 99001.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void n100000ReturnsMaximumTotal() {
    assertThat(sut.countCommas(100_000)).isEqualTo(99_001);
  }

  // ===========================================================================================
  // Hygiene (Steps 17-18).
  // ===========================================================================================

  // Step 17: one instance answers many inputs, deliberately out of order with the largest in the
  //          middle. An implementation that accumulates into a field instead of a local — or that
  //          memoizes the previous n and returns a stale total — passes every single-call step
  //          above and fails here.
  @Test
  void oneInstanceAnswersManyInputsOutOfOrder() {
    assertThat(sut.countCommas(1002)).isEqualTo(3);
    assertThat(sut.countCommas(100_000)).isEqualTo(99_001);
    assertThat(sut.countCommas(998)).isZero();
    assertThat(sut.countCommas(2000)).isEqualTo(1001);
  }

  // Step 18: the method is a pure function of n, so asking twice gives the same answer twice.
  //          Catches a total that keeps growing across calls, which Step 17 could in principle
  //          miss if the leftover state happened to cancel out.
  @Test
  void repeatedCallsWithSameInputAgree() {
    assertThat(sut.countCommas(1002)).isEqualTo(3);
    assertThat(sut.countCommas(1002)).isEqualTo(3);
  }
}
