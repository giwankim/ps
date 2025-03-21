package boj.boj15552;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  void fastAPlusB() throws IOException {
    String input =
        """
        5
        1 1
        12 34
        5 500
        40 60
        1000 1000
        """;
    String expected =
        """
        2
        46
        505
        100
        2000
        """;
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    Main.fastAPlusB(r, pw);

    assertThat(out).hasToString(expected);
  }
}
