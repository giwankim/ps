package leetcode.p3501_3600;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumMovesToCleanTheClassroomTest {
  MinimumMovesToCleanTheClassroom sut = new MinimumMovesToCleanTheClassroom();

  // ===========================================================================================
  // Reading the grid and moving (Steps 1-5).
  // ===========================================================================================

  // Step 1: the smallest grid the constraints permit is 1 x 1, and since exactly one cell is 'S',
  //         that grid holds nothing else. "At most 10" litter cells includes zero of them, so the
  //         student is already done and the answer is 0, not -1
  @Test
  void singleCellRoomHoldsNoLitterAndNeedsNoMoves() {
    assertThat(sut.minMoves(new String[] {"S"}, 1)).isZero();
  }

  // Step 2: a larger room with no litter is equally already clean. A solution that goes searching
  //         for a litter cell to aim at, or that returns -1 when it finds none, fails here
  @Test
  void roomWithoutLitterNeedsNoMovesRegardlessOfSize() {
    assertThat(sut.minMoves(new String[] {"S..", "..."}, 1)).isZero();
  }

  // Step 3: the smallest positive answer — one move onto an adjacent litter cell, with the minimal
  //         energy of 1 exactly covering it
  @Test
  void adjacentLitterTakesOneMove() {
    assertThat(sut.minMoves(new String[] {"SL"}, 1)).isEqualTo(1);
  }

  // Step 4: moves are up, down, left, and right only. The litter is one diagonal step away, which
  //         is two orthogonal moves; a solution that allows diagonal steps answers 1
  @Test
  void diagonalStepsAreNotMoves() {
    assertThat(sut.minMoves(new String[] {"S.", ".L"}, 2)).isEqualTo(2);
  }

  // Step 5: the litter sits at distance 3 with energy exactly 3, so the student arrives with 0
  //         energy remaining. Collection happens on arrival and costs nothing further: a solution
  //         that requires energy to stay positive after the final move answers -1
  @Test
  void spendingTheLastEnergyUnitOnTheLitterCellStillCollects() {
    assertThat(sut.minMoves(new String[] {"S..L"}, 3)).isEqualTo(3);
  }

  // ===========================================================================================
  // The energy budget (Steps 6-8).
  // ===========================================================================================

  // Step 6: the same corridor one energy unit short. There is no reset area, so the student
  //         strands at distance 2 and the answer is -1. A solution that ignores energy entirely
  //         answers 3
  @Test
  void litterBeyondTheEnergyBudgetIsUnreachable() {
    assertThat(sut.minMoves(new String[] {"S..L"}, 2)).isEqualTo(-1);
  }

  // Step 7: an obstacle blocks the direct step, forcing a detour of 4 moves around the lower row
  //         where the straight-line distance is 2. A solution that measures Manhattan distance, or
  //         walks through 'X', answers 2
  @Test
  void obstacleForcesADetourLongerThanTheStraightLine() {
    assertThat(sut.minMoves(new String[] {"SXL", "..."}, 4)).isEqualTo(4);
  }

  // Step 8: with no second row, the obstacle is a wall and no amount of energy helps — even the
  //         constraint maximum of 50. Unreachable litter means -1, not the distance to it
  @Test
  void obstacleWallMakesLitterUnreachableAtAnyEnergy() {
    assertThat(sut.minMoves(new String[] {"SXL"}, 50)).isEqualTo(-1);
  }

  // ===========================================================================================
  // Reset areas (Steps 9-14).
  // ===========================================================================================

  // Step 9: the statement's "if the energy reaches 0" sentence. With energy 3 the student lands on
  //         the reset area with 0 energy left, and the reset fires anyway, funding the second
  //         3-move leg. A solution that discards a state the moment energy hits 0, before checking
  //         whether the cell is 'R', answers -1
  @Test
  void landingOnResetWithTheLastEnergyUnitStillResets() {
    assertThat(sut.minMoves(new String[] {"S..R..L"}, 3)).isEqualTo(6);
  }

  // Step 10: the same corridor with energy 4 — the student reaches 'R' with 1 unit left, and the
  //          reset fires "regardless of their current energy level". A solution that only resets
  //          when energy is exactly 0 continues with 1 unit, strands one cell past the reset area,
  //          and answers -1
  @Test
  void resetFiresOnAnyArrivalNotOnlyAtZeroEnergy() {
    assertThat(sut.minMoves(new String[] {"S..R..L"}, 4)).isEqualTo(6);
  }

  // Step 11: with energy 6 the corridor is affordable outright, and the reset area on the way is
  //          simply walked over — it costs no extra move and is not an obstacle. A solution that
  //          refuses to step on 'R' answers -1, since it is the only path
  @Test
  void resetAreaIsWalkableAndCostsNothingWhenNotNeeded() {
    assertThat(sut.minMoves(new String[] {"S..R..L"}, 6)).isEqualTo(6);
  }

  // Step 12: the energy floor of the constraints. With capacity 1, every single move must end on a
  //          reset area or on the final litter cell, and the row of consecutive 'R' cells is
  //          crossed one refill at a time for 3 moves
  @Test
  void capacityOfOneCrossesARowOfConsecutiveResetAreas() {
    assertThat(sut.minMoves(new String[] {"SRRL"}, 1)).isEqualTo(3);
  }

  // Step 13: a reset restores energy to capacity — it does not add a tank. After the two reset
  //          cells the student holds 2 units, not 4, and the litter 3 cells further is out of
  //          reach: the answer is -1. A solution that accumulates energy answers 5
  @Test
  void resetRestoresToCapacityRatherThanAccumulating() {
    assertThat(sut.minMoves(new String[] {"SRR..L"}, 2)).isEqualTo(-1);
  }

  // Step 14: the statement says a reset area "can be used multiple times". The only 7-move route
  //          passes through the single 'R' at move 1, walks out to the left litter, and comes back
  //          through the same 'R' at move 5 before crossing to the right litter. A solution that
  //          lets each reset area fire only once answers -1
  @Test
  void sameResetAreaCanBeUsedTwice() {
    assertThat(sut.minMoves(new String[] {"L.R.L", "..S.."}, 4)).isEqualTo(7);
  }

  // ===========================================================================================
  // Revisits and collection order (Steps 15-18).
  // ===========================================================================================

  // Step 15: litter on both sides of the start means one side is collected first and the start
  //          cell is walked a second time on the way back. A BFS that marks bare cells visited,
  //          without the collected-litter set in the state, can never re-cross 'S' and answers -1
  @Test
  void walkingBackOverTheStartCollectsBothSides() {
    assertThat(sut.minMoves(new String[] {"LSL"}, 3)).isEqualTo(3);
  }

  // Step 16: two litter cells on one line — the nearer one is collected in passing on the way to
  //          the farther one, so the answer is the 4 moves to the far cell. A solution that sums
  //          the individual distances from 'S' to each litter cell answers 6
  @Test
  void litterOnTheWayIsCollectedInPassing() {
    assertThat(sut.minMoves(new String[] {"S.L.L"}, 4)).isEqualTo(4);
  }

  // Step 17: litter at distance 2 to the left and distance 3 to the right, so an order must be
  //          chosen. Left first costs 2 + 5 = 7; right first costs 3 + 5 = 8. A solution that
  //          commits to the farther side first answers 8, and summing the two one-way distances
  //          answers 5
  @Test
  void nearerEndFirstBeatsFartherEndFirst() {
    assertThat(sut.minMoves(new String[] {"L.S..L"}, 8)).isEqualTo(7);
  }

  // Step 18: the state-space trap the whole problem turns on. The direct top-row walk reaches the
  //          corridor junction in 2 moves but with only 2 energy for the 3 remaining cells; the
  //          detour through the reset area arrives 2 moves later with 3 energy, and is the only
  //          arrival that can finish, for 7 moves in all. A search that keeps just the first,
  //          fewest-moves arrival per cell and litter set discards the slower but richer state and
  //          answers -1: at a tie in position and litter, more remaining energy must survive
  @Test
  void slowerArrivalWithMoreEnergyMustSurvive() {
    assertThat(sut.minMoves(new String[] {"S....L", "..RXXX"}, 4)).isEqualTo(7);
  }

  // ===========================================================================================
  // The official examples, verbatim (Steps 19-21).
  // ===========================================================================================

  // Step 19: the obstacle removes the downward step, leaving exactly one route — right, then down
  //          onto the litter — whose 2 moves consume the energy of 2 exactly
  @Test
  void leetCodeExample1() {
    assertThat(sut.minMoves(new String[] {"S.", "XL"}, 2)).isEqualTo(2);
  }

  // Step 20: the explanation's sequence collects the first litter, resets at 'R' while still
  //          holding 2 units, and finishes at the second litter in 3 moves. The mirror route —
  //          down, up, left — also does it in 3 without the reset area, so both orders agree. A
  //          bare visited-per-cell BFS answers -1, since either route re-crosses nothing but needs
  //          the litter set in its state to know it is not done
  @Test
  void leetCodeExample2() {
    assertThat(sut.minMoves(new String[] {"LS", "RL"}, 4)).isEqualTo(3);
  }

  // Step 21: both litter cells are reachable in graph terms — a connectivity-only solution
  //          answers 4 — but no ordering of the two survives the energy budget: whichever litter
  //          is collected first, the walk onward strands at 0 energy off the reset area. Energy,
  //          not reachability, is what fails
  @Test
  void leetCodeExample3() {
    assertThat(sut.minMoves(new String[] {"L.S", "RXL"}, 3)).isEqualTo(-1);
  }

  // ===========================================================================================
  // Upper end of the constraints (Steps 22-23): 20 x 20 cells, 10 litter, energy up to 50.
  //
  // The state space is position x litter subset x energy: 400 * 2^10 * 51, about 21 million
  // states, each visited at most once by the intended BFS, which finishes well under a second. A
  // search that enumerates collection orders — 10! permutations with a pathfinding run per leg —
  // or that re-explores states instead of pruning dominated ones does billions of operations and
  // cannot finish inside the timeout. The timeouts run on a separate thread because JUnit's
  // default mode only measures after the method returns, so a genuinely too-slow solution would
  // hang the build instead of failing it.
  // ===========================================================================================

  // Step 22: a fully open 20 x 20 room, litter scattered into all four quadrants, and a column of
  //          reset areas down the middle that keeps every leg of the tour affordable at energy 50.
  //          The shortest tour over all ten litter cells takes 78 moves
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void fullSizeOpenRoomIsCleanedInSeventyEightMoves() {
    assertThat(sut.minMoves(openRoomWithResetColumn(), 50)).isEqualTo(78);
  }

  // Step 23: a serpentine corridor — every odd row is a wall of obstacles with a single reset-area
  //          gate at alternating ends, litter in the middle of each open row. The forced snake
  //          walk is 198 moves: nine full 19-move rows, nine 2-move gate crossings, and 9 moves
  //          into the last row. Energy 50 covers each leg between gates but nowhere near the whole
  //          walk, so every reset is mandatory
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void snakeMazeForcesResetsAcrossAllTwentyRows() {
    assertThat(sut.minMoves(snakeMaze(), 50)).isEqualTo(198);
  }

  // ===========================================================================================
  // Hygiene (Steps 24-25).
  // ===========================================================================================

  // Step 24: the grid is the caller's, and this one holds all five cell types. Solutions that copy
  //          into a char matrix and then write progress back over the array's rows — or clear
  //          collected litter in place — fail here; the array must hold its original rows
  @Test
  void inputArrayIsNotModified() {
    String[] classroom = {"S....L", "..RXXX"};
    String[] original = classroom.clone();

    sut.minMoves(classroom, 4);

    assertThat(classroom).containsExactly(original);
  }

  // Step 25: several rooms answered by one instance, the largest in the middle and the sizes
  //          deliberately out of order. A solution that keeps the visited set, the litter count,
  //          or the best-energy table on the instance instead of resetting it per call answers the
  //          later rooms from the earlier ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersRoomsOfAnySizeInAnyOrder() {
    assertThat(sut.minMoves(new String[] {"SL"}, 1)).isEqualTo(1);
    assertThat(sut.minMoves(snakeMaze(), 50)).isEqualTo(198);
    assertThat(sut.minMoves(new String[] {"S"}, 1)).isZero();
    assertThat(sut.minMoves(new String[] {"S....L", "..RXXX"}, 4)).isEqualTo(7);
    assertThat(sut.minMoves(new String[] {"L.S", "RXL"}, 3)).isEqualTo(-1);
  }

  private static String[] openRoomWithResetColumn() {
    char[][] g = new char[20][20];
    for (char[] row : g) {
      Arrays.fill(row, '.');
    }
    for (int i = 0; i < 20; i++) {
      g[i][10] = 'R';
    }
    g[0][0] = 'S';
    int[][] litter = {
      {1, 3}, {3, 17}, {5, 6}, {7, 14}, {9, 2}, {11, 18}, {13, 7}, {15, 15}, {17, 4}, {19, 19}
    };
    for (int[] cell : litter) {
      g[cell[0]][cell[1]] = 'L';
    }
    return rows(g);
  }

  private static String[] snakeMaze() {
    char[][] g = new char[20][20];
    for (char[] row : g) {
      Arrays.fill(row, '.');
    }
    for (int i = 1; i < 20; i += 2) {
      Arrays.fill(g[i], 'X');
      g[i][i % 4 == 1 ? 19 : 0] = 'R';
    }
    g[0][0] = 'S';
    g[0][19] = 'L';
    for (int i = 2; i < 20; i += 2) {
      g[i][10] = 'L';
    }
    return rows(g);
  }

  private static String[] rows(char[][] g) {
    return Arrays.stream(g).map(String::new).toArray(String[]::new);
  }
}
