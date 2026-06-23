package boj.boj23880;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}};

  private static int n, k;
  private static char[][] farm;
  private static int[][][][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int t = io.nextInt();
      while (t-- > 0) {
        n = io.nextInt();
        k = io.nextInt();
        farm = new char[n][n];
        for (int i = 0; i < n; i++) {
          farm[i] = io.next().toCharArray();
        }

        dp = new int[n][n][k + 1][2];
        for (int[][][] cube : dp) {
          for (int[][] plane : cube) {
            for (int[] row : plane) {
              Arrays.fill(row, -1);
            }
          }
        }

        int ans = farm[1][0] == '.' ? walk(1, 0, 0, 0) : 0;
        ans += farm[0][1] == '.' ? walk(0, 1, 0, 1) : 0;
        io.println(ans);
      }
    }
  }

  private static int walk(int x, int y, int turns, int dir) {
    if (x < 0 || x >= n || y < 0 || y >= n || farm[x][y] == 'H') {
      return 0;
    }
    if (x == n - 1 && y == n - 1) {
      return 1;
    }
    if (dp[x][y][turns][dir] != -1) {
      return dp[x][y][turns][dir];
    }
    int nx = x + DIRECTIONS[dir][0];
    int ny = y + DIRECTIONS[dir][1];
    int result = walk(nx, ny, turns, dir);
    if (turns < k) {
      int nDir = (dir + 1) % 2;
      nx = x + DIRECTIONS[nDir][0];
      ny = y + DIRECTIONS[nDir][1];
      result += walk(nx, ny, turns + 1, nDir);
    }
    dp[x][y][turns][dir] = result;
    return result;
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out);
      r = new BufferedReader(new InputStreamReader(in));
    }

    public boolean hasNext() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        String line = r.readLine();
        if (line == null) {
          return false;
        }
        st = new StringTokenizer(line);
      }
      return true;
    }

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
