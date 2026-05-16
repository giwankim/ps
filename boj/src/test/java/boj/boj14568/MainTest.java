package boj.boj14568;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {

  // Step 1: lower constraint bound N == 1. No leftover-free split of 1 candy can give
  // everyone >= 1, so the answer is 0 — also guards the "print 0 when impossible" rule.
  @Test
  @StdIo({"1"})
  void minimumInputIsInfeasible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 2: just below the feasibility threshold. The cheapest legal triple is
  // A=2 (even, >=1), B=1, C=3 (C >= B+2), summing to 6 — so every N <= 5 yields 0.
  @Test
  @StdIo({"5"})
  void justBelowFeasibilityThresholdIsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 3: the feasibility threshold N == 6 — exactly one triple (A,B,C)=(2,1,3).
  // First non-zero answer; pins the boundary between Step 2 and here.
  @Test
  @StdIo({"6"})
  void feasibilityThresholdHasExactlyOneWay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 4: odd N == 7 is still feasible (count 1). Isolates the rule that *Taekhee's*
  // share A must be even — it is N-parity-independent, ruling out an "N must be even" bug.
  @Test
  @StdIo({"7"})
  void oddNStillFeasibleBecauseConstraintIsOnAnotN(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 5: first count > 1. N == 8 has 3 triples — exercises summing over multiple
  // valid A values rather than short-circuiting on the first match.
  @Test
  @StdIo({"8"})
  void multipleWaysAreAllCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 6: parity guard. Even N=8 and odd N=9 both yield 3 — equal counts across an
  // even/odd pair would be impossible if the code keyed feasibility off N's parity.
  @Test
  @StdIo({"9"})
  void adjacentOddNMatchesEvenNCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 7: representative mid-range value. N == 12 -> 10 distinct triples.
  @Test
  @StdIo({"12"})
  void midRangeValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // Step 8: odd value just below the upper bound. N == 99 -> 1128.
  @Test
  @StdIo({"99"})
  void nearUpperBoundOdd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1128");
  }

  // Step 9: upper constraint bound N == 100 (1 <= N <= 100) -> 1176, the maximum
  // answer over the whole legal input range.
  @Test
  @StdIo({"100"})
  void upperConstraintBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1176");
  }
}
