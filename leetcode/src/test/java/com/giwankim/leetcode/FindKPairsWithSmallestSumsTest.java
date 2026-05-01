package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class FindKPairsWithSmallestSumsTest {
  FindKPairsWithSmallestSums sut = new FindKPairsWithSmallestSums();

  // Step 1: 1×1 arrays, k=1 — the only pair the inputs admit.
  @Test
  void singleElementEachReturnsOnePair() {
    assertThat(sut.kSmallestPairs(new int[] {1}, new int[] {2}, 1))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2)));
  }

  // Step 2: k=1 with multi-element arrays — the non-decreasing-sorted invariant pins the smallest
  // pair to (nums1[0], nums2[0]) without any search.
  @Test
  void k1PicksFirstElementsOfBothArrays() {
    assertThat(sut.kSmallestPairs(new int[] {1, 5}, new int[] {2, 8}, 1))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2)));
  }

  // Step 3: singleton nums1 — every result pair must share the only nums1 element; the algorithm
  // can only walk forward through nums2.
  @Test
  void singletonNums1ExtendsAlongNums2() {
    assertThat(sut.kSmallestPairs(new int[] {1}, new int[] {2, 4}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2), List.of(1, 4)));
  }

  // Step 4: singleton nums2 — mirror of Step 3, walking forward through nums1.
  @Test
  void singletonNums2ExtendsAlongNums1() {
    assertThat(sut.kSmallestPairs(new int[] {1, 4}, new int[] {2}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2), List.of(4, 2)));
  }

  // Step 5: k=2 where the second pair must extend through nums2 — a wide gap in nums1 (1 → 100)
  // makes (1,2) sum=3 the next-smallest, beating (100,1) sum=101.
  @Test
  void k2ExtendsAlongNums2WhenNums1HasLargeGap() {
    assertThat(sut.kSmallestPairs(new int[] {1, 100}, new int[] {1, 2}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 1), List.of(1, 2)));
  }

  // Step 6: k=2 where the second pair must extend through nums1 — symmetric mirror of Step 5
  // catching implementations that only advance one axis.
  @Test
  void k2ExtendsAlongNums1WhenNums2HasLargeGap() {
    assertThat(sut.kSmallestPairs(new int[] {1, 2}, new int[] {1, 100}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 1), List.of(2, 1)));
  }

  // Step 7: k=3 over [1,2] × [1,2] — after (1,1) sum=2, the next two smallest are (1,2) and (2,1),
  // both sum=3, on DIFFERENT axes from the seed. This is the first test that breaks a greedy
  // walk-one-array strategy and forces a true frontier (priority queue).
  @Test
  void k3NavigatesFrontierAcrossBothArrays() {
    assertThat(sut.kSmallestPairs(new int[] {1, 2}, new int[] {1, 2}, 3))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 1), List.of(1, 2), List.of(2, 1)));
  }

  // Step 8: k equals the total pair count — exhaustive boundary; the result is the full Cartesian
  // product. Catches off-by-one termination (returning k-1 or k+1 pairs).
  @Test
  void kEqualsTotalPairsReturnsAll() {
    assertThat(sut.kSmallestPairs(new int[] {1, 3}, new int[] {1, 3}, 4))
        .containsExactlyInAnyOrderElementsOf(
            List.of(List.of(1, 1), List.of(1, 3), List.of(3, 1), List.of(3, 3)));
  }

  // Step 9: negative values — confirms the sorted-input invariant still anchors the smallest pair
  // at (nums1[0], nums2[0]) when sums can themselves be negative.
  @Test
  void negativeValuesK1PicksMostNegativePair() {
    assertThat(sut.kSmallestPairs(new int[] {-2, 3}, new int[] {-1, 4}, 1))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(-2, -1)));
  }

  // Step 10: LeetCode Example 1 — nums1=[1,7,11], nums2=[2,4,6], k=3 → [[1,2],[1,4],[1,6]].
  // All three winners share nums1[0]=1 because the jump to nums1[1]=7 dwarfs every nums2 step.
  @Test
  void leetCodeExampleOneAllFromFirstNums1Element() {
    assertThat(sut.kSmallestPairs(new int[] {1, 7, 11}, new int[] {2, 4, 6}, 3))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2), List.of(1, 4), List.of(1, 6)));
  }

  // Step 11: LeetCode Example 2 — nums1=[1,1,2], nums2=[1,2,3], k=2 → [[1,1],[1,1]].
  // Two distinct-but-equal-valued nums1 entries pair with the same nums2[0]=1 to produce duplicate
  // output pairs — the result is a multiset, not a set.
  @Test
  void leetCodeExampleTwoDuplicatesProduceIdenticalPairs() {
    assertThat(sut.kSmallestPairs(new int[] {1, 1, 2}, new int[] {1, 2, 3}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 1), List.of(1, 1)));
  }

  // Step 12: extreme LeetCode-bounded values — guards against int overflow in the heap comparator.
  // Pair sums fit in int (max |sum| = 2e9), but a naive `sumB - sumA` subtracts them and can
  // produce 4e9, wrapping past Integer.MAX_VALUE and inverting the comparator's verdict. With
  // k=1 the smallest pair (-1e9, -1e9) must be returned; an overflowing comparator returns the
  // largest-sum pair (1e9, 1e9) instead.
  @Test
  void k1WithExtremeValuesPicksMostNegativePairWithoutOverflow() {
    assertThat(sut.kSmallestPairs(
            new int[] {-1_000_000_000, 1_000_000_000},
            new int[] {-1_000_000_000, 1_000_000_000},
            1))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(-1_000_000_000, -1_000_000_000)));
  }

  // Step 13: duplicates inside nums2 only — mirror of Step 11. Two distinct-but-equal-valued
  // nums2 entries pair with the same nums1[0]=1 to produce duplicate output pairs, confirming
  // the algorithm treats both axes symmetrically.
  @Test
  void duplicatesInNums2ProduceIdenticalPairs() {
    assertThat(sut.kSmallestPairs(new int[] {1, 2, 3}, new int[] {1, 1, 2}, 2))
        .containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 1), List.of(1, 1)));
  }

  // Step 14: k = 10_000 (the constraint upper bound for k) on a 100×100 grid where k = m·n.
  // Returns the full Cartesian product. Catches TLE, off-by-one termination, and any algorithmic
  // pathology that surfaces only at scale.
  @Test
  void kAtUpperBoundReturnsFullCartesianProduct() {
    int n = 100;
    int[] nums1 = IntStream.range(0, n).toArray();
    int[] nums2 = IntStream.range(0, n).toArray();
    int k = n * n; // 10_000 — constraint upper bound for k

    List<List<Integer>> expected = new ArrayList<>(k);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        expected.add(List.of(i, j));
      }
    }

    assertThat(sut.kSmallestPairs(nums1, nums2, k)).containsExactlyInAnyOrderElementsOf(expected);
  }

  // Step 15: nums1.length = 100_000 (the constraint upper bound for length) with a singleton
  // nums2 and k = 10_000. The algorithm must walk forward through nums1 and stop at exactly k
  // pairs without exploring the rest. Catches over-eager exploration that scales with m·n
  // instead of k, and OOM in the visited[][] allocation.
  @Test
  void lengthAtUpperBoundWithSingletonOtherAxis() {
    int m = 100_000;
    int[] nums1 = IntStream.range(0, m).toArray();
    int[] nums2 = {0};
    int k = 10_000;

    List<List<Integer>> expected = new ArrayList<>(k);
    for (int i = 0; i < k; i++) {
      expected.add(List.of(i, 0));
    }

    assertThat(sut.kSmallestPairs(nums1, nums2, k)).containsExactlyInAnyOrderElementsOf(expected);
  }
}
