package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConcatenateNonZeroDigitsAndMultiplyBySumITest {
  ConcatenateNonZeroDigitsAndMultiplyBySumI sut = new ConcatenateNonZeroDigitsAndMultiplyBySumI();

  // Step 1: LeetCode Example 1 — the zeros in 10203004 vanish, leaving x = 1234 with digit sum
  // 1 + 2 + 3 + 4 = 10, so the answer is 1234 * 10 = 12340
  @Test
  void interiorZerosVanishFromTheConcatenation() {
    assertThat(sut.sumAndMultiply(10_203_004)).isEqualTo(12_340);
  }

  // Step 2: LeetCode Example 2 — trailing zeros vanish too; 1000 collapses to x = 1 with
  // sum = 1, so the answer is 1
  @Test
  void trailingZerosCollapseToTheLeadingDigit() {
    assertThat(sut.sumAndMultiply(1000)).isEqualTo(1);
  }

  // Step 3: a single digit is its own concatenation and its own digit sum, so the answer is its
  // square: 7 * 7 = 49
  @Test
  void singleDigitYieldsItsSquare() {
    assertThat(sut.sumAndMultiply(7)).isEqualTo(49);
  }

  // Step 4: with no zeros to drop, x is n itself and the answer is n times its digit sum:
  // 123 * (1 + 2 + 3) = 738
  @Test
  void zeroFreeNumberIsMultipliedByItsDigitSum() {
    assertThat(sut.sumAndMultiply(123)).isEqualTo(738);
  }

  // Step 5: 102 must become x = 12, not 21 — non-zero digits keep their original order, so a
  // solution that assembles x back to front returns 21 * 3 = 63 instead of 12 * 3 = 36
  @Test
  void nonZeroDigitsKeepTheirOriginalOrder() {
    assertThat(sut.sumAndMultiply(102)).isEqualTo(36);
  }

  // Step 6: the constraint floor n = 0 has no non-zero digits at all — the spec defines x = 0,
  // so sum = 0 and the product is 0, not an exception from an empty digit loop
  @Test
  void zeroHasNoNonZeroDigitsAndReturnsZero() {
    assertThat(sut.sumAndMultiply(0)).isEqualTo(0);
  }

  // Step 7: repeated digits each count separately — 505 gives x = 55 and sum = 5 + 5 = 10,
  // so 55 * 10 = 550
  @Test
  void repeatedDigitsEachCountTowardTheSum() {
    assertThat(sut.sumAndMultiply(505)).isEqualTo(550);
  }

  // Step 8: 999,999,999 keeps all nine digits, so x still fits in an int, but
  // x * sum = 999,999,999 * 81 = 80,999,999,919 does not — the product must be taken in long
  @Test
  void productPastIntRangeNeedsLongArithmetic() {
    assertThat(sut.sumAndMultiply(999_999_999)).isEqualTo(80_999_999_919L);
  }

  // Step 9: the constraint ceiling n = 10^9 is a one followed by nine zeros — ten digits in,
  // a single 1 out, so the answer is 1 * 1 = 1
  @Test
  void constraintCeilingCollapsesToOne() {
    assertThat(sut.sumAndMultiply(1_000_000_000)).isEqualTo(1);
  }
}
