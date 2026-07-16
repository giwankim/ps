package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SumOfGcdOfFormedPairsTest {
  SumOfGcdOfFormedPairs sut = new SumOfGcdOfFormedPairs();

  // Step 1: a single element is the middle of its own sorted array, so it stays unpaired and is
  // ignored — the smallest input, and the sum of zero pairs is 0
  @Test
  void singleElementFormsNoPairs() {
    assertThat(sut.gcdSum(new int[] {7})).isEqualTo(0L);
  }

  // Step 2: two equal elements form the only pair, and gcd(5, 5) collapses to the value itself —
  // the simplest input with a non-zero answer
  @Test
  void twoEqualElementsPairToTheirValue() {
    assertThat(sut.gcdSum(new int[] {5, 5})).isEqualTo(5L);
  }

  // Step 3: gcd(4, 6) = 2 is neither element — the pair contributes the true gcd, not the min,
  // the max, or the sum of its members
  @Test
  void pairContributesGcdNotEitherElement() {
    assertThat(sut.gcdSum(new int[] {4, 6})).isEqualTo(2L);
  }

  // Step 4: LeetCode Example 1 — the trailing 4 is gcd'd with the earlier prefix max 6 (not with
  // itself), giving prefixGcd = [2, 6, 2]; sorted, the ends pair to gcd(2, 6) = 2 and the middle
  // 2 is dropped
  @Test
  void exampleOneGcdsLaterElementWithEarlierMax() {
    assertThat(sut.gcdSum(new int[] {2, 6, 4})).isEqualTo(2L);
  }

  // Step 5: LeetCode Example 2 — prefixGcd = [3, 6, 2, 8] must be sorted before pairing: sorted
  // ends give gcd(2, 8) + gcd(3, 6) = 5, while pairing the unsorted ends would give only 3
  @Test
  void exampleTwoPairsOnTheSortedArrayNotInputOrder() {
    assertThat(sut.gcdSum(new int[] {3, 6, 2, 8})).isEqualTo(5L);
  }

  // Step 6: a strictly decreasing array freezes the prefix max at the first element, so every
  // later entry gcds against 12 — prefixGcd = [12, 3, 6, 3] pairs to gcd(3, 12) + gcd(3, 6) = 6
  @Test
  void decreasingArrayKeepsFirstElementAsPrefixMax() {
    assertThat(sut.gcdSum(new int[] {12, 9, 6, 3})).isEqualTo(6L);
  }

  // Step 7: in a strictly increasing array every element is its own prefix max, so prefixGcd is
  // the array itself — pairs gcd(1, 5) + gcd(2, 4) = 3 with the middle 3 dropped
  @Test
  void increasingArrayMakesEachElementItsOwnMax() {
    assertThat(sut.gcdSum(new int[] {1, 2, 3, 4, 5})).isEqualTo(3L);
  }

  // Step 8: with sorted prefixGcd [4, 6, 9] the answer is gcd(4, 9) = 1 — dropping the smallest
  // instead would give 3 and dropping the largest would give 2, so this pins that the sorted
  // MIDDLE element is the one ignored
  @Test
  void oddLengthDropsTheSortedMiddleElement() {
    assertThat(sut.gcdSum(new int[] {4, 6, 9})).isEqualTo(1L);
  }

  // Step 9: a medium case away from any boundary — the prefix max moves twice (18 → 30 → 36 → 45)
  // and four pairs mix gcds of 3, 6, 3, and 9 into 21, a total an off-by-one in the two-pointer
  // pairing or a stale prefix max would be unlikely to reproduce by accident
  @Test
  void mediumMixedCaseMovesThePrefixMaxSeveralTimes() {
    assertThat(sut.gcdSum(new int[] {18, 30, 12, 24, 36, 9, 27, 45})).isEqualTo(21L);
  }

  // Step 10: both constraint ceilings at once — 1e5 copies of 1e9 form 50,000 pairs of
  // gcd 1e9 each, so the sum 5e13 overflows int and the O(n log n) workload must finish fast
  @Test
  void maxWorkloadAtValueCeilingNeedsLongSum() {
    int[] nums = new int[100_000];
    java.util.Arrays.fill(nums, 1_000_000_000);
    assertThat(sut.gcdSum(nums)).isEqualTo(50_000_000_000_000L);
  }
}
