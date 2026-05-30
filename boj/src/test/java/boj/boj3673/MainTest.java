package boj.boj3673;

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
 * BOJ 3673 나눌 수 있는 부분 수열 (Divisible Subsequences) -- NWERC 2009, also published as POJ 3844.
 *
 * <p>Given a sequence of <em>positive</em> integers, count every <em>contiguous</em> subsequence (a
 * non-empty subarray) whose sum is divisible by a given divisor {@code d}. Overlapping subarrays
 * are counted separately, and subarrays at different index ranges are distinct even when their sums
 * coincide. The standard solution is the prefix-sum / remainder-bucket trick: a subarray {@code (i,
 * j]} has a sum divisible by {@code d} exactly when {@code prefix[i] ≡ prefix[j] (mod d)}, so the
 * answer is the number of unordered pairs of prefix sums sharing a remainder. Seeding the empty
 * prefix {@code prefix[0] = 0} (i.e. {@code count[0] = 1}) is what counts the subarrays that start
 * at index 0; omitting it is the canonical bug.
 *
 * <p>Input: the first line is the test-case count {@code c}. Each test case is two lines -- a line
 * {@code "d n"} (divisor and sequence length) followed by a line of {@code n} space-separated
 * elements. Output: one line per test case, the count of divisible contiguous subarrays (often
 * {@code 0}).
 *
 * <p>Constraints (triangulated from the NWERC 2009 / POJ 3844 statement while acmicpc.net was
 * unreachable -- the POJ and ShareCode mirrors agree exactly): {@code 1 <= c <= 200}; {@code 1 <= d
 * <= 1,000,000}; {@code 1 <= n <= 50,000}; every element is in {@code [1, 1,000,000,000]}.
 *
 * <p><b>Why the elements being positive matters.</b> Because every element is {@code >= 1}, the
 * prefix sums are strictly positive, so {@code prefix % d} always lands in {@code [0, d-1]} and
 * Java's signed-modulo trap (where {@code -3 % 5 == -3}) never fires -- no remainder normalisation
 * is needed. The positive elements instead create a different trap, exercised below.
 *
 * <p><b>Overflow note.</b> A prefix sum reaches {@code 50,000 * 1,000,000,000 = 5 * 10^13}, which
 * is far beyond a signed 32-bit {@code int} (limit {@code 2,147,483,647}); the running prefix sum
 * <em>must</em> be a {@code long}, even though the per-element values fit an {@code int}. The
 * <em>answer</em> is bounded by the number of contiguous subarrays, {@code n(n+1)/2 <= 50,000 *
 * 50,001 / 2 = 1,250,025,000}, which does fit a signed {@code int} -- so the overflow risk lives in
 * the prefix sum, not the count. {@link #maxSizeAllDivisibleCountsEverySubarrayWithoutOverflow()}
 * and {@link #largeElementValuesRequireALongPrefixSum(StdOut)} pin the prefix-sum overflow down.
 *
 * <p>The tests are ordered as an iterative TDD ladder -- single element, the start-anchored (empty
 * prefix) case, an interior match, the all-divisible and combinatorial-count cases, the {@code d =
 * 1} boundary, the published sample, multi-case structure and per-case state reset, the prefix-sum
 * overflow, maximum-size performance, and finally a randomised cross-check -- each expected value
 * independently confirmed by brute-force enumeration of every subarray.
 */
class MainTest {

  // --- Smallest input (n = 1): the lone subarray is the single element, so the answer is 0 or 1.

  // [3] sums to 3, which is not divisible by 7.
  @Test
  @StdIo({"1", "7 1", "3"})
  void singleElementNotDivisibleByDivisorCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // [5] sums to 5, exactly the divisor, so it is divisible.
  @Test
  @StdIo({"1", "5 1", "5"})
  void singleElementEqualToDivisorCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [9] sums to 9 = 3 * 3, a multiple of the divisor (not merely equal to it).
  @Test
  @StdIo({"1", "3 1", "9"})
  void singleElementMultipleOfDivisorCountsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The start-anchored (empty-prefix) case: a subarray beginning at index 0. ---

  // [1,2]: the subarrays are [1]=1, [1,2]=3, [2]=2; only [1,2]=3 is divisible by 3. That subarray
  // starts at index 0, so it is counted only if the empty prefix (prefix[0]=0, i.e. count[0]=1) is
  // seeded. This is the single most common bug in the problem.
  @Test
  @StdIo({"1", "3 2", "1 2"})
  void subarrayStartingAtIndexZeroIsCountedViaTheEmptyPrefix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // [3,1,2]: divisible subarrays are [3]=3, [3,1,2]=6 (both start-anchored), and [1,2]=3 (interior)
  // -> 3. Two distinct start-anchored subarrays must each pair with the empty prefix.
  @Test
  @StdIo({"1", "3 3", "3 1 2"})
  void multipleStartAnchoredSubarraysAreEachCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- An interior subarray (touching neither end) must still be found. ---

  // [1,2,3,4] with d=7: the only divisible subarray is [3,4]=7, which starts at index 2 and ends at
  // index 3 -- it touches neither boundary, so it is found purely by two interior prefix sums
  // sharing a remainder (prefix[2]=3 ≡ prefix[4]=10 mod 7).
  @Test
  @StdIo({"1", "7 4", "1 2 3 4"})
  void interiorSubarrayDivisibleIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- An unreachable divisor: every subarray sum is smaller than d, so the answer is 0. ---

  // [1,2,3,4] with d=100: the largest subarray sum is 1+2+3+4 = 10 < 100, so nothing is divisible.
  @Test
  @StdIo({"1", "100 4", "1 2 3 4"})
  void noContiguousSubarrayDivisibleCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Every element divisible by d => every one of the n(n+1)/2 subarrays is divisible. ---

  // [2,4,6] with d=2: all 3*4/2 = 6 subarrays have even sums. Every prefix sum shares remainder 0,
  // so the answer is C(4,2) = 6.
  @Test
  @StdIo({"1", "2 3", "2 4 6"})
  void everyElementDivisibleCountsAllSubarrays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- A remainder shared by three prefixes contributes C(3,2)=3, not 2: counting is pairwise. ---

  // [2,5,5] with d=5: prefix sums 0,2,7,12 have remainders 0,2,2,2. The three prefixes in bucket
  // r=2 yield C(3,2)=3 divisible subarrays: [5] (2nd), [5,5] (2nd-3rd), and [5] (3rd). A solution
  // that merely pairs consecutive equal remainders, instead of counting all pairs, would return 2.
  @Test
  @StdIo({"1", "5 3", "2 5 5"})
  void repeatedRemainderUsesPairwiseCombinationCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The divisor lower bound d = 1: every integer is divisible by 1, so every subarray counts.

  // [7, 13, 1000000000] with d=1: all 3*4/2 = 6 subarrays are divisible. Also lightly exercises a
  // maximum-magnitude element (10^9) on the read path.
  @Test
  @StdIo({"1", "1 3", "7 13 1000000000"})
  void divisorOneCountsEverySubarray(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- The published sample, split into its two cases, then run together. ---

  // Sample case 1: d=7, [1,2,3]. Subarray sums are 1,3,6,2,5,3; none divisible by 7 -> 0.
  @Test
  @StdIo({"1", "7 3", "1 2 3"})
  void officialSampleFirstCaseCountsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Sample case 2: d=4, [2,1,2,1,1,2,1,2]. The six divisible subarrays named in the statement are
  // the 1st-8th, 2nd-4th, 2nd-7th, 3rd-5th, 4th-6th, and 5th-7th -> 6.
  @Test
  @StdIo({"1", "4 8", "2 1 2 1 1 2 1 2"})
  void officialSampleSecondCaseCountsSix(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // The exact published input (c=2): the two answers must appear on their own lines, in order.
  @Test
  @StdIo({"2", "7 3", "1 2 3", "4 8", "2 1 2 1 1 2 1 2"})
  void officialSampleBothCasesProduceTwoLinesInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("0", "6");
  }

  // --- Multi-case bookkeeping: per-case state must reset so counts do not leak between cases. ---

  // Two identical cases (d=5, [5]) must each answer 1. If the remainder-count map is reused without
  // being cleared, the second case sees the first case's prefix in bucket 0 and answers 2.
  @Test
  @StdIo({"2", "5 1", "5", "5 1", "5"})
  void perTestCaseStateIsResetSoCountsDoNotLeak(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("1", "1");
  }

  // The test-case count at its upper bound (c = 200): all cases are processed, in order, and the
  // per-case reset holds at scale. Every case is the identical (d=7, [7]) -> 1; a state leak would
  // make the outputs climb 1, 2, 3, ... instead of staying 1.
  @Test
  void upperBoundNumberOfTestCasesAreEachAnswered() throws IOException {
    int cases = 200;
    StringBuilder input = new StringBuilder().append(cases).append('\n');
    for (int i = 0; i < cases; i++) {
      appendCase(input, 7, new int[] {7});
    }
    String[] expected = new String[cases];
    Arrays.fill(expected, "1");
    assertThat(runMainLines(input.toString())).containsExactly(expected);
  }

  // --- Prefix-sum overflow: cumulative sums exceed Integer.MAX_VALUE though each element fits int.

  // Three copies of 10^9 with d=10^6 (the maximum divisor). Each element is 1000 * 10^6, so every
  // subarray sum is divisible -> all 3*4/2 = 6 subarrays count. The third prefix sum is 3 * 10^9,
  // which overflows a signed int (max 2.147 * 10^9) to a negative value; an int accumulator would
  // compute a wrong remainder and miscount. Only a long prefix sum yields 6.
  @Test
  @StdIo({"1", "1000000 3", "1000000000 1000000000 1000000000"})
  void largeElementValuesRequireALongPrefixSum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Maximum-size inputs: O(n) is required (O(n^2) times out) and the prefix sum must be long.

  // n = 50,000 copies of 10^9 with d = 10^6: every subarray is divisible, so the answer is the
  // maximum possible n(n+1)/2 = 1,250,025,000 (which itself just fits a signed int). The prefix sum
  // climbs to 5 * 10^13, forcing a long; an O(n^2) pair scan over 50,000 elements would not finish
  // in time.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAllDivisibleCountsEverySubarrayWithoutOverflow() throws IOException {
    int n = 50_000;
    int[] arr = filled(n, 1_000_000_000);
    long expected = (long) n * (n + 1) / 2;
    assertThat(runMain(singleCaseInput(1_000_000, arr))).isEqualTo(Long.toString(expected));
  }

  // n = 50,000 copies of 1 with d = 50,000: a subarray's sum equals its length, divisible by 50,000
  // only when the length is exactly 50,000 -- i.e. the whole array, and nothing else. The answer is
  // 1. Guards against an O(n) solution that over-counts, and confirms a tiny answer at maximum n.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeSparseSequenceMatchesOnlyTheWholeArray() throws IOException {
    int n = 50_000;
    int[] arr = filled(n, 1);
    assertThat(runMain(singleCaseInput(50_000, arr))).isEqualTo("1");
  }

  // --- Randomised cross-check against an independent brute-force oracle. Small positive arrays and
  // small divisors make exact hits, near misses, and shared remainders frequent, catching
  // off-by-one window bugs and miscounting the hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rnd = new Random(3673);
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rnd.nextInt(12); // length 1..12
      int[] arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = 1 + rnd.nextInt(10); // positive elements 1..10, per the constraints
      }
      int d = 1 + rnd.nextInt(15); // divisor 1..15
      String input = singleCaseInput(d, arr);
      String expected = Long.toString(bruteForceCount(d, arr));
      assertThat(runMain(input)).as("d=%d arr=%s", d, Arrays.toString(arr)).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: count every contiguous subarray whose sum is divisible by {@code d} by
   * enumerating all subarrays. Obviously correct, far too slow for the judge, trustworthy for tiny
   * {@code n}.
   *
   * @implNote Brute force in {@code O(n^2)} time, where {@code n} is {@code arr.length}; the
   *     running sum is a {@code long} so the oracle never overflows.
   */
  private static long bruteForceCount(int d, int[] arr) {
    long count = 0;
    for (int i = 0; i < arr.length; i++) {
      long sum = 0;
      for (int j = i; j < arr.length; j++) {
        sum += arr[j];
        if (sum % d == 0) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Builds a single-test-case BOJ 3673 input: {@code "1"}, then {@code "d n"}, then the elements.
   */
  private static String singleCaseInput(int d, int[] arr) {
    StringBuilder sb = new StringBuilder().append(1).append('\n');
    appendCase(sb, d, arr);
    return sb.toString();
  }

  /**
   * Appends one test case -- a {@code "d n"} line followed by the {@code n} space-separated values.
   */
  private static void appendCase(StringBuilder sb, int d, int[] arr) {
    sb.append(d).append(' ').append(arr.length).append('\n');
    for (int i = 0; i < arr.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(arr[i]);
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

  private static String[] runMainLines(String input) throws IOException {
    String out = capture(input).trim();
    return out.isEmpty() ? new String[0] : out.split("\\R");
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
