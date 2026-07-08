package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConcatenateNonZeroDigitsAndMultiplyBySumIITest {
  ConcatenateNonZeroDigitsAndMultiplyBySumII sut = new ConcatenateNonZeroDigitsAndMultiplyBySumII();

  // Step 1: LeetCode Example 1 — each query concatenates the non-zero digits of its own window:
  // s[0..7] = "10203004" gives x = 1234 and sum = 10, so 12340; s[1..3] = "020" gives 2 * 2 = 4;
  // s[4..6] = "300" gives 3 * 3 = 9
  @Test
  void eachQueryAnswersItsOwnWindow() {
    assertThat(sut.sumAndMultiply("10203004", new int[][] {{0, 7}, {1, 3}, {4, 6}}))
        .containsExactly(12_340, 4, 9);
  }

  // Step 2: LeetCode Example 2 — s[0..3] = "1000" collapses to x = 1, while the all-zero window
  // s[1..1] = "0" has no non-zero digits, so the spec defines x = 0 and the answer is 0 * 0 = 0,
  // not an exception from an empty concatenation
  @Test
  void allZeroWindowDefinesXAsZero() {
    assertThat(sut.sumAndMultiply("1000", new int[][] {{0, 3}, {1, 1}})).containsExactly(1, 0);
  }

  // Step 3: LeetCode Example 3 — x = 987654321 with sum = 45 gives 44444444445, which overflows
  // int, so the answer is reported modulo 10^9 + 7: 44444444445 mod 1000000007 = 444444137
  @Test
  void productPastIntRangeIsReducedModulo1e9Plus7() {
    assertThat(sut.sumAndMultiply("9876543210", new int[][] {{0, 9}})).containsExactly(444_444_137);
  }

  // Step 4: the constraint floor m = 1 — a single-digit window is its own concatenation and its
  // own digit sum, so the answer is its square: 7 * 7 = 49
  @Test
  void singleDigitWindowYieldsItsSquare() {
    assertThat(sut.sumAndMultiply("7", new int[][] {{0, 0}})).containsExactly(49);
  }

  // Step 5: the other single-character floor — s = "0" has no non-zero digits anywhere, so the
  // only possible query returns 0
  @Test
  void singleZeroStringReturnsZero() {
    assertThat(sut.sumAndMultiply("0", new int[][] {{0, 0}})).containsExactly(0);
  }

  // Step 6: "102" must become x = 12, not 21 — non-zero digits keep their original order, so a
  // solution that assembles x back to front returns 21 * 3 = 63 instead of 12 * 3 = 36
  @Test
  void nonZeroDigitsKeepTheirOriginalOrder() {
    assertThat(sut.sumAndMultiply("102", new int[][] {{0, 2}})).containsExactly(36);
  }

  // Step 7: overlapping windows over "39" answer independently and in query order — [0,0] gives
  // 3 * 3 = 9, [1,1] gives 9 * 9 = 81, and [0,1] gives 39 * (3 + 9) = 468
  @Test
  void overlappingWindowsAnswerIndependentlyInQueryOrder() {
    assertThat(sut.sumAndMultiply("39", new int[][] {{0, 0}, {1, 1}, {0, 1}}))
        .containsExactly(9, 81, 468);
  }

  // Step 8: a window starting past a zero-bearing prefix — s[1..3] = "203" in "1203" gives x = 23
  // and sum = 5, so 115; a prefix decomposition must shift the left prefix by the window's count
  // of non-zero digits (two), not by its raw width (three)
  @Test
  void windowPastAZeroBearingPrefixShiftsByNonZeroCount() {
    assertThat(sut.sumAndMultiply("1203", new int[][] {{1, 3}})).containsExactly(115);
  }

  // Step 9: twenty nines concatenate to 10^20 - 1, far past long range, so x must be reduced
  // modulo 10^9 + 7 while it is built — 10^9 ≡ -7 gives x ≡ 49 * 100 - 1 = 4899, and with
  // sum = 180 the answer is 4899 * 180 = 881820
  @Test
  void concatenationPastLongRangeIsReducedWhileBuilt() {
    assertThat(sut.sumAndMultiply("9".repeat(20), new int[][] {{0, 19}})).containsExactly(881_820);
  }

  // Step 10: an interior window of eleven nines — s[2..10] is nine nines, so x = 999999999 and
  // sum = 81, giving 80999999919 mod 1000000007 = 999999359; a prefix-difference solution wraps
  // negative here and must renormalize the residue into [0, MOD)
  @Test
  void prefixDifferencePastModulusRenormalizesNonNegative() {
    assertThat(sut.sumAndMultiply("9".repeat(11), new int[][] {{2, 10}}))
        .containsExactly(999_999_359);
  }
}
