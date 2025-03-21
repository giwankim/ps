package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class WalkAroundLakeTest {
  @Test
  void walkAroundLake() throws IOException {
    String input =
        """
        30 5
        8 14 19 16 6
        6 5 6 5 6
        """;
    String expected = "4\n";
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    WalkAroundLake.walkAroundLake(r, pw);

    assertThat(out).hasToString(expected);
  }
}
