package leetcode.p3601_3700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CheckDivisibilityByDigitSumAndProductTest {
  CheckDivisibilityByDigitSumAndProduct sut = new CheckDivisibilityByDigitSumAndProduct();

  // =============================================================================================
  // Single digits, where the divisor is larger than n itself (Steps 1-2).
  // =============================================================================================

  // Step 1: the constraint floor n = 1. Its only digit is both the sum and the product, so the
  //         divisor is 1 + 1 = 2 and 1 leaves remainder 1. Every solution that divides by the sum
  //         alone, by the product alone, by the sum times the product, or that asks whether the
  //         divisor is divisible by n rather than the other way round, answers true here
  @Test
  void n1IsNotDivisibleBecauseTheDivisorIsTwo() {
    assertThat(sut.checkDivisibility(1)).isFalse();
  }

  // Step 2: the generalization of Step 1 and the only family where the divisor exceeds n. A single
  //         digit d has sum d and product d, so the divisor is always 2d, and d leaves remainder d
  //         against 2d. Nothing in 1 through 9 is ever divisible
  @Test
  void n1To9AreNeverDivisible() {
    for (int n = 1; n <= 9; n++) {
      assertThat(sut.checkDivisibility(n)).as("n = %d", n).isFalse();
    }
  }

  // =============================================================================================
  // Two digits — both halves of the divisor matter (Steps 3-7).
  // =============================================================================================

  // Step 3: the first input containing a zero digit. The product collapses to 0, so the divisor is
  //         the digit sum 1 alone and every number is divisible by 1. A solution that divides by
  //         the product alone, or by the sum times the product, divides by zero and throws
  //         ArithmeticException. A solution that tests whether the divisor is divisible by n gets
  //         1 against 10 and answers false
  @Test
  void n10IsDivisibleBecauseTheZeroDigitLeavesOnlyTheDigitSum() {
    assertThat(sut.checkDivisibility(10)).isTrue();
  }

  // Step 4: the mirror of Step 3 for a trailing zero on a larger leading digit — sum 2, product 0,
  //         divisor 2, and 20 is twice 10. The zero digit must zero the product rather than be
  //         skipped over
  @Test
  void n20IsDivisibleByItsDigitSumAlone() {
    assertThat(sut.checkDivisibility(20)).isTrue();
  }

  // Step 5: the product is not optional. Sum 3, product 2, divisor 5, and 12 leaves remainder 2.
  //         A solution that forgets the product entirely — or that initializes the running product
  //         to 0 instead of 1, which amounts to the same thing — divides 12 by 3 and answers true
  @Test
  void n12IsNotDivisibleEvenThoughTheDigitSumDividesIt() {
    assertThat(sut.checkDivisibility(12)).isFalse();
  }

  // Step 6: the sum is not optional either. Sum 6, product 8, divisor 14, and 42 is 14 times 3.
  //         A solution that divides by the product alone gets remainder 2, and one that multiplies
  //         instead of adding gets a divisor of 48, which is larger than 42
  @Test
  void n42IsDivisibleOnlyWhenSumAndProductAreAdded() {
    assertThat(sut.checkDivisibility(42)).isTrue();
  }

  // Step 7: the boundary case where the divisor equals n exactly — sum 17, product 72, divisor 89.
  //         A solution that guards with a strict "divisor smaller than n" test, or that returns
  //         early when the divisor reaches n, answers false
  @Test
  void n89IsDivisibleBecauseTheDivisorEqualsN() {
    assertThat(sut.checkDivisibility(89)).isTrue();
  }

  // =============================================================================================
  // Zero digits anywhere in the number (Steps 8-10).
  // =============================================================================================

  // Step 8: an interior zero, not a trailing one. Sum 3, product 0, divisor 3, and 102 is 3 times
  //         34. A solution that multiplies only the nonzero digits gets a product of 2, a divisor
  //         of 5, and answers false. The zero has to reach the product wherever it sits
  @Test
  void n102IsDivisibleWhenTheZeroSitsBetweenNonzeroDigits() {
    assertThat(sut.checkDivisibility(102)).isTrue();
  }

  // Step 9: the trap Steps 3, 4 and 8 set up. A zero digit does not make the answer true, it only
  //         reduces the divisor to the digit sum — here sum 6, product 0, divisor 6, and 105
  //         leaves remainder 3. A solution that short-circuits to true as soon as it sees a zero
  //         digit answers true
  @Test
  void n105IsNotDivisibleDespiteContainingAZeroDigit() {
    assertThat(sut.checkDivisibility(105)).isFalse();
  }

  // Step 10: more than one zero digit. Sum 2, product 0, divisor 2, and 110 is 2 times 55. The
  //          product stays 0 rather than resetting on the second zero
  @Test
  void n110IsDivisibleWithTwoZeroDigits() {
    assertThat(sut.checkDivisibility(110)).isTrue();
  }

  // =============================================================================================
  // Longer numbers, where the product outgrows the sum (Steps 11-12).
  // =============================================================================================

  // Step 11: three repeated nines. Sum 27, product 729, divisor 756, and 999 leaves remainder 243.
  //          The digit sum divides 999 exactly, so a solution that ignores the product answers
  //          true — the same bug Step 5 catches, restated where the product is large enough to
  //          dominate the divisor
  @Test
  void n999IsNotDivisibleOnceTheLargeProductJoinsTheSum() {
    assertThat(sut.checkDivisibility(999)).isFalse();
  }

  // Step 12: six repeated ones. Sum 6, product 1, divisor 7, and 111111 is 7 times 15873. The
  //          product of all-one digits is 1, not 0 — a running product initialized to 0 yields
  //          divisor 6, and 111111 leaves remainder 3 against 6, so that bug answers false here
  //          while Step 5 catches it answering true. The two steps pin the initializer from both
  //          sides
  @Test
  void n111111IsDivisibleBecauseTheProductOfOnesIsOne() {
    assertThat(sut.checkDivisibility(111111)).isTrue();
  }

  // =============================================================================================
  // The official examples (Steps 13-14).
  // =============================================================================================

  // Step 13: LeetCode Example 1 — sum 9 + 9 = 18, product 9 times 9 = 81, and 18 + 81 = 99 equals
  //          n itself, so the answer is true. Note what this example cannot rule out: because the
  //          divisor equals n, it agrees with the reversed test that asks whether the divisor is
  //          divisible by n. Step 3 is what kills that reading
  @Test
  void leetCodeExample1() {
    assertThat(sut.checkDivisibility(99)).isTrue();
  }

  // Step 14: LeetCode Example 2 — sum 2 + 3 = 5, product 2 times 3 = 6, divisor 11, and 23 leaves
  //          remainder 1. The explanation adds the two values rather than concatenating or
  //          comparing them, which is what makes 11 the divisor and not 56 or 5
  @Test
  void leetCodeExample2() {
    assertThat(sut.checkDivisibility(23)).isFalse();
  }

  // =============================================================================================
  // Constraint bounds, 1 <= n <= 10^6 (Steps 15-18). The divisor is bounded by 54 + 9^6 = 531495,
  // which fits an int with room to spare, so the timeouts here separate per-call cost rather than
  // arithmetic width. A solution that is O(number of digits) sweeps the whole range in well under
  // a second, while anything that loops up to n per call needs on the order of 10^12 steps.
  // =============================================================================================

  // Step 15: the constraint ceiling n = 10^6, and the only seven-digit input allowed. Sum 1,
  //          product 0, divisor 1, so the answer is true. A solution that assumes at most six
  //          digits, or that indexes a fixed-size digit buffer, breaks here
  @Test
  void n1000000IsDivisibleAtTheConstraintCeiling() {
    assertThat(sut.checkDivisibility(1_000_000)).isTrue();
  }

  // Step 16: the largest input with no zero digit, which maximizes the product at 9^6 = 531441.
  //          Sum 54 gives divisor 531495 and 999999 leaves remainder 468504. The intermediate
  //          product never approaches the int limit, so a solution using long is not wrong, only
  //          unnecessary
  @Test
  void n999999IsNotDivisibleAtTheLargestPossibleProduct() {
    assertThat(sut.checkDivisibility(999_999)).isFalse();
  }

  // Step 17: the largest input in range that is divisible and has no zero digit. Sum 36, product
  //          17496, divisor 17532, and 999324 is 17532 times 57. This is the one large case where
  //          the answer is true without the product collapsing to zero
  @Test
  void n999324IsDivisibleWithoutAnyZeroDigit() {
    assertThat(sut.checkDivisibility(999_324)).isTrue();
  }

  // Step 18: every value the constraints permit, in one sweep. Exactly 54669 of the 10^6 inputs
  //          are divisible. A single wrong branch anywhere in the digit walk moves this count, and
  //          a per-call cost worse than O(number of digits) cannot finish inside the timeout
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void theWholeConstraintRangeHasExactly54669DivisibleValues() {
    int divisible = 0;
    for (int n = 1; n <= 1_000_000; n++) {
      if (sut.checkDivisibility(n)) {
        divisible++;
      }
    }
    assertThat(divisible).isEqualTo(54_669);
  }

  // =============================================================================================
  // Hygiene (Steps 19-20).
  // =============================================================================================

  // Step 19: one instance answers inputs of several digit lengths, deliberately out of order with
  //          the largest in the middle. A running sum or product kept in an instance field instead
  //          of a local carries over between calls, and the second answer onward goes wrong
  @Test
  void oneInstanceAnswersManyInputsInAnyOrder() {
    assertThat(sut.checkDivisibility(23)).isFalse();
    assertThat(sut.checkDivisibility(1_000_000)).isTrue();
    assertThat(sut.checkDivisibility(999_999)).isFalse();
    assertThat(sut.checkDivisibility(42)).isTrue();
    assertThat(sut.checkDivisibility(1)).isFalse();
    assertThat(sut.checkDivisibility(111_111)).isTrue();
  }

  // Step 20: the same input asked repeatedly gives the same answer. This is the pure-function
  //          claim Step 19 makes across differing inputs, narrowed to the case where cached state
  //          would look correct on the first call and drift only afterward
  @Test
  void repeatedCallsWithTheSameInputAgree() {
    for (int i = 0; i < 5; i++) {
      assertThat(sut.checkDivisibility(42)).isTrue();
      assertThat(sut.checkDivisibility(105)).isFalse();
    }
  }
}
