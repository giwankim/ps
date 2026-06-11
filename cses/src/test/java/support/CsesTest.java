package support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static support.Cses.run;
import static support.Cses.tests;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

class CsesTest {
  /** Echoes the first input line, the simplest solver that satisfies the valid fixture pairs. */
  private static final Cses.Solver ECHO = (r, pw) -> pw.println(r.readLine());

  @Test
  void runFeedsInputToTheSolver() throws IOException {
    assertThat(run(ECHO, "hello")).isEqualTo("hello");
  }

  @Test
  void runStripsTrailingWhitespaceFromSolverOutput() throws IOException {
    assertThat(run((r, pw) -> pw.println("answer  "), "")).isEqualTo("answer");
  }

  @Test
  void runPreservesLeadingWhitespace() throws IOException {
    assertThat(run((r, pw) -> pw.println("  answer"), "")).isEqualTo("  answer");
  }

  @Test
  void testsDiscoversAllPairsInNumericOrder() {
    List<String> names =
        tests("cses-test-fixture", ECHO).map(DynamicTest::getDisplayName).toList();
    assertThat(names).containsExactly("1", "2", "10");
  }

  @Test
  void testsExecuteTheSolverAgainstOutFiles() throws Throwable {
    for (DynamicTest test : tests("cses-test-fixture", ECHO).toList()) {
      test.getExecutable().execute();
    }
  }

  @Test
  void mismatchedOutputFailsTheDynamicTest() {
    DynamicTest first =
        tests("cses-test-fixture", (r, pw) -> pw.println("wrong")).findFirst().orElseThrow();
    assertThatThrownBy(first.getExecutable()::execute).isInstanceOf(AssertionError.class);
  }

  @Test
  void missingSlugFolderThrows() {
    assertThatThrownBy(() -> tests("no-such-slug", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("no-such-slug");
  }

  @Test
  void slugFolderWithoutInputFilesThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-empty", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("cses-test-fixture-empty");
  }

  @Test
  void inputWithoutMatchingOutputThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-orphan", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("1.out");
  }

  @Test
  void nonNumericInputFileNameThrows() {
    assertThatThrownBy(() -> tests("cses-test-fixture-nonnumeric", ECHO))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("sample.in");
  }
}
