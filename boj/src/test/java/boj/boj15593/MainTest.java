package boj.boj15593;

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

// BOJ 15593 "Lifeguards (Bronze)": each shift covers a half-open interval
// [start, end). Fire exactly one lifeguard and maximize the remaining covered time.
class MainTest {
  // Step 1: With a single lifeguard, exactly one must still be fired.
  @Test
  @StdIo({"1", "5 9"})
  void singleLifeguardLeavesNoCoveredTime(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 2: Published sample. Firing the middle interval keeps [1,4) and [5,9): 7 units.
  @Test
  @StdIo({"3", "5 9", "1 4", "3 7"})
  void sample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // Step 3: Endpoints are time points, so [0,1) and [999,1000) each cover one unit.
  @Test
  @StdIo({"2", "0 1", "999 1000"})
  void boundaryUnitIntervalsAreHalfOpen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 4: Overlap must be counted once, and one lifeguard is still fired.
  @Test
  @StdIo({"2", "0 2", "1 3"})
  void twoOverlappingShiftsKeepTheLongerRemainingCoverage(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 5: Disjoint shifts are handled by dropping the shortest covered interval.
  @Test
  @StdIo({"3", "0 3", "5 10", "20 30"})
  void disjointShiftsDropTheShortestOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("15");
  }

  // Step 6: A contained shift can be fired without reducing total coverage.
  @Test
  @StdIo({"3", "0 10", "2 4", "6 8"})
  void containedShiftIsFullyRedundant(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Step 7: In an overlap chain, the middle shift can be completely covered by neighbors.
  @Test
  @StdIo({"3", "0 6", "2 8", "5 10"})
  void chainedOverlapCanMakeMiddleShiftRedundant(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Step 8: When everyone has private time, fire the lifeguard with the least private coverage.
  @Test
  @StdIo({"3", "0 4", "2 7", "6 10"})
  void partialOverlapsMinimizeLostPrivateCoverage(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // Step 9: Full-day coverage can remain intact after firing a redundant nested shift.
  @Test
  @StdIo({"3", "0 1000", "1 999", "2 998"})
  void fullDayCoverageCanRemainAfterOneFire(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // Step 10: Multiple separated overlap components should still use one global best fire.
  @Test
  @StdIo({"4", "0 4", "2 6", "10 15", "14 20"})
  void separatedComponentsUseGlobalMinimumPrivateCoverage(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // Step 11: Maximum N with distinct endpoints. All shifts cover one isolated unit,
  // so firing any one of the 100 lifeguards leaves 99 units.
  @Test
  void maximumLifeguardCountWithDistinctEndpoints() throws IOException {
    int n = 100;
    StringBuilder input = new StringBuilder();
    input.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      input.append(i * 2).append(' ').append(i * 2 + 1).append('\n');
    }

    assertThat(runMain(input.toString())).isEqualTo("99");
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
