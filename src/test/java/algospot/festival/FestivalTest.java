package algospot.festival;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class FestivalTest {
  @Test
  void festival() {
    String input =
        """
        2
        6 3
        1 2 3 1 2 3
        6 2
        1 2 3 1 2 3
        """;
    String expected =
        """
        1.75000000000
        1.50000000000
        """;

    InputStream is = System.in;
    PrintStream ps = System.out;

    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    try {
      Main.main(new String[0]);
      assertThat(out).hasToString(expected);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      System.setIn(is);
      System.setOut(ps);
    }
  }
}
