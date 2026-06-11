package cses.introductory.problems;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MissingNumberTest {
  @Test
  @StdIo({"5", "2 3 1 5"})
  void officialSampleFindsMissingMiddleValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"2", "2"})
  void smallestCaseMissingFirstValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "1"})
  void smallestCaseMissingLastValue(StdOut out) throws IOException {
    MissingNumber.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Generated input cannot be an annotation constant -> redirect helper.
  @Test
  void maxSizeSumOverflowsIntWithoutLong() throws IOException {
    String numbers =
        IntStream.rangeClosed(2, 200_000).mapToObj(Integer::toString).collect(joining(" "));
    assertThat(run(MissingNumber::main, "200000\n" + numbers)).isEqualTo("1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("missing-number", MissingNumber::main);
  }
}
