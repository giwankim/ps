package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class CombinationSumTest {
  CombinationSum sut = new CombinationSum();

  // Step 1: target unreachable from any candidate (LeetCode Example 3)
  @Test
  void targetUnreachableReturnsEmpty() {
    assertThat(sut.combinationSum(new int[] {2}, 1)).isEmpty();
  }

  // Step 2: single candidate exactly equal to target — one one-element combination
  @Test
  void singleCandidateEqualToTargetReturnsItself() {
    assertThat(sut.combinationSum(new int[] {5}, 5)).containsExactlyInAnyOrder(List.of(5));
  }

  // Step 3: single candidate reused multiple times — locks unlimited reuse
  @Test
  void singleCandidateRepeatedToReachTarget() {
    assertThat(sut.combinationSum(new int[] {3}, 9)).containsExactlyInAnyOrder(List.of(3, 3, 3));
  }

  // Step 4: single candidate does not divide target — prune branch reaches target < 0
  @Test
  void singleCandidateNotDivisibleIntoTargetReturnsEmpty() {
    assertThat(sut.combinationSum(new int[] {3}, 10)).isEmpty();
  }

  // Step 5: two distinct candidates combined exactly once
  @Test
  void twoCandidatesCombinedOnce() {
    assertThat(sut.combinationSum(new int[] {2, 3}, 5)).containsExactlyInAnyOrder(List.of(2, 3));
  }

  // Step 6: same candidate reused before adding a different one — reuse + mix together
  @Test
  void sameCandidateReusedThenDifferentCandidate() {
    assertThat(sut.combinationSum(new int[] {2, 3}, 7)).containsExactlyInAnyOrder(List.of(2, 2, 3));
  }

  // Step 7: first case where more than one combination exists
  @Test
  void twoCandidatesYieldDistinctCombinations() {
    assertThat(sut.combinationSum(new int[] {2, 3}, 6))
        .containsExactlyInAnyOrder(List.of(2, 2, 2), List.of(3, 3));
  }

  // Step 8: LeetCode Example 1 — mix of repetition and single-element combinations
  @Test
  void leetCodeExampleOneReturnsTwoCombinations() {
    assertThat(sut.combinationSum(new int[] {2, 3, 6, 7}, 7))
        .containsExactlyInAnyOrder(List.of(2, 2, 3), List.of(7));
  }

  // Step 9: LeetCode Example 2 — three combinations including a long repetition
  @Test
  void leetCodeExampleTwoReturnsThreeCombinations() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8))
        .containsExactlyInAnyOrder(List.of(2, 2, 2, 2), List.of(2, 3, 3), List.of(3, 5));
  }

  // Step 10: all candidates strictly exceed the target — no combination possible
  @Test
  void allCandidatesExceedTargetReturnsEmpty() {
    assertThat(sut.combinationSum(new int[] {5, 6, 7}, 3)).isEmpty();
  }

  // Step 11: a too-large candidate is skipped but smaller ones still produce combinations
  @Test
  void candidateExceedingTargetIsSkippedButSmallerOnesStillUsed() {
    assertThat(sut.combinationSum(new int[] {2, 10}, 4)).containsExactlyInAnyOrder(List.of(2, 2));
  }

  // Step 12: caller passes target == 0 directly — base case yields a single empty combination
  @Test
  void zeroTargetReturnsSingleEmptyCombination() {
    assertThat(sut.combinationSum(new int[] {2, 3}, 0))
        .hasSize(1)
        .allSatisfy(c -> assertThat(c).isEmpty());
  }

  // Step 13: property — every combination sums to the target
  @Test
  void everyCombinationSumsToTarget() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8))
        .allSatisfy(
            c -> assertThat(c.stream().mapToInt(Integer::intValue).sum()).isEqualTo(8));
  }

  // Step 14: property — every element in every combination is one of the candidates
  @Test
  void everyElementInEveryCombinationIsACandidate() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8))
        .allSatisfy(c -> assertThat(c).isSubsetOf(2, 3, 5));
  }

  // Step 15: property — no duplicate combinations in the result
  @Test
  void allCombinationsAreDistinct() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8)).doesNotHaveDuplicates();
  }

  // Step 16: property — each combination is non-decreasing (locks the start-index dedup invariant)
  @Test
  void everyCombinationIsNonDecreasing() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8))
        .allSatisfy(c -> assertThat(c).isSorted());
  }

  // Step 17: implementation-locking regression — DFS traversal yields results in this exact order
  @Test
  void combinationsAreReturnedInDepthFirstOrder() {
    assertThat(sut.combinationSum(new int[] {2, 3, 5}, 8))
        .containsExactly(List.of(2, 2, 2, 2), List.of(2, 3, 3), List.of(3, 5));
  }
}
