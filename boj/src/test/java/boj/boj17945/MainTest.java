package boj.boj17945;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo("2 3")
  void twoDistinctNegativeRoots(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-3 -1");
  }

  @Test
  @StdIo("1 -8")
  void distinctRootsWithDifferentSigns(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-4 2");
  }

  @Test
  @StdIo("1 1")
  void repeatedRootIsPrintedOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo("0 -100")
  void symmetricRootsArePrintedInAscendingOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-10 10");
  }

  @Test
  @StdIo("-1000 0")
  void coefficientLowerBoundaryCanProducePositiveRootOutsideCoefficientRange(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 2000");
  }

  @Test
  @StdIo("1000 0")
  void coefficientUpperBoundaryCanProduceNegativeRootOutsideCoefficientRange(StdOut out)
      throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-2000 0");
  }
}
