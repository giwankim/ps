package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MinimumScoreOfAPathBetweenTwoCitiesTest {
  MinimumScoreOfAPathBetweenTwoCities sut = new MinimumScoreOfAPathBetweenTwoCities();

  // Step 1: smallest valid input (n = 2) — the only path is the single road, so the
  // score is that road's distance
  @Test
  void singleRoadBetweenTheTwoCitiesReturnsItsDistance() {
    int[][] roads = {{1, 2, 5}};
    assertThat(sut.minScore(2, roads)).isEqualTo(5);
    assertThat(sut.minScore2(2, roads)).isEqualTo(5);
  }

  // Step 2: a forced chain 1 - 2 - 3 — the score of the only path is its minimum road
  @Test
  void chainPathScoresItsCheapestRoad() {
    int[][] roads = {{1, 2, 3}, {2, 3, 7}};
    assertThat(sut.minScore(3, roads)).isEqualTo(3);
    assertThat(sut.minScore2(3, roads)).isEqualTo(3);
  }

  // Step 3: two alternative routes — the direct road 1 - 3 costs 5, but routing through
  // city 2 picks up the cheaper road of distance 4
  @Test
  void choosesTheRouteContainingTheCheapestRoad() {
    int[][] roads = {{1, 2, 4}, {2, 3, 6}, {1, 3, 5}};
    assertThat(sut.minScore(3, roads)).isEqualTo(4);
    assertThat(sut.minScore2(3, roads)).isEqualTo(4);
  }

  // Step 4: roads can be revisited, so a dead-end branch counts — detour
  // 1 -> 3 -> 2 -> 3 -> 4 picks up the distance-1 road even though city 2 leads nowhere
  @Test
  void deadEndBranchRoadCountsBecauseRoadsCanBeRevisited() {
    int[][] roads = {{1, 3, 5}, {3, 4, 6}, {3, 2, 1}};
    assertThat(sut.minScore(4, roads)).isEqualTo(1);
    assertThat(sut.minScore2(4, roads)).isEqualTo(1);
  }

  // Step 5: city n can be revisited too — after reaching city 4, backtracking
  // 1 -> 4 -> 3 -> 4 picks up the distance-2 road on the far side of n
  @Test
  void roadBeyondCityNCountsViaBacktracking() {
    int[][] roads = {{1, 4, 9}, {4, 3, 2}, {2, 3, 5}};
    assertThat(sut.minScore(4, roads)).isEqualTo(2);
    assertThat(sut.minScore2(4, roads)).isEqualTo(2);
  }

  // Step 6: the graph need not be connected — the distance-1 and distance-2 roads live in
  // an unreachable component {3, 4, 5}, so the score comes from the component of 1 and n
  @Test
  void unreachableComponentRoadsDoNotLowerTheScore() {
    int[][] roads = {{1, 2, 10}, {2, 6, 8}, {3, 4, 1}, {4, 5, 2}};
    assertThat(sut.minScore(6, roads)).isEqualTo(8);
    assertThat(sut.minScore2(6, roads)).isEqualTo(8);
  }

  // Step 7: LeetCode Example 1 — the path 1 -> 2 -> 4 scores min(9, 5) = 5
  @Test
  void leetCodeExample1() {
    int[][] roads = {{1, 2, 9}, {2, 3, 6}, {2, 4, 5}, {1, 4, 7}};
    assertThat(sut.minScore(4, roads)).isEqualTo(5);
    assertThat(sut.minScore2(4, roads)).isEqualTo(5);
  }

  // Step 8: LeetCode Example 2 — the path 1 -> 2 -> 1 -> 3 -> 4 scores min(2, 2, 4, 7) = 2
  @Test
  void leetCodeExample2() {
    int[][] roads = {{1, 2, 2}, {1, 3, 4}, {3, 4, 7}};
    assertThat(sut.minScore(4, roads)).isEqualTo(2);
    assertThat(sut.minScore2(4, roads)).isEqualTo(2);
  }

  // Step 9: constraint bounds — a chain of 100_000 cities (99_999 roads) at the maximum
  // distance 10_000, with a single distance-1 road in the middle; must handle the deepest
  // possible graph without stack overflow or timeout (the recursive DFS relies on the
  // judge-like -Xss the test JVM gets from ps.test-conventions)
  @Test
  void maximumChainOfCitiesFindsTheOneCheapRoad() {
    int n = 100_000;
    int[][] roads = new int[n - 1][];
    for (int i = 1; i < n; i++) {
      roads[i - 1] = new int[] {i, i + 1, 10_000};
    }
    roads[n / 2][2] = 1;
    assertThat(sut.minScore(n, roads)).isEqualTo(1);
    assertThat(sut.minScore2(n, roads)).isEqualTo(1);
  }
}
