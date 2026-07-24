package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NumberOfUniqueXORTripletsIITest {
  NumberOfUniqueXORTripletsII sut = new NumberOfUniqueXORTripletsII();

  // Step 1: LeetCode Example 1 — with only [1,3], every legal triplet repeats at least one index,
  // so pairwise cancellation leaves either 1 or 3 and exactly two unique values
  @Test
  void twoElementsCanOnlyProduceTheirOwnValues() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 3})).isEqualTo(2);
  }

  // Step 2: LeetCode Example 2 — for [6,7,8,9], XORing any three distinct elements yields the
  // omitted fourth element, while repeated indices also reduce to an input value
  @Test
  void fourElementsCanStillCollapseToFourValues() {
    assertThat(sut.uniqueXorTriplets(new int[] {6, 7, 8, 9})).isEqualTo(4);
  }

  // Step 3: constraint floor nums.length = 1 — the sole legal choice is i = j = k, and
  // 42 XOR 42 XOR 42 = 42, so an arbitrary singleton contributes exactly one value
  @Test
  void singletonHasOnlyItsOwnValue() {
    assertThat(sut.uniqueXorTriplets(new int[] {42})).isEqualTo(1);
  }

  // Step 4: unlike problem I, nums may contain duplicates — every triplet from [5,5,5,5] is
  // 5 XOR 5 XOR 5 = 5 regardless of which legal indices are chosen
  @Test
  void duplicateValuesDoNotCreateAdditionalResults() {
    assertThat(sut.uniqueXorTriplets(new int[] {5, 5, 5, 5})).isEqualTo(1);
  }

  // Step 5: three distinct indices can create a value absent from nums — repeated-index triplets
  // yield {1,2,4}, while 1 XOR 2 XOR 4 adds 7
  @Test
  void threeDistinctElementsCanAddANewValue() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 2, 4})).isEqualTo(4);
  }

  // Step 6: even though every nums[i] is positive, a triplet result may be zero because
  // 1 XOR 2 XOR 3 = 0; together with the three input values the result set is {0,1,2,3}
  @Test
  void positiveInputsCanProduceZero() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 2, 3})).isEqualTo(4);
  }

  // Step 7: [1,2,4,8] has four input values plus one new value from each three-distinct-index
  // choice: {7,11,13,14}; all four combinations must be considered, for eight results total
  @Test
  void everyThreeDistinctIndexChoiceContributes() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 2, 4, 8})).isEqualTo(8);
  }

  // Step 8: i <= j <= k orders indices, not values — shuffling the same multiset does not change
  // which element combinations exist, and XOR itself is commutative
  @Test
  void inputOrderDoesNotChangeReachableValues() {
    assertThat(sut.uniqueXorTriplets(new int[] {8, 1, 4, 2})).isEqualTo(8);
  }

  // Step 9: the result domain is wider than the input bound — 1 XOR 1023 XOR 1025 = 2047, so the
  // reachable values are {1,1023,1025,2047}; storage sized only for values 0..1500 is insufficient
  @Test
  void xorResultCanExceedTheInputUpperBound() {
    assertThat(sut.uniqueXorTriplets(new int[] {1, 1023, 1025})).isEqualTo(4);
  }

  // Step 10: constraint ceilings nums.length = 1500 and max(nums[i]) = 1500 via [1..1500] — its
  // triplets cover every 11-bit value 0..2047. This also rules out cubic enumeration of roughly
  // 563 million legal index triples in favor of reusing intermediate XOR results as the hints
  // require
  @Test
  void maxInputCoversTheEntireXorDomain() {
    assertThat(sut.uniqueXorTriplets(rangeFromOne(1500))).isEqualTo(2048);
  }

  /** Returns [1, 2, ..., n]. */
  private static int[] rangeFromOne(int n) {
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i + 1;
    }
    return nums;
  }
}
