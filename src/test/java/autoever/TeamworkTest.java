package autoever;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class TeamworkTest {

  @StdIo({
    """
          5 2 5
          4 5
          3 4
          2 1
          5 7
          1 1
          """
  })
  @Test
  void teamwork(StdOut out) throws IOException {
    Teamwork.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("13");
  }

  @Test
  @StdIo({
    """
      10 3 7
      4 5
      3 4
      2 1
      5 7
      1 1
      7 8
      8 6
      3 3
      4 3
      5 4
      """
  })
  void teamwork2(StdOut out) throws IOException {
    Teamwork.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("25");
  }
}
