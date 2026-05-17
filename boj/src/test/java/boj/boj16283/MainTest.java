package boj.boj16283;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo("3 4 9 32")
  void sampleUniqueSplit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 5");
  }

  @Test
  @StdIo("3 4 8 32")
  void zeroSheepCandidateIsRejected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo("100 100 2 200")
  void minimumAnimalCountHasSinglePositiveSplitWhenFeedRatesMatch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1");
  }

  @Test
  @StdIo("1000 1000 1000 1000000")
  void matchingFeedRatesWithManyAnimalsIsAmbiguousAtMaxBounds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo("6 6 4 23")
  void matchingFeedRatesWithWrongTotalHasNoSolution(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo("7 10 5 47")
  void uniqueSplitCanUseOneSheep(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 4");
  }

  @Test
  @StdIo("7 10 5 38")
  void uniqueSplitCanUseOneGoat(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 1");
  }

  @Test
  @StdIo("10 3 7 35")
  void sheepMayEatMoreThanGoats(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 5");
  }

  @Test
  @StdIo("2 4 5 11")
  void nonIntegralSplitHasNoSolution(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo("1 1 2 2")
  void minimumInputValuesProduceSingleSplit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1");
  }

  @Test
  @StdIo("1000 1 1000 999001")
  void maximumAnimalCountCanStillHaveUniqueBoundarySplit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("999 1");
  }
}
