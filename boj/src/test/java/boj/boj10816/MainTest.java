package boj.boj10816;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 10816 숫자 카드 2 (Number Card 2) -- the counting sequel to 10815's set-membership exercise.
 *
 * <p>Sang-geun owns {@code N} number cards, each printed with one integer. Unlike 10815, the cards
 * are <em>not</em> guaranteed distinct -- the same value may appear on many cards. Given {@code M}
 * query integers, report for each <em>how many</em> of Sang-geun's cards bear that number (the
 * multiset count), {@code 0} when he holds none.
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 500,000}); line 2 is the {@code N} card values,
 * space-separated, each in {@code [-10,000,000, 10,000,000]}; line 3 is {@code M} ({@code 1 <= M <=
 * 500,000}); line 4 is the {@code M} query values, space-separated, in the same range. Queries are
 * not guaranteed distinct. Output: the {@code M} counts, space-separated on one line. (Spec
 * triangulated across blog mirrors of the statement while acmicpc.net was unreachable; the
 * published sample {@code 6 3 2 10 10 10 -10 -10 7 3} / {@code 10 9 -5 2 3 4 5 -10} -> {@code 3 0 0
 * 1 2 0 0 2} corroborates it.)
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Counts, not membership.</b> A value backed by several cards must report its
 *       multiplicity, so {@link #duplicateCardsAreCountedNotCollapsed} and
 *       {@link #allIdenticalCardsReturnTheFullCount} reject any 10815-style {@code 0}/{@code 1}
 *       answer.
 *   <li><b>A miss is {@code 0}, not the nearest neighbour.</b> Queries below the smallest card,
 *       inside a gap, or above the largest collapse to {@code 0} while genuine hits keep their
 *       counts ({@link #missesPrintZeroWhileHitsKeepTheirCount}).
 *   <li><b>Order-independent tally.</b> Cards arrive unsorted with duplicates interleaved, so a
 *       binary-search solution must sort first ({@link #unsortedInterleavedDuplicatesAreCounted}).
 *   <li><b>Per-query answers, repeats included.</b> Each of the {@code M} queries is answered
 *       independently and a repeated query repeats its count
 *       ({@link #repeatedQueriesAreAnsweredIndependently}).
 *   <li><b>Signed values to the full bound.</b> Negative cards order correctly and
 *       {@code ±10,000,000} parse and tally without trouble
 *       ({@link #negativeValuesIncludingDuplicatesAreCounted},
 *       {@link #extremeValueBoundsAreCounted}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins {@code O((N + M) log N)} against an
 * {@code O(N * M)} linear rescan, and {@link #randomizedInputsMatchMultisetCountOracle}
 * cross-checks the judged output against an obviously-correct {@link java.util.HashMap}
 * multiset-count oracle. The empty {@code Main} stub under test reads nothing and prints nothing,
 * so every assertion here is RED until the solution is implemented.
 */
class MainTest {

  // --- The official sample from the statement: an unsorted hand carrying duplicates (three 10s,
  // two
  // 3s, two -10s), queried out of order, mixing hits of varying multiplicity with outright misses.
  // The end-to-end smoke test, and already enough to pin that duplicates are *counted*, not
  // collapsed to 1 as 10815 would. ---

  @Test
  @StdIo({"10", "6 3 2 10 10 10 -10 -10 7 3", "8", "10 9 -5 2 3 4 5 -10"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 0 0 1 2 0 0 2");
  }

  // --- The one thing 10816 adds over 10815: a held value backed by several cards must report its
  // multiplicity, never collapse to 1. cards {5,5,5,7}; 5 -> 3, 7 -> 1, 6 absent -> 0. A
  // 10815-style
  // membership solution would misprint "1 1 0" here. ---

  @Test
  @StdIo({"4", "5 5 5 7", "3", "5 7 6"})
  void duplicateCardsAreCountedNotCollapsed(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 1 0");
  }

  // --- Smallest input (N = 1, M = 1): the lone card contributes a count of 1 to a matching query.

  // The single card 5 answers the single query 5 -> one copy.
  @Test
  @StdIo({"1", "5", "1", "5"})
  void singleCardSingleQueryPresentPrintsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The single card 5 does not bear the queried 3 -> zero copies.
  @Test
  @StdIo({"1", "5", "1", "3"})
  void singleCardSingleQueryAbsentPrintsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- The multiplicity extreme: every card carries the same value, so its count equals N. cards
  // {4,4,4,4,4}; 4 -> 5, 3 absent -> 0. Pins that the tally can reach all the way to N. ---

  @Test
  @StdIo({"5", "4 4 4 4 4", "2", "4 3"})
  void allIdenticalCardsReturnTheFullCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 0");
  }

  // --- A miss reports 0 -- below the smallest card, inside a gap between two cards, and above the
  // largest -- while genuine hits keep their counts. cards {2,4,4,6}; queries 1 (below), 3 (gap), 7
  // (above), 4 (two copies), 2 (one copy) -> 0 0 0 2 1. The interleaved hits stop a trivial
  // "always 0" solution from passing. ---

  @Test
  @StdIo({"4", "2 4 4 6", "5", "1 3 7 4 2"})
  void missesPrintZeroWhileHitsKeepTheirCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 0 2 1");
  }

  // --- Queries may repeat, and each occurrence is answered on its own with the same count. cards
  // {2,2,3}; asking 2 three times then 4 echoes the count per occurrence -> 2 2 2 0. ---

  @Test
  @StdIo({"3", "2 2 3", "4", "2 2 2 4"})
  void repeatedQueriesAreAnsweredIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 2 2 0");
  }

  // --- Input order is arbitrary and duplicates arrive interleaved: a binary-search solution that
  // forgets to sort first would tally the wrong runs. cards {5,9,1,5,9,5} -> 5 thrice, 9 twice, 1
  // once; queries 9,5,1,7 -> 2 3 1 0. ---

  @Test
  @StdIo({"6", "5 9 1 5 9 5", "4", "9 5 1 7"})
  void unsortedInterleavedDuplicatesAreCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3 1 0");
  }

  // --- Negative values order and tally correctly, duplicates included (the comparison cannot
  // assume
  // non-negative). cards {-5,-5,-3,-1,-1,-1}; queries -5 (two), -3 (one), -1 (three), -2 (gap), 0
  // (above all) -> 2 1 3 0 0. ---

  @Test
  @StdIo({"6", "-5 -5 -3 -1 -1 -1", "5", "-5 -3 -1 -2 0"})
  void negativeValuesIncludingDuplicatesAreCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 1 3 0 0");
  }

  // --- The value bound ±10,000,000 must parse and compare cleanly (no overflow, no clipping), with
  // duplicates pinned at the extreme. cards {-1e7,-1e7,0,1e7}; queries -1e7 (two), 1e7 (one), 0
  // (one), and the values one step inside each extreme (misses) -> 2 1 1 0 0. ---

  @Test
  @StdIo({"4", "-10000000 -10000000 0 10000000", "5", "-10000000 10000000 0 9999999 -9999999"})
  void extremeValueBoundsAreCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 1 1 0 0");
  }

  // --- Upper bound on N and M: 500,000 identical cards and 500,000 queries. Every query but the
  // last misses (values 1..499,999, none equal to the card value 0); the final query 0 must tally
  // all 500,000 copies. The O((N + M) log N) sort-and-search (or O(N + M) hashing) solution stays
  // within the time limit where an O(N * M) per-query rescan -- 2.5e11 comparisons -- would not.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    int n = 500_000;
    int[] cards = new int[n]; // all zeros
    int m = 500_000;
    int[] queries = new int[m];
    StringBuilder expected = new StringBuilder();
    for (int i = 0; i < m - 1; i++) {
      queries[i] = i + 1; // 1..499,999 -- all misses
      expected.append("0 ");
    }
    queries[m - 1] = 0; // the sole hit, backed by every card
    expected.append(n);
    assertThat(runMain(cardQueryInput(cards, queries))).isEqualTo(expected.toString());
  }

  // --- Small random hands drawn from a narrow value band -- so duplicates and misses are both
  // frequent -- checked against an independent HashMap multiset-count oracle. The narrow band makes
  // every count from 0 up to a handful common, catching off-by-one boundary bugs (lower_bound vs
  // upper_bound, or a hash miss returning null) that the hand-picked cases might slip past. ---

  @Test
  void randomizedInputsMatchMultisetCountOracle() throws IOException {
    Random rnd = new Random(10816);
    for (int trial = 0; trial < 1000; trial++) {
      int n = 1 + rnd.nextInt(20);
      int[] cards = new int[n];
      for (int i = 0; i < n; i++) {
        cards[i] = rnd.nextInt(11) - 5; // values in [-5, 5]
      }
      int m = 1 + rnd.nextInt(20);
      int[] queries = new int[m];
      for (int i = 0; i < m; i++) {
        queries[i] = rnd.nextInt(15) - 7; // queries in [-7, 7] -> some certain misses
      }
      String expected = multisetCountAnswer(cards, queries);
      assertThat(runMain(cardQueryInput(cards, queries)))
          .as("cards=%s queries=%s", Arrays.toString(cards), Arrays.toString(queries))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: tally the cards into a {@link HashMap} from value to occurrence count, then
   * read each query's count straight out of the map ({@code 0} when absent). Shares no logic with a
   * sorted binary-search judge solution, so the two agree only when both are right.
   *
   * @implNote {@code O(N + M)} time, where {@code N} is {@code cards.length} and {@code M} is
   *     {@code queries.length}.
   */
  private static String multisetCountAnswer(int[] cards, int[] queries) {
    Map<Integer, Integer> counts = new HashMap<>();
    for (int card : cards) {
      counts.merge(card, 1, Integer::sum);
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < queries.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(counts.getOrDefault(queries[i], 0));
    }
    return sb.toString();
  }

  /**
   * Renders cards and queries as BOJ 10816 input: {@code N}, the card line, {@code M}, the query
   * line.
   */
  private static String cardQueryInput(int[] cards, int[] queries) {
    StringBuilder sb = new StringBuilder();
    sb.append(cards.length).append('\n');
    for (int i = 0; i < cards.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(cards[i]);
    }
    sb.append('\n').append(queries.length).append('\n');
    for (int i = 0; i < queries.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(queries[i]);
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
