package cses.graph.algorithms.counting.rooms;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class CountingRoomsTest {
  /** The thread stack for Steps 13-14; see the banner above them for how it was chosen. */
  private static final long BOUNDED_STACK_BYTES = 16L << 20;

  // ===========================================================================================
  // The smallest map (Steps 1-2). 1 <= n, m <= 1000, so a 1x1 map is legal and both of its
  // squares are worth a spec.
  // ===========================================================================================

  // Step 1: a lone floor square is a room even though no wall surrounds it. A solution that
  //         scans only the interior, assuming a wall border like the example's, answers 0
  @Test
  @StdIo({"1 1", "."})
  void singleFloorSquareIsOneRoom(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 2: the answer can be 0. A building with no floor has no rooms, so a counter that starts
  //         at 1 answers 1
  @Test
  @StdIo({"1 1", "#"})
  void singleWallSquareHasNoRooms(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // ===========================================================================================
  // Moving through floor squares (Steps 3-7): left, right, up and down, never through a wall,
  // never diagonally, and never off the map.
  // ===========================================================================================

  // Step 3: squares joined left-right form one room, so the answer counts rooms, not floor
  //         squares. Counting floor squares answers 4
  @Test
  @StdIo({"1 4", "...."})
  void horizontalRunOfFloorIsOneRoom(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 4: the mirror of Step 3. Up-down moves join squares too, so counting maximal horizontal
  //         runs of floor answers 4. It also pins n as the height: four lines of one square each,
  //         so a reader that swaps n and m looks for a four-square line that is not there
  @Test
  @StdIo({"4 1", ".", ".", ".", "."})
  void verticalRunOfFloorIsOneRoom(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 5: walking is through floor squares only, so one wall square cuts the row in two. A
  //         fill that steps onto walls answers 1
  @Test
  @StdIo({"1 5", "..#.."})
  void wallSplitsARowIntoTwoRooms(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 6: a diagonal step is not a move. The five floor squares of this X touch only at corners,
  //         along both diagonals, so each is its own room. An 8-direction flood fill answers 1
  @Test
  @StdIo({"3 3", ".#.", "#.#", ".#."})
  void squaresTouchingOnlyAtCornersAreSeparateRooms(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // Step 7: the example happens to be walled in, but the input need not be. Here the only floor
  //         squares are the four corners. Scanning only the interior answers 0, and a neighbor
  //         lookup without a bounds check throws an index-out-of-bounds error
  @Test
  @StdIo({"3 3", ".#.", "###", ".#."})
  void roomsMayTouchTheMapEdge(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // ===========================================================================================
  // Rooms whose shape defeats a shortcut (Steps 8-9). A row-major scan meets a room at its
  // top-left square first, which tempts a fill that only moves forward. These shapes have to be
  // walked back up and back left.
  // ===========================================================================================

  // Step 8: both arms of the U start in the top row and meet only at the bottom. Counting squares
  //         with no floor above and no floor to the left answers 2, and so does a fill that only
  //         expands right and down, since it cannot climb the right arm
  @Test
  @StdIo({"3 3", ".#.", ".#.", "..."})
  void uShapedRoomIsCountedOnce(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 9: one corridor winds right along the top, down the right edge, left along the bottom,
  //         up the left edge and right into the center, so every direction is needed. A fill that
  //         never moves up answers 2, and so does one that never moves left
  @Test
  @StdIo({"5 5", ".....", "####.", "...#.", ".###.", "....."})
  void spiralRoomNeedsAllFourDirections(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // ===========================================================================================
  // The official example (Step 10).
  // ===========================================================================================

  // Step 10: the two small rooms on the left share columns but are cut apart by a full wall row,
  //          and the room on the right is a ring around a single wall pillar. The ring is one room
  //          however it is entered. Counting horizontal runs of floor answers 6
  @Test
  @StdIo({"5 8", "########", "#..#...#", "####.#.#", "#..#...#", "########"})
  void csesExample(StdOut out) throws IOException {
    CountingRooms.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // ===========================================================================================
  // The constraint bounds (Steps 11-12). n = m = 1000 is 10^6 squares, and an O(nm) flood fill
  // finishes in well under a second. Work that is superlinear in a room's size, or that costs
  // O(nm) per room, needs about 5 * 10^11 steps and cannot finish.
  //
  // Steps 11-12 do not police recursion depth. The suite runs with -Xss64m
  // (ps.test-conventions), under which a recursive flood fill survives Step 11's 10^6-deep walk.
  // Steps 13-14 do.
  // ===========================================================================================

  // Step 11: one room covering all 10^6 squares. Tracking visited squares in a List and checking
  //          contains() before each step costs at least 5 * 10^11 comparisons
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxSizeOpenFloorIsOneRoom() throws IOException {
    assertThat(run(CountingRooms::main, map(1000, 1000, (i, j) -> true))).isEqualTo("1");
  }

  // Step 12: a 1000 x 1000 checkerboard, so 5 * 10^5 single-square rooms, every pair touching
  //          only at corners. Per-room O(nm) work, such as clearing the visited grid for each
  //          room or rescanning from the top-left for the next unvisited square, costs
  //          5 * 10^5 * 10^6 = 5 * 10^11 steps. An 8-direction fill answers 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxSizeCheckerboardIsAllSingleSquareRooms() throws IOException {
    assertThat(run(CountingRooms::main, map(1000, 1000, (i, j) -> (i + j) % 2 == 0)))
        .isEqualTo("500000");
  }

  // ===========================================================================================
  // CSES judge regressions (Steps 13-14): the two tests a recursive flood fill failed with TIME
  // LIMIT EXCEEDED. A timeout in this shared JVM cannot reproduce that verdict. Measured here, a
  // cold JVM spends about 0.76 s on the first 10^6-deep descent, against a 1 s limit, but once an
  // earlier step such as Step 12 has JIT-compiled the fill, the same input takes 27 ms.
  //
  // So both steps run main on a 16 MB stack instead. That holds at most about 520,000 frames of
  // a compiled flood fill (measured), so a 10^6-deep recursion overflows however warm the JVM
  // is, while an explicit stack or queue needs almost no thread stack. Even fully interpreted,
  // 16 MB still holds the 10^5-deep recursion that ps.test-conventions promises to allow.
  // ===========================================================================================

  // Step 13: CSES test #10, the open floor of Step 11 again. A recursive fill reaches depth 10^6
  //          under every one of the 24 orders of the four moves, so reordering them cannot help
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void csesTest10OpenFloorNeedsNoDeepRecursion() throws Throwable {
    assertThat(runOnBoundedStack(map(1000, 1000, (i, j) -> true))).isEqualTo("1");
  }

  // Step 14: CSES test #17, the same floor with only the bottom-right square walled off, so a
  //          shortcut for an all-floor map does not apply. A recursive fill still reaches depth
  //          999,998 under every order of the four moves
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void csesTest17CornerWallNeedsNoDeepRecursion() throws Throwable {
    assertThat(runOnBoundedStack(map(1000, 1000, (i, j) -> i < 999 || j < 999))).isEqualTo("1");
  }

  // ===========================================================================================
  // Hygiene (Step 15).
  // ===========================================================================================

  // Step 15: competitive-programming solutions keep the grid, the visited marks and the count in
  //          static fields, and every main call in this JVM shares them. Three runs, largest in
  //          the middle: a visited grid allocated at the first run's 1 x 4 size overflows on the
  //          second, a count that is never reset answers 500001 on the second, and a max-size
  //          visited grid that is never cleared answers 0 on the third, because the checkerboard
  //          already visited both of its floor squares
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void repeatedRunsStartFromFreshState() throws IOException {
    assertThat(run(CountingRooms::main, "1 4\n....\n")).isEqualTo("1");
    assertThat(run(CountingRooms::main, map(1000, 1000, (i, j) -> (i + j) % 2 == 0)))
        .isEqualTo("500000");
    assertThat(run(CountingRooms::main, "2 2\n.#\n#.\n")).isEqualTo("2");
  }

  /**
   * Runs main like {@link support.Cses#run}, but on a thread with a {@link #BOUNDED_STACK_BYTES}
   * stack, and rethrows whatever main threw there, such as a StackOverflowError.
   */
  private static String runOnBoundedStack(String input) throws Throwable {
    FutureTask<String> task = new FutureTask<>(() -> run(CountingRooms::main, input));
    new Thread(null, task, "bounded-stack", BOUNDED_STACK_BYTES).start();
    try {
      return task.get();
    } catch (ExecutionException e) {
      throw e.getCause();
    }
  }

  /** The header line plus n rows of m squares, where the square at row i, column j is floor. */
  private static String map(int n, int m, BiPredicate<Integer, Integer> isFloor) {
    StringBuilder sb = new StringBuilder().append(n).append(' ').append(m).append('\n');
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        sb.append(isFloor.test(i, j) ? '.' : '#');
      }
      sb.append('\n');
    }
    return sb.toString();
  }
}
