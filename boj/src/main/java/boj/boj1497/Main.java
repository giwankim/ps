package boj.boj1497;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int m;
  private static long[] play;
  private static int maxSongs;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      play = new long[n];
      for (int i = 0; i < n; i++) {
        io.next();
        String s = io.next();
        for (int j = 0; j < m; j++) {
          play[i] |= s.charAt(j) == 'Y' ? 1 : 0;
          play[i] <<= 1;
        }
      }
      maxSongs = 0;
      ans = -1;
      backtrack(0, 0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int index, long mask, int guitars) {
    if (index == n) {
      int songs = Long.bitCount(mask);
      if (songs == 0) {
        return;
      }
      if (songs >= maxSongs) {
        maxSongs = songs;
        if (ans == -1 || guitars < ans) {
          ans = guitars;
        }
      }
      return;
    }
    backtrack(index + 1, mask | play[index], guitars + 1);
    backtrack(index + 1, mask, guitars);
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
