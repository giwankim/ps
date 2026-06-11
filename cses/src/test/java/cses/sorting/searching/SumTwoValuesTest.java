package cses.sorting.searching;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class SumTwoValuesTest {
  @Test
  @StdIo({"4 8", "2 7 5 1"})
  void officialSampleFindsAPairSummingToTarget(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 2");
  }

  @Test
  @StdIo({"4 6", "3 3 1 9"})
  void duplicateValuesCanFormThePair(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2");
  }

  @Test
  @StdIo({"3 4", "2 5 8"})
  void valueCannotPairWithItself(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("IMPOSSIBLE");
  }

  @Test
  @StdIo({"1 2", "1"})
  void singleValueIsImpossible(StdOut out) throws IOException {
    SumTwoValues.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("IMPOSSIBLE");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("sum-of-two-values", SumTwoValues::main);
  }
}
