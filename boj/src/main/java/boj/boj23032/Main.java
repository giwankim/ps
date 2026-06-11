package boj.boj23032;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int[] steaks = new int[n];
      for (int i = 0; i < n; i++) {
        steaks[i] = io.nextInt();
      }
      //      io.println(twoPointer(steaks));
      io.println(binarySearch(steaks));
    }
  }

  private static int binarySearch(int[] steaks) {
    int n = steaks.length;
    int[] prefix = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      prefix[i] = prefix[i - 1] + steaks[i - 1];
    }

    int result = -1;
    int diff = Integer.MAX_VALUE;
    for (int s = 0; s < n; s++) {
      for (int e = s + 1; e < n; e++) {
        int m = lowerBound(prefix, s, e);
        int L = prefix[m] - prefix[s];
        int R = prefix[e + 1] - prefix[m];
        if (Math.abs(L - R) < diff) {
          diff = Math.abs(L - R);
          result = L + R;
        } else if (Math.abs(L - R) == diff) {
          result = Math.max(result, L + R);
        }
        if (m - 2 >= s) {
          L = prefix[m - 1] - prefix[s];
          R = prefix[e + 1] - prefix[m - 1];
          if (Math.abs(L - R) < diff) {
            diff = Math.abs(L - R);
            result = L + R;
          } else if (Math.abs(L - R) == diff) {
            result = Math.max(result, L + R);
          }
        }
      }
    }
    return result;
  }

  private static int lowerBound(int[] prefix, int s, int e) {
    int result = e;
    int lo = s + 1;
    int hi = e;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (2 * prefix[mid] - prefix[s] - prefix[e + 1] >= 0) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }

  private static int twoPointer(int[] steaks) {
    int result = 0;
    int n = steaks.length;
    int diff = Integer.MAX_VALUE;
    for (int mid = 0; mid < n - 1; mid++) {
      // expand s and e
      // w[s:mid] vs w[mid+1:e]
      int s = mid;
      int e = mid + 1;
      int L = steaks[mid];
      int R = steaks[mid + 1];
      while (s >= 0 && e < n) {
        if (Math.abs(L - R) < diff) {
          diff = Math.abs(L - R);
          result = L + R;
        } else if (Math.abs(L - R) == diff) {
          result = Math.max(result, L + R);
        }
        if (L < R) {
          if (s-- == 0) {
            break;
          }
          L += steaks[s];
        } else {
          if (e++ == n - 1) {
            break;
          }
          R += steaks[e];
        }
      }
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
