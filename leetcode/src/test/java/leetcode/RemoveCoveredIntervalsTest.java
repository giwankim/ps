package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RemoveCoveredIntervalsTest {
  RemoveCoveredIntervals sut = new RemoveCoveredIntervals();

  // Step 1: smallest valid input — a single interval has nothing that can cover it
  @Test
  void singleIntervalReturnsOne() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 4}})).isEqualTo(1);
  }

  // Step 2: disjoint intervals never cover each other
  @Test
  void twoDisjointIntervalsBothRemain() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 2}, {3, 4}})).isEqualTo(2);
  }

  // Step 3: partial overlap is not coverage — [c,d) must contain both endpoints
  @Test
  void partialOverlapKeepsBothIntervals() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 4}, {2, 6}})).isEqualTo(2);
  }

  // Step 4: strict containment removes the inner interval (LeetCode Example 2)
  @Test
  void strictlyNestedIntervalIsRemoved() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 4}, {2, 3}})).isEqualTo(1);
  }

  // Step 5: coverage allows equality on the left edge — c <= a holds with c == a
  @Test
  void sharedStartShorterIntervalIsCovered() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 3}, {1, 5}})).isEqualTo(1);
  }

  // Step 6: coverage allows equality on the right edge — b <= d holds with b == d
  @Test
  void sharedEndShorterIntervalIsCovered() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{2, 5}, {1, 5}})).isEqualTo(1);
  }

  // Step 7: [3,6] falls to [2,8] but [1,4] pokes out on the left (LeetCode Example 1)
  @Test
  void onlyFullyCoveredIntervalIsRemoved() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 4}, {3, 6}, {2, 8}})).isEqualTo(2);
  }

  // Step 8: input order is irrelevant — Example 1 shuffled gives the same count
  @Test
  void coverageIsIndependentOfInputOrder() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{3, 6}, {2, 8}, {1, 4}})).isEqualTo(2);
  }

  // Step 9: one wide interval can cover several disjoint ones at once
  @Test
  void oneIntervalCoversMultipleOthers() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 10}, {2, 3}, {4, 5}, {6, 7}}))
        .isEqualTo(1);
  }

  // Step 10: a fully nested chain collapses to the outermost interval
  @Test
  void nestedChainLeavesOnlyOutermost() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{4, 7}, {3, 8}, {2, 9}, {1, 10}}))
        .isEqualTo(1);
  }

  // Step 11: staircase overlaps — every interval pokes out on one side, none removed
  @Test
  void staircaseOverlapsKeepAllIntervals() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 4}, {2, 5}, {3, 6}})).isEqualTo(3);
  }

  // Step 12: same-start tiebreak trap — [1,4) covers both [1,2) and [3,4); a sort that
  // ties by end ascending keeps [1,2) before its coverer is seen and answers 2
  @Test
  void sameStartTiebreakStillDetectsAllCoverage() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{1, 2}, {1, 4}, {3, 4}})).isEqualTo(1);
  }

  // Step 13: constraint boundaries 0 <= l < r <= 10^5 — the full-range interval covers all
  @Test
  void handlesBoundaryValues() {
    assertThat(sut.removeCoveredIntervals(new int[][] {{0, 100000}, {0, 1}, {99999, 100000}}))
        .isEqualTo(1);
  }
}
