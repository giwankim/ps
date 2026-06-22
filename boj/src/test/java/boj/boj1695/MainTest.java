package boj.boj1695;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1695 팰린드롬 만들기 ("Make Palindrome") -- the fewest numbers you must insert to turn a sequence
 * into a palindrome.
 *
 * <p>A sequence is a <b>palindrome</b> when it reads the same forwards and backwards: {@code {1}},
 * {@code {1, 2, 1}}, and {@code {1, 2, 2, 1}} are palindromes, while {@code {1, 2, 3}} and
 * {@code {1, 2, 3, 2}} are not. Given a sequence you may insert numbers at any positions (any
 * value, any count); print the minimum number of insertions that makes the whole sequence a
 * palindrome.
 *
 * <p>Constraints: the length {@code N} satisfies {@code 1 <= N <= 5,000}, and every element is an
 * arbitrary value <em>within the {@code int} range</em> -- so negatives, {@code 0},
 * {@code Integer.MIN_VALUE}, and {@code Integer.MAX_VALUE} are all legal inputs. The answer is
 * therefore between {@code 0} (already a palindrome) and {@code N - 1} (every element distinct),
 * comfortably inside an {@code int}.
 *
 * <ul>
 *   <li><b>Base cases and already-palindromic input.</b> A length-one sequence, and any input that
 *       is already a palindrome, need zero insertions. The even-length case additionally exercises
 *       the {@code dp[i+1][j-1]} access when the two indices cross at the center
 *       ({@link #singleElementSequenceNeedsNoInsertions(StdOut)},
 *       {@link #oddLengthPalindromeNeedsNoInsertions(StdOut)},
 *       {@link #evenLengthPalindromeNeedsNoInsertions(StdOut)},
 *       {@link #repeatedElementSequenceIsAlreadyPalindrome(StdOut)}).
 *   <li><b>Comparing values, not indexing by them.</b> Elements span the full {@code int} range, so
 *       a solver must compare values directly. Indexing a table by value, assuming non-negative
 *       input, or parsing into a narrower type all break on negatives and the {@code int} extremes
 *       ({@link #negativeAndZeroValuesFormAPalindrome(StdOut)},
 *       {@link #fullIntRangeValuesAreComparedByValue(StdOut)},
 *       {@link #intExtremesThatDifferNeedOneInsertion(StdOut)}).
 *   <li><b>Insertions = N - LPS.</b> With every element distinct the LPS is a single element, so
 *       the answer is {@code N - 1}; a near-palindrome needs only the one mirror that completes it
 *       ({@link #twoDistinctElementsNeedOneInsertion(StdOut)},
 *       {@link #allDistinctTripleNeedsTwoInsertions(StdOut)},
 *       {@link #nearPalindromeNeedsOneTrailingInsertion(StdOut)},
 *       {@link #everyElementDistinctNeedsLengthMinusOne(StdOut)}).
 *   <li><b>The kept palindrome is a subsequence, not a contiguous block.</b> Matching ends may sit
 *       far apart with mismatches between them, and a matched outer pair reduces the work to its
 *       interior slice. A solver that only collapses adjacent equal ends, or that requires the
 *       palindrome to be contiguous, miscounts these
 *       ({@link #matchingEndsReduceToTheInteriorSubproblem(StdOut)},
 *       {@link #matchingEndsAroundDistinctInteriorNeedsTwoInsertions(StdOut)},
 *       {@link #interiorMismatchInsideMatchingEndsNeedsTwoInsertions(StdOut)}).
 *   <li><b>The {@code O(N^2)} wall.</b> {@code N} reaches 5,000, so a quadratic dynamic program is
 *       required; an exponential top-down search without memoization cannot finish
 *       ({@link #maximumLengthAllDistinctIsHandledWithinTheTimeLimit()},
 *       {@link #maximumLengthMirroredPalindromeIsHandledWithinTheTimeLimit()}).
 * </ul>
 *
 * <p>The hand-picked answers are cross-checked by two independent oracles built from algorithms
 * unlike the intended interval recurrence: an exhaustive longest-palindromic-subsequence search
 * over all {@code 2^N} subsequences for tiny inputs ({@link #bruteForceMinInsertions(int[])}), and
 * the longest-common-subsequence of the sequence with its own reverse for moderate inputs
 * ({@link #lcsOracleMinInsertions(int[])}). The randomized sweeps
 * ({@link #randomTinySequencesMatchTheBruteForceOracle()},
 * {@link #randomModerateSequencesMatchTheLcsOracle()}) drive both.
 */
class MainTest {

  // --- Base cases and already-palindromic input. ---

  // The smallest sequence: a lone element reads the same in both directions, so nothing need be
  // inserted. Pins the base case dp[i][i] = 0 and guards against a solver that demands length >= 2.
  @Test
  @StdIo({"1", "7"})
  void singleElementSequenceNeedsNoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The statement's own odd-length palindrome example. Already symmetric about its center element,
  // so the answer is 0.
  @Test
  @StdIo({"3", "1 2 1"})
  void oddLengthPalindromeNeedsNoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The statement's own even-length palindrome example. Beyond confirming the zero answer, it
  // exercises the dp[i+1][j-1] access at the center, where i+1 > j-1 and the slice is empty -- a
  // common off-by-one when handling even-length palindromes.
  @Test
  @StdIo({"4", "1 2 2 1"})
  void evenLengthPalindromeNeedsNoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Every element identical: already a palindrome, so 0 insertions. Confirms that a run of equal
  // values collapses fully through the matching-ends branch rather than counting any insertion.
  @Test
  @StdIo({"3", "5 5 5"})
  void repeatedElementSequenceIsAlreadyPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Comparing values, not indexing by them. ---

  // Negative values and zero, arranged as a palindrome (-1, 0, -1). A solver that indexes a table
  // by
  // value or assumes non-negative input would index out of bounds or misread; comparing values
  // directly yields the correct 0.
  @Test
  @StdIo({"3", "-1 0 -1"})
  void negativeAndZeroValuesFormAPalindrome(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The int extremes as a palindrome: MAX, MIN, MAX. Exercises parsing both Integer.MAX_VALUE and
  // Integer.MIN_VALUE and comparing them by value. Already symmetric, so 0.
  @Test
  @StdIo({"3", "2147483647 -2147483648 2147483647"})
  void fullIntRangeValuesAreComparedByValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The two int extremes, which differ, so exactly one insertion makes a palindrome (e.g. append
  // the
  // first value). Guards against any narrowing or overflow when the operands are MAX and MIN.
  @Test
  @StdIo({"2", "2147483647 -2147483648"})
  void intExtremesThatDifferNeedOneInsertion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Insertions = N - longest palindromic subsequence. ---

  // The smallest non-palindrome: two distinct elements. Mirroring one of them ({1,2} -> {1,2,1})
  // takes a single insertion.
  @Test
  @StdIo({"2", "1 2"})
  void twoDistinctElementsNeedOneInsertion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The statement's non-palindrome {1, 2, 3}. The longest palindromic subsequence is a single
  // element, so two insertions are required ({1,2,3} -> {1,2,3,2,1}).
  @Test
  @StdIo({"3", "1 2 3"})
  void allDistinctTripleNeedsTwoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // The statement's other non-palindrome {1, 2, 3, 2}. It is one element short of the palindrome
  // {1,2,3,2,1}, so a single trailing insertion suffices. A solver that only checks whole-sequence
  // symmetry rather than the LPS would overcount here.
  @Test
  @StdIo({"4", "1 2 3 2"})
  void nearPalindromeNeedsOneTrailingInsertion(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Five distinct elements: the longest palindromic subsequence is length 1, so the answer is the
  // worst case N - 1 = 4. Pins the upper bound of the answer for a given length.
  @Test
  @StdIo({"5", "1 2 3 4 5"})
  void everyElementDistinctNeedsLengthMinusOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The kept palindrome is a subsequence, not a contiguous block. ---

  // Matching ends wrap a non-palindromic interior {1, 2, 3, 1}: the outer 1s match and reduce the
  // problem to {2, 3}, which needs one insertion. Confirms the matching-ends branch recurses inward
  // rather than declaring the whole thing done.
  @Test
  @StdIo({"4", "1 2 3 1"})
  void matchingEndsReduceToTheInteriorSubproblem(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Matching ends around a fully distinct interior {1, 2, 3, 4, 1}: the outer 1s match, leaving
  // {2, 3, 4}, whose answer is two. A larger hand-checked anchor for the matching-ends recursion.
  @Test
  @StdIo({"5", "1 2 3 4 1"})
  void matchingEndsAroundDistinctInteriorNeedsTwoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // A sequence whose 1s recur at indices 0, 2, 4 but with a stray 3 between {1, 2, 1, 3, 1}. The
  // longest palindromic subsequence has length 3 (e.g. {1,2,1} or {1,3,1}), not contiguous, so the
  // answer is 2. A solver requiring a contiguous palindrome, or only collapsing adjacent equal
  // ends,
  // would miscount.
  @Test
  @StdIo({"5", "1 2 1 3 1"})
  void interiorMismatchInsideMatchingEndsNeedsTwoInsertions(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Randomized cross-checks against the two independent oracles. ---

  // Tiny sequences over a small alphabet, cross-checked against the exhaustive LPS search that
  // obviously enumerates every subsequence. The narrow value range (1..4) packs in repeated values
  // so the random inputs span the full spectrum from already-palindromic to all-distinct.
  @Test
  void randomTinySequencesMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(1695L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rng.nextInt(12); // 1..12 -- small enough for a 2^n search.
      int[] seq = new int[n];
      for (int i = 0; i < n; i++) {
        seq[i] = 1 + rng.nextInt(4); // 1..4
      }
      assertThat(runMain(buildInput(seq)))
          .as("seq=%s", Arrays.toString(seq))
          .isEqualTo(Integer.toString(bruteForceMinInsertions(seq)));
    }
  }

  // Moderate sequences cross-checked against the longest-common-subsequence-with-reverse oracle, a
  // different formulation from the intended interval recurrence. Lengths and alphabets vary so that
  // some instances are dense with matches and others nearly all-distinct.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomModerateSequencesMatchTheLcsOracle() throws IOException {
    Random rng = new Random(16950L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rng.nextInt(200); // 1..200
      int cap = 1 + rng.nextInt(30); // small ceilings create repeated values and shared structure
      int[] seq = new int[n];
      for (int i = 0; i < n; i++) {
        seq[i] = 1 + rng.nextInt(cap);
      }
      assertThat(runMain(buildInput(seq)))
          .as("seq=%s", Arrays.toString(seq))
          .isEqualTo(Integer.toString(lcsOracleMinInsertions(seq)));
    }
  }

  // --- Maximum-size cases: the quadratic dynamic program must fit the time limit. ---

  // The maximum length 5,000 with every element distinct -- the worst case for the recurrence, with
  // no matching ends until the recursion bottoms out. The longest palindromic subsequence is a
  // single element, so the answer is N - 1 = 4,999. An exponential search cannot finish; an O(N^2)
  // program does.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumLengthAllDistinctIsHandledWithinTheTimeLimit() throws IOException {
    int n = 5_000;
    int[] seq = new int[n];
    for (int i = 0; i < n; i++) {
      seq[i] = i + 1; // 1..5000, all distinct
    }
    assertThat(runMain(buildInput(seq))).isEqualTo("4999");
  }

  // The maximum length 5,000 arranged as a perfect mirror (1..2500 then 2500..1). It is already a
  // palindrome, so the answer is 0, but only if equal ends propagate correctly through 2,500 nested
  // matching pairs at full size. Stresses parsing 5,000 numbers and the full-size sweep.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumLengthMirroredPalindromeIsHandledWithinTheTimeLimit() throws IOException {
    int half = 2_500;
    int[] seq = new int[2 * half];
    for (int i = 0; i < half; i++) {
      seq[i] = i + 1; // ascending 1..2500
      seq[2 * half - 1 - i] = i + 1; // mirrored on the far end
    }
    assertThat(runMain(buildInput(seq))).isEqualTo("0");
  }

  /**
   * Independent exhaustive oracle: the minimum insertions equal {@code N} minus the longest
   * palindromic subsequence, and this finds that subsequence by trying all {@code 2^N} subsequences
   * and keeping the longest one that is a palindrome. Transparently correct, so trustworthy only
   * for tiny inputs.
   *
   * @implNote {@code O(2^N * N)} time -- where {@code N} is the sequence length {@code seq.length}.
   *     Callers must keep {@code N} tiny (here at most {@code 12}).
   */
  private static int bruteForceMinInsertions(int[] seq) {
    int n = seq.length;
    int longestPalindrome = 0;
    for (int mask = 1; mask < (1 << n); mask++) {
      int size = Integer.bitCount(mask);
      if (size <= longestPalindrome) {
        continue; // cannot beat the best found so far
      }
      int[] sub = new int[size];
      int k = 0;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
          sub[k++] = seq[i];
        }
      }
      if (isPalindrome(sub)) {
        longestPalindrome = size;
      }
    }
    return n - longestPalindrome;
  }

  private static boolean isPalindrome(int[] a) {
    for (int lo = 0, hi = a.length - 1; lo < hi; lo++, hi--) {
      if (a[lo] != a[hi]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Independent oracle: the longest palindromic subsequence of a sequence is the longest common
   * subsequence of the sequence with its own reverse, so the minimum insertions equal {@code N}
   * minus that LCS length. This is a different formulation from the intended interval recurrence on
   * {@code dp[i][j]}, so agreement across many random instances is real evidence.
   *
   * @implNote {@code O(N^2)} time and space -- where {@code N} is the sequence length
   *     {@code seq.length}.
   */
  private static int lcsOracleMinInsertions(int[] seq) {
    int n = seq.length;
    int[] reversed = new int[n];
    for (int i = 0; i < n; i++) {
      reversed[i] = seq[n - 1 - i];
    }
    int[][] dp = new int[n + 1][n + 1];
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        dp[i][j] = seq[i - 1] == reversed[j - 1]
            ? dp[i - 1][j - 1] + 1
            : Math.max(dp[i - 1][j], dp[i][j - 1]);
      }
    }
    return n - dp[n][n];
  }

  /**
   * Builds BOJ 1695 input: {@code N} on the first line, then the {@code N} values on the second.
   */
  private static String buildInput(int[] seq) {
    StringBuilder sb = new StringBuilder(seq.length * 4 + 16);
    sb.append(seq.length).append('\n');
    for (int i = 0; i < seq.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(seq[i]);
    }
    sb.append('\n');
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
