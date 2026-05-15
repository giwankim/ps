package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// LeetCode 190 constraints (as of 2026-05-13):
//   * n is even
//   * 0 <= n <= 2^31 - 2
// Equivalently: bit 0 of n is 0, and bit 31 of n is 0. By symmetry of the
// reversal, the output is also even and in [0, 2^31 - 2], so no test case in
// this file uses a negative or odd value as input or expected output.
class ReverseBitsTest {
  ReverseBits sut = new ReverseBits();

  // Step 1: identity at the empty bit set — zero reverses to zero. Guards against an
  // off-by-one that introduces a stray high bit when no bits should be set.
  @Test
  void zeroReversesToZero() {
    assertThat(sut.reverseBits(0)).isZero();
    assertThat(sut.reverseBits2(0)).as("reverseBits2").isZero();
    assertThat(sut.reverseBits3(0)).as("reverseBits3").isZero();
  }

  // Step 2: smallest valid nonzero input — bit 1 must move to bit 30 (one in from the
  // forbidden MSB). With bit 0 constrained to 0, this is the new "LSB-edge" probe.
  @Test
  void bitOneReversesToBitThirty() {
    assertThat(sut.reverseBits(0b10)).isEqualTo(0x40000000);
    assertThat(sut.reverseBits2(0b10)).as("reverseBits2").isEqualTo(0x40000000);
    assertThat(sut.reverseBits3(0b10)).as("reverseBits3").isEqualTo(0x40000000);
  }

  // Step 3: inverse of Step 2 — bit 30 must move to bit 1. The pair (Step 2, Step 3)
  // pins symmetric handling of the inner-edge positions.
  @Test
  void bitThirtyReversesToBitOne() {
    assertThat(sut.reverseBits(0x40000000)).isEqualTo(0b10);
    assertThat(sut.reverseBits2(0x40000000)).as("reverseBits2").isEqualTo(0b10);
    assertThat(sut.reverseBits3(0x40000000)).as("reverseBits3").isEqualTo(0b10);
  }

  // Step 4: two adjacent low bits — n = 0b110 (bits 1 and 2 set) must reverse to the
  // two adjacent high bits 0x60000000 (bits 29 and 30 set). Verifies that bit order is
  // preserved across the reversal, not just bit presence. Bit 0 is intentionally not
  // set because the constraints forbid odd inputs.
  @Test
  void twoAdjacentLowBitsReverseToTwoAdjacentHighBits() {
    assertThat(sut.reverseBits(0b110)).isEqualTo(0x60000000);
    assertThat(sut.reverseBits2(0b110)).as("reverseBits2").isEqualTo(0x60000000);
    assertThat(sut.reverseBits3(0b110)).as("reverseBits3").isEqualTo(0x60000000);
  }

  // Step 5: dense alternating pattern in the valid bit band — 0x2AAAAAAA (every odd
  // index in 1..29 set, bits 0 and 31 clear) reverses to 0x55555554 (every even index
  // in 2..30 set). Exercises almost every iteration of the reversal loop while staying
  // inside the [0, 2^31 - 2] window.
  @Test
  void alternatingOddBitsReverseToAlternatingEvenBits() {
    assertThat(sut.reverseBits(0x2AAAAAAA)).isEqualTo(0x55555554);
    assertThat(sut.reverseBits2(0x2AAAAAAA)).as("reverseBits2").isEqualTo(0x55555554);
    assertThat(sut.reverseBits3(0x2AAAAAAA)).as("reverseBits3").isEqualTo(0x55555554);
  }

  // Step 6: inverse of Step 5 — 0x55555554 reverses to 0x2AAAAAAA. Together with
  // Step 5 these confirm the algorithm is its own inverse on this pattern family.
  @Test
  void alternatingEvenBitsReverseToAlternatingOddBits() {
    assertThat(sut.reverseBits(0x55555554)).isEqualTo(0x2AAAAAAA);
    assertThat(sut.reverseBits2(0x55555554)).as("reverseBits2").isEqualTo(0x2AAAAAAA);
    assertThat(sut.reverseBits3(0x55555554)).as("reverseBits3").isEqualTo(0x2AAAAAAA);
  }

