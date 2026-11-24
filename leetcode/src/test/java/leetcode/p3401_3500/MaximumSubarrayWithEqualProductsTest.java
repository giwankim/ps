package leetcode.p3401_3500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumSubarrayWithEqualProductsTest {
  MaximumSubarrayWithEqualProducts sut = new MaximumSubarrayWithEqualProducts();

  // ===========================================================================================
  // Pairs: the answer is never below 2 (Steps 1-4).
  // ===========================================================================================

  // Step 1: the floor. The constraints say 2 <= nums.length, so there is no single-element input.
  //         prod = 1, gcd = 1, lcm = 1 -> 1 == 1 * 1, the whole array qualifies
  @Test
  void twoOnesAreProductEquivalent() {
    assertThat(sut.maxLength(new int[] {1, 1})).isEqualTo(2);
  }

  // Step 2: a lone 2 is not product equivalent (prod 2, lcm * gcd = 4) but the pair is
  //         (prod 6, gcd 1, lcm 6). Validity is not monotone in length, so a scan that extends
  //         from each start and breaks at the first failing length answers 0
  @Test
  void coprimePairQualifiesEvenThoughNeitherElementDoesAlone() {
    assertThat(sut.maxLength(new int[] {2, 3})).isEqualTo(2);
  }

  // Step 3: a * b == gcd * lcm holds for every pair, coprime or not: prod 8, gcd 2, lcm 4.
  //         A window that demands coprime neighbors answers 1, and one that demands gcd == 1
  //         answers 0
  @Test
  void pairSharingAFactorStillQualifies() {
    assertThat(sut.maxLength(new int[] {2, 4})).isEqualTo(2);
  }

  // Step 4: the same rule at the value ceiling with identical elements: prod 100, gcd 10, lcm 10
  @Test
  void identicalPairAtTheValueCeilingQualifies() {
    assertThat(sut.maxLength(new int[] {10, 10})).isEqualTo(2);
  }

  // ===========================================================================================
  // Three or more elements must be pairwise coprime (Steps 5-11).
  // ===========================================================================================

  // Step 5: the pair rule stops at length 3: prod 8, gcd 2, lcm 2 -> 8 != 4. Only the pairs
  //         survive
  @Test
  void threeEqualElementsFallBackToAPair() {
    assertThat(sut.maxLength(new int[] {2, 2, 2})).isEqualTo(2);
  }

  // Step 6: the gcd of the whole array is 1, yet 2 and 4 share a factor: prod 24, lcm 12.
  //         Checking gcd == 1 instead of pairwise coprimality answers 3, and so does computing
  //         lcm as prod / gcd
  @Test
  void setwiseCoprimeIsNotEnough() {
    assertThat(sut.maxLength(new int[] {2, 3, 4})).isEqualTo(2);
  }

  // Step 7: four distinct primes are pairwise coprime: prod 210, gcd 1, lcm 210
  @Test
  void pairwiseCoprimePrimesSpanTheWholeArray() {
    assertThat(sut.maxLength(new int[] {2, 3, 5, 7})).isEqualTo(4);
  }

  // Step 8: composites count too as long as they share no prime: 8 and 9 are coprime.
  //         prod = lcm = 2520, the largest lcm any valid subarray can reach
  @Test
  void coprimeCompositesSpanTheWholeArray() {
    assertThat(sut.maxLength(new int[] {5, 7, 8, 9})).isEqualTo(4);
  }

  // Step 9: the leading 2 collides with the second 2, so the best window starts at index 1
  @Test
  void conflictAtTheFrontLeavesTheTrailingWindow() {
    assertThat(sut.maxLength(new int[] {2, 2, 3, 5, 7})).isEqualTo(4);
  }

  // Step 10: the mirror of Step 9. The best window ends one short of the array
  @Test
  void conflictAtTheBackLeavesTheLeadingWindow() {
    assertThat(sut.maxLength(new int[] {3, 5, 7, 2, 2})).isEqualTo(4);
  }

  // Step 11: a subarray is contiguous. Dropping one 2 would give the coprime subsequence
  //          3, 2, 5, 7 of length 4, but no four adjacent elements avoid both 2s
  @Test
  void subarrayIsContiguousNotASubsequence() {
    assertThat(sut.maxLength(new int[] {3, 2, 5, 2, 7})).isEqualTo(3);
  }

  // ===========================================================================================
  // Ones are coprime to everything, including each other (Steps 12-17).
  // ===========================================================================================

  // Step 12: repeated 1s never conflict. A window keyed on distinct values, or a seen-set of
  //          elements instead of prime factors, answers 2
  @Test
  void repeatedOnesAllQualify() {
    assertThat(sut.maxLength(new int[] {1, 1, 1, 1, 1})).isEqualTo(5);
  }

  // Step 13: ones extend a window for free: prod 2, gcd 1, lcm 2. The break-at-first-failure
  //          scan of Step 2 gives up on the start at index 0 and answers 3
  @Test
  void onesExtendAWindowThatStartsAboveOne() {
    assertThat(sut.maxLength(new int[] {2, 1, 1, 1})).isEqualTo(4);
  }

  // Step 14: the mirror of Step 13
  @Test
  void onesExtendAWindowThatEndsAboveOne() {
    assertThat(sut.maxLength(new int[] {1, 1, 1, 2})).isEqualTo(4);
  }

  // Step 15: ones do not reset the factors already in the window. The two 2s still collide
  //          across the 1 between them, so the best is 1, 2, 1. A gcd == 1 check answers 5
  @Test
  void onesDoNotSeparateTwoConflictingElements() {
    assertThat(sut.maxLength(new int[] {1, 2, 1, 2, 1})).isEqualTo(3);
  }

  // Step 16: two runs of ones around a colliding pair. Each 2 can keep only its own side:
  //          1, 1, 2 on the left is 3 and 2, 1, 1, 1 on the right is 4
  @Test
  void longerRunOfOnesBesideTheConflictWins() {
    assertThat(sut.maxLength(new int[] {1, 1, 2, 2, 1, 1, 1})).isEqualTo(4);
  }

  // Step 17: the best window is strictly interior, 6, 1, 7, 5, with conflicts on both sides
  @Test
  void bestWindowIsInTheInterior() {
    assertThat(sut.maxLength(new int[] {6, 6, 1, 7, 5, 6, 6})).isEqualTo(4);
  }

  // ===========================================================================================
  // The official examples (Steps 18-20).
  // ===========================================================================================

  // Step 18: the window 1, 2, 1, 1, 1 holds a single 2. The whole array has gcd 1 and lcm 2
  //          but prod 4, so a gcd == 1 check answers 7
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxLength(new int[] {1, 2, 1, 2, 1, 1, 1})).isEqualTo(5);
  }

  // Step 19: 3, 4, 5 is the only pairwise coprime triple of neighbors, since 2 and 4 collide on
  //          the left and 4 and 6 on the right. No element is 1, so the break-at-first-failure
  //          scan answers 0
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxLength(new int[] {2, 3, 4, 5, 6})).isEqualTo(3);
  }

  // Step 20: 2 and 4 collide, so the window is 3, 1, 4, 5, 1 and holds two 1s. A distinct-value
  //          window answers 4
  @Test
  void leetCodeExample3() {
    assertThat(sut.maxLength(new int[] {1, 2, 3, 1, 4, 5, 1})).isEqualTo(5);
  }

  // ===========================================================================================
  // Constraint bounds: nums.length = 100, nums[i] up to 10 (Steps 21-26). With n this small
  // even an O(n^3) scan finishes instantly, so these carry no timeouts. What they pin down is
  // arithmetic: the product of a whole array reaches 10^100, far beyond long, so the product
  // must be bounded or the check done on prime factors.
  // ===========================================================================================

  // Step 21: the longest possible answer
  @Test
  void maximumLengthOfOnesQualifiesEntirely() {
    assertThat(sut.maxLength(filled(100, 1))).isEqualTo(100);
  }

  // Step 22: the shortest possible answer at maximum length and maximum value. The full
  //          product is 10^100. Only pairs survive
  @Test
  void maximumLengthOfTensFallsBackToAPair() {
    assertThat(sut.maxLength(filled(100, 10))).isEqualTo(2);
  }

  // Step 23: the two 2s sit at opposite ends, 99 apart, and still collide. A check that only
  //          compares neighbors, or gcd == 1, answers 100
  @Test
  void conflictSpanningTheWholeArrayDropsOneEnd() {
    int[] nums = filled(100, 1);
    nums[0] = 2;
    nums[99] = 2;
    assertThat(sut.maxLength(nums)).isEqualTo(99);
  }

  // Step 24: 48 ones, then 5, 7, 8, 9, then 48 ones. Everything is pairwise coprime with
  //          prod = lcm = 2520
  @Test
  void maximumLengthWithFourCoprimeValuesAmongOnesQualifiesEntirely() {
    int[] nums = filled(100, 1);
    nums[48] = 5;
    nums[49] = 7;
    nums[50] = 8;
    nums[51] = 9;
    assertThat(sut.maxLength(nums)).isEqualTo(100);
  }

  // Step 25: 2s at index 30 and index 61 split the ones into runs of 30, 30 and 38. The best
  //          window keeps the second 2 with the runs on both of its sides: 30 + 1 + 38 = 69
  @Test
  void twoConflictingElementsSplitTheOnesIntoUnevenRuns() {
    int[] nums = filled(100, 1);
    nums[30] = 2;
    nums[61] = 2;
    assertThat(sut.maxLength(nums)).isEqualTo(69);
  }

  // Step 26: the ramp 1, 2, ..., 10 repeated ten times. Even values are never more than two
  //          apart, so no window beats 1, 2, 3. A gcd == 1 check answers 100
  @Test
  void repeatingRampNeverExceedsThree() {
    int[] nums = new int[100];
    Arrays.setAll(nums, i -> i % 10 + 1);
    assertThat(sut.maxLength(nums)).isEqualTo(3);
  }

  // ===========================================================================================
  // Hygiene (Step 27).
  // ===========================================================================================

  // Step 27: one instance answers inputs of different sizes, largest in the middle. Factor
  //          bookkeeping kept on the instance and not reset per call leaks between them
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.maxLength(new int[] {2, 3, 5, 7})).isEqualTo(4);
    assertThat(sut.maxLength(filled(100, 1))).isEqualTo(100);
    assertThat(sut.maxLength(new int[] {2, 4})).isEqualTo(2);
    assertThat(sut.maxLength(new int[] {1, 2, 3, 1, 4, 5, 1})).isEqualTo(5);
    assertThat(sut.maxLength(new int[] {2, 2, 2})).isEqualTo(2);
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }
}
