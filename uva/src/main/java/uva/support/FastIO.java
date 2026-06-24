package uva.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * FastIO -- a fast-I/O helper modeled on the USACO Guide's Kattio template
 * (https://usaco.guide/general/input-output): a {@link PrintWriter} subclass that also reads
 * whitespace-delimited tokens from a buffered stream. One object is both scanner and writer: call
 * {@link #nextInt()} and friends to read, {@code println}/{@code printf} (inherited from
 * {@link PrintWriter}) to write, and {@link #close()} once at the end to flush the buffered output.
 *
 * <p>Adapted from the canonical version in two ways for this repo: the {@code next*} methods
 * propagate {@link IOException} instead of swallowing it, matching the {@code main(...) throws
 * IOException} convention used across these solutions; and the USACO-style file-input constructor
 * is dropped because OJ is purely stdin/stdout.
 *
 * <p>This is the shared, in-repo source of truth, imported by solutions so it can be tested once.
 * For an actual single-file BOJ submission, inline this class beside {@code Main} -- the judge
 * compiles a single pasted file and cannot see {@code boj.support}.
 */
public class FastIO extends PrintWriter {
  private final BufferedReader r;
  private StringTokenizer st;

  public FastIO() {
    this(System.in, System.out);
  }

  public FastIO(InputStream in, OutputStream out) {
    super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
    r = new BufferedReader(new InputStreamReader(in));
  }

  /**
   * Whether another token remains, pulling fresh lines as needed; {@code false} at end of input.
   */
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
