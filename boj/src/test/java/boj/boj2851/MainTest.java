package boj.boj2851;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {

  @Test
  @StdIo({"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"})
  void exactHundredFromSampleStyleInput(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"10", "10", "10", "10", "10", "10", "10", "10", "10", "10"})
  void exactHundredCanRequireAllTenMushrooms(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"80", "30", "1", "1", "1", "1", "1", "1", "1", "1"})
  void nearestScoreCanBeAfterPassingHundred(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("110");
  }

  @Test
  @StdIo({"49", "49", "4", "1", "1", "1", "1", "1", "1", "1"})
  void tieBetweenBelowAndAboveHundredChoosesLargerScore(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("102");
  }

  @Test
  @StdIo({"1", "1", "1", "1", "1", "1", "1", "1", "91", "3"})
  void closerBelowHundredBeatsLargerOvershoot(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("99");
  }

  @Test
  @StdIo({"99", "100", "1", "1", "1", "1", "1", "1", "1", "1"})
  void stopsBeforeLargeOvershootWhenPreviousPrefixIsCloser(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("99");
  }

  @Test
  @StdIo({"1", "1", "1", "1", "1", "1", "1", "1", "1", "1"})
  void usesAllMushroomsWhenEveryPrefixIsBelowHundred(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"100", "100", "100", "100", "100", "100", "100", "100", "100", "100"})
  void firstMushroomCanBeOptimalAtScoreUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  @Test
  @StdIo({"60", "100", "1", "1", "1", "1", "1", "1", "1", "1"})
  void cannotSkipEarlierMushroomsToUseALaterScore(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("60");
  }
}
