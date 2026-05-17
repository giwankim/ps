package boj.boj19532;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo("1 3 -1 4 1 7")
  void sampleWithPositiveXAndNegativeY(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 -1");
  }

  @Test
  @StdIo("2 5 8 3 -4 -11")
  void sampleWithNegativeXAndPositiveY(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1 2");
  }

  @Test
  @StdIo("7 5 -1 -4 6 -26")
  void nonUnitDeterminantWithMixedSigns(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 -3");
  }

  @Test
  @StdIo("1 1 5 2 -3 -15")
  void zeroXIsValidSolutionComponent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 5");
  }

  @Test
  @StdIo("2 3 -14 -5 4 35")
  void zeroYIsValidSolutionComponent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-7 0");
  }

  @Test
  @StdIo("0 1 0 1 0 0")
  void zeroCoefficientsCanStillHaveUniqueZeroSolution(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0");
  }

  @Test
  @StdIo("1 0 -999 0 1 999")
  void solutionCanUseLowerAndUpperBoundaries(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-999 999");
  }

  @Test
  @StdIo("1 0 999 0 1 -999")
  void solutionCanUseUpperAndLowerBoundaries(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("999 -999");
  }

  @Test
  @StdIo("999 0 999 0 -999 999")
  void coefficientBoundsCanProduceSmallMixedSignSolution(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 -1");
  }

  @Test
  @StdIo("1 -1 -999 1 1 999")
  void rhsBoundsCanAppearInCoupledEquations(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 999");
  }
}
