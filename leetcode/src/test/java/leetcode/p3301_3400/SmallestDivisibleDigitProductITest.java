package leetcode.p3301_3400;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SmallestDivisibleDigitProductITest {
  SmallestDivisibleDigitProductI sut = new SmallestDivisibleDigitProductI();

  // Step 1: the smallest t — every product is divisible by 1, so the answer is n itself and no
  // candidate past n is ever examined
  @Test
  void tOfOneReturnsNImmediately() {
    assertThat(sut.smallestNumber(1, 1)).isEqualTo(1);
  }

  // Step 2: "greater than or equal to n" — n is a candidate, so a search that starts at n + 1
  // would skip the answer here (5 -> 5, divisible by 5)
  @Test
  void nItselfIsTheAnswerWhenItAlreadyQualifies() {
    assertThat(sut.smallestNumber(5, 5)).isEqualTo(5);
  }

  // Step 3: the first real search — 1 fails t = 2, so the scan must step forward to 2
  @Test
  void stepsPastNWhenNFails() {
    assertThat(sut.smallestNumber(1, 2)).isEqualTo(2);
  }

  // Step 4: "smallest", not merely "some" — from 2 the scan must reject 3 and stop at 4, rather
  // than jumping straight to an obvious multiple like t itself or 8
  @Test
  void returnsTheFirstQualifyingCandidateNotALaterOne() {
    assertThat(sut.smallestNumber(2, 4)).isEqualTo(4);
  }

  // Step 5: for a one-digit number the product is the digit itself, so 1..8 all fail t = 9 and the
  // scan runs the full width of the single digits
  @Test
  void scansSingleDigitsUntilTheDigitItselfDivides() {
    assertThat(sut.smallestNumber(1, 9)).isEqualTo(9);
  }

  // Step 6: LeetCode Example 1 — the digit product of 10 is 1 * 0 = 0, and 0 is divisible by every
  // t, so 10 wins outright; an accumulator that skips zero digits would compute 1 and reject it
  @Test
  void zeroDigitZeroesTheProductWhichEveryTDivides() {
    assertThat(sut.smallestNumber(10, 2)).isEqualTo(10);
  }

  // Step 7: LeetCode Example 2 — the test is on the digit product, not the number: 15 is divisible
  // by 3 but its product 5 is not, so the answer is 16 (product 6)
  @Test
  void digitsAreMultipliedRatherThanTheNumberItselfTested() {
    assertThat(sut.smallestNumber(15, 3)).isEqualTo(16);
  }

  // Step 8: the answer may need one more digit than n — 9 fails t = 8, and the next candidate 10
  // qualifies only through its zero digit
  @Test
  void searchCrossesFromOneDigitIntoTwo() {
    assertThat(sut.smallestNumber(9, 8)).isEqualTo(10);
  }

  // Step 9: every digit contributes — 25 gives 10 (not divisible by 6) and 26 gives 12, which is;
  // a solution that inspected only the last digit would stop at 24 or 30
  @Test
  void bothDigitsContributeToTheProduct() {
    assertThat(sut.smallestNumber(25, 6)).isEqualTo(26);
  }

  // Step 10: a composite t need not be met by a single digit — no digit is divisible by 10, yet
  // 58 works because 5 * 8 = 40 supplies the factors 2 and 5 between them
  @Test
  void compositeTIsSatisfiedByFactorsSpreadAcrossDigits() {
    assertThat(sut.smallestNumber(57, 10)).isEqualTo(58);
  }

  // Step 11: t = 7 is prime and larger than any digit's other factors, so only a literal 7 digit
  // (or a zero) can satisfy it — 41..46 all fail and 47 gives 28
  @Test
  void primeTOfSevenNeedsASevenDigit() {
    assertThat(sut.smallestNumber(41, 7)).isEqualTo(47);
  }

  // Step 12: the longest scan in the whole input space — 91..99 give 9, 18, 27, ... 81, none
  // divisible by 10, so the answer is the tenth candidate, 100; this is also the only way the
  // answer grows from two digits to three
  @Test
  void worstCaseScansTenCandidatesAndGainsADigit() {
    assertThat(sut.smallestNumber(91, 10)).isEqualTo(100);
  }

  // Step 13: n at the upper constraint bound (n <= 100) — 100 has a zero digit, so it answers
  // itself for every t and the scan never runs past the stated range
  @Test
  void nAtTheUpperBoundIsItsOwnAnswer() {
    assertThat(sut.smallestNumber(100, 7)).isEqualTo(100);
  }
}
