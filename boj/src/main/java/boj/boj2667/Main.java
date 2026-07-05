package boj.boj2667;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  private static int n;
  private static char[][] houses;
  private static boolean[][] visited;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      houses = new char[n][n];
      for (int i = 0; i < n; i++) {
        houses[i] = io.next().toCharArray();
      }
      visited = new boolean[n][n];

      List<Integer> ans = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          if (houses[i][j] == '1' && !visited[i][j]) {
            ans.add(dfs(i, j));
          }
        }
      }
      io.println(ans.size());
      ans.sort(Integer::compareTo);
      for (int count : ans) {
        io.println(count);
      }
    }
  }

  private static int dfs(int x, int y) {
    visited[x][y] = true;
    int result = 1;
    for (int[] dir : DIRS) {
      int nx = x + dir[0];
      int ny = y + dir[1];
      if (nx < 0 || nx >= n || ny < 0 || ny >= n || houses[nx][ny] == '0' || visited[nx][ny]) {
        continue;
      }
      result += dfs(nx, ny);
    }
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
