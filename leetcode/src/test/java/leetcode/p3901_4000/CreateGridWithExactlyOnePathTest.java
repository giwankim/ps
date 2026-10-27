package leetcode.p3901_4000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CreateGridWithExactlyOnePathTest {
  CreateGridWithExactlyOnePath sut = new CreateGridWithExactlyOnePath();

  // ===========================================================================================
  // A single row or column leaves no choice (Steps 1-3). With m = 1 or n = 1 there is only one
  // right/down walk, so any '#' cuts it and the all-free grid is the only answer. These are the
  // only shapes where the spec can pin the exact strings.
  // ===========================================================================================

  // Step 1: the floor — 1 <= m, n allows a 1 x 1 grid. Its single cell is both the start and the
  //         end, and the one-cell walk is exactly one path, so the only answer is ".". A walk loop
  //         that marks a cell and then moves, stopping once it arrives, never marks the end cell
  //         and answers ["#"] with zero paths
  @Test
  void m1n1ReturnsTheSingleFreeCell() {
    assertThat(sut.createGrid(1, 1)).containsExactly(".");
  }

  // Step 2: a single row. m counts rows, so m = 1 is one string of n cells, and the only walk is
  //         the row itself, so both cells are forced free. A solution that swaps m and n returns
  //         two strings of one cell each
  @Test
  void m1n2ReturnsOneRowOfFreeCells() {
    assertThat(sut.createGrid(1, 2)).containsExactly("..");
  }

  // Step 3: the mirror of Step 2 — a single column, two strings of one cell, both forced free. A
  //         solution that swaps m and n returns [".."], and the diagonal staircase Examples 1 and 2
  //         print (row i frees columns i and i + 1) answers [".", "#"] with zero paths
  @Test
  void m2n1ReturnsOneColumnOfFreeCells() {
    assertThat(sut.createGrid(2, 1)).containsExactly(".", ".");
  }

  // ===========================================================================================
  // Two sides of at least 2: a route must be chosen and every other route cut (Steps 4-6). From
  // here on several grids are valid, so assertExactlyOnePath checks the contract instead of a
  // fixed picture.
  // ===========================================================================================

  // Step 4: the smallest grid with a choice. The walk passes either row 0, column 1 or row 1,
  //         column 0, and exactly one of those two cells must be '#' — the only two valid answers
  //         are ["..", "#."] and [".#", ".."]. The all-free grid and the border ring both leave 2
  //         paths, and blocking every cell but the two corners leaves 0. A row built from
  //         new char[n] that only writes the path cells leaves '\0' where the '#' belongs
  @Test
  void m2n2BlocksExactlyOneOfTheTwoMiddleCells() {
    assertExactlyOnePath(sut.createGrid(2, 2), 2, 2);
  }

  // Step 5: a wide grid. Once the route reaches the last row it has to keep running right to the
  //         corner. The examples' staircase answers ["..##", "#..#"], leaving the end blocked with
  //         zero paths. One char[] buffer reused for every row without re-filling '#' carries row
  //         0's free cells down into row 1 and answers the all-free grid, 4 paths
  @Test
  void m2n4RouteRunsRightAlongTheLastRow() {
    assertExactlyOnePath(sut.createGrid(2, 4), 2, 4);
  }

  // Step 6: the mirror of Step 5 — a tall grid. Once the route reaches the last column it has to
  //         keep running down. The staircase answers ["..", "#.", "##", "##"] with zero paths, and
  //         a solution that swaps m and n returns two rows of four cells
  @Test
  void m4n2RouteRunsDownTheLastColumn() {
    assertExactlyOnePath(sut.createGrid(4, 2), 4, 2);
  }

  // ===========================================================================================
  // The official examples (Steps 7-9).
  // ===========================================================================================

  // Step 7: the printed ["..#", "#.."] is one of 5 valid 2 x 3 grids, and the judge accepts any of
  //         them — Hint 3's right-then-down route ["...", "##."] is just as correct. The reading
  //         this rules out is that the printed picture is required, so the spec checks the path
  //         count, not the strings. The all-free grid leaves 3 paths
  @Test
  void leetCodeExample1() {
    assertExactlyOnePath(sut.createGrid(2, 3), 2, 3);
  }

  // Step 8: one of 26 valid 3 x 3 grids. Keeping the whole border free is the tempting mistake —
  //         the ring of row 0, row 2, column 0 and column 2 leaves 2 paths, one around each side
  //         of the blocked center, and the all-free grid leaves 6. Every free cell off the chosen
  //         route must be unable to rejoin it
  @Test
  void leetCodeExample2() {
    assertExactlyOnePath(sut.createGrid(3, 3), 3, 3);
  }

  // Step 9: forced — a single row has one walk, so "...." is the only answer and the spec pins it
  //         exactly. The staircase that reproduces Examples 1 and 2 answers "..##" here with zero
  //         paths: it is valid only when n is m or m + 1, so matching the first two examples is not
  //         the same as solving the problem
  @Test
  void leetCodeExample3() {
    assertThat(sut.createGrid(1, 4)).containsExactly("....");
  }

  // ===========================================================================================
  // The constraint bounds (Steps 10-12). At most 25 x 25 = 625 cells, so any direct construction
  // is O(m * n) and instant. The timeouts are for a search instead: blocking cells one at a time
  // and re-checking uniqueness by enumerating walks faces C(48, 24) ~ 3.2 * 10^13 walks in the
  // all-free 25 x 25 grid, which cannot finish and overflows an int counter besides.
  // ===========================================================================================

  // Step 10: the largest grid. The staircase happens to pass here because n = m, but the all-free
  //          grid has 32,247,603,683,100 paths and the border ring 2
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void m25n25LargestGrid() {
    assertExactlyOnePath(sut.createGrid(25, 25), 25, 25);
  }

  // Step 11: the widest single row, forced all free. The staircase answers ".." followed by 23 '#'
  //          with zero paths
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void m1n25WidestRowIsAllFree() {
    assertThat(sut.createGrid(1, 25)).containsExactly(".".repeat(25));
  }

  // Step 12: the mirror of Step 11 — the tallest single column, 25 strings of one free cell each
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void m25n1TallestColumnIsAllFree() {
    assertThat(sut.createGrid(25, 1)).hasSize(25).containsOnly(".");
  }

  // ===========================================================================================
  // Hygiene (Step 13).
  // ===========================================================================================

  // Step 13: one instance answers several sizes, the largest in the middle and the shapes
  //          deliberately out of order. A grid, char[] buffer or StringBuilder kept on the instance
  //          from the 25 x 25 call hands the 1 x 1 call rows of the wrong length, and the mirrored
  //          2 x 7 then 7 x 2 pair catches a dimension remembered from the previous call
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersGridsOfAnySizeInAnyOrder() {
    assertExactlyOnePath(sut.createGrid(3, 4), 3, 4);
    assertExactlyOnePath(sut.createGrid(25, 25), 25, 25);
    assertExactlyOnePath(sut.createGrid(1, 1), 1, 1);
    assertExactlyOnePath(sut.createGrid(2, 7), 2, 7);
    assertExactlyOnePath(sut.createGrid(7, 2), 7, 2);
  }

  // Asserts the contract every answer meets when more than one grid is valid: m rows of n cells
  // drawn only from '.' and '#', and exactly one right/down walk over free cells from the top-left
  // to the bottom-right. Free cells off that walk are allowed as long as they open no second
  // route, so the grid is judged by counting walks, never against a fixed picture.
  private static void assertExactlyOnePath(String[] grid, int m, int n) {
    assertThat(grid).hasSize(m).allSatisfy(row -> assertThat(row).hasSize(n).matches("[.#]+"));
    assertThat(countPaths(grid)).as("valid paths in %s", Arrays.toString(grid)).isOne();
  }

  // Rolling row DP: ways[j] is the number of walks from the top-left that end at column j of the
  // current row. A blocked cell resets it to 0, so a blocked start or end counts 0 walks. long
  // because the all-free 25 x 25 grid has C(48, 24) walks, far past Integer.MAX_VALUE.
  private static long countPaths(String[] grid) {
    long[] ways = new long[grid[0].length()];
    ways[0] = 1;
    for (String row : grid) {
      for (int j = 0; j < row.length(); j++) {
        if (row.charAt(j) == '#') {
          ways[j] = 0;
        } else if (j > 0) {
          ways[j] += ways[j - 1];
        }
      }
    }
    return ways[ways.length - 1];
  }
}
