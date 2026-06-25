package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class FindTheHighestAltitudeTest {
  FindTheHighestAltitude sut = new FindTheHighestAltitude();

  // Step 1: smallest valid input (n == 1) — a single climb from the origin.
  // altitudes = [0, 1] -> highest is 1
  @Test
  void singlePositiveGainClimbsToThatAltitude() {
    assertThat(sut.largestAltitude(new int[] {1})).isEqualTo(1);
  }

  // Step 2: a single descent — the biker only ever goes down, so the starting
  // point 0 is the highest. This pins down that the origin is always a candidate.
  // altitudes = [0, -5] -> highest is 0
  @Test
  void singleNegativeGainKeepsTheStartingAltitude() {
    assertThat(sut.largestAltitude(new int[] {-5})).isEqualTo(0);
  }

  // Step 3: every gain is positive, so altitude rises monotonically and the
  // final point is the peak.
  // altitudes = [0, 1, 3, 6] -> highest is 6
  @Test
  void monotonicAscentPeaksAtTheFinalPoint() {
    assertThat(sut.largestAltitude(new int[] {1, 2, 3})).isEqualTo(6);
  }

  // Step 4: every gain is negative, so altitude falls monotonically and nothing
  // ever beats the origin.
  // altitudes = [0, -1, -3, -6] -> highest is 0
  @Test
  void monotonicDescentNeverBeatsTheOrigin() {
    assertThat(sut.largestAltitude(new int[] {-1, -2, -3})).isEqualTo(0);
  }

  // Step 5: the trip dips below the origin first, then recovers past it — the
  // scan must keep looking after an early low to find a later, higher peak.
  // altitudes = [0, -2, 1, 2] -> highest is 2
  @Test
  void dipThenRecoveryReachesANewHigh() {
    assertThat(sut.largestAltitude(new int[] {-2, 3, 1})).isEqualTo(2);
  }

  // Step 6: zero gains hold the altitude flat (a plateau), so the maximum value
  // repeats across several points without changing the answer.
  // altitudes = [0, 3, 3, 3, 2] -> highest is 3
  @Test
  void plateauHoldsTheMaximum() {
    assertThat(sut.largestAltitude(new int[] {3, 0, 0, -1})).isEqualTo(3);
  }

  // Step 7: LeetCode Example 1 — climbs to a peak, plateaus, then descends.
  // altitudes = [0, -5, -4, 1, 1, -6] -> highest is 1
  @Test
  void leetCodeExample1() {
    assertThat(sut.largestAltitude(new int[] {-5, 1, 5, 0, -7})).isEqualTo(1);
  }

  // Step 8: LeetCode Example 2 — a long descent followed by a partial recovery
  // that never climbs back above the origin.
  // altitudes = [0, -4, -7, -9, -10, -6, -3, -1] -> highest is 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.largestAltitude(new int[] {-4, -3, -2, -1, 4, 3, 2})).isEqualTo(0);
  }

  // Step 9: upper constraint bounds (n == 100, every gain == 100) — the altitude
  // climbs to 100 * 100, comfortably within int range.
  // altitudes peak at 10_000
  @Test
  void maximumPositiveGainsStayWithinIntRange() {
    int[] gain = new int[100];
    Arrays.fill(gain, 100);
    assertThat(sut.largestAltitude(gain)).isEqualTo(10_000);
  }

  // Step 10: lower constraint bounds (n == 100, every gain == -100) — a maximal
  // descent still leaves the origin as the highest point.
  // altitudes bottom out at -10_000 -> highest is 0
  @Test
  void maximumNegativeGainsKeepTheOrigin() {
    int[] gain = new int[100];
    Arrays.fill(gain, -100);
    assertThat(sut.largestAltitude(gain)).isEqualTo(0);
  }
}
