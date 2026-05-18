package boj.boj2563;

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
  @StdIo({"3", "3 7", "15 7", "5 2"})
  void sampleOverlappingPapers(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("260");
  }

  @Test
  @StdIo({"1", "0 0"})
  void singlePaperAtLowerLeftCorner(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"1", "90 90"})
  void singlePaperAtUpperRightBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"2", "0 0", "10 0"})
  void touchingEdgesDoNotOverlap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  @Test
  @StdIo({"2", "0 0", "0 10"})
  void touchingEdgesVerticallyDoNotOverlap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("200");
  }

  @Test
  @StdIo({"2", "20 20", "20 20"})
  void identicalPapersCountOnlyOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"2", "0 0", "5 0"})
  void horizontalHalfOverlapSubtractsSharedCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("150");
  }

  @Test
  @StdIo({"2", "0 0", "0 5"})
  void verticalHalfOverlapSubtractsSharedCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("150");
  }

  @Test
  @StdIo({"2", "0 0", "5 5"})
  void diagonalOverlapSubtractsSquareIntersection(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("175");
  }

  @Test
  @StdIo({"3", "0 0", "5 0", "2 0"})
  void nestedTripleOverlapIsNotDoubleSubtracted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("150");
  }

  @Test
  @StdIo({"4", "0 0", "5 0", "0 5", "5 5"})
  void fourPapersCoverOneLargeSquare(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("225");
  }

  @Test
  @StdIo({"4", "0 0", "90 0", "0 90", "90 90"})
  void papersAtAllBoardCornersDoNotOverlap(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("400");
  }

  @Test
  void maximumPaperCountCanCoverTheWholeBoard() throws IOException {
    StringBuilder input = new StringBuilder("100\n");
    for (int y = 0; y < 100; y += 10) {
      for (int x = 0; x < 100; x += 10) {
        input.append(x).append(' ').append(y).append('\n');
      }
    }

    assertThat(runMain(input.toString())).isEqualTo("10000");
  }

  @Test
  void maximumPaperCountAllDuplicatesStillCountsOnce() throws IOException {
    StringBuilder input = new StringBuilder("100\n");
    for (int i = 0; i < 100; i++) {
      input.append("45 45\n");
    }

    assertThat(runMain(input.toString())).isEqualTo("100");
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
