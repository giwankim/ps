package boj.boj2435;

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
  @StdIo({"10 2", "3 -2 -4 -9 0 3 7 13 8 -3"})
  void sampleTwoDayWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  @Test
  @StdIo({"10 5", "3 -2 -4 -9 0 3 7 13 8 -3"})
  void exampleFiveDayWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("31");
  }

  @Test
  @StdIo({"5 1", "-100 -3 -50 -4 -99"})
  void oneDayWindowReturnsLargestSingleTemperature(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-3");
  }

  @Test
  @StdIo({"5 5", "-1 2 -3 4 -5"})
  void wholePeriodWindowReturnsTotalSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-3");
  }

  @Test
  @StdIo({"5 2", "-8 -3 -7 -2 -6"})
  void allNegativeWindowsStillReturnLargestSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-8");
  }

  @Test
  @StdIo({"5 3", "10 20 30 -100 -100"})
  void bestWindowCanStartAtFirstDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }

  @Test
  @StdIo({"5 3", "-100 -100 30 20 10"})
  void bestWindowCanEndAtLastDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }

  @Test
  @StdIo({"2 2", "-100 100"})
  void minimumDayCountCanUseWholeWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  void maximumDayCountCanProduceLargestPossibleSum() throws IOException {
    assertThat(runMain(repeatedTemperaturesInput(100, 100, 100))).isEqualTo("10000");
  }

  @Test
  void maximumDayCountCanProduceSmallestPossibleSum() throws IOException {
    assertThat(runMain(repeatedTemperaturesInput(100, 100, -100))).isEqualTo("-10000");
  }

  private static String repeatedTemperaturesInput(int n, int k, int temperature) {
    StringBuilder input = new StringBuilder();
    input.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      if (i > 0) {
        input.append(' ');
      }
      input.append(temperature);
    }
    input.append('\n');
    return input.toString();
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
