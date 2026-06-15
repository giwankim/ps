package boj.boj1759;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int l;
  private static int c;
  private static char[] alphabet;
  private static char[] selected;
  private static StringBuilder result;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      l = io.nextInt();
      c = io.nextInt();
      alphabet = new char[c];
      for (int i = 0; i < c; i++) {
        alphabet[i] = io.next().charAt(0);
      }
      selected = new char[l];
      result = new StringBuilder();
      Arrays.sort(alphabet);
      backtrack(0, 0);
      io.print(result);
    }
  }

  private static void backtrack(int index, int start) {
    if (index == l) {
      int vowels = 0;
      int consonants = 0;
      for (char c : selected) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
          vowels++;
        } else {
          consonants++;
        }
      }
      if (vowels < 1 || consonants < 2) {
        return;
      }
      for (int i = 0; i < l; i++) {
        result.append(selected[i]);
      }
      result.append('\n');
      return;
    }
    for (int i = start; i < c; i++) {
      selected[index] = alphabet[i];
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
