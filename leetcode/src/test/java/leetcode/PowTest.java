package leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class PowTest {
  // LeetCode judges x^n to 5 decimal places, so match that as the comparison budget.
  private static final double TOLERANCE = 1e-5;

  Pow sut = new Pow();

  // Step 1: x^0 == 1 — the base case that anchors binary exponentiation.
  @Test
  void zeroExponentReturnsOne() {
    assertThat(sut.myPow(2.0, 0)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, 0)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 2: x^1 == x — the smallest non-trivial exponent.
  @Test
  void exponentOneReturnsBase() {
    assertThat(sut.myPow(2.0, 1)).isCloseTo(2.0, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, 1)).isCloseTo(2.0, within(TOLERANCE));
  }

  // Step 3: LC Example 1 — a plain positive exponent on an integer base.
  @Test
  void leetCodeExampleOnePositiveExponent() {
    assertThat(sut.myPow(2.0, 10)).isCloseTo(1024.0, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, 10)).isCloseTo(1024.0, within(TOLERANCE));
  }

  // Step 4: LC Example 2 — a fractional base accumulates rounding across squarings.
  @Test
  void leetCodeExampleTwoFractionalBase() {
    assertThat(sut.myPow(2.1, 3)).isCloseTo(9.261, within(TOLERANCE));
    assertThat(sut.myPow2(2.1, 3)).isCloseTo(9.261, within(TOLERANCE));
  }

  // Step 5: LC Example 3 — a negative exponent yields the reciprocal power.
  @Test
  void leetCodeExampleThreeNegativeExponent() {
    assertThat(sut.myPow(2.0, -2)).isCloseTo(0.25, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, -2)).isCloseTo(0.25, within(TOLERANCE));
  }

  // Step 6: x^-1 == 1/x — the simplest reciprocal case.
  @Test
  void exponentNegativeOneReturnsReciprocal() {
    assertThat(sut.myPow(2.0, -1)).isCloseTo(0.5, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, -1)).isCloseTo(0.5, within(TOLERANCE));
  }

  // Step 7: 1 raised to any power stays 1, even for the largest exponent.
  @Test
  void baseOfOneIsAlwaysOne() {
    assertThat(sut.myPow(1.0, 2_147_483_647)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(1.0, 2_147_483_647)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 8: a negative base with an odd exponent keeps the sign.
  @Test
  void negativeBaseOddExponentStaysNegative() {
    assertThat(sut.myPow(-2.0, 3)).isCloseTo(-8.0, within(TOLERANCE));
    assertThat(sut.myPow2(-2.0, 3)).isCloseTo(-8.0, within(TOLERANCE));
  }

  // Step 9: a negative base with an even exponent becomes positive.
  @Test
  void negativeBaseEvenExponentIsPositive() {
    assertThat(sut.myPow(-2.0, 2)).isCloseTo(4.0, within(TOLERANCE));
    assertThat(sut.myPow2(-2.0, 2)).isCloseTo(4.0, within(TOLERANCE));
  }

  // Step 10: a negative base with a negative odd exponent is a negative reciprocal.
  @Test
  void negativeBaseNegativeOddExponent() {
    assertThat(sut.myPow(-2.0, -3)).isCloseTo(-0.125, within(TOLERANCE));
    assertThat(sut.myPow2(-2.0, -3)).isCloseTo(-0.125, within(TOLERANCE));
  }

  // Step 11: -1 raised to an even exponent collapses to 1.
  @Test
  void baseNegativeOneEvenExponentReturnsOne() {
    assertThat(sut.myPow(-1.0, 2)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(-1.0, 2)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 12: -1 raised to an odd exponent collapses to -1.
  @Test
  void baseNegativeOneOddExponentReturnsNegativeOne() {
    assertThat(sut.myPow(-1.0, 3)).isCloseTo(-1.0, within(TOLERANCE));
    assertThat(sut.myPow2(-1.0, 3)).isCloseTo(-1.0, within(TOLERANCE));
  }

  // Step 13: 0 raised to a positive exponent is 0 (constraints allow x == 0 only when n > 0).
  @Test
  void zeroBasePositiveExponentReturnsZero() {
    assertThat(sut.myPow(0.0, 5)).isCloseTo(0.0, within(TOLERANCE));
    assertThat(sut.myPow2(0.0, 5)).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 14: a base between 0 and 1 with a positive exponent shrinks toward 0.
  @Test
  void fractionalBaseLessThanOnePositiveExponent() {
    assertThat(sut.myPow(0.5, 3)).isCloseTo(0.125, within(TOLERANCE));
    assertThat(sut.myPow2(0.5, 3)).isCloseTo(0.125, within(TOLERANCE));
  }

  // Step 15: a base between 0 and 1 with a negative exponent grows above 1.
  @Test
  void fractionalBaseNegativeExponentGrows() {
    assertThat(sut.myPow(0.5, -2)).isCloseTo(4.0, within(TOLERANCE));
    assertThat(sut.myPow2(0.5, -2)).isCloseTo(4.0, within(TOLERANCE));
  }

  // Step 16: a power-of-two exponent (1000b) exercises the pure repeated-squaring path.
  @Test
  void evenExponentUsesPureSquaring() {
    assertThat(sut.myPow(3.0, 8)).isCloseTo(6561.0, within(TOLERANCE));
    assertThat(sut.myPow2(3.0, 8)).isCloseTo(6561.0, within(TOLERANCE));
  }

  // Step 17: an exponent with several set bits (101b) mixes squaring with accumulation.
  @Test
  void oddExponentMixesSetBits() {
    assertThat(sut.myPow(3.0, 5)).isCloseTo(243.0, within(TOLERANCE));
    assertThat(sut.myPow2(3.0, 5)).isCloseTo(243.0, within(TOLERANCE));
  }

  // Step 18: repeated squaring of a near-one base must stay within the 5-decimal budget.
  @Test
  void repeatedSquaringStaysAccurate() {
    assertThat(sut.myPow(1.1, 10)).isCloseTo(2.59374, within(TOLERANCE));
    assertThat(sut.myPow2(1.1, 10)).isCloseTo(2.59374, within(TOLERANCE));
  }

  // Step 19: the result may reach the constraint bound of 10^4.
  @Test
  void resultReachesMaximumMagnitudeBound() {
    assertThat(sut.myPow(10.0, 4)).isCloseTo(10_000.0, within(TOLERANCE));
    assertThat(sut.myPow2(10.0, 4)).isCloseTo(10_000.0, within(TOLERANCE));
  }

  // Step 20: a base just under the |x| < 100 bound still squares correctly.
  @Test
  void baseNearConstraintUpperBound() {
    assertThat(sut.myPow(99.0, 2)).isCloseTo(9801.0, within(TOLERANCE));
    assertThat(sut.myPow2(99.0, 2)).isCloseTo(9801.0, within(TOLERANCE));
  }

  // Step 21: Integer.MAX_VALUE as the exponent must not overflow the loop counter.
  @Test
  void integerMaxValueExponentWithBaseOne() {
    assertThat(sut.myPow(1.0, Integer.MAX_VALUE)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(1.0, Integer.MAX_VALUE)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 22: Integer.MAX_VALUE is odd, so a -1 base stays -1.
  @Test
  void integerMaxValueOddExponentWithBaseNegativeOne() {
    assertThat(sut.myPow(-1.0, Integer.MAX_VALUE)).isCloseTo(-1.0, within(TOLERANCE));
    assertThat(sut.myPow2(-1.0, Integer.MAX_VALUE)).isCloseTo(-1.0, within(TOLERANCE));
  }

  // Step 23: the classic trap — negating Integer.MIN_VALUE overflows int, so the
  // implementation must widen to long before flipping the sign of the exponent.
  @Test
  void integerMinValueExponentDoesNotOverflow() {
    assertThat(sut.myPow(1.0, Integer.MIN_VALUE)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(1.0, Integer.MIN_VALUE)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 24: Integer.MIN_VALUE is even, so a -1 base resolves to 1 after the safe negation.
  @Test
  void integerMinValueEvenExponentWithBaseNegativeOne() {
    assertThat(sut.myPow(-1.0, Integer.MIN_VALUE)).isCloseTo(1.0, within(TOLERANCE));
    assertThat(sut.myPow2(-1.0, Integer.MIN_VALUE)).isCloseTo(1.0, within(TOLERANCE));
  }

  // Step 25: |x| > 1 raised to Integer.MIN_VALUE underflows the double to 0.
  @Test
  void integerMinValueExponentUnderflowsToZero() {
    assertThat(sut.myPow(2.0, Integer.MIN_VALUE)).isCloseTo(0.0, within(TOLERANCE));
    assertThat(sut.myPow2(2.0, Integer.MIN_VALUE)).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 26: an extreme exponent must resolve via O(log n) fast exponentiation,
  // not an O(n) loop that would take billions of iterations.
  @Test
  void largeExponentComputesViaFastExponentiation() {
    assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
      assertThat(sut.myPow(1.0, Integer.MAX_VALUE)).isCloseTo(1.0, within(TOLERANCE));
      assertThat(sut.myPow2(1.0, Integer.MAX_VALUE)).isCloseTo(1.0, within(TOLERANCE));
    });
  }
}
