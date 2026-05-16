package boj.boj6131;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {

  // Step 1: simplest happy path — the smallest positive N that admits a solution.
  // (A-B)(A+B)=3 forces A-B=1, A+B=3 -> the single pair (2,1): 2^2 - 1^2 = 3.
  @Test
  @StdIo({"3"})
  void smallestSolvableNHasExactlyOnePair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 2: the answer can legitimately be zero. N=1 needs (A-B)(A+B)=1, i.e. A=1,B=0,
  // but B>=1 is required, so no pair qualifies.
  @Test
  @StdIo({"1"})
  void smallestPositiveNHasNoPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 3: parity structure. A^2-B^2 = (A-B)(A+B); the two factors share parity, so the
  // product is odd or divisible by 4 — never N ≡ 2 (mod 4). N=2 must yield 0.
  @Test
  @StdIo({"2"})
  void nCongruentToTwoMod4IsImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 4: confirm Step 3 is a structural rule, not a coincidence at N=2 — another
  // N ≡ 2 (mod 4) value (6 = 2 + 4) is likewise unrepresentable.
  @Test
  @StdIo({"6"})
  void anotherNCongruentToTwoMod4IsImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 5: divisible-by-4 is necessary but not sufficient. N=4 = (A-B)(A+B): the only
  // same-parity factoring is 2*2 -> A=2,B=0, rejected by B>=1. Still 0.
  @Test
  @StdIo({"4"})
  void divisibleByFourCanStillHaveNoPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Step 6: odd perfect square. N=9 -> 5^2 - 4^2 = 9, exactly one pair.
  @Test
  @StdIo({"9"})
  void oddPerfectSquareHasOnePair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 7: even N that IS representable. N=16 -> 5^2 - 3^2 = 16, exactly one pair.
  @Test
  @StdIo({"16"})
  void evenRepresentableNHasOnePair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 8: multiple distinct pairs must all be counted, not just the first found.
  // N=24 -> 5^2-1^2 and 7^2-5^2 -> 2.
  @Test
  @StdIo({"24"})
  void multiplePairsAreAllCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 9: a mid-range value with several solutions, as a general correctness anchor.
  // N=99 -> (10,1),(18,15),(50,49) -> 3.
  @Test
  @StdIo({"99"})
  void midRangeValueCountsAllThreePairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 10: stress the full 500x500 scan and accumulation. N=20160 is the unique
  // positive N with the maximum number of pairs in range: 20.
  @Test
  @StdIo({"20160"})
  void valueWithMaximumNumberOfPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  // Step 11: lower edge of the input domain. N=0 forces A=B, and 1<=B<=A<=500 keeps the
  // whole 500-long diagonal -> 500. Beyond the guaranteed positive domain, but the spec's
  // loop is well-defined here and every double-loop reference solution agrees.
  @Test
  @StdIo({"0"})
  void zeroCountsTheEntireDiagonal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("500");
  }

  // Step 12: the largest representable difference, 500^2 - 1^2 = 249999, has exactly the
  // single boundary pair (500, 1).
  @Test
  @StdIo({"249999"})
  void maximumRepresentableDifferenceHasOneBoundaryPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 13: robustness just past the representable maximum. N=250000 exceeds every
  // achievable A^2-B^2 in range; the program must print 0, not crash or stall.
  @Test
  @StdIo({"250000"})
  void valueAboveMaximumDifferenceYieldsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }
}
