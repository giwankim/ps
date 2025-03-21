package algospot.boggle;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import org.junit.jupiter.api.Test;

class BoggleTest {
  @Test
  void boggle() {
    String input =
        """
        1
        URLPM
        XPRET
        GIAET
        XTNZY
        XOQRS
        6
        PRETTY
        GIRL
        REPEAT
        KARA
        PANDORA
        GIAZAPX
        """;
    String expected =
        """
        PRETTY YES
        GIRL YES
        REPEAT YES
        KARA NO
        PANDORA NO
        GIAZAPX YES
        """;

    InputStream is = System.in;
    PrintStream ps = System.out;

    ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    System.setIn(in);
    System.setOut(new PrintStream(out));

    try {
      Main.main(new String[] {});
      assertThat(out).hasToString(expected);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      System.setIn(is);
      System.setOut(ps);
    }
  }
}
