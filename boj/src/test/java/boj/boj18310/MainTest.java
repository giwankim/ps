package boj.boj18310;

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
  @StdIo({"4", "5 1 7 9"})
  void sample(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("5");
  }

  @Test
  @StdIo({"1", "1"})
  void singleHouseAtLowerBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  @Test
  @StdIo({"1", "100000"})
  void singleHouseAtUpperBoundary(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("100000");
  }

  @Test
  @StdIo({"2", "100000 1"})
  void twoHousesChooseSmallerWhenBothMinimizeDistance(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1");
  }

  @Test
  @StdIo({"5", "10 1 7 4 3"})
  void oddHouseCountUsesMiddlePositionAfterSorting(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4");
  }

  @Test
  @StdIo({"6", "8 1 2 100 4 5"})
  void evenHouseCountUsesLowerMiddlePositionAfterSorting(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4");
  }

  @Test
  @StdIo({"6", "100 1 100 2 1 100"})
  void duplicatePositionsAreCountedWhenFindingMedian(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2");
  }

  @Test
  @StdIo({"5", "42 42 42 42 42"})
  void allHousesAtSamePosition(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("42");
  }

  @Test
  void maximumHouseCountChoosesSmallestOptimalBoundaryPosition() throws IOException {
    StringBuilder input = new StringBuilder();
    input.append("200000\n");
    for (int i = 0; i < 200000; i++) {
      if (i > 0) {
        input.append(' ');
      }
      input.append(i % 2 == 0 ? 100000 : 1);
    }
    input.append('\n');

    assertThat(runMain(input.toString())).isEqualTo("1");
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
