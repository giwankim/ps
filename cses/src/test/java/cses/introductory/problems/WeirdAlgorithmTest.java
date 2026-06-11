package cses.introductory.problems;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class WeirdAlgorithmTest {
  @Test
  @StdIo("3")
  void officialSampleWalksThreeDownToOne(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 10 5 16 8 4 2 1");
  }

  @Test
  @StdIo("1")
  void oneIsAlreadyTheTerminalValue(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo("16")
  void powerOfTwoOnlyHalves(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16 8 4 2 1");
  }

  @Test
  @StdIo("2147483648")
  void valueAboveIntRangeKeepsHalvingWithoutOverflow(StdOut out) throws IOException {
    WeirdAlgorithm.main(new String[0]);
    assertThat(out.capturedString().trim())
        .isEqualTo("2147483648 1073741824 536870912 268435456 134217728 67108864 33554432"
            + " 16777216 8388608 4194304 2097152 1048576 524288 262144 131072 65536 32768 16384"
            + " 8192 4096 2048 1024 512 256 128 64 32 16 8 4 2 1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("weird-algorithm", WeirdAlgorithm::main);
  }
}
