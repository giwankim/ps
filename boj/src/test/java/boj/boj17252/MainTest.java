package boj.boj17252;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo("109")
  void sampleThreeThreeNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("298")
  void sampleNonThreeThreeNumber(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("0")
  void zeroIsNotThreeThreeBecauseAtLeastOnePowerIsRequired(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("1")
  void oneUsesTheSmallestPower(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("2")
  void twoWouldNeedToReuseOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("3")
  void singlePowerOtherThanOneIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("4")
  void adjacentDistinctPowersCanBeAdded(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("18")
  void repeatedLargerPowerIsRejected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("40")
  void allLowerPowersBeforeNextPowerAreAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("41")
  void oneMoreThanAllLowerPowersWouldReuseOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("81")
  void exactHigherPowerIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("90")
  void representationCanSkipSmallerPowers(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("1162261467")
  void largestSinglePowerWithinIntegerRangeIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("1743392200")
  void sumOfAllUsablePowersWithinIntegerRangeIsAllowed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  @Test
  @StdIo("1743392201")
  void oneAboveLargestRepresentableValueIsRejected(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  @Test
  @StdIo("2147483647")
  void maximumInputValueIsRejectedWhenBaseThreeContainsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }
}
