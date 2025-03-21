package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class MissingNumberTest {
  @Test
  void missingNumber() throws IOException {
    String input =
        """
        8
        2 1 8 6 7 4 3
        """;
    String expected = "5\n";
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    MissingNumber.missingNumber(r, pw);

    assertThat(out).hasToString(expected);
  }
}
