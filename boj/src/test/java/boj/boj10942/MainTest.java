package boj.boj10942;

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

/**
 * BOJ 10942 팰린드롬? ("Palindrome?") -- a range-palindrome query problem.
 *
 * <p>Hongjun writes N natural numbers on a board (1 &lt;= N &lt;= 2,000, each value at most
 * 100,000) and then asks Myungwoo M questions (1 &lt;= M &lt;= 1,000,000). Each question is a pair
 * (S, E) with 1 &lt;= S &lt;= E &lt;= N and asks whether the subsequence from the S-th to the E-th
 * number (inclusive, 1-indexed) reads the same forwards and backwards. For each question the
 * program prints 1 if that range is a palindrome and 0 otherwise, one answer per line, in the order
 * the questions are asked.
 *
 * <p><b>I/O contract.</b> Line 1: N. Line 2: the N numbers, space-separated. Line 3: M. The next M
 * lines each hold one query "S E". Output is exactly M lines of 1/0. Time limit 1 s, memory 256 MB;
 * with up to a million queries over a length-2,000 array a per-query rescan (O(N*M)) is too slow,
 * so the intended solution precomputes an O(N^2) table {@code dp[s][e] = "is range (s,e) a
 * palindrome"} via the recurrence {@code dp[s][e] = (A[s]==A[e]) && dp[s+1][e-1]} with length-1 and
 * length-2 base cases, then answers every query by table lookup.
 */
class MainTest {

  // --- Base case: a length-1 range is a palindrome no matter what value it holds. ---

  // N = 1 with the lone query (1, 1). A single number trivially reads the same both directions, so
  // the answer is 1. This pins the dp[i][i] = 1 diagonal that every longer case bottoms out on.
  @Test
  @StdIo({"1", "9", "1", "1 1"})
  void singleElementRangeIsAlwaysAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Base case: a length-2 range is a palindrome exactly when its two values are equal. ---

  // [5, 5] over (1, 2). The two-element case is the second base case of the recurrence; equal
  // endpoints make it a palindrome -> 1.
  @Test
  @StdIo({"2", "5 5", "1", "1 2"})
  void lengthTwoRangeWithEqualValuesIsAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [5, 7] over (1, 2). Distinct endpoints break the length-2 base case -> 0. Together with the
  // test
  // above this nails the only two outcomes a length-2 range can have.
  @Test
  @StdIo({"2", "5 7", "1", "1 2"})
  void lengthTwoRangeWithDistinctValuesIsNotAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The smallest recursive case: length 3, where a distinct center is irrelevant. ---

  // [1, 2, 1] over (1, 3). The endpoints match (1 == 1) and the inner range (2) is a length-1
  // palindrome, so the whole thing is a palindrome -> 1. The differing center value 2 must NOT
  // matter; only the mirror positions are compared.
  @Test
  @StdIo({"3", "1 2 1", "1", "1 3"})
  void oddLengthRangeIgnoresItsCenterValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- An even-length palindrome, to exercise the recurrence with no center element. ---

