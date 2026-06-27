package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindTheMaximumNumberOfElementsInSubsetTest {
  FindTheMaximumNumberOfElementsInSubset sut = new FindTheMaximumNumberOfElementsInSubset();

  // Step 1: smallest valid input — two distinct values with no power relation;
  //         each is only its own length-1 mountain [x]
  @Test
  void distinctValuesEachFormASingletonMountain() {
    assertThat(sut.maximumLength(new int[] {2, 3})).isEqualTo(1);
  }

  // Step 2: a base and its square but only one copy of the base — a mountain shoulder
  //         needs two copies of the base, so neither value can climb; answer stays 1
  @Test
  void singleBaseCopyCannotClimbToItsSquare() {
    assertThat(sut.maximumLength(new int[] {3, 9})).isEqualTo(1);
  }

  // Step 3: a duplicated value has no distinct peak above it ([4, 16, 4] would need 16),
  //         so the pair collapses back to a single element
  @Test
  void duplicatedValueWithoutPeakIsJustOneElement() {
    assertThat(sut.maximumLength(new int[] {4, 4})).isEqualTo(1);
  }

  // Step 4: the canonical three-element mountain [x, x^2, x] — two bases and one peak
  @Test
  void basicThreeElementMountain() {
    assertThat(sut.maximumLength(new int[] {3, 9, 3})).isEqualTo(3);
  }

  // Step 5: LeetCode Example 1 — {4, 2, 2} arranges as [2, 4, 2] since 2^2 == 4
  @Test
  void leetCodeExample1() {
    assertThat(sut.maximumLength(new int[] {5, 4, 1, 2, 2})).isEqualTo(3);
  }

  // Step 6: LeetCode Example 2 — no value has the two copies needed to climb, so every
  //         element is only a singleton mountain
  @Test
  void leetCodeExample2() {
    assertThat(sut.maximumLength(new int[] {1, 3, 2, 4})).isEqualTo(1);
  }

  // Step 7: a mountain is symmetric, so a third copy of the base cannot extend it —
  //         [2, 2, 2, 4] still yields only [2, 4, 2]
  @Test
  void extraCopiesOfTheBaseDoNotLengthenTheMountain() {
    assertThat(sut.maximumLength(new int[] {2, 2, 2, 4})).isEqualTo(3);
  }

  // Step 8: the peak is the first level that cannot be doubled — with one 4 the chain
  //         stops at 4 and the lone 16 above it is unusable
  @Test
  void chainPeaksAtFirstNonDoubledLevelLeavingHigherPowerUnused() {
    assertThat(sut.maximumLength(new int[] {2, 2, 4, 16})).isEqualTo(3);
  }

  // Step 9: a fully paired chain whose true peak (16) is absent must drop its top pair to
  //         an odd length — {2:2, 4:2} realizes as [2, 4, 2]
  @Test
  void pairedChainWithMissingPeakCollapsesToOddLength() {
    assertThat(sut.maximumLength(new int[] {2, 2, 4, 4})).isEqualTo(3);
  }

  // Step 10: a deeper five-element mountain [2, 4, 16, 4, 2] — both shoulders doubled,
  //          peak 16 present once
  @Test
  void fiveElementMountainWithPeakPresent() {
    assertThat(sut.maximumLength(new int[] {2, 2, 4, 4, 16})).isEqualTo(5);
  }

  // Step 11: the base-1 special case — 1^k == 1 for every k, so an odd run of 1s is itself
  //          a valid mountain [1, 1, 1]
  @Test
  void oddRunOfOnesFormsAMountain() {
    assertThat(sut.maximumLength(new int[] {1, 1, 1})).isEqualTo(3);
  }

  // Step 12: a mountain length is always odd, so an even run of 1s must leave one out
  @Test
  void evenRunOfOnesDropsTheSpare() {
    assertThat(sut.maximumLength(new int[] {1, 1, 1, 1})).isEqualTo(3);
  }

  // Step 13: the ones can win — five 1s beat the [2, 4, 2] available from the other values
  @Test
  void onesCanOutnumberAPowerChain() {
    assertThat(sut.maximumLength(new int[] {1, 1, 1, 1, 1, 2, 2, 4})).isEqualTo(5);
  }

  // Step 14: a power chain can win — [3, 9, 81, 9, 3] beats the pair of 1s
  @Test
  void powerChainCanOutnumberTheOnes() {
    assertThat(sut.maximumLength(new int[] {1, 1, 3, 3, 9, 9, 81})).isEqualTo(5);
  }

  // Step 15: values reach 10^9, so squaring a base must use 64-bit arithmetic —
  //          31622^2 == 999950884 forms the valid triple [31622, 999950884, 31622]
  @Test
  void largeValuedTripleNearTheConstraintBound() {
    assertThat(sut.maximumLength(new int[] {31622, 999950884, 31622})).isEqualTo(3);
  }

  // Step 16: squaring a large base (46341^2 > Integer.MAX_VALUE) must not overflow into a
  //          phantom peak — with no real peak the duplicated base stays a single element
  @Test
  void squaringALargeBaseMustNotOverflowIntoAPhantomPeak() {
    assertThat(sut.maximumLength(new int[] {46341, 46341})).isEqualTo(1);
  }
}
