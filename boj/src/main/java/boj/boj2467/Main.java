package boj.boj2467;

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

      int lo = 0;
      int hi = n - 1;
      int[] ans = {a[lo] + a[hi], lo, hi};
      while (lo < hi) {
        int sum = a[lo] + a[hi];
        if (Math.abs(sum) < Math.abs(ans[0])) {
          ans[0] = sum;
          ans[1] = lo;
          ans[2] = hi;
        }
        if (sum == 0) {
          break;
        }
        if (sum < 0) {
          lo++;
        } else {
          hi--;
        }
      }

      pw.println(a[ans[1]] + " " + a[ans[2]]);
    }
  }
}
