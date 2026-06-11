package cses.sorting.searching;

import static org.assertj.core.api.Assertions.assertThat;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class SumTwoValuesTest {
  @Test
  void officialSampleFindsAPairSummingToTarget() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "4 8\n2 7 5 1")).isEqualTo("4 2");
  }

  @Test
  void duplicateValuesCanFormThePair() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "4 6\n3 3 1 9")).isEqualTo("1 2");
  }

  @Test
  void valueCannotPairWithItself() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "3 4\n2 5 8")).isEqualTo("IMPOSSIBLE");
  }

  @Test
  void singleValueIsImpossible() throws IOException {
    assertThat(run(SumTwoValues::sumTwoValues, "1 2\n1")).isEqualTo("IMPOSSIBLE");
  }

  @TestFactory
  Stream<DynamicTest> officialTestData() {
    return tests("sum-of-two-values", SumTwoValues::sumTwoValues);
  }
}
