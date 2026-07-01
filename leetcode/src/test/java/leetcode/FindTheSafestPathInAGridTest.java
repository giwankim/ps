package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class FindTheSafestPathInAGridTest {
  FindTheSafestPathInAGrid sut = new FindTheSafestPathInAGrid();

  // Step 1: the smallest valid input (n = 1) is itself the lone thief, so the path's only cell
  // sits zero distance from a thief.
  @Test
  void singleCellGridWithThiefReturnsZero() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {{1}}))).isZero();
  }

  // Step 2: a thief sitting on the start cell forces safeness factor 0 no matter how far every
  // other cell is from a thief, since the path must begin there.
  @Test
  void thiefAtStartCellForcesZeroRegardlessOfRestOfGrid() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {1, 0, 0},
          {0, 0, 0},
          {0, 0, 0}
        })))
        .isZero();
  }

  // Step 3: the symmetric case - a thief on the destination cell - forces the same zero result.
  @Test
  void thiefAtDestinationCellForcesZeroRegardlessOfRestOfGrid() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0},
          {0, 0, 0},
          {0, 0, 1}
        })))
        .isZero();
  }

  // Step 4: LeetCode Example 1 - thieves occupy both the start and destination corners, so every
  // path is stuck at safeness factor 0.
  @Test
  void leetCodeExampleOneThievesAtBothEndpointsReturnsZero() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {1, 0, 0},
          {0, 0, 0},
          {0, 0, 1}
        })))
        .isZero();
  }

  // Step 5: the smallest grid with a genuine routing choice - a lone thief in the top-right corner
  // of a 2x2 lets the path drop down its left edge instead of brushing the thief, achieving the
  // smallest nonzero safeness factor of 1.
  @Test
  void twoByTwoWithLoneCornerThiefReturnsOne() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 1},
          {0, 0}
        })))
        .isEqualTo(1);
  }

  // Step 6: LeetCode Example 2 - a single thief off the direct path caps the safeness factor at 2.
  @Test
  void leetCodeExampleTwoSingleOffPathThiefReturnsTwo() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 1},
          {0, 0, 0},
          {0, 0, 0}
        })))
        .isEqualTo(2);
  }

  // Step 7: LeetCode Example 3 - two thieves near opposite corners force the path to balance
  // distance from both, still capping the safeness factor at 2.
  @Test
  void leetCodeExampleThreeTwoOppositeThievesReturnsTwo() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0, 1},
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {1, 0, 0, 0}
        })))
        .isEqualTo(2);
  }

  // Step 8: a lone thief dead-center of a 5x5 grid forms a diamond of near cells that touches all
  // four edges, so any path is forced to cross it at distance 2 - the max achievable, rather than
  // the larger distance a naive corner-to-corner glance might suggest.
  @Test
  void centeredThiefFormsDiamondBarrierCappingSafenessFactorAtTwo() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0},
          {0, 0, 1, 0, 0},
          {0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0}
        })))
        .isEqualTo(2);
  }

  // Step 9: a lone thief tucked in a corner off both the start and destination lets a path hug
  // the opposite corner, achieving a higher safeness factor of 3.
  @Test
  void singleCornerThiefOffPathAllowsSafenessFactorThree() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0, 1},
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0}
        })))
        .isEqualTo(3);
  }

  // Step 10: thieves at the two extreme opposite corners of a larger 5x5 grid scale the achievable
  // safeness factor up to 3, confirming the result grows with the grid rather than staying pinned
  // to the 2 seen in the smaller examples.
  @Test
  void twoExtremeCornerThievesOnLargerGridScalesSafenessFactorToThree() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0, 0, 1},
          {0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0},
          {1, 0, 0, 0, 0}
        })))
        .isEqualTo(3);
  }

  // Step 11: a lone thief in the top-right corner of a 7x7 grid leaves both endpoints six steps
  // away, and a route can hug the far bottom-left corner (up to distance 12) without ever dropping
  // below that, so the safeness factor scales to 6 - proof the search explores large thresholds and
  // never silently caps at the 2s and 3s of the smaller grids.
  @Test
  void singleCornerThiefOnSevenGridScalesSafenessFactorToSix() {
    assertThat(sut.maximumSafenessFactor(grid(new int[][] {
          {0, 0, 0, 0, 0, 0, 1},
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0},
          {0, 0, 0, 0, 0, 0, 0}
        })))
        .isEqualTo(6);
  }

  /** Builds a grid from row arrays so tests can pass int literals instead of list boilerplate. */
  private static List<List<Integer>> grid(int[][] rows) {
    return Arrays.stream(rows).map(row -> Arrays.stream(row).boxed().toList()).toList();
  }
}
