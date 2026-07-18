package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindGreatesCommonDivisorOfArrayTest {
  FindGreatesCommonDivisorOfArray sut = new FindGreatesCommonDivisorOfArray();

  // Step 1: smallest valid input (length 2) with equal values — gcd(x, x) = x
  //         (LeetCode Example 3)
  @Test
  void twoEqualNumbersReturnThatNumber() {
    assertThat(sut.findGCD(new int[] {3, 3})).isEqualTo(3);
  }

  // Step 2: when the minimum divides the maximum, the minimum itself is the gcd
  @Test
  void minDividingMaxReturnsMin() {
    assertThat(sut.findGCD(new int[] {4, 8})).isEqualTo(4);
  }

  // Step 3: coprime extremes — no common divisor above 1, so the answer collapses to 1
  @Test
  void coprimeExtremesReturnOne() {
    assertThat(sut.findGCD(new int[] {3, 8})).isEqualTo(1);
  }

  // Step 4: a gcd strictly between 1 and the minimum — neither trivial case applies
  @Test
  void sharedFactorSmallerThanMin() {
    assertThat(sut.findGCD(new int[] {12, 18})).isEqualTo(6);
  }

  // Step 5: the classic multi-step Euclidean reduction —
  //         252 % 105 = 42, 105 % 42 = 21, 42 % 21 = 0
  @Test
  void multiStepEuclideanReduction() {
    assertThat(sut.findGCD(new int[] {105, 252})).isEqualTo(21);
  }

  // Step 6: middle elements never participate — 7 is coprime with both extremes,
  //         yet the answer is still gcd(4, 8)
  @Test
  void middleElementsDoNotAffectTheAnswer() {
    assertThat(sut.findGCD(new int[] {4, 7, 8})).isEqualTo(4);
  }

  // Step 7: LeetCode Example 1 — min 2, max 10, gcd(2, 10) = 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.findGCD(new int[] {2, 5, 6, 9, 10})).isEqualTo(2);
  }

  // Step 8: LeetCode Example 2 — min 3, max 8, gcd(3, 8) = 1
  @Test
  void leetCodeExample2() {
    assertThat(sut.findGCD(new int[] {7, 5, 6, 8, 3})).isEqualTo(1);
  }

  // Step 9: unsorted input with the maximum before the minimum — the scan must find
  //         both extremes regardless of position: min 12, max 30, gcd = 6
  @Test
  void maxAppearingBeforeMinIsStillFound() {
    assertThat(sut.findGCD(new int[] {30, 12, 18})).isEqualTo(6);
  }

  // Step 10: a 1 anywhere in the array becomes the minimum and forces gcd(1, max) = 1,
  //          here paired with the largest allowed value (1 <= nums[i] <= 1000)
  @Test
  void onePresentForcesGcdOfOne() {
    assertThat(sut.findGCD(new int[] {1, 1000})).isEqualTo(1);
  }

  // Step 11: duplicated extremes — repeated 6s and 15s must not change gcd(6, 15) = 3
  @Test
  void duplicatedExtremesDoNotChangeTheAnswer() {
    assertThat(sut.findGCD(new int[] {6, 15, 6, 15})).isEqualTo(3);
  }

  // Step 12: all elements equal in a longer array — min and max coincide, gcd is the value
  @Test
  void allEqualElementsReturnThatValue() {
    assertThat(sut.findGCD(new int[] {7, 7, 7, 7})).isEqualTo(7);
  }

  // Step 13: the constraint bounds (length 1000, values up to 1000) — 999 copies of 1000
  //          plus a single 625 give gcd(625, 1000) = 125 at full scale
  @Test
  void maximumSizeArrayAtValueBound() {
    int[] nums = new int[1000];
    java.util.Arrays.fill(nums, 1000);
    nums[500] = 625;
    assertThat(sut.findGCD(nums)).isEqualTo(125);
  }
}
