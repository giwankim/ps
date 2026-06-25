package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOf1BitsTest {
  NumberOf1Bits sut = new NumberOf1Bits();

  // Step 1: smallest valid input under 1 <= n <= 2^31 - 1 — the lone LSB must produce a
  // count of 1. Forces the loop to run at least once and to count the bit it lands on,
  // rather than skipping the very first iteration.
  @Test
  void singleLowBitIsOne() {
    assertThat(sut.hammingWeight(1)).isEqualTo(1);
    assertThat(sut.hammingWeight2(1)).isEqualTo(1);
    assertThat(sut.hammingWeight3(1)).isEqualTo(1);
  }

  // Step 2: a single bit one position above the LSB — n = 2 (= 0b10) must yield 1. Catches
  // implementations that only inspect n & 1 once and exit; the loop must keep advancing
  // until it reaches the bit that is actually set.
  @Test
  void singleBitAtPositionOne() {
    assertThat(sut.hammingWeight(2)).isEqualTo(1);
    assertThat(sut.hammingWeight2(2)).isEqualTo(1);
    assertThat(sut.hammingWeight3(2)).isEqualTo(1);
  }

  // Step 3: two adjacent low bits — n = 3 (= 0b11) must yield 2. The accumulator must add
  // across iterations, not overwrite, and the loop must not terminate after the first set
  // bit it sees.
  @Test
  void twoAdjacentLowBitsIsTwo() {
    assertThat(sut.hammingWeight(3)).isEqualTo(2);
    assertThat(sut.hammingWeight2(3)).isEqualTo(2);
    assertThat(sut.hammingWeight3(3)).isEqualTo(2);
  }

  // Step 4: LeetCode Example 1 — n = 11 (= 0b1011) must yield 3. The set bits are
  // separated by an unset bit, so any implementation that exits as soon as it encounters a
  // zero bit (e.g. `while ((n & 1) == 1) ...`) is exposed here.
  @Test
  void leetCodeExample1() {
    assertThat(sut.hammingWeight(11)).isEqualTo(3);
    assertThat(sut.hammingWeight2(11)).isEqualTo(3);
    assertThat(sut.hammingWeight3(11)).isEqualTo(3);
  }

  // Step 5: LeetCode Example 2 — n = 128 (= 0b10000000) must yield 1. A single set bit
  // sitting on top of seven unset columns. Forces the loop to traverse seven non-set
  // iterations before reaching the bit that contributes; complements Step 2 by stretching
  // the same shape further from the LSB.
  @Test
  void leetCodeExample2() {
    assertThat(sut.hammingWeight(128)).isEqualTo(1);
    assertThat(sut.hammingWeight2(128)).isEqualTo(1);
    assertThat(sut.hammingWeight3(128)).isEqualTo(1);
  }

  // Step 6: a single bit just below the sign-bit position — n = 0x40000000 (= 1 << 30)
  // must yield 1. This is the highest single-bit input the constraint admits, since bit 31
  // is the sign bit and n <= 2^31 - 1. Catches an implementation that stops one iteration
  // early, or one that confuses bit 30 with the sign bit.
  @Test
  void singleBitAtPositionThirty() {
    assertThat(sut.hammingWeight(0x40000000)).isEqualTo(1);
    assertThat(sut.hammingWeight2(0x40000000)).isEqualTo(1);
    assertThat(sut.hammingWeight3(0x40000000)).isEqualTo(1);
  }

  // Step 7: bits at both extremes of the valid range — n = 0x40000001 (bit 0 and bit 30)
  // must yield 2. Combines a one-step iteration (LSB) with a 30-step traversal (bit 30) in
  // a single input. Catches a loop that stops after seeing the first set bit, or one that
  // never reaches the high end.
  @Test
  void bitsAtBothExtremesIsTwo() {
    assertThat(sut.hammingWeight(0x40000001)).isEqualTo(2);
    assertThat(sut.hammingWeight2(0x40000001)).isEqualTo(2);
    assertThat(sut.hammingWeight3(0x40000001)).isEqualTo(2);
  }

  // Step 8: alternating bits starting at position 0 — n = 0x55555555 has every
  // even-indexed bit set (0, 2, 4, ..., 30), so the count is 16. Exercises 16 of the 31
  // valid iterations and verifies the loop counts only the columns where the bit is set,
  // not the columns it merely visits.
  @Test
  void alternatingEvenBitsIsSixteen() {
    assertThat(sut.hammingWeight(0x55555555)).isEqualTo(16);
    assertThat(sut.hammingWeight2(0x55555555)).isEqualTo(16);
    assertThat(sut.hammingWeight3(0x55555555)).isEqualTo(16);
  }

  // Step 9: alternating bits starting at position 1 — n = 0x2AAAAAAA has every
  // odd-indexed bit set (1, 3, 5, ..., 29), so the count is 15. Pairs with Step 8 to pin
  // both bit alignments; together they catch an off-by-one in the shift index that would
  // either double-count or miss a single bit at the edge.
  @Test
  void alternatingOddBitsIsFifteen() {
    assertThat(sut.hammingWeight(0x2AAAAAAA)).isEqualTo(15);
    assertThat(sut.hammingWeight2(0x2AAAAAAA)).isEqualTo(15);
    assertThat(sut.hammingWeight3(0x2AAAAAAA)).isEqualTo(15);
  }

  // Step 10: a contiguous run of 16 set bits in the low half — n = 0x0000FFFF must yield
  // 16. A "long run without spanning the whole word" shape that distinguishes a correct
  // counter from one that special-cases sparse vs. dense inputs (e.g. a Brian Kernighan
  // implementation converges in 16 steps here, while a bit-shift implementation runs the
  // full 16 iterations from bit 0 through bit 15 with no zeros in between).
  @Test
  void contiguousLowSixteenBitsIsSixteen() {
    assertThat(sut.hammingWeight(0x0000FFFF)).isEqualTo(16);
    assertThat(sut.hammingWeight2(0x0000FFFF)).isEqualTo(16);
    assertThat(sut.hammingWeight3(0x0000FFFF)).isEqualTo(16);
  }

  // Step 11: n = 2147483645 (= 0x7FFFFFFD = Integer.MAX_VALUE - 2) — every valid bit set
  // except bit 1, yielding 30. One notch below the constraint upper bound, with a single
  // gap deep in the low end. Catches an implementation that special-cases MAX_VALUE or
  // assumes "high inputs are all-ones".
  @Test
  void maxValueMinusTwoIsThirty() {
    assertThat(sut.hammingWeight(2147483645)).isEqualTo(30);
    assertThat(sut.hammingWeight2(2147483645)).isEqualTo(30);
    assertThat(sut.hammingWeight3(2147483645)).isEqualTo(30);
  }

  // Step 12: constraint upper bound — n = Integer.MAX_VALUE (= 0x7FFFFFFF) is all 31 valid
  // bits set, yielding 31. Confirms the loop traverses every bit from position 0 through
  // position 30 without dropping any, and never accidentally counts a 32nd bit (the sign
  // bit, which is unreachable under the constraint).
  @Test
  void integerMaxValueIsThirtyOne() {
    assertThat(sut.hammingWeight(Integer.MAX_VALUE)).isEqualTo(31);
    assertThat(sut.hammingWeight2(Integer.MAX_VALUE)).isEqualTo(31);
    assertThat(sut.hammingWeight3(Integer.MAX_VALUE)).isEqualTo(31);
  }
}
