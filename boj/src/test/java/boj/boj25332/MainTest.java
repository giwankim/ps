package boj.boj25332;

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
 * BOJ 25332 수들의 합 8 (Sum of Numbers 8) -- Gold III, 쇼미더코드 2022 round 2, problem C.
 *
 * <p>Two integer sequences {@code A[1..N]} and {@code B[1..N]} of equal length are given. Count the
 * number of index pairs {@code (i, j)} with {@code 1 <= i <= j <= N} whose interval sums are equal:
 * {@code A[i] + ... + A[j] == B[i] + ... + B[j]}. Distinct index ranges are counted separately even
 * when the matching sum coincides.
 *
 * <p>Input: line 1 is {@code N}; line 2 is the {@code N} space-separated values of {@code A}; line
 * 3 is the {@code N} space-separated values of {@code B}. Output: a single integer, the count of
 * equal pairs (which can be {@code 0}).
 *
 * <p>Constraints (from the official statement, recovered from an archived problem PDF while
 * acmicpc.net was down): {@code 1 <= N <= 2 * 10^5}; {@code |A| = |B| = N}; {@code 1 <= A_i, B_i <=
 * 10^4}; time limit 1 s, memory 512 MB. Because the elements are positive but {@code A} and
 * {@code B} are independent, {@code d[k] = A[k] - B[k]} can be negative and prefix sums can dip
 * below zero -- the count map must handle negative keys.
 *
 * <p><b>The overflow trap is in the answer, not the prefix.</b> The running prefix diff stays
 * within {@code +/- N * (10^4 - 1) = +/- 1,999,800,000}, which fits a signed 32-bit {@code int}
 * (limit {@code 2,147,483,647}) -- so, unlike BOJ 3673, the prefix accumulator does <em>not</em>
 * require a {@code long}. The <em>answer</em>, however, can reach {@code C(N+1, 2) = 200,000 *
 * 200,001 / 2 = 20,000,100,000}, far beyond {@code int}; the problem statement explicitly notes
 * that the count needs a 64-bit type (Java: {@code long}).
 * {@link #maxSizeIdenticalSequencesCountExceedsIntRange()} pins this down: an {@code int} counter
 * would overflow to a wrong (often negative) value.
 */
class MainTest {

  // --- Smallest input (N = 1): the lone window is the single element, matching iff A[1] == B[1].

  // A=[5], B=[5]: the only window [1,1] has equal sums (5 == 5).
  @Test
  @StdIo({"1", "5", "5"})
  void singleElementEqualCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A=[5], B=[3]: the only window [1,1] has 5 != 3.
  @Test
  @StdIo({"1", "5", "3"})
  void singleElementDifferentCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // A=[10000], B=[10000]: equal at the maximum element value 10^4 -- exercises the read path at the
  // upper value bound.
  @Test
  @StdIo({"1", "10000", "10000"})
  void singleElementAtValueUpperBoundEqualCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The empty-prefix (start-anchored) case: a window beginning at index 1. ---

  // A=[1,2], B=[2,1]: d=[-1,1], prefix D=[0,-1,0]. Windows are [1,1] (1 vs 2), [1,2] (3 vs 3,
  // equal), [2,2] (2 vs 1). Only [1,2] matches, and it starts at index 1, so it is found only if
  // the empty prefix (D[0]=0, i.e. count[0]=1) is seeded. This is the single most common bug.
  @Test
  @StdIo({"2", "1 2", "2 1"})
  void wholeArrayWindowIsCountedViaTheEmptyPrefix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A=[2,1,3,1], B=[1,2,1,3]: d=[1,-1,2,-2], prefix D=[0,1,0,2,0]. Value 0 sits at indices {0,2,4}
  // -> C(3,2)=3 windows: [1,2] and [1,4] (both start-anchored, each pairing with the empty prefix)
  // and [3,4] (interior). Two distinct start-anchored windows must each be counted.
  @Test
  @StdIo({"4", "2 1 3 1", "1 2 1 3"})
  void multipleWindowsShareTheEmptyPrefixValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- An interior window (touching neither end) must still be found. ---

  // A=[2,3,1,2], B=[1,1,3,1]: d=[1,2,-2,1], prefix D=[0,1,3,1,2]. The only repeat is value 1 at
  // indices {1,3}, giving the single window [2,3] -- it starts at index 2 and ends at index 3,
  // touching neither boundary, so it is found purely by two interior prefix sums sharing a value
  // (D[1]=D[3]=1), never involving the empty prefix.
  @Test
  @StdIo({"4", "2 3 1 2", "1 1 3 1"})
  void interiorWindowMatchingIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- No window matches: every prefix value is distinct, so the answer is 0. ---

  // A=[1,2,3], B=[2,3,5]: d=[-1,-1,-2], prefix D=[0,-1,-2,-4], all distinct -> 0.
  @Test
  @StdIo({"3", "1 2 3", "2 3 5"})
  void noMatchingWindowCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Identical sequences => every one of the N(N+1)/2 windows matches. ---

  // A=B=[2,2,2]: d=[0,0,0], every prefix is 0, so all 3*4/2 = 6 windows match -> C(4,2) = 6.
  @Test
  @StdIo({"3", "2 2 2", "2 2 2"})
  void identicalSequencesCountEveryWindow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- A prefix value shared by three indices contributes C(3,2)=3, not 2: counting is pairwise.

  // A=[3,1,5], B=[2,2,5]: d=[1,-1,0], prefix D=[0,1,0,0]. Value 0 sits at indices {0,2,3}, yielding
  // C(3,2)=3 windows: [1,2], [1,3], and [3,3]. A solution that only pairs consecutive equal
  // prefixes, instead of counting all pairs in a bucket, would return 2.
  @Test
  @StdIo({"3", "3 1 5", "2 2 5"})
  void repeatedPrefixValueUsesPairwiseCombination(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Negative diffs (B[k] > A[k]) push the prefix below zero; negative keys must bucket too. ---

  // A=[1,1,5], B=[4,1,2]: d=[-3,0,3], prefix D=[0,-3,-3,0]. Value -3 at {1,2} gives the interior
  // window [2,2]; value 0 at {0,3} gives the whole-array window [1,3] -> 2. The prefix dips to -3
  // before returning, so a map keyed on a non-negative type (or one mishandling negative keys)
  // would miscount.
  @Test
  @StdIo({"3", "1 1 5", "4 1 2"})
  void negativeRunningPrefixFormsItsOwnBucket(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- A reversed-pair shape: A ascending against B descending. ---

  // A=[1,2,3,4,5], B=[5,4,3,2,1]: d=[-4,-2,0,2,4], prefix D=[0,-4,-6,-6,-4,0]. Equal-value buckets
  // are {0,5}, {1,4} (value -4), and {2,3} (value -6), each C(2,2)=1 -> 3. The three matching
  // windows are the whole array [1,5] (15==15), the centre band [2,4] (9==9), and the midpoint
  // [3,3] (3==3).
  @Test
  @StdIo({"5", "1 2 3 4 5", "5 4 3 2 1"})
  void ascendingVersusDescendingCountsThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The four published samples, verbatim from the official statement. ---

  // Sample 1: A=[1,2,3], B=[1,3,2]. The matching (i,j) are (1,1) [1==1], (2,3) [5==5], and (1,3)
  // [6==6] -> 3.
  @Test
  @StdIo({"3", "1 2 3", "1 3 2"})
  void officialSample1CountsThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // Sample 2: A=[1,2,3,4,5], B=[4,5,6,7,8]. Every B[k] exceeds A[k] by 3, so d=[-3,-3,-3,-3,-3] and
  // every prefix is distinct -> 0.
  @Test
  @StdIo({"5", "1 2 3 4 5", "4 5 6 7 8"})
  void officialSample2CountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Sample 3: A=B=[23,13,31,17,29,19]. Identical sequences -> all C(7,2) = 6*7/2 = 21 windows
  // match.
  @Test
  @StdIo({"6", "23 13 31 17 29 19", "23 13 31 17 29 19"})
  void officialSample3IdenticalCountsTwentyOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  // Sample 4: A=[1,2,1], B=[1,1,1]. d=[0,1,0], prefix D=[0,0,1,1]. Buckets {0,1} and {2,3} each
  // give
  // one window -> the matches [1,1] (1==1) and [3,3] (1==1) -> 2.
  @Test
  @StdIo({"3", "1 2 1", "1 1 1"})
  void officialSample4CountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Count-overflow boundary + performance: maximum-size identical sequences. ---

  // N = 200,000 with A = B (all 1s): every window matches, so the answer is the maximum possible
  // C(N+1,2) = 200,000 * 200,001 / 2 = 20,000,100,000, which overflows a signed int and forces a
  // long count (as the problem statement's note requires). Also guards O(N): an O(N^2) pair scan
  // over 200,000 elements would not finish within the 1 s judge limit.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeIdenticalSequencesCountExceedsIntRange() throws IOException {
    int n = 200_000;
    int[] a = filled(n, 1);
    int[] b = filled(n, 1);
    long expected = (long) n * (n + 1) / 2;
    assertThat(runMain(buildInput(a, b))).isEqualTo(Long.toString(expected));
  }

  // --- Performance with a large, non-trivial answer that is not the degenerate all-match case. ---

  // N = 200,000: first half A=10^4/B=1 (d = +9999), second half A=1/B=10^4 (d = -9999). The prefix
  // climbs to 100,000 * 9999 = 999,900,000 at the midpoint, then mirrors back down, so each value
  // below the peak occurs exactly twice -> N/2 = 100,000 matching windows. Exercises O(N) time at
  // full scale with a non-degenerate answer; the peak prefix still fits a 32-bit int, matching the
  // constraint analysis.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeMirroredPrefixCountsHalfN() throws IOException {
    int n = 200_000;
    int half = n / 2;
    int[] a = new int[n];
    int[] b = new int[n];
    for (int i = 0; i < n; i++) {
      boolean firstHalf = i < half;
      a[i] = firstHalf ? 10_000 : 1;
      b[i] = firstHalf ? 1 : 10_000;
    }
    assertThat(runMain(buildInput(a, b))).isEqualTo(Long.toString((long) half));
  }

  // --- Randomised cross-check against an independent brute-force oracle. Small positive values and
  // short lengths make exact matches, near misses, and shared prefixes frequent, catching
  // off-by-one window bugs and the miscounts that hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(25332);
    for (int trial = 0; trial < 400; trial++) {
      int n = 1 + rnd.nextInt(12); // length 1..12
      int[] a = new int[n];
      int[] b = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = 1 + rnd.nextInt(6); // small positive values so equal windows are common
        b[i] = 1 + rnd.nextInt(6);
      }
      String input = buildInput(a, b);
      String expected = Long.toString(bruteForceCount(a, b));
      assertThat(runMain(input))
          .as("A=%s B=%s", Arrays.toString(a), Arrays.toString(b))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: count every window {@code [i, j]} whose interval sums are equal by
   * enumerating all windows over the diff sequence. Obviously correct, far too slow for the judge,
   * trustworthy for tiny {@code n}.
   *
   * @implNote Brute force in {@code O(n^2)} time, where {@code n} is {@code a.length}; the running
   *     diff is a {@code long} so the oracle never overflows.
   */
  private static long bruteForceCount(int[] a, int[] b) {
    long count = 0;
    for (int i = 0; i < a.length; i++) {
      long diff = 0;
      for (int j = i; j < a.length; j++) {
        diff += (long) a[j] - b[j];
        if (diff == 0) {
          count++;
        }
      }
    }
    return count;
  }

  /** Builds a BOJ 25332 input: {@code N}, then the {@code A} values, then the {@code B} values. */
  private static String buildInput(int[] a, int[] b) {
    StringBuilder sb = new StringBuilder().append(a.length).append('\n');
    appendRow(sb, a);
    appendRow(sb, b);
    return sb.toString();
  }

  /** Appends one space-separated row of values followed by a newline. */
  private static void appendRow(StringBuilder sb, int[] values) {
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(values[i]);
    }
    sb.append('\n');
  }

  private static int[] filled(int length, int value) {
    int[] arr = new int[length];
    Arrays.fill(arr, value);
    return arr;
  }

  private static String runMain(String input) throws IOException {
    return capture(input).trim();
  }

  private static String capture(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
