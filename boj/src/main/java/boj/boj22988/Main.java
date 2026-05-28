package boj.boj22988;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      long x = Long.parseLong(st.nextToken());
      long[] c = new long[n];
      st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        c[i] = Long.parseLong(st.nextToken());
      }

      // count already full bottles
      int full = 0;
      for (int i = 0; i < n; i++) {
        if (c[i] >= x) {
          full++;
        }
      }

      // count pairs using two pointers
      Arrays.sort(c);
      int pairs = 0;
      int lo = 0;
      int hi = n - 1 - full;
      while (lo < hi) {
        if (2 * (c[lo] + c[hi]) >= x) {
          pairs++;
          lo++;
          hi--;
        } else {
          lo++;
        }
      }

      int leftover = n - full - 2 * pairs;
      int ans = full + pairs + leftover / 3;
      pw.println(ans);
    }
  }
}
