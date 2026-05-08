package boj.boj1000;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  void aPlusB_smallDigits() throws IOException {
    assertAPlusB("1 2\n", "3\n");
  }

  @Test
  void aPlusB_minBoundary() throws IOException {
    assertAPlusB("1 1\n", "2\n");
  }

  @Test
  void aPlusB_maxSingleDigits() throws IOException {
    assertAPlusB("9 9\n", "18\n");
  }

  private static void assertAPlusB(String input, String expected) throws IOException {
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    Main.aPlusB(r, new PrintWriter(out));
    assertThat(out).hasToString(expected);
  }
}
