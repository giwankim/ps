package boj.boj1637;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1637 날카로운 눈 (Sharp Eyes; originally SEERC 2007 E "Showstopper") -- find the single integer
 * that occurs an odd number of times in a pile described by arithmetic progressions.
 *
 * <p>A pile of integers is given compressed as {@code N} arithmetic progressions. Each input line
 * {@code "A C B"} puts the integers {@code A, A+B, A+2B, ..., A+kB} (largest {@code k} with
 * {@code A+kB <= C}) into the pile. At most one integer occurs an odd number of times in the whole
 * pile; every other integer occurs an even number of times.
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 20,000}); the next {@code N} lines each hold
 * {@code "A C B"} with {@code 1 <= A, B, C <= 2,147,483,647}. Output: the odd-count integer and how
 * many times it occurs, space-separated -- or {@code "NOTHING"} if no integer occurs an odd number
 * of times. (Spec recovered from acmicpc.net through the exa search cache -- the site itself is
 * unreachable -- including the official sample below.)
 *
 * <p>The pile can hold on the order of {@code 20,000 * 2^31} integers, so no fixture may rely on
 * the implementation enumerating it. What a fixture can rely on is the characterization behind the
 * intended parametric search: the count of pile members {@code <= x} is even for every {@code x}
 * below the odd-count value and odd from it onward, so the answer is the smallest {@code x} whose
 * prefix count is odd. Fixture idesign inherits the judge's guarantee the other way around: every
 * fixture must consist of progressions whose elements cancel pairwise except for at most one value,
 * since inputs with two or more odd-count values are outside the spec.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Progression shape.</b> {@code A == C} leaves the single {@code k = 0} term
 *       ({@link #singleTermProgressionIsTheOddValueItself}); a step overshooting the limit keeps
 *       only the start ({@link #stepOvershootingTheLimitKeepsOnlyTheStart}); values between stride
 *       terms are not in the pile, only those with {@code (v - A) % B == 0}
 *       ({@link #valueBetweenStrideTermsOccursOnlyOnce}).
 *   <li><b>Prefix counting.</b> The official sample ({@link #officialSampleFindsTheTripledFour});
 *       per-line counts clamp at {@code min(x, C)} and guard {@code x >= A}, or phantom parity
 *       flips below the answer derail the search
 *       ({@link #prefixCountsClampEachProgressionAtItsLimit}); occurrences aggregate across all
 *       lines holding the value ({@link #overlappingRunsStackTheOddCountToFive}).
 *   <li><b>No odd value at all.</b> Identical progressions cancel to the {@code NOTHING} branch
 *       ({@link #identicalProgressionsCancelToNothing}).
 *   <li><b>Extremes.</b> The answer can be 2,147,483,647 itself while prefix counts pass
 *       {@code Integer.MAX_VALUE} and demand {@code long} accumulation
 *       ({@link #maximumValueAnswerNeedsLongPrefixCounts}); {@code N = 20,000} full-range lines
 *       must be answered without materializing or walking the pile
 *       ({@link #fullScaleInputIsSolvedWithoutWalkingThePile}).
 * </ul>
 *
 * <p>{@link #randomizedPilesMatchAnEnumeratingOracle} then cross-checks 500 seeded small piles
 * against an oracle that tallies every progression term into a map -- no parity reasoning and no
 * search, so the two agree only when both are right. Every fixed expectation and all 500 oracle
 * expectations were additionally validated against an independent parametric-search reference
 * solver while authoring this suite. The {@code Main} under test is an empty stub that reads
 * nothing and prints nothing, so every assertion here is RED until the search is implemented.
 */
class MainTest {

  // --- The official sample: 1..10 plus {4} plus 1..5 plus 6..10. Every value in 1..10 appears
  // exactly twice except 4, which the second line tops up to three occurrences -> "4 3". ---

  @Test
  @StdIo({"4", "1 10 1", "4 4 1", "1 5 1", "6 10 1"})
  void officialSampleFindsTheTripledFour(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 3");
  }

  // --- The smallest possible pile: N = 1 and A == C, so the progression has the k = 0 term only
  // and the pile is {5}. Pins the "+1" in the per-line term count -- a solution computing
  // floor((C-A)/B) without it sees an empty pile and prints NOTHING. ---

  @Test
  @StdIo({"1", "5 5 1"})
  void singleTermProgressionIsTheOddValueItself(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 1");
  }

  // --- A step far larger than the A..C window: "5 6 100" holds 5 but not 105, so the pile is
  // again just {5}. Pins that the progression is cut at C by flooring (C-A)/B, not by emitting
  // terms until one merely starts beyond some bound. ---

  @Test
  @StdIo({"1", "5 6 100"})
  void stepOvershootingTheLimitKeepsOnlyTheStart(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 1");
  }

  // --- Two identical progressions: every value occurs exactly twice, so no odd-count value
  // exists. Pins the NOTHING branch -- a solution assuming the story's "exactly one odd value"
  // and printing its binary search result unconditionally emits garbage here. ---

  @Test
  @StdIo({"2", "1 10 1", "1 10 1"})
  void identicalProgressionsCancelToNothing(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("NOTHING");
  }

  // --- Values between terms are not in the pile: "1 9 2" holds the odd values {1,3,5,7,9}, so 4
  // sits in the A..C window of both copies yet occurs only via the "4 4 1" line. Pins stride
  // membership, i.e. v is in a line's pile only when (v - A) % B == 0 -- a solution counting each
  // line as the dense range [A, C] answers "4 3" here. ---

  @Test
  @StdIo({"3", "1 9 2", "1 9 2", "4 4 1"})
  void valueBetweenStrideTermsOccursOnlyOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 1");
  }

  // --- The odd value (9) lies far above every other progression's limit: {1,3} + {3} + {1} cancel
  // pairwise and {9} is left over. Pins the two clamps in the prefix count "terms <= x", whose
  // per-line value is floor((min(x,C) - A) / B) + 1 taken only when x >= A: dropping the min(x,C)
  // clamp credits the "1 3 2" line with phantom terms 5 and 7, dropping the x >= A guard charges
  // lines negative counts, and either way the parity of "terms <= 5" flips from even to odd and
  // the search answers "5 1" instead of "9 1". ---

  @Test
  @StdIo({"4", "1 3 2", "3 3 1", "1 1 1", "9 9 1"})
  void prefixCountsClampEachProgressionAtItsLimit(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("9 1");
  }

  // --- The odd value occurs five times, assembled from differently shaped lines: 7 sits mid-run
  // in both "5 9 2" copies and is topped up by three single-term lines, while 5 and 9 stay at two
  // occurrences. Pins that the reported count aggregates over all lines containing the value
  // rather than stopping at the first hit or at the sample's maximum of three. ---

  @Test
  @StdIo({"5", "5 9 2", "5 9 2", "7 7 1", "7 7 1", "7 7 1"})
  void overlappingRunsStackTheOddCountToFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7 5");
  }

  // --- The numeric extremes: two copies of the full range 1..2,147,483,647 plus one extra
  // occurrence of the maximum itself. Pins two things at once. First, the answer can be the
  // largest legal value, so a search over [1, 2,147,483,646] -- a common exclusive-bound slip --
  // can never report it. Second, the prefix count below the answer is 2x ~ 4.29 billion, past
  // Integer.MAX_VALUE: counts must accumulate in long (int wraparound preserves parity, but a
  // "cnt % 2 == 1" oddness test still misreads a wrapped negative count, whose remainder is -1).
  // ---

  @Test
  @StdIo({"3", "1 2147483647 1", "1 2147483647 1", "2147483647 2147483647 1"})
  void maximumValueAnswerNeedsLongPrefixCounts(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2147483647 3");
  }

  // --- Full scale: N = 20,000 lines over the full value range -- 9,999 self-canceling pairs
  // "a 2147483647 1" plus the stride-2 lines "1 2147483647 2" and "3 2147483647 2", which top up
  // every odd value from 3 on twice but the value 1 only once, leaving "1 3" as the answer. The
  // pile holds about 4.3 * 10^13 integers, so a solution that materializes or walks it cannot
  // finish; the intended O(N log X) search, where X is the value bound 2^31 - 1, answers at once.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void fullScaleInputIsSolvedWithoutWalkingThePile() throws IOException {
    StringBuilder input = new StringBuilder("20000\n");
    for (int a = 1; a <= 9999; a++) {
      input.append(a).append(" 2147483647 1\n");
      input.append(a).append(" 2147483647 1\n");
    }
    input.append("1 2147483647 2\n");
    input.append("3 2147483647 2\n");
    assertThat(runMain(input.toString())).isEqualTo("1 3");
  }

  // --- Small random piles cross-checked against an oracle that walks every progression term into
  // a HashMap tally -- no prefix counts, no parity reasoning, no search, so the two agree only
  // when both are right. Each trial draws a few short progressions, tallies them, and then
  // re-establishes the judge's guarantee that careless fixtures would break: every odd-count
  // value except a randomly kept survivor (or none, for a NOTHING trial) is padded with a
  // single-term line topping it up to an even count. ---

  @Test
  void randomizedPilesMatchAnEnumeratingOracle() throws IOException {
    Random rnd = new Random(1637);
    for (int trial = 0; trial < 500; trial++) {
      List<int[]> lines = new ArrayList<>();
      int n = 1 + rnd.nextInt(6);
      for (int i = 0; i < n; i++) {
        int a = 1 + rnd.nextInt(30);
        int c = a + rnd.nextInt(15);
        int b = 1 + rnd.nextInt(8);
        lines.add(new int[] {a, c, b});
      }

      Map<Integer, Integer> counts = enumeratePile(lines);
      List<Integer> odds = new ArrayList<>();
      for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
        if (entry.getValue() % 2 == 1) {
          odds.add(entry.getKey());
        }
      }
      Collections.sort(odds);
      int kept = odds.isEmpty() || rnd.nextBoolean() ? -1 : odds.get(rnd.nextInt(odds.size()));
      for (int v : odds) {
        if (v != kept) {
          lines.add(new int[] {v, v, 1});
        }
      }

      String expected = kept == -1 ? "NOTHING" : kept + " " + counts.get(kept);
      String input = render(lines);
      assertThat(runMain(input)).as("trial=%d input=%s", trial, input).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle tally: walks every term of every progression into a value-to-count map.
   *
   * @implNote {@code O(P)} time, where {@code P} is the total number of progression terms across
   *     all lines -- tractable only because the randomized fixtures keep the ranges tiny.
   */
  private static Map<Integer, Integer> enumeratePile(List<int[]> lines) {
    Map<Integer, Integer> counts = new HashMap<>();
    for (int[] line : lines) {
      for (int v = line[0]; v <= line[1]; v += line[2]) {
        counts.merge(v, 1, Integer::sum);
      }
    }
    return counts;
  }

  /** Renders BOJ 1637 input: {@code N} on line 1, then one progression {@code "A C B"} per line. */
  private static String render(List<int[]> lines) {
    StringBuilder sb = new StringBuilder().append(lines.size()).append('\n');
    for (int[] line : lines) {
      sb.append(line[0]).append(' ').append(line[1]).append(' ').append(line[2]).append('\n');
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