  // Step 7: word-middle boundary — bit 15 and bit 16 are the closest pair straddling
  // the midpoint of the 32-bit word; under reversal they swap with each other (bit 15
  // ↔ bit 16). Guards against an off-by-one in the loop bound that would scramble the
  // central bits.
  @Test
  void middleBitsSwapAcrossWordCenter() {
    int input = (1 << 15) | (1 << 16);
    assertThat(sut.reverseBits(input)).isEqualTo(input);
    assertThat(sut.reverseBits2(input)).as("reverseBits2").isEqualTo(input);
    assertThat(sut.reverseBits3(input)).as("reverseBits3").isEqualTo(input);
  }

  // Step 8: maximum valid input — 0x7FFFFFFE = 2^31 - 2 has every bit in 1..30 set and
  // bits 0 and 31 clear. Under reversal each bit k in 1..30 maps to bit (31 - k), also
  // in 1..30, so the value is its own image: a palindrome under reversal. This is the
  // upper-boundary case of the constraint range.
  @Test
  void maxValidInputReversesToItself() {
    assertThat(sut.reverseBits(0x7FFFFFFE)).isEqualTo(0x7FFFFFFE);
    assertThat(sut.reverseBits2(0x7FFFFFFE)).as("reverseBits2").isEqualTo(0x7FFFFFFE);
    assertThat(sut.reverseBits3(0x7FFFFFFE)).as("reverseBits3").isEqualTo(0x7FFFFFFE);
  }

  // Step 9: LeetCode Example 1 — 43261596 (00000010100101000001111010011100) reverses
  // to 964176192 (00111001011110000010100101000000). Canonical mixed-pattern check
  // from the problem statement.
  @Test
  void leetCodeExample1() {
    int input = 0b00000010100101000001111010011100;
    int expected = 0b00111001011110000010100101000000;
    assertThat(sut.reverseBits(input)).isEqualTo(expected);
    assertThat(sut.reverseBits2(input)).as("reverseBits2").isEqualTo(expected);
    assertThat(sut.reverseBits3(input)).as("reverseBits3").isEqualTo(expected);
  }

  // Step 10: LeetCode Example 2 — 2147483644 (01111111111111111111111111111100)
  // reverses to 1073741822 (00111111111111111111111111111110). Exercises the
  // high-density input near the upper bound of the constraint range.
  @Test
  void leetCodeExample2() {
    int input = 0b01111111111111111111111111111100;
    int expected = 0b00111111111111111111111111111110;
    assertThat(sut.reverseBits(input)).isEqualTo(expected);
    assertThat(sut.reverseBits2(input)).as("reverseBits2").isEqualTo(expected);
    assertThat(sut.reverseBits3(input)).as("reverseBits3").isEqualTo(expected);
  }

  // Step 11: algebraic property — reversing twice is the identity on any input. A
  // handful of structurally different *constraint-valid* values (zero, the LSB-edge
  // bit, the MSB-edge bit, both alternating patterns, mid-word, the max input, and
  // both LeetCode examples) cover the involution property without resorting to
  // property-based tooling.
  @Test
  void doubleReverseIsIdentity() {
    int[] samples = {
      0,
      0b10,
      0x40000000,
      0x2AAAAAAA,
      0x55555554,
      (1 << 15) | (1 << 16),
      0x7FFFFFFE,
      0b00000010100101000001111010011100,
      0b01111111111111111111111111111100,
    };
    for (int n : samples) {
      assertThat(sut.reverseBits(sut.reverseBits(n)))
          .as("reverseBits double-reverse of 0x%08X", n)
          .isEqualTo(n);
      assertThat(sut.reverseBits2(sut.reverseBits2(n)))
          .as("reverseBits2 double-reverse of 0x%08X", n)
          .isEqualTo(n);
      assertThat(sut.reverseBits3(sut.reverseBits3(n)))
          .as("reverseBits3 double-reverse of 0x%08X", n)
          .isEqualTo(n);
    }
  }
}
