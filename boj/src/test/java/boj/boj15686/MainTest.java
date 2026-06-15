package boj.boj15686;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 15686 치킨 배달 (Chicken Delivery).
 *
 * <p>An N×N city grid is given where each cell is empty (0), a house (1), or a chicken restaurant
 * (2), with 2 ≤ N ≤ 50. A house's <em>chicken distance</em> is the Manhattan distance {@code |r1 -
 * r2| + |c1 - c2|} to the nearest <em>kept</em> restaurant, and the <em>city's chicken
 * distance</em> is the sum of every house's chicken distance.
 *
 * <p>At most M of the restaurants are kept open and the rest close (1 ≤ M ≤ 13). The first input
 * line holds N and M; the next N lines hold the grid. Print the smallest city chicken distance over
 * every choice of kept restaurants. Keeping an extra restaurant can only shorten a house's distance
 * and the input always supplies at least M restaurants (M ≤ #restaurants ≤ 13), so an optimal
 * solution keeps exactly M. The grid holds between 1 and 2N houses.
 */
class MainTest {

  // --- Smallest grids and the Manhattan distance itself. ---

  @Test
  @StdIo({"2 1", "1 2", "0 0"})
  void singleHouseAdjacentToItsOnlyRestaurantHasDistanceOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The N = 2 lower bound: one house at (0,0) and the one restaurant beside it at (0,1). The lone
    // restaurant must be kept, so the city distance is the single step between them.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2 1", "1 0", "0 2"})
  void chickenDistanceAddsRowAndColumnGaps(StdOut out) throws IOException {
    Main.main(new String[0]);
    // House (0,0) and restaurant (1,1) differ by one row and one column, so the Manhattan distance
    // is 1 + 1 = 2 — a diagonal neighbor is two away, not one.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- One kept restaurant (M = 1): the choice must be globally best, not best for one house. ---

  @Test
  @StdIo({"3 1", "2 1 0", "1 0 0", "0 0 2"})
  void withOneRestaurantTheGloballyBestChoiceWins(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Houses (0,1) and (1,0) are one step from restaurant (0,0) but three steps from (2,2). Keeping
    // (0,0) totals 1 + 1 = 2; keeping (2,2) would cost 3 + 3 = 6, so the nearer restaurant wins.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3 1", "1 1 1", "0 2 0", "0 0 0"})
  void oneRestaurantCanBeNearestForEveryHouse(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The single restaurant at (1,1) serves all three houses on the top row: the corner houses are
    // two away and the middle house is one away, for 2 + 1 + 2 = 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Keeping every restaurant (M = restaurant count): each house uses its nearest. ---

  @Test
  @StdIo({"3 2", "2 1 2", "0 0 0", "1 0 1"})
  void whenEveryRestaurantIsKeptEachHouseUsesItsNearest(StdOut out) throws IOException {
    Main.main(new String[0]);
    // M equals the restaurant count (2), so both stay open. The top house is one step from either;
    // each bottom-corner house is two steps from the restaurant above it, giving 1 + 2 + 2 = 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- One grid at M = 1, 2, 3: keeping more restaurants never increases the distance. ---
  // All three share a 4×4 grid with restaurants at (0,0), (3,0) and (3,3) and houses at (0,1),
  // (2,3) and (3,2); only M changes.

  @Test
  @StdIo({"4 1", "2 1 0 0", "0 0 0 0", "0 0 0 1", "2 0 1 2"})
  void withOnlyOneRestaurantTwoHousesMustTravelFar(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Forced down to a single restaurant, the best choice (3,3) still strands the top house: (0,1)
    // is five away while (2,3) and (3,2) are one away each, for 5 + 1 + 1 = 7.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"4 2", "2 1 0 0", "0 0 0 0", "0 0 0 1", "2 0 1 2"})
  void aRedundantRestaurantIsDroppedWhenOnlyTwoAreKept(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Keeping (0,0) and (3,3) puts every house one step from a restaurant (1 + 1 + 1 = 3); the
    // bottom-left restaurant (3,0) is nearest to no house and is dropped.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"4 3", "2 1 0 0", "0 0 0 0", "0 0 0 1", "2 0 1 2"})
  void keepingAThirdRestaurantCannotBeatTheBestTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With all three restaurants kept, every house is already one step away — total 3, the same as
    // the best two-restaurant choice above. The extra restaurant adds nothing, illustrating the
    // "at most M" rule: more kept restaurants can only match, never beat, fewer.
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Exclusion has a real cost when each house has its own neighboring restaurant. ---
  // Grid: house (0,0) beside restaurant (0,1), and house (2,2) beside restaurant (2,1).

  @Test
  @StdIo({"3 2", "1 2 0", "0 0 0", "0 2 1"})
  void keepingBothRestaurantsLetsEachHouseUseItsNeighbor(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Each house sits next to its own restaurant, so keeping both gives 1 + 1 = 2.
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"3 1", "1 2 0", "0 0 0", "0 2 1"})
  void droppingOneRestaurantStrandsTheOtherHouse(StdOut out) throws IOException {
    Main.main(new String[0]);
    // With M = 1 only one restaurant survives; whichever is kept, its own house is one away but the
    // other house is three away, so the best the city can do is 1 + 3 = 4 instead of 2.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Upper bound on M: thirteen restaurants, the most the constraints allow, all kept. ---

  @Test
  @StdIo({"5 13", "2 2 2 2 2", "2 2 2 2 2", "2 2 2 0 0", "0 0 0 0 0", "1 0 0 0 1"})
  void theThirteenRestaurantUpperBoundIsAllKept(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Thirteen restaurants fill the top of the grid and M = 13, so every one stays open. House
    // (4,0) is two rows below restaurant (2,0) and house (4,4) is three rows below restaurant
    // (1,4), for 2 + 3 = 5.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Official samples from the statement. ---

  @Test
  @StdIo({"5 3", "0 0 1 0 0", "0 0 2 0 1", "0 1 2 0 0", "0 0 1 0 0", "0 0 0 0 2"})
  void officialSampleOneKeepsAllThreeRestaurantsForFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // There are exactly three restaurants and M = 3, so all are kept; the four houses lie
    // 1 + 2 + 1 + 1 from their nearest restaurant.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5 2", "0 2 0 1 0", "1 0 1 0 0", "0 0 0 0 0", "2 0 0 1 1", "2 2 0 1 2"})
  void officialSampleTwoChoosesTwoOfFiveRestaurantsForTen(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Keeping the restaurant at (0,1) and the one at (4,4) lets all six houses reach their globally
    // nearest restaurant at once, hitting the lower bound of 10.
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"5 1", "1 2 0 0 0", "1 2 0 0 0", "1 2 0 0 0", "1 2 0 0 0", "1 2 0 0 0"})
  void officialSampleThreePicksTheMiddleRestaurantForEleven(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Five houses fill column 0 and five restaurants fill column 1; with M = 1 the best single
    // restaurant is the middle one at (2,1), giving 3 + 2 + 1 + 2 + 3 = 11.
    assertThat(out.capturedString().trim()).isEqualTo("11");
  }

  // --- Generated grids through stdin/stdout, checked against a brute-force oracle. ---

  @Test
  void fullSizeRandomGridMatchesOracle() throws IOException {
    // Largest dimensions: N = 50 with 100 houses (the 2N ceiling) and 13 restaurants, keeping 6 —
    // the widest subset search the constraints allow (C(13,6) = 1716 combinations).
    int[][] grid = randomGrid(50, 100, 13, new Random(15686L));
    assertThat(runMain(buildInput(grid, 6))).isEqualTo(solve(grid, 6));
  }

  @Test
  void keepAllThirteenRandomGridMatchesOracle() throws IOException {
    // M equals the restaurant count, so every house collapses to its global nearest — the
    // degenerate end of the subset search, at full scale.
    int[][] grid = randomGrid(50, 100, 13, new Random(156861L));
    assertThat(runMain(buildInput(grid, 13))).isEqualTo(solve(grid, 13));
  }

  @Test
  void midSizeRandomGridMatchesOracle() throws IOException {
    // A different shape — N = 25, 40 houses, 11 restaurants, keep 4 — to vary the grid and the
    // restaurant-to-house ratio away from the extremes.
    int[][] grid = randomGrid(25, 40, 11, new Random(150686L));
    assertThat(runMain(buildInput(grid, 4))).isEqualTo(solve(grid, 4));
  }

  /**
   * Independent oracle: brute force over every size-{@code m} subset of the restaurants, summing
   * each house's nearest-restaurant distance and keeping the minimum total.
   */
  private static String solve(int[][] grid, int m) {
    List<int[]> houses = new ArrayList<>();
    List<int[]> shops = new ArrayList<>();
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[r].length; c++) {
        if (grid[r][c] == 1) {
          houses.add(new int[] {r, c});
        } else if (grid[r][c] == 2) {
          shops.add(new int[] {r, c});
        }
      }
    }
    int shopCount = shops.size();
    int best = Integer.MAX_VALUE;
    for (int mask = 0; mask < (1 << shopCount); mask++) {
      if (Integer.bitCount(mask) != m) {
        continue;
      }
      int total = 0;
      for (int[] house : houses) {
        int nearest = Integer.MAX_VALUE;
        for (int j = 0; j < shopCount; j++) {
          if ((mask & (1 << j)) != 0) {
            int[] shop = shops.get(j);
            int d = Math.abs(house[0] - shop[0]) + Math.abs(house[1] - shop[1]);
            nearest = Math.min(nearest, d);
          }
        }
        total += nearest;
      }
      best = Math.min(best, total);
    }
    return Integer.toString(best);
  }

  /** Builds BOJ 15686 input: an {@code "N M"} header line then N space-separated grid rows. */
  private static String buildInput(int[][] grid, int m) {
    int n = grid.length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(m).append('\n');
    for (int[] row : grid) {
      for (int c = 0; c < row.length; c++) {
        if (c > 0) {
          sb.append(' ');
        }
        sb.append(row[c]);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /**
   * Random N×N grid holding exactly {@code numHouses} houses (1) and {@code numShops} restaurants
   * (2) at distinct cells; every other cell stays empty (0).
   */
  private static int[][] randomGrid(int n, int numHouses, int numShops, Random rng) {
    int[][] grid = new int[n][n];
    Set<Integer> used = new HashSet<>();
    scatter(grid, used, n, numHouses, 1, rng);
    scatter(grid, used, n, numShops, 2, rng);
    return grid;
  }

  /** Places {@code count} cells of {@code value} at fresh random positions not already in use. */
  private static void scatter(
      int[][] grid, Set<Integer> used, int n, int count, int value, Random rng) {
    int placed = 0;
    while (placed < count) {
      int cell = rng.nextInt(n * n);
      if (used.add(cell)) {
        grid[cell / n][cell % n] = value;
        placed++;
      }
    }
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
