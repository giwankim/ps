package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AddBinaryTest {
  AddBinary sut = new AddBinary();

  // Step 1: smallest valid input under 1 <= a.length, b.length — "0" + "0" must yield "0",
  // not the empty string and not "00". Guards against a loop that emits nothing when no
  // column ever sets a bit, and against an unconditional final-carry append that always
  // prepends a stray leading "0".
  @Test
  void zeroPlusZeroIsZero() {
    assertThat(sut.addBinary("0", "0")).isEqualTo("0");
  }

  // Step 2: identity on the left arm — "0" + "1" = "1". Forces the loop to keep walking
  // the right arm after the left arm contributes nothing past the shared suffix.
  @Test
  void zeroPlusOneIsOne() {
    assertThat(sut.addBinary("0", "1")).isEqualTo("1");
  }

  // Step 3: identity on the right arm — "1" + "0" = "1". Together with Step 2, pins
  // symmetric handling of the two arms; rules out a swap of i/j in the carry computation.
  @Test
  void onePlusZeroIsOne() {
    assertThat(sut.addBinary("1", "0")).isEqualTo("1");
  }

  // Step 4: first carry-out — "1" + "1" = "10". The simplest case where the result is
  // longer than either input. Catches a loop that bounds on `i >= 0 || j >= 0` alone and
  // forgets to emit the final carry once both indices are exhausted.
  @Test
  void onePlusOneIsTen() {
    assertThat(sut.addBinary("1", "1")).isEqualTo("10");
  }

  // Step 5: LeetCode Example 1 — "11" + "1" = "100". The carry from the LSB column ripples
  // into the second column, which itself overflows, so the result grows by one bit beyond
  // the longer input. Exercises a multi-step ripple that begins inside the inputs and
  // continues past their end.
  @Test
  void leetCodeExample1() {
    assertThat(sut.addBinary("11", "1")).isEqualTo("100");
  }

  // Step 6: LeetCode Example 2 — "1010" + "1011" = "10101". Equal-length inputs whose sum
  // overflows the input width and whose columns alternate between carrying and not. No
  // single-shape shortcut (always-carry, never-carry, leading-1-only) survives this case.
  @Test
  void leetCodeExample2() {
    assertThat(sut.addBinary("1010", "1011")).isEqualTo("10101");
  }

  // Step 7: control case for the no-carry path with mismatched lengths — "1000" + "11" =
  // "1011". Note that an equal-length no-carry case is impossible under the "no leading
  // zeros except '0'" constraint, since both inputs would start with 1 and the leading
  // column would carry. Catches an early termination that stops as soon as the shorter
  // input runs out, dropping the longer arm's high bits.
  @Test
  void differentLengthsNoCarry() {
    assertThat(sut.addBinary("1000", "11")).isEqualTo("1011");
  }

  // Step 8: full carry cascade through the longer arm — "1111" + "1" = "10000". The
  // carry-in from the LSB column propagates through every column of the longer input and
  // finally produces a brand-new leading bit. Targets implementations that handle the
  // residual carry at the end but stop walking the longer arm at the first non-carry
  // column.
  @Test
  void carryCascadesThroughLongerInput() {
    assertThat(sut.addBinary("1111", "1")).isEqualTo("10000");
  }

  // Step 9: every column produces a carry — "1111" + "1111" = "11110". Each column sums to
  // 2 (no carry-in) or 3 (with carry-in), and the final carry escapes the input width.
  // Verifies the loop combines column sums of 2 and 3 interchangeably; an off-by-one in
  // the digit/carry split (e.g. `digit = sum & 1` vs. `digit = sum / 2`) shows up here.
  @Test
  void everyColumnCarries() {
    assertThat(sut.addBinary("1111", "1111")).isEqualTo("11110");
  }

  // Step 10: carry born inside the overlap propagates past the shorter arm's end —
  // "100" + "11111" = "100011" (4 + 31 = 35). The overlap columns produce a carry that
  // must survive into the tail of the longer arm and ripple through it. Distinguishes a
  // correct carry-aware loop (`while i >= 0 || j >= 0 || carry`) from the buggy split
  // pattern that adds the overlap, then naively copies the longer arm's tail and silently
  // drops the live carry.
  @Test
  void carryRipplesPastShorterArm() {
    assertThat(sut.addBinary("100", "11111")).isEqualTo("100011");
  }

  // Step 11: cascade at the constraint upper bound — 10 000 ones plus "1" must yield "1"
  // followed by 10 000 zeros (length 10 001). Confirms the carry rope survives the full
  // 1 <= a.length, b.length <= 10^4 input width without truncation, and that no
  // intermediate StringBuilder reallocation drops bits mid-cascade.
  @Test
  void singleCarryCascadeAtConstraintBound() {
    String allOnes = "1".repeat(10_000);
    String expected = "1" + "0".repeat(10_000);
    assertThat(sut.addBinary(allOnes, "1")).isEqualTo(expected);
  }

  // Step 12: both arms at the constraint upper bound — 10 000 ones plus 10 000 ones must
  // yield 10 000 ones followed by a "0" (length 10 001), i.e. (2^10000 - 1) * 2. Exercises
  // linear-time behavior at full scale where every column carries; together with Step 11,
  // pins both the "single carry rope" and the "every column carries" shapes at the
  // problem's input ceiling.
  @Test
  void bothArmsAtConstraintBound() {
    String allOnes = "1".repeat(10_000);
    String expected = "1".repeat(10_000) + "0";
    assertThat(sut.addBinary(allOnes, allOnes)).isEqualTo(expected);
  }
}
