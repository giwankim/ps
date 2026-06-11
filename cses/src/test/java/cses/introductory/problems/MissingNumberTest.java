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

class MissingNumberTest {
  @Test
  void officialSampleFindsMissingMiddleValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "5\n2 3 1 5")).isEqualTo("4");
  }

  @Test
  void smallestCaseMissingFirstValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "2\n2")).isEqualTo("1");
  }

  @Test
  void smallestCaseMissingLastValue() throws IOException {
    assertThat(run(MissingNumber::missingNumber, "2\n1")).isEqualTo("2");
  }

  @Test
  void maxSizeSumOverflowsIntWithoutLong() throws IOException {
    String numbers =
        IntStream.rangeClosed(2, 200_000).mapToObj(Integer::toString).collect(joining(" "));
    assertThat(run(MissingNumber::missingNumber, "200000\n" + numbers)).isEqualTo("1");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("missing-number", MissingNumber::missingNumber);
  }
}
