package boj.boj10815;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 10815 숫자 카드 (Number Card) -- the textbook set-membership / binary-search exercise.
 *
 * <p>Sang-geun owns {@code N} number cards, each printed with one integer, and <em>no two cards
 * share a value</em>. Given {@code M} query integers, decide for each whether Sang-geun holds a
 * card bearing that number: print {@code 1} if so, {@code 0} otherwise.
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 500,000}); line 2 is the {@code N} distinct card
 * values, space-separated, each in {@code [-10,000,000, 10,000,000]}; line 3 is {@code M} ({@code 1
 * <= M <= 500,000}); line 4 is the {@code M} query values, space-separated, in the same range.
 * Unlike the cards, the queries are <em>not</em> guaranteed distinct -- the same number may be
 * asked repeatedly. Output: the {@code M} answers, each {@code 0} or {@code 1}, space-separated on
 * one line. (Constraints recovered from blog mirrors of the statement while acmicpc.net was
 * unreachable; the published sample {@code 6 3 2 10 -10} / {@code 10 9 -5 2 3 4 5 -10} -> {@code 1
 * 0 0 1 1 0 0 1} corroborates them.)
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>The cards must be sorted before a binary search.</b> The input order is arbitrary;
 *       {@link #descendingCardsMustBeSortedBeforeSearch} feeds them in descending order so a search
 *       that skips the sort lands on the wrong half and misprints.
 *   <li><b>A miss must report {@code 0}, not the nearest neighbor.</b> Queries that fall in a gap
 *       between two cards, below the smallest, or above the largest must collapse to {@code 0}
 *       ({@link #queryBetweenTwoCardsPrintsZero}, {@link #queriesAtAndBeyondCardRangeBoundaries}).
 *   <li><b>Per-query answers, repeats included.</b> Each of the {@code M} queries is answered
 *       independently and a repeated query repeats its answer
 *       ({@link #repeatedQueriesAreAnsweredIndependently}).
 *   <li><b>Signed values to the full bound.</b> Negative cards order correctly and
 *       {@code ±10,000,000} parse and compare without trouble ({@link #negativeNumbersAreMatched},
 *       {@link #extremeCardValuesAreHandled}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins {@code O((N + M) log N)} against an
 * {@code O(N * M)} linear rescan, and {@link #randomizedInputsMatchMembershipOracle} cross-checks
 * the judged output against an obviously-correct {@link java.util.HashSet} membership oracle. The
 * {@code Main} under test is an unfinished stub that reads the cards but never reads the queries or
 * prints anything, so every assertion here is RED until the solution is implemented.
 */
class MainTest {

  // --- The official sample from the statement: an unsorted hand with negatives, a mix of hits and
  // misses, queried out of order. The end-to-end smoke test. ---

  // cards {6,3,2,10,-10}; queries 10,9,-5,2,3,4,5,-10 -> hits at 10,2,3,-10 and misses elsewhere.
  @Test
  @StdIo({"5", "6 3 2 10 -10", "8", "10 9 -5 2 3 4 5 -10"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 0 0 1 1 0 0 1");
  }

  // --- Smallest input (N = 1, M = 1): the lone card either matches the lone query or it does not.

  // The single card 5 answers the single query 5 -> present.
  @Test
  @StdIo({"1", "5", "1", "5"})
  void singleCardSingleQueryPresentPrintsOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // The single card 5 does not bear the queried 3 -> absent.
  @Test
  @StdIo({"1", "5", "1", "3"})
  void singleCardSingleQueryAbsentPrintsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- All-hit and all-miss rows fix the two uniform extremes of the answer line. ---

  // Every query is a held card (asked in a different order than the cards are listed) -> all ones.
  @Test
  @StdIo({"3", "1 2 3", "3", "3 1 2"})
  void everyQueryPresentPrintsAllOnes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 1");
  }

  // No query is a held card -> all zeros.
  @Test
  @StdIo({"3", "1 2 3", "3", "4 5 6"})
  void noQueryPresentPrintsAllZeros(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 0");
  }

  // --- Queries may repeat even though cards are distinct: each occurrence is answered on its own.

  // 2 is held and 4 is not; asking 2 three times then 4 twice must echo each answer per occurrence.
  @Test
  @StdIo({"3", "1 2 3", "5", "2 2 2 4 4"})
  void repeatedQueriesAreAnsweredIndependently(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 1 0 0");
  }

  // --- Negative values must order and match correctly (the comparison cannot assume non-negative).

  // cards {-5,-3,-1}; queries -3,-2,-1,0 -> hits at -3 and -1, misses at -2 (a gap) and 0 (above
  // all).
  @Test
  @StdIo({"3", "-5 -3 -1", "4", "-3 -2 -1 0"})
  void negativeNumbersAreMatched(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 0 1 0");
  }

  // --- The cards arrive in descending order: a binary search that forgets to sort first searches a
  // non-ascending array and returns wrong answers. Every queried card is present, so any misprint
  // exposes the missing sort. ---

  @Test
  @StdIo({"5", "10 8 6 4 2", "5", "2 10 6 5 1"})
  void descendingCardsMustBeSortedBeforeSearch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 1 0 0");
  }

  // --- Binary-search edges: the smallest and largest cards are hits; values just below the minimum
  // and just above the maximum are misses, exercising the lo == 0 and hi == n-1 boundaries. ---

  // sorted cards {2,4,6,8,10}; queries 2 (min), 10 (max), 1 (below all), 11 (above all).
  @Test
  @StdIo({"5", "2 4 6 8 10", "4", "2 10 1 11"})
  void queriesAtAndBeyondCardRangeBoundaries(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 0 0");
  }

  // --- A query landing strictly between two adjacent cards must miss: the search narrows to an
  // empty range without ever equalling a card. ---

  // cards {1,5,10}; 3 sits in (1,5) and 7 in (5,10) -> both miss; 5 is an exact hit.
  @Test
  @StdIo({"3", "1 5 10", "3", "3 7 5"})
  void queryBetweenTwoCardsPrintsZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0 0 1");
  }

  // --- The value bound ±10,000,000 must parse and compare cleanly (no overflow, no clipping). ---

  // cards at both extremes and zero; queries hit the two extremes and zero, miss the values one
  // step inside each extreme.
  @Test
  @StdIo({"3", "-10000000 0 10000000", "5", "-10000000 10000000 9999999 -9999999 0"})
  void extremeCardValuesAreHandled(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 1 0 0 1");
  }
}
