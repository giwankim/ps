package algospot.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Algospot BOGGLE -- trace each queried word on a 5x5 letter board, stepping to any of the eight
 * neighbors and reusing cells as often as needed.
 *
 * <p>Because cells may be revisited, whether a word can be spelled from a given cell depends only
 * on that cell and how far into the word we are -- never on the route taken to get there. That
 * makes {@code (y, x, i)} a memoizable state. Without the memo the recursion branches eight ways
 * per character, which the problem statement singles out as too slow: on a board of one repeated
 * letter a single 10-character query explores on the order of 8^9 traces from each of 25 starting
 * cells.
 *
 * @implNote {@code O(B^2 * L)} time and {@code O(B^2 * L)} space per query, where {@code B} is the
 *     board side (5) and {@code L} the word length (at most 10): each of the {@code B^2 * L} states
 *     is solved once and inspects eight neighbors.
 */
public class Main {
  private static final int SIZE = 5;
  private static final int MAX_LEN = 10;
  private static final int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};
  private static final int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};

  private static char[][] board = new char[SIZE][SIZE];
  private static int[][][] cache = new int[SIZE][SIZE][MAX_LEN];
  private static String word;

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter pw = new PrintWriter(System.out);

    int C = Integer.parseInt(br.readLine());
    while (C-- > 0) {
      for (int i = 0; i < SIZE; i++) {
        board[i] = br.readLine().toCharArray();
      }

      int N = Integer.parseInt(br.readLine());
      while (N-- > 0) {
        word = br.readLine();
        for (int[][] plane : cache) {
          for (int[] row : plane) {
            Arrays.fill(row, -1);
          }
        }
        pw.println(word + (hasWord() ? " YES" : " NO"));
      }
    }

    br.close();
    pw.close();
  }

  private static boolean hasWord() {
    for (int y = 0; y < SIZE; y++) {
      for (int x = 0; x < SIZE; x++) {
        if (hasWord(y, x, 0) == 1) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns 1 if {@code word} from index {@code i} onward can be traced starting at {@code (y, x)}.
   */
  private static int hasWord(int y, int x, int i) {
    if (y < 0 || y >= SIZE || x < 0 || x >= SIZE) {
      return 0;
    }
    if (board[y][x] != word.charAt(i)) {
      return 0;
    }
    if (i == word.length() - 1) {
      return 1;
    }
    if (cache[y][x][i] != -1) {
      return cache[y][x][i];
    }
    int result = 0;
    for (int d = 0; d < 8 && result == 0; d++) {
      if (hasWord(y + dy[d], x + dx[d], i + 1) == 1) {
        result = 1;
      }
    }
    return cache[y][x][i] = result;
  }
}
