package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindASafeWalkThroughAGridTest {
  FindASafeWalkThroughAGrid sut = new FindASafeWalkThroughAGrid();

  // Step 1: smallest valid input (2 <= m * n) — an all-safe 1x2 corridor costs no health,
  // so the minimum starting health of 1 reaches the end intact
  @Test
  void allSafeTwoCellGridSucceedsWithMinimumHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 0)), 1)).isTrue();
  }

  // Step 2: the destination cell itself deals damage — health 1 drops to 0 on arrival,
  // violating the "health value of 1 or more" requirement
  @Test
  void unsafeDestinationKillsWalkerWithMinimumHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 1)), 1)).isFalse();
  }

  // Step 3: one spare health point absorbs the destination damage
  @Test
  void unsafeDestinationSurvivedWithSpareHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 1)), 2)).isTrue();
  }

  // Step 4: the starting cell (0, 0) also deals damage before any move is made
  @Test
  void unsafeStartKillsWalkerWithMinimumHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(1, 0)), 1)).isFalse();
  }

  // Step 5: the start damage is survivable with one spare health point
  @Test
  void unsafeStartSurvivedWithSpareHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(1, 0)), 2)).isTrue();
  }

  // Step 6: the first real route choice — going down then right dodges the unsafe cell,
  // so health 1 survives even though the greedy right-first route would not
  @Test
  void detourAroundTheOnlyUnsafeCellCostsNothing() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 1), List.of(0, 0)), 1)).isTrue();
  }

  // Step 7: damage is unavoidable when every route crosses an unsafe cell — both 2x2
  // diagonals hold a 1, so health 1 is exhausted on any walk
  @Test
  void everyRouteCrossesOneUnsafeCell() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 1), List.of(1, 0)), 1)).isFalse();
  }

  // Step 8: the same unavoidable hit is survivable with one spare health point
  @Test
  void unavoidableHitSurvivedWithSpareHealth() {
    assertThat(sut.findSafeWalk(List.of(List.of(0, 1), List.of(1, 0)), 2)).isTrue();
  }

  // Step 9: a single column forces the walk through every cell — the middle 1 costs
  // one point, so health 2 arrives with exactly 1 remaining
  @Test
  void singleColumnWalksThroughEveryCell() {
    assertThat(sut.findSafeWalk(List.of(List.of(0), List.of(1), List.of(0)), 2)).isTrue();
  }

  // Step 10: movement is 4-directional, not monotone. The only damage-free walk snakes
  // right along row 0, down to row 2, LEFT back to column 0, then down and right again.
  // Any right/down-only path pays at least one health point and a monotone DP answers false
  @Test
  void zigzagWalkRequiresMovingLeft() {
    List<List<Integer>> grid = List.of(
        List.of(0, 0, 0), List.of(1, 1, 0), List.of(0, 0, 0), List.of(0, 1, 1), List.of(0, 0, 0));
    assertThat(sut.findSafeWalk(grid, 1)).isTrue();
  }

  // Step 11: LeetCode Example 1 — a damage-free walk exists, so the minimum health of 1 survives
  @Test
  void leetCodeExample1() {
    List<List<Integer>> grid =
        List.of(List.of(0, 1, 0, 0, 0), List.of(0, 1, 0, 1, 0), List.of(0, 0, 0, 1, 0));
    assertThat(sut.findSafeWalk(grid, 1)).isTrue();
  }

  // Step 12: LeetCode Example 2 — a minimum of 4 health points is needed, so 3 is not enough
  @Test
  void leetCodeExample2() {
    List<List<Integer>> grid = List.of(
        List.of(0, 1, 1, 0, 0, 0),
        List.of(1, 0, 1, 0, 0, 0),
        List.of(0, 1, 1, 1, 0, 1),
        List.of(0, 0, 1, 0, 1, 0));
    assertThat(sut.findSafeWalk(grid, 3)).isFalse();
  }

  // Step 13: Example 2's explanation states 4 health points suffice — the boundary flips to true
  @Test
  void leetCodeExample2SucceedsWithFourHealth() {
    List<List<Integer>> grid = List.of(
        List.of(0, 1, 1, 0, 0, 0),
        List.of(1, 0, 1, 0, 0, 0),
        List.of(0, 1, 1, 1, 0, 1),
        List.of(0, 0, 1, 0, 1, 0));
    assertThat(sut.findSafeWalk(grid, 4)).isTrue();
  }

  // Step 14: LeetCode Example 3 — the walk must detour through the safe center cell at
  // row 1, column 1, because every path around it crosses 5 unsafe cells and ends at 0 health
  @Test
  void leetCodeExample3() {
    List<List<Integer>> grid = List.of(List.of(1, 1, 1), List.of(1, 0, 1), List.of(1, 1, 1));
    assertThat(sut.findSafeWalk(grid, 5)).isTrue();
  }

  // Step 15: the cheapest walk in Example 3 still crosses 4 unsafe cells (both corners plus
  // two ring cells around the center), so health 4 ends the walk at 0
  @Test
  void leetCodeExample3FailsWithFourHealth() {
    List<List<Integer>> grid = List.of(List.of(1, 1, 1), List.of(1, 0, 1), List.of(1, 1, 1));
    assertThat(sut.findSafeWalk(grid, 4)).isFalse();
  }

  // Step 16: a mostly unsafe 5x3 grid — both cheapest walks, down the safe left column or
  // across the middle safe cells, cross exactly 5 unsafe cells, so health 6 arrives with 1 left
  @Test
  void cheapestWalkCrossesFiveUnsafeCells() {
    List<List<Integer>> grid = List.of(
        List.of(1, 1, 1), List.of(1, 0, 0), List.of(0, 1, 1), List.of(0, 1, 1), List.of(1, 1, 1));
    assertThat(sut.findSafeWalk(grid, 6)).isTrue();
  }

  // Step 17: upper constraint bound — a 50x50 all-safe grid needs only the minimum health of 1
  @Test
  void maximumAllSafeGridSucceedsWithMinimumHealth() {
    assertThat(sut.findSafeWalk(maxGrid(0), 1)).isTrue();
  }

  // Step 18: worst case — the shortest walk across a 50x50 all-unsafe grid visits 99 cells,
  // every one dealing damage, so the maximum allowed health of 100 arrives with exactly 1 left
  @Test
  void maximumAllUnsafeGridSucceedsWithMaximumHealth() {
    assertThat(sut.findSafeWalk(maxGrid(1), 100)).isTrue();
  }

  // Step 19: one point below the worst-case requirement ends the walk at 0 health
  @Test
  void maximumAllUnsafeGridFailsJustBelowMaximumHealth() {
    assertThat(sut.findSafeWalk(maxGrid(1), 99)).isFalse();
  }

  private static List<List<Integer>> maxGrid(int value) {
    return Collections.nCopies(50, Collections.nCopies(50, value));
  }
}
