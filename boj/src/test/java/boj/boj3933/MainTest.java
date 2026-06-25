package boj.boj3933;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 3933 라그랑주의 네 제곱수 정리 ("Lagrange's Four-Square Theorem") -- ICPC Asia Regional 2003 (Aizu),
 * problem B.
 *
 * <p>For each query {@code n}, count how many ways {@code n} can be written as a sum of <b>at most
 * four positive squares</b> (squares of positive integers). Order does not matter, so {@code 3^2 +
 * 4^2} and {@code 4^2 + 3^2} are the <em>same</em> representation and are counted once. "At most
 * four" means exactly one, two, three, or four squares -- a zero term is never used, but counting
 * "at most four positive squares" is equivalent to counting "exactly four non-negative squares"
 * (pad any shorter representation with zeros), so the two framings yield identical counts.
 *
 * <p>The statement's worked example is {@code n = 25}, which has exactly three representations:
 * {@code 5^2}, {@code 3^2 + 4^2}, and {@code 1^2 + 2^2 + 2^2 + 4^2}.
 *
 * <p><b>I/O contract.</b> Input is at most 255 lines, each a single positive integer less than
 * {@code 2^15} (so {@code 1 <= n <= 32767}), followed by a line containing a single {@code 0} that
 * terminates the stream and is <em>not</em> a query. For each query the program prints the count on
 * its own line, in order, and nothing else. Time limit 2 s, memory 128 MB; the intended solution
 * precomputes a {@code dp[value][count]} table once and answers every query by table lookup.
 *
 * <p>The expected counts below come from two independent sources that agree: the four values quoted
 * by the judge ({@code 1 -> 1}, {@code 25 -> 3}, {@code 2003 -> 48}, {@code 211 -> 7}, {@code 20007
 * -> 738}) and an exhaustive enumeration oracle ({@link #bruteForceCount(int)}) that simply tries
 * every non-decreasing tuple of one to four positive squares.
 * {@link #everyValueUpToTwoHundredFiftyMatchesExhaustiveEnumeration()} sweeps a dense contiguous
 * range through that oracle, so any miscounted length, double-counted permutation, or missing
 * single-square case surfaces immediately.
 */
class MainTest {

  // --- The lone terminator: 0 ends the stream before any query, so nothing is printed. ---

  // A program that treats the sentinel 0 as a query (and prints a count for it, e.g. 0) or that
  // prints a header would fail here. This is the only spec an empty do-nothing stub satisfies.
  @Test
  @StdIo({"0"})
  void terminatorOnlyInputProducesNoOutput(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString()).isEmpty();
  }

  // --- The smallest query: 1 is itself a single square, 1^2. ---

  // n = 1 -> 1. The only representation is the single square 1^2; there is no way to use two or
  // more
  // positive squares (the smallest two-square sum is 1^2 + 1^2 = 2). Pins the base of the table and
  // the "a lone square counts" case.
  @Test
  @StdIo({"1", "0"})
  void smallestInputCountsTheSingleOneSquare(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The first integer with two representations: it must count BOTH the bare square and the
  //     all-ones expansion. ---

  // n = 4 -> 2. The two representations are the single square 2^2 and the four-square sum 1^2 + 1^2
  // +
  // 1^2 + 1^2; there is no valid two- or three-square sum (3 and 2 are not squares). This is the
  // sharpest small guard that the count spans the whole range "1..4 squares": dropping the
  // single-square case OR the four-square case each leaves the wrong answer 1.
  @Test
  @StdIo({"4", "0"})
  void fourIsTheSmallestIntegerWithTwoRepresentations(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The statement's own worked example. ---

  // n = 25 -> 3: 5^2, 3^2 + 4^2, and 1^2 + 2^2 + 2^2 + 4^2. The classic trap the statement warns
  // about is counting 3^2 + 4^2 and 4^2 + 3^2 separately; a solution that does so would over-count.
  @Test
  @StdIo({"25", "0"})
  void statementExampleTwentyFiveHasThreeRepresentations(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Judge sample values beyond the hand-checkable range, pinning the recurrence. ---

  // n = 211 -> 7. Small enough to stay fast, large enough that the count is no longer enumerable by
  // eye, so an off-by-one in the table transition first shows up around here.
  @Test
  @StdIo({"211", "0"})
  void officialSampleTwoHundredElevenHasSevenRepresentations(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // n = 2003 -> 48.
  @Test
  @StdIo({"2003", "0"})
  void officialSampleTwoThousandThreeHasFortyEightRepresentations(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("48");
  }

  // n = 20007 -> 738. A large query whose count is in the hundreds; a table that is too small, or
  // an
  // accumulation that overflows, would miss this.
  @Test
  @StdIo({"20007", "0"})
  void officialSampleTwentyThousandSevenHasSevenHundredThirtyEight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("738");
  }

  // --- Multiple queries in one run must be answered, in order, each on its own line. ---

  // The judge's full sample fed as a single session. A loop that handled only the first query,
  // printed the terminating 0, or emitted the answers out of order would fail here.
  @Test
  @StdIo({"1", "25", "2003", "211", "20007", "0"})
  void everyOfficialSampleLineIsAnsweredInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "3", "48", "7", "738");
  }

  // --- The maximum query value (just under 2^15) clears the time limit and counts correctly. ---

  // n = 32767 -> 868. The largest legal input. It forces the full-size precomputed table and a
  // count
  // far too large to enumerate by hand; the value is cross-checked by the independent oracle below.
  // @Timeout guards against an accidentally exponential per-query search.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  @StdIo({"32767", "0"})
  void maxValueIsCountedWithinTheTimeLimit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("868");
  }

  // --- Exhaustive cross-check against the independent enumeration oracle. ---

  // Every value from 1 to 250 fed as a single session must match the brute-force oracle line for
  // line. One dense sweep re-derives the counting rule from scratch (non-decreasing tuples of one
  // to
  // four positive squares) across a contiguous range, catching any rule the table enforces
  // incorrectly. Kept within the 255-line input contract so it also exercises the batch loop.
  @Test
  @Timeout(value = 20, unit = TimeUnit.SECONDS)
  void everyValueUpToTwoHundredFiftyMatchesExhaustiveEnumeration() throws IOException {
    int limit = 250;
    StringBuilder input = new StringBuilder();
    String[] expected = new String[limit];
    for (int n = 1; n <= limit; n++) {
      input.append(n).append('\n');
      expected[n - 1] = Integer.toString(bruteForceCount(n));
    }
    input.append("0\n");
    assertThat(runMainLines(input.toString())).containsExactly(expected);
  }

  /**
   * Independent oracle for the number of representations of {@code n} as a sum of at most four
   * positive squares, by brute-force enumeration rather than the table DP, so agreement is a
   * genuine cross-check. Enumerates every non-decreasing tuple {@code 1 <= a <= b <= c <= d} of
   * bases whose squares sum to {@code n}, for tuple lengths one through four, and tallies the
   * matches. The non-decreasing requirement is exactly what makes a multiset (an order-independent
   * representation) be counted once.
   *
   * @implNote {@code O(n^1.5)} time, where {@code n} is the queried integer: the three outer base
   *     loops are bounded by the running partial sum and the innermost length-four term is found in
   *     amortized constant time because the partial sum increases monotonically in {@code d}. Every
   *     partial sum is bounded by {@code n <= 32767}, so plain {@code int} arithmetic never
   *     overflows.
   */
  private static int bruteForceCount(int n) {
    int count = 0;
    for (int a = 1; a * a <= n; a++) {
      int sumA = a * a;
      if (sumA == n) {
        count++; // exactly one square
      }
      for (int b = a; sumA + b * b <= n; b++) {
        int sumAb = sumA + b * b;
        if (sumAb == n) {
          count++; // exactly two squares
        }
        for (int c = b; sumAb + c * c <= n; c++) {
          int sumAbc = sumAb + c * c;
          if (sumAbc == n) {
            count++; // exactly three squares
          }
          for (int d = c; sumAbc + d * d <= n; d++) {
            if (sumAbc + d * d == n) {
              count++; // exactly four squares
              break; // the sum only grows with d, so no larger d can match
            }
          }
        }
      }
    }
    return count;
  }

  /** Runs {@link Main} on {@code input}, returning stdout split into trimmed non-empty lines. */
  private static String[] runMainLines(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      Main.main(new String[0]);
      String captured = out.toString(StandardCharsets.UTF_8).trim();
      return captured.isEmpty() ? new String[0] : captured.split("\\R");
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
