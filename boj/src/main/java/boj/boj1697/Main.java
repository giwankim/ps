package boj.boj1697;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  private static int n, k;
  private static int[] dist = new int[200_001];

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      k = io.nextInt();

      Arrays.fill(dist, Integer.MAX_VALUE);
      Queue<Integer> queue = new ArrayDeque<>();
      queue.offer(n);
      dist[n] = 0;
      while (!queue.isEmpty()) {
        int pos = queue.poll();
        if (pos == k) {
          break;
        }
        for (int next : new int[] {pos - 1, pos + 1, 2 * pos}) {
          if (next < 0 || next >= 200_001 || dist[next] != Integer.MAX_VALUE) {
            continue;
          }
          queue.offer(next);
          dist[next] = dist[pos] + 1;
        }
      }
      io.println(dist[k]);
    }
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
