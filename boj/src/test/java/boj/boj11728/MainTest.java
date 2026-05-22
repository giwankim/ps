package boj.boj11728;

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

/** BOJ 11728 배열 합치기 (Merge Two Sorted Arrays). */
class MainTest {

  // --- Official samples. Anchor the suite against the published examples. ---

  // A=[3,5], B=[2,9]: the two pairs interleave -> "2 3 5 9".
  @Test
  @StdIo({"2 2", "3 5", "2 9"})
  void officialSampleInterleavesTwoPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3 5 9");
  }

  // A=[4,7], B=[1] (N=2, M=1): B's lone element is the global minimum, then A drains -> "1 4 7".
  @Test
  @StdIo({"2 1", "4 7", "1"})
  void officialSampleDrainsLongerArrayAfterSingleton(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 4 7");
  }

  // A=[2,3,5,9], B=[1,4,7]: a fully interleaved seven-element merge -> "1 2 3 4 5 7 9".
  @Test
  @StdIo({"4 3", "2 3 5 9", "1 4 7"})
  void officialSampleMergesFourWithThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 4 5 7 9");
  }

  // --- Minimum sizes (N = M = 1): the smallest legal input in either array. ---

  // A=[1], B=[2]: A's element already precedes B's -> "1 2".
  @Test
  @StdIo({"1 1", "1", "2"})
  void singleElementEachAlreadyInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2");
  }

  // A=[2], B=[1]: B's element is smaller, so the merge must emit B before A -> "1 2".
  @Test
  @StdIo({"1 1", "2", "1"})
  void singleElementEachOutOfOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2");
  }

  // A=[5], B=[5]: equal singletons must both survive the tie -> "5 5".
  @Test
  @StdIo({"1 1", "5", "5"})
  void singleElementEachEqualKeepsBoth(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 5");
  }

  // --- Disjoint blocks: exercises each "drain the remainder" branch in isolation. ---

  // A=[1,2,3] entirely below B=[4,5,6]: A empties first, then B's tail is drained -> "1 2 3 4 5 6".
  @Test
  @StdIo({"3 3", "1 2 3", "4 5 6"})
  void allOfAPrecedesAllOfB(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 4 5 6");
  }

  // A=[4,5,6] entirely above B=[1,2,3]: B empties first, then A's tail is drained -> "1 2 3 4 5 6".
  @Test
  @StdIo({"3 3", "4 5 6", "1 2 3"})
  void allOfBPrecedesAllOfA(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 4 5 6");
  }

  // --- Interleaving and insertion: values from the two arrays alternate or nest. ---

  // A=[1,3,5], B=[2,4,6]: perfectly alternating sources force a comparison at every step ->
  // "1 2 3 4 5 6".
  @Test
  @StdIo({"3 3", "1 3 5", "2 4 6"})
  void perfectlyAlternatingSources(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 4 5 6");
  }

  // A=[5], B=[1,9]: A's single element lands between B's two -> "1 5 9".
  @Test
  @StdIo({"1 2", "5", "1 9"})
  void singletonLandsBetweenTwoElements(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 5 9");
  }

  // --- Duplicates: equal values within and across the arrays must all be emitted. ---

  // A=[1,2,2], B=[2,3]: three 2s in total across both arrays must all appear -> "1 2 2 2 3".
  @Test
  @StdIo({"3 2", "1 2 2", "2 3"})
  void duplicatesAcrossArraysAreAllKept(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 2 2 3");
  }

  // A=[1,2,3], B=[1,2,3]: identical arrays yield every value exactly twice -> "1 1 2 2 3 3".
  @Test
  @StdIo({"3 3", "1 2 3", "1 2 3"})
  void identicalArraysDoubleEveryValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 2 2 3 3");
  }

  // A=[7,7], B=[7,7,7]: every element equal collapses to a run of five 7s -> "7 7 7 7 7".
  @Test
  @StdIo({"2 3", "7 7", "7 7 7"})
  void allElementsEqualProducesOneRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7 7 7 7 7");
  }

  // --- Sign and magnitude: negatives, zero, and the |value| <= 1e9 constraint bounds. ---

  // A=[-5,-1], B=[-3,-2]: all-negative inputs must still sort ascending -> "-5 -3 -2 -1".
  @Test
  @StdIo({"2 2", "-5 -1", "-3 -2"})
  void negativeValuesSortAscending(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-5 -3 -2 -1");
  }

  // A=[-2,0,4], B=[-1,3]: a merge straddling zero -> "-2 -1 0 3 4".
  @Test
  @StdIo({"3 2", "-2 0 4", "-1 3"})
  void mixedSignsMergeAroundZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-2 -1 0 3 4");
  }

  // A=[-1000000000, 0], B=[1000000000]: the +/-1e9 limits must parse and print correctly (they fit
  // in a signed int) -> "-1000000000 0 1000000000".
  @Test
  @StdIo({"2 1", "-1000000000 0", "1000000000"})
  void extremeValuesAtConstraintBounds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1000000000 0 1000000000");
  }

  // --- Lopsided sizes: a long array merged against a singleton from either side. ---

  // A=[1,2,3,4,5], B=[3]: the singleton nests among the longer array's values -> "1 2 3 3 4 5".
  @Test
  @StdIo({"5 1", "1 2 3 4 5", "3"})
  void longArrayWithSingletonFromB(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 3 4 5");
  }

  // A=[3], B=[1,2,3,4,5]: the same shape with the roles of A and B swapped -> "1 2 3 3 4 5".
  @Test
  @StdIo({"1 5", "3", "1 2 3 4 5"})
  void longArrayWithSingletonFromA(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 2 3 3 4 5");
  }

  // --- Upper size bound (N = M = 1,000,000): must finish fast and stream output efficiently. ---

  // Odd-valued A=[1,3,..] and even-valued B=[2,4,..] force a comparison at every step; their merge
  // is
  // the contiguous run 1..2,000,000. A re-sort of the whole 2M-element array or an O(n^2) merge
  // blows
  // the 1.5 s judge limit; the intended linear two-pointer sweep returns quickly.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeAlternatingMergeStaysWithinTimeLimit() throws IOException {
    StringBuilder a = new StringBuilder();
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < MAX_N; i++) {
      a.append(2 * i + 1).append(i < MAX_N - 1 ? ' ' : '\n');
      b.append(2 * i + 2).append(i < MAX_N - 1 ? ' ' : '\n');
    }
    String input = MAX_N + " " + MAX_N + "\n" + a + b;
    assertThat(isContiguousRangeFromOne(runMain(input), 2 * MAX_N)).isTrue();
  }

  // Disjoint blocks at scale: A=[1..1e6] entirely below B=[1e6+1..2e6] keeps a single drain loop
  // busy
  // for a million elements, again yielding 1..2,000,000. Stresses the tail-copy path that the small
  // disjoint cases above check only in miniature.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeDisjointMergeStaysWithinTimeLimit() throws IOException {
    StringBuilder a = new StringBuilder();
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < MAX_N; i++) {
      a.append(i + 1).append(i < MAX_N - 1 ? ' ' : '\n');
      b.append(MAX_N + i + 1).append(i < MAX_N - 1 ? ' ' : '\n');
    }
    String input = MAX_N + " " + MAX_N + "\n" + a + b;
    assertThat(isContiguousRangeFromOne(runMain(input), 2 * MAX_N)).isTrue();
  }

  private static final int MAX_N = 1_000_000;

  /**
   * Reports whether {@code output} is exactly the whitespace-separated contiguous sequence 1, 2,
   * ..., {@code count}. Parses primitives in a single pass so the million-element cases never
   * allocate a giant expected string or a {@code String[]} of every token.
   */
  private static boolean isContiguousRangeFromOne(String output, int count) {
    int seen = 0;
    long expected = 1;
    int i = 0;
    int n = output.length();
    while (i < n) {
      while (i < n && Character.isWhitespace(output.charAt(i))) {
        i++;
      }
      if (i >= n) {
        break;
      }
      boolean negative = output.charAt(i) == '-';
      if (negative) {
        i++;
      }
      long value = 0;
      while (i < n && output.charAt(i) >= '0' && output.charAt(i) <= '9') {
        value = value * 10 + (output.charAt(i) - '0');
        i++;
      }
      if (negative) {
        value = -value;
      }
      if (value != expected) {
        return false;
      }
      expected++;
      seen++;
    }
    return seen == count;
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
