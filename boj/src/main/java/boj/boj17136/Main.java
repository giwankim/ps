package boj.boj17136;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int[][] board = new int[10][10];
  private static boolean[][] visited;
  private static int[] used;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          board[i][j] = io.nextInt();
        }
      }
      visited = new boolean[10][10];
      used = new int[6];
      ans = -1;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int x, int y) {
    if (y == 10) {
      x++;
      y = 0;
    }
    if (x == 10) {
      int total = 0;
      for (int i = 1; i <= 5; i++) {
        total += used[i];
      }
      if (ans == -1 || total < ans) {
        ans = total;
      }
      return;
    }
    if (visited[x][y] || board[x][y] == 0) {
      backtrack(x, y + 1);
      return;
    }
    for (int sz = 1; sz <= 5; sz++) {
      if (x + sz > 10 || y + sz > 10) {
        break;
      }
      if (used[sz] >= 5 || !canCover(x, y, sz)) {
        continue;
      }
      used[sz]++;
      set(x, y, sz, true);
      backtrack(x, y + 1);
      used[sz]--;
      set(x, y, sz, false);
    }
  }

  private static void set(int x, int y, int sz, boolean val) {
    for (int i = 0; i < sz; i++) {
      for (int j = 0; j < sz; j++) {
        visited[x + i][y + j] = val;
      }
    }
  }

  private static boolean canCover(int x, int y, int sz) {
    for (int i = 0; i < sz; i++) {
      int nx = x + i;
      for (int j = 0; j < sz; j++) {
        int ny = y + j;
        if (board[nx][ny] == 0 || visited[nx][ny]) {
          return false;
        }
      }
    }
    return true;
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
