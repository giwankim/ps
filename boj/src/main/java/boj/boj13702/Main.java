package boj.boj13702;

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
      int k = Integer.parseInt(st.nextToken());
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = Integer.parseInt(r.readLine());
      }

      long ans = 0;
      long lo = 1;
      long hi = Arrays.stream(a).max().orElse(0);
      while (lo <= hi) {
        long mid = lo + (hi - lo) / 2;
        if (count(a, mid) >= k) {
          ans = mid;
          lo = mid + 1;
        } else {
          hi = mid - 1;
        }
      }
      pw.println(ans);
    }
  }

  private static long count(int[] a, long x) {
    long result = 0;
    for (int num : a) {
      result += num / x;
    }
    return result;
  }
}
