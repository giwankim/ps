package boj.boj22945;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int[] a = new int[n];
      StringTokenizer st = new StringTokenizer(r.readLine());
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(st.nextToken());
      }

      long ans = 0;
      int lo = 0;
      int hi = n - 1;
      while (lo < hi) {
        ans = Math.max(ans, (long) (hi - lo - 1) * Math.min(a[lo], a[hi]));
        if (a[lo] < a[hi]) {
          lo++;
        } else {
          hi--;
        }
      }
      pw.println(ans);
    }
  }
}
