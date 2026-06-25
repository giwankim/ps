package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximumTotalSubarrayValueITest {
  MaximumTotalSubarrayValueI sut = new MaximumTotalSubarrayValueI();

  // Step 1: smallest valid input — a length-1 array has only one subarray, whose
  // max and min are the same element, so its value (max - min) is 0 (1 <= n).
  @Test
  void singleElementHasNoSpread() {
    assertThat(sut.maxTotalValue(new int[] {5}, 1)).isEqualTo(0);
  }

  // Step 2: two distinct elements — the value is simply their difference, proving
  // we actually compute max - min rather than always returning 0.
  @Test
  void twoElementsYieldTheirDifference() {
    assertThat(sut.maxTotalValue(new int[] {1, 3}, 1)).isEqualTo(2);
  }

  // Step 3: the value depends on the GLOBAL spread, not on adjacent differences —
  // here every consecutive gap is 1, but the best subarray spans 4 - 1 = 3.
  @Test
  void usesGlobalSpreadNotAdjacentDifferences() {
    assertThat(sut.maxTotalValue(new int[] {1, 2, 3, 4}, 1)).isEqualTo(3);
  }

  // Step 4: the max (5) and min (2) can both sit in the interior, so a full scan is
  // required — peeking only at the endpoints (3 and 4) would give the wrong answer.
  @Test
  void extremesCanBeInteriorElements() {
    assertThat(sut.maxTotalValue(new int[] {3, 5, 2, 4}, 1)).isEqualTo(3);
  }

  // Step 5: LeetCode Example 1 — with k = 2 the best subarray (value 2) is simply
  // chosen twice, so the total scales to 2 * 2 = 4. This pins down multiplying by k.
  @Test
  void repeatingTheBestSubarrayScalesByK() {
    assertThat(sut.maxTotalValue(new int[] {1, 3, 2}, 2)).isEqualTo(4);
  }

  // Step 6: LeetCode Example 2 — best spread is 5 - 1 = 4, taken k = 3 times = 12.
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxTotalValue(new int[] {4, 2, 5, 1}, 3)).isEqualTo(12);
  }

  // Step 7: a constant array has zero spread, so the total is 0 no matter how large
  // k is — guards against accidentally adding k or returning k instead of k * spread.
  @Test
  void constantArrayYieldsZeroForAnyK() {
    assertThat(sut.maxTotalValue(new int[] {7, 7, 7}, 5)).isEqualTo(0);
  }

  // Step 8: only one subarray exists for a single element, yet k may be as large as
  // 1e5 (1 <= k); the value stays 0 and nothing overflows.
  @Test
  void singleElementWithLargeKStaysZero() {
    assertThat(sut.maxTotalValue(new int[] {5}, 100_000)).isEqualTo(0);
  }

  // Step 9: zero is a legal value (0 <= nums[i]); the spread here is 9 - 0 = 9.
  @Test
  void handlesZeroValuedElements() {
    assertThat(sut.maxTotalValue(new int[] {0, 9}, 1)).isEqualTo(9);
  }

  // Step 10: the upper constraint bounds (nums[i] up to 1e9, k up to 1e5) push the
  // answer to 1e9 * 1e5 = 1e14, which overflows a 32-bit int — the result must be
  // computed with long arithmetic (hence the long return type).
  @Test
  void largeProductRequiresLongArithmetic() {
    assertThat(sut.maxTotalValue(new int[] {0, 1_000_000_000}, 100_000))
        .isEqualTo(100_000_000_000_000L);
  }
}
