package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.sun.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximumTotalSubarrayValueIITest {
  MaximumTotalSubarrayValueII sut = new MaximumTotalSubarrayValueII();

  // Step 1: smallest valid input — a length-1 array has only one subarray, whose
  // max and min are the same element, so its value (max - min) is 0 (1 <= n).
  @Test
  void singleElementHasNoSpread() {
    assertThat(sut.maxTotalValue(new int[] {5}, 1)).isEqualTo(0);
  }

  // Step 2: two distinct elements with k = 1 — the best subarray is the whole
  // array and its value is simply max - min, proving the spread is computed at all.
  @Test
  void wholeArrayGivesMaxMinusMin() {
    assertThat(sut.maxTotalValue(new int[] {1, 3}, 1)).isEqualTo(2);
  }

  // Step 3: THE behavior that separates II from I — subarrays must be distinct
  // (l, r) pairs, so the best subarray [1,3] (value 2) can be taken only once and
  // the second pick is a singleton worth 0. Version I would answer 2 * 2 = 4.
  @Test
  void bestSubarrayCannotBeChosenTwice() {
    assertThat(sut.maxTotalValue(new int[] {1, 3}, 2)).isEqualTo(2);
  }

  // Step 4: LeetCode Example 1 — two DIFFERENT subarrays, [1,3] and [1,3,2], both
  // achieve spread 3 - 1 = 2, so distinctness still allows a total of 2 + 2 = 4.
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxTotalValue(new int[] {1, 3, 2}, 2)).isEqualTo(4);
  }

  // Step 5: LeetCode Example 2 — [4,2,5,1], [2,5,1], and [5,1] all span the global
  // max 5 and min 1, giving 4 + 4 + 4 = 12.
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxTotalValue(new int[] {4, 2, 5, 1}, 3)).isEqualTo(12);
  }

  // Step 6: the top-k must be ranked across left endpoints — the two best come
  // from l = 0 ([5,0] = 5 and [5,0,3] = 5) but the third best is [0,3] = 3 from
  // l = 1, beating the remaining zero-value singletons: 5 + 5 + 3 = 13.
  @Test
  void ranksValuesAcrossDifferentLeftEndpoints() {
    assertThat(sut.maxTotalValue(new int[] {5, 0, 3}, 3)).isEqualTo(13);
  }

  // Step 7: k may equal n * (n + 1) / 2, exhausting every distinct subarray — for
  // [1,3,2] the six values are 2 ([1,3]), 2 ([1,3,2]), 1 ([3,2]), and three zero
  // singletons, summing to 5.
  @Test
  void kExhaustsAllDistinctSubarrays() {
    assertThat(sut.maxTotalValue(new int[] {1, 3, 2}, 6)).isEqualTo(5);
  }

  // Step 8: a constant array has zero spread in every subarray, so the total is 0
  // regardless of k — guards against adding k or counting subarrays instead of values.
  @Test
  void constantArrayYieldsZeroForAnyK() {
    assertThat(sut.maxTotalValue(new int[] {7, 7, 7}, 6)).isEqualTo(0);
  }

  // Step 9: zero is a legal element value (0 <= nums[i]); the spread here is 9 - 0 = 9.
  @Test
  void handlesZeroValuedElements() {
    assertThat(sut.maxTotalValue(new int[] {0, 9}, 1)).isEqualTo(9);
  }

  // Step 10: [0..1], [1..2], and [0..2] each span 1e9 and 0, so the top three sum
  // to 3e9, which exceeds Integer.MAX_VALUE — the total must accumulate in a long.
  @Test
  void sumOfTopValuesOverflowsInt() {
    assertThat(sut.maxTotalValue(new int[] {0, 1_000_000_000, 0}, 3)).isEqualTo(3_000_000_000L);
  }

  // Step 11: at the constraint bounds (n = 5 * 10^4, k = 10^5) there are ~1.25e9
  // subarrays, so enumerating them all is infeasible — the top k must be found
  // lazily (e.g., RMQ + max-heap). For nums = [0, 1, ..., n-1] the value of
  // nums[l..r] is r - l, and spread d is shared by n - d subarrays; the top
  // 100,000 values are all subarrays with d > 49,553 (446 * 447 / 2 = 99,681 of
  // them) plus 319 more of spread 49,553:
  // sum(m * (n - m) for m in 1..446) + 319 * 49,553 = 4,970,185,696.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeInputDemandsLazyTopKEnumeration() {
    int n = 50_000;
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i);

    assertThat(sut.maxTotalValue(nums, 100_000)).isEqualTo(4_970_185_696L);
  }

  // Step 12: memory — n x n precomputation needs 3.2 GB already at n = 20,000 (and
  // 20 GB at the n = 5 * 10^4 bound), while an O(n log n) sparse table needs ~5 MB.
  // HotSpot's per-thread allocation counter gives a deterministic measurement: only
  // bytes allocated by THIS thread count, so JIT and GC activity do not pollute it.
  // The 64 MB budget is 10x headroom for a linearithmic solution and ~50x below a
  // quadratic one. The expected total follows the Step 11 closed form with
  // n = 20,000: sum(m * (n - m) for m in 1..446) + 319 * 19,553 = 1,970,185,696.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void staysWithinLinearithmicMemoryBudget() {
    ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
    assumeTrue(threadMXBean.isThreadAllocatedMemorySupported());
    assumeTrue(threadMXBean.isThreadAllocatedMemoryEnabled());
    int n = 20_000;
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i);

    long before = threadMXBean.getCurrentThreadAllocatedBytes();
    long total = sut.maxTotalValue(nums, 100_000);
    long allocated = threadMXBean.getCurrentThreadAllocatedBytes() - before;

    assertThat(total).isEqualTo(1_970_185_696L);
    assertThat(allocated).isLessThan(64L * 1024 * 1024);
  }
}
