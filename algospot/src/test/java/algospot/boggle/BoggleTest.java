package algospot.boggle;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Algospot BOGGLE -- decide whether each queried word can be traced on a 5x5 letter board.
 *
 * <p>A trace starts on any cell and steps to one of the eight neighbors (orthogonal or diagonal) at
 * each character. Unlike the boxed game this is named after, <b>cells may be revisited freely</b>;
 * only stepping off the board is forbidden. That single difference is what makes the search a
 * memoizable {@code (cell, position)} recurrence rather than a path enumeration.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code C} ({@code C <= 50}), the number of test cases. Each
 * test case is five lines of five uppercase letters, then a line with {@code N} ({@code 1 <= N <=
 * 10}), then {@code N} query words of 1 to 10 uppercase letters. For each query the program prints
 * {@code "<word> YES"} or {@code "<word> NO"}, echoing the word, in input order.
 *
 * <p><b>TDD ladder.</b> Work down the file one rung at a time. Each rung is the smallest input that
 * fails against the minimal code satisfying every rung above it, so the first failure is always the
 * next thing to write. The staircase was verified by building those intermediate implementations
 * and observing each rung break exactly the one below it.
 *
 * <ol>
 *   <li>one letter that is on the board &rarr; the {@code "<word> YES"} echo format
 *   <li>one letter that is not &rarr; the answer must depend on the board
 *   <li>two letters, both present but not adjacent &rarr; look past the first character
 *   <li>two letters a diagonal apart &rarr; all eight directions, not just four
 *   <li>a word that reuses a cell &rarr; no visited set; this is not the boxed game
 *   <li>two words in one case &rarr; the loop over {@code N}
 *   <li>two cases &rarr; the loop over {@code C}, and with it re-reading the board
 *   <li>ten near-miss words on a single-letter board &rarr; memoization
 * </ol>
 *
 * <p>Rung 8 is the one the problem statement itself warns about: the plain recursion of rungs 1-7
 * is correct but takes seconds per word, and at the stated maximum of 50 cases x 10 words that is
 * tens of minutes against a 10-second limit.
 */
class BoggleTest {

  // The board every small rung below traces on, laid out with coordinates for the hand-checking:
  //
  //        x=0 1 2 3 4
  //   y=0   U R L P M
  //   y=1   X P R E T
  //   y=2   G I A E T
  //   y=3   X T N Z Y
  //   y=4   X O Q R S
  //
  // U appears exactly once, at (0,0), which is what makes the single-start rungs below unambiguous.

  // RUNG 1 -- forces: printing the queried word back, followed by a verdict.
  // Fails on: an empty main. U sits at (0,0), so the shortest possible query is answerable by
  // looking at the board once. The echo, the space, and the word YES are all pinned here.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "U"})
  void singleLetterOnTheBoardIsFound(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("U YES");
  }

  // RUNG 2 -- forces: the verdict to depend on the board.
  // Fails on: rung-1 code that always answers YES. B appears nowhere on the board, so the only way
  // to get this right is to actually look for the letter.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "B"})
  void singleLetterMissingFromTheBoardIsNotFound(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("B NO");
  }

  // RUNG 3 -- forces: looking past the first character, i.e. an actual trace.
  // Fails on: rung-2 code that only checks whether the word's first letter is somewhere on the
  // board. Both U (0,0) and Z (3,3) are present, but they are nowhere near each other, so no pen
  // stroke spells UZ. This is also the rung that forces the off-board bounds check, since tracing
  // from the corner (0,0) immediately probes negative coordinates.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "UZ"})
  void twoPresentLettersThatAreNotNeighborsSpellNothing(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("UZ NO");
  }

  // RUNG 4 -- forces: diagonal steps, i.e. all eight neighbors rather than four.
  // Fails on: rung-3 code that steps only up, down, left and right. The lone U is at (0,0) and its
  // orthogonal neighbors are R (0,1) and X (1,0); the P that completes the word is at (1,1),
  // reachable only on the diagonal.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "UP"})
  void diagonalStepIsALegalMove(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("UP YES");
  }

  // RUNG 5 -- forces: dropping any visited set. Cells may be reused.
  // Fails on: rung-4 code written with the boxed game's rule in mind, marking each cell used. URU
  // is spelled (0,0) -> (0,1) -> back to (0,0), and since U occurs only once on the board there is
  // no alternative route. This is the trap the problem sets for anyone who knows real Boggle.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "URU"})
  void aCellMayBeUsedTwiceInOneWord(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("URU YES");
  }

