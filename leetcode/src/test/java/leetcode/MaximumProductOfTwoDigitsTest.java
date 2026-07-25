package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximumProductOfTwoDigitsTest {
  MaximumProductOfTwoDigits sut = new MaximumProductOfTwoDigits();

  // Step 1: smallest input the constraints allow (10 <= n) — two digits, one of them zero
  @Test
  void smallestInputProducesZero() {
    assertThat(sut.maxProduct(10)).isEqualTo(0);
  }

  // Step 2: two distinct digits leave exactly one product to choose (LeetCode Example 1)
  @Test
  void twoDistinctDigitsMultiplyTogether() {
    assertThat(sut.maxProduct(31)).isEqualTo(3);
  }

  // Step 3: a digit occurring twice may be paired with itself (LeetCode Example 2) — the Note.
  // Guards against picking the two largest *distinct values*, which would return 2 here.
  @Test
  void repeatedDigitPairsWithItself() {
    assertThat(sut.maxProduct(22)).isEqualTo(4);
  }

  // Step 4: more than two digits — the two largest win, not the first two (LeetCode Example 3)
  @Test
  void threeDigitsUseTheTwoLargest() {
    assertThat(sut.maxProduct(124)).isEqualTo(8);
  }

  // Step 5: digits in descending order — the pair sits at the front
  @Test
  void descendingDigitsUseTheLeadingPair() {
    assertThat(sut.maxProduct(987)).isEqualTo(72);
  }

  // Step 6: the same digits ascending — position must not affect the answer
  @Test
  void ascendingDigitsUseTheTrailingPair() {
    assertThat(sut.maxProduct(789)).isEqualTo(72);
  }

  // Step 7: a new maximum must demote the previous maximum to second place.
  // A tracker that overwrites the maximum without demoting returns 0 here.
  @Test
  void newMaximumDemotesPreviousMaximumToSecond() {
    assertThat(sut.maxProduct(1900)).isEqualTo(9);
  }

  // Step 8: the winning pair may sit entirely at the tail, so every digit must be scanned
  @Test
  void largestPairAtTheTailIsFound() {
    assertThat(sut.maxProduct(1099)).isEqualTo(81);
  }

  // Step 9: the two occurrences of the maximum are separated by a smaller digit —
  // a strict "second must be less than first" comparison would return 9 instead of 81
  @Test
  void separatedDuplicateMaximumsStillPair() {
    assertThat(sut.maxProduct(919)).isEqualTo(81);
  }

  // Step 10: interior zeros are ordinary digits, simply too small to be chosen
  @Test
  void interiorZerosAreIgnoredWhenLargerDigitsExist() {
    assertThat(sut.maxProduct(505)).isEqualTo(25);
  }

  // Step 11: only one nonzero digit — the best available partner is a zero
  @Test
  void singleNonzeroDigitPairsWithZero() {
    assertThat(sut.maxProduct(1000)).isEqualTo(0);
  }

  // Step 12: every digit identical — the pair is that digit with itself
  @Test
  void allIdenticalDigitsSquareThatDigit() {
    assertThat(sut.maxProduct(1111)).isEqualTo(1);
  }

  // Step 13: all nines gives the largest answer possible, 9 * 9
  @Test
  void allNinesProducesMaximumPossibleProduct() {
    assertThat(sut.maxProduct(999999999)).isEqualTo(81);
  }

  // Step 14: largest input the constraints allow (n <= 10^9) — ten digits, nine of them zero
  @Test
  void largestInputProducesZero() {
    assertThat(sut.maxProduct(1000000000)).isEqualTo(0);
  }
}
