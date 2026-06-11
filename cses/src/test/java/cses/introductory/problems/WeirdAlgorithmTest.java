package cses.introductory.problems;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class WeirdAlgorithmTest {
  @Test
  void officialSampleWalksThreeDownToOne() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "3")).isEqualTo("3 10 5 16 8 4 2 1");
  }

  @Test
  void oneIsAlreadyTheTerminalValue() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "1")).isEqualTo("1");
  }

  @Test
  void powerOfTwoOnlyHalves() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "16")).isEqualTo("16 8 4 2 1");
  }

  @Test
  void valueAboveIntRangeKeepsHalvingWithoutOverflow() throws IOException {
    assertThat(run(WeirdAlgorithm::weirdAlgorithm, "2147483648"))
        .isEqualTo("2147483648 1073741824 536870912 268435456 134217728 67108864 33554432"
            + " 16777216 8388608 4194304 2097152 1048576 524288 262144 131072 65536 32768 16384"
            + " 8192 4096 2048 1024 512 256 128 64 32 16 8 4 2 1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("weird-algorithm", WeirdAlgorithm::weirdAlgorithm);
  }
}