  // RUNG 6 -- forces: the loop over the N queries of a test case.
  // Fails on: rung-5 code that answers the first word and stops, printing one line here. The two
  // verdicts differ so a solver cannot pass by echoing both words with one shared answer.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "2", "PRETTY", "KARA"})
  void everyQueriedWordGetsItsOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("PRETTY YES", "KARA NO");
  }

  // RUNG 7 -- forces: the loop over C, and with it re-reading the board for each test case.
  // Fails on: rung-6 code that solves one case and stops. The same word is asked twice against
  // different boards, so this also pins the board being refilled per case: the solution keeps it
  // in a static field, and a version that loaded it once would answer YES twice instead of YES
  // then NO.
  @Test
  @StdIo({
    "2", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "PRETTY", "AAAAA", "AAAAA", "AAAAA",
    "AAAAA", "AAAAA", "1", "PRETTY"
  })
  void eachTestCaseIsAnsweredAgainstItsOwnBoard(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("PRETTY YES", "PRETTY NO");
  }

  // RUNG 8 -- forces: memoizing on (cell, position in the word).
  // Fails on: rung-7 code, the plain recursion, which does not finish. Every cell is an A, so every
  // one of the eight steps matches at every position and the search fans out 8^9 ways from each of
  // 25 starting cells -- for each word. Measured at roughly four seconds per word, so these ten
  // words take the better part of a minute; the judge allows ten seconds for input up to 50 cases
  // of 10 words. Memoized there are only 25 x 10 states per word and it returns instantly.
  //
  // The statement flags this directly: the chapter 6 example code is too slow for this problem.
  @Test
  @Timeout(value = 20, unit = TimeUnit.SECONDS)
  @StdIo({
    "1",
    "AAAAA",
    "AAAAA",
    "AAAAA",
    "AAAAA",
    "AAAAA",
    "10",
    "AAAAAAAAAB",
    "AAAAAAAAAC",
    "AAAAAAAAAD",
    "AAAAAAAAAE",
    "AAAAAAAAAF",
    "AAAAAAAAAG",
    "AAAAAAAAAH",
    "AAAAAAAAAI",
    "AAAAAAAAAJ",
    "AAAAAAAAAK"
  })
  void nearMissWordsOnAUniformBoardMustNotBeSearchedPathByPath(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly(
            "AAAAAAAAAB NO",
            "AAAAAAAAAC NO",
            "AAAAAAAAAD NO",
            "AAAAAAAAAE NO",
            "AAAAAAAAAF NO",
            "AAAAAAAAAG NO",
            "AAAAAAAAAH NO",
            "AAAAAAAAAI NO",
            "AAAAAAAAAJ NO",
            "AAAAAAAAAK NO");
  }

  // GUARD -- the judge's official sample, verbatim from problems/BOGGLE/samples/sample-01.
  // Not a rung: any implementation clearing rung 7 clears this too. Its six words happen to cover
  // the interesting shapes -- PRETTY and GIRL trace plainly, REPEAT bends back on itself, KARA and
  // PANDORA are absent, and GIAZAPX reuses the A at (2,2) -- which is why the ladder above could be
  // built by pulling those shapes apart into one rung each.
  @Test
  @StdIo({
    "1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "6", "PRETTY", "GIRL", "REPEAT", "KARA",
    "PANDORA", "GIAZAPX"
  })
  void officialSampleIsReproducedExactly(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly(
            "PRETTY YES", "GIRL YES", "REPEAT YES", "KARA NO", "PANDORA NO", "GIAZAPX YES");
  }

  // GUARD -- the longest legal word, 10 characters, answered YES.
  // Not a rung: nothing in the recursion is length-sensitive once rung 5 removed the visited set.
  // It exists because the memo table added at rung 8 is indexed by position in the word, so an
  // implementation that sizes that dimension at 10 instead of the needed length would overflow
  // exactly here, at the boundary the statement allows.
  @Test
  @StdIo({"1", "URLPM", "XPRET", "GIAET", "XTNZY", "XOQRS", "1", "URURURURUR"})
  void wordOfMaximumLengthIsHandled(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("URURURURUR YES");
  }
}
