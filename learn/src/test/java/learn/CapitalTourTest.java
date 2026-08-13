package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class CapitalTourTest {
  @Test
  @StdIo({"4 20", "4 7 12 15", "6 8 14 17"})
  void printsThePairSummingToTheTargetFromTheTwoLines(StdOut out) throws IOException {
    CapitalTour.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12 8");
  }
}
