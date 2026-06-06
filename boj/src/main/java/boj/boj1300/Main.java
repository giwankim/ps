package boj.boj1300;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      int k = Integer.parseInt(r.readLine());
      long ans = -1;
      long lo = 1;
      long hi = (long) n * n;
      while (lo <= hi) {
        long mid = lo + (hi - lo) / 2;
        long cnt = count(n, mid);
        if (cnt >= k) {
          ans = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }
      pw.println(ans);
    }
  }

  private static long count(int n, long x) {
    long result = 0;
    for (int i = 1; i <= n; i++) {
      result += Math.min(x / i, n);
    }
    return result;
  }
}
