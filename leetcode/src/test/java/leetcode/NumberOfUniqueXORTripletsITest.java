package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOfUniqueXORTripletsITest {
  NumberOfUniqueXORTripletsI sut = new NumberOfUniqueXORTripletsI();

  // Step 1: LeetCode Example 1 — nums = [1,2]; the four index triples (i <= j <= k) all collapse
  // to values {1, 2}: with only two distinct numbers some pair must repeat and cancel, so 0 and 3
  // are unreachable
  @Test
  void pairPermutationCannotReachZero() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 2})).isEqualTo(2);
  }

  // Step 2: LeetCode Example 2 — nums = [3,1,2]; three distinct elements unlock 3^1^2 = 0 and with
  // it every value in [0, 3], so the answer jumps to 4
  @Test
  void threeElementsUnlockZeroAndAllTwoBitValues() {
    assertThat(sut.uniqueXorTriplets(new int[] {3, 1, 2})).isEqualTo(4);
  }

  // Step 3: constraint floor n = 1 — the only legal triple is i = j = k, and 1 XOR 1 XOR 1 = 1, so
  // exactly one value exists; a formula-only solution that skips the n <= 2 special case returns 2
  @Test
  void singletonHasOnlyItsOwnTripleXor() {
    assertThat(sut.uniqueXorTriplets(new int[] {1})).isEqualTo(1);
  }

  // Step 4: the sorted permutation [1,2,3] must match Example 2's shuffled [3,1,2] — nums is
  // always a permutation of 1..n, so the value set depends only on n, never on element order
  @Test
  void answerDependsOnlyOnLengthNotOrder() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 2, 3})).isEqualTo(4);
  }

  // Step 5: the n = 2 special case with the pair reversed — [2,1] must not be distinguished from
  // [1,2] by peeking at nums[0] or assuming sorted input
  @Test
  void reversedPairStillCannotReachZero() {
    assertThat(sut.uniqueXorTriplets(new int[] {2, 1})).isEqualTo(2);
  }

  // Step 6: n = 4 is itself a power of two — the new high bit combines with the smaller values
  // (4^1=5, 4^2=6, 4^3=7) to cover all of [0, 7], so the count doubles to 8 right at the boundary
  @Test
  void powerOfTwoLengthOpensTheNextBit() {
    assertThat(sut.uniqueXorTriplets(perm(4))).isEqualTo(8);
  }

  // Step 7: n = 5 sits strictly inside the [4, 8) block and still answers 8 — the count is a step
  // function of the most significant bit, not anything proportional to n
  @Test
  void answerPlateausInsideAPowerOfTwoBlock() {
    assertThat(sut.uniqueXorTriplets(perm(5))).isEqualTo(8);
  }

  // Step 8: n = 7 tops out the [4, 8) block at the same 8 — one below a power of two must NOT spill
  // into the next block
  @Test
  void blockTopStillSharesThePlateau() {
    assertThat(sut.uniqueXorTriplets(perm(7))).isEqualTo(8);
  }

  // Step 9: n = 8 starts the next block — 16, i.e. the smallest power of two strictly greater than
  // n; together with steps 6-8 this pins 2^(msb(n)+1), not 2^msb(n) or a rounding of n
  @Test
  void nextPowerOfTwoDoublesTheCount() {
    assertThat(sut.uniqueXorTriplets(perm(8))).isEqualTo(16);
  }

  // Step 10: the constraint ceiling n = 1e5 — 2^16 = 65,536 <= 100,000 < 2^17, so the answer is
  // 131,072. Also a performance pin: enumerating triplets is ~1.7e14 combinations, so the solution
  // must derive the count from n alone in at most linear time
  @Test
  void maxLengthPermutationNeedsClosedFormCount() {
    assertThat(sut.uniqueXorTriplets(perm(100_000))).isEqualTo(131_072);
  }

  /** The identity permutation [1, 2, ..., n]. */
  private static int[] perm(int n) {
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i + 1;
    }
    return nums;
  }
}
