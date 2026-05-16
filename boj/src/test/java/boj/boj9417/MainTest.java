package boj.boj9417;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

// BOJ 9417 "최대 GCD": line 1 is N (number of test cases). Each of the next N
// lines is a whitespace-separated list of integers. For each line, output the
// maximum of gcd(a[i], a[j]) over all pairs i < j — one integer per line, in
// input order.
class MainTest {

  // Step 1: simplest case — one test case, one pair. gcd(2, 4) = 2.
  @Test
  @StdIo({"1", "2 4"})
  void singlePairPlainGcd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // Step 2: a coprime pair — the only pair's gcd is 1, so the answer is 1.
  @Test
  @StdIo({"1", "3 5"})
  void coprimePairYieldsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Step 3: equal numbers — gcd(x, x) = x, so the max pair gcd can be a whole
  // element value, not just a proper divisor.
  @Test
  @StdIo({"1", "6 6"})
  void equalNumbersGcdIsTheValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // Step 4: one number divides the other — gcd is the smaller number.
  @Test
  @StdIo({"1", "4 8"})
  void divisibilityGcdIsSmaller(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Step 5: shared factor where neither divides the other. gcd(12, 18) = 6.
  @Test
  @StdIo({"1", "12 18"})
  void sharedFactorNeitherDivides(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // Step 6: more than two numbers — the optimal pair is non-adjacent and not
  // the first pair. "12 7 8 9 24": gcd(12, 24) = 12 (indices 0 and 4) beats
  // gcd(8, 24) = 8 and every other pair. Forces a full O(K^2) scan.
  @Test
  @StdIo({"1", "12 7 8 9 24"})
  void bestPairIsNonAdjacent(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // Step 7: the answer is NOT from the two largest values. "9 6 13 17": the
  // largest two (13, 17) are coprime; the max gcd 3 comes from (9, 6).
  @Test
  @StdIo({"1", "9 6 13 17"})
  void bestPairIsNotTheLargestValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Step 8: a 1 in the list must not cap the answer — gcd(1, x) = 1, but
  // gcd(5, 10) = 5 still wins.
  @Test
  @StdIo({"1", "1 5 10"})
  void presenceOfOneDoesNotCapTheAnswer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // Step 9: all identical with more than two elements — gcd is that value.
  @Test
  @StdIo({"1", "7 7 7"})
  void allIdenticalElements(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // Step 10: the canonical multi-test-case sample. Verifies multi-line output
  // and that lines are emitted in input order.
  //   "10 20 30 40" -> gcd(20, 40) = 20
  //   "7 5 12 11"    -> all pairwise gcds are 1
  //   "125 15 25"    -> gcd(125, 25) = 25
  @Test
  @StdIo({"3", "10 20 30 40", "7 5 12 11", "125 15 25"})
  void canonicalSampleMultipleTestCases(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("20", "1", "25");
  }

  // Step 11: per-line independence and output ordering with distinct answers,
  // so a swapped order would be detected.
  @Test
  @StdIo({"3", "4 8", "100 75", "9 6"})
  void perLineIndependenceAndOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("4", "25", "3");
  }

  // Step 12: large values. gcd(1000000, 500000) = 500000 dominates while
  // 1000000 and 999999 (consecutive integers) are coprime.
  @Test
  @StdIo({"1", "1000000 999999 500000"})
  void largeValuesGcd(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("500000");
  }

  // Step 13: a fully pairwise-coprime line (distinct primes -> 1) alongside a
  // high-gcd line, to confirm each line is reduced on its own.
  @Test
  @StdIo({"2", "2 3 5 7 11 13", "8 12 16"})
  void allCoprimeLineThenHighGcdLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "8");
  }

  // Step 14: N-loop boundary — exactly N=5 lines must be consumed and printed,
  // no off-by-one. gcd(10, 4) = 2 and a trailing coprime line guard the ends.
  @Test
  @StdIo({"5", "2 4", "3 9", "5 5", "10 4", "7 13"})
  void exactlyNLinesConsumedAndPrinted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("2", "3", "5", "2", "1");
  }

  // Step 15: robustness beyond the guaranteed domain. The spec guarantees at
  // least two numbers per line, but a lone element has no pair; every
  // pair-scanning reference solution leaves the running max at its 0 initial
  // value, so the well-defined output is 0 — the program must not crash.
  @Test
  @StdIo({"1", "42"})
  void singleElementLineHasNoPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }
}
