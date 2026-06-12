package boj.boj2613;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static final int MAX_SUM = 30_000;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int m = io.nextInt();
      int[] marbles = new int[n];
      for (int i = 0; i < n; i++) {
        marbles[i] = io.nextInt();
      }

      int minMax = Integer.MAX_VALUE;
      int lo = Arrays.stream(marbles).max().orElseThrow();
      int hi = MAX_SUM;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (isFeasible(marbles, m, mid)) {
          minMax = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }

      StringBuilder result = new StringBuilder();
      result.append(minMax).append('\n');
      List<Integer> groups = getGroups(marbles, m, minMax);
      for (int group : groups) {
        result.append(group).append(' ');
      }
      io.println(result);
    }
  }

  private static boolean isFeasible(int[] marbles, int m, int maxSum) {
    int curr = 0;
    int groupCnt = 1;
    for (int marble : marbles) {
      curr += marble;
      if (curr > maxSum) {
        curr = marble;
        groupCnt += 1;
      }
    }
    return groupCnt <= m;
  }

  private static List<Integer> getGroups(int[] marbles, int m, int maxSum) {
    List<Integer> result = new ArrayList<>();
    int n = marbles.length;
    int marblesLeft = n;
    int groupsLeft = m;
    int curr = 0;
    int cnt = 0;
    int i = 0;
    while (marblesLeft != groupsLeft - 1) {
      curr += marbles[i];
      cnt++;
      marblesLeft--;
      if (curr > maxSum) {
        result.add(cnt - 1);
        groupsLeft--;
        curr = marbles[i];
        cnt = 1;
      }
      i++;
    }

    // add current cnt to results
    result.add(cnt);

    // remaining groups are size 1
    while (result.size() < m) {
      result.add(1);
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
