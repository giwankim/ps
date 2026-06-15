package boj.boj15686;

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
  private static int n;
  private static int m;
  private static int[][] city;
  private static List<int[]> houses;
  private static List<int[]> chickens;
  private static int[] selected;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      city = new int[n][n];
      chickens = new ArrayList<>();
      houses = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          city[i][j] = io.nextInt();
          if (city[i][j] == 1) {
            houses.add(new int[] {i, j});
          } else if (city[i][j] == 2) {
            chickens.add(new int[] {i, j});
          }
        }
      }
      selected = new int[m];
      ans = Integer.MAX_VALUE;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int index, int start) {
    if (index == m) {
      // sum of min distance from each house to one of the selected chicken stores
      // min with ans
      int sum = 0;
      for (int[] house : houses) {
        int minDist = Integer.MAX_VALUE;
        for (int i : selected) {
          int[] chicken = chickens.get(i);
          minDist =
              Math.min(minDist, Math.abs(house[0] - chicken[0]) + Math.abs(house[1] - chicken[1]));
        }
        sum += minDist;
      }
      ans = Math.min(ans, sum);
      return;
    }
    for (int i = start; i < chickens.size(); i++) {
      selected[index] = i;
      backtrack(index + 1, i + 1);
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
