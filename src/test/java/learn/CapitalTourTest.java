package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class CapitalTourTest {
  @Test
  void capitalTour() throws IOException {
    String input =
        """
        4 20
        4 7 12 15
        6 8 14 17
        """;
    String expected =
        """
        12 8
        """;
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    CapitalTour.capitalTour(r, pw);

    assertThat(out).hasToString(expected);
  }
}
