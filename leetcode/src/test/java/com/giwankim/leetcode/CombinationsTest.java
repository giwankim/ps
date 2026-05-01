package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CombinationsTest {
  Combinations sut = new Combinations();

  // Step 1 — smallest possible input: a single element chosen one way.
  @Test
  void n1k1ReturnsSingleton() {
    assertThat(sut.combine(1, 1)).containsExactlyInAnyOrder(List.of(1));
  }

  // Step 2 — k = 1 with multiple elements: each element produces its own
  // singleton, exercising the exclude branch for the first time.
  @Test
  void n2k1ReturnsTwoSingletons() {
    assertThat(sut.combine(2, 1)).containsExactlyInAnyOrder(List.of(1), List.of(2));
  }

  // Step 3 — k = n at n = 2: forces correct termination when the include
  // path immediately fills the combination after one recursive step.
  @Test
  void n2k2ReturnsAllElements() {
    assertThat(sut.combine(2, 2)).containsExactlyInAnyOrder(List.of(1, 2));
  }

  // Step 4 — smallest case where combinatorial structure becomes visible:
  // all three pairs from {1, 2, 3}.
  @Test
  void n3k2ReturnsAllThreePairs() {
    assertThat(sut.combine(3, 2))
        .containsExactlyInAnyOrder(List.of(1, 2), List.of(1, 3), List.of(2, 3));
  }

  // Step 5 — k = n at n = 3: only the all-included combination, with
  // multiple chained include decisions.
  @Test
  void n3k3ReturnsAllElements() {
    assertThat(sut.combine(3, 3)).containsExactlyInAnyOrder(List.of(1, 2, 3));
  }

  // Step 6 — k = 1 at n = 4: every element appears as a distinct singleton.
  @Test
  void n4k1ReturnsFourSingletons() {
    assertThat(sut.combine(4, 1))
        .containsExactlyInAnyOrder(List.of(1), List.of(2), List.of(3), List.of(4));
  }

  // Step 7 — moderate k = 2 with n = 4: all six pairs from {1, 2, 3, 4}.
  @Test
  void n4k2ReturnsSixPairs() {
    assertThat(sut.combine(4, 2))
        .containsExactlyInAnyOrder(
            List.of(1, 2),
            List.of(1, 3),
            List.of(1, 4),
            List.of(2, 3),
            List.of(2, 4),
            List.of(3, 4));
  }

  // Step 8 — k = n − 1: each result omits exactly one element; structurally
  // dual to the k = 1 cases (n results, each missing a different value).
  @Test
  void n4k3OmitsEachElementInTurn() {
    assertThat(sut.combine(4, 3))
        .containsExactlyInAnyOrder(
            List.of(1, 2, 3), List.of(1, 2, 4), List.of(1, 3, 4), List.of(2, 3, 4));
  }

  // Step 9 — k = n at n = 4: only the fully-included combination.
  @Test
  void n4k4ReturnsAllElements() {
    assertThat(sut.combine(4, 4)).containsExactlyInAnyOrder(List.of(1, 2, 3, 4));
  }

  // Step 10 — deeper recursion: all ten triples from {1, 2, 3, 4, 5}.
  @Test
  void n5k3ReturnsTenTriples() {
    assertThat(sut.combine(5, 3))
        .containsExactlyInAnyOrder(
            List.of(1, 2, 3),
            List.of(1, 2, 4),
            List.of(1, 2, 5),
            List.of(1, 3, 4),
            List.of(1, 3, 5),
            List.of(1, 4, 5),
            List.of(2, 3, 4),
            List.of(2, 3, 5),
            List.of(2, 4, 5),
            List.of(3, 4, 5));
  }

  // Step 11 — invariant: the include branch appends i + 1 and recurses with
  // i + 1, so every produced combination must be strictly increasing.
  @Test
  void everyCombinationIsStrictlyIncreasing() {
    assertThat(sut.combine(5, 3)).allSatisfy(combo -> assertThat(combo).isSorted());
  }

  // Step 12 — invariant: each combination has no repeated elements. Implied
  // by strict ordering, but asserted directly so a regression that broke
  // index advancement (without breaking sort order) names this property.
  @Test
  void everyCombinationHasDistinctElements() {
    assertThat(sut.combine(5, 3)).allSatisfy(combo -> assertThat(combo).doesNotHaveDuplicates());
  }

  // Step 13 — invariant: the i + 1 advance after both branches must not let
  // recursion revisit the same multiset of indices; guards against a
  // double-counting bug across the result list.
  @Test
  void resultsContainNoDuplicateCombinations() {
    assertThat(sut.combine(5, 3)).doesNotHaveDuplicates();
  }

  // Step 14 — invariant: every emitted value lies within the closed range
  // [1, n], catching off-by-one errors at either end of the index space.
  @Test
  void everyValueIsWithinOneToN() {
    assertThat(sut.combine(5, 3))
        .allSatisfy(combo -> assertThat(combo).allMatch(v -> v >= 1 && v <= 5));
  }

  // Step 15 — invariant: every combination has exactly k elements; guards
  // against a leaf condition that admits short or overlong combinations.
  @Test
  void everyCombinationHasSizeK() {
    assertThat(sut.combine(5, 3)).allSatisfy(combo -> assertThat(combo).hasSize(3));
  }

  // Step 16 — cardinality: result count equals C(6, 3) = 20. Structural
  // sanity at a moderate scale where enumerating by hand would be tedious.
  @Test
  void n6k3CardinalityMatchesBinomialCoefficient() {
    assertThat(sut.combine(6, 3)).hasSize(20);
  }

  // Step 17 — LeetCode upper bound n = 20 with k = 1: exercises the n = 20
  // boundary without combinatorial explosion (C(20, 10) = 184,756 would be
  // excessive for a unit test).
  @Test
  void n20k1ProducesTwentyDistinctSingletons() {
    assertThat(sut.combine(20, 1))
        .hasSize(20)
        .contains(List.of(1), List.of(20))
        .doesNotHaveDuplicates();
  }

  // Step 18 — LeetCode upper bound n = k = 20: exactly one combination
  // containing every value from 1 to 20, in order.
  @Test
  void n20k20ReturnsAllTwentyElementsInOrder() {
    List<Integer> all = IntStream.rangeClosed(1, 20).boxed().toList();
    assertThat(sut.combine(20, 20)).containsExactly(all);
  }
}
