package boj.boj7453;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/** BOJ 7453 합이 0인 네 정수 (Four Integers With Sum Zero). */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // The canonical 6-row matrix has exactly five sum-zero quadruples -> "5".
  @Test
  @StdIo({
    "6",
    "-45 22 42 -16",
    "-41 -27 56 30",
    "-36 53 -37 77",
    "-36 30 -75 -46",
    "26 -38 -10 62",
    "-32 -54 -6 45"
  })
  void officialSampleCountsFiveZeroSumQuadruples(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Minimum size (n = 1): a single element per array, so there is exactly one quadruple. ---

  // The lone quadruple is all zeros, which sums to zero -> "1".
  @Test
  @StdIo({"1", "0 0 0 0"})
  void singleAllZeroQuadrupleCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Nonzero elements that still cancel (2 + 3 - 4 - 1 = 0) must be counted -> "1".
  @Test
  @StdIo({"1", "2 3 -4 -1"})
  void singleNonZeroQuadrupleSummingToZeroCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The lone quadruple does not sum to zero (1 + 2 + 3 + 4 = 10) -> "0".
  @Test
  @StdIo({"1", "1 2 3 4"})
  void singleQuadrupleNotSummingToZeroCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- No solution: every value positive, so no quadruple can reach zero. ---

  // All elements are 1 or 2, so the minimum possible sum is 4 -> "0".
  @Test
  @StdIo({"2", "1 1 1 1", "2 2 2 2"})
  void noQuadrupleSumsToZeroCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All zeros: every one of the n^4 quadruples sums to zero. Exercises the count's
  // multiplicative nature and the long-sized accumulator. ---

  // n = 2 -> 2^4 = 16 quadruples.
  @Test
  @StdIo({"2", "0 0 0 0", "0 0 0 0"})
  void allZerosWithTwoElementsCountsSixteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // n = 3 -> 3^4 = 81 quadruples (an odd size guards against off-by-one pairing bugs).
  @Test
  @StdIo({"3", "0 0 0 0", "0 0 0 0", "0 0 0 0"})
  void allZerosWithThreeElementsCountsEightyOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("81");
  }

  // --- Uniqueness: widely spread magnitudes admit exactly one cancelling quadruple. ---

  // Only the index-0 elements cancel (1 + 10 + 100 - 111 = 0); every other combination uses a large
  // positive index-1 value that cannot be offset -> "1".
  @Test
  @StdIo({"2", "1 10 100 -111", "1000 10000 100000 1000000"})
  void uniqueQuadrupleAmongSpreadOutValuesCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Multiplicity: the count must multiply matching pair-sums, not merely detect existence. ---

  // A = [0,0], B = [1,-1], C = [0,0], D = [1,-1]. The sum reduces to B[b] + D[d], which is zero for
  // two of the four (b,d) pairs; each pairs with two free (a,c) choices -> 2 * 4 = 8. A solution
  // that counts existence (1) instead of multiplicity would report far fewer.
  @Test
  @StdIo({"2", "0 1 0 1", "0 -1 0 -1"})
  void duplicatePairSumsMultiplyAcrossArrays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Magnitude bound: values at +/-2^28 (= 268435456), the largest the spec allows. ---

  // Every value is +/-2^28, so a quadruple sums to zero iff it has two positives and two negatives:
  // C(4,2) = 6 such quadruples. Confirms large magnitudes and signs are handled without overflow in
  // the pair sums (which can reach +/-2^29).
  @Test
  @StdIo({
    "2",
    "268435456 268435456 -268435456 -268435456",
    "-268435456 -268435456 268435456 268435456"
  })
  void extremeMagnitudeValuesAtConstraintBoundCountSix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Long overflow: 216^4 = 2,176,782,336 just exceeds Integer.MAX_VALUE (2,147,483,647), so an
  // int accumulator overflows to a negative value. The smallest all-zeros size that crosses the
  // boundary, isolating the long-vs-int decision. ---

  @Test
  void countExceedingIntRangeRequiresLong() throws IOException {
    assertThat(runMain(allZerosInput(216))).isEqualTo("2176782336");
  }

  // --- Performance: brute force is O(n^4); the intended meet-in-the-middle is ~O(n^2 log n). ---

  // n = 1000 -> 1000^4 = 10^12 quadruples. A brute-force solution would need 10^12 iterations and
  // blow the timeout, while the meet-in-the-middle approach returns almost instantly.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeInputRulesOutBruteForce() throws IOException {
    assertThat(runMain(allZerosInput(1000))).isEqualTo("1000000000000");
  }

  // Maximum size n = 4000 -> 4000^4 = 2.56 * 10^14 quadruples. Stresses both the upper input bound
  // and the largest possible answer; the AB/CD arrays each hold 16,000,000 sums.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumSizeInputWithinTimeLimit() throws IOException {
    assertThat(runMain(allZerosInput(4000))).isEqualTo("256000000000000");
  }

  // --- Randomized cross-check: compare Main against an O(n^4) brute-force oracle over many small,
  // duplicate- and zero-heavy inputs. Samples sign and multiplicity combinations exhaustively
  // enough to catch bugs the hand-written cases miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(42);
    for (int trial = 0; trial < 200; trial++) {
      int n = 1 + rnd.nextInt(8);
      int[] a = randomArray(n, rnd);
      int[] b = randomArray(n, rnd);
      int[] c = randomArray(n, rnd);
      int[] d = randomArray(n, rnd);
      String input = inputFor(a, b, c, d);
      String expected = Long.toString(bruteForceCount(a, b, c, d));
      assertThat(runMain(input)).as("input=%n%s", input).isEqualTo(expected);
    }
  }

  private static int[] randomArray(int n, Random rnd) {
    int[] values = new int[n];
    for (int i = 0; i < n; i++) {
      values[i] = rnd.nextInt(7) - 3; // small range [-3, 3] to force frequent cancellations
    }
    return values;
  }

  // Reference O(n^4) count, correct but far too slow for the judge; trustworthy only for tiny n.
  private static long bruteForceCount(int[] a, int[] b, int[] c, int[] d) {
    long count = 0;
    for (int x : a) {
      for (int y : b) {
        for (int z : c) {
          for (int w : d) {
            if ((long) x + y + z + w == 0) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }

  private static String allZerosInput(int n) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append("0 0 0 0").append('\n');
    }
    return sb.toString();
  }

  private static String inputFor(int[] a, int[] b, int[] c, int[] d) {
    int n = a.length;
    StringBuilder sb = new StringBuilder();
    sb.append(n).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(a[i])
          .append(' ')
          .append(b[i])
          .append(' ')
          .append(c[i])
          .append(' ')
          .append(d[i])
          .append('\n');
    }
    return sb.toString();
  }

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
}
