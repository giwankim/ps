package boj.boj2304;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 2304 창고 다각형 (warehouse polygon).
 *
 * <p>Each pillar at left-face position {@code L} with height {@code H} is a width-1 rectangle
 * covering column {@code [L, L+1)}. The minimal warehouse roof is the unimodal envelope; the area
 * of column {@code x} equals {@code min(maxH over L<=x, maxH over L>=x)}, summed over {@code x} from
 * the leftmost to the rightmost pillar. Every expected value below is hand-derived with that model
 * and cross-checked against an accepted jump-based reference solution.
 */
class MainTest {

  // --- Single pillar: area is just its width-1 column = its height. ---

  @Test
  @StdIo({"1", "3 9"})
  void singlePillarAreaEqualsItsHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  @Test
  @StdIo({"1", "1 1"})
  void singlePillarAtMinimumConstraintsHasAreaOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "1 1000"})
  void singlePillarAtMaximumHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000");
  }

  // --- Two pillars: which side is taller decides the descending step. ---

  @Test
  @StdIo({"2", "1 5", "2 3"})
  void twoAdjacentPillarsTallestOnTheLeft(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  @Test
  @StdIo({"2", "1 3", "2 5"})
  void twoAdjacentPillarsTallestOnTheRight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  @Test
  @StdIo({"2", "1 5", "2 5"})
  void twoAdjacentEqualHeightPillarsFormFlatTop(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"2", "1 4", "5 4"})
  void equalHeightPillarsKeepRoofFlatAcrossTheGap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  @Test
  @StdIo({"2", "1 2", "8 9"})
  void risingRoofStaysAtTheShortLeftPillarUntilTheTallRightPeak(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("23");
  }

  // --- Symmetric mountain: peak in the middle. ---

  @Test
  @StdIo({"3", "1 3", "2 6", "3 3"})
  void symmetricMountainWithPeakInTheMiddle(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Shorter pillars swallowed under a higher roof. ---

  @Test
  @StdIo({"3", "1 10", "2 2", "5 4"})
  void shortPillarSwallowedUnderRoofHeldByAFartherRightPillar(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("26");
  }

  @Test
  @StdIo({"5", "1 2", "3 6", "5 9", "7 5", "9 3"})
  void mountainWithSwallowedPillarsOnBothSlopes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("41");
  }

  // --- Peak at an end: one slope only. ---

  @Test
  @StdIo({"4", "1 10", "3 7", "6 4", "10 1"})
  void peakAtTheLeftEndWithLongDescendingStaircase(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("40");
  }

  @Test
  @StdIo({"4", "1 1", "5 4", "8 7", "10 10"})
  void peakAtTheRightEndWithLongAscendingStaircase(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("40");
  }

  // --- Tied tallest pillars: flat top spans between them. ---

  @Test
  @StdIo({"5", "1 3", "3 8", "5 2", "7 8", "9 4"})
  void tiedTallestPillarsKeepFlatTopAndSwallowTheValleyBetween(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("54");
  }

  @Test
  @StdIo({"3", "1 7", "2 7", "5 2"})
  void tiedTallestPillarsAdjacentAtTheLeftThenDropToShortRight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // --- Realistic mixed case (7 pillars, both slopes, swallowed valleys). ---

  @Test
  @StdIo({"7", "2 4", "11 4", "15 6", "4 8", "5 3", "8 10", "13 7"})
  void mixedSevenPillarScenario(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("97");
  }

  // --- Degenerate: multiple pillars stacked at the same position. ---

  @Disabled(
      "The problem statement doesn't specify how to handle this case, but the solution should at least be consistent and not crash.")
  @Test
  @StdIo({"3", "2 5", "2 9", "2 1"})
  void duplicatePositionKeepsOnlyTheTallestColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- Upper-bound sizing: N at the limit, area near the int ceiling. ---

  @Test
  void maxPillarsFormingOneThousandByOneThousandRectangle() throws IOException {
    StringBuilder input = new StringBuilder("1000\n");
    for (int l = 1; l <= 1000; l++) {
      input.append(l).append(' ').append(1000).append('\n');
    }

    assertThat(runMain(input.toString())).isEqualTo("1000000");
  }

  @Test
  void maxPillarsAllStackedAtTheSamePositionCountAsOneColumn() throws IOException {
    StringBuilder input = new StringBuilder("1000\n");
    for (int i = 0; i < 1000; i++) {
      input.append("500 1000\n");
    }

    assertThat(runMain(input.toString())).isEqualTo("1000");
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
