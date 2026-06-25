package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FactorialTrailingZeroesTest {
  FactorialTrailingZeroes sut = new FactorialTrailingZeroes();

  @Test
  void zeroFactorialHasNoTrailingZeroes() {
    // Lower bound of the 0 <= n <= 10^4 constraint; 0! = 1 by convention.
    assertThat(sut.trailingZeroes(0)).isZero();
  }

  @Test
  void factorialOfOneHasNoTrailingZeroes() {
    assertThat(sut.trailingZeroes(1)).isZero();
  }

  @Test
  void factorialOfThreeHasNoTrailingZeroes() {
    // LeetCode example 1: 3! = 6.
    assertThat(sut.trailingZeroes(3)).isZero();
  }

  @Test
  void noTrailingZeroJustBelowTheFirstMultipleOfFive() {
    // 4! = 24; the last input before any factor of 5 appears.
    assertThat(sut.trailingZeroes(4)).isZero();
  }

  @Test
  void factorialOfFiveHasOneTrailingZero() {
    // LeetCode example 2: 5! = 120, the first input that gains a trailing zero.
    assertThat(sut.trailingZeroes(5)).isOne();
  }

  @Test
  void countStaysConstantBetweenConsecutiveMultiplesOfFive() {
    // 6! = 720 through 9! = 362880 each carry exactly one factor of 5,
    // so the count plateaus at 1 across the whole range.
    assertThat(sut.trailingZeroes(6)).isOne();
    assertThat(sut.trailingZeroes(9)).isOne();
  }

  @Test
  void factorialOfTenHasTwoTrailingZeroes() {
    // Second multiple of 5 (5 and 10) contributes a second factor: 10! = 3628800.
    assertThat(sut.trailingZeroes(10)).isEqualTo(2);
  }

  @Test
  void multipleOfFiveJustBelowTheFirstSquareOfFive() {
    // 24 / 5 = 4 with no extra contribution yet from 25.
    assertThat(sut.trailingZeroes(24)).isEqualTo(4);
  }

  @Test
  void squareOfFiveContributesAnExtraZero() {
    // 25 = 5^2 adds two factors of 5 at once, so the count jumps 4 -> 6 (skipping 5):
    // 25/5 + 25/25 = 5 + 1 = 6.
    assertThat(sut.trailingZeroes(25)).isEqualTo(6);
  }

  @Test
  void cubeOfFiveContributesYetAnotherZero() {
    // 125 = 5^3 contributes a third tier: 125/5 + 125/25 + 125/125 = 25 + 5 + 1 = 31.
    assertThat(sut.trailingZeroes(125)).isEqualTo(31);
  }

  @Test
  void upperBoundOfTheConstraintRange() {
    // n = 10^4, the maximum allowed input:
    // 10000/5 + 10000/25 + 10000/125 + 10000/625 + 10000/3125 = 2000 + 400 + 80 + 16 + 3 = 2499.
    assertThat(sut.trailingZeroes(10000)).isEqualTo(2499);
  }
}
