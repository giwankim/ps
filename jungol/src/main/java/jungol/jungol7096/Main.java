package jungol.jungol7096;

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
  private static int[] parent;
  private static List<Integer> ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      parent = new int[101];
      Arrays.fill(parent, -1);

      int k = io.nextInt();
      int a;
      while ((a = io.nextInt()) != -1) {
        int n = io.nextInt();
        for (int i = 0; i < n; i++) {
          int b = io.nextInt();
          parent[b] = a;
        }
      }

      ans = new ArrayList<>();
      dfs(k);
      for (int i = 0; i < ans.size(); i++) {
        if (i == ans.size() - 1) {
          io.println(ans.get(i));
        } else {
          io.print(ans.get(i) + " ");
        }
      }
    }
  }

  private static void dfs(int k) {
    if (parent[k] == -1) {
      ans.add(k);
      return;
    }
    ans.add(k);
    dfs(parent[k]);
  }

  public static class FastIO extends PrintWriter {
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
