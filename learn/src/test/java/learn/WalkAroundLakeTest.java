package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class WalkAroundLakeTest {
  @Test
  @StdIo({"30 5", "8 14 19 16 6", "6 5 6 5 6"})
  void printsTheFirstStepWhereBessieCatchesElsie(StdOut out) throws IOException {
    WalkAroundLake.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }
}
