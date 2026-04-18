package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PermutationsTest {
  Permutations sut = new Permutations();

  // Step 1: single element — base case of recursion. Forces permute to return
  // a non-empty outer list containing exactly one inner list that wraps the value.
  @Test
  void singleElementReturnsItselfAsOnlyPermutation() {
    assertThat(sut.permute(new int[] {1})).containsExactly(List.of(1));
  }

  // Step 2: two elements — smallest case that genuinely requires choice.
  // 2! = 2 orderings; forces both [1,2] and [2,1] to appear, not just one pass.
  @Test
  void twoElementsReturnBothOrderings() {
    assertThat(sut.permute(new int[] {1, 2}))
        .containsExactlyInAnyOrder(List.of(1, 2), List.of(2, 1));
  }

  // Step 3: three elements (canonical LeetCode example) — 3! = 6 permutations.
  // Exercises recursion depth ≥ 3; catches state-leak between sibling branches
  // (the classic "swap but forget to undo" backtracking bug).
  @Test
  void threeElementsReturnSixPermutations() {
    assertThat(sut.permute(new int[] {1, 2, 3}))
        .containsExactlyInAnyOrder(
            List.of(1, 2, 3),
            List.of(1, 3, 2),
            List.of(2, 1, 3),
            List.of(2, 3, 1),
            List.of(3, 1, 2),
            List.of(3, 2, 1));
  }

  // Step 4: negative and zero values — verifies that the values themselves are
  // returned (not indices), and that sign/zero are handled correctly.
  @Test
  void threeElementsWithNegativeAndZeroValues() {
    assertThat(sut.permute(new int[] {-1, 0, 1}))
        .containsExactlyInAnyOrder(
            List.of(-1, 0, 1),
            List.of(-1, 1, 0),
            List.of(0, -1, 1),
            List.of(0, 1, -1),
            List.of(1, -1, 0),
            List.of(1, 0, -1));
  }

  // Step 5: four elements — 4! = 24. Beyond this point, exhaustive enumeration
  // is tedious and error-prone, so we assert the algebraic spec of a permutation
  // set: right count, every inner list permutes the input, no duplicates.
  @Test
  void fourElementsProduceTwentyFourUniquePermutations() {
    List<List<Integer>> result = sut.permute(new int[] {1, 2, 3, 4});
    assertThat(result)
        .hasSize(24)
        .allSatisfy(p -> assertThat(p).containsExactlyInAnyOrder(1, 2, 3, 4))
        .doesNotHaveDuplicates();
  }

  // Step 6: maximum length per LeetCode constraint (nums.length <= 6).
  // 6! = 720; same structural assertion at the constraint boundary.
  @Test
  void sixElementsReachMaxLengthConstraint() {
    List<List<Integer>> result = sut.permute(new int[] {1, 2, 3, 4, 5, 6});
    assertThat(result)
        .hasSize(720)
        .allSatisfy(p -> assertThat(p).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6))
        .doesNotHaveDuplicates();
  }
}
