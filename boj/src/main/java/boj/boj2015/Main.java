package boj.boj2015;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      StringTokenizer st = new StringTokenizer(r.readLine());
      int n = Integer.parseInt(st.nextToken());
      int k = Integer.parseInt(st.nextToken());
      st = new StringTokenizer(r.readLine());
      int[] arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = Integer.parseInt(st.nextToken());
      }

      long[] P = new long[n + 1];
      for (int i = 1; i <= n; i++) {
        P[i] = P[i - 1] + arr[i - 1];
      }

      long ans = 0;
      Map<Long, Long> prefixCounts = new HashMap<>();
      prefixCounts.put(0L, 1L);
      for (int i = 1; i <= n; i++) {
        if (prefixCounts.containsKey(P[i] - k)) {
          ans += prefixCounts.get(P[i] - k);
        }
        prefixCounts.merge(P[i], 1L, Long::sum);
      }
      pw.println(ans);
    }
  }
}
