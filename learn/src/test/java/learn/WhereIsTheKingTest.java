package learn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class WhereIsTheKingTest {
  @Test
  @StdIo({"8 3 7", "3 7 2 1 4 8 5 6"})
  void printsTheCupPositionHoldingTheKingAfterTheShuffles(StdOut out) throws IOException {
    WhereIsTheKing.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }
}
