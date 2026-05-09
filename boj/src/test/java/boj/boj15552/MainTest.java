package boj.boj15552;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo({"5", "1 1", "12 34", "5 500", "40 60", "1000 1000"})
  void fastAPlusB(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "46", "505", "100", "2000");
  }
}