  // [1, 2, 2, 1] over (1, 4): endpoints 1 == 1 and the inner [2, 2] is itself a palindrome -> 1.
  @Test
  @StdIo({"4", "1 2 2 1", "1", "1 4"})
  void evenLengthRangeIsAPalindromeWhenItMirrors(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The two ways a range fails: at the outer pair, or somewhere in the interior. ---

  // [2, 1, 3, 1, 5] over (1, 5). The interior [1, 3, 1] IS a palindrome, but the outer pair differs
  // (2 != 5), so the range is not a palindrome -> 0. Catches an implementation that recurses into
  // the
  // middle yet forgets to compare A[s] against A[e].
  @Test
  @StdIo({"5", "2 1 3 1 5", "1", "1 5"})
  void mismatchAtTheOuterPairAloneBreaksThePalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [1, 2, 3, 4, 1] over (1, 5). The outer pair matches (1 == 1) but the interior [2, 3, 4] is not
  // a
  // palindrome -> 0. Catches the opposite bug: comparing only the endpoints and skipping the
  // recursive inner check.
  @Test
  @StdIo({"5", "1 2 3 4 1", "1", "1 5"})
  void mismatchInTheInteriorAloneBreaksThePalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The judge's official sample, answered line for line. ---

  // S=1,E=3 -> [1,2,1] palindrome (1); S=2,E=5 -> [2,1,3,1] not (0); S=3,E=3 -> [1] palindrome (1);
  // S=5,E=7 -> [1,2,1] palindrome (1). A run that answered only the first query, reordered the
  // answers, or printed a header would fail here.
  @Test
  @StdIo({"7", "1 2 1 3 1 2 1", "4", "1 3", "2 5", "3 3", "5 7"})
  void officialSampleIsAnsweredLineForLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "0", "1", "1");
  }

  // --- Every single-index query is a palindrome, regardless of position or value. ---

  // [4, 4, 1, 9, 9] queried at (1,1), (2,2), (3,3), (4,4), (5,5) -> all 1. Sweeping the whole
  // diagonal in one run guards against an off-by-one that mishandles the first or last index.
  @Test
  @StdIo({"5", "4 4 1 9 9", "5", "1 1", "2 2", "3 3", "4 4", "5 5"})
  void everySingleIndexQueryIsAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "1", "1", "1", "1");
  }

  // --- An all-equal array: every possible sub-range is a palindrome. ---

  // [7, 7, 7, 7, 7, 7] with ranges of assorted lengths and offsets -> all 1. The strongest
  // "positive"
  // guard: if any base case or recurrence step is wrong, at least one of these mixed-length ranges
  // turns into a 0.
  @Test
  @StdIo({"6", "7 7 7 7 7 7", "5", "1 6", "2 5", "3 3", "1 2", "4 6"})
  void everyRangeOfAnAllEqualArrayIsAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "1", "1", "1", "1");
  }

  // --- Answers must follow the input order of the queries, not any sorted or grouped order. ---

  // [1, 2, 3, 2, 1] queried (1,2)->0, (1,5)->1, (2,3)->0. The distinctive 0,1,0 shape would be
  // scrambled by any implementation that sorted queries by S/E or by length before answering.
  @Test
  @StdIo({"5", "1 2 3 2 1", "3", "1 2", "1 5", "2 3"})
  void answersAreEmittedInQueryInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "1", "0");
  }

  // --- Each query gets its own answer line, even when the same range is asked more than once. ---

  // [1, 2, 2, 1] with (1,4), (2,3), (1,4), (1,3), (2,3). The repeated (1,4) and (2,3) must each
  // print
  // again -- the program emits exactly M lines, never deduplicating. This is the heart of the
  // problem: overlapping/repeated queries are why a memoized table beats a rescan, and they must
  // still each be answered. Expected: 1, 1, 1, 0, 1.
  @Test
  @StdIo({"4", "1 2 2 1", "5", "1 4", "2 3", "1 4", "1 3", "2 3"})
  void repeatedAndOverlappingQueriesEachGetTheirOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "1", "1", "0", "1");
  }

  // --- A maximum-size array (N = 2,000) is handled correctly and quickly. ---

  // The array a[i] = min(i, 2001 - i) is the mirror 1,2,...,1000,1000,...,2,1, symmetric about the
  // center, with values well inside the 100,000 cap. A range is a palindrome iff it is centered on
  // that global center (S + E == 2001), so the chosen queries mix centered ranges (palindromes)
  // with
  // off-center and singleton ranges. Expectations come from the independent oracle, and @Timeout
  // guards against an accidentally O(N) per-query scan at full size.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumSizeMirrorArrayIsHandledWithinTheTimeLimit() throws IOException {
    int n = 2000;
    int[] values = new int[n];
    for (int i = 1; i <= n; i++) {
      values[i - 1] = Math.min(i, n + 1 - i);
    }
    int[][] queries = {
      {1, n}, // whole array, centered -> palindrome
      {1, n - 1}, // drops the last element, off-center -> not
      {2, n - 1}, // trims both ends, still centered -> palindrome
      {500, 1501}, // centered (500 + 1501 == 2001) -> palindrome
      {500, 1502}, // off-center by one -> not
      {1000, 1001}, // the two center elements (both 1000) -> palindrome
      {1, 1}, // singleton -> palindrome
      {n, n} // singleton at the far end -> palindrome
    };
    String[] expected = expectedAnswers(values, queries);
    assertThat(runQueries(values, queries)).containsExactly(expected);
  }

  // --- Exhaustive cross-check: every (S, E) range against the independent two-pointer oracle. ---

  // A deterministic length-64 array over a small {1, 2} alphabet is fed every one of its
  // N*(N+1)/2 = 2,080 sub-ranges as queries in a single run. The small alphabet guarantees a rich
  // mix of palindromes and non-palindromes across all lengths. Each expected answer is re-derived
  // by
  // {@link #isPalindrome}, a plain two-pointer scan that shares no logic with the table DP, so any
  // indexing slip, swapped S/E, or broken recurrence surfaces as a single mismatched line.
  @Test
  @Timeout(value = 15, unit = TimeUnit.SECONDS)
  void everySubRangeMatchesTheTwoPointerOracle() throws IOException {
    int n = 64;
    int[] values = new int[n];
    Random random = new Random(42); // fixed seed -> fully reproducible array
    for (int i = 0; i < n; i++) {
      values[i] = random.nextInt(2) + 1; // alphabet {1, 2}
    }
    int[][] queries = new int[n * (n + 1) / 2][2];
    int q = 0;
    for (int s = 1; s <= n; s++) {
      for (int e = s; e <= n; e++) {
        queries[q][0] = s;
        queries[q][1] = e;
        q++;
      }
    }
    String[] expected = expectedAnswers(values, queries);
    assertThat(runQueries(values, queries)).containsExactly(expected);
  }

  /**
   * Independent oracle: decides whether the 1-indexed inclusive range {@code [s, e]} of
   * {@code values} is a palindrome by walking a pointer in from each end, a method entirely
   * separate from the table DP under test so that agreement is a genuine cross-check.
   *
   * @implNote O(e - s) time and O(1) space: the two pointers meet after at most half the range.
   */
  private static boolean isPalindrome(int[] values, int s, int e) {
    int lo = s - 1;
    int hi = e - 1;
    while (lo < hi) {
      if (values[lo] != values[hi]) {
        return false;
      }
      lo++;
      hi--;
    }
    return true;
  }

  /** Maps each query to the oracle's "1"/"0" verdict, preserving query order. */
  private static String[] expectedAnswers(int[] values, int[][] queries) {
    String[] expected = new String[queries.length];
    for (int i = 0; i < queries.length; i++) {
      expected[i] = isPalindrome(values, queries[i][0], queries[i][1]) ? "1" : "0";
    }
    return expected;
  }

  /**
   * Builds the BOJ 10942 input for {@code values} and {@code queries} (N, the numbers, M, then one
   * "S E" line per query) and runs {@link Main} on it, returning stdout split into trimmed
   * non-empty lines. Used where the input is generated rather than a compile-time {@code @StdIo}
   * literal.
   */
  private static String[] runQueries(int[] values, int[][] queries) throws IOException {
    StringBuilder input = new StringBuilder();
    input.append(values.length).append('\n');
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        input.append(' ');
      }
      input.append(values[i]);
    }
    input.append('\n').append(queries.length).append('\n');
    for (int[] query : queries) {
      input.append(query[0]).append(' ').append(query[1]).append('\n');
    }
    return runMainLines(input.toString());
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
