package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BitwiseAndOfNumbersRangeTest {
  BitwiseAndOfNumbersRange sut = new BitwiseAndOfNumbersRange();

  // Step 1: degenerate range at the lower bound of the constraint — left = right = 0 must
  // yield 0. Smallest legal input. Pins the trivial case where the range has exactly one
  // element and that element is zero, before any bit-shifting logic gets involved.
  @Test
  void singletonZeroIsZero() {
    assertThat(sut.rangeBitwiseAnd(0, 0)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(0, 0)).isEqualTo(0);
  }

  // Step 2: singleton range with a set bit — left = right = 1 must yield 1. Establishes the
  // identity left == right => result == left for a value with one bit set. Catches an
  // implementation that always returns 0, or one that clears bits even when nothing in the
  // range differs.
  @Test
  void singletonOneIsOne() {
    assertThat(sut.rangeBitwiseAnd(1, 1)).isEqualTo(1);
    assertThat(sut.rangeBitwiseAnd2(1, 1)).isEqualTo(1);
  }

  // Step 3: singleton range at the upper bound — left = right = Integer.MAX_VALUE must
  // yield Integer.MAX_VALUE. Pairs with Step 1 to pin both ends of the constraint when the
  // range has a single element. Also catches an implementation that uses `right + 1`
  // anywhere, since 2^31 - 1 + 1 overflows to Integer.MIN_VALUE.
  @Test
  void singletonIntegerMaxValueIsItself() {
    assertThat(sut.rangeBitwiseAnd(Integer.MAX_VALUE, Integer.MAX_VALUE))
        .isEqualTo(Integer.MAX_VALUE);
    assertThat(sut.rangeBitwiseAnd2(Integer.MAX_VALUE, Integer.MAX_VALUE))
        .isEqualTo(Integer.MAX_VALUE);
  }

  // Step 4: smallest non-trivial range — [0, 1] must yield 0. The two values differ in the
  // LSB and 0 AND anything is 0. Forces the implementation to handle a range with more than
  // one element, and exposes any "return left" or "return right" shortcut (which would
  // return 0 by coincidence here only because left is 0 — see Step 5 for the version that
  // really kills the shortcut).
  @Test
  void zeroToOneIsZero() {
    assertThat(sut.rangeBitwiseAnd(0, 1)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(0, 1)).isEqualTo(0);
  }

  // Step 5: even-odd consecutive pair — [2, 3] = (0b10, 0b11) must yield 2. Only the LSB
  // toggles; the high bits are shared, so the result preserves them. Catches an
  // implementation that always returns 0 for any multi-element range, and verifies that the
  // common high-bit prefix is what survives.
  @Test
  void consecutiveEvenOddPreservesHighBits() {
    assertThat(sut.rangeBitwiseAnd(2, 3)).isEqualTo(2);
    assertThat(sut.rangeBitwiseAnd2(2, 3)).isEqualTo(2);
  }

  // Step 6: LeetCode Example 1 — [5, 7] = (0b101, 0b110, 0b111) must yield 4 (0b100). This
  // is the canonical test that kills `return left & right`: 5 & 7 = 5, but the correct
  // answer is 4 because the middle value 6 zeroes out bit 0. The common prefix of 5 and 7
  // is 0b1__, which becomes 0b100 = 4 once the differing low bits are masked out.
  @Test
  void leetCodeExample1() {
    assertThat(sut.rangeBitwiseAnd(5, 7)).isEqualTo(4);
    assertThat(sut.rangeBitwiseAnd2(5, 7)).isEqualTo(4);
  }

  // Step 7: smallest range that crosses a power-of-two boundary — [1, 2] = (0b01, 0b10)
  // must yield 0. The high bit of 2 is not set in 1, and the low bit of 1 is not set in 2,
  // so no bit is shared. Catches an implementation that assumes left and right share at
  // least one bit position.
  @Test
  void rangeCrossingFirstPowerOfTwoIsZero() {
    assertThat(sut.rangeBitwiseAnd(1, 2)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(1, 2)).isEqualTo(0);
  }

  // Step 8: range crossing a higher power-of-two boundary — [7, 8] = (0b0111, 0b1000) must
  // yield 0. Same shape as Step 7 but shifted up: the bits of 7 and 8 are completely
  // disjoint. Reinforces that "crosses a 2^k boundary" alone is enough to zero everything
  // below that boundary, regardless of how high up the boundary sits.
  @Test
  void rangeCrossingPowerOfTwoBoundaryIsZero() {
    assertThat(sut.rangeBitwiseAnd(7, 8)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(7, 8)).isEqualTo(0);
  }

  // Step 9: a full 2^k block aligned on its boundary — [12, 15] = (0b1100, 0b1101, 0b1110,
  // 0b1111) must yield 12 (0b1100). The high two bits are shared by every element; the low
  // two bits collectively cover all four combinations and so AND to 0. Verifies that the
  // common-prefix logic correctly identifies a 2-bit prefix when the range fills an entire
  // 4-element power-of-two block.
  @Test
  void fullAlignedBlockYieldsCommonPrefix() {
    assertThat(sut.rangeBitwiseAnd(12, 15)).isEqualTo(12);
    assertThat(sut.rangeBitwiseAnd2(12, 15)).isEqualTo(12);
  }

  // Step 10: a range that fully spans a 2^k boundary in the middle — [2, 4] = (0b010,
  // 0b011, 0b100) must yield 0. Distinct from Steps 7 and 8 because the range has three
  // elements rather than two, and the boundary crossing happens between the second and
  // third element. Catches an implementation that only checks the endpoints and misses
  // bit-cancellation caused by intermediate values.
  @Test
  void rangeSpanningBoundaryInteriorIsZero() {
    assertThat(sut.rangeBitwiseAnd(2, 4)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(2, 4)).isEqualTo(0);
  }

  // Step 11: a non-trivial common prefix where neither endpoint is itself the answer —
  // [26, 30] = (0b11010, 0b11011, 0b11100, 0b11101, 0b11110) must yield 24 (0b11000).
  // Neither 26 nor 30 equals 24, so this exposes any implementation that lazily returns one
  // of the endpoints. The shared prefix is the top two bits (0b11); everything below
  // toggles at least once across the five-element range.
  @Test
  void commonPrefixDistinctFromEndpoints() {
    assertThat(sut.rangeBitwiseAnd(26, 30)).isEqualTo(24);
    assertThat(sut.rangeBitwiseAnd2(26, 30)).isEqualTo(24);
  }

  // Step 12: left = 0 with a non-zero right — [0, 5] must yield 0. Once 0 is in the range,
  // the result is 0 regardless of right. Distinct from Step 4 because the range has six
  // elements, ensuring an implementation that loops or recursively shifts still terminates
  // correctly when one endpoint is 0.
  @Test
  void leftZeroSwallowsRange() {
    assertThat(sut.rangeBitwiseAnd(0, 5)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(0, 5)).isEqualTo(0);
  }

  // Step 13: adjacent values near the constraint ceiling — [Integer.MAX_VALUE - 1,
  // Integer.MAX_VALUE] must yield 0x7FFFFFFE. The two values differ only in the LSB and
  // share 30 high bits, so the result is right with bit 0 cleared. Catches sign-bit
  // confusion (bit 30 is the highest reachable bit under the constraint, not bit 31) and
  // any unsigned-vs-signed shift mistakes near the top of the int range.
  @Test
  void adjacentValuesNearIntegerMaxValue() {
    assertThat(sut.rangeBitwiseAnd(Integer.MAX_VALUE - 1, Integer.MAX_VALUE))
        .isEqualTo(Integer.MAX_VALUE - 1);
    assertThat(sut.rangeBitwiseAnd2(Integer.MAX_VALUE - 1, Integer.MAX_VALUE))
        .isEqualTo(Integer.MAX_VALUE - 1);
  }

  // Step 14: the widest legal range — [1, Integer.MAX_VALUE] must yield 0. The range spans
  // every power-of-two boundary up to 2^30, so every bit toggles at least once and nothing
  // survives the AND. This is also a guard against `right + 1` style implementations: at
  // right = 2^31 - 1, incrementing overflows to Integer.MIN_VALUE and silently corrupts any
  // loop bound that depends on it. The correct answer of 0 is easy to compute by hand (1 &
  // 2 = 0 already kills every remaining bit), so the test is unambiguous.
  @Test
  void widestLegalRangeIsZero() {
    assertThat(sut.rangeBitwiseAnd(1, Integer.MAX_VALUE)).isEqualTo(0);
    assertThat(sut.rangeBitwiseAnd2(1, Integer.MAX_VALUE)).isEqualTo(0);
  }
}
