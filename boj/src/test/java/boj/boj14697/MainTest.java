package boj.boj14697;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo("5 9 12 113")
  void samplePossibleAssignment(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("3 6 9 112")
  void sampleImpossibleAssignment(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo("1 2 3 1")
  void minimumStudentCountPossibleWhenSingleRoomFits(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("2 3 4 1")
  void minimumStudentCountImpossibleWithoutOnePersonRoom(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo("5 6 7 4")
  void belowSmallestRoomCapacityIsImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo("4 7 10 28")
  void exactMultipleOfOneRoomTypeIsPossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("5 9 12 95")
  void twoRoomTypesAreEnoughWithoutUsingThird(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("5 9 12 26")
  void allThreeRoomTypesCanBeRequired(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("6 10 14 299")
  void commonDivisorBlocksOddStudentCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo("48 49 50 51")
  void justAboveLargestCapacityCanStillBeImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo("48 49 50 300")
  void maximumStudentCountAtMaximumRoomSizesIsPossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }
}
