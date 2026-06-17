package boj.boj1553;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}};
  private static int[][] board = new int[8][7];
  private static boolean[][] visited;
  private static boolean[][] used;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      for (int i = 0; i < 8; i++) {
        String row = io.next();
        for (int j = 0; j < 7; j++) {
          board[i][j] = row.charAt(j) - '0';
        }
      }
      visited = new boolean[8][7];
      used = new boolean[7][7];
      ans = 0;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int x, int y) {
    if (y == 7) {
      x += 1;
      y = 0;
    }
    if (x == 8) {
      ans++;
      return;
    }
    if (visited[x][y]) {
      backtrack(x, y + 1);
      return;
    }
    for (int[] dir : DIRECTIONS) {
      int nx = x + dir[0];
      int ny = y + dir[1];
      if (nx < 0 || nx >= 8 || ny < 0 || ny >= 7 || visited[nx][ny]) {
        continue;
      }
      int a = board[x][y];
      int b = board[nx][ny];
      if (used[a][b]) {
        continue;
      }
      visited[x][y] = true;
      visited[nx][ny] = true;
      used[a][b] = true;
      used[b][a] = true;
      backtrack(x, y + 1);
      visited[x][y] = false;
      visited[nx][ny] = false;
      used[a][b] = false;
      used[b][a] = false;
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
