package boj.boj2015;

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
 * BOJ 2015 수들의 합 4 (Sum of Numbers 4) -- count contiguous subarrays whose sum equals {@code K}.
 *
 * <p>Given an array {@code A} of {@code N} integers and a target {@code K}, count the pairs
 * {@code (i, j)} with {@code 1 <= i <= j <= N} such that {@code A[i] + A[i+1] + ... + A[j] == K}.2
 *
 * <p>Input: line 1 is {@code "N K"}; line 2 is {@code N} space-separated integers. Output: a single
 * line with the count.
 *
 * <p>Constraints (triangulated while acmicpc.net was unreachable; all consulted mirrors agree):
 * {@code 1 <= N <= 200,000}; {@code |K| <= 2,000,000,000}; and each {@code |A[i]| <= 10,000}.
 *
 * <p><b>Overflow note -- three different widths.</b>
 *
 * <ul>
 *   <li>The <em>prefix sum</em> itself fits a signed 32-bit {@code int}: {@code |prefix| <= 200,000
 *       * 10,000 = 2,000,000,000 < 2,147,483,647}. (Unlike many prefix-sum problems, the running
 *       sum is not where the overflow lives.)
 *   <li>The <em>lookup key</em> {@code prefix[j] - K} spans {@code [-4,000,000,000, 4,000,000,000]}
 *       (e.g. {@code 2e9 - (-2e9)}), which does <em>not</em> fit an {@code int}; the key/arithmetic
 *       must be {@code long}.
 *   <li>The <em>answer</em> reaches {@code N(N+1)/2 = 200,000 * 200,001 / 2 = 20,000,100,000} when
 *       every element is {@code 0} and {@code K = 0}, far beyond {@code int}; the accumulator must
 *       be {@code long}. {@link #maxSizeAllZerosWithZeroTargetOverflowsIntAnswer()} pins this down.
 * </ul>
 */
class MainTest {

  // --- Smallest input (n = 1): the lone subarray is the single element, so the answer is 0 or 1.
  // ---

  // [5] sums to 5, exactly K.
  @Test
  @StdIo({"1 5", "5"})
  void singleElementEqualToKCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [3] sums to 3, not K = 5.
  @Test
  @StdIo({"1 5", "3"})
  void singleElementNotEqualToKCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [-3] sums to -3 = K: elements and K may both be negative.
  @Test
  @StdIo({"1 -3", "-3"})
  void singleNegativeElementMatchingNegativeKCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Start-anchored (empty-prefix) case: a subarray beginning at index 1 (prefix[j] == K). ---

  // [1,2,3] with K = 6: only the whole array 1+2+3 = 6 matches. It starts at index 1, so it is
  // found
  // only via the seeded empty prefix (count[0] = 1). Dropping that seed is the single most common
  // bug.
  @Test
  @StdIo({"3 6", "1 2 3"})
  void wholeArrayPrefixEqualToKIsCountedViaEmptyPrefix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [1,2,3] with K = 3: the matches are [1,2] (start-anchored, 1-2) and [3] (interior, index 3) ->
  // 2.
  @Test
  @StdIo({"3 3", "1 2 3"})
  void startAnchoredAndInteriorMatchesAreBothCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- An interior subarray (touching neither end) must still be found. ---

  // [1,3,4,2] with K = 7: the only match is [3,4] at indices 2-3, which touches neither boundary --
  // found purely by two interior prefixes differing by K (prefix[1]=1, prefix[3]=8; 8-1=7).
  @Test
  @StdIo({"4 7", "1 3 4 2"})
  void interiorSubarrayMatchingKIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- No subarray reaches K, and the K = 0 guard against a spurious seed match. ---

  // [1,2,3] with K = 100: the largest subarray sum is 6 < 100, so nothing matches.
  @Test
  @StdIo({"3 100", "1 2 3"})
  void noSubarraySumsToKCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [1,2,3] with K = 0: all elements positive, so prefixes are strictly increasing and distinct and
  // none equals another (nor the seeded 0). The answer is 0 -- the empty-prefix seed must not be
  // mistaken for a length-0 subarray.
  @Test
  @StdIo({"3 0", "1 2 3"})
  void allPositiveTargetingZeroCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- K = 0 with negatives: zero-sum subarrays are real and must be counted. ---

  // [1,-1,2] with K = 0: only [1,-1] (indices 1-2) sums to 0.
  @Test
  @StdIo({"3 0", "1 -1 2"})
  void zeroSumSubarrayWithNegativesIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Recovered official sample: [2,-2,2,-2] with K = 0. Prefix sums 0,2,0,2,0; the four zero-sum
  // subarrays are [2,-2] (1-2), [-2,2] (2-3), [2,-2] (3-4), and the whole array [2,-2,2,-2] (1-4).
  @Test
  @StdIo({"4 0", "2 -2 2 -2"})
  void officialSampleZeroTargetWithAlternatingSignsCountsFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- A negative target reached by negative elements. ---

  // [-1,-2,-3] with K = -3: the matches are [-1,-2] (1-2) and [-3] (index 3) -> 2.
  @Test
  @StdIo({"3 -3", "-1 -2 -3"})
  void negativeKWithAllNegativeNumbersCountsTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Overlapping matches are each counted. ---

  // [1,1,1,1] with K = 2: the matches are [1,1] at 1-2, 2-3, and 3-4 -- three overlapping windows
  // -> 3.
  @Test
  @StdIo({"4 2", "1 1 1 1"})
  void overlappingSubarraysAreEachCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- A prefix value shared by three positions contributes C(3,2)=3, not 2: counting is pairwise.
  // ---

  // [5,0,0] with K = 0: prefix sums 0,5,5,5. The three prefixes valued 5 (after indices 1,2,3) form
  // C(3,2)=3 zero-sum subarrays: [0] (index 2), [0] (index 3), and [0,0] (2-3). A solution that
  // only
  // pairs adjacent equal prefixes would return 2.
  @Test
  @StdIo({"3 0", "5 0 0"})
  void repeatedPrefixSumCountsAllPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Negatives make the prefix non-monotonic, so a sliding window would fail; the hashmap is
  // required. ---

  // [3,-2,2] with K = 3: prefix sums 0,3,1,3 (note 3 -> 1 decreases). The matches are [3] (index 1)
  // and the whole array [3,-2,2] (1-3); a positive-only two-pointer scan cannot find both.
  @Test
  @StdIo({"3 3", "3 -2 2"})
  void negativesBreakMonotonicitySoHashIsRequired(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Several disjoint subarrays each summing to K, scattered across the array. ---

  // [1,4,2,3,5] with K = 5: three disjoint matches -- [1,4] (1-2), [2,3] (3-4), [5] (index 5) -> 3.
  @Test
  @StdIo({"5 5", "1 4 2 3 5"})
  void multipleDisjointSubarraysEachSummingToKAreCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Boundary and maximum-size inputs, driven through the stdin/stdout helper. ---

  // All zeros with K = 0 at the maximum N: every one of the N(N+1)/2 subarrays sums to 0, so the
  // answer is 200,000 * 200,001 / 2 = 20,000,100,000 -- which overflows a signed int (max
  // 2,147,483,647). Only a long accumulator yields the right value, and O(N^2) would not finish.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAllZerosWithZeroTargetOverflowsIntAnswer() throws IOException {
    int n = 200_000;
    int[] arr = filled(n, 0);
    long expected = (long) n * (n + 1) / 2;
    assertThat(runMain(buildInput(0, arr))).isEqualTo(Long.toString(expected));
  }

  // K at its positive bound (2,000,000,000) reached only by the whole array: 200,000 copies of
  // 10,000
  // sum to exactly 2e9, and no proper subarray of positives can. The answer is 1.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void wholeArrayAtMaxValueSumMatchesMaxK() throws IOException {
    int n = 200_000;
    int[] arr = filled(n, 10_000);
    assertThat(runMain(buildInput(2_000_000_000L, arr))).isEqualTo("1");
  }

  // K at its negative bound (-2,000,000,000), the mirror of the previous case: 200,000 copies of
  // -10,000 sum to exactly -2e9 (only the whole array), so the answer is 1.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void wholeArrayAtMinValueSumMatchesMinK() throws IOException {
    int n = 200_000;
    int[] arr = filled(n, -10_000);
    assertThat(runMain(buildInput(-2_000_000_000L, arr))).isEqualTo("1");
  }

  // Maximum N with many distinct prefixes, exercising the hashmap at scale: 200,000 ones with K =
  // 1.
  // Each single element matches and nothing longer does, so the answer is exactly N = 200,000,
  // found
  // via N distinct prefix keys (a real load test, unlike the single-key all-zeros case).
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAllOnesCountsEachSingleElementWithLargeMap() throws IOException {
    int n = 200_000;
    int[] arr = filled(n, 1);
    assertThat(runMain(buildInput(1, arr))).isEqualTo(Long.toString((long) n));
  }

  // --- Randomised cross-check against an independent brute-force oracle. Small arrays with
  // negative,
  // zero, and positive elements and a target spanning negatives, zero, and positives make exact
  // hits,
  // near misses, overlaps, and shared prefixes frequent -- catching off-by-one and sign bugs the
  // hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(2015);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rnd.nextInt(12); // length 1..12
      int[] arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = rnd.nextInt(21) - 10; // elements in [-10, 10], including 0 and negatives
      }
      long k = rnd.nextInt(41) - 20; // target in [-20, 20], including 0 and negatives
      String input = buildInput(k, arr);
      String expected = Long.toString(bruteForceCount(k, arr));
      assertThat(runMain(input)).as("K=%d arr=%s", k, Arrays.toString(arr)).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: count every contiguous subarray whose sum equals {@code k} by enumerating
   * all subarrays. Obviously correct, far too slow for the judge, trustworthy for tiny {@code n}.
   *
   * @implNote Brute force in {@code O(n^2)} time, where {@code n} is {@code arr.length}; the
   *     running sum is a {@code long} so the oracle never overflows.
   */
  private static long bruteForceCount(long k, int[] arr) {
    long count = 0;
    for (int i = 0; i < arr.length; i++) {
      long sum = 0;
      for (int j = i; j < arr.length; j++) {
        sum += arr[j];
        if (sum == k) {
          count++;
        }
      }
    }
    return count;
  }

  /** Builds BOJ 2015 input: a {@code "N K"} header line followed by the {@code N} elements. */
  private static String buildInput(long k, int[] arr) {
    StringBuilder sb = new StringBuilder();
    sb.append(arr.length).append(' ').append(k).append('\n');
    for (int i = 0; i < arr.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(arr[i]);
    }
    sb.append('\n');
    return sb.toString();
  }

  private static int[] filled(int length, int value) {
    int[] arr = new int[length];
    Arrays.fill(arr, value);
    return arr;
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
