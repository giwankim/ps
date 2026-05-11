package boj.boj14400;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  // Step 1: Single house at origin — store coincides with the house, sum is 0.
  @Test
  @StdIo({"1", "0 0"})
  void singleHouseAtOriginGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0");
  }

  // Step 2: Single house off-origin — still zero, the store can sit on the house.
  @Test
  @StdIo({"1", "5 7"})
  void singleHouseAwayFromOriginGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0");
  }

  // Step 3: Two houses sharing y — answer is the horizontal gap.
  @Test
  @StdIo({"2", "0 0", "4 0"})
  void twoHousesHorizontallySeparated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4");
  }

  // Step 4: Two houses sharing x — answer is the vertical gap (symmetric to step 3).
  @Test
  @StdIo({"2", "0 0", "0 4"})
  void twoHousesVerticallySeparated(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4");
  }

  // Step 5: Two diagonally-placed houses — sum of axis-wise distances (|3|+|4| = 7).
  // Establishes that the answer separates into x- and y-components.
  @Test
  @StdIo({"2", "0 0", "3 4"})
  void twoHousesDiagonallySeparatedSumsAxisDistances(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("7");
  }

  // Step 6: Three collinear houses — store sits on the middle (median) house.
  // xs sorted = [1, 5, 10], median = 5, sum |xi - 5| = 4 + 0 + 5 = 9.
  @Test
  @StdIo({"3", "1 0", "5 0", "10 0"})
  void threeCollinearHousesUseMiddleAsMedian(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("9");
  }

  // Step 7: Three houses in 2D — coordinate-wise medians of x and y are picked independently.
  // xs = [0, 4, 0] → median 0, sum_x = 4. ys = [0, 0, 4] → median 0, sum_y = 4. Total = 8.
  @Test
  @StdIo({"3", "0 0", "4 0", "0 4"})
  void threeHousesUseSeparableCoordinateMedians(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("8");
  }

  // Step 8: Even N — any X in [x_sorted[N/2-1], x_sorted[N/2]] is optimal, but the
  // minimum sum is unique. xs = [0,1,2,3]: at X=1 sum=4, at X=2 sum=4 → answer 4.
  @Test
  @StdIo({"4", "0 0", "1 0", "2 0", "3 0"})
  void evenCountUsesAnyMedianBetweenMiddleTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4");
  }

  // Step 9: Negative coordinates are valid (constraint allows -10^9 ≤ x, y).
  // xs = [-5, 5] → sum_x = 10, ys = [-5, 5] → sum_y = 10, total = 20.
  @Test
  @StdIo({"2", "-5 -5", "5 5"})
  void negativeCoordinatesHandled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("20");
  }

  // Step 10: Duplicate coordinates count separately toward the median.
  // xs = [0,0,10,10], ys = [0,0,10,10] — median range [0,10] for each, sum = 20+20 = 40.
  @Test
  @StdIo({"4", "0 0", "0 0", "10 10", "10 10"})
  void duplicatePositionsCountedSeparately(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("40");
  }

  // Step 11: All houses identical — store sits on top, sum is 0.
  @Test
  @StdIo({"5", "3 7", "3 7", "3 7", "3 7", "3 7"})
  void allHousesAtSamePositionGiveZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0");
  }

  // Step 12: Input order must not matter — implementation must sort (or use selection)
  // to find the median. xs = [5,1,3,2,4] → median 3, sum 6. Same for ys. Total 12.
  @Test
  @StdIo({"5", "5 3", "1 1", "3 5", "2 2", "4 4"})
  void unsortedInputProducesCorrectMedian(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("12");
  }

  // Step 13: Result exceeds int range — forces use of `long` for the running sum.
  // Coords at the (-10^9, 10^9) extremes: sum_x = 2*10^9, sum_y = 2*10^9, total = 4*10^9.
  // int max ≈ 2.1*10^9, so an int accumulator silently overflows here.
  @Test
  @StdIo({"2", "-1000000000 -1000000000", "1000000000 1000000000"})
  void resultExceedsIntRangeRequiresLong(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4000000000");
  }

  // Step 14: Lower coordinate boundary — single house at (-10^9, -10^9).
  @Test
  @StdIo({"1", "-1000000000 -1000000000"})
  void coordinatesAtMaxNegativeBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0");
  }

  // Step 15: Upper coordinate boundary — single house at (10^9, 10^9), still 0.
  @Test
  @StdIo({"1", "1000000000 1000000000"})
  void coordinatesAtMaxPositiveBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0");
  }

  // Step 16: Maximum N (10^5) with worst-case coordinates — stress test for both
  // overflow and performance. 50,000 houses at (-10^9,-10^9) and 50,000 at (10^9,10^9):
  // sum_x = sum_y = 50,000 * 2*10^9 = 10^14, total = 2*10^14.
  // An O(N log N) sort solution must finish well within BOJ's time limit.
  @Test
  void maximumHouseCountWithExtremeCoordinatesDoesntOverflow() throws IOException {
    int n = 100_000;
    StringBuilder input = new StringBuilder();
    input.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      int v = i % 2 == 0 ? -1_000_000_000 : 1_000_000_000;
      input.append(v).append(' ').append(v).append('\n');
    }

    assertThat(runMain(input.toString())).isEqualTo("200000000000000");
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return new String(out.toByteArray(), StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
