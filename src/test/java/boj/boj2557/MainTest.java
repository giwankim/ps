package boj.boj2557;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  void helloWorld() {
    BufferedReader r = new BufferedReader(new StringReader(""));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);

    Main.helloWorld(r, pw);

    assertThat(out).hasToString("Hello World!\n");
  }
}
