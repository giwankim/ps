package leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class AngleBetweenHandsOfAClockTest {
  AngleBetweenHandsOfAClock sut = new AngleBetweenHandsOfAClock();

  // The judge accepts any answer within 1e-5 of the true value, so every assertion compares the
  // double result against that same tolerance with isCloseTo rather than demanding exact equality.

  // Step 1: the degenerate base case — at 12:00 both hands rest on 12, so the angle is 0.
  // This also pins the hour=12 wrap: twelve o'clock must behave like 0, not 360 degrees.
  @Test
  void noonPlacesBothHandsTogetherForZeroAngle() {
    assertThat(sut.angleClock(12, 0)).isCloseTo(0, within(1e-5));
  }

  // Step 2: a whole hour with the minute hand on 12 isolates the hour-hand rule —
  // each hour advances the hour hand by 30 degrees (360 / 12), so 3:00 is 3 * 30.
  @Test
  void wholeHourAdvancesHourHandInThirtyDegreeSteps() {
    assertThat(sut.angleClock(3, 0)).isCloseTo(90, within(1e-5));
  }

  // Step 3: the smallest hour (1 <= hour) is a single 30-degree step.
  @Test
  void oneOClockIsThirtyDegrees() {
    assertThat(sut.angleClock(1, 0)).isCloseTo(30, within(1e-5));
  }

  // Step 4: at 6:00 the hands are diametrically opposite — the angle is 180, which is also the
  // boundary where the two arcs tie (180 vs 360 - 180), the largest a "smaller angle" can ever be.
  @Test
  void sixOClockHandsAreDiametricallyOpposite() {
    assertThat(sut.angleClock(6, 0)).isCloseTo(180, within(1e-5));
  }

  // Step 5: the key discriminator — at 9:00 the raw gap is 270, but we want the SMALLER arc, so the
  // answer is 360 - 270 = 90. A naive abs(hourAngle - minuteAngle) would wrongly return 270.
  @Test
  void takesTheSmallerOfTheTwoArcs() {
    assertThat(sut.angleClock(9, 0)).isCloseTo(90, within(1e-5));
  }

  // Step 6: the crux of the problem (Hint 1) — the minute hand drags the hour hand along. At 12:30
  // the minute hand is at 180 while the hour hand has crept to 15 (30 min * 0.5 deg/min), giving
  // 165 rather than the 180 you would get if the hour hand stayed on 12. (LeetCode Example 1)
  @Test
  void minuteHandDragsHourHandAtHalfPastTwelve() {
    assertThat(sut.angleClock(12, 30)).isCloseTo(165, within(1e-5));
  }

  // Step 7: the same drift at 3:30 — hour hand at 105 (90 + 15), minute hand at 180, gap 75.
  // (LeetCode Example 2)
  @Test
  void halfPastThreeAccountsForHourHandDrift() {
    assertThat(sut.angleClock(3, 30)).isCloseTo(75, within(1e-5));
  }

  // Step 8: the answer is a real number, not an integer — at 3:15 the hour hand has drifted to
  // 97.5 (90 + 15 * 0.5) while the minute hand sits exactly on 90, leaving a 7.5-degree gap.
  // (LeetCode Example 3)
  @Test
  void quarterPastThreeProducesAFractionalAngle() {
    assertThat(sut.angleClock(3, 15)).isCloseTo(7.5, within(1e-5));
  }

  // Step 9: the minute hand can sit well ahead of the hour hand — at 4:50 the minute hand is at 300
  // and the hour hand at 145 (120 + 25), a 155-degree smaller arc. (leetcode.ca Example 4)
  @Test
  void minuteHandAheadOfHourHandAtFiftyPastFour() {
    assertThat(sut.angleClock(4, 50)).isCloseTo(155, within(1e-5));
  }

  // Step 10: the largest minute value (minutes <= 59) combined with the 12-wrap — at 12:59 the
  // minute hand is at 354 and the hour hand at 29.5, a raw gap of 324.5 whose smaller arc is 35.5.
  @Test
  void largestMinuteWrapsAroundPastTwelve() {
    assertThat(sut.angleClock(12, 59)).isCloseTo(35.5, within(1e-5));
  }
}
