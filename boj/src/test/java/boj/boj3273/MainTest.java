package boj.boj3273;

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

/** BOJ 3273 두 수의 합 (Sum of two numbers). */
class MainTest {

  // --- Official sample. Anchors the suite against the published example. ---

  // Sorted: [1,2,3,5,7,9,10,11,12]; the pairs summing to 13 are {1,12}, {2,11}, {3,10} -> 3.
  @Test
  @StdIo({"9", "5 12 7 10 9 1 2 3 11", "13"})
  void officialSampleCountsThreePairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Smallest answers: one qualifying pair, or none. ---

  // Two numbers that add up to x are the minimal positive case -> exactly one pair.
  @Test
  @StdIo({"2", "4 6", "10"})
  void twoElementsSummingToXGiveOnePair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The same two numbers against a target they cannot reach -> zero, the lower bound.
  @Test
  @StdIo({"2", "4 6", "11"})
  void twoElementsNotSummingToXGiveZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // i < j needs two indices, so a one-element sequence can never form a pair, whatever x is.
  @Test
  @StdIo({"1", "7", "10"})
  void singleElementHasNoPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Lower edge of the value domain: the two smallest legal values, 1 and 2, summing to 3.
  @Test
  @StdIo({"2", "1 2", "3"})
  void minimumValuePairOneAndTwoIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A larger set where no value has its complement present (max sum 4+8=12 < 100) -> zero.
  @Test
  @StdIo({"4", "1 2 4 8", "100"})
  void noComplementPresentGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Counting several pairs across the whole sequence. ---

  // {1,6}, {2,5}, {3,4} all sum to 7; every element participates in exactly one pair -> 3.
  @Test
  @StdIo({"6", "1 2 3 4 5 6", "7"})
  void multipleDisjointPairsAreAllCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // The sequence is unsorted; {20,1}, {19,2}, {18,3} sum to 21. Guards against any algorithm
  // that assumes pre-sorted input rather than sorting (or hashing) for itself.
  @Test
  @StdIo({"6", "20 1 19 2 18 3", "21"})
  void unsortedInputIsCountedCorrectly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Distinctness: a value equal to x/2 cannot pair with itself (no duplicates exist). ---

  // x is even and x/2 (=6) is present, but 6+6 is forbidden and 7 has no complement -> 0.
  @Test
  @StdIo({"2", "6 7", "12"})
  void evenTargetWithHalfValuePresentCannotSelfPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // x/2 (=6) sits alongside a genuine pair {3,9}. The half value must be excluded, not
  // miscounted, so the answer is the single real pair, not two.
  @Test
  @StdIo({"3", "3 6 9", "12"})
  void evenTargetExcludesHalfValueButCountsRealPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Boundary values of x and a_i. ---

  // Lower bound of x: the smallest sum of two distinct positives is 1+2=3, so x=2 is
  // unreachable (it would need 1+1). Mirrors the unreachable upper bound below.
  @Test
  @StdIo({"2", "1 2", "2"})
  void smallestUnreachableTargetGivesZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Largest sum two distinct values within [1, 1,000,000] can reach: 999999 + 1000000.
  @Test
  @StdIo({"2", "999999 1000000", "1999999"})
  void largestAchievableSumOfDistinctValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Upper bound of x: 2,000,000 would require 1000000 + 1000000, impossible without a
  // duplicate, so even with values pressed against the ceiling the count is 0.
  @Test
  @StdIo({"3", "1 999999 1000000", "2000000"})
  void maximumTargetIsUnreachableWithDistinctValues(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Scale: the upper size bound must finish well within the time limit. ---

  // For values 1 through 100,000 and a target of 100,001, every value v pairs with 100001 minus v,
  // giving one pair for each v from 1 to 50,000 -- so 50,000 pairs in total. A quadratic scan over
  // 100,000 elements would exceed the timeout, whereas an O(n log n) sort with two pointers (or
  // O(n) hashing) returns almost immediately.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void largeSequenceWithFiftyThousandPairsFinishesInTime() throws IOException {
    assertThat(runMain(largeConsecutiveInput())).isEqualTo("50000");
  }

  /**
   * Builds the largest fixture: the sequence 1, 2, ..., 100000 on one line, then target 100001. Its
   * answer is 50,000 pairs, since each value v matches 100001 minus v.
   */
  private static String largeConsecutiveInput() {
    int n = 100_000;
    int x = n + 1;
    StringBuilder sb = new StringBuilder().append(n).append('\n');
    for (int i = 1; i <= n; i++) {
      sb.append(i);
      sb.append(i < n ? ' ' : '\n');
    }
    return sb.append(x).append('\n').toString();
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
