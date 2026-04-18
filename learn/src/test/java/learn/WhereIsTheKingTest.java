package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class WhereIsTheKingTest {
  @Test
  void whereIsTheKing() throws IOException {
    String input =
        """
        8 3 7
        3 7 2 1 4 8 5 6
        """;
    String expected = "1\n";
    BufferedReader r = new BufferedReader(new StringReader(input));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    WhereIsTheKing.whereIsTheKing(r, pw);

    assertThat(out).hasToString(expected);
  }
}
