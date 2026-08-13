package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MissingNumberTest {
  @Test
  @StdIo({"8", "2 1 8 6 7 4 3"})
  void printsTheNumberMissingFromOneThroughN(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }
}
