package boj.boj14939;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
  private static int[][] lightbulbs;
  private static int[] selected;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      lightbulbs = new int[10][10];
      for (int i = 0; i < 10; i++) {
        String s = io.next();
        for (int j = 0; j < 10; j++) {
          lightbulbs[i][j] = s.charAt(j) == 'O' ? 1 : 0;
        }
      }
      selected = new int[10];
      ans = Integer.MAX_VALUE;
      backtrack(0);
      if (ans == Integer.MAX_VALUE) {
        io.println(-1);
        return;
      }
      io.println(ans);
    }
  }

  private static void backtrack(int y) {
    if (y == 10) {
      int[][] board = new int[10][10];
      for (int i = 0; i < 10; i++) {
        board[i] = lightbulbs[i].clone();
      }
      // count presses on first row
      int cnt = Arrays.stream(selected).sum();
      for (int j = 0; j < 10; j++) {
        if (selected[j] == 1) {
          press(board, 0, j);
        }
      }
      // propagate the first row
      for (int i = 1; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          if (board[i - 1][j] == 1) {
            press(board, i, j);
            cnt++;
          }
        }
      }
      // check if the last row in valid
      for (int j = 0; j < 10; j++) {
        if (board[9][j] == 1) {
          return;
        }
      }
      ans = Math.min(ans, cnt);
      return;
    }
    selected[y] = 0;
    backtrack(y + 1);
    selected[y] = 1;
    backtrack(y + 1);
  }

  private static void press(int[][] board, int x, int y) {
    board[x][y] ^= 1;
    for (int[] dir : DIRECTIONS) {
      int nx = x + dir[0];
      int ny = y + dir[1];
      if (nx >= 0 && nx < 10 && ny >= 0 && ny < 10) {
        board[nx][ny] ^= 1;
      }
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
      r = new BufferedReader(new InputStreamReader(in));
    }

    /** Next whitespace-delimited token, pulling fresh lines across boundaries as needed. */
    public String next() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        st = new StringTokenizer(r.readLine());
      }
      return st.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
      return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
      return Double.parseDouble(next());
    }
  }
}
