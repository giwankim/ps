package boj.boj2508;

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
  @Test
  @StdIo({"1", "", "5 4", ".>o<", "v.^.", "ooo.", "^.^.", ">o<<"})
  void sampleCountsTwoHorizontalAndOneVerticalCandy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"1", "", "1 3", ">o<"})
  void singleHorizontalCandyInMinimalThreeCellRow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "3 1", "v", "o", "^"})
  void singleVerticalCandyInMinimalThreeCellColumn(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "3 3", "...", "...", "..."})
  void gridOfOnlyEmptyCellsCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "1 1", "."})
  void oneByOneGridHasZeroCandies(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "2 2", "><", "ov"})
  void twoByTwoGridIsTooSmallToFitAnyCandy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "1 6", ">o<>o<"})
  void adjacentHorizontalCandiesAreCountedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"1", "", "6 1", "v", "o", "^", "v", "o", "^"})
  void adjacentVerticalCandiesAreCountedIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"1", "", "1 4", ">o<."})
  void horizontalCandyFlushAgainstTheLeftEdgeIsDetected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "1 4", ".>o<"})
  void horizontalCandyFlushAgainstTheRightEdgeIsDetected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "4 1", "v", "o", "^", "."})
  void verticalCandyFlushAgainstTheTopEdgeIsDetected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "4 1", ".", "v", "o", "^"})
  void verticalCandyFlushAgainstTheBottomEdgeIsDetected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "1 3", ">o."})
  void partialHorizontalCandyMissingClosingArrowDoesNotCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "3 1", "v", "o", "."})
  void partialVerticalCandyMissingBottomArrowDoesNotCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "1 3", "<o>"})
  void reversedHorizontalArrowsDoNotFormACandy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "3 1", "^", "o", "v"})
  void reversedVerticalArrowsDoNotFormACandy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "2 3", ">o<", "<o>"})
  void mixOfCorrectAndReversedHorizontalArrowsCountsOnlyTheValidOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "", "2 3", "v<.", "^>o"})
  void scatteredShellCharactersWithoutACompleteTripleCountZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1", "", "3 5", ">o<.v", ".....", "....^"})
  void verticalShellSplitAcrossDistantRowsDoesNotFormACandy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3", "", "1 3", ">o<", "", "1 3", "...", "", "3 1", "v", "o", "^"})
  void multipleTestCasesProduceOneCountPerLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim().split("\\R")).containsExactly("1", "0", "1");
  }

  @Test
  void maximumGridDimensionsCountsAllPackedHorizontalCandies() throws IOException {
    StringBuilder input = new StringBuilder();
    input.append("1\n\n400 400\n");
    String row = ">o<".repeat(133) + ".";
    for (int i = 0; i < 400; i++) {
      input.append(row).append('\n');
    }
    assertThat(runMain(input.toString())).isEqualTo("53200");
  }

  @Test
  void maximumGridDimensionsCountsAllPackedVerticalCandies() throws IOException {
    StringBuilder input = new StringBuilder();
    input.append("1\n\n400 400\n");
    for (int i = 0; i < 400; i++) {
      char ch = i == 399
          ? '.'
          : switch (i % 3) {
            case 0 -> 'v';
            case 1 -> 'o';
            default -> '^';
          };
      input.append(String.valueOf(ch).repeat(400)).append('\n');
    }
    assertThat(runMain(input.toString())).isEqualTo("53200");
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
