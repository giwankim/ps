package atcoder.abc474.c;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 474 C -- Remove and Append.
 *
 * <p>Line 1 holds N and Q (1 ≤ N, Q ≤ 2 x 10^5); line 2 holds a permutation P_1 ... P_N of 1 ... N;
 * each of the next Q lines holds a value a_q. In order, each query removes the element whose value
 * is a_q from P and appends it to the end. Print the final P on one line, space separated.
 *
 * <p>The final order has a closed form: the elements no query names stay in front, in their
 * original order, and behind them come the named elements sorted by the time of their last move.
 * Each part of that has a guard. A move is by value, not by position
 * ({@link #theQueryNamesAValueNotAPosition}), and it slides the elements behind the mover forward
 * rather than swapping it with the last one ({@link #theMovedElementGoesToTheVeryEnd}). The
 * untouched prefix keeps its own order rather than any sorted one
 * ({@link #untouchedElementsKeepTheirOriginalOrderInFront}), and a repeated move counts from its
 * last occurrence, not its first ({@link #movedElementsFollowTheOrderOfTheirLastMove}, with
 * official samples two and three repeating single elements).
 *
 * <p>The two largest cases set N = Q = 2 x 10^5. Their inputs run past the 64 KiB a class-file
 * string constant may hold, so they are built as Java strings and driven through
 * {@link #runMain(String)}, and each is capped at thirty seconds: a linear pass finishes well
 * inside a second, while removing from the middle of a list Q times does not.
 */
class MainTest {

  /** N and Q at their ceiling. */
  private static final int MAX = 200_000;

  // --- Official samples. ---

  @Test
  @StdIo({"4 2", "2 4 3 1", "3", "2"})
  void officialSampleOneMovesThreeThenTwo(StdOut out) throws IOException {
    // (2, 4, 3, 1) -> (2, 4, 1, 3) -> (4, 1, 3, 2).
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 1 3 2");
  }

  @Test
  @StdIo({"3 3", "1 2 3", "1", "1", "1"})
  void officialSampleTwoMovesTheSameElementThreeTimes(StdOut out) throws IOException {
    // Only the first move changes anything; the next two find 1 already at the end.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 3 1");
  }

  @Test
  @StdIo({"2 5", "2 1", "1", "1", "2", "2", "1"})
  void officialSampleThreeShufflesTwoElementsFiveTimes(StdOut out) throws IOException {
    // Q above N. The last moves are 2 at query four and 1 at query five, so 2 leads.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 1");
  }

  // --- One move: the element is found by value and slid to the end. ---

  @Test
  @StdIo({"4 1", "1 2 3 4", "2"})
  void theMovedElementGoesToTheVeryEnd(StdOut out) throws IOException {
    // 3 and 4 close ranks ahead of the moved 2. Swapping 2 with the last element instead prints
    // 1 4 3 2.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1 3 4 2");
  }

  @Test
  @StdIo({"5 1", "5 3 1 4 2", "1"})
  void theQueryNamesAValueNotAPosition(StdOut out) throws IOException {
    // The value 1 sits in position 3. Reading a_q as a position moves the 5 instead and prints
    // 3 1 4 2 5.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 3 4 2 1");
  }

  @Test
  @StdIo({"3 1", "3 1 2", "2"})
  void movingTheLastElementChangesNothing(StdOut out) throws IOException {
    // Removing the last element and appending it puts it straight back.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 1 2");
  }

  // --- Many moves: untouched elements first, then the movers by their last move. ---

  @Test
  @StdIo({"5 2", "4 2 5 1 3", "2", "4"})
  void untouchedElementsKeepTheirOriginalOrderInFront(StdOut out) throws IOException {
    // 5, 1, 3 are never named and stay in that order, not sorted; then 2, then 4. Sorting the
    // untouched prefix prints 1 3 5 2 4.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 1 3 2 4");
  }

  @Test
  @StdIo({"3 3", "1 2 3", "1", "2", "1"})
  void movedElementsFollowTheOrderOfTheirLastMove(StdOut out) throws IOException {
    // 1 moves first and last; its second move puts it behind 2. Ordering by first move prints
    // 3 1 2.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 2 1");
  }

  @Test
  @StdIo({"4 4", "1 2 3 4", "4", "3", "2", "1"})
  void movingEveryElementOnceLeavesThemInMoveOrder(StdOut out) throws IOException {
    // Once every element has moved, the original order plays no part: the identity comes out
    // reversed.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4 3 2 1");
  }

  // --- N and Q at their floor and ceiling. ---

  @Test
  @StdIo({"1 1", "1", "1"})
  void aSingleElementStaysPut(StdOut out) throws IOException {
    // N = Q = 1: the only element is moved onto itself.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLargestCaseMovesEveryElementOnceInReverse() throws IOException {
    // Query q names N + 1 - q, so every element moves exactly once and the identity comes out
    // reversed. Nothing is untouched, and every move after the first pulls from just ahead of the
    // block already moved.
    int[] expected = new int[MAX];
    for (int i = 0; i < MAX; i++) {
      expected[i] = MAX - i;
    }
    assertOutputIs(identityWithQueries(MAX, MAX, q -> MAX + 1 - q), expected);
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLargestCaseMovesTheSameElementEveryTime() throws IOException {
    // All 2 x 10^5 queries name 1. Only the first changes anything, but every one must still be
    // read: 2 ... N stay in front, in order, with 1 behind them.
    int[] expected = new int[MAX];
    for (int i = 0; i < MAX - 1; i++) {
      expected[i] = i + 2;
    }
    expected[MAX - 1] = 1;
    assertOutputIs(identityWithQueries(MAX, MAX, q -> 1), expected);
  }

  /**
   * An input whose P is the identity 1 ... n and whose q queries name {@code query(1)} through
   * {@code query(q)}, each on its own line.
   */
  private static String identityWithQueries(int n, int q, IntUnaryOperator query) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append(' ').append(q).append('\n');
    for (int i = 1; i <= n; i++) {
      sb.append(i).append(i < n ? ' ' : '\n');
    }
    for (int i = 1; i <= q; i++) {
      sb.append(query.applyAsInt(i)).append('\n');
    }
    return sb.toString();
  }

  /**
   * Runs {@code Main} on {@code input} and holds each value of its output line against
   * {@code expected}, position by position, so that a mismatch names the first wrong P_i rather
   * than dumping two lines of 2 x 10^5 numbers.
   */
  private static void assertOutputIs(String input, int[] expected) throws IOException {
    String line = runMain(input).trim();
    String[] tokens = line.isEmpty() ? new String[0] : line.split(" ");
    assertThat(tokens).as("number of values printed").hasSize(expected.length);
    for (int i = 0; i < expected.length; i++) {
      assertThat(tokens[i]).as("P_%d", i + 1).isEqualTo(String.valueOf(expected[i]));
    }
  }

  /** Runs {@code Main} with {@code input} on standard input and returns what it printed. */
  private static String runMain(String input) throws IOException {
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
