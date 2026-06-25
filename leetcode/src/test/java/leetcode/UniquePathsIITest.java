package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UniquePathsIITest {
  UniquePathsII sut = new UniquePathsII();

  // Step 1: a clear 1x1 grid — the robot already stands on the destination, so there is one path
  // (the empty path). This pins the DP base case dp[0][0] = 1.
  @Test
  void singleCellWithoutObstacleHasOnePath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0}})).isEqualTo(1);
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0}})).isEqualTo(1);
  }

  // Step 2: a 1x1 grid whose only cell is an obstacle — the start is also the goal and it is
  // blocked, so there are no paths. Forces dp[0][0] = grid[0][0] == 1 ? 0 : 1.
  @Test
  void singleCellThatIsObstacleHasNoPath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{1}})).isZero();
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{1}})).isZero();
  }

  // Step 3: a single open row — the robot can only walk right, so exactly one path exists. Forces
  // first-row propagation dp[0][j] = dp[0][j - 1].
  @Test
  void singleRowWithoutObstaclesHasOnePath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0, 0, 0}})).isEqualTo(1);
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0, 0, 0}})).isEqualTo(1);
  }

  // Step 4: a single open column — the robot can only walk down, so exactly one path exists. Forces
  // the symmetric first-column propagation dp[i][0] = dp[i - 1][0].
  @Test
  void singleColumnWithoutObstaclesHasOnePath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0}, {0}, {0}})).isEqualTo(1);
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0}, {0}, {0}})).isEqualTo(1);
  }

  // Step 5: an obstacle walls off the only corridor — the cell past it must inherit 0, not a
  // phantom 1. This is the classic first-row bug: the zero has to propagate rightward.
  @Test
  void obstacleSeveringSingleRowHasNoPath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0, 1, 0}})).isZero();
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0, 1, 0}})).isZero();
  }

  // Step 6: the smallest grid that actually branches — right-then-down and down-then-right both
  // reach the goal. Forces the general recurrence dp[i][j] = dp[i - 1][j] + dp[i][j - 1].
  @Test
  void twoByTwoWithoutObstaclesHasTwoPaths() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0, 0}, {0, 0}})).isEqualTo(2);
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0, 0}, {0, 0}})).isEqualTo(2);
  }

  // Step 7: LeetCode Example 2 — the obstacle at (0,1) kills the right-then-down route, leaving
  // only the down-then-right detour.
  @Test
  void obstacleInFirstRowForcesDownThenRight() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0, 1}, {0, 0}})).isEqualTo(1);
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0, 1}, {0, 0}})).isEqualTo(1);
  }

  // Step 8: the origin itself is blocked inside a larger grid — every path is dead before it
  // starts. Reinforces that a blocked start short-circuits to 0 regardless of grid size.
  @Test
  void blockedStartHasNoPath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{1, 0}, {0, 0}})).isZero();
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{1, 0}, {0, 0}})).isZero();
  }

  // Step 9: the destination is blocked — the DP must read the obstacle flag at the final cell, not
  // assume the bottom-right is always reachable.
  @Test
  void blockedDestinationHasNoPath() {
    assertThat(sut.uniquePathsWithObstacles(new int[][] {{0, 0}, {0, 1}})).isZero();
    assertThat(sut.uniquePathsWithObstacles2(new int[][] {{0, 0}, {0, 1}})).isZero();
  }

  // Step 10: an obstacle in the first row zeroes the top route to (0,2), but a path still exists
  // through the second row — so the answer is 1, not 0. Distinguishes "this cell is unreachable
  // from the top" from "the whole grid is blocked".
  @Test
  void firstRowObstacleStillAllowsLowerPath() {
    int[][] grid = {
      {0, 1, 0},
      {0, 0, 0},
    };
    assertThat(sut.uniquePathsWithObstacles(grid)).isEqualTo(1);
    assertThat(sut.uniquePathsWithObstacles2(grid)).isEqualTo(1);
  }

  // Step 11: LeetCode Example 1 — the center obstacle removes the routes through the middle,
  // cutting the open 3x3's six paths down to two.
  @Test
  void centerObstacleInThreeByThreeHasTwoPaths() {
    int[][] grid = {
      {0, 0, 0},
      {0, 1, 0},
      {0, 0, 0},
    };
    assertThat(sut.uniquePathsWithObstacles(grid)).isEqualTo(2);
    assertThat(sut.uniquePathsWithObstacles2(grid)).isEqualTo(2);
  }

  // Step 12: a fully open 3x3 reduces to Unique Paths I — C(4,2) = 6 paths — a sanity check that no
  // obstacles means no behavior change from the obstacle-free problem.
  @Test
  void threeByThreeWithoutObstaclesHasSixPaths() {
    int[][] grid = {
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0},
    };
    assertThat(sut.uniquePathsWithObstacles(grid)).isEqualTo(6);
    assertThat(sut.uniquePathsWithObstacles2(grid)).isEqualTo(6);
  }
}
