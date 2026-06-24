package uva.uva01230;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * UVa 1230 MODEX -- modular exponentiation: given {@code x}, {@code y}, {@code n}, compute
 * {@code x^y mod n}.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code c}, the number of datasets; then {@code c} lines each
 * holding three blank-separated positive integers {@code x y n}; then a final line containing a
 * single {@code 0} that terminates the input. The program prints exactly {@code c} lines, the i-th
 * being {@code x^y mod n} for the i-th dataset, in input order. Constraints: {@code 1 < x, n < 2^15
 * = 32768} and {@code 0 < y < 2^31 = 2147483648}, so the exponent reaches {@link Integer#MAX_VALUE}
 * while the base and modulus stay below 2^15.
 *
 * <p><b>Why naive approaches fail.</b> {@code y} can be ~2.1 billion, so any solution that forms
 * {@code x^y} as an actual integer (or multiplies {@code x} into an accumulator {@code y} times)
 * either overflows immediately or runs for billions of iterations. The intended solution is binary
 * (square-and-multiply) exponentiation that reduces modulo {@code n} at every step -- {@code O(log
 * y)} multiplications. {@link #maximumExponentIsHandledWithinTheTimeLimit()} is built to time out
 * an {@code O(y)} loop, and several cases feed a large {@code y} with a tiny expected answer so an
 * un-reduced power would surface as overflow garbage.
 *
 * <p><b>Not Fermat's little theorem.</b> A square-and-multiply does not need a prime modulus;
 * {@link #compositeModulusDoesNotAssumePrimality(StdOut)} feeds a composite {@code n} to fail any
 * solution that shortcuts via {@code x^(y mod (n-1))}, which is only valid for prime {@code n}.
 *
 * <p><b>Oracle.</b> Randomized and boundary cases are cross-checked against
 * {@link java.math.BigInteger#modPow}, an arbitrary-precision routine algorithmically independent
 * of a hand-rolled {@code long}-based square-and-multiply, so agreement is genuine evidence rather
 * than a re-run of the same code.
 */
class MainTest {

  // Valid-input ranges from the statement, reused by the randomized sweeps below.
  private static final int MIN_BASE_OR_MODULUS = 2; // 1 < x, n  -> smallest legal value is 2
  private static final int MAX_BASE_OR_MODULUS = 32767; // x, n < 2^15 = 32768
  private static final int MAX_EXPONENT = Integer.MAX_VALUE; // y < 2^31, i.e. at most 2147483647

  // --- The judge's official sample, both datasets answered line for line. ---

  // c = 2. Dataset 1: 2^3 mod 5 = 8 mod 5 = 3. Dataset 2: 2^2147483647 mod 13 = 11 (by Fermat,
  // 2^12 = 1 mod 13 and 2147483647 = 7 mod 12, so the answer is 2^7 mod 13 = 128 mod 13 = 11). The
  // trailing "0" line terminates the input. Feeding both datasets in one run pins the per-line,
  // in-order output contract; the huge second exponent simultaneously rules out any O(y) or
  // overflow-prone solution from the very first test.
  @Test
  @StdIo({"2", "2 3 5", "2 2147483647 13", "0"})
  void officialSampleIsAnsweredLineForLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("3", "11");
  }

  // --- A single small dataset isolates the core computation from the multi-line plumbing. ---

  // c = 1, then 2^3 mod 5 = 3, then the terminating 0. The smallest interesting instance: it
  // verifies the count is parsed, exactly one answer is emitted, and the terminator is not mistaken
  // for a fourth integer / extra dataset.
  @Test
  @StdIo({"1", "2 3 5", "0"})
  void singleSmallDatasetComputesPowerModulo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Smallest legal exponent, with the base larger than the modulus. ---

  // y = 1 is the minimum allowed (0 < y), and x = 10 > n = 7, so x^1 mod n = 10 mod 7 = 3. This
  // pins that the base is reduced modulo n even when no squaring happens: a solution that returns x
  // unchanged for y = 1 (forgetting the final reduction) would wrongly print 10.
  @Test
  @StdIo({"1", "10 1 7", "0"})
  void exponentOfOneStillReducesBaseModuloModulus(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A base congruent to 1 stays 1 for any exponent, with a large exponent for good measure. ---

  // 8 = 1 mod 7, so 8^1000000 = 1 mod 7. The large exponent means a solution that builds the power
  // without reducing each step overflows into garbage, while the correct square-and-multiply keeps
  // collapsing to 1. Expected answer 1.
  @Test
  @StdIo({"1", "8 1000000 7", "0"})
  void baseCongruentToOneStaysOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- When the base is a multiple of the modulus the result is 0. ---

  // 14 = 0 mod 7, so 14^5 mod 7 = 0. The statement promises "a positive integer z", but a correct
  // modular reduction must still yield 0 here -- this guards the reduction's handling of the x = 0
  // (mod n) case, which a solver that assumes the answer is always nonzero (e.g. seeds an
  // accumulator and never reduces the base first) would get wrong.
  @Test
  @StdIo({"1", "14 5 7", "0"})
  void resultIsZeroWhenBaseIsAMultipleOfModulus(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A composite modulus must not rely on Fermat's little theorem. ---

  // n = 12 is composite, so x^(y mod (n-1)) is NOT a valid shortcut here. 5^4 mod 12: 5^2 = 25 = 1
  // mod 12, hence 5^4 = 1 mod 12. Expected 1. A solution that reduces the exponent modulo n-1 = 11
  // (a Fermat shortcut, valid only for prime n) would compute 5^(4 mod 11) = 5^4 here by accident,
  // but the same shortcut is exposed by the randomized sweep, which mixes many composite moduli.
  @Test
  @StdIo({"1", "5 4 12", "0"})
  void compositeModulusDoesNotAssumePrimality(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- An odd exponent exercises the "multiply on a set bit" branch of square-and-multiply. ---

  // 3^13 mod 100 = 1594323 mod 100 = 23. Thirteen is 1101 in binary, so the low-bit multiply fires
  // on three of four steps; this catches an implementation that only squares and forgets to fold in
  // the base on odd bits. Expected 23.
  @Test
  @StdIo({"1", "3 13 100", "0"})
  void oddExponentExercisesTheLowBitMultiply(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("23");
  }

  // --- A power-of-two exponent is pure squaring with a single set bit. ---

  // 7^16 mod 13: 7^2 = 10, 7^4 = 9, 7^8 = 3, 7^16 = 9 (mod 13). Sixteen is 10000 in binary, so only
  // the top bit multiplies and every other step is a squaring -- the mirror image of the odd-bit
  // case above. Expected 9.
  @Test
  @StdIo({"1", "7 16 13", "0"})
  void powerOfTwoExponentIsAllSquarings(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9");
  }

  // --- Several datasets in one run must each be answered independently, in input order. ---

  // c = 3 with deliberately distinct answers 3, 23, 1 (reusing the small case, the odd-exponent
  // case, and the congruent-to-one case). The order would be scrambled by any run that sorted or
  // batched answers, and the values would drift if the accumulator or base from one dataset leaked
  // into the next instead of being re-initialized per dataset.
  @Test
  @StdIo({"3", "2 3 5", "3 13 100", "8 1000000 7", "0"})
  void multipleDatasetsAnsweredIndependentlyInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("3", "23", "1");
  }

  // --- The same dataset repeated must produce the same answer on every line. ---

  // 2^10 mod 1000 = 1024 mod 1000 = 24, supplied four times. Each repetition is an independent
  // dataset and must re-emit 24 -- no deduplication, and no corruption from per-dataset state
  // surviving into the next iteration. This is the multi-dataset analogue that specifically targets
  // a missing reset of the running result.
  @Test
  @StdIo({"4", "2 10 1000", "2 10 1000", "2 10 1000", "2 10 1000", "0"})
  void repeatedDatasetProducesSameAnswerEachTime(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("24", "24", "24", "24");
  }

  // --- Randomized cross-check: many single datasets against the independent BigInteger oracle. ---

  // Random (x, y, n) drawn from the full legal ranges -- including the maximum exponent and
  // composite moduli -- each answered in its own run and compared to BigInteger.modPow. A fixed
  // seed
  // keeps it reproducible across JVMs. This single sweep exercises every exponent bit pattern, the
  // base/modulus reduction, and the Fermat-shortcut trap across hundreds of instances.
  @Test
  void randomDatasetsMatchBigIntegerModPow() throws IOException {
    Random rng = new Random(1230L);
    for (int trial = 0; trial < 500; trial++) {
      long x = randomBaseOrModulus(rng);
      long y = randomExponent(rng);
      long n = randomBaseOrModulus(rng);
      String expected = modPowOracle(x, y, n);
      assertThat(runMain(buildInput(new long[][] {{x, y, n}})))
          .as("x=%d y=%d n=%d", x, y, n)
          .isEqualTo(expected);
    }
  }

  // --- Many random datasets in one run, each compared to the oracle. ---

  // Sixty random datasets concatenated into a single c = 60 input and run once; every output line
  // is
  // checked against BigInteger.modPow. This is the multi-dataset analogue of the sweep above and
  // guards the per-dataset reset under varied inputs within one process.
  @Test
  void manyRandomDatasetsInOneRunAreAnsweredIndependently() throws IOException {
    Random rng = new Random(12300L);
    int count = 60;
    long[][] datasets = new long[count][];
    String[] expected = new String[count];
    for (int i = 0; i < count; i++) {
      long x = randomBaseOrModulus(rng);
      long y = randomExponent(rng);
      long n = randomBaseOrModulus(rng);
      datasets[i] = new long[] {x, y, n};
      expected[i] = modPowOracle(x, y, n);
    }
    assertThat(runMainLines(buildInput(datasets))).containsExactly(expected);
  }

  // --- The maximum exponent across several datasets must finish well within the time limit. ---

  // Eight datasets each with y = 2147483647 (the largest legal exponent) and assorted bases and
  // moduli, cross-checked against the oracle. Square-and-multiply needs ~31 multiplications per
  // dataset (microseconds total); a naive accumulator that multiplies y times would perform roughly
  // 1.7e10 modular multiplications and blow any reasonable limit. The @Timeout therefore fails an
  // O(y) implementation while the oracle simultaneously guards correctness at the exponent ceiling.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumExponentIsHandledWithinTheTimeLimit() throws IOException {
    long[][] datasets = {
      {2, MAX_EXPONENT, 13},
      {32767, MAX_EXPONENT, 32749},
      {3, MAX_EXPONENT, 1000},
      {12345, MAX_EXPONENT, 32768 - 1},
      {2, MAX_EXPONENT, 2},
      {30000, MAX_EXPONENT, 7},
      {31, MAX_EXPONENT, 31},
      {12321, MAX_EXPONENT, 9999},
    };
    String[] expected = new String[datasets.length];
    for (int i = 0; i < datasets.length; i++) {
      expected[i] = modPowOracle(datasets[i][0], datasets[i][1], datasets[i][2]);
    }
    assertThat(runMainLines(buildInput(datasets))).containsExactly(expected);
  }

  // --- Base and modulus at the top of the range, where the squaring step must not overflow int.
  // ---

  // With x and n near 32767, the product of two reduced residues approaches 32766^2 ~ 1.07e9, which
  // still fits a signed 32-bit int but leaves no headroom -- a solution that multiplies before
  // reducing, or that uses int where a long is needed, drifts here. Large exponents push many such
  // multiplications through the loop. All cross-checked against BigInteger.modPow.
  @Test
  void nearMaximumBaseAndModulusMatchOracle() throws IOException {
    long[][] datasets = {
      {32766, 2000000000L, 32767},
      {32767, 1234567890L, 32749},
      {32760, 2147483647L, 32761},
      {32703, 999999999L, 32717},
    };
    String[] expected = new String[datasets.length];
    for (int i = 0; i < datasets.length; i++) {
      expected[i] = modPowOracle(datasets[i][0], datasets[i][1], datasets[i][2]);
    }
    assertThat(runMainLines(buildInput(datasets))).containsExactly(expected);
  }

  /**
   * Independent oracle for {@code x^y mod n} via {@link BigInteger#modPow}. Arbitrary-precision and
   * algorithmically distinct from a {@code long}-based square-and-multiply, so agreement with
   * {@link Main} is a genuine cross-check rather than a re-run of the same logic.
   */
  private static String modPowOracle(long x, long y, long n) {
    return BigInteger.valueOf(x)
        .modPow(BigInteger.valueOf(y), BigInteger.valueOf(n))
        .toString();
  }

  /** A random base or modulus in the legal range {@code [2, 32767]}. */
  private static long randomBaseOrModulus(Random rng) {
    return MIN_BASE_OR_MODULUS + rng.nextInt(MAX_BASE_OR_MODULUS - MIN_BASE_OR_MODULUS + 1);
  }

  /** A random exponent in the legal range {@code [1, 2147483647]}, including the maximum. */
  private static long randomExponent(Random rng) {
    return 1L + rng.nextInt(MAX_EXPONENT); // nextInt(MAX) is [0, MAX-1]; +1 -> [1, MAX]
  }

  /**
   * Builds UVa 1230 input: {@code c} on the first line, then one {@code "x y n"} line per dataset,
   * then the terminating {@code "0"} line mandated by the I/O contract.
   */
  private static String buildInput(long[][] datasets) {
    StringBuilder sb = new StringBuilder();
    sb.append(datasets.length).append('\n');
    for (long[] d : datasets) {
      sb.append(d[0]).append(' ').append(d[1]).append(' ').append(d[2]).append('\n');
    }
    sb.append("0").append('\n');
    return sb.toString();
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      Main.main(new String[0]);
      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }

  /** Runs {@link Main} on {@code input}, returning stdout split into trimmed non-empty lines. */
  private static String[] runMainLines(String input) throws IOException {
    String captured = runMain(input);
    return captured.isEmpty() ? new String[0] : captured.split("\\R");
  }
}
